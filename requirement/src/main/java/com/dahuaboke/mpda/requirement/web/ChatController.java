package com.dahuaboke.mpda.requirement.web;

import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.dahuaboke.mpda.requirement.scenes.requirement.analyze.AnalyzeScene;
import com.dahuaboke.mpda.requirement.scenes.requirement.check.CheckScene;
import com.dahuaboke.mpda.requirement.scenes.requirement.document.DocumentScene;
import com.dahuaboke.mpda.requirement.scenes.requirement.optimize.OptimizeScene;
import com.dahuaboke.mpda.requirement.scenes.requirement.split.SplitScene;
import com.dahuaboke.mpda.requirement.scenes.requirement.value.ValueScene;
import com.dahuaboke.mpda.requirement.utils.FileParser;
import com.dahuaboke.mpda.requirement.web.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

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
    }

    @PostMapping("/analyze")
    public SceneResponse analyze(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestBody String q) throws MpdaException, JsonProcessingException {
        CoreContext context = new CoreContext(q, conversationId);
        context.setSceneName(AnalyzeScene.class.getSimpleName());
        SceneResponse response = chatService.split(context);
        return response;
    }

    @PostMapping("/check")
    public SceneResponse check(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestBody String q) throws MpdaException, JsonProcessingException {
        CoreContext context = new CoreContext(q, conversationId);
        context.setSceneName(CheckScene.class.getSimpleName());
        SceneResponse response = chatService.split(context);
        return response;
    }

    @PostMapping("/value")
    public SceneResponse value(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestBody String q) throws MpdaException, JsonProcessingException {
        CoreContext context = new CoreContext(q, conversationId);
        context.setSceneName(ValueScene.class.getSimpleName());
        SceneResponse response = chatService.split(context);
        return response;
    }

    @PostMapping("/document")
    public SceneResponse document(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestBody String q) throws MpdaException, JsonProcessingException {
        CoreContext context = new CoreContext(q, conversationId);
        context.setSceneName(DocumentScene.class.getSimpleName());
        SceneResponse response = chatService.split(context);
        return response;
    }

    @PostMapping("/optimize")
    public SceneResponse optimize(
            @RequestHeader("Conversation-Id") String conversationId,
            @RequestBody String q) throws MpdaException, JsonProcessingException {
        CoreContext context = new CoreContext(q, conversationId);
        context.setSceneName(OptimizeScene.class.getSimpleName());
        SceneResponse response = chatService.split(context);
        return response;
    }
}
