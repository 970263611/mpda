package com.dahuaboke.mpda.bot.scenes.product.marketRanking.node;


import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.bot.scenes.node.TranslateNode;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankDto;
import com.dahuaboke.mpda.bot.utils.TranslationUtils;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 10:24
 */
@Component
public class MarketRankingTranslateNode extends TranslateNode implements NodeAction {

    private static final Logger log = LoggerFactory.getLogger(MarketRankingTranslateNode.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String reBuildToolResp(String toolResponseData) {
        String translateToolResponseData = null;
        try {
            ToolResult toolResult = objectMapper.readValue(toolResponseData, ToolResult.class);
            List<Map<String, Object>> marketRankDtosFinal = null;
            List<Map<String, Object>> marketRankDtos = (List<Map<String, Object>>) toolResult.getData();
            marketRankDtosFinal = TranslationUtils.convertToChineseFieldMap(marketRankDtos, MarketRankDto.class);
            toolResult.setData(marketRankDtosFinal);
            translateToolResponseData = objectMapper.writeValueAsString(toolResult);
        } catch (JsonProcessingException e) {
            log.error("MarketRankingTranslateNode JsonProcessingException {}", e.getMessage());
        } catch (Exception e) {
            log.error("MarketRankingTranslateNode,TranslationUtils Exception {},toolResponseData is {})", e.getMessage(), toolResponseData);
        }
        return translateToolResponseData;
    }


}
