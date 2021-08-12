package com.toone.v3.platform.function.encrypt.impl;

import com.toone.v3.platform.function.encrypt.api.AbstractEncrypt;
import com.toone.v3.platform.function.encrypt.util.EncryptUtil;

/**
 * @Author xugang
 * @Date 2021/6/1 13:34
 */
public class AESEncrypt extends AbstractEncrypt {

    @Override
    public CharSequence encrypt(String content, String algorithmKey) throws Exception {
        return EncryptUtil.encryptAES(content, algorithmKey);
    }

    @Override
    public CharSequence decrypt(String content, String algorithmKey) throws Exception {
        return EncryptUtil.decryptAES(content, algorithmKey);
    }

}
