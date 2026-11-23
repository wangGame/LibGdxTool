package com.joker.discover;


import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassScanner {

    /**
     * 扫描指定包下的所有类名，包括子包
     */
    public static Set<String> scan(String packageName) {
        Set<String> classNames = new LinkedHashSet<>();
        String packagePath = packageName.replace('.', '/');

        ClassLoader loader = Thread.currentThread()
                .getContextClassLoader();

        if (loader == null) {
            loader = ClassScanner.class.getClassLoader();
        }

        // 1. 扫描 ClassLoader 可见的资源
        try {
            Enumeration<URL> urls = loader.getResources(packagePath);

            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();

                if ("file".equals(url.getProtocol())) {
                    File directory = new File(url.toURI());
                    scanDirectory(directory, packageName, classNames);

                } else if ("jar".equals(url.getProtocol())) {
                    JarURLConnection connection =
                            (JarURLConnection) url.openConnection();

                    try (JarFile jar = connection.getJarFile()) {
                        scanJar(jar, packagePath, classNames);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("扫描资源失败", e);
        }

        // 2. 扫描 java.class.path，补充没有包目录资源的 JAR
        String classPath = System.getProperty("java.class.path", "");

        for (String entry : classPath.split(
                java.util.regex.Pattern.quote(File.pathSeparator))) {

            if (entry.isEmpty()) continue;

            File file = new File(entry);

            if (file.isDirectory()) {
                File directory = new File(file, packagePath);
                scanDirectory(directory, packageName, classNames);

            } else if (file.isFile()
                    && entry.toLowerCase().endsWith(".jar")) {

                try (JarFile jar = new JarFile(file)) {
                    scanJar(jar, packagePath, classNames);
                } catch (IOException e) {
                    throw new RuntimeException(
                            "扫描 JAR 失败: " + file, e);
                }
            }
        }

        return classNames;
    }

    /**
     * 递归扫描目录
     */
    private static void scanDirectory(
            File directory,
            String packageName,
            Set<String> result) {

        if (!directory.isDirectory()) return;

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {

                scanDirectory(
                        file,
                        packageName + "." + file.getName(),
                        result
                );

            } else if (file.getName().endsWith(".class")) {

                String simpleName = file.getName()
                        .substring(0, file.getName().length() - 6);

                if (simpleName.equals("module-info")
                        || simpleName.equals("package-info")) {
                    continue;
                }

                result.add(packageName + "." + simpleName);
            }
        }
    }

    /**
     * 扫描 JAR 包
     */
    private static void scanJar(
            JarFile jar,
            String packagePath,
            Set<String> result) {

        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            if (entry.isDirectory()) continue;

            if (!name.startsWith(packagePath + "/")) continue;
            if (!name.endsWith(".class")) continue;

            if (name.endsWith("/module-info.class")
                    || name.endsWith("/package-info.class")) {
                continue;
            }

            // 避免把多版本 JAR 的特殊条目当作普通类
            if (name.startsWith("META-INF/versions/")) continue;

            String className = name
                    .substring(0, name.length() - 6)
                    .replace('/', '.');

            result.add(className);
        }
    }

    /**
     * 将扫描到的类名加载为 Class 对象
     * 不触发静态初始化
     */
    public static Class<?> loadClass(String className)
            throws ClassNotFoundException {

        ClassLoader loader = Thread.currentThread()
                .getContextClassLoader();

        if (loader == null) {
            loader = ClassScanner.class.getClassLoader();
        }

        return Class.forName(className, false, loader);
    }

    public static void main(String[] args) {

        Set<String> names = scan("com.libGdx.test");

        System.out.println("扫描到 " + names.size() + " 个类");

        for (String name : names) {
            System.out.println(name);
        }
    }
}
