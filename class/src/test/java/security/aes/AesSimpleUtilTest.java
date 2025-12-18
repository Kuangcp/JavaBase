package security.aes;

import com.github.kuangcp.util.ShowBinary;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.RepeatedTest;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author Kuangcp
 * 2025-12-18 21:01
 */
@Slf4j
public class AesSimpleUtilTest {

    @Test
    public void testFlow() throws Exception {
        // 生成随机密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 128 192 256
        keyGenerator.init(128, new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();

        byte[] key = secretKey.getEncoded();
        System.out.println("AES 密钥 Base64：" + Base64.getEncoder().encodeToString(key));
        System.out.println("AES 密钥 Hex：" + ShowBinary.byteToHex(key));

        String content = "KeyGenerator keyGenerator = KeyGenerator.getInstance(\"AES\");";
        System.out.println("原文：" + content);

        byte[] ret = AesSimpleUtil.encrypt(key, content.getBytes());
        System.out.println("密文：" + Base64.getEncoder().encodeToString(ret));
        System.out.println("密文：" + ShowBinary.byteToHex(ret));

        byte[] raw = AesSimpleUtil.decrypt(key, ret);
        System.out.println("原文：" + new String(raw));
        Assert.assertEquals(content, new String(raw));
    }

    @Test
    @RepeatedTest(6)
    public void testHexKeyFlow() throws Exception {
        // 生成随机密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 128 192 256
        keyGenerator.init(128, new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key = secretKey.getEncoded();

//        System.out.println("Origin: " + key.length);
        String hexKey = ShowBinary.byteToHex(key);

        System.out.println("AES 密钥：" + hexKey);

        String content = "KeyGenerator keyGenerator = KeyGenerator.getInstance(\"AES\");";
        System.out.println("原文：" + content);

        System.out.println("------");
        byte[] ret = AesSimpleUtil.encrypt(hexKey, content.getBytes());
//        System.out.println("密文：" + Base64.getEncoder().encodeToString(ret));
        System.out.println("密文：" + ShowBinary.byteToHex(ret));

        byte[] raw = AesSimpleUtil.decrypt(hexKey, ret);
        System.out.println("原文：" + new String(raw));
        Assert.assertEquals(content, new String(raw));
    }


    /**
     * 只保留一个密钥，iv使用0值，安全性降低，但是更方便
     */
    @Test
    public void testSimple() throws Exception {
        String key = "这个不是密钥";
        SecretKeySpec secretKey = AesSimpleUtil.generateKey(key);

        String plainText = "Hello, AES Encryption!";
        String encryptedText = AesSimpleUtil.encrypt(plainText, secretKey);
        System.out.println("加密后的密文 (Base64): " + encryptedText);

        String decryptedText = AesSimpleUtil.decrypt(encryptedText, secretKey);
        System.out.println("解密后的明文: " + decryptedText);
    }

    @Test
    public void testViaMd5() throws Exception {
        String key = "这个不是密钥";
        SecretKeySpec secretKey = AesSimpleUtil.generateKeyViaMd5(key);

        String plainText = "Hello, AES Encryption!";
        String encryptedText = AesSimpleUtil.encrypt(plainText, secretKey);
        System.out.println("加密后的密文 (Base64): " + encryptedText);

        String decryptedText = AesSimpleUtil.decrypt(encryptedText, secretKey);
        System.out.println("解密后的明文: " + decryptedText);
    }
}
