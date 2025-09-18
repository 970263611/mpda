package com.dahuaboke.mpda.client;

import com.dahuaboke.mpda.client.constants.RagConstant;
import com.dahuaboke.mpda.client.handle.RerankModelRequestHandle;
import com.dahuaboke.mpda.client.rerank.CustomRerankModel;
import com.dahuaboke.mpda.core.rag.rerank.Rerank;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Desc: 新核心接口重排序自动配置类
 * @Author：zhh
 * @Date：2025/9/15 15:24
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = RagConstant.RERANK_MODEL, name = "enabled", havingValue = "true")
@ImportAutoConfiguration(ClientConfiguration.class)
@EnableConfigurationProperties(ClientProperties.class)
public class ClientRerankModelAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RerankModelRequestHandle rerankModelRequestHandle(CustomClient customClient, ClientProperties clientProperties) {
        return new RerankModelRequestHandle(customClient, clientProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public Rerank customRerankModel(RerankModelRequestHandle rerankModelRequestHandle) {
        return new CustomRerankModel(rerankModelRequestHandle);
    }
}
