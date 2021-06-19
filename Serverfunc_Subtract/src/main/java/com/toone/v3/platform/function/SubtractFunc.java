package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 减法<br>
 * <br>
 * 代码示例:ServerSubtract(arg1,arg2,...,argN,argN+1) 返回值为数字类型。<br>
 * 参数1--被减数(数字类型)；<br>
 * 参数2--减数(数字类型)；<br>
 * 参数N--减数(数字类型)；<br>
 * 参数N+1--小数保留位数(数字类型)；<br>
 * 返回值为数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:50
 */
public class SubtractFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.ServerSubtract.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(SubtractFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object last = null;
        try {
            int size = context.getInputSize();
            if (size < 3) {
                throw new ServerFuncException("函数【" + funcCode + "】至少需要3个参数，当前参数个数：" + size);
            }
            last = context.getInput(size - 1);
            int decimalDigit = 0;
            try {
                decimalDigit = Integer.parseInt(removeZero(last.toString()));
            } catch (Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】最后一个参数（小数位数）必须为整数，最后一个参数：" + last);
            }

            BigDecimal result = new BigDecimal(0);
            BigDecimal mulValue = getDecimailDigit(context);
            for (int i = 0; i < size - 1; i++) {
                BigDecimal tmpNum = obj2BigDecimal(context.getInput(i));

                if (tmpNum == null) {
                    outputVo.put("NaN");
                    outputVo.setSuccess(true);
                    return outputVo;
                } else if (i == 0) {
                    result = tmpNum.multiply(mulValue);
                    continue;
                } else
                    result = result.subtract(tmpNum.multiply(mulValue));
            }
            result.divide(mulValue, decimalDigit, BigDecimal.ROUND_HALF_UP)

            outputVo.put(res);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }

    // 处理加法减法保留位数
    private BigDecimal getDecimailDigit(IFuncContext context) {
        int result = 1;
        for (int i = 0; i < context.getInputSize() - 1; i++) {
            // 去除小数的结尾数字零
            String tmpNum = removeZero(context.getInput(i).toString());
            String[] tmpNums = tmpNum.split("\\.");
            if (tmpNums.length == 2) {
                int decimalDigit = tmpNums[1].length();
                result = result > decimalDigit ? result : decimalDigit;
            }
        }
        return (new BigDecimal("10")).pow(result);
    }

    // 去除小数位最后的零
    // eg： 1.20 ==> 1.2 1.0 ==> 1
    private String removeZero(String val) {
        if (val == null)
            return null;

        if (val.indexOf(".") > 0) {
            val = val.replaceAll("0+?$", "");
            val = val.replaceAll("[.]$", "");
        }
        return val;
    }

    private static BigDecimal obj2BigDecimal(Object obj) {
        return strBigDecimal(obj2String(obj));
    }

    private static BigDecimal strBigDecimal(String val) {
        if (val == null || "".equals(val.trim()))
            return null;
        try {
            return new BigDecimal(val);
        } catch (Exception e) {
            throw new ServerFuncException("ServerSubtract函数参数不正确，要求参数为数值类型，实际参数为" + val);
        }
    }

    private static String obj2String(Object obj) {
        if (obj == null)
            return null;
        return obj.toString();
    }
}
