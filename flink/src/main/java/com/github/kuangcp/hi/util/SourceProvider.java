package com.github.kuangcp.hi.util;

import com.github.kuangcp.hi.domain.CalculateVO;
import java.util.List;

/**
 * @author https://github.com/kuangcp
 * 2019-06-16 15:12
 */
public class SourceProvider {


  private int count = 10;

  private int cursor;

  public SourceProvider(CalculateVO calculateVO) {
  }

  public boolean hasNextPage() {
    return cursor <= count;
  }

  public List<String> generateResource() {
    return null;
  }
}
