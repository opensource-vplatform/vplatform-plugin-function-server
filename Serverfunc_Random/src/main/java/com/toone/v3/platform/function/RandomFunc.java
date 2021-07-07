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
 * 随机数<br>
 * <br>
 * 代码示例：Random(1.5,2.5,3)返回值为1.508。<br>
 * 参数1--随机区域的最小值(数字类型)；<br>
 * 参数2--随机区域的最大值(数字类型)；<br>
 * 参数3--返回值保留的小数位数(整数类型)；<br>
 * 返回值类型：数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:45
 */
public class RandomFunc implements IFunction {

    // 函数编码
    private final static String funcCode = RandomRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(RandomFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 3);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            param3 = context.getInput(2);
            service.checkParamNull(funcCode, param1, param2, param3);

            String min = param1.toString();
            String max = param2.toString();
            String fixed = param3.toString();

            if (!isEmpty(min)) {
                if (!isNumeric(min))
                    throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须是数字类型，参数1：" + param1);
            }
            if (!isEmpty(max)) {
                if (!isNumeric(max))
                    throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须是数字类型，参数2：" + param2);
            }
            if (!isEmpty(fixed)) {
                if (!isInteger(fixed))
                    throw new ServerFuncException("函数【" + funcCode + "】的第3个参数必须是正整数类型，参数3：" + param3);
            }

            // 0 ~ 1 之间的随机数。
            BigDecimal randomNum = new BigDecimal(Math.random());
            BigDecimal num = new BigDecimal(0);
            // 最大值、最小值存在条件
            if (!isEmpty(min) && !isEmpty(max)) {
                BigDecimal minBig = new BigDecimal(min);
                BigDecimal maxBig = new BigDecimal(max);
                if (minBig.compareTo(maxBig) == 0) {
                    outputVo.put(minBig);
                    outputVo.setSuccess(true);
                    return outputVo;
                }
                if (minBig.compareTo(maxBig) > 0)
                    throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须小于等于第2个参数，参数1：" + param1 + "，参数2：" + param2);
                //(max - min) * randomNum + min
                BigDecimal n1 = maxBig.subtract(minBig);
                BigDecimal n2 = n1.multiply(randomNum);
                num = n2.add(minBig);
            } else if (isEmpty(min) && !isEmpty(max)) {
                BigDecimal maxBig = new BigDecimal(max);
                //max - max * randomNum
                BigDecimal n3 = maxBig.multiply(randomNum);
                num = maxBig.subtract(n3);
            } else if (!isEmpty(min) && isEmpty(max)) {
                BigDecimal minBig = new BigDecimal(min);
                //min + min * randomNum
                BigDecimal n4 = minBig.multiply(randomNum);
                num = minBig.add(n4);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数、第2个参数不能都为空，参数1：" + param1 + "，参数2：" + param2);
            }
            if (!isEmpty(fixed)) {
                BigDecimal ret = num.setScale(Integer.parseInt(fixed), BigDecimal.ROUND_DOWN);
                outputVo.put(ret);
            } else {
                outputVo.put(num);
            }

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

    private boolean isNumeric(String str) {
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        if (!str.matches(regex)) {
            return false;
        }
        return true;
    }

    private boolean isInteger(String str) {
        String regex = "^\\+?[0-9]*$";
        if (!str.matches(regex)) {
            return false;
        }
        return true;
    }

    private boolean isEmpty(String str) {
        if (str == null || str == "") {
            return true;
        }
        return false;
    }
}
