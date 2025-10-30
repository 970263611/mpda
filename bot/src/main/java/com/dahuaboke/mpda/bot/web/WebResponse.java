package com.dahuaboke.mpda.bot.web;

import com.dahuaboke.mpda.bot.scenes.entity.PlatformExtend;
import reactor.core.publisher.Flux;

public record WebResponse(String code, String msg, String content, PlatformExtend extend) {
}
