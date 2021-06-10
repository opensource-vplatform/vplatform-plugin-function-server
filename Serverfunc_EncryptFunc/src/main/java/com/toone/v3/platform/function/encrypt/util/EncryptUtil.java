package com.toone.v3.platform.function.encrypt.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @Author xugang
 * @Date 2021/6/1 13:37
 */
public class EncryptUtil {
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
    /**
     * byte转16进制。小端
     * @param bytes
     * @return
     */
    private static String to16Hex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            // 左移并与运算，小端
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    // ------------------------------------ encrypt start ---------------------------
    /**
     * 加密函数
     * @param content 需要加密的字符串
     * @param algorithm 加密算法，不区分大小写，支持【md5、SHA-1、SHA-256、SHA-384、SHA-512】。
     * @return 加密完成的字符串，16进制，小端
     * @throws Exception
     */
    public static String encrypt(String content, String algorithm) throws Exception {
        return encrypt(content, algorithm, Charset.forName("UTF-8"));
    }
    /**
     * 加密函数
     * @param content 需要加密的字符串
     * @param algorithm 加密算法，支持【md5、SHA-1、SHA-256、SHA-384、SHA-512】
     * @param charset 字符串编码格式
     * @return 加密完成的字符串，16进制，小端
     * @throws Exception
     */
    public static String encrypt(String content, String algorithm, Charset charset) throws Exception {
        if (content==null || algorithm==null || charset==null) {
            throw new NullPointerException("content="+content + "algorithm="+algorithm + "charset="+charset);
        }

        // 加密
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] bytes = digest.digest(content.getBytes(charset));

        return to16Hex(bytes);
    }
    // ------------------------------------ encrypt end ---------------------------



    // ------------------------------------ base64 start ---------------------------
    public static String encryptBase64(String content) {
        if (content==null) {
            throw new NullPointerException("content=" + content);
        }
        return encryptBase64(content, Charset.forName("UTF-8"));
    }

    public static String encryptBase64(String content, Charset charset) {
        if (content==null || charset==null) {
            throw new NullPointerException("content=" + content + "charset=" + charset);
        }

        byte[] results = Base64.encodeBase64(content.getBytes(charset));
        return new String(results, charset);
    }

    public static String decryptBase64(String content) {
        if (content==null) {
            throw new NullPointerException("content=" + content);
        }

        return decryptBase64(content, Charset.forName("UTF-8"));
    }
    public static String decryptBase64(String content, Charset charset) {
        if (content==null || charset==null) {
            throw new NullPointerException("content=" + content + "charset=" + charset);
        }

        byte[] results = Base64.decodeBase64(content.getBytes(charset));
        return new String(results, charset);
    }
    // ------------------------------------ base64 end ---------------------------



    // ------------------------------------ AES Encrypt start ---------------------------
    public static String encryptAES(String content, String algorithmKey) throws Exception {
        return encryptAES(content, algorithmKey, Charset.forName("UTF-8"));
    }
    /**
     * AES加密
     *
     * 在线AES加密目前有2种：
     * 	https://www.sojson.com/encrypt_aes.html -> 采取模式与该实现不同
     * 	https://the-x.cn/cryptography/Aes.aspx -> 与该加密一致。加密模式为ECB，填充为PKCS5Padding，数据块为128
     *
     * @param content 按照16byte进行分组，不足16byte时用特定的Padding（采用PKCS5Padding）进填充。
     * @param algorithmKey 密钥。仅大小写字母以及数字。长度支持128bit，超过将截取，不足补0。
     * @param charset 需要加密内容的编码格式
     * @return	经过base64编码
     * @throws Exception
     */
    public static String encryptAES(String content, String algorithmKey, Charset charset) throws Exception {
        if (content==null || algorithmKey==null || charset==null) {
            throw new NullPointerException("content="+content + "algorithmKey="+algorithmKey + "charset="+charset);
        }
        byte[] bytes = algorithmKey.getBytes(charset);
        bytes = Arrays.copyOfRange(bytes, 0, 16);

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // 密钥
            SecretKeySpec key = new SecretKeySpec(bytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] result = cipher.doFinal(content.getBytes(charset));
            result = Base64.encodeBase64(result);
            return new String(result, charset);
        } catch (Exception e) {
            throw e;
        }
    }
    // ------------------------------------ AES Encrypt end ---------------------------



    // ------------------------------------ AES Decrypt start ---------------------------
    public static String decryptAES(String content, String algorithmKey) throws Exception {
        return decryptAES(content, algorithmKey, Charset.forName("UTF-8"));
    }
    public static String decryptAES(String content, String algorithmKey, Charset charset) throws Exception {
        if (content==null || algorithmKey==null || charset==null) {
            throw new NullPointerException("content="+content + "algorithmKey="+algorithmKey + "charset="+charset);
        }

        // 转换为AES专用密钥
        byte[] bytes = algorithmKey.getBytes(charset);
        bytes = Arrays.copyOfRange(bytes, 0, 16);

        byte[] data = content.getBytes(charset);
        data = Base64.decodeBase64(data);

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            // 密钥
            SecretKeySpec key = new SecretKeySpec(bytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] result = cipher.doFinal(data);
            return new String(result, charset);
        } catch (Exception e) {
            throw e;
        }
    }
    // ------------------------------------ AES Decrypt end ---------------------------
}
