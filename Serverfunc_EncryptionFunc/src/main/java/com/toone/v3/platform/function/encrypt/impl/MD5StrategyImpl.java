package com.toone.v3.platform.function.encrypt.impl;

import com.toone.v3.platform.function.encrypt.api.DensityStrategy;

import com.toone.v3.platform.function.encrypt.util.Base64;

import java.security.MessageDigest;

/**
 * @Author xugang
 * @Date 2021/6/1 14:19
 */
public class MD5StrategyImpl implements DensityStrategy {

    @Override
    public String encryption(String data) throws Exception {
        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] out = m.digest(data.getBytes());

        return new String(Base64.encode(out));
    }

    @Override
    public String encryption(String data, String encryptionkey) throws Exception {
        return encryption(data);
    }

    @Override
    public String decrypt(String data) throws Exception {
        return data;
    }

    @Override
    public String decrypt(String data, String encryptionkey) throws Exception {
        return data;
    }
}
