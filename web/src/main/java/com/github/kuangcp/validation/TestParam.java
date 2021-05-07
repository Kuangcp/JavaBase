package com.github.kuangcp.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
class TestParam {

  @Contain(value = ActiveState.class, message = "状态不合法")
  private Integer state;

  @Contain(value = StrState.class, message = "状态str不合法")
  private String str;
}
