package com.github.kuangcp.caculator;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * 搜到的一个计算器实现代码 TODO 思考如何重构
 */
@Slf4j
public class Calculator extends JFrame {

  private static final short width = 47;
  private static final short height = 38;
  private static final Font dialogFont = new Font("Dialog", Font.PLAIN, 16);

  private String front = "";
  private String behind = ""; //分别用于记录加减乘除运算符之前,之后输入的内容
  private String operator; //用于记录运算符
  private String result;//用于存储运算结果的字符串格式
  private boolean flag = false; //用于记录是否按下了运算符
  private boolean dotFlag = false;//用于判断是否输入了点运算符
  private boolean numFlag = false;//用于判断是否输入了数字
  private boolean calculateFlag = false;//用于判断是否按下了等号运算符

  private final JTextField txtResult = new JTextField("0");
  private final JTextField inputCache = new JTextField("");
  private final JButton btnNull = new JButton("  ");

  private final JButton btnDecrease = new JButton("-");
  private final JButton btnBegin = new JButton("C");

  private final JButton btnMultiply = new JButton("*");
  private final JButton btnCancel = new JButton("←");

  private final JButton btnMinus = new JButton("+/-");
  private final JButton btnPoint = new JButton(".");
  private final JButton btnDivide = new JButton("/");
  private final JButton btnEqual = new JButton("=");
  private final JButton btnIncrease = new JButton("+");

  private Calculator() {
    try {
      rootPane.setLayout(null);
      this.setResizable(false);
      setSize(new Dimension(400, 300));
      setTitle("计算器");

      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      initLogicButton();
      initNumberButton();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * 初始化数字按钮
   */
  private void initNumberButton() {
    int startX = 30;
    int startY = 170;
    int deltaW = 70;
    int deltaH = 50;
    JButton[] numberButtons = new JButton[10];
    for (int i = 1; i < 10; i++) {
      JButton button = new JButton(String.valueOf(i));
      int x = startX + ((i - 1) % 3) * deltaW;
      int y = startY - ((i - 1) / 3) * deltaH;
      button.setBounds(new Rectangle(x, y, width, height));
      numberButtons[i] = button;
    }

    JButton btnZero = new JButton("0");
    btnZero.setBounds(new Rectangle(startX, startY + deltaH, width, height));
    numberButtons[0] = btnZero;

    //加载数字0-9的监听事件
    bindListener(numberButtons);
    configFont(numberButtons);
    showButton(numberButtons);
  }

  private void initLogicButton() {
    txtResult.setEnabled(false);
    txtResult.setEditable(false);
    txtResult.setHorizontalAlignment(SwingConstants.RIGHT);
    txtResult.setBounds(new Rectangle(33, 19, 310, 34));

    inputCache.setEnabled(false);
    inputCache.setEditable(false);
    inputCache.setHorizontalAlignment(SwingConstants.RIGHT);
    inputCache.setBounds(new Rectangle(27, 19, 310, 8));

    btnNull.setBounds(new Rectangle(298, 70, width, height));
    btnNull.setFont(new Font("Dialog", Font.PLAIN, 12));

    btnDecrease.setBounds(new Rectangle(234, 120, width, height));

    btnBegin.setBounds(new Rectangle(298, 121, width, height));

    btnBegin.addActionListener(new CalculateBtnBeginActionAdapter(this));

    btnMultiply.setBounds(new Rectangle(234, 172, width, height));

    btnCancel.setBounds(new Rectangle(298, 172, width, height));
    btnCancel.setFont(new Font("Dialog", Font.PLAIN, 12));

    btnCancel.addActionListener(new CalculateBtnCancelActionAdapter(this));

    configFont(txtResult, btnDecrease, btnBegin, btnMultiply, btnDivide, btnIncrease, btnEqual);

    btnMinus.setBounds(new Rectangle(101, 222, width, height));
    btnMinus.setFont(new Font("Dialog", Font.PLAIN, 10));

    btnMinus.addActionListener(new CalculateBtnMinusActionAdapter(this));
    btnPoint.setBounds(new Rectangle(167, 222, width, height));
    btnPoint.setFont(new Font("Dialog", Font.PLAIN, 30));
    btnPoint.setHorizontalTextPosition(SwingConstants.CENTER);

    btnPoint.addActionListener(new CalculateBtnPointActionAdapter(this));
    btnDivide.setBounds(new Rectangle(234, 222, width, height));

    btnEqual.setBounds(new Rectangle(298, 222, width, height));

    btnEqual.addActionListener(new CalculateBtnEqualActionAdapter(this));
    btnIncrease.setBounds(new Rectangle(234, 70, width, height));

    //加载加减乘除运算符的监听事件
    btnIncrease.addActionListener(new CalculateBtnIncreaseActionAdapter(this));
    btnDecrease.addActionListener(new CalculateBtnIncreaseActionAdapter(this));
    btnMultiply.addActionListener(new CalculateBtnIncreaseActionAdapter(this));
    btnDivide.addActionListener(new CalculateBtnIncreaseActionAdapter(this));

    showButton(btnDecrease, btnBegin, btnMultiply, btnCancel,
        btnMinus, btnPoint, btnDivide, btnEqual, btnIncrease, btnNull);

    rootPane.add(txtResult);
  }

  private void configFont(JComponent... components) {
    if (Objects.isNull(components)) {
      return;
    }
    for (JComponent component : components) {
      component.setFont(dialogFont);
    }
  }

  private void bindListener(JButton... buttons) {
    if (Objects.isNull(buttons)) {
      return;
    }
    for (JButton button : buttons) {
      if (Objects.isNull(button)) {
        continue;
      }
      button.addActionListener(new CalculateBtnZeroActionAdapter(this));
    }
  }

  private void showButton(JButton... buttons) {
    if (Objects.isNull(buttons)) {
      return;
    }
    if (Objects.isNull(rootPane)) {
      return;
    }
    for (JButton button : buttons) {
      if (Objects.isNull(button)) {
        continue;
      }

      rootPane.add(button);
    }
  }

  void btnZeroActionPerformed(ActionEvent e) {
    if (flag) { //如果刚刚按下了运算符
      txtResult.setText("");
      if (dotFlag) {//判断之前是否输入了点运算符
        txtResult.setText("0." + e.getActionCommand());
        dotFlag = false;
      } else {
        txtResult.setText(e.getActionCommand());
      }
      numFlag = true;
    } else {
      int num = txtResult.getText().indexOf(".");
      if (num < 0 && !txtResult.getText().equals("0")) {
        txtResult.setText(txtResult.getText() + e.getActionCommand());
      } else if (num < 0 && txtResult.getText().equals("0")) {
        txtResult.setText(e.getActionCommand());
      } else if (num >= 0 && txtResult.getText().equals("0")) {
        txtResult.setText("0." + e.getActionCommand());
      } else if (num >= 0 && !txtResult.getText().equals("0")) {
        txtResult.setText(txtResult.getText() + e.getActionCommand());
      }
    }
    flag = false;
    calculateFlag = false;
  }

  void btnIncreaseActionPerformed(ActionEvent e) {
    if (calculateFlag) {
      txtResult.setText(txtResult.getText());
      operator = e.getActionCommand(); //得到刚刚按下的运算符
      front = txtResult.getText(); //记录加减乘除运算符之前输入的内容
    } else if (numFlag) {
//            ActionEvent ee = new ActionEvent("qq", 1, "pp");
      btnEqualActionPerformed();
      operator = e.getActionCommand(); //得到刚刚按下的运算符
      front = result;
      numFlag = false;
    } else {
      front = txtResult.getText(); //记录加减乘除运算符之前输入的内容
      operator = e.getActionCommand(); //得到刚刚按下的运算符
    }
    calculateFlag = false;
    flag = true; //记录已经按下了加减乘除运算符的其中一个
  }

  void btnEqualActionPerformed() {
    if (!calculateFlag) { //未曾按下等于运算符
      behind = txtResult.getText();
    } else {
      front = result;
    }
    try {
      if (Objects.isNull(front) || front.isEmpty() || Objects.isNull(behind) || behind.isEmpty()) {
        return;
      }
      double a1 = Double.parseDouble(front);
      double b1 = Double.parseDouble(behind);
      double result;
      if (Objects.equals(operator, "+")) {
        result = a1 + b1;
      } else if (Objects.equals(operator, "-")) {
        result = a1 - b1;
      } else if (Objects.equals(operator, "*")) {
        result = a1 * b1;
      } else {
        result = a1 / b1;
      }
      this.result = Double.toString(result);
      txtResult.setText(this.result);
    } catch (ArithmeticException ce) {
      txtResult.setText("除数不能为零");
    }
    if (!calculateFlag) {
      calculateFlag = true;
    }
  }

  void btnPointActionPerformed(ActionEvent e) {
    int num = txtResult.getText().indexOf(".");
    if (num < 0 && !flag) {
      txtResult.setText(txtResult.getText() + e.getActionCommand());
    }
    if (flag) {
      dotFlag = true;
    }
  }

  void btnBeginActionPerformed() {//清零运算符事件处理
    flag = false;
    dotFlag = false;
    numFlag = false;
    calculateFlag = false;
    front = "";
    behind = "";
    result = "";
    txtResult.setText("0");
  }

  void btnMinusActionPerformed() {//取反运算符事件处理
    if (txtResult.getText().equals("0")) {//如果文本框内容为0
      txtResult.setText(txtResult.getText());
    } else if (txtResult.getText().contains("-")) {//若文本框中含有负号
      String a = txtResult.getText().replaceAll("-", "");
      txtResult.setText(a);
    } else if (flag) {
      txtResult.setText("0");
    } else {
      txtResult.setText("-" + txtResult.getText());
    }
  }

  void btnCancel_actionPerformed() {//退格事件处理方法
    String str = txtResult.getText();
    if (str.length() == 1) {//如文本框中只剩下最后一个字符,将文本框内容置为0
      txtResult.setText("0");
    }
    if (str.length() > 1) {
      str = str.substring(0, str.length() - 1);
      txtResult.setText(str);
    }
  }

  public static void main(String[] args) {
    Calculator calculator = new Calculator();
    calculator.setSize(400, 310);
    calculator.setLocation(200, 150);
    calculator.setVisible(true);
  }
}

