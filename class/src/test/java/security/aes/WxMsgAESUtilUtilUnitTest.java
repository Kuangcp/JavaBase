package security.aes;


import org.junit.Assert;
import org.junit.Test;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Paths;
import java.security.SecureRandom;

public class WxMsgAESUtilUtilUnitTest {


    @Test
    public void testStringEncrypt() throws Exception {
        // given
        String input = "test for that";

        // 生成一个key的重点在于 16 24 32 字节数组的随机数，将key base64后配置共享即可完成两端系统加密通信
        byte[] var2 = new byte[16];
        new SecureRandom().nextBytes(var2);
        new SecretKeySpec(var2, "AES");

        SecretKey key = AESUtil.generateKey(128);
        final String base64 = AESUtil.generateIvByte();

        // 固定iv密钥
        System.out.println(base64);
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        String algorithm = "AES/CBC/PKCS5Padding";

        // when
        String cipherText = AESUtil.encrypt(algorithm, input, key, ivParameterSpec);
        String plainText = AESUtil.decrypt(algorithm, cipherText, key, ivParameterSpec);

        // then
        Assert.assertEquals(input, plainText);
    }

    @Test
    public void testGivenFile_whenEncrypt_thenSuccess() throws Exception {
        // given
        SecretKey key = AESUtil.generateKey(128);
        String algorithm = "AES/CBC/PKCS5Padding";
        IvParameterSpec ivParameterSpec = AESUtil.generateIv();
        File inputFile = Paths.get("src/test/resources/origin.txt")
                .toFile();
        File encryptedFile = new File("baeldung.encrypted");
        File decryptedFile = new File("document.decrypted");

        // when
        AESUtil.encryptFile(algorithm, key, ivParameterSpec, inputFile, encryptedFile);
        AESUtil.decryptFile(algorithm, key, ivParameterSpec, encryptedFile, decryptedFile);

        // then
//        assertThat(inputFile).hasSameTextualContentAs(decryptedFile);
        encryptedFile.deleteOnExit();
        decryptedFile.deleteOnExit();
    }

    @Test
    public void givenObject_whenEncrypt_thenSuccess() throws Exception {
        // given
        Student student = new Student("Baeldung", 20);
        SecretKey key = AESUtil.generateKey(128);
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
        String password = "baeldung";
        String salt = "12345678";
        final String base64 = AESUtil.generateIvByte();

        // 固定iv密钥
        System.out.println(base64);
        IvParameterSpec ivParameterSpec = AESUtil.generateIv(base64);
        SecretKey key = AESUtil.getKeyFromPassword(password, salt);

        // when
        String cipherText = AESUtil.encryptPasswordBased(plainText, key, ivParameterSpec);
        String decryptedCipherText = AESUtil.decryptPasswordBased(cipherText, key, ivParameterSpec);

        // then
        Assert.assertEquals(plainText, decryptedCipherText);
    }

    /**
     */
    @Test
    public void testConfigWay() throws Exception {
        String plainText = "www.baeldung.com";
        String password = "baeldung";
        String salt = "12345678";
        final String iv = AESUtil.generateIvByte();
        final String result = encryptPass(plainText, password, salt, iv);
        final String origin = decryptPass(result, password, salt, iv);

        Assert.assertEquals(origin, plainText);

    }
    public String encryptPass(String text, String passwd, String salt, String iv) throws Exception {
        return AESUtil.encryptPasswordBased(text, AESUtil.getKeyFromPassword(passwd, salt), AESUtil.generateIv(iv));
    }

    public String decryptPass(String text, String passwd, String salt, String iv) throws Exception {
        return AESUtil.decryptPasswordBased(text, AESUtil.getKeyFromPassword(passwd, salt), AESUtil.generateIv(iv));
    }
}
