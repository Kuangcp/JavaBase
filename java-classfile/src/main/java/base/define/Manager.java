package base.define;

import java.util.Objects;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public class Manager {

  public static Manager manager;
  private String name;
  static {
    manager = new Manager();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Manager manager = (Manager) o;
    return Objects.equals(name, manager.name);
  }

  @Override
  public int hashCode() {

    return Objects.hash(name);
  }
}
