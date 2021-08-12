package com.toone.v3.platform.function.encrypt;

/**
 * @Author xugang
 * @Date 2021/6/1 14:22
 */
public enum Strategy {
    MD5("MD5"),
    Base64("Base64"),
    AES("AES");
//    SHA1("SHA1"),
//    SHA256("SHA256"),
//    SHA384("SHA384"),
//    SHA512("SHA512"),
//    Bcrypt("Bcrypt");

    private String	key;

    private Strategy(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

    public static Strategy get(String value) {
        Strategy[] strategys = Strategy.values();
        for (Strategy strategy : strategys) {
            if (strategy.key.equalsIgnoreCase(value)) {
                return strategy;
            }
        }
        return null;
    }
}
