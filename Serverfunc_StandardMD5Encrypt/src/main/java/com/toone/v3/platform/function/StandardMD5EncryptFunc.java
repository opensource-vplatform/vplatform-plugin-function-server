package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

/**
 * 标准MD5加密<br>
 * <br>
 * 代码示例：StandardMD5Encrypt("123456","16","big") 返回值为："49BA59ABBE56E057"。<br>
 * 参数1--需要加密的字符串(必填，字符串类型)；<br>
 * 参数2--加密的位数(选填，字符串类型，有16位和32位两种类型，默认为32)；<br>
 * 参数3--输出的大小写(选填，字符串类型，有"big"和"small"两种类型，默认为big)；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:56
 */
public class StandardMD5EncryptFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.StandardMD5Encrypt.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(StandardMD5EncryptFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            int size = context.getInputSize();
            if (size == 1) {
                param1 = context.getInput(0);
            } else if (size == 2) {
                param1 = context.getInput(0);
                param2 = context.getInput(1);
            } else if (size == 3) {
                param1 = context.getInput(0);
                param2 = context.getInput(1);
                param3 = context.getInput(2);
            } else {
                throw new ServerFuncException("函数【】需要1个或者2个或者3个参数，当前参数个数：" + size);
            }
            String desVal = param1 == null ? "" : param1.toString();
            String unit = "32";
            String upperType = "big";
            if (size > 1) {
                if (param2 != null && param2.toString().equals("16")) {
                    unit = "16";
                }
                if (size > 2) {
                    if (param3 != null && param3.toString().equals("small")) {
                        upperType = "small";
                    }
                }
            }
            if (!desVal.equals("")) {
                desVal = hexMD5(desVal);
                if (unit.equals("16")) {
                    desVal = desVal.substring(8, 24);
                }
                if (upperType.equals("big")) {
                    desVal = desVal.toUpperCase();
                }
            }

            outputVo.put(desVal);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3, e);
        }
        return outputVo;
    }

    private String hexMD5(String value) throws Exception {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(value.getBytes("utf-8"));
            byte[] digest = messageDigest.digest();
            return byteToHexString(digest);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public static String byteToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            int v = bytes[i];
            if (v < 0) {
                v += 256;
            }
            String n = Integer.toHexString(v);
            if (n.length() == 1)
                n = "0" + n;
            builder.append(n);
        }

        return builder.toString();
    }
}
