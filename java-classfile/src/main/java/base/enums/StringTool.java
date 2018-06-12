package base.enums;

/**
 * Created by https://github.com/kuangcp
 * 枚举类实现单例模式, 适用于单例外部对象, 如果单例自身,
 * @author kuangcp
 */
public enum StringTool {

  INSTANCE(new TransString());

  private TransString tool;

  StringTool(TransString tool) {

    this.tool = tool;
  }

  public TransString getTool() {
    return tool;
  }

  public void setTool(TransString tool) {
    this.tool = tool;
  }
}

class TransString {

  public String up(String target) {
    return target.toUpperCase();
  }
}