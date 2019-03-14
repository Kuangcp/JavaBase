package com.github.kuangcp.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author kuangcp on 3/14/19-11:51 AM
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Junior extends Student {

  private String classes;
}
