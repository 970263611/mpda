package com.dahuaboke.mpda.core.code;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 2025/9/26 9:22
 * auth: dahua
 * desc:
 */
public class CodeBuilder {

    private static final Logger logger = LoggerFactory.getLogger(CodeBuilder.class);

    public static void main(String[] args) throws IOException {
        String projectName = "requirement";
        String packageName = "requirement";
        String sceneName = "value";

        String path = "C:/Users/dahua/Desktop/mpda/requirement/src/main/java/com/dahuaboke/mpda/requirement/scenes/requirement/" + sceneName;
        create(projectName, packageName, sceneName, path);
    }

    public static void create(String projectName, String packageName, String scene, String path) throws IOException {
        scene = firstToUpper(scene);
        if (!path.endsWith("/")) {
            path += "/";
        }
        createDir(path);
        String sceneFilename = path + scene + "Scene.java";
        String promptFilename = path + scene + "AgentPrompt.java";
        String graphFilename = path + scene + "Graph.java";
        String edgePath = path + "/edge/";
        String edgeFilename = edgePath + scene + "Dispatcher.java";
        createFile(sceneFilename);
        String content1 = sceneTemplate(projectName, packageName, scene, "\"描述\"");
        writeContent(sceneFilename, content1);
        createFile(promptFilename);
        String content2 = promptTemplate(projectName, packageName, scene, "\"提示词\"");
        writeContent(promptFilename, content2);
        createFile(graphFilename);
        String content3 = graphTemplate(projectName, packageName, scene);
        writeContent(graphFilename, content3);
        createDir(edgePath);
        createFile(edgeFilename);
        String content4 = dispatcherTemplate(projectName, packageName, scene);
        writeContent(edgeFilename, content4);
    }

    public static String firstToUpper(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        if (Character.isUpperCase(str.charAt(0))) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String firstToLower(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        if (Character.isLowerCase(str.charAt(0))) {
            return str;
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public static void createDir(String dir) throws IOException {
        Path path = Paths.get(dir);
        if (Files.exists(path)) {
            return;
        }
        Files.createDirectories(path);
    }

    public static void createFile(String filename) throws IOException {
        File file = new File(filename);
        file.createNewFile();
    }

    public static void writeContent(String filename, String content) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(filename, false))) {
            printWriter.print(content);
        } catch (IOException e) {
            logger.error("create file error {} {}", filename, content, e);
        }
    }

    public static String replacePlaceholders(String template, Object... params) {
        if (template == null || params == null || params.length == 0) {
            return template;
        }
        String result = template;
        for (int i = 0; i < params.length; i++) {
            String placeholder = "{" + i + "}";
            String value = params[i] != null ? params[i].toString() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }

    public static String sceneTemplate(String projectName, String packageName, String scene, String description) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m");
        String now = LocalDateTime.now().format(formatter);
        String template =
                """
                        package com.dahuaboke.mpda.{0}.scenes.{1}.{2};
                        
                        
                        import com.dahuaboke.mpda.{3}.scenes.resolution.ResolutionScene;
                        import com.dahuaboke.mpda.core.agent.graph.Graph;
                        import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
                        import com.dahuaboke.mpda.core.agent.scene.Scene;
                        import org.springframework.beans.factory.annotation.Autowired;
                        import org.springframework.stereotype.Component;
                        
                        /**
                         * auth: dahua
                         * time: {4}
                         */
                        @Component
                        public class {5}Scene implements Scene {
                        
                            @Autowired
                            private {6}Graph {7}Graph;
                        
                            @Autowired
                            private {8}AgentPrompt {9}AgentPrompt;
                        
                            @Override
                            public String description() {
                                return {10};
                            }
                        
                            @Override
                            public Graph graph() {
                                return {11}Graph;
                            }
                        
                            @Override
                            public AgentPrompt prompt() {
                                return {12}AgentPrompt;
                            }
                        
                            @Override
                            public Class<? extends Scene> parent() {
                                return ResolutionScene.class;
                            }
                        }
                        
                        """;
        return replacePlaceholders(template, projectName, packageName, firstToLower(scene), projectName, now,
                scene, scene, firstToLower(scene), scene, firstToLower(scene), description, firstToLower(scene), firstToLower(scene));
    }

    public static String promptTemplate(String projectName, String packageName, String scene, String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m");
        String now = LocalDateTime.now().format(formatter);
        String template =
                """
                        package com.dahuaboke.mpda.{0}.scenes.{1}.{2};
                        
                        
                        import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
                        import org.springframework.stereotype.Component;
                        
                        import java.util.Map;
                        
                        /**
                         * auth: dahua
                         * time: {3}
                         */
                        @Component
                        public class {4}AgentPrompt implements AgentPrompt {
                        
                            private final String prompt = {5};
                        
                            @Override
                            public String description() {
                                return this.prompt;
                            }
                        
                            @Override
                            public void build(Map params) {
                            }
                        }
                        
                        """;
        return replacePlaceholders(template, projectName, packageName, firstToLower(scene), now, scene, prompt);
    }

    public static String graphTemplate(String projectName, String packageName, String scene) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m");
        String now = LocalDateTime.now().format(formatter);
        String template = """
                package com.dahuaboke.mpda.{0}.scenes.{1}.{2};
                
                
                import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
                import com.alibaba.cloud.ai.graph.StateGraph;
                import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
                import com.alibaba.cloud.ai.graph.exception.GraphStateException;
                import com.dahuaboke.mpda.core.agent.graph.AbstractGraph;
                import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
                import com.dahuaboke.mpda.core.exception.MpdaGraphException;
                import com.dahuaboke.mpda.core.exception.MpdaRuntimeException;
                import com.dahuaboke.mpda.core.node.HumanNode;
                import com.dahuaboke.mpda.core.node.LlmNode;
                import com.dahuaboke.mpda.core.node.StreamLlmNode;
                import com.dahuaboke.mpda.core.node.ToolNode;
                import com.dahuaboke.mpda.{3}.scenes.{4}.{5}.edge.{6}Dispatcher;
                import org.springframework.beans.factory.annotation.Autowired;
                import org.springframework.stereotype.Component;
                import reactor.core.publisher.Flux;
                
                import java.util.Map;
                
                import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
                import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;
                
                /**
                 * auth: dahua
                 * time: {7}
                 */
                @Component
                public class {8}Graph extends AbstractGraph {
                
                    @Autowired
                    private {9}Dispatcher {10}Dispatcher;
                
                    @Autowired
                    private LlmNode llmNode;
                
                    @Autowired
                    private StreamLlmNode streamLlmNode;
                
                    @Autowired
                    private HumanNode humanNode;
                
                    @Autowired
                    private ToolNode toolNode;
                
                    @Override
                    public Map<Object, StateGraph> buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException {
                        try {
                            StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                                    .addNode("llm", node_async(llmNode))
                                    .addNode("streamLlmNode", node_async(streamLlmNode))
                                    .addNode("human", node_async(humanNode))
                                    .addNode("tool", node_async(toolNode))
                
                                    .addEdge(StateGraph.START, "llm")
                                    .addConditionalEdges("llm", edge_async({11}Dispatcher),
                                            Map.of("go_human", "human", "go_tool", "tool"))
                                    .addEdge("tool", "streamLlmNode")
                                    .addEdge("human", StateGraph.END)
                                    .addEdge("streamLlmNode", StateGraph.END);
                            return Map.of("default", stateGraph);
                        } catch (GraphStateException e) {
                            throw new MpdaGraphException(e);
                        }
                    }
                
                    @Override
                    public SceneResponse execute(Map<String, Object> attribute) throws MpdaRuntimeException {
                        try {
                            return response(attribute, "default");
                        } catch (GraphRunnerException e) {
                            throw new MpdaRuntimeException(e);
                        }
                    }
                
                    @Override
                    public Flux<SceneResponse> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
                        try {
                            return streamResponse(attribute, "default");
                        } catch (GraphRunnerException e) {
                            throw new MpdaRuntimeException(e);
                        }
                    }
                }
                
                """;
        return replacePlaceholders(template, projectName, packageName, firstToLower(scene), projectName,
                packageName, firstToLower(scene), scene, now, scene, scene, firstToLower(scene), firstToLower(scene));
    }

    public static String dispatcherTemplate(String projectName, String packageName, String scene) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d H:m");
        String now = LocalDateTime.now().format(formatter);
        String template = """
                package com.dahuaboke.mpda.{0}.scenes.{1}.{2}.edge;
                
                
                import com.alibaba.cloud.ai.graph.OverAllState;
                import com.alibaba.cloud.ai.graph.action.EdgeAction;
                import org.springframework.stereotype.Component;
                
                /**
                 * auth: dahua
                 * time: {3}
                 */
                @Component
                public class {4}Dispatcher implements EdgeAction {
                
                    @Override
                    public String apply(OverAllState state) throws Exception {
                        return "";
                    }
                }
                
                """;
        return replacePlaceholders(template, projectName, packageName, firstToLower(scene), now, scene);
    }
}
