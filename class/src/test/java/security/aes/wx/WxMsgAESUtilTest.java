package security.aes.wx;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author https://github.com/kuangcp on 2020-11-01 21:35
 */
public class WxMsgAESUtilTest {

    @Test
    public void testEncrypt() throws Exception {
//        byte[] aesKey = Base64.getDecoder().decode("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG=");
        byte[] aesKey = "46EBA22EF5204DD5B110A1F730513965".getBytes(StandardCharsets.UTF_8);

        final String text = "123456";
        String target = WxMsgAESUtil.encrypt(aesKey, text);

        System.out.println("密文： " + target);

        String origin = WxMsgAESUtil.decrypt(aesKey, target);
        assertThat(origin, equalTo(text));
    }
}
