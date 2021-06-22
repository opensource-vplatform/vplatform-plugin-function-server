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
 * 1. 对数值做除法运算<br>
 * 2. 如果除数为0，则返回NaN<br>
 * 3. 最后一位参数为小数保留位数<br>
 * 4. 结果按照保留小数位数进行四舍五入<br>
 * 代码示例:ServerDivide(arg1,arg2,...,argN,argN+1) 返回值为数字类型。<br>
 * 参数1--被除数(数字类型)；<br>
 * 参数2--除数(数字类型)；<br>
 * 参数N--除数(数字类型)；<br>
 * 参数N+1--小数保留位数(数字类型)；<br>
 * 返回值为数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:33
 */
public class ServerDivideFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.ServerDivide.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(ServerDivideFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        try {
            int size = context.getInputSize();
            if (size < 3) {
                throw new ServerFuncException("函数【" + funcCode + "】至少需要3个参数，当前参数个数：" + size);
            } else {
                Object lastParam = context.getInput(size - 1);
                if(!(lastParam instanceof Integer)) {
                    throw new ServerFuncException("函数【" + funcCode + "】最后一个参数不能为空，且必须是整数，当前值：" + lastParam);
                }
                int decimalDigit = (Integer) lastParam;

                BigDecimal result = BigDecimal.valueOf(0);
                for (int i = 0; i < size - 1; i++) {
                    Object param = context.getInput(i);
                    // 处理除数为 0 情况
                    if (0 == i && "0".equals(removeZero(param)))
                        continue;

                    BigDecimal tmpNum = obj2BigDecimal(param, i);
                    if (tmpNum == null) {
//                        throw new ServerFuncException("函数【" + funcCode + "】的第" + (i+1) + "个参数不能为空或者0");
                        outputVo.put("NaN");
                        outputVo.setSuccess(true);
                        return outputVo;
                    } else if (i == 0) {
                        result = tmpNum;
                    } else {
                        result = result.divide(tmpNum, decimalDigit, BigDecimal.ROUND_HALF_UP);
                    }
                }

                outputVo.put(result.toString());
                outputVo.setSuccess(true);
            }
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，msg=" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }

        return outputVo;
    }

    // 去除小数位最后的零
    // eg： 1.20 ==> 1.2 1.0 ==> 1
    private String removeZero(Object obj) {
        String val;
        if (obj == null)
            return null;
        else
            val = obj.toString();

        if (val.indexOf(".") > 0) {
            val = val.replaceAll("0+?$", "");
            val = val.replaceAll("[.]$", "");
        }
        return val;
    }

    private BigDecimal obj2BigDecimal(Object obj, int index) {
        return strBigDecimal(obj2String(obj), index);
    }

    private BigDecimal strBigDecimal(String val, int index) {
        if (val == null || "".equals(val.trim()) || "0".equals(removeZero(val.trim())))
            return null;
        try {
            return new BigDecimal(val);
        } catch (Exception e) {
            throw new ServerFuncException("函数【" + funcCode + "】参数不正确，要求参数为数值类型，参数" + (index + 1) + "：" + val);
        }
    }

    private String obj2String(Object obj) {
        if (obj == null)
            return null;
        return obj.toString();
    }
}
