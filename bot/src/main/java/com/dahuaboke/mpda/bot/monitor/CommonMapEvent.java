package com.dahuaboke.mpda.bot.monitor;

import com.dahuaboke.mpda.core.event.Event;

import java.util.Map;

/**
 * @description: zhangjie fw
 * @author: ZHANGSHUHAN
 * @date: 2025/12/09
 */
public record CommonMapEvent(Map map, String type) implements Event {
}
