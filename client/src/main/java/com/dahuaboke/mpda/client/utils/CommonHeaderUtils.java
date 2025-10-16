package com.dahuaboke.mpda.client.utils;

import com.dahuaboke.mpda.client.ClientProperties;
import com.dahuaboke.mpda.client.entity.TxHeaderReq;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Desc: 通用报文头构建工具类
 * @Author：zhh
 * @Date：2025/9/5 10:43
 */
public class CommonHeaderUtils {

    private static final AtomicLong INSTANCE_SEQ = new AtomicLong(0);

    private static final AtomicLong MACHINE_SEQ = new AtomicLong(0);

    /**
     * 构建通用报文头
     *
     * @param coreClientProperties   配置属性
     * @param servNo      接口服务号（如RAG_V1_C014007）
     * @return 构建好的TxHeaderReq
     */
    public static TxHeaderReq build(ClientProperties coreClientProperties,String servNo) {
        String sendSysNo = coreClientProperties.getSendSysNo();
        String targetSysNo = coreClientProperties.getTargetSysNo();
        String resvedInputInfo = coreClientProperties.getResvedInputInfo();

        TxHeaderReq txHeaderReq = new TxHeaderReq();
        txHeaderReq.setStartSysOrCmptNo(sendSysNo);
        txHeaderReq.setSendSysOrCmptNo(sendSysNo);
        txHeaderReq.setTargetSysOrCmptNo(targetSysNo);
        txHeaderReq.setServNo(servNo);
        String generateGlobalBusiTrackNo = generateGlobalBusiTrackNo(sendSysNo);
        txHeaderReq.setGlobalBusiTrackNo(generateGlobalBusiTrackNo);
        txHeaderReq.setReqSysSriNo(generateGlobalBusiTrackNo);
        txHeaderReq.setSubtxNo(generateSubtxNo(sendSysNo));
        txHeaderReq.setResvedInputInfo(resvedInputInfo);
        return txHeaderReq;
    }

    /**
     * 子交易序号
     * 子交易序列号由三部分组成：七位系统编码（系统编码大于七位则取前七位）
     * +五位workid（workid生成逻辑为从系统变量获取workid，然后从环境变量中获取，如果不存在就会拼接mac地址+dubboPort+maxWorkerId拼接，目前平台写了一个demo，WorkIdGeneratorUtil仅供参考！！目标就是不同的服务workid不同即可）
     * +20位序列号（传20位0即可）----（必传且不能写死）
     */
    public static String generateSubtxNo(String sendSysNo) {
        StringBuilder stringBuilder = new StringBuilder();
        //第一段
        String originSys = sendSysNo.substring(0, 7);
        stringBuilder.append(originSys);
        //第二段
        stringBuilder.append("15771");
        //第三段
        stringBuilder.append("00000000000000000000");
        return stringBuilder.toString();
    }

    /**
     * 4段32位编码。
     * 第一段：时间戳，长度14位，表示交易发起时间，格式为YYYYMMDDHHMMSS;
     * 第二段：源系统，长度7位，标识交易发起的系统；
     * 第三位：发起交易实例序号，长度5位；
     * 第四段：长度6位，由机器自身的序列号生成器产生，从0开始递增，每取出一个序列号后即向上加一，到达999999后循环从0开始，序列号与时间不相关。（必传且不能写死）
     */
    public static String generateGlobalBusiTrackNo(String sendSysNo) {
        StringBuilder stringBuilder = new StringBuilder();
        //第一段
        SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = SDF.format(new Date());
        stringBuilder.append(time);
        //第二段
        String originSys = sendSysNo.substring(0, 7);
        stringBuilder.append(originSys);
        //第三段
        long instanceSeq = INSTANCE_SEQ.getAndIncrement() % 100000;
        stringBuilder.append(String.format("%05d", instanceSeq));
        // 第四段
        long machineSeq = MACHINE_SEQ.incrementAndGet() % 1000000;
        stringBuilder.append(String.format("%06d", machineSeq));
        return stringBuilder.toString();
    }

}
