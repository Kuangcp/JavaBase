package com.github.kuangcp.read;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * Created by https://github.com/kuangcp
 * 读取类  是一个工具类
 *
 * @author kuangcp
 * @date 18-5-21  下午2:14
 */
public class ClassScanner {
    private boolean checkInOrEx = true;
    private List<String> classFilters = null;
    private boolean excludeInner = true;

    public ClassScanner() {
    }

    public ClassScanner(Boolean excludeInner, Boolean checkInOrEx, List<String> classFilters) {
        this.excludeInner = excludeInner;
        this.checkInOrEx = checkInOrEx;
        this.classFilters = classFilters;
    }

    public List<String> getClassFilters() {
        return this.classFilters;
    }


    /**
     * 按正则匹配, 递归获取包下所有类
     * @param basePackage 包根路径
     * @param recursive 是否递归
     * @return 类对象集合
     */
    public Set<Class<?>> getPackageAllClasses(String basePackage, boolean recursive) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        String packageName = basePackage;
        if (basePackage.endsWith(".")) {
            packageName = basePackage.substring(0, basePackage.length()-1);
        }
        String package2Path = packageName.replace('.', '/');
        try {
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(package2Path);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    this.doScanPackageClassesByFile(classes, packageName, filePath, recursive);
                } else if ("jar".equals(protocol)) {
                    this.doScanPackageClassesByJar(packageName, url, recursive, classes);
                }
            }
            return classes;
        } catch (IOException var10) {
            var10.printStackTrace();
            throw new InternalError("扫描包失败");
        }
    }

    /**
     * 从文件中读取类
     * @param classes
     * @param packageName
     * @param packagePath
     * @param recursive
     */
    private void doScanPackageClassesByFile(Set<Class<?>> classes, String packageName, String packagePath, final boolean recursive) {
        File dir = new File(packagePath);
        if (dir.exists() && dir.isDirectory()) {
            File[] dirfiles = dir.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        return recursive;
                    } else {
                        String filename = file.getName();
                        return ClassScanner.this.excludeInner && filename.indexOf(36) != -1 ? false : ClassScanner.this.filterClassName(filename);
                    }
                }
            });
            if (dirfiles != null) {
                File[] var8 = dirfiles;
                int var9 = dirfiles.length;

                for (int var10 = 0; var10 < var9; ++var10) {
                    File file = var8[var10];
                    if (file.isDirectory()) {
                        this.doScanPackageClassesByFile(classes, packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
                    } else {
                        String className = file.getName().substring(0, file.getName().length() - 6);

                        try {
                            classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                        } catch (ClassNotFoundException var14) {
                            var14.printStackTrace();
                            throw new InternalError("过滤失败");
                        }
                    }
                }

            }
        }
    }

    /**
     * 从Jar中读取类
     * @param basePackage
     * @param url
     * @param recursive
     * @param classes
     */
    private void doScanPackageClassesByJar(String basePackage, URL url, boolean recursive, Set<Class<?>> classes) {
        String package2Path = basePackage.replace('.', '/');

        try {
            JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
            Enumeration entries = jar.entries();

            while (true) {
                String name;
                do {
                    JarEntry entry;
                    do {
                        do {
                            do {
                                if (!entries.hasMoreElements()) {
                                    return;
                                }

                                entry = (JarEntry) entries.nextElement();
                                name = entry.getName();
                            } while (!name.startsWith(package2Path));
                        } while (entry.isDirectory());
                    } while (!recursive && name.lastIndexOf(47) != package2Path.length());
                } while (this.excludeInner && name.indexOf(36) != -1);

                String classSimpleName = name.substring(name.lastIndexOf(47) + 1);
                if (this.filterClassName(classSimpleName)) {
                    String className = name.replace('/', '.');
                    className = className.substring(0, className.length() - 6);

                    try {
                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
                    } catch (ClassNotFoundException var14) {
                        var14.printStackTrace();
                        throw new InternalError("过滤失败");
                    }
                }
            }
        } catch (IOException var15) {
            var15.printStackTrace();
            throw new InternalError("扫描jar失败");
        }
    }

    private boolean filterClassName(String className) {
        if (!className.endsWith(".class")) {
            return false;
        } else if (null != this.classFilters && !this.classFilters.isEmpty()) {
            String tmpName = className.substring(0, className.length() - 6);
            boolean flag = false;
            Iterator var4 = this.classFilters.iterator();

            while (var4.hasNext()) {
                String str = (String) var4.next();
                String tmpreg = "^" + str.replace("*", ".*") + "$";
                Pattern p = Pattern.compile(tmpreg);
                if (p.matcher(tmpName).find()) {
                    flag = true;
                    break;
                }
            }
            return this.checkInOrEx && flag || !this.checkInOrEx && !flag;
        } else {
            return true;
        }
    }
}