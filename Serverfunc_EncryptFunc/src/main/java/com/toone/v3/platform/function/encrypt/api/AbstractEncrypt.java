package com.toone.v3.platform.function.encrypt.api;

/**
 * @Author xugang
 * @Date 2021/6/1 13:33
 */
public class AbstractEncrypt implements IEncrypt {
    @Override
    public CharSequence encrypt(String content) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    @Override
    public CharSequence encrypt(String content, String algorithmKey) throws Exception {
        // TODO Auto-generated method stub
        return encrypt(content);
    }

    @Override
    public CharSequence decrypt(String content) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException();
    }

    @Override
    public CharSequence decrypt(String content, String algorithmKey) throws Exception {
        // TODO Auto-generated method stub
        return decrypt(content);
    }
}
