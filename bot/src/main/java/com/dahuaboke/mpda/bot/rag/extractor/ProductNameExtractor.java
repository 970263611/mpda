package com.dahuaboke.mpda.bot.rag.extractor;

import com.dahuaboke.mpda.bot.rag.utils.FundDocUtil;
import com.dahuaboke.mpda.core.rag.extractor.QueryExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Desc: 提取用户输入的基金名称
 * @Author：zhh
 * @Date：2025/7/25 11:06
 */
public class ProductNameExtractor implements QueryExtractor {

    private static final Logger log = LoggerFactory.getLogger(ProductNameExtractor.class);

    private final String query;

    private final ChatModel chatModel;

    private final String prefix = "『RESULT』";

    private final String suffix = "『END』";

    private final PromptTemplate DEFAULT_PROMPT_TEMPLATE = new PromptTemplate("""
            根据用户查询问题,从基金名称中提取最精确的基金:
            
            **用户查询（核心焦点，必须优先匹配）** \s
            "{query}"                  
            **基金名称 (检查用户是否包含以下字段,若存在则提取，否则留空)** \s
            {productName}
            
            1.只提取"基金名称"中定义的字符串：若未提取到基金名称，留空，不要产生不存在"基金名称"的基金。             
            2.请将最终结果放在{prefix}和{suffix}之间：
            
             {prefix}
             基金名称1,基金名称2 
             {suffix}
            4.处理示例
             用户查询："华泰柏瑞积极优选股票A的基金经理..."
            
             {prefix}
             华泰柏瑞积极优选股票A
             {suffix}        
            """);

    public ProductNameExtractor(ChatModel chatModel, String query) {
        Assert.notNull(chatModel, "ChatModel must not be null");
        this.chatModel = chatModel;
        this.query = query;

    }

    public static List<String> getProductName() {
        //TODO 需要额外处理
        return FundDocUtil.getProductMap("").entrySet().stream().flatMap(entry -> Stream.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> extractor() {
        String productName;
        try {
            List<String> productList = getProductName();
            String augmentedUserText = DEFAULT_PROMPT_TEMPLATE
                    .render(Map.of("prefix", prefix, "suffix", suffix, "query", query, "productName", productList.toString()));
            Prompt text = new Prompt(augmentedUserText);
            String content = chatModel.call(text).getResult().getOutput().getText();
            assert content != null;
            productName = content.split("『RESULT』")[1].split("『END』")[0].trim();
            log.info("提取用户的基金名称为:" + productName);
            if (productName.equals("")) {
                return new ArrayList<>();
            }
            return Arrays.asList(productName.split(","));
        } catch (Exception e) {
            log.error(e.getMessage());
            return List.of();
        }
    }
}
