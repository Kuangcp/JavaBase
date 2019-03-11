package syntax.innerclass;

import lombok.Data;

/**
 * @author kuangcp on 3/1/19-4:13 PM
 * https://www.tutorialspoint.com/java/java_innerclasses.htm
 * https://www.geeksforgeeks.org/anonymous-inner-class-java/
 */
@Data
public class ServerConfig {

  private String host;
  private Integer port;

  public ServerConfig(ServerConfig.Builder builder) {
    this.host = builder.host;
    this.port = builder.port;
  }

  // 如果没有这个方法, 内部类就无需 static 声明
  // TODO 原因
  public static ServerConfig defaultConfig() {
    return new ServerConfig.Builder().build();
  }

  public static class Builder {

    private String host;
    private Integer port;

    public Builder setHost(String host) {
      this.host = host;
      return this;
    }

    public Builder setPort(Integer port) {
      this.port = port;
      return this;
    }

    public ServerConfig build() {
      return new ServerConfig(this);
    }
  }

}
