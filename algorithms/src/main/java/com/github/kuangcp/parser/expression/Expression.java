package com.github.kuangcp.parser.expression;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * <a href="https://github.com/simplewz/Expression">Github: simplewz/Expression</a>
 * <p>
 * 四则运算表达式解析
 * <p>
 * 2023-06-09 18:30
 */
public class Expression {

    private String expression;  //算术表达式字符串
    private final List<ExpressionWord> expressionWord; //存储表达式词法分析所得的单词与该单词所对应的种别编码的集合
    private int index;  //读取词法分析器的字符游标
    private int errorCode = 0;  //表达式错误编码
    private int sym;  //单词种别编码

    public Expression(String expression) {
        String temp = expression.replace("{", "");
        temp = temp.replace("}", "");

        this.expression = temp;
        expressionWord = wordAnalysis(this.expression);
    }

    /**
     * 词法分析器：用于扫描表达式并处理得到该表达式的词库
     *
     * @param expressionStr 表达式字符串
     */
    public static List<ExpressionWord> wordAnalysis(String expressionStr) {
        String expStrNoSpace = remove(expressionStr, ' '); //去除目标表达式中的空格字符
        char[] exChar = expStrNoSpace.toCharArray();
        List<ExpressionWord> expressionWord = new ArrayList<>();
        for (int i = 0; i < exChar.length; i++) {
            //如果是操作符或是括号,直接识别出来
            if (exChar[i] == '+' || exChar[i] == '-' || exChar[i] == '*' || exChar[i] == '/' || exChar[i] == '(' || exChar[i] == ')') {
                StringBuilder sb = new StringBuilder();
                sb.append(exChar[i]);
                switch (exChar[i]) {
                    case '+':
                        expressionWord.add(new ExpressionWord(sb.toString(), 1));
                        break;
                    case '-':
                        expressionWord.add(new ExpressionWord(sb.toString(), 2));
                        break;
                    case '*':
                        expressionWord.add(new ExpressionWord(sb.toString(), 3));
                        break;
                    case '/':
                        expressionWord.add(new ExpressionWord(sb.toString(), 4));
                        break;
                    case '(':
                        expressionWord.add(new ExpressionWord(sb.toString(), 6));
                        break;
                    case ')':
                        expressionWord.add(new ExpressionWord(sb.toString(), 7));
                        break;
                    default:
                        break;
                }
            } else if (('a' <= exChar[i] && exChar[i] <= 'z') || ('A' <= exChar[i] && exChar[i] <= 'Z') || exChar[i] == '_') {
                StringBuilder sb = new StringBuilder();
                i = getI(exChar, i, sb);
                if (exChar[i] == '.') {
                    ++i;
                    if (('a' <= exChar[i] && exChar[i] <= 'z') || ('A' <= exChar[i] && exChar[i] <= 'Z') || exChar[i] == '_' || ('0' <= exChar[i] && exChar[i] <= '9')) {
                        sb.append('.');
                        i = getI(exChar, i, sb);
                    } else {
                        throw new RuntimeException("词法错误!");
                    }
                }
                expressionWord.add(new ExpressionWord(sb.toString(), 5));
                if (i == exChar.length - 1) {
                    switch (exChar[i]) {
                        case '+':
                            expressionWord.add(new ExpressionWord("+", 1));
                            break;
                        case '-':
                            expressionWord.add(new ExpressionWord("-", 2));
                            break;
                        case '*':
                            expressionWord.add(new ExpressionWord("*", 3));
                            break;
                        case '/':
                            expressionWord.add(new ExpressionWord("/", 4));
                            break;
                        case '(':
                            expressionWord.add(new ExpressionWord("(", 6));
                            break;
                        case ')':
                            expressionWord.add(new ExpressionWord(")", 7));
                            break;
                        default:
                            break;
                    }
                    break;
                } else {
                    i--;
                }

            } else if ('0' <= exChar[i] && exChar[i] <= '9') {
                StringBuilder sb = new StringBuilder();
                i = getA(exChar, i, sb, '0' <= exChar[i]);

                if (exChar[i] == '.') {
                    ++i;
                    if (exChar[i] >= '0' && exChar[i] <= '9') {
                        sb.append('.');
                        i = getA(exChar, i, sb, exChar[i] >= '0');
                    } else {
                        throw new RuntimeException("词法错误!");
                    }
                }
                expressionWord.add(new ExpressionWord(sb.toString(), 5));
                if (i == exChar.length - 1) {
                    switch (exChar[i]) {
                        case '+':
                            expressionWord.add(new ExpressionWord("+", 1));
                            break;
                        case '-':
                            expressionWord.add(new ExpressionWord("-", 2));
                            break;
                        case '*':
                            expressionWord.add(new ExpressionWord("*", 3));
                            break;
                        case '/':
                            expressionWord.add(new ExpressionWord("/", 4));
                            break;
                        case '(':
                            expressionWord.add(new ExpressionWord("(", 6));
                            break;
                        case ')':
                            expressionWord.add(new ExpressionWord(")", 7));
                            break;
                        default:
                            break;
                    }
                    break;
                } else {
                    i--;
                }
            } else {
                throw new RuntimeException("词法错误!");
            }
        }
        return expressionWord;
    }

    private static int getA(char[] exChar, int i, StringBuilder sb, boolean b) {
        while (b && exChar[i] <= '9') {
            if (i == exChar.length - 1) {
                sb.append(exChar[i]);
                break;
            } else {
                sb.append(exChar[i]);
                i++;
            }
        }
        return i;
    }

    private static int getI(char[] exChar, int i, StringBuilder sb) {
        while (('a' <= exChar[i] && exChar[i] <= 'z') || ('A' <= exChar[i] && exChar[i] <= 'Z') || exChar[i] == '_' || ('0' <= exChar[i] && exChar[i] <= '9')) {
            if (i == exChar.length - 1) {
                sb.append(exChar[i]);
                break;
            } else {
                sb.append(exChar[i]);
                i++;
            }
        }
        return i;
    }

    private static String remove(String srcStr, char ch) {
        StringBuilder sbBuilder = new StringBuilder();
        for (int i = 0; i < srcStr.length(); i++) {
            char currentChar = srcStr.charAt(i);
            if (currentChar != ch) {
                sbBuilder.append(currentChar);
            }
        }
        return sbBuilder.toString();
    }

    public boolean isRightExpression() {
        errorCode = 0;
        sym = 0;
        index = 0;
        //上述三条语句在进行表达式的合法检测之前还原表达式的初始状态：原因表达式调用一次判断之后不会还原上述三个参数
        next();
        E();
        return sym == 0 && errorCode == 0;
    }

    private void next() {
        if (index < expressionWord.size()) {
            sym = expressionWord.get(index).code;
            index++;
        } else {
            sym = 0;
        }
    }

    /**
     * 算数表达式的文法规则G(E)：
     * E -> E+T | E-T | T
     * T ->T*F | T/F | F
     * F -> (E) | d
     */
    private void E() {
        T();
        E1();
    }

    private void E1() {
        if (sym == 1) {
            next();
            T();
            E1();
        } else if (sym == 2) {
            next();
            T();
            E1();
        } else if (sym != 7 && sym != 0) {
            errorCode = -1;
        }
    }

    private void T() {
        F();
        T1();
    }

    private void F() {
        if (sym == 5) {
            next();
        } else if (sym == 6) {
            next();
            E();
            if (sym == 7) {
                next();
            } else {
                errorCode = -1;
            }
        } else {
            errorCode = -1;
        }
    }

    private void T1() {
        if (sym == 3) {
            next();
            F();
            T1();
        } else if (sym == 4) {
            next();
            F();
            T1();
        } else if (sym != 0 && sym != 1 && sym != 2 && sym != 7) {
            errorCode = -1;
        }
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * 只提供get方法，返回Expression表达式对应的词法分析器识别的结果
     *
     * @return
     */
    public List<ExpressionWord> getWord() {
        return expressionWord;
    }

    /**
     * 只提供get方法，用于返回表达式是否有误的编码  0为没有错 -1为表达式有误
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 只提供get方法，用于返回表达式词法分析器中识别出的单词的种别编码
     *
     * @return
     */
    public int getSym() {
        return sym;
    }

    @Override
    public String toString() {
        return "Experssion [experssion=" + expression + ", word=" + expressionWord + ", index=" + index + ", errorCode="
                + errorCode + ", sym=" + sym + "]";
    }

    /*
     * 根据种别编码获取运算符的优先级
     */
    private static int piror(int sym) {
        switch (sym) {
            case 1:
            case 2:
                return 1;  // +  - 对应的运算级别为 1
            case 3:
            case 4:
                return 2;  // *  / 对应的运算级别为 2
            default:
                return 0;  //  其余的运算级别为 0
        }
    }

    /*
     * 根据种别编码判断是否是操作符
     */
    private static boolean isOperation(int sym) {
        switch (sym) {
            case 1:  //  +
            case 2:  //  -
            case 3:  //  *
            case 4:  //  /
                return true;
            default:
                return false;
        }
    }

    private static boolean isNumber(int sym) {
        if (sym == 5) { //单词种别编码为5时表明该单词是一个操作数，返回true
            return true;
        }
        return false;
    }


    /**
     * 中缀表达式转后缀表达式
     *
     * @return
     */
    public static List<ExpressionWord> getPostfix(Expression expression) {
        List<ExpressionWord> midfix = expression.getWord();  //获取中缀表达式的经过词法分析器分析的结果集
        List<ExpressionWord> result = new ArrayList<>();   //存储后缀表达式结果的List集合
        Stack<ExpressionWord> operationStack = new Stack<>();  //操作符栈

        if (expression.isRightExpression()) {//如果是正确的表达式则将执行将中缀表达式转换为后缀表达式的操作
            for (int i = 0; i < midfix.size(); i++) {
                ExpressionWord currentExpressionWord = midfix.get(i);  //获取当前单词
                if (isOperation(currentExpressionWord.code)) {
                    //如果操作符栈不为空且操作符栈的栈顶元素是操作符并且操作符栈顶的运算级别高于当前操作符的运算级别
                    while (!operationStack.isEmpty() && isOperation(operationStack.peek().code) && piror(operationStack.peek().code) >= piror(currentExpressionWord.code)) {
                        result.add(operationStack.peek());  //将操作符栈顶元素添加到后缀表达式List中
                        operationStack.pop();  //操作符栈顶元素出栈
                    }
                    operationStack.push(currentExpressionWord);  //当前操作符入栈
                } else if (currentExpressionWord.code.equals(6)) {  //如果当前单词是左括号 无条件入栈
                    operationStack.push(currentExpressionWord);
                } else if (currentExpressionWord.code.equals(7)) {//当前单词是右括号 将操作符栈中的左括号以前的操作符出栈
                    while (!operationStack.peek().code.equals(6)) { //如果栈顶操作符不是左括号
                        result.add(operationStack.peek()); //栈顶操作符加入后缀表达式List中
                        operationStack.pop();  //栈顶操作符出栈
                    }
                    operationStack.pop(); //当前操作符入栈
                } else {
                    result.add(currentExpressionWord); //当前单词不是操作符，即数字直接加入到后缀表达式的List中
                }
            }
            //中缀表达式链表元素遍历完毕，需要将栈中元素依次出栈加入到后缀表达式List中
            while (!operationStack.isEmpty()) {
                result.add(operationStack.peek());
                operationStack.pop();
            }
        } else {//不是正确的表达式，给出提示信息
            System.out.println("表达式有误！");
        }
        return result;
    }

    /**
     * 计算表达式的运算结果
     */
    public static BigDecimal expCalculate(Expression expression) {
        List<ExpressionWord> postfix = getPostfix(expression);  //获取表达式的后缀表达式
        Stack<BigDecimal> result = new Stack<>();  //操作数栈
        if (postfix != null && postfix.size() > 0) {
            for (int i = 0; i < postfix.size(); i++) {
                ExpressionWord currentExpressionWord = postfix.get(i);
                if (isNumber(currentExpressionWord.code)) {
                    result.push(new BigDecimal(currentExpressionWord.word));
                } else if (isOperation(currentExpressionWord.code)) {
                    BigDecimal first = result.peek();
                    result.pop();
                    BigDecimal second = result.peek();
                    result.pop();
                    switch (currentExpressionWord.code) {
                        case 1:
                            result.push(second.add(first));
                            break;
                        case 2:
                            result.push(second.subtract(first));
                            break;
                        case 3:
                            result.push(second.multiply(first));
                            break;
                        case 4:
                            result.push(second.divide(first, 4, BigDecimal.ROUND_HALF_UP));
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return result.peek();
    }
}

