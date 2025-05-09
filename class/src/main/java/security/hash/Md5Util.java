package security.hash;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Optional;

/**
 * @author Kuangcp
 * 2025-05-09 17:18
 */
@Slf4j
public class Md5Util {

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    /**
     * 性能更好
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToHexBuilder(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 逐块计算MD5值，缓存区是512整数倍即可
     */
    public static Optional<String> urlMd5(String urlString) {
        if (StringUtils.isBlank(urlString)) {
            return Optional.empty();
        }

        try {
            URL url = new URL(urlString);
            // 获取MD5摘要算法实例
            MessageDigest digest = MessageDigest.getInstance("MD5");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // 打开URL连接并获取输入流
            try (InputStream is = connection.getInputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                // 逐块读取内容并更新摘要
                while ((bytesRead = is.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }

            // 获取最终的摘要值（字节数组）
            byte[] hashBytes = digest.digest();
            return Optional.of(bytesToHex(hashBytes));
        } catch (Exception e) {
            log.error("计算文件md5异常,url={}", urlString, e);
        }

        return Optional.empty();
    }

    /**
     * 本地文件完整计算MD5
     */
    public static Optional<String> fileMd5(String path) {
        try {
            byte[] data = Files.readAllBytes(Paths.get(path));
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(data);
            return Optional.of(bytesToHex(hash));
        } catch (Exception e) {
            log.error("", e);
        }
        return Optional.empty();
    }

    /**
     * 字符串计算MD5
     */
    public static Optional<String> stringMd5(String raw) {
        try {
            byte[] data = raw.getBytes();
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(data);
            return Optional.of(bytesToHex(hash));
        } catch (Exception e) {
            log.error("", e);
        }
        return Optional.empty();
    }
}
