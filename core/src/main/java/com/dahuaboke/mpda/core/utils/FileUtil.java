package com.dahuaboke.mpda.core.utils;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
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

    // 资源加载器（核心，适配classpath和本地文件）
    private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

    /**
     * 获取文件输入流（核心方法，统一处理classpath和本地文件）
     *
     * @param path 路径支持：
     *             - classpath资源：config/test.txt（等价于classpath:config/test.txt）
     *             - 本地文件：C:/test.txt 或 file:C:/test.txt 或 ./test.txt
     * @return 输入流
     * @throws IOException 读取失败时抛出
     */
    public static InputStream getFileInputStream(String path) throws IOException {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("File path is null or empty");
        }
        // 标准化路径：如果是本地绝对路径（Windows以盘符开头，Linux以/开头），自动补file:前缀
        String standardPath = standardizePath(path);
        // 加载资源
        Resource resource = RESOURCE_LOADER.getResource(standardPath);
        if (!resource.exists()) {
            throw new FileNotFoundException("File not found: " + path);
        }
        if (!resource.isReadable()) {
            throw new IOException("File can not read: " + path);
        }
        return resource.getInputStream();
    }

    /**
     * 读取文件内容为字符串（默认UTF-8编码）
     *
     * @param path 文件路径（同getFileInputStream）
     * @return 文件内容字符串
     * @throws IOException 读取失败时抛出
     */
    public static String getFileAsString(String path) throws IOException {
        try (InputStream inputStream = getFileInputStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            // 移除最后一个换行符，避免内容末尾多空行
            if (content.length() > 0) {
                content.deleteCharAt(content.length() - 1);
            }
            return content.toString();
        }
    }

    /**
     * 读取文件内容为字节数组
     *
     * @param path 文件路径（同getFileInputStream）
     * @return 字节数组
     * @throws IOException 读取失败时抛出
     */
    public static byte[] getFileAsBytes(String path) throws IOException {
        try (InputStream inputStream = getFileInputStream(path);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream.toByteArray();
        }
    }

    /**
     * 路径标准化处理：自动为本地绝对路径补file:前缀，兼容Windows/Linux
     *
     * @param path 原始路径
     * @return 标准化后的路径
     */
    private static String standardizePath(String path) {
        String trimPath = path.trim();
        // 已包含classpath:或file:前缀，直接返回
        if (trimPath.startsWith("classpath:") || trimPath.startsWith("file:")) {
            return trimPath;
        }
        // Windows绝对路径（以盘符开头，如C:/、D:\）
        if (trimPath.matches("^[A-Za-z]:[/\\\\].*")) {
            return "file:" + trimPath.replace("\\", "/");
        }
        // Linux绝对路径（以/开头）
        if (trimPath.startsWith("/")) {
            return "file:" + trimPath;
        }
        // 其他情况：视为classpath资源（相对路径）
        return trimPath;
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
                listFileSystemFiles(file, baseDir, filePaths);
            } else {
                String absolutePath = file.getAbsolutePath().replace("\\", "/");
                filePaths.add(absolutePath);
            }
        }
    }
}