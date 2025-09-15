package com.dahuaboke.mpda.bot.web;

import com.dahuaboke.mpda.bot.model.request.ChatBotRequest;
import com.dahuaboke.mpda.bot.model.response.ChatBotResponse;
import com.dahuaboke.mpda.bot.model.common.CommonResponse;
import com.dahuaboke.mpda.bot.web.service.ChatService;
import com.dahuaboke.mpda.core.agent.exception.MpdaException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;



    @RequestMapping("/chat")
    public CommonResponse<ChatBotResponse> chat(
            @RequestBody ChatBotRequest chatBotRequest) throws MpdaException {
        return chatService.chat(chatBotRequest);
    }

    @RequestMapping("/stream")
    public Flux<ServerSentEvent<String>> chatStream(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestBody String q) throws MpdaException {
        Flux<String> response = chatService.chatStream(conversationId, q);
        return response.map(res -> {
            Map<String, Object> delta = Map.of("role", "assistant", "content", res);
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

}
