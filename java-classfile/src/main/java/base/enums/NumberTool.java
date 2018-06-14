package base.enums;

/**
 * Created by https://github.com/kuangcp
 * 枚举实现该类的单例模式
 * @author kuangcp
 */
public enum NumberTool {

  INSTANCE(1, 2);
  private int max;
  private int min;

  NumberTool(int max, int min) {
    this.max = max;
    this.min = min;
  }

  public int getMax() {
    return max;
  }

  public NumberTool setMax(int max) {
    this.max = max;
    return this;
  }

  public int getMin() {
    return min;
  }

  public void setMin(int min) {
    this.min = min;
  }

  @Override
  public String toString() {
    return "NumberTool{" +
        "max=" + max +
        ", min=" + min +
        '}';
  }
}
