package com.toone.v3.platform.function.encrypt;

import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.toone.v3.platform.function.encrypt.api.IEncrypt;
import com.toone.v3.platform.function.encrypt.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xugang
 * @Date 2021/6/1 13:34
 */
public class EncryptFactory {

    private static Map<String, IEncrypt> providerMap = new HashMap<String, IEncrypt>();

    static {
        providerMap.put("AES", new AESEncrypt());
        providerMap.put("SHA-1", new SHA1Encrypt());
        providerMap.put("SHA-256", new SHA256Encrypt());
        providerMap.put("SHA-384", new SHA384Encrypt());
        providerMap.put("SHA-512", new SHA512Encrypt());
        providerMap.put("MD5", new MD5Encrypt());
    }

    public static IEncrypt getProvider(String algorithm, Object algorithmKey, String funcCode) {
        algorithm = algorithm.toUpperCase().trim();
        IEncrypt encrypt = providerMap.get(algorithm);
        if(encrypt == null) {
            throw new ServerFuncException("函数【" + funcCode + "】的第2个参数符合枚举值：AES、MD5、SHA-1、SHA-256、SHA-384、SHA-512，参数2：" + algorithm);
        }

        if(encrypt instanceof AESEncrypt) {
            if(algorithmKey == null || algorithmKey.toString().equals("")) {
                throw new ServerFuncException("函数【" + funcCode + "】执行出错，选择AES加密时，必须指定密钥，参数2：" + algorithm + "，参数3：" + algorithmKey);
            }
        }

        return encrypt;
    }

}
