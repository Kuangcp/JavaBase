package com.github.kuangcp.util;

import java.util.List;

/**
 * Created by https://github.com/kuangcp on 18-2-28  下午10:01
 *
 * @author kuangcp
 */
public class ImportUtil {

  public static <T extends BaseInterface> List<T> getList(Class<T> target) {

    return null;
  }

  public static void main(String[] s) {
    List<Entity> list = getList(Entity.class);
    String name = list.get(0).getName();
  }
}
