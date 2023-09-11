package com.github.kuangcp.antlr4.expression;

import expression.ExprBaseVisitor;
import expression.ExprParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenVisitor extends ExprBaseVisitor<String> {

    @Override
    public String visitProg(ExprParser.ProgContext ctx) {
        String value = visit(ctx.expr());
        System.out.println(value);
        return value;
    }


    /* expr ('*'|'/') expr */
    @Override
    public String visitMulDiv(ExprParser.MulDivContext ctx) {
        String left = visit(ctx.expr(0));
        String right = visit(ctx.expr(1));
        if (ctx.op.getType() == ExprParser.MUL) {
            return left + "*" + right;
        }
        return left + " / round(" + right + ", 2)";
    }

    /* expr ('+'|'-') expr */
    @Override
    public String visitAddSub(ExprParser.AddSubContext ctx) {
        String left = visit(ctx.expr(0));
        String right = visit(ctx.expr(1));
        if (ctx.op.getType() == ExprParser.Add) {
            return left + "+" + right;
        }
        return left + "-" + right;
    }


    /* INT */
    @Override
    public String visitInt(ExprParser.IntContext ctx) {
        return ctx.INT().getText();
    }


    /* '(' expr ')' */
    @Override
    public String visitParens(ExprParser.ParensContext ctx) {
        return visit(ctx.expr());
    }
}
