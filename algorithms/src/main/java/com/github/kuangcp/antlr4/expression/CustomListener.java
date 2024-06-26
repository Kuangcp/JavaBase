package com.github.kuangcp.antlr4.expression;

import expression.ExprBaseListener;
import expression.ExprParser;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ParserRuleContext;

@Slf4j
public class CustomListener extends ExprBaseListener {

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        log.info("ctx={}", ctx);
        super.enterEveryRule(ctx);
    }

    @Override
    public void enterAddSub(ExprParser.AddSubContext ctx) {
        super.enterAddSub(ctx);
    }
}
