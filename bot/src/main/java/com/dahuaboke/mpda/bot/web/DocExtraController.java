package com.dahuaboke.mpda.bot.web;

import com.dahuaboke.mpda.bot.rag.utils.FundDocUtil;
import com.dahuaboke.mpda.bot.tools.entity.DocEntity;
import com.dahuaboke.mpda.core.rag.entity.FundFieldMapper;
import com.dahuaboke.mpda.core.utils.List2MdUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
public class DocExtraController {

    private static final Logger log = LoggerFactory.getLogger(DocExtraController.class);

    @Autowired
    private ChatClient chatClient;

    private static final PromptTemplate promptTemplate = new PromptTemplate("""
                 请严格根据用户查询问题，从提供的参考内容中提取最精确的答案：
                        
             **用户查询（核心焦点，必须优先匹配）** \s
             "{query}"
                        
             **参考内容（仅作为检索来源）** \s
             {question_answer_context}
                        
             **硬性要求** \s
             1. **答案必须100%来自参考内容**，禁止任何形式的编造、推断或外部知识 \s
             2. **匹配逻辑**： \s
                - 先筛选参考内容中所有与用户查询直接相关的片段 \s
                - 选择其中语义匹配度最高的一段作为最终答案 \s
                - 输出结果只要答案即可，切记不要包含用户问题 \s
                - 根据最终答案和用户查询问题,分析结果直接输出原文内容 \s
                        
             3. 根据最终答案,分析结果和整理格式 ,并将最终结果放在『RESULT』和『END』之间：
                  <think>思考过程...</think>
                  『RESULT』
                    制造业占比60% ,金融业是...
                  『END』
                 \s
             4. **无答案情况**：如果参考内容中无任何相关片段，不输出内容"
                        
             5.处理示例
                  输入页面内容："报告期末按行业分类的境内股票投资组合：制造业 (60%)、金融业 (20%)..."
                 \s
                  <think>思考过程...</think>
                  『RESULT』
                     制造业占比60% ,金融业是...
                  『END』 ""\";
            注：用户查询是判断相关性的唯一标准，参考内容仅用于检索，不可影响问题理解。
            """);


    @PostMapping("/getExtraContent")
    public HashMap<String, String> getExtraContent(@RequestParam("files") MultipartFile[] files ,@RequestParam("isSlow") boolean isSlow) throws Exception {
        HashMap<String, String> fileMap = new HashMap<>();
        Arrays.stream(files).forEach(file -> processDoc(file, isSlow, fileMap));
        return fileMap;
    }

    public void processDoc(MultipartFile file, boolean isSlow, HashMap<String, String> fileMap) {
        long currentTimeMillis = System.currentTimeMillis();
        Resource resource = parseFile(file);
        String docContent = getDocContent(resource);
        if (isSlow) {
            String contentFast = extraSlow(docContent);
            fileMap.put(file.getOriginalFilename(), contentFast);
        } else {
            String contentSlow = extraFast(docContent);
            fileMap.put(file.getOriginalFilename(), contentSlow);
        }
        long endTime = System.currentTimeMillis();
        log.info("{}文件提取耗时{}s", file.getOriginalFilename(), (currentTimeMillis - endTime) / 1000);
    }

    private Resource parseFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("文件名无效");
        }
        try (InputStream is = file.getInputStream()) {
            if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
                byte[] bytes = is.readAllBytes();
                return new ByteArrayResource(bytes);
            } else {
                throw new IllegalArgumentException("不支持的文件");
            }
        } catch (Exception e) {
            throw new RuntimeException("Word解析失败: " + e.getMessage(), e);
        }
    }

    private String getDocContent(Resource resource) {
        /*String filePath = "classpath:/data/代销理财产品上线审查表--中邮理财鸿业远图封闭式2025年第75期人民币理财产品.docx";
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(filePath);*/
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
        List<Document> documents = tikaDocumentReader.read();
        List<String> texts = documents.stream().map(Document::getText).toList();
        StringBuilder documentContext = new StringBuilder();
        texts.forEach(documentContext::append);
        return documentContext.toString();
    }


    private String extraSlow(String documentContext) {
        // 初始化查询
        Map<String, String> queryMap = FundDocUtil.initAllQuery(DocEntity.class);
        // 将模型结果封装到基金对象
        FundFieldMapper mapper = new FundFieldMapper(DocEntity.class);
        DocEntity fdProductReport = new DocEntity();
        // 处理所有查询问题
        for (Map.Entry<String, String> entry : queryMap.entrySet()) {
            String question = entry.getKey();
            try {
                String augmentedUserText = promptTemplate
                        .render(Map.of("query", question, "question_answer_context", documentContext.toString()));
                String content = chatClient.prompt().options(ChatOptions.builder().temperature(0.1).build())
                        .user(augmentedUserText).call().content();

                String answer = "无数据";
                String prefix = "『RESULT』";
                String suffix = "『END』";
                if (content.contains(prefix) && content.contains(suffix)) {
                    answer = content.split(prefix)[1].split(suffix)[0].trim();
                }

                mapper.setFieldByComment(fdProductReport, question, StringUtils.isEmpty(answer) ? "无数据" : answer);
            } catch (Exception ignored) {

            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(fdProductReport);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonStr;
    }


    private String extraFast(String documentContext) {
        // 初始化查询
        Map<String, String> queryMap = FundDocUtil.initAllQuery(DocEntity.class);

        Set<Map.Entry<String, String>> set = queryMap.entrySet();
        List<String> keys = set.stream().map(Map.Entry::getKey).toList();

        String augmentedUserText = promptTemplate
                .render(Map.of("query", List2MdUtil.convert(keys), "question_answer_context", documentContext.toString()));
        DocEntity entity = chatClient.prompt().options(ChatOptions.builder().temperature(0.1).build())
                .user(augmentedUserText).call().entity(DocEntity.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonStr;
    }

}
