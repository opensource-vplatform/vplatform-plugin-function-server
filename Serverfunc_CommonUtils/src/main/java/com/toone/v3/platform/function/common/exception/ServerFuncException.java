package com.toone.v3.platform.function.common.exception;

/**
 * 服务端函数异常类
 *
 * @Author xugang
 * @Date 2021/5/28 14:17
 */
public class ServerFuncException extends RuntimeException {

    private static final long serialVersionUID = 6036548387058481785L;

    /**
     * 构造函数
     *
     * @param message 异常信息
     */
    public ServerFuncException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param throwable 堆栈
     */
    public ServerFuncException(Throwable throwable) {
        super(throwable);
    }

    /**
     * 构造函数
     *
     * @param message 异常信息
     * @param throwable 堆栈
     */
    public ServerFuncException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
