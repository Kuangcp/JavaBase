package com.github.kuangcp.strcuture.stackapp;

import com.github.kuangcp.strcuture.stack.MythBaseStack;
import com.github.kuangcp.strcuture.stack.MythLinkedStack;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午3:02
 * TODO 对进栈的数据  输出所有的出栈结果
 */
@Data
class ShowStackPop {

  private MythBaseStack<Integer> dataStack = new MythLinkedStack<>();
  private MythBaseStack<Integer> status = new MythLinkedStack<>();

  private List<String> results = new ArrayList<>();

  void init() {
    for (int i = 0; i < 6; i++) {
      dataStack.push(i);
    }
  }

  void deal(String temp) {
    if (!this.status.isEmpty()) {
      temp += status.pop();
      deal(temp);
    }
    if (!this.dataStack.isEmpty()) {
      status.push(this.dataStack.pop());
      deal(temp);
    }
    if (status.isEmpty()) {
      results.add(temp);
    }

  }
}
