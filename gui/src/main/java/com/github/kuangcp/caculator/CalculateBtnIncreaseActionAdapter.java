package com.github.kuangcp.caculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/19/19-1:02 AM
 */
class CalculateBtnIncreaseActionAdapter implements ActionListener {

  private final Calculator adapter;

  CalculateBtnIncreaseActionAdapter(Calculator adapter) {
    this.adapter = adapter;
  }

  public void actionPerformed(ActionEvent e) {
    adapter.btnIncreaseActionPerformed(e);
  }
}
