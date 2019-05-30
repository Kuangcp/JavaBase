package jvm.oom;

/**
 * -Xss2M
 *
 *  在容器中运行 结果竟然是 内存满载一段时间后 终端输出了 Killed .. 系统并没有卡死
 * TODO 可能 Windows 会卡死吧
 *
 * @author kuangcp on 4/3/19-11:10 PM
 */
public class JvmStackOOM {

  private void doLoop() {
    while (true) {
    }
  }

  private void stackLeakByThread() {
    while (true) {
      new Thread(this::doLoop);
    }
  }

  public static void main(String[] args) {
    JvmStackOOM oom = new JvmStackOOM();
    oom.stackLeakByThread();
  }
}
