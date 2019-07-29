package com.github.kuangcp.caculator;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * 搜到的一个计算器实现代码 TODO 思考如何重构
 */
public class Calculator extends JFrame {

  private String front = "", behind = ""; //分别用于记录加减乘除运算符之前,之后输入的内容
  private String op; //用于记录运算符
  private String re;//用于存储运算结果的字符串格式
  private boolean flag = false; //用于记录是否按下了运算符
  private boolean flag1 = false;//用于判断是否输入了点运算符
  private boolean flag2 = false;//用于判断是否输入了数字
  private boolean flag3 = false;//用于判断是否按下了等号运算符

  private JTextField txtResult = new JTextField("0");
  private JButton btnNull = new JButton("sqrt");
  private JButton btnFour = new JButton("4");
  private JButton btnFive = new JButton("5");
  private JButton btnSix = new JButton("6");
  private JButton btnDecrease = new JButton("-");
  private JButton btnBegin = new JButton("C");
  private JButton btnOne = new JButton("1");
  private JButton btnTwo = new JButton("2");
  private JButton btnThree = new JButton("3");
  private JButton btnMultiply = new JButton("*");
  private JButton btnCancel = new JButton("←");
  private JButton btnZero = new JButton("0");
  private JButton btnMinus = new JButton("+/-");
  private JButton btnPoint = new JButton(".");
  private JButton btnDivide = new JButton("/");
  private JButton btnEqual = new JButton("=");
  private JButton btnIncrease = new JButton("+");
  private JButton btnSeven = new JButton("7");
  private JButton btnEight = new JButton("8");
  private JButton btnNine = new JButton("9");

  private Font dialogFont = new Font("Dialog", Font.PLAIN, 16);

  private Calculator() {
    try {
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      initButton();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private void initButton() {
    JPanel contentPane = (JPanel) getContentPane();
    contentPane.setLayout(null);
    this.setResizable(false);
    setSize(new Dimension(400, 300));
    setTitle("计算器");
    txtResult.setEnabled(false);
    txtResult.setEditable(false);

    txtResult.setHorizontalAlignment(SwingConstants.RIGHT);
    txtResult.setBounds(new Rectangle(33, 19, 310, 34));
    btnNull.setBounds(new Rectangle(298, 70, 46, 37));
    btnNull.setFont(new Font("Dialog", Font.PLAIN, 12));

    //btnNull.addActionListener(new FrameCalculate_btnNull_actionAdapter(this));
    btnFour.setBounds(new Rectangle(33, 120, 46, 37));

    btnFive.setBounds(new Rectangle(101, 120, 46, 37));

    btnSix.setBounds(new Rectangle(167, 119, 46, 37));

    btnDecrease.setBounds(new Rectangle(234, 120, 46, 37));

    btnBegin.setBounds(new Rectangle(298, 121, 46, 37));

    btnBegin.addActionListener(new Calculate_btnBegin_actionAdapter(this));
    btnOne.setBounds(new Rectangle(33, 172, 46, 37));

    btnTwo.setBounds(new Rectangle(101, 172, 46, 37));

    btnThree.setBounds(new Rectangle(167, 172, 46, 37));

    btnMultiply.setBounds(new Rectangle(234, 172, 46, 37));

    btnCancel.setBounds(new Rectangle(298, 172, 46, 37));
    btnCancel.setFont(new Font("Dialog", Font.PLAIN, 12));

    btnCancel.addActionListener(new Calculate_btnCancel_actionAdapter(this));
    btnZero.setBounds(new Rectangle(33, 222, 46, 37));

    //加载数字0-9的监听事件
    bindListener(btnZero, btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight,
        btnNine);
    setFontForCompenent(btnZero, btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven,
        btnEight, btnNine, txtResult, btnDecrease, btnBegin, btnMultiply, btnDivide, btnIncrease,
        btnEqual);

    btnMinus.setBounds(new Rectangle(101, 222, 46, 37));
    btnMinus.setFont(new Font("Dialog", Font.PLAIN, 10));

    btnMinus.addActionListener(new Calculate_btnMinus_actionAdapter(this));
    btnPoint.setBounds(new Rectangle(167, 222, 46, 37));
    btnPoint.setFont(new Font("Dialog", Font.PLAIN, 30));
    btnPoint.setHorizontalTextPosition(SwingConstants.CENTER);

    btnPoint.addActionListener(new Calculate_btnPoint_actionAdapter(this));
    btnDivide.setBounds(new Rectangle(234, 222, 46, 37));

    btnEqual.setBounds(new Rectangle(298, 222, 46, 37));

    btnEqual.addActionListener(new Calculate_btnEqual_actionAdapter(this));
    btnIncrease.setBounds(new Rectangle(234, 70, 46, 37));

    //加载加减乘除运算符的监听事件
    btnIncrease.addActionListener(new Calculate_btnIncrease_actionAdapter(this));
    btnDecrease.addActionListener(new Calculate_btnIncrease_actionAdapter(this));
    btnMultiply.addActionListener(new Calculate_btnIncrease_actionAdapter(this));
    btnDivide.addActionListener(new Calculate_btnIncrease_actionAdapter(this));

    btnSeven.setBounds(new Rectangle(33, 70, 46, 37));
    btnEight.setBounds(new Rectangle(101, 70, 46, 37));
    btnNine.setBounds(new Rectangle(167, 70, 46, 37));

    bindButton(contentPane, btnZero, btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven,
        btnEight, btnNine, btnDecrease, btnBegin, btnMultiply, btnCancel, btnMinus, btnPoint,
        btnDivide, btnEqual, btnIncrease, btnNull);

    contentPane.add(txtResult);
  }

  private void setFontForCompenent(JComponent... components) {
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
      button.addActionListener(new Calculate_btnZero_actionAdapter(this));
    }
  }

  private void bindButton(JPanel panel, JButton... buttons) {
    if (Objects.isNull(buttons)) {
      return;
    }
    if (Objects.isNull(panel)) {
      return;
    }
    for (JButton button : buttons) {
      if (Objects.isNull(button)) {
        continue;
      }
      panel.add(button);
    }
  }

  void btnZeroActionPerformed(ActionEvent e) {
    if (flag) { //如果刚刚按下了运算符
      txtResult.setText("");
      if (flag1) {//判断之前是否输入了点运算符
        txtResult.setText("0." + e.getActionCommand());
        flag1 = false;
      } else {
        txtResult.setText(e.getActionCommand());
      }
      flag2 = true;
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
    flag3 = false;
  }

  void btnIncreaseActionPerformed(ActionEvent e) {
    if (flag3) {
      txtResult.setText(txtResult.getText());
      op = e.getActionCommand(); //得到刚刚按下的运算符
      front = txtResult.getText(); //记录加减乘除运算符之前输入的内容
    } else if (flag2) {
//            ActionEvent ee = new ActionEvent("qq", 1, "pp");
      btnEqualActionPerformed();
      op = e.getActionCommand(); //得到刚刚按下的运算符
      front = re;
      flag2 = false;
    } else {
      front = txtResult.getText(); //记录加减乘除运算符之前输入的内容
      op = e.getActionCommand(); //得到刚刚按下的运算符
    }
    flag3 = false;
    flag = true; //记录已经按下了加减乘除运算符的其中一个
  }

  void btnEqualActionPerformed() {
    if (!flag3) { //未曾按下等于运算符
      behind = txtResult.getText();
    } else {
      front = re;
    }
    try {
      if (Objects.isNull(front) || front.isEmpty() || Objects.isNull(behind) || behind.isEmpty()) {
        return;
      }
      double a1 = Double.parseDouble(front);
      double b1 = Double.parseDouble(behind);
      double result;
      if (Objects.equals(op, "+")) {
        result = a1 + b1;
      } else if (Objects.equals(op, "-")) {
        result = a1 - b1;
      } else if (Objects.equals(op, "*")) {
        result = a1 * b1;
      } else {
        result = a1 / b1;
      }
      re = Double.toString(result);
      txtResult.setText(re);
    } catch (ArithmeticException ce) {
      txtResult.setText("除数不能为零");
    }
    if (!flag3) {
      flag3 = true;
    }
  }

  void btnPointActionPerformed(ActionEvent e) {
    int num = txtResult.getText().indexOf(".");
    if (num < 0 && !flag) {
      txtResult.setText(txtResult.getText() + e.getActionCommand());
    }
    if (flag) {
      flag1 = true;
    }
  }

  void btnBeginActionPerformed() {//清零运算符事件处理
    flag = false;
    flag1 = false;
    flag2 = false;
    flag3 = false;
    front = "";
    behind = "";
    re = "";
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

