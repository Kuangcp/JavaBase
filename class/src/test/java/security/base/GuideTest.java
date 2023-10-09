package security.base;

import org.junit.Test;

import java.security.Provider;
import java.security.Security;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2023-10-09 23:54
 */
public class GuideTest {
    @Test
    public void testProvider() throws Exception {
        final Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            System.out.println(provider);
        }
    }
}
