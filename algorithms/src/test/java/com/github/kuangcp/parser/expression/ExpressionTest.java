package com.github.kuangcp.parser.expression;

import org.junit.Test;

import java.util.*;

/**
 * 2023-08-31 17:09
 */
public class ExpressionTest {

    @Test
    public void testInvalid() throws Exception {
        Expression e1 = new Expression("a_qq.b+5b+6cu+f");
        Expression e2 = new Expression("((12*(2.50-1.05" + ")" + "-10)+90)");
        Expression e3 = new Expression("3 * 5 / (" + " 12 + 11 " + ")");

        List<ExpressionWord> words1 = Expression.wordAnalysis(e1.getExpression());
        System.out.println(words1);
        System.out.println(e1.isRightExpression());
        System.out.println(e1);

        List<ExpressionWord> words2 = Expression.wordAnalysis(e2.getExpression());
        //e2.setWord(words2);
        System.out.println(words2);
        System.out.println(e2.isRightExpression());
        System.out.println(Expression.expCalculate(e2));
        System.out.println(e2);

        List<ExpressionWord> words3 = Expression.wordAnalysis(e3.getExpression());
        //e3.setWord(words3);
        System.out.println(words3);
        System.out.println(e3);
        System.out.println(e3.isRightExpression());
        System.out.println(Expression.expCalculate(e3));


        Expression e4 = new Expression("tc_zd_plan.xztdmj+tc_zd_plan.cltdmj");
        System.out.println(e4);
        System.out.println(e4.getWord());
        //System.out.println(expCalculate(e4));
        System.out.println(e4.isRightExpression());
        System.out.println(e4);
    }


    @org.junit.Test
    public void testFormula() throws Exception {
        String formula = "12/14";

        Expression expression = new Expression(formula);
        List<ExpressionWord> words = expression.getWord();

        System.out.println(expression.isRightExpression());
        if (words.size() != 3) {
            System.out.println("NOT");
            return;
        }

        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("12", "SUM(`放大销售额　`)");
        nameMap.put("14", "SUM(`放大销售量　`)");
        String result = "";
        String sec = nameMap.get(words.get(2).word);
        if (Objects.equals("/", words.get(1).word)) {
            result = String.format("IF(%s = 0 OR %s IS NULL, NULL, %s / materialize(%s))",
                    sec, sec, nameMap.get(words.get(0).word), sec);
        } else {
            result = String.format("%s%s%s", nameMap.get(words.get(0).word), words.get(1).word, sec);
        }

        Integer round = 2;
        if (Objects.nonNull(round)) {
            result = String.format("ROUND(%s,%d)", result, round);
        }
        System.out.println(result);
    }


}
