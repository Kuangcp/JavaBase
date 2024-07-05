package syntax.base;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/12/19-12:02 AM
 */
@Data
@Slf4j
public class BaseTypeDefaultValueTest {

    private short a;
    private int b;
    private long c;
    private char d;
    private boolean e;

    @Test
    public void testDefaultValue() {
        log.info("{} {} {} {} {}", a, b, c, d, e);

        assertThat(a, equalTo((short) 0));
        assertThat(b, equalTo(0));
        assertThat(c, equalTo(0L));
        assertThat(d, equalTo('\0'));
        assertThat(e, equalTo(false));
    }
}
