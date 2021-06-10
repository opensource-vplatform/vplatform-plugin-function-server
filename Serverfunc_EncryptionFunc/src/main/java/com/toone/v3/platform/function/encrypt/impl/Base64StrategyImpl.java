package com.toone.v3.platform.function.encrypt.impl;

import com.toone.v3.platform.function.encrypt.api.DensityStrategy;
import com.toone.v3.platform.function.encrypt.util.Base64;

/**
 * @Author xugang
 * @Date 2021/6/1 14:20
 */
public class Base64StrategyImpl implements DensityStrategy {

    @Override
    public String encryption(String data) throws Exception {
        return new String(Base64.encode(data.getBytes("utf-8")));
    }

    @Override
    public String encryption(String data, String encryptionkey) throws Exception {
        return encryption(data);
    }

    @Override
    public String decrypt(String data) throws Exception {
        return new String(Base64.decode(data));
    }

    @Override
    public String decrypt(String data, String encryptionkey) throws Exception {
        return decrypt(data);
    }
}
