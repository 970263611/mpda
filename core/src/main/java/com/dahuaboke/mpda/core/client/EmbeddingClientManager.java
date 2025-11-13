package com.dahuaboke.mpda.core.client;


import com.dahuaboke.mpda.core.client.entity.EmbeddingResponse;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/9/1 11:16
 */
@Component
public class EmbeddingClientManager {

    public EmbeddingResponse embed(String text) {
        return new EmbeddingResponse("");
    }
}
