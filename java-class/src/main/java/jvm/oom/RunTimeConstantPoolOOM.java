package jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * -XX:PermSize=10M -XX:MaxPermSize=10M
 *
 * Java8: -XX:MetaspaceSize=10m  -XX:MaxMetaspaceSize=10m
 *
 * TODO 元空间的参数限制没有起作用
 * 同样的在Docker中运行, 同样的被Killed
 *
 * @author kuangcp on 4/4/19-12:20 AM
 */
public class RunTimeConstantPoolOOM {

  public static void main(String[] args) {
    List<String> data = new ArrayList<>();
    int i = 0;
    while (true) {
      data.add(String.valueOf(i).intern());
    }
  }
}
