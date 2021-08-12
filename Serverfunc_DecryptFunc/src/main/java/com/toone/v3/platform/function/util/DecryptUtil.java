package com.toone.v3.platform.function.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @Author xugang
 * @Date 2021/6/17 10:23
 */
public class DecryptUtil {

    public static String decryptAES(String content, String algorithmKey) throws Exception {
        return decryptAES(content, algorithmKey, Charset.forName("UTF-8"));
    }

    public static String decryptAES(String content, String algorithmKey, Charset charset) throws Exception {
        if (content == null || algorithmKey == null || charset == null) {
            throw new NullPointerException("content=" + content + "algorithmKey=" + algorithmKey + "charset=" + charset);
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
}
