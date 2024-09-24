package junit4.param;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Kuangcp
 * 2024-09-24 14:23
 */
// 1
@RunWith(Parameterized.class)
public class CalculateTest {
    // 2
    private final double numA;
    private final double numB;

    // 3
    public CalculateTest(double numA, double numB) {
        this.numA = numA;
        this.numB = numB;
    }

    // 4
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                {2, 4},
                {3, 5},
                {7, 3}
        };
        return Arrays.asList(data);
    }

    // 5
    @Test
    public void testAdd() throws Exception {
        Calculate calc = new Calculate();
        double result = calc.add(numA, numB);
        System.out.println("input " + numA + " + " + numB + " = " + result);
        assert result != 0;
    }

    @Test
    public void testDivide() {
        Calculate calc = new Calculate();
        double result = calc.divide(numA, 3);
        System.out.println("input " + numA + " / " + 3 + " = " + result);
        assert result != 0;
    }
}
