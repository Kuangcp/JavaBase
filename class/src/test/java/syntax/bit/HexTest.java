package syntax.bit;

import org.junit.Test;
import security.hash.Md5Util;

import java.math.BigInteger;
import java.security.MessageDigest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Kuangcp
 * 2025-04-10 16:14
 */
public class HexTest {

    /**
     * printf hi | md5sum
     */
    @Test
    @Deprecated
    public void testHex() throws Exception {
        String txt = "hi";
        byte[] hash = MessageDigest.getInstance("MD5").digest(txt.getBytes());
        String checksum = new BigInteger(1, hash).toString(16);
        System.out.println(checksum);
        System.out.println(Md5Util.bytesToHexBuilder(hash));
        System.out.println(Md5Util.bytesToHex(hash));
    }

    @Test
    public void testLossHigh() throws Exception {
        byte[] hash = new byte[]{
                0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0
        };
        // 简单但是有隐患，例如最高位是0时，会丢失高位的所有0
        String checksum = new BigInteger(1, hash).toString(16);

        System.out.println(checksum);
        assertThat(checksum, equalTo("0"));

        System.out.println(Md5Util.bytesToHexBuilder(hash));
        assertThat(Md5Util.bytesToHexBuilder(hash), equalTo("000000000000000000"));
        assertThat(Md5Util.bytesToHex(hash), equalTo("000000000000000000"));
    }
}
