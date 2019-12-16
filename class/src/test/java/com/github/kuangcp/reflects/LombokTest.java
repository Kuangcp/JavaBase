package com.github.kuangcp.reflects;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author https://github.com/kuangcp on 2019-12-15 22:42
 */
public class LombokTest {

  @Test
  public void testHashCode() throws Exception {
    App app = new App();
    System.out.println(app);
    System.out.println(app.hashCode());

    // hash use all field
    // https://projectlombok.org/features/EqualsAndHashCode
    Table table = new Table();
    System.out.println(table);
    System.out.println(table.hashCode());
  }
}


@ToString
@Slf4j
class App {

  private String name;

  public String getName() {
    log.info("getName");
    return name;
  }

  public void setName(String name) {
    log.info("setName");
    this.name = name;
  }
}

@Data
@Slf4j
class Table {

  private String name;

  public String getName() {
    log.info("getName");
    return name;
  }

  public void setName(String name) {
    log.info("setName");
    this.name = name;
  }
}