package com.github.kuangcp.antlr4.expression;

import expression.ExprBaseVisitor;
import expression.ExprParser;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/kuangcp">Github</a>
 * 2023-09-01 13:46
 */
@Slf4j
public class EvalVisitor extends ExprBaseVisitor<Integer> {

    @Override
    public Integer visitProg(ExprParser.ProgContext ctx) {
        Integer value = visit(ctx.expr());
        System.out.println(value);
        return value;
    }


    /* expr ('*'|'/') expr */
    @Override
    public Integer visitMulDiv(ExprParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if (ctx.op.getType() == ExprParser.MUL) {
            return left * right;
        }
        return left / right;
    }

    /* expr ('+'|'-') expr */
    @Override
    public Integer visitAddSub(ExprParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        if (ctx.op.getType() == ExprParser.Add) {
            return left + right;
        }
        return left - right;
    }


    /* INT */
    @Override
    public Integer visitInt(ExprParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }


    /* '(' expr ')' */
    @Override
    public Integer visitParens(ExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }
}
