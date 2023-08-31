package com.github.kuangcp.parser.expression;

/**
 * 2023-06-09 18:35
 */
public class ExpressionWord {

    String word;  //单词部分

    Integer code;  //单词种别编码

    @Override
    public String toString() {
        return "(" + word + "," + code + ")";
    }

    public ExpressionWord(String word, Integer code) {
        this.word = word;
        this.code = code;
    }

    public String getWord() {
        return word;
    }

    public Integer getCode() {
        return code;
    }
}
