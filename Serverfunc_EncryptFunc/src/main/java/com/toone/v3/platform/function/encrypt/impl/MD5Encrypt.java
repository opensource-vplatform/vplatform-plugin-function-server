package com.toone.v3.platform.function.encrypt.impl;

import com.toone.v3.platform.function.encrypt.api.AbstractEncrypt;
import com.toone.v3.platform.function.encrypt.util.EncryptUtil;

/**
 * @Author xugang
 * @Date 2021/6/1 13:35
 */
public class MD5Encrypt extends AbstractEncrypt {

    @Override
    public CharSequence encrypt(String content) throws Exception {
        return EncryptUtil.encrypt(content, "md5");
    }
}
