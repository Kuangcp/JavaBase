package com.github.kuangcp.caculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/19/19-1:02 AM
 */
class Calculate_btnIncrease_actionAdapter implements ActionListener {

  private Calculator adapter;

  Calculate_btnIncrease_actionAdapter(Calculator adapter) {
    this.adapter = adapter;
  }

  public void actionPerformed(ActionEvent e) {
    adapter.btnIncrease_actionPerformed(e);
  }
}
