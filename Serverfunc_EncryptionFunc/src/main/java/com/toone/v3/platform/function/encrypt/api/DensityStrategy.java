package com.toone.v3.platform.function.encrypt.api;

import com.toone.v3.platform.function.encrypt.Strategy;
import com.toone.v3.platform.function.encrypt.impl.AES_128StrategyImpl;
import com.toone.v3.platform.function.encrypt.impl.Base64StrategyImpl;
import com.toone.v3.platform.function.encrypt.impl.MD5StrategyImpl;

/**
 * @Author xugang
 * @Date 2021/6/1 14:19
 */
public interface DensityStrategy {

    String encryption(String data) throws Exception;

    String encryption(String data, String encryptionkey) throws Exception;

    String decrypt(String data) throws Exception;

    String decrypt(String data, String encryptionkey) throws Exception;

    abstract class Factory {

        private final static DensityStrategy md5 = new MD5StrategyImpl();
        private final static DensityStrategy base64 = new Base64StrategyImpl();
        private final static DensityStrategy aes = new AES_128StrategyImpl();
//        private final static DensityStrategy sha1 = new SHA1StrategyImpl();
//        private final static DensityStrategy sha256 = new SHA256StrategyImpl();
//        private final static DensityStrategy sha384 = new SHA384StrategyImpl();
//        private final static DensityStrategy sha512 = new SHA512StrategyImpl();
//        private final static DensityStrategy bcrypt = new BcryptStrategyImpl();

        public static DensityStrategy getDensityStrategyInstance(Strategy strategy) {
            switch (strategy) {
                case MD5:
                    return md5;
                case Base64:
                    return base64;
                case AES:
                    return aes;
//                case SHA1:
//                    return sha1;
//                case SHA256:
//                    return sha256;
//                case SHA384:
//                    return sha384;
//                case SHA512:
//                    return sha512;
//                case Bcrypt:
//                    return bcrypt;
                default:
                    return null;
            }
        }
    }
}
