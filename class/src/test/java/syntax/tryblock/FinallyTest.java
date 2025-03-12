package syntax.tryblock;

import org.junit.Test;

/**
 * @author Kuangcp
 * 2025-03-12 18:22
 */
public class FinallyTest {


    /**
     * 将 输出 try 和 finally
     */
    @Test
    public void testExceptionFinally() throws Exception {
        try {
            System.out.println("try");
            throw new RuntimeException("error");
        } finally {
            System.out.println("finally");
        }
    }
}
