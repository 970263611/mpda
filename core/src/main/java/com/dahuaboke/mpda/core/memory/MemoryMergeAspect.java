package com.dahuaboke.mpda.core.memory;

import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.context.CacheManager;
import com.dahuaboke.mpda.core.context.consts.Constants;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * auth: dahua
 * time: 2025/9/20 11:16
 */
@Aspect
@Component
public class MemoryMergeAspect {

    @Autowired
    private CacheManager cacheManager;

    @Pointcut("@annotation(com.dahuaboke.mpda.core.memory.MemoryMerge)")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        MemoryMerge memoryMerge = method.getAnnotation(MemoryMerge.class);
        Class<? extends Scene>[] value = memoryMerge.value();
        if (value != null && value.length > 0) {
            List<String> sceneIds = Arrays.stream(value).map(clz -> cacheManager.getSceneIdBySceneClass(clz)).toList();
            cacheManager.getAttribute().put(Constants.SCENE_MERGE, sceneIds);
        }
    }
}
