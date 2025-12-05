package com.dahuaboke.mpda.core.client.entity;


import org.springframework.ai.chat.model.ChatResponse;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/8/21 10:37
 */
public record StreamLlmResponse(Flux<ChatResponse> response) {
}
