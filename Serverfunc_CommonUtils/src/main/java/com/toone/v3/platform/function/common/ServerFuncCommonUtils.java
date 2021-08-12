package com.toone.v3.platform.function.common;

import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.service.IOutService;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * 服务端函数公共方法及常量定义
 *
 * @Author xugang
 * @Date 2021/5/28 13:43
 */
public interface ServerFuncCommonUtils extends IOutService {

    public final static String OutServer_Code = "com_toone_v3_platform_Serverfunc_CommonUtils_serverFuncCommonUtils";

    /**
     * 检查参数个数
     *
     * @param funcName 函数名
     * @param context  函数上下文
     * @param size     预期的参数个数
     * @throws ServerFuncException 参数个数不符合要求时，抛出异常
     */
    default void checkParamSize(String funcName, IFuncContext context, int size) throws ServerFuncException {
        int inputSize = context.getInputSize();
        if (inputSize != size) {
            throw new ServerFuncException("函数【" + funcName + "】要求参数个数：" + size + ", 实际参数个数：" + inputSize);
        }
    }

    /**
     * 检查参数是否为null
     *
     * @param funcName 函数名
     * @param objs     参数列表
     * @throws ServerFuncException 当参数中存在null时，抛出异常
     */
    default void checkParamNull(String funcName, Object... objs) throws ServerFuncException {
        if (objs != null && objs.length > 0) {
            for (int i = 0; i < objs.length; i++) {
                if (objs[i] == null) {
                    throw new ServerFuncException("函数【" + funcName + "】的第" + (i + 1) + "个参数不能为空");
                }
            }
        }
    }

    /**
     * 检查参数是否为null或者空字符串
     *
     * @param funcName 函数名
     * @param objs     参数列表
     * @throws ServerFuncException 当参数中存在null或者空字符串时，抛出异常
     */
    default void checkParamBlank(String funcName, Object... objs) throws ServerFuncException {
        if (objs != null && objs.length > 0) {
            for (int i = 0; i < objs.length; i++) {
                if (objs[i] == null || objs[i].toString().trim().equals("")) {
                    throw new ServerFuncException("函数【" + funcName + "】的第" + (i + 1) + "个参数不能为空");
                }
            }
        }
    }

    /**
     * 检查参数是否为数字类型
     *
     * @param funcName 函数名
     * @param objs     参数列表
     * @throws ServerFuncException 当参数中存在null时，抛出异常
     */
    default void checkParamNumeric(String funcName, Object... objs) throws ServerFuncException {
        if (objs != null && objs.length > 0) {
            for (int i = 0; i < objs.length; i++) {
                if (objs[i] == null || !isNumeric(objs[i].toString())) {
                    throw new ServerFuncException("函数【" + funcName + "】的第" + (i + 1) + "个参数必须是数字类型");
                }
            }
        }
    }

    /**
     * 检查参数是否为数字类型
     *
     * @param funcName 函数名
     * @param obj      参数
     * @param index    参数序号
     * @throws ServerFuncException 当参数中存在null时，抛出异常
     */
    default void checkParamNumeric(String funcName, Object obj, int index) throws ServerFuncException {
        if (obj == null || !isNumeric(obj.toString())) {
            throw new ServerFuncException("函数【" + funcName + "】的第" + index + "个参数必须是数字类型");
        }
    }

    /**
     * 判断字符串是否为非零正整数
     *
     * @param funcName 函数名
     * @param obj      参数
     * @param index    参数顺序
     * @throws ServerFuncException 如果参数不满足非零正整数，抛出异常
     */
    default void checkParamInteger(String funcName, Object obj, int index) throws ServerFuncException {
        boolean isMatch = false;
        if (obj != null) {
            Pattern pattern = Pattern.compile("^[1-9]\\d*$");
            isMatch = pattern.matcher(obj.toString().trim()).matches();
        }
        if (!isMatch) {
            throw new ServerFuncException("函数【" + funcName + "】的第" + index + "个参数必须是非零正整数");
        }
    }

    /**
     * 是否数字
     *
     * @param str 字符串
     * @return 数字返回true，否则返回false
     */
    default boolean isNumeric(String str) {
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        if (!str.matches(regex)) {
            return false;
        }
        return true;
    }

    /**
     * 获取日期格式
     *
     * @param funcName 函数名
     * @param obj      参数
     * @param index    参数序号
     * @return
     * @throws ServerFuncException
     */
    default SimpleDateFormat getSimpleDateFormat(String funcName, Object obj, int index) throws ServerFuncException {
        if (obj == null) {
            throw new ServerFuncException("函数【" + funcName + "】的第" + index + "个参数必须满足格式：yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss，当前值：" + obj);
        } else {
            String str = obj.toString().trim();
            if (str.length() > 10) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } else {
                return new SimpleDateFormat("yyyy-MM-dd");
            }
        }
    }

    /**
     * 弧度转角度
     *
     * @param arg 弧度
     * @return 角度
     */
    default double radianToAngle(double arg) {
        return 180 * arg / Math.PI;
    }

    /**
     * 角度转弧度
     *
     * @param arg 角度
     * @return 弧度
     */
    default double angleToRadian(double arg) {
        return arg * Math.PI / 180;
    }
}
