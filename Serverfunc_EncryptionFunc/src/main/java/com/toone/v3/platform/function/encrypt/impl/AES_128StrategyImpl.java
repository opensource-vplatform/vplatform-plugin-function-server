package com.toone.v3.platform.function.encrypt.impl;

import com.toone.v3.platform.function.encrypt.api.DensityStrategy;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author xugang
 * @Date 2021/6/1 14:21
 */
public class AES_128StrategyImpl implements DensityStrategy {

    // 默认密钥
    private static final String DEFAULT_ENCRYPTIONKEY = "12345678abcdefgh";

    @Override
    public String encryption(String data) throws Exception {
        return encryption(data, DEFAULT_ENCRYPTIONKEY);
    }

    @Override
    public String decrypt(String data) throws Exception {
        return decrypt(data, DEFAULT_ENCRYPTIONKEY);
    }

    @Override
    public String encryption(String data, String encryptionkey) throws Exception {
        if(encryptionkey == null || "".equals(encryptionkey)) {
            encryptionkey = DEFAULT_ENCRYPTIONKEY;
        }
        byte[] raw = encryptionkey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return new String(cipher.doFinal(hexStringToByte(data)));

    }

    @Override
    public String decrypt(String data, String encryptionkey) throws Exception {
        if(encryptionkey == null || "".equals(encryptionkey)) {
            encryptionkey = DEFAULT_ENCRYPTIONKEY;
        }
        byte[] raw = encryptionkey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return new String(cipher.doFinal(hexStringToByte(data)));
    }

    private byte[] hexStringToByte(String hexString) {
        byte[] raw = new byte[hexString.length()/2];

        for(int i=0;i<raw.length;i++) {
            raw[i] = Integer.decode("0x" + hexString.substring(i*2, i*2+2)).byteValue();
        }
        return raw;
    }
}
