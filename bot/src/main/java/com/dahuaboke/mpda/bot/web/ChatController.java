package com.dahuaboke.mpda.bot.web;

import com.dahuaboke.mpda.bot.constants.enums.ResponseCode;
import com.dahuaboke.mpda.bot.scenes.entity.PlatformRep;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankDto;
import com.dahuaboke.mpda.bot.tools.dto.MarketRankReq;
import com.dahuaboke.mpda.bot.tools.service.RobotService;
import com.dahuaboke.mpda.bot.web.service.ChatService;
import com.dahuaboke.mpda.client.entity.CommonReq;
import com.dahuaboke.mpda.client.entity.CommonResp;
import com.dahuaboke.mpda.client.entity.TxBodyResp;
import com.dahuaboke.mpda.client.entity.TxHeaderResp;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RobotService robotService;

    @RequestMapping("/stream")
    public Flux<ServerSentEvent<String>> chatStream(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestBody String q) throws MpdaException {
        CoreContext context = new CoreContext(q, conversationId);
        Flux<WebResponse> response = chatService.chatStream(context);
        return response.map(res -> {
            Map<String, Object> delta = Map.of("role", "assistant", "content", res.content());
            Map<String, Object> choice = Map.of("index", 0, "delta", delta, "finish_reason", "");
            List<Map<String, Object>> choices = List.of(choice);
            try {
                String jsonData = objectMapper.writeValueAsString(Map.of("choices", choices));
                return ServerSentEvent.<String>builder()
                        .id(UUID.randomUUID().toString())
                        .event("message")
                        .data(jsonData)
                        .build();
            } catch (JsonProcessingException e) {
                return ServerSentEvent.<String>builder().build();
            }
        });
    }

    @RequestMapping("/platFromStream")
    public Flux<ServerSentEvent<String>> platFromStream(
            @RequestBody CoreContext context) throws MpdaException {
        Flux<WebResponse> response = chatService.chatStream(context);
        return response.map(res -> {
            try {
                String jsonData = objectMapper.writeValueAsString(res);
                return ServerSentEvent.<String>builder()
                        .id(UUID.randomUUID().toString())
                        .event("message")
                        .data(jsonData)
                        .build();
            } catch (JsonProcessingException e) {
                return ServerSentEvent.<String>builder().build();
            }
        });
    }

    /**
     * 平台市场报告下载场景返回
     */
    @PostMapping("/access/postRequest")
    public CommonResp<PlatformRep> selectMarketReportByTimeAndFundType(@RequestBody CommonReq<MarketRankReq> commonReq) {
        log.info("esb请求接入,服务编码为:{} 全局水流号为:{}", commonReq.getTxHeader().getServNo(), commonReq.getTxHeader().getGlobalBusiTrackNo());
        MarketRankReq marketRankReq = commonReq.getTxBody().getTxEntity();

        CommonResp<PlatformRep> commonResp = new CommonResp<>();
        //封装返回头
        TxHeaderResp txHeaderResp = new TxHeaderResp();
        BeanUtils.copyProperties(commonReq.getTxHeader(),txHeaderResp);
        commonResp.setTxHeader(txHeaderResp);
        //封装返回体
        TxBodyResp<PlatformRep> txBodyResp = new TxBodyResp<>();
        PlatformRep platformRep = new PlatformRep();
        if (StringUtils.isEmpty(marketRankReq.getFinBondType()) || StringUtils.isEmpty(marketRankReq.getPeriod())) {
            txHeaderResp.setServRespCd(ResponseCode.MISSING_PARAM.getCode());
            txHeaderResp.setServRespDescInfo(ResponseCode.MISSING_PARAM.getMsg());
            txBodyResp.setTxEntity(platformRep);
            commonResp.setTxBody(txBodyResp);
            return commonResp;
        }
        List<MarketRankDto> marketRankDtos = robotService.selectMarketReportByTimeAndFundType(marketRankReq.getFinBondType(), marketRankReq.getPeriod());
        platformRep.setMarketRankDtoList(marketRankDtos);
        txHeaderResp.setServRespCd(ResponseCode.SUCCESS.getCode());
        txHeaderResp.setServRespDescInfo(ResponseCode.SUCCESS.getMsg());
        txBodyResp.setTxEntity(platformRep);
        commonResp.setTxBody(txBodyResp);
        return commonResp;
    }

}
