package security.aes;

import com.github.kuangcp.util.ShowBinary;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 *
 * @author Kuangcp
 * 2025-12-18 21:01
 */
public class AesSimpleUtil {

    public static byte[] encrypt(String hexKey, byte[] data) throws Exception {
        return encrypt(ShowBinary.hexToByte(hexKey), data);
    }

    public static byte[] decrypt(String hexKey, byte[] data) throws Exception {
        return decrypt(ShowBinary.hexToByte(hexKey), data);
    }

    /**
     * 加密
     *
     * @param key  密钥
     * @param data 加密数据
     * @return 密文
     */
    public static byte[] encrypt(byte[] key, byte[] data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param key  密钥
     * @param data 密文
     * @return 解密后的数据
     */
    public static byte[] decrypt(byte[] key, byte[] data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return cipher.doFinal(data);
    }


    // 密钥长度（AES支持128、192、256位）
    private static final int KEY_SIZE = 256;

    // 固定IV 0值
    private static final byte[] FIXED_IV = new byte[16]; // AES块大小为16字节

    /**
     * @param key 字符串长度 允许任意，但是这里选择了256，最好要32位
     */
    public static SecretKeySpec generateKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        byte[] keyArray = new byte[KEY_SIZE / 8];
        System.arraycopy(keyBytes, 0, keyArray, 0, Math.min(keyBytes.length, keyArray.length));
        return new SecretKeySpec(keyArray, "AES");
    }

    /**
     * 使用MD5就是使用了 128位长度的密钥
     */
    public static SecretKeySpec generateKeyViaMd5(String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] keyBytes = md5.digest(key.getBytes(StandardCharsets.UTF_8));
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("derive AES key error", e);
        }
    }

    // 加密方法
    public static String encrypt(String plainText, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new javax.crypto.spec.IvParameterSpec(FIXED_IV));
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // 解密方法
    public static String decrypt(String encryptedText, SecretKeySpec key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new javax.crypto.spec.IvParameterSpec(FIXED_IV));
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
