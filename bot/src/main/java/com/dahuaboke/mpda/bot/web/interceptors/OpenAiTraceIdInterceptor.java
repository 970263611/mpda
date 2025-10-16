package com.dahuaboke.mpda.bot.web.interceptors;

import com.dahuaboke.mpda.bot.constants.FundConstant;

import com.dahuaboke.mpda.client.utils.CommonHeaderUtils;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class OpenAiTraceIdInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger log = LoggerFactory.getLogger(OpenAiTraceIdInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        String generateGlobalBusiTrackNo = CommonHeaderUtils.generateGlobalBusiTrackNo(FundConstant.LC_SYSTEM);
        headers.add("globalBusiTrackNo", generateGlobalBusiTrackNo);

        log.debug("OpenAI 请求：globalBusiTrackNo={}",generateGlobalBusiTrackNo);
        try{
            return execution.execute(request, body);
        }catch (Exception e){
            log.error("OpenAI 请求失败： globalBusiTrackNo={}",generateGlobalBusiTrackNo,e);
            throw e;
        }
    }

}
