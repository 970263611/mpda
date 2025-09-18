package com.dahuaboke.mpda.ai_code.web;

import com.dahuaboke.mpda.ai_code.web.service.ChatService;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
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

    @RequestMapping("/stream")
    public Flux<ServerSentEvent<String>> chat(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestBody String q) throws MpdaException {
        CoreContext context = new CoreContext(q, conversationId);
        Flux<SceneResponse> response = chatService.chat(context);
        return response.map(res -> {
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
}
