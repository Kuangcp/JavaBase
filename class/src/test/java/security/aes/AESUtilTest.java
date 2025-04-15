package security.aes;


import com.github.kuangcp.util.ShowBinary;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.RepeatedTest;

import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class AESUtilTest {


    @Test
    public void testFlow() throws Exception {
        // 生成随机密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 128 192 256
        keyGenerator.init(128, new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();

        byte[] key = secretKey.getEncoded();
        System.out.println("AES 密钥：" + Base64.getEncoder().encodeToString(key));
        System.out.println("AES 密钥：" + ShowBinary.byteToHex(key));

        String content = "KeyGenerator keyGenerator = KeyGenerator.getInstance(\"AES\");";
        System.out.println("原文：" + content);

        byte[] ret = AESUtil.encrypt(key, content.getBytes());
        System.out.println("密文：" + Base64.getEncoder().encodeToString(ret));
        System.out.println("密文：" + ShowBinary.byteToHex(ret));

        byte[] raw = AESUtil.decrypt(key, ret);
        System.out.println("原文：" + new String(raw));
        Assert.assertEquals(content, new String(raw));
    }

    @Test
    @RepeatedTest(6)
    public void testHexKeyFlow() throws Exception {
        // 生成随机密钥
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 128 192 256
        keyGenerator.init(192, new SecureRandom());
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] key = secretKey.getEncoded();

//        System.out.println("Origin: " + key.length);
        String hexKey = ShowBinary.byteToHex(key);

        System.out.println("AES 密钥：" + hexKey);

        String content = "KeyGenerator keyGenerator = KeyGenerator.getInstance(\"AES\");";
        System.out.println("原文：" + content);

        System.out.println("------");
        byte[] ret = AESUtil.encrypt(hexKey, content.getBytes());
//        System.out.println("密文：" + Base64.getEncoder().encodeToString(ret));
        System.out.println("密文：" + ShowBinary.byteToHex(ret));

        byte[] raw = AESUtil.decrypt(hexKey, ret);
        System.out.println("原文：" + new String(raw));
        Assert.assertEquals(content, new String(raw));
    }


    @Test
    public void testStringEncrypt() throws Exception {
        // given
        String input = "test for that";

        // 生成一个key的重点在于 16 24 32 字节数组的随机数，将key base64后配置共享即可完成两端系统加密通信
        byte[] var2 = new byte[16];
        new SecureRandom().nextBytes(var2);
        new SecretKeySpec(var2, "AES");

        SecretKey key = AESUtil.randomKey(128);
        final String base64 = AESUtil.generateIvByte();

        // 固定iv密钥
        log.info("iv:{}", base64);
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        String algorithm = "AES/CBC/PKCS5Padding";

        // when
        String cipherText = AESUtil.encrypt(algorithm, input, key, ivParameterSpec);
        String plainText = AESUtil.decrypt(algorithm, cipherText, key, ivParameterSpec);

        log.info("cipher={} plain={}", cipherText, plainText);

        // then
        Assert.assertEquals(input, plainText);
    }


    @Test
    public void testGivenFile_whenEncrypt_thenSuccess() throws Exception {
        // given
        SecretKey key = AESUtil.randomKey(128);
        String algorithm = "AES/CBC/PKCS5Padding";
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        Path originPath = Paths.get("src/test/resources/origin.txt");
        File inputFile = originPath.toFile();
        File encryptedFile = new File("baeldung.encrypted");
        File decryptedFile = new File("document.decrypted");

        // when
        AESUtil.encryptFile(algorithm, key, ivParameterSpec, inputFile, encryptedFile);
        AESUtil.decryptFile(algorithm, key, ivParameterSpec, encryptedFile, decryptedFile);

        // then
//        assertThat(inputFile).hasSameTextualContentAs(decryptedFile);
        byte[] handle = Files.readAllBytes(Paths.get("document.decrypted"));
        byte[] origin = Files.readAllBytes(originPath);
        assertThat("", handle.length == origin.length);
        Assert.assertEquals(new String(handle), new String(origin));

        encryptedFile.deleteOnExit();
        decryptedFile.deleteOnExit();
    }

    @Test
    public void givenObject_whenEncrypt_thenSuccess() throws Exception {
        // given
        Student student = new Student("Baeldung", 20);
        SecretKey key = AESUtil.randomKey(128);
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        String algorithm = "AES/CBC/PKCS5Padding";

        // when
        SealedObject sealedObject = AESUtil.encryptObject(algorithm, student, key, ivParameterSpec);
        Student object = (Student) AESUtil.decryptObject(algorithm, sealedObject, key, ivParameterSpec);

        // then
        Assert.assertEquals(student, object);
    }

    @Test
    public void givenPassword_whenEncrypt_thenSuccess() throws Exception {
        // given
        String plainText = "www.baeldung.com";

        // 双方预先配置或通过通信达成共识的配置项
        String password = "baeldung";
        String salt = "12345678";
        final String base64 = AESUtil.generateIvByte();

        // 固定iv密钥
        log.info("iv={}", base64);
        SecretKey key = AESUtil.getKeyFromPassword(password, salt);

        // when
        String cipherText = AESUtil.encryptPasswordBased(plainText, key, AESUtil.generateIv(base64));
        String decryptedCipherText = AESUtil.decryptPasswordBased(cipherText, key, AESUtil.generateIv(base64));
        log.info("cipher={} decrypted={}", cipherText, decryptedCipherText);

        // then
        Assert.assertEquals(plainText, decryptedCipherText);
    }

    @Test
    public void testConfigWay() throws Exception {
        String plainText = "" + System.currentTimeMillis();
        String password = "baeldung";
        String salt = "12345678";
//        final String iv = AESUtil.generateIvByte();
        String iv = "D+OER4Pzbc+CtaXeVKlTEQ==";
        log.info("iv [{}] salt [{}]", iv, salt);
        // 即使固定了iv 初始化向量，在跨系统传输仍需要双方约定三个 变量比较麻烦
        final String result = AESUtil.encryptPass(plainText, password, salt, iv);
        final String origin = AESUtil.decryptPass(result, password, salt, iv);
        log.info("{} {}", result, origin);

        Assert.assertEquals(origin, plainText);
    }

    /**
     * 只保留一个密钥，iv使用0值，安全性降低，但是更方便
     */
    @Test
    public void testSimple() throws Exception {
        try {
            String key = "b423eb489f47dbe933f7e761946ec216";
            SecretKeySpec secretKey = AESUtil.generateKey(key);

            String plainText = "Hello, AES Encryption!";
            String encryptedText = AESUtil.encrypt(plainText, secretKey);
            System.out.println("加密后的密文 (Base64): " + encryptedText);

            String decryptedText = AESUtil.decrypt(encryptedText, secretKey);
            System.out.println("解密后的明文: " + decryptedText);
        } catch (Exception e) {
            log.error("", e);
        }
    }

}
