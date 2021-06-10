package com.toone.v3.platform.function.encrypt.api;

/**
 * @Author xugang
 * @Date 2021/6/1 13:33
 */
public interface IEncrypt {
    /**
     * 加密
     * @param content 明文
     * @return
     */
    CharSequence encrypt(String content) throws Exception;
    /**
     * 加密
     * @param content 明文
     * @param algorithmKey 密钥
     * @return
     */
    CharSequence encrypt(String content, String algorithmKey) throws Exception;

    // *********************************************************************************

    /**
     * 解密
     * @param content 密文
     * @return
     */
    CharSequence decrypt(String content) throws Exception;
    /**
     * 解密
     * @param content 密文
     * @param algorithmKey 密钥
     * @return
     */
    CharSequence decrypt(String content, String algorithmKey) throws Exception;
}
