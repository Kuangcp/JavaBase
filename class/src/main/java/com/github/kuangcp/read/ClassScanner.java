package com.github.kuangcp.read;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp 读取类  是一个工具类
 *
 * @author kuangcp
 */
@Slf4j
@NoArgsConstructor
public class ClassScanner {

  private static final String suffix = ".class";

  private boolean checkInOrEx;
  private List<String> classFilters;
  private boolean excludeInner;
  private boolean hasEnterEntry;

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
   *
   * @param basePackage 包根路径
   * @param recursive 是否递归
   * @return 类对象集合
   */
  public Set<Class<?>> getPackageAllClasses(String basePackage, boolean recursive) {
    Set<Class<?>> classes = new LinkedHashSet<>();
    String packageName = basePackage;
    if (basePackage.endsWith(".")) {
      packageName = basePackage.substring(0, basePackage.length() - 1);
    }

    String package2Path = packageName.replace('.', '/');
    try {
      Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader()
          .getResources(package2Path);
      while (dirs.hasMoreElements()) {
        URL url = dirs.nextElement();
        String protocol = url.getProtocol();
        if ("file".equals(protocol)) {
          String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.name());
          this.doScanPackageClassesByFile(classes, packageName, filePath, recursive);
        } else if ("jar".equals(protocol)) {
          this.scanPackageClassesByJar(packageName, url, classes);
        }
      }
      return classes;
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new InternalError("扫描包失败");
    }
  }

  /**
   * 从文件中读取类
   */
  private void doScanPackageClassesByFile(
      Set<Class<?>> classes,
      String packageName,
      String packagePath,
      final boolean recursive) {

    File dir = new File(packagePath);
    if (!dir.exists() || !dir.isDirectory()) {
      return;
    }

    File[] files = dir.listFiles(file -> {
      if (file.isDirectory()) {
        return recursive;
      } else {
        String filename = file.getName();
        return (!ClassScanner.this.excludeInner || filename.indexOf(36) == -1)
            && ClassScanner.this.filterClassName(filename);
      }
    });

    if (Objects.isNull(files)) {
      return;
    }

    for (File file : files) {
      if (file.isDirectory()) {
        this.doScanPackageClassesByFile(classes, packageName + "." + file.getName(),
            file.getAbsolutePath(), recursive);
      } else {
        String className = file.getName().substring(0, file.getName().length() - suffix.length());

        try {
          String classPath = packageName + '.' + className;
          Class<?> target = Thread.currentThread().getContextClassLoader().loadClass(classPath);
          classes.add(target);
        } catch (ClassNotFoundException e) {
          log.error(e.getMessage(), e);
          throw new InternalError("过滤失败");
        }
      }
    }
  }

  /**
   * 从Jar中读取类
   */
  private void scanPackageClassesByJar(String basePackage, URL url, Set<Class<?>> classes) {
    String package2Path = basePackage.replace('.', '/');

    try {
      JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
      Enumeration entries = jar.entries();

      scanEntry(package2Path, classes, entries);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new InternalError("扫描jar失败");
    }
  }

  private void scanEntry(String packagePath, Set<Class<?>> classes, Enumeration entries) {
    if (!entries.hasMoreElements()) {
      return;
    }

    JarEntry entry = (JarEntry) entries.nextElement();
    String name = entry.getName();

    String classSimpleName = name.substring(name.lastIndexOf('/') + 1);
    if (this.filterClassName(classSimpleName) && name.startsWith(packagePath)) {
      String className = name.replace('/', '.');
      hasEnterEntry = true;
      className = className.substring(0, className.length() - suffix.length());
      try {
        classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
      } catch (ClassNotFoundException e) {
        log.error(e.getMessage(), e);
        throw new RuntimeException("过滤失败");
      }
    }

    // 已经遍历完目标目录，无需继续递归
    if (hasEnterEntry && !name.startsWith(packagePath)) {
      return;
    }
    scanEntry(packagePath, classes, entries);
  }

  private boolean filterClassName(String className) {
    if (!className.endsWith(suffix)) {
      return false;
    }

    if (Objects.isNull(classFilters) || this.classFilters.isEmpty()) {
      return true;
    }

    String tmpName = className.substring(0, className.length() - 6);
    boolean matchFlag = this.classFilters.stream()
        .anyMatch(v -> {
          String reg = "^" + v.replace("*", ".*") + "$";
          Pattern p = Pattern.compile(reg);
          return p.matcher(tmpName).find();
        });

    return this.checkInOrEx && matchFlag || !this.checkInOrEx && !matchFlag;
  }
}