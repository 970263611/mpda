package com.dahuaboke.mpda.requirement.scenes.codeGenerate.tools;


import com.dahuaboke.mpda.core.agent.tools.AbstractBaseTool;
import com.dahuaboke.mpda.core.agent.tools.ToolResult;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/9/10 16:45
 */
@Component
public class CodeGenerateTool extends AbstractBaseTool<CodeGenerateTool.Input> {

    @Override
    public String getDescription() {
        return """
                    代码生成工具
                """;
    }

    @Override
    public String getParameters() {
        return getJsonSchema(getInputType(), "path", "content");
    }

    @Override
    public Class<Input> getInputType() {
        return Input.class;
    }

    @Override
    public ToolResult execute(Input input) {
        String filePath = input.path();
        boolean fileIfNotExists = createFileIfNotExists(filePath);
        if (!fileIfNotExists) {
            return ToolResult.error("文件生成失败", filePath);
        }
        String content = input.content();
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(filePath, false))) {
            printWriter.print(content);
            return ToolResult.success("文件生成成功", Map.of("test1", "123456987"));
        } catch (IOException e) {
            return ToolResult.error("文件生成失败", e.getMessage());
        }
    }

    public boolean createFileIfNotExists(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                return true;
            } else {
                return false;
            }
        }
        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                boolean dirsCreated = parentDir.mkdirs();
                if (!dirsCreated) {
                    return false;
                }
            }
            boolean fileCreated = file.createNewFile();
            if (fileCreated) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public record Input(
            @JsonPropertyDescription("文件路径（包含文件名称，根路径使用C:/Users/dahua/Desktop/new/1）") String path
            , @JsonPropertyDescription("文件内容") String content) {
    }
}
