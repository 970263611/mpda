package com.dahuaboke.mpda.core.utils;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtil {

    // Spring内置的资源加载器，全局唯一
    private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

    /**
     * 加载文件，返回输入流
     *
     * @param path 资源路径
     * @return 输入流
     * @throws IOException 资源不存在或读取失败时抛出
     */
    public static InputStream getFileInputStream(String path) throws IOException {
        Resource resource = RESOURCE_LOADER.getResource(path);
        // 校验资源是否存在
        if (!resource.exists()) {
            throw new IOException("File not found: " + path);
        }
        return resource.getInputStream();
    }

    /**
     * 加载文本文件，返回字符串
     *
     * @param path 资源路径
     * @return 文本内容（UTF-8编码）
     * @throws IOException 读取失败时抛出
     */
    public static String getFileAsString(String path) throws IOException {
        try (InputStream is = getFileInputStream(path)) {
            // Spring工具类快速将输入流转字符串，自动关闭流
            return new String(FileCopyUtils.copyToByteArray(is), StandardCharsets.UTF_8);
        }
    }

    /**
     * 加载文件，返回字节数组
     *
     * @param path 资源路径
     * @return 字节数组
     * @throws IOException 读取失败时抛出
     */
    public static byte[] getFileAsBytes(String path) throws IOException {
        try (InputStream is = getFileInputStream(path)) {
            return FileCopyUtils.copyToByteArray(is);
        }
    }

    public static List<String> listAllFiles(String dirPath) throws IOException {
        String normalizedDir = dirPath.trim().replace("\\", "/");
        if (!normalizedDir.endsWith("/")) {
            normalizedDir += "/";
        }
        Resource dirResource = RESOURCE_LOADER.getResource(normalizedDir);
        if (!dirResource.exists()) {
            return List.of();
        }
        List<String> filePaths = new ArrayList<>();
        try {
            URL url = dirResource.getURL();
            // 判断是JAR包环境还是文件系统环境
            if (url.getProtocol().equals("jar")) {
                // JAR包环境：解析JAR包内的目录
                listJarFiles(url, normalizedDir, filePaths);
            } else {
                // 开发环境：文件系统遍历
                listFileSystemFiles(new File(url.toURI()), normalizedDir, filePaths);
            }
        } catch (URISyntaxException e) {
            throw new IOException("Invalid resource URI: " + dirPath, e);
        }
        return filePaths;
    }


    /**
     * 遍历JAR包内指定目录下的所有文件
     */
    private static void listJarFiles(URL jarUrl, String dirPath, List<String> filePaths) throws IOException {
        JarURLConnection jarConn = (JarURLConnection) jarUrl.openConnection();
        try (JarFile jarFile = jarConn.getJarFile()) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                // 过滤：1. 是指定目录下的资源 2. 不是目录（只保留文件）
                if (entryName.startsWith(dirPath) && !entry.isDirectory()) {
                    filePaths.add(entryName);
                }
            }
        }
    }

    /**
     * 遍历文件系统中指定目录下的所有文件（开发环境）
     */
    private static void listFileSystemFiles(File dir, String baseDir, List<String> filePaths) {
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                // 递归遍历子目录
                listFileSystemFiles(file, baseDir, filePaths);
            } else {
                // 转换为相对于resources的路径
                String absolutePath = file.getAbsolutePath().replace("\\", "/");
                String resourcePath = absolutePath.substring(absolutePath.indexOf(baseDir));
                filePaths.add(resourcePath);
            }
        }
    }
}