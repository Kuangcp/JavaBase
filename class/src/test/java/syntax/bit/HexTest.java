package syntax.bit;

import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author Kuangcp
 * 2025-04-10 16:14
 */
public class HexTest {

    /**
     * printf hi | md5sum
     */
    @Test
    public void testHex() throws Exception {
        String txt = "hi";
        byte[] hash = MessageDigest.getInstance("MD5").digest(txt.getBytes());
        String checksum = new BigInteger(1, hash).toString(16);
        System.out.println(checksum);
    }
}
