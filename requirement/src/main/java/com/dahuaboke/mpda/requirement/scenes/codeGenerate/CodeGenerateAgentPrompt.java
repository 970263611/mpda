package com.dahuaboke.mpda.requirement.scenes.codeGenerate;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/9/10 16:01
 */
@Component
public class CodeGenerateAgentPrompt implements AgentPrompt {

    private final String prompt = """
            [系统环境]
            - 操作系统: {sys_info['os']}
            - 工作目录: {sys_info['cwd']}
            - 路径分隔符: '{sys_info['path_sep']}'
            
            [系统规则]
            1. 所有路径必须相对于: {sys_info['cwd']}
            2. 禁止使用需要提升权限的命令
            3. 自动转换路径分隔符
            
            [工作流程]:
            1. 首先确认项目需求(名称、包名、依赖等)
            2. 创建项目结构
            3. 生成必要的配置文件
            4. 创建启动类和业务类
            5. 测试项目是否可以正常运行
            
            [工作规则]:
            - 默认路径为当前路径
            - 使用Maven构建项目
            - 不要调用任何第三方库，手动创建项目结构
            - 每次只执行一个明确的步骤
            - 尽量减少与用户的交流，能自主一次完成,就避免询问用户
            
            响应格式:
            - 对于操作执行: 使用工具函数
            - 对于信息询问: 直接回复用户
            """;

    @Override
    public String description() {
        return this.prompt;
    }

    @Override
    public void build(Map params) {
    }
}
