package com.dahuaboke.mpda.bot.scenes.product.information;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.utils.List2MdUtil;
import com.dahuaboke.mpda.core.utils.Map2MdUtil;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * auth: dahua
 * time: 2025/8/21 14:02
 */
@Component
public class InformationAgentPrompt implements AgentPrompt {

    private final String prompt = """
                1.根据上下文消息和用户新问题,来判断查询产品是通过提供的【6位产品编号】还是【产品名称】亦或者是【没有提供编号或名称】。
                2.根据以下对应关系返回产品编号场景还是产品名称场景的对应场景编号：
                    {scenes}
                3.【6位产品编号】：返回通过编号查询产品场景的场景编号。
                4.【产品名称】：返回通过产品名称查询产品的场景编号。
                5.【没有提供编号或名称】：返回没有提供编号或名称场景编号。
                6.示例：
                    数据：
                        abcdefg
                        1234567
                    返回：abcdefg
                7.关键！ 必须返回{ids}的其中之一,注意不要添加任何其他符号,切勿返回其余内容影响后续流程。
            """;
    private String description;

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public void build(Map params) {
        PromptTemplate promptTemplate = new PromptTemplate(prompt);
        Set<Map.Entry<String, String>> set = params.entrySet();
        List<String> keys = set.stream().map(Map.Entry::getKey).toList();
        promptTemplate.add("scenes", Map2MdUtil.convert(params));
        promptTemplate.add("ids", List2MdUtil.convert(keys));
        this.description = promptTemplate.create().getContents();
    }
}
