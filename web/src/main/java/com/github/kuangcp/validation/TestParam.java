package com.github.kuangcp.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
class TestParam {

  @Contain(value = ActiveState.class, message = ErrorMsgConstant.STATE_ERROR)
  private Integer state;

  @Contain(value = StrState.class, message = ErrorMsgConstant.STR_STATE_ERROR)
  private String str;
}
