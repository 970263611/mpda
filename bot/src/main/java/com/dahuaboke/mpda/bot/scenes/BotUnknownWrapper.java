package com.dahuaboke.mpda.bot.scenes;

import com.dahuaboke.mpda.core.agent.scene.UnknownWrapper;
import org.springframework.stereotype.Component;

/**
 * @author: ZHANGSHUHAN
 * @date: 2025/10/28
 */
@Component
public class BotUnknownWrapper extends UnknownWrapper {
    @Override
    public String reply() {
        return """
                很抱歉，邮小盈还不能回答您的问题，我们正在努力开发中~\n您可以问我：查询产品信息；个性化推荐产品；定制市场产品报告；产品对比                
                """;
    }
}
