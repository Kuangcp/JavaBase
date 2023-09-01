package com.github.kuangcp.antlr4.expression;

import expression.ExprBaseListener;
import expression.ExprListener;
import org.antlr.v4.runtime.ParserRuleContext;

public class CustomListener extends ExprBaseListener {


    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        super.enterEveryRule(ctx);
    }
}
