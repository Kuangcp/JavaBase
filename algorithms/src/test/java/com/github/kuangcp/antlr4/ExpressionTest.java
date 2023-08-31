package com.github.kuangcp.antlr4;

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

        antlr4.ExprLexer serverLogLexer = new antlr4.ExprLexer(CharStreams.fromString(str));
        CommonTokenStream tokens = new CommonTokenStream(serverLogLexer);
        antlr4.ExprParser logParser = new antlr4.ExprParser(tokens);
        ParseTreeWalker walker = new ParseTreeWalker();
        antlr4.ExprListener logWalker = new antlr4.ExprListener();
        walker.walk(logWalker, logParser.expr());
    }
}
