package com.dahuaboke.mpda.bot.rag.client;

import com.dahuaboke.mpda.bot.constants.FundConstant;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import com.dahuaboke.mpda.client.vector.CustomVectorStore;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Desc: 引入新核心接口的向量数据库对象
 * @Author：zhh
 * @Date：2025/9/4 20:16
 */
@Component
public class VectorStoreConfig {


    @Bean
    public VectorStore customVectorStore(EmbeddingModel embeddingModel, VectorStoreRequestHandle vectorStoreRequestHandle) {
        return CustomVectorStore.builder(embeddingModel, vectorStoreRequestHandle)
                .collectionName(FundConstant.INDEX_NAME)
                .vectorFieldName(FundConstant.EMBEDDING)
                .converter(new FundDocumentConverter())
                .build();
    }

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder

                .defaultAdvisors(
                        new SimpleLoggerAdvisor()
                )
                .build();
    }


}
