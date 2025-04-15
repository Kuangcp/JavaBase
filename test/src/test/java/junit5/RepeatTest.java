package junit5;


import org.junit.jupiter.api.RepeatedTest;

/**
 * @author Kuangcp
 * 2025-04-15 21:24
 */
public class RepeatTest {

    @RepeatedTest(5)
    public void testRepeat() throws Exception {
        System.out.println("xxxxxx");
    }
}
