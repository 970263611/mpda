package com.dahuaboke.mpda.core.agent.scene;

/**
 * @author: ZHANGSHUHAN
 * @date: 2025/10/28
 */
public class DefaultUnknownWrapper extends UnknownWrapper {
    @Override
    public String reply() {
        return """
                您的问题我还不懂，等我变聪明些就可以回答了。
                """;
    }
}
