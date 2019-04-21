package com.github.kuangcp.serialize.json.speed;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.List;

/**
 * Created by https://github.com/kuangcp
 *
 * @author kuangcp
 */
public interface JsonTool<T> {

  void fromJSON(String json, Class<T> target) throws IOException;

  void toJSON(List<T> dataList) throws JsonProcessingException;

  String getName();
}
