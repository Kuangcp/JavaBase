package com.github.kuangcp.wrapper.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author https://github.com/kuangcp on 2019-11-23 23:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

  private static final long serialVersionUID = -5993442476257577012L;

  private String name;

  private String type;
}
