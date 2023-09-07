package security.aes.wx;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.Provider;
import java.security.Security;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author https://github.com/kuangcp on 2020-11-01 21:35
 */
public class AESTest {

    @Test
    public void testEncrypt() throws Exception {
//        byte[] aesKey = Base64.getDecoder().decode("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG=");
        byte[] aesKey = "46EBA22EF5204DD5B110A1F730513965".getBytes(StandardCharsets.UTF_8);

        final Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            System.out.println(provider);
        }
        final String text = "123456";
        String target = AES.encrypt(aesKey, text);

        System.out.println("密文： " + target);

        String origin = AES.decrypt(aesKey, target);
        assertThat(origin, equalTo(text));
    }
}
