package com.dahuaboke.mpda.core.agent.scene;

import com.dahuaboke.mpda.core.agent.chain.Chain;
import com.dahuaboke.mpda.core.agent.scene.entity.SceneResponse;
import com.dahuaboke.mpda.core.context.CoreContext;
import com.dahuaboke.mpda.core.exception.MpdaException;
import com.dahuaboke.mpda.core.exception.MpdaGraphException;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * auth: dahua
 * time: 2025/08/21 8:50
 */
public class SceneWrapper {

    private final String sceneId = UUID.randomUUID().toString();
    private final Chain chain;
    private final Scene scene;
    protected Set<SceneWrapper> childrenWrapper;

    protected SceneWrapper(Chain chain, Scene scene) {
        this.chain = chain;
        this.scene = scene;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getDescription() {
        return scene.description();
    }

    public String getSceneId() {
        return sceneId;
    }

    public Class<? extends Scene> getSceneClass() {
        return scene.getClass();
    }

    public void addChildWrapper(SceneWrapper childWrapper) {
        if (childrenWrapper == null) {
            childrenWrapper = new HashSet<>();
        }
        this.childrenWrapper.add(childWrapper);
    }

    public void init() throws MpdaGraphException {
        if (CollectionUtils.isNotEmpty(childrenWrapper) && scene != null && scene.prompt() != null) {
            scene.prompt().build(childrenWrapper.stream().collect(Collectors.toMap(child -> {
                if (child != null) {
                    return child.getSceneId();
                }
                return "";
            }, child -> {
                if (child != null) {
                    return child.getDescription();
                }
                return "";
            })));
        }
        this.chain.init();
    }

    public boolean isEnd() {
        return childrenWrapper == null;
    }

    public SceneResponse apply(CoreContext context) throws MpdaException {
        return chain.slide(context);
    }

    public Flux<SceneResponse> applyAsync(CoreContext context) throws MpdaException {
        return chain.slideAsync(context);
    }

    public SceneWrapper next(CoreContext context) throws MpdaException {
        return next(context, 0);
    }

    private SceneWrapper next(CoreContext context, int retry) throws MpdaException {
        SceneResponse execute = apply(context);
        String output = execute.output();
        if (output.startsWith("<think>")) {
            output = output.replaceFirst("(?s)<think>.*?</think>", "");
        }
        String finalExecute = output.trim();
        Optional<SceneWrapper> match = childrenWrapper.stream().filter(child -> child.getSceneId().equals(finalExecute)).findFirst();
        if (match.isPresent()) {
            return match.get();
        }
        retry++;
        if (retry >= 3) {
            return new UnknowWrapper();
        }
        return next(context, retry);
    }

    public static final class Builder {

        private Chain chain;
        private Scene scene;

        private Builder() {
        }

        public Builder chain(Chain chain) {
            this.chain = chain;
            return this;
        }

        public Builder scene(Scene scene) {
            this.scene = scene;
            return this;
        }

        public SceneWrapper build() {
            return new SceneWrapper(chain, scene);
        }
    }
}
