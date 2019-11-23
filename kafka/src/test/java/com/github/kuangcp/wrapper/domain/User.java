package com.github.kuangcp.wrapper.domain;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author https://github.com/kuangcp on 2019-11-23 21:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

  private static final long serialVersionUID = 4902781473338262495L;

  private String name;

  private String nickName;

  private List<String> phones;

  private List<Book> books;

}
