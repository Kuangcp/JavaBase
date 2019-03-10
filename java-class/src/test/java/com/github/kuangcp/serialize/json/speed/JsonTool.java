package com.github.kuangcp.serialize.json.speed;

import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public interface JsonTool<T> {

  void read();

  void write(List<T> dataList);

  String getName();
}
