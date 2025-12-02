package com.dahuaboke.mpda.bot.scenes.product.information.name;


import com.dahuaboke.mpda.bot.scenes.product.AbstractProductAgentPrompt;
import com.dahuaboke.mpda.core.utils.Map2MdUtil;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 14:10
 */
@Component
public class InformationByNameAgentPrompt extends AbstractProductAgentPrompt {

    public InformationByNameAgentPrompt() {
        this.promptMap = Map.of(
                "guide", """
                        结合上下文分析用户的对话内容，判断用户最近的问题是否是首次查询(首次查询可以通过用户输出的内容是否是新的产品名称),还是已确认产品,亦或是无关对话：
                           1. 首次查询(忽略历史消息,不要造成混乱)：走如下流程
                                1.1.处理逻辑：
                                   - 获取产品映射：将{mapper} 映射关系整理格式直接返回给用户,让用户选择,切勿胡编乱造!
                                   - 返回用户语义为: 为您查询到[x]支以上相关产品，请问您想了解哪支产品呢？若无您需要的产品，请您提供更详细的产品名称或产品代码，我会继续为您查询哦～
                                1.2.示例：
                                    问题：帮我查询下工银的费率
                                    数据：
                                        0001：工银瑞1，
                                        0002：工银瑞2，
                                        0003：工银瑞3，
                                        0004：建银瑞4，
                                    返回：
                                        根据你的查询，以下是与"工银"名称匹配的产品：
                                        1.工银瑞1(0001)
                                        2.工银瑞2(0002)
                                        3.工银瑞3(0003)
                                        请确认具体需要查询的产品
                           2. 已确认具体产品,此时可以通过用户上下文提取出6位产品编码,直接调用工具:informationByIdTool
                           3. 无关对话,需要引导用户补充产品名称。
                        """,
                "before", """
                        1.通过上下文分析用户的对话，提取用户最新一次提问的产品名称
                        2.仅返回产品名称，不要添加其他内容
                        3.示例：
                            提问：我想了解下邮银相关的基金
                            返回：邮银
                        """);
        this.description = promptMap.get("guide");
    }

    public void buildGuide(String params) {
        PromptTemplate promptTemplate = new PromptTemplate(promptMap.get("guide"));
        promptTemplate.add("mapper", params);
        this.description = promptTemplate.create().getContents();
    }
}

