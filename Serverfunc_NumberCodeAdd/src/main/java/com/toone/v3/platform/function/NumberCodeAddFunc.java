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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 编码字符串累加<br>
 * <br>
 * 代码示例:NumberCodeAdd("0010203",-3)返回值为"0010200"。<br>
 * 参数1--能转换成数值的编码字符串(字符串类型, 仅支持正整数)；<br>
 * 参数2--累加的数值(整型)；<br>
 * 返回值为字符串类型（返回值长度有参数1决定，补前导零，相加值小于零，结果将为零）。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:43
 */
public class NumberCodeAddFunc implements IFunction {

    // 函数编码
    private final static String funcCode = NumberCodeAddRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(NumberCodeAddFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 2);
            param1 = context.getInput(0);
            param2 = context.getInput(1);

            if (!(param1 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须为字符串类型，参数1：" + param1);
            }

            String numberCode = (String) param1;

            if (!(param2 instanceof Integer || param2 instanceof Double || param2 instanceof Long)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须为数字类型，参数2：" + param2);
            }
            // 解析参数为整数类型的数值
            BigDecimal num = null;
            try {
                num = new BigDecimal(param2.toString());
            } catch (Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须为数字类型，参数2：" + param2);
            }

            String resultNumCode = codeAdd(numberCode, num);

            outputVo.put(resultNumCode);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2, e);
        }
        return outputVo;
    }

    /**
     * 编码字符串累加或累减
     *
     * @param {String} numberCode
     * @param {int}    num
     */

    private String codeAdd(String numberCode, BigDecimal num) {

        int numberCodeSize = 0;

        BigDecimal sourceNum = null;
        BigDecimal addNum = num;

        if (numberCode == null || numberCode == "") {
            sourceNum = new BigDecimal(0);

        } else {

            boolean isNumber = isNumeric(numberCode);

            if (isNumber == false) {
                throw new RuntimeException("转换值必须为正整数");
            }

            sourceNum = new BigDecimal(numberCode);
            numberCodeSize = numberCode.length();

        }

        BigDecimal resultBigDecimal = sourceNum.add(addNum);
        if (resultBigDecimal.compareTo(BigDecimal.ZERO) == -1) {
            resultBigDecimal = new BigDecimal(0);
        }

        String resultNumCode = resultBigDecimal.toString();

        // 计算补零个数
        int fillZeroCount = numberCodeSize - resultNumCode.length();

        // 进行补零操作
        if (fillZeroCount > 0) {
            for (int index = 0; index < fillZeroCount; index++) {
                resultNumCode = "0" + resultNumCode;
            }
        }

        return resultNumCode;
    }

    ;

    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    ;
}
