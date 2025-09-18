package com.dahuaboke.mpda.core.agent.tools;


import com.dahuaboke.mpda.core.exception.MpdaIllegalArgumentException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.BiFunction;

/**
 * auth: dahua
 * time: 2025/8/22 10:53
 */
public abstract class AbstractBaseTool<I> implements BiFunction<I, ToolContext, ToolResult> {

    @Autowired
    protected ObjectMapper objectMapper;

    @Override
    public ToolResult apply(I input, ToolContext toolContext) {
        return execute(objectMapper.convertValue(input, getInputType()));
    }

    public String getName() {
        String simpleName = getClass().getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    abstract public String getDescription();

    abstract public String getParameters();

    abstract public Class<I> getInputType();

    abstract public ToolResult execute(I input);

    protected String getJsonSchema(Class<I> clz, String... required) {
        try {
            JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator(objectMapper);
            JsonSchema schema = schemaGenerator.generateSchema(clz);
            JsonNode schemaNode = objectMapper.valueToTree(schema);
            if (required != null && required.length > 0) {
                ArrayNode requiredArray = objectMapper.createArrayNode();
                for (String s : required) {
                    requiredArray.add(s);
                }
                ((ObjectNode) schemaNode).set("required", requiredArray);
            }
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaNode);
        } catch (JsonProcessingException e) {
            throw new MpdaIllegalArgumentException(e);
        }
    }
}
