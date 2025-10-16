package com.dahuaboke.mpda.bot.rag.extractor;

import com.dahuaboke.mpda.bot.rag.RagPrompt;
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

/**
 * @Desc: 提取用户输入的关键字
 * @Author：zhh
 * @Date：2025/7/25 11:06
 */
public class KeyWordExtractor implements QueryExtractor {

    private static final Logger log = LoggerFactory.getLogger(KeyWordExtractor.class);

    private final String query;

    private final ChatModel chatModel;

    private final String prefix = "『RESULT』";

    private final String suffix = "『END』";

    private final PromptTemplate DEFAULT_PROMPT_TEMPLATE = new PromptTemplate("""
            根据用户查询问题,从一下关键词中提取最精确的关键词:
            
            **用户查询（核心焦点，必须优先匹配）** \s
            "{query}"                  
            **关键词 (检查用户是否包含以下字段,若存在则提取，否则留空)** \s
            {keyWords}
            
            1.只提取"关键词"中定义的字符串：若未提取到关键词，留空，不要产生不存在"关键词"的基金。             
            2.请将最终结果放在{prefix}和{suffix}之间：
            
             {prefix}
             关键词1,关键词2 
             {suffix}
            4.处理示例
             用户查询："华泰柏瑞积极优选股票A的基金经理..."
            
             {prefix}
             基金经理
             {suffix}        
            """);

    public KeyWordExtractor(ChatModel chatModel, String query) {
        Assert.notNull(chatModel, "ChatModel must not be null");
        this.chatModel = chatModel;
        this.query = query;
    }

    @Override
    public List<String> extractor() {
        String keys;
        try {
            List<String> keyWords = RagPrompt.FUND_KEYS;
            String augmentedUserText = DEFAULT_PROMPT_TEMPLATE
                    .render(Map.of("prefix", prefix, "suffix", suffix, "query", query, "keyWords", keyWords.toString()));
            Prompt text = new Prompt(augmentedUserText);
            String content = chatModel.call(text).getResult().getOutput().getText();
            assert content != null;
            keys = content.split(prefix)[1].split(suffix)[0].trim();
            if (keys.equals("")) {
                return new ArrayList<>();
            }
            return Arrays.asList(keys.split(","));
        } catch (Exception e) {
            log.error(e.getMessage());
            return List.of();
        }
    }
}
