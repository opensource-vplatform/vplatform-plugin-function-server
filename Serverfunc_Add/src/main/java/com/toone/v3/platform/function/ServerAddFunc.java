package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 函数加法<br>
 * <br>
 * 1. 对数值做加法运算<br>
 * 2. 最后一位参数为小数保留位数<br>
 * 3. 结果按照保留小数位数进行四舍五入<br>
 * 代码示例:ServerAdd(arg1,arg2,...,argN,argN+1) 返回值为数字类型。<br>
 * 参数1--被加数(数字类型);<br>
 * 参数2--加数(数字类型);<br>
 * 参数N--加数(数字类型);<br>
 * 参数N+1--小数保留位数(数字类型);<br>
 * 返回值为数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/27 11:49
 */
public class ServerAddFunc implements IFunction {

    private final static String funcCode = ServerAddRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(ServerAddFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();

        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            int size = context.getInputSize();
            if(size < 3) {
                throw new ServerFuncException("函数【" + funcCode + "】至少需要3个参数，当前参数个数：" + size);
            }

            Object lastParam = context.getInput(size - 1);
            if(!(lastParam instanceof Integer)) {
                throw new ServerFuncException("函数【" + funcCode + "】最后一个参数不能为空，且必须是整数，当前值：" + lastParam);
            }
            int scal = (Integer) lastParam;

            BigDecimal result = new BigDecimal(0);
            BigDecimal mulValue = getDecimailDigit(context);
            for (int i = 0; i < size - 1; i++) {
                BigDecimal tmpNum = obj2BigDecimal(context.getInput(i), i);
                if (tmpNum == null)
                    result = result.add(new BigDecimal(0).multiply(mulValue));
                else
                    result = result.add(tmpNum.multiply(mulValue));
            }
            result = result.divide(mulValue, scal, BigDecimal.ROUND_HALF_UP);

            outputVo.setSuccess(true);
            outputVo.put(result.toString());
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

    // 处理加法减法保留位数
    private BigDecimal getDecimailDigit(IFuncContext context) {
        int result = 1;
        int size = context.getInputSize();
        for (int i = 0; i < size - 1; i++) {
            // 去除小数的结尾数字零
            String tmpNum = "";
            if(context.getInput(i)==null){
                tmpNum = "0";
            }else{
                tmpNum = removeZero(context.getInput(i).toString());
            }

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

    private BigDecimal obj2BigDecimal(Object obj, int index) {
        return strBigDecimal(obj2String(obj), index);
    }

    private BigDecimal strBigDecimal(String val, int index) {
        if (val == null || "".equals(val.trim()))
            return null;
        try {
            return new BigDecimal(val);
        } catch (Exception e) {
            throw new ServerFuncException("函数【" + funcCode + "】第" + (index+1) + "个参数不正确，要求参数为数值类型，实际参数为"
                    + val);
        }
    }

    private String obj2String(Object obj) {
        if (obj == null)
            return null;
        return obj.toString();
    }
}
