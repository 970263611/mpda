package com.dahuaboke.mpda.bot.scenes.product.information.code.node;


import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.bot.scenes.node.TranslateNode;
import com.dahuaboke.mpda.bot.tools.dto.ProdInfoDto;
import com.dahuaboke.mpda.bot.utils.TranslationUtils;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 10:24
 */
@Component
public class InformationByIdTranslateNode extends TranslateNode implements NodeAction {

    private static final Logger log = LoggerFactory.getLogger(InformationByIdTranslateNode.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String reBuildToolResp(String toolResponseData) {
        String translateToolResponseData = null;
        try {
            ToolResult toolResult = objectMapper.readValue(toolResponseData, ToolResult.class);
            Map<String, Object> prodInfoDtoFinal = null;
            Map<String, Object> prodInfoDto =(Map<String, Object>)((Map<String, Object>) toolResult.getData()).get("result");
            prodInfoDtoFinal = TranslationUtils.convertToChineseFieldMap(prodInfoDto, ProdInfoDto.class);
            toolResult.setData(prodInfoDtoFinal);
            translateToolResponseData = objectMapper.writeValueAsString(toolResult);
        } catch (JsonProcessingException e) {
            log.error("InformationByIdTranslateNode JsonProcessingException {}", e.getMessage());
        } catch (Exception e) {
            log.error("InformationByIdTranslateNode,TranslationUtils Exception {},toolResponseData is {})", e.getMessage(), toolResponseData);
        }
        return translateToolResponseData;
    }


}
