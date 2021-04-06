package security;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

public class AES {

  //密钥 (需要前端和后端保持一致)
  private static final String KEY = "qwertyuiqwertyui";
  //算法
  private static final String ALGORITHMSTR = "AES/CTR/PKCS5Padding";

  /**
   * aes解密
   *
   * @param encrypt 内容
   */
  public static String aesDecrypt(String encrypt) {
    try {
      return aesDecrypt(encrypt, KEY);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * aes加密
   */
  public static String aesEncrypt(String content) {
    try {
      return aesEncrypt(content, KEY);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * 将byte[]转为各种进制的字符串
   *
   * @param bytes byte[]
   * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
   * @return 转换后的字符串
   */
  public static String binary(byte[] bytes, int radix) {
    return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
  }

  /**
   * base 64 encode
   *
   * @param bytes 待编码的byte[]
   * @return 编码后的base 64 code
   */
  public static String base64Encode(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  /**
   * base 64 decode
   *
   * @param base64Code 待解码的base 64 code
   * @return 解码后的byte[]
   */
  public static byte[] base64Decode(String base64Code) throws Exception {
    return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
  }


  /**
   * AES加密
   *
   * @param content 待加密的内容
   * @param encryptKey 加密密钥
   * @return 加密后的byte[]
   */
  public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128);
    Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

    return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
  }


  /**
   * AES加密为base 64 code
   *
   * @param content 待加密的内容
   * @param encryptKey 加密密钥
   * @return 加密后的base 64 code
   */
  public static String aesEncrypt(String content, String encryptKey) throws Exception {
    return base64Encode(aesEncryptToBytes(content, encryptKey));
  }

  /**
   * AES解密
   *
   * @param encryptBytes 待解密的byte[]
   * @param decryptKey 解密密钥
   * @return 解密后的String
   */
  public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128);

    Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
    byte[] decryptBytes = cipher.doFinal(encryptBytes);
    return new String(decryptBytes);
  }


  /**
   * 将base 64 code AES解密
   *
   * @param encryptStr 待解密的base 64 code
   * @param decryptKey 解密密钥
   * @return 解密后的string
   */
  public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
    return StringUtils.isEmpty(encryptStr) ? null
        : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
  }

  /**
   * 测试
   */
  public static void main(String[] args) throws Exception {
    String content = "123";
    System.out.println("加密前：" + content);
    System.out.println("加密密钥和解密密钥：" + KEY);
    String encrypt = aesEncrypt(content, KEY);
    System.out.println("加密后：" + encrypt);
    String decrypt = aesDecrypt(encrypt, KEY);
    System.out.println("解密后：" + decrypt);
  }
}