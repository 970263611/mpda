package com.dahuaboke.mpda.bot.web;

import com.dahuaboke.mpda.bot.model.common.CommonResponse;
import com.dahuaboke.mpda.bot.model.response.ChatBotResponse;
import com.dahuaboke.mpda.bot.web.service.ChatService;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@CrossOrigin
@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping("/chat")
    public CommonResponse<ChatBotResponse, Object> chat(
            @RequestBody CoreContext context) throws MpdaException {
        return chatService.chat(context);
    }

    @RequestMapping("/stream")
    public Flux<ServerSentEvent<String>> chatStream(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestBody String q) throws MpdaException {
        CoreContext context = new CoreContext(q, conversationId);
        Flux<SceneResponse> response = chatService.chatStream(context);
        return response.map(res -> {
            try {
                System.out.println("-------------------------------------------" + objectMapper.writeValueAsString(res.extend()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Map<String, Object> delta = Map.of("role", "assistant", "content", res.output());
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
        Flux<SceneResponse> response = chatService.chatStream(context);
        return response.map(res -> {
            Object extend = null;
            try {
                if (res.extend() != null) {
                    extend = objectMapper.writeValueAsString(res.extend().graphExtend());
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if ("null".equals(extend) || "".equals(extend)) {
                extend = null;
            }
            CommonResponse<String, Object> success = CommonResponse.success(res.output(), extend);
            try {
                String jsonData = objectMapper.writeValueAsString(success);
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


}
