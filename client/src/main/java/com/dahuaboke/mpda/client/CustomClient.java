package com.dahuaboke.mpda.client;

import com.dahuaboke.mpda.client.constants.RagConstant;
import com.dahuaboke.mpda.client.entity.CommonReq;
import com.dahuaboke.mpda.client.entity.CommonResp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.glassfish.hk2.utilities.reflection.ParameterizedTypeImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.util.JacksonUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Predicate;

/**
 * @Desc: 新核心通用请求客户端
 * @Author：zhh
 * @Date：2025/9/4 17:32
 */

public class CustomClient {


    public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .addModules(JacksonUtils.instantiateAvailableModules())
            .build()
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    private static final Predicate<String> SSE_DONE_PREDICATE = "[DONE]"::equals;
    private static final Logger log = LoggerFactory.getLogger(CustomClient.class);
    private final RestClient restClient;
    private final WebClient webClient;


    public CustomClient(RestClient restClient, WebClient webClient) {
        this.restClient = restClient;
        this.webClient = webClient;
    }

    public <T, R> CommonResp<R> execute(String url, CommonReq<T> commonReq) {
        ParameterizedTypeReference<CommonResp<R>> typeReference = new ParameterizedTypeReference<>() {
        };
        return request(url, commonReq, typeReference);
    }

    public <T, R> R execute(String url, CommonReq<T> commonReq, Class<R> dataTypeClass) {
        ParameterizedTypeReference<CommonResp<R>> typeReference = new ParameterizedTypeReference<>() {
            @NotNull
            @Override
            public Type getType() {
                return new ParameterizedTypeImpl(CommonResp.class, dataTypeClass);
            }
        };
        CommonResp<R> resp = request(url, commonReq, typeReference);
        if (resp.getTxBody() == null) {
            log.error("{} txBody is null  due to: {}:{}", url, resp.getTxHeader().getServRespCd(), resp.getTxHeader().getServRespDescInfo());
        }
        if (resp.getTxBody().getTxEntity() == null) {
            log.error("{} txEntity is null  due to: {}:{}", url, resp.getTxHeader().getServRespCd(), resp.getTxHeader().getServRespDescInfo());
        }
        return resp.getTxBody().getTxEntity();
    }

    private <T, R> CommonResp<R> request(String url, CommonReq<T> commonReq, ParameterizedTypeReference<CommonResp<R>> typeReference) {

        ResponseEntity<CommonResp<R>> commonRespResponseEntity = restClient
                .post()
                .uri(url)
                .body(commonReq)
                .retrieve()
                .toEntity(typeReference);

        CommonResp<R> resp = commonRespResponseEntity.getBody();
        if (!RagConstant.SUCCESS_CODE.equals(resp.getTxHeader().getServRespCd())) {
            log.error("interface {} fails to send due to: {}", url, resp.getTxHeader().getServRespDescInfo());
        }
        return resp;
    }


    public <T, R> Flux<R> executeStream(String url, CommonReq<T> commonReq) {
        TypeReference<CommonResp<R>> typeRef = new TypeReference<>() {
        };
        return requestStream(url, commonReq, typeRef);
    }


    private <T, R> Flux<R> requestStream(String url, CommonReq<T> commonReq, TypeReference<CommonResp<R>> typeRef) {

        return this.webClient.post()
                .uri(url)
                .body(Mono.just(commonReq), CommonReq.class)
                .retrieve()
                .bodyToFlux(String.class)
                .takeUntil(SSE_DONE_PREDICATE)
                .filter(SSE_DONE_PREDICATE.negate())
                .map(content ->
                {
                    try {
                        return OBJECT_MAPPER.readValue(content, typeRef);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })

                .concatMapIterable(window -> {
                    Mono<R> monoChunk = Mono.just(window.getTxBody().getTxEntity());
                    return List.of(monoChunk);
                })
                .flatMap(mono -> mono);
    }


}
