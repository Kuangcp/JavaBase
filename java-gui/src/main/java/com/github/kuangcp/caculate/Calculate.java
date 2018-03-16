package com.github.kuangcp.caculate;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 *
 * 搜到的一个计算器实现代码，挺不符合规范的，现在把idea的警告全改过来了
 */
public class Calculate extends JFrame {
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
    private Calculate() {
        try {
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(null);
        this.setResizable(false);
        setSize(new Dimension(400, 300));
        setTitle("计算器");
        txtResult.setEnabled(false);
        txtResult.setFont(new Font("Dialog", Font.PLAIN, 20));
        txtResult.setEditable(false);

        txtResult.setHorizontalAlignment(SwingConstants.RIGHT);
        txtResult.setBounds(new Rectangle(33, 19, 310, 34));
        btnNull.setBounds(new Rectangle(298, 70, 46, 37));
        btnNull.setFont(new Font("Dialog", Font.PLAIN, 12));

        //btnNull.addActionListener(new FrameCalculate_btnNull_actionAdapter(this));
        btnFour.setBounds(new Rectangle(33, 120, 46, 37));
        btnFour.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnFive.setBounds(new Rectangle(101, 120, 46, 37));
        btnFive.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnSix.setBounds(new Rectangle(167, 119, 46, 37));
        btnSix.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnDecrease.setBounds(new Rectangle(234, 120, 46, 37));
        btnDecrease.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnBegin.setBounds(new Rectangle(298, 121, 46, 37));
        btnBegin.setFont(new Font("Dialog", Font.PLAIN, 15));

        btnBegin.addActionListener(new Calculate_btnBegin_actionAdapter(this));
        btnOne.setBounds(new Rectangle(33, 172, 46, 37));
        btnOne.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnTwo.setBounds(new Rectangle(101, 172, 46, 37));
        btnTwo.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnThree.setBounds(new Rectangle(167, 172, 46, 37));
        btnThree.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnMultiply.setBounds(new Rectangle(234, 172, 46, 37));
        btnMultiply.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnCancel.setBounds(new Rectangle(298, 172, 46, 37));
        btnCancel.setFont(new Font("Dialog", Font.PLAIN, 12));

        btnCancel.addActionListener(new Calculate_btnCancel_actionAdapter(this));
        btnZero.setBounds(new Rectangle(33, 222, 46, 37));
        btnZero.setFont(new Font("Dialog", Font.PLAIN, 20));

        //加载数字0-9的监听事件
        btnZero.addActionListener(new Calculate_btnZero_actionAdapter(this));
        btnOne.addActionListener(new Calculate_btnZero_actionAdapter(this));
        btnTwo.addActionListener(new Calculate_btnZero_actionAdapter(this));
        btnThree.addActionListener(new Calculate_btnZero_actionAdapter(this));
        btnFour.addActionListener(new Calculate_btnZero_actionAdapter(this));
        btnFive.addActionListener(new Calculate_btnZero_actionAdapter(this));
        btnSix.addActionListener(new Calculate_btnZero_actionAdapter(this));
        btnSeven.addActionListener(new Calculate_btnZero_actionAdapter(this));
        btnEight.addActionListener(new Calculate_btnZero_actionAdapter(this));
        btnNine.addActionListener(new Calculate_btnZero_actionAdapter(this));
        btnMinus.setBounds(new Rectangle(101, 222, 46, 37));
        btnMinus.setFont(new Font("Dialog", Font.PLAIN, 10));

        btnMinus.addActionListener(new Calculate_btnMinus_actionAdapter(this));
        btnPoint.setBounds(new Rectangle(167, 222, 46, 37));
        btnPoint.setFont(new Font("Dialog", Font.PLAIN, 30));
        btnPoint.setHorizontalTextPosition(SwingConstants.CENTER);

        btnPoint.addActionListener(new Calculate_btnPoint_actionAdapter(this));
        btnDivide.setBounds(new Rectangle(234, 222, 46, 37));
        btnDivide.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnEqual.setBounds(new Rectangle(298, 222, 46, 37));
        btnEqual.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnEqual.addActionListener(new Calculate_btnEqual_actionAdapter(this));
        btnIncrease.setBounds(new Rectangle(234, 70, 46, 37));
        btnIncrease.setFont(new Font("Dialog", Font.PLAIN, 20));

        //加载加减乘除运算符的监听事件
        btnIncrease.addActionListener(new
                Calculate_btnIncrease_actionAdapter(this));
        btnDecrease.addActionListener(new
                Calculate_btnIncrease_actionAdapter(this));
        btnMultiply.addActionListener(new
                Calculate_btnIncrease_actionAdapter(this));
        btnDivide.addActionListener(new
                Calculate_btnIncrease_actionAdapter(this));
        btnSeven.setBounds(new Rectangle(33, 70, 46, 37));
        btnSeven.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnEight.setBounds(new Rectangle(101, 70, 46, 37));
        btnEight.setFont(new Font("Dialog", Font.PLAIN, 20));

        btnNine.setBounds(new Rectangle(167, 70, 46, 37));
        btnNine.setFont(new Font("Dialog", Font.PLAIN, 20));

        contentPane.add(btnFive);
        contentPane.add(btnSix);
        contentPane.add(btnDecrease);
        contentPane.add(btnBegin);
        contentPane.add(btnOne);
        contentPane.add(btnTwo);
        contentPane.add(btnThree);
        contentPane.add(btnMultiply);
        contentPane.add(btnCancel);
        contentPane.add(btnMinus);
        contentPane.add(btnPoint);
        contentPane.add(btnDivide);
        contentPane.add(btnEqual);
        contentPane.add(btnEight);
        contentPane.add(btnNine);
        contentPane.add(btnFour);
        contentPane.add(btnSeven);
        contentPane.add(btnIncrease);
        contentPane.add(btnNull);
        contentPane.add(txtResult);
        contentPane.add(btnZero);
    }
    void btnZero_actionPerformed(ActionEvent e) {
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
        flag3=false;
    }
    void btnIncrease_actionPerformed(ActionEvent e) {
        if(flag3){
            txtResult.setText(txtResult.getText());
            op = e.getActionCommand(); //得到刚刚按下的运算符
            front = txtResult.getText(); //记录加减乘除运算符之前输入的内容
        }
        else if (flag2) {
//            ActionEvent ee = new ActionEvent("qq", 1, "pp");
            btnEqual_actionPerformed();
            op = e.getActionCommand(); //得到刚刚按下的运算符
            front = re;
            flag2 = false;
        } else {
            front = txtResult.getText(); //记录加减乘除运算符之前输入的内容
            op = e.getActionCommand(); //得到刚刚按下的运算符
        }
        flag3=false;
        flag = true; //记录已经按下了加减乘除运算符的其中一个
    }
    void btnEqual_actionPerformed() {
        if(!flag3)//未曾按下等于运算符
            behind = txtResult.getText();
        else
            front = re;
        try {
            double a1 = Double.parseDouble(front);
            double b1 = Double.parseDouble(behind);
            double result;
            if (Objects.equals(op, "+")) result = a1 + b1;
            else if (Objects.equals(op, "-")) {
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
        if (!flag3)
            flag3 = true;
    }
    void btnPoint_actionPerformed(ActionEvent e) {
        int num=txtResult.getText().indexOf(".");
        if(num<0 && !flag)
            txtResult.setText(txtResult.getText()+e.getActionCommand());
        if(flag)
            flag1=true;
    }
    void btnBegin_actionPerformed() {//清零运算符事件处理
        flag=false;
        flag1=false;
        flag2=false;
        flag3=false;
        front="";
        behind="";
        re="";
        txtResult.setText("0");
    }
    void btnMinus_actionPerformed() {//取反运算符事件处理
        if(txtResult.getText().equals("0")){//如果文本框内容为0
            txtResult.setText(txtResult.getText());
        }else if(txtResult.getText().contains("-")){//若文本框中含有负号
            String a=txtResult.getText().replaceAll("-","");
            txtResult.setText(a);
        }else if(flag){
            txtResult.setText("0");
        }else{
            txtResult.setText("-"+txtResult.getText());
        }
    }
    void btnCancel_actionPerformed() {//退格事件处理方法
        String str=txtResult.getText();
        if(str.length() == 1){//如文本框中只剩下最后一个字符,将文本框内容置为0
            txtResult.setText("0");
        }
        if(str.length()>1){
            str=str.substring(0,str.length()-1);
            txtResult.setText(str);
        }
    }
    public static void main(String[] args){
        Calculate fc = new Calculate();
        fc.setSize(400,310);
        fc.setLocation(200,150);
        fc.setVisible(true);
    }
}

class Calculate_btnCancel_actionAdapter implements ActionListener {
    private Calculate adaptee;
    Calculate_btnCancel_actionAdapter(Calculate adaptee) {
        this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
        adaptee.btnCancel_actionPerformed();
    }
}

class Calculate_btnMinus_actionAdapter implements ActionListener {
    private Calculate adaptee;
    Calculate_btnMinus_actionAdapter(Calculate adaptee) {
        this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
        adaptee.btnMinus_actionPerformed();
    }
}

class Calculate_btnBegin_actionAdapter implements ActionListener {
    private Calculate adaptee;
    Calculate_btnBegin_actionAdapter(Calculate adaptee) {
        this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
        adaptee.btnBegin_actionPerformed();
    }
}

class Calculate_btnPoint_actionAdapter implements ActionListener {
    private Calculate adaptee;
    Calculate_btnPoint_actionAdapter(Calculate adaptee) {
        this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
        adaptee.btnPoint_actionPerformed(e);
    }
}

class Calculate_btnEqual_actionAdapter implements ActionListener {
    private Calculate adaptee;
    Calculate_btnEqual_actionAdapter(Calculate adaptee) {
        this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
        adaptee.btnEqual_actionPerformed();
    }
}

class Calculate_btnIncrease_actionAdapter implements ActionListener {
    private Calculate adaptee;
    Calculate_btnIncrease_actionAdapter(Calculate adaptee) {
        this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
        adaptee.btnIncrease_actionPerformed(e);
    }
}

class Calculate_btnZero_actionAdapter implements ActionListener {
    private Calculate adaptee;
    Calculate_btnZero_actionAdapter(Calculate adaptee) {
        this.adaptee = adaptee;
    }
    public void actionPerformed(ActionEvent e) {
        adaptee.btnZero_actionPerformed(e);
    }
}