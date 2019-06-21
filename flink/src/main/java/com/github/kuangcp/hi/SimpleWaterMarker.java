package com.github.kuangcp.hi;

import javax.annotation.Nullable;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

/**
 * @author https://github.com/kuangcp
 * @date 2019-06-21 11:47
 */
public class SimpleWaterMarker implements AssignerWithPeriodicWatermarks {

  @Nullable
  @Override
  public Watermark getCurrentWatermark() {
    return null;
  }

  @Override
  public long extractTimestamp(Object element, long previousElementTimestamp) {
    return 0;
  }
}
