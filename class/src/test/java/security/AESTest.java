//package security;
//
//import static org.hamcrest.Matchers.equalTo;
//import static org.junit.Assert.assertThat;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.security.Provider;
//import java.security.Security;
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.kafka.common.utils.ByteUtils;
//import org.junit.Test;
//
///**
// * @author https://github.com/kuangcp on 2020-11-01 21:35
// */
//public class AESTest {
//
//  @Test
//  public void testEncrypt() throws Exception {
//    final Provider[] providers = Security.getProviders();
//    for (Provider provider : providers) {
//      System.out.println(provider);
//    }
//    String key = "qwertyuiqwertyuo";
//    final String text = "123456";
//    String result = AES.aesEncrypt(text, key);
//    System.out.println(result);
//    String origin = AES.aesDecrypt(result, "qwertyuiqwertoki");
//    assertThat(origin, equalTo(text));
//  }
//
//  /**
//   * DES解密：CTR操作模式     *      * @param cipherText：密文     * @param key：密钥     * @param iv：初始计数器 *
//   *
//   * @return 原文
//   */
//  public static String decryptCTR(String cipherText, String key, String counter) {
//    try {
//      // 获取解密密钥
//      byte[] keyBytes = getKey(key);
//      Key keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
//      // 获取初始矢量
//      byte[] ivBytes = counter.getBytes(StandardCharsets.UTF_8);
//      IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
//      // 获取Cipher实例
//      Cipher cipher = Cipher.getInstance("DES/CTR/PKCS5Padding");
//      // 初始化Cipher实例，设置执行模式，解密密钥和初始计数器
//      cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
//      // 解密
//
//      byte[] cipherTextBytes = hex2byte(cipherText);
//      byte[] plainTextBytes = cipher.doFinal(cipherTextBytes);
//      // 返回明文
//      return new String(plainTextBytes, StandardCharsets.UTF_8);
//    } catch (Exception e) {
//      System.out.println("CTR解密异常");
//      e.printStackTrace();
//    }
//    return null;
//  }
//
//  @Test
//  public void testdd() throws Exception {
//
//  }