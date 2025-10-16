package com.dahuaboke.mpda.core.rag.reader;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Desc: 本地/远程文件资源路径 读取到Resource
 * @Author：zhh
 * @Date：2025/9/1 22:50
 */
@Component
public class DefaultDocumentReader implements DocumentReader {

    @Override
    public Resource[] read(String path) throws IOException {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return new UrlResource[]{new UrlResource(path)};
        } else {
            //相对路径转为file:前缀
           /* if (!path.startsWith("file:") && !path.startsWith("/")) {
                String os = System.getProperty("os.name").toLowerCase();
                if(os.contains("win")) {
                    path = "file:" + path.replace("\\","/");
                }else {
                    path = "file:" + path;
                }
            }*/
            FileSystemResource fileSystemResource = new FileSystemResource(path);
            return new FileSystemResource[]{fileSystemResource};
        }
    }
}
