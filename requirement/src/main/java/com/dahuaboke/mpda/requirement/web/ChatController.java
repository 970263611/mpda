package com.dahuaboke.mpda.requirement.web;

import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.dahuaboke.mpda.requirement.scenes.requirement.split.SplitScene;
import com.dahuaboke.mpda.requirement.utils.FileParser;
import com.dahuaboke.mpda.requirement.web.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.*;

@CrossOrigin
@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/split")
    public SceneResponse split(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestParam("files") MultipartFile[] files) throws MpdaException, JsonProcessingException {
        Map fileMap = new LinkedHashMap();
        try {
            Arrays.stream(files).forEach(file -> fileMap.put(file.getName(), FileParser.parseText(file)));
        } catch (Exception e) {
        }
        String q = objectMapper.writeValueAsString(fileMap);
        CoreContext context = new CoreContext(q, conversationId);
        context.setSceneName(SplitScene.class.getSimpleName());
        SceneResponse response = chatService.split(context);
        return response;
//        return response.map(res -> {
//            Map<String, Object> delta = Map.of("role", "assistant", "content", res.output());
//            Map<String, Object> choice = Map.of("index", 0, "delta", delta, "finish_reason", "");
//            List<Map<String, Object>> choices = List.of(choice);
//            try {
//                String jsonData = objectMapper.writeValueAsString(Map.of("choices", choices));
//                return ServerSentEvent.<String>builder()
//                        .id(UUID.randomUUID().toString())
//                        .event("message")
//                        .data(jsonData)
//                        .build();
//            } catch (JsonProcessingException e) {
//                return ServerSentEvent.<String>builder().build();
//            }
//        });
    }
}
