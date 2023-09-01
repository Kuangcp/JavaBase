package com.github.kuangcp.antlr4;

import com.github.kuangcp.antlr4.expression.CustomListener;
import expression.ExprLexer;
import expression.ExprListener;
import expression.ExprParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

/**
 * @author kuangchengping@sinohealth.cn
 * 2023-08-31 21:23
 */
public class ExpressionTest {

    @Test
    public void testFirst() throws Exception {
        String str = "a/b";

        ExprLexer serverLogLexer = new ExprLexer(CharStreams.fromString(str));
        CommonTokenStream tokens = new CommonTokenStream(serverLogLexer);
        ExprParser logParser = new ExprParser(tokens);
        ParseTreeWalker walker = new ParseTreeWalker();
        ExprListener logWalker = new CustomListener();
        walker.walk(logWalker, logParser.expr());
    }
}
