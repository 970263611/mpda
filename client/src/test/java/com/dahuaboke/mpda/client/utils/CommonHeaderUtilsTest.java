package com.dahuaboke.mpda.client.utils;

import org.junit.jupiter.api.Test;

public class CommonHeaderUtilsTest {

    @Test
    public void generateGlobalBusiTrackNo() {
        String globalTraceNo = CommonHeaderUtils.generateGlobalBusiTrackNo("99370000000");
        System.out.println("全局流水号为: " + globalTraceNo);
    }

    @Test
    public void generateSubtxNo() {

        String subtxNo = CommonHeaderUtils.generateSubtxNo("99370000000");
        System.out.println("子交易序号为: " + subtxNo);
    }
}
