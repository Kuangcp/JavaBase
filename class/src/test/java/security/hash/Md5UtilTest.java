package security.hash;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Kuangcp
 * 2025-05-09 18:02
 */
public class Md5UtilTest {

    @Test
    public void testCalculateFileMd5() throws Exception {
        Optional<String> result = Md5Util.calculateFileMd5("https://docs.github.com/assets/cb-345/images/site/favicon.png");
        assertThat(result.isPresent(), equalTo(true));
        Assert.assertEquals("3f0e5c3208cc4bfcb07223777cfbca25", result.get());
    }
}
