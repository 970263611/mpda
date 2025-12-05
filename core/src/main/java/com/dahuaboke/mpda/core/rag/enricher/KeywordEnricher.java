package com.dahuaboke.mpda.core.rag.enricher;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @Desc: 文档关键字提取工具
 * @Author：zhh
 * @Date：2025/7/22 15:03
 */
public class KeywordEnricher implements DocumentTransformer {

    public static final String EXCERPT_KEYWORDS_METADATA_KEY = "excerpt_keywords";
    private final ChatModel chatModel;
    private final String keyWordTemplate;
    private final String prefix;
    private final String suffix;

    public KeywordEnricher(ChatModel chatModel, String keyWordTemplate, String prefix, String suffix) {
        this.chatModel = chatModel;
        this.keyWordTemplate = keyWordTemplate;
        this.prefix = prefix;
        this.suffix = suffix;
    }


    @Override
    public List<Document> apply(List<Document> documents) {
        for (Document document : documents) {
            Prompt text = new Prompt(document.getText() + keyWordTemplate);
            String content = chatModel.call(text).getResult().getOutput().getText();
            String keys = "";
            if (content.contains(prefix) && content.contains(suffix)) {
                keys = content.split(prefix)[1].split(suffix)[0].trim();
            }
            document.getMetadata().putAll(Map.of(EXCERPT_KEYWORDS_METADATA_KEY, commaSeparatedToKeywords(keys)));
        }
        return documents;
    }

    private List<String> commaSeparatedToKeywords(String input) {
        if (input == null || input.trim().isEmpty()) {
            return List.of();
        }

        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
