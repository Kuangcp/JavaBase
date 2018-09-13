package com.github.kuangcp.serialize.json.speed;

import com.github.kuangcp.serialize.binary.Person;
import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public interface JsonTool {

  void read();

  void write(int total, List<Person> dataList);

  String getName();

}
