package jvm.oom;

import lombok.extern.slf4j.Slf4j;

/**
 * -Xss240k 设置栈大小, 所以设置的越大, 递归次数越大
 * 本地变量越多 栈深度越小
 * Java8 至少需要 228k 否则无法运行, 默认值似乎为 1024k
 *
 * @author kuangcp on 4/3/19-10:47 PM
 */
@Slf4j
public class JvmStackOverFlow {

  private int count = 1;

  private void stackLeak() {
    int a = 0;
    int b = 0;
    int c = 0;
    int d = 0;

    count++;
    stackLeak();
  }

  public static void main(String[] args) {
    JvmStackOverFlow overFlow = new JvmStackOverFlow();
    try {
      overFlow.stackLeak();

      // 不能用 Exception 因为 Error 和 Exception 是两个体系...
    } catch (Throwable e) {
      log.info("count={}", overFlow.count);
//      log.error(e.getMessage(), e);
    }
  }
}
