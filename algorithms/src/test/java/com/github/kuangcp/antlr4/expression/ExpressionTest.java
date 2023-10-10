package com.github.kuangcp.antlr4.expression;

import expression.ExprLexer;
import expression.ExprListener;
import expression.ExprParser;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

/**
 * @author <a href="https://github.com/kuangcp">Github</a>
 * 2023-08-31 21:23
 */
@Slf4j
public class ExpressionTest {

    @Test
    public void testFirst() throws Exception {
        String str = "a/b";

        ExprLexer serverLogLexer = new ExprLexer(CharStreams.fromString(str));
        CommonTokenStream tokens = new CommonTokenStream(serverLogLexer);
        ExprParser logParser = new ExprParser(tokens);
        ParseTreeWalker walker = new ParseTreeWalker();
        ExprListener logWalker = new CustomListener();

//        logWalker.add

        walker.walk(logWalker, logParser.expr());
    }

    // https://juejin.cn/post/6844903701472083982
    @Test
    public void testEval() throws Exception {
        try {
            ExprLexer lexer = new ExprLexer(CharStreams.fromString("(1+3)*124+(9/3 + 4)\n"));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ExprParser parser = new ExprParser(tokens);
            ExprParser.ProgContext tree = parser.prog();
            EvalVisitor eval = new EvalVisitor();
            Integer result = eval.visit(tree);
            log.info("result={}", result);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Test
    public void testTokenHandle() throws Exception {
        ExprLexer lexer = new ExprLexer(CharStreams.fromString("9/3*(6+7)\n"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExprParser parser = new ExprParser(tokens);
        ExprParser.ProgContext tree = parser.prog();
        TokenVisitor eval = new TokenVisitor();
        String result = eval.visit(tree);
        log.info("result={}", result);
    }
}
