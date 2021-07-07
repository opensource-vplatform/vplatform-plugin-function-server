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
import java.text.NumberFormat;

/**
 * 格式转换，将指定值转换成指定格式返回。<br>
 * <br>
 * 代码示例:ConvertFunc(3,2) 返回值为"3"。<br>
 * 参数1--传入的数据(各种类型)；<br>
 * 参数2--转换的类型，1-整数,2-字符串,3-布尔值,4-小数；<br>
 * 返回值为不确定的类型，根据参数2的格式而定。<br>
 * 注：整数的支持范围是：[-2147483648，2147483647]，小数位数最大支持6位。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 13:37
 */
public class ConvertFunc implements IFunction {

    private final static String funcCode = ConvertRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(ConvertFunc.class);

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

            service.checkParamNull(funcCode, param1, param2);

            // 1-整数,2-字符串,3-布尔值,4-小数
            int type;
            NumberFormat nf = NumberFormat.getInstance();
            try {
                type = Integer.parseInt(nf.format(param2).replace(",", ""));
            } catch (Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须是整数，参数2：" + param2);
            }

            String str = param1.toString().trim();
            switch (type) {
                case 1: // 转换为整数,传入数据必须是数字
                    if (str.equals("")) {
                        outputVo.put(0);
                    } else if (isNumber(param1.toString())) {
                        BigDecimal temp_bd = new BigDecimal(param1.toString());
                        if (temp_bd.longValue() > Integer.MAX_VALUE || temp_bd.longValue() < Integer.MIN_VALUE) {
                            throw new ServerFuncException("函数【" + funcCode + "】的第1个参数的数值大小超过支持范围，数值支持的范围是：[" + Integer.MIN_VALUE + "，" + Integer.MAX_VALUE + "]，参数1：" + param1);
                        }
                        outputVo.put(temp_bd.intValue());
                    } else {
                        throw new ServerFuncException("函数【" + funcCode + "】的参数不正确，参数1：" + param1 + "，参数2：" + param2 + "，转换为整数，参数1必须是数字");
                    }
                    break;
                case 2: // 转换为字符串,传入数据不为空即可
                    outputVo.put(param1.toString());
                    break;
                case 3: // 转换为布尔值
                    String content = str.toLowerCase();
                    if (content.equals("false") || content.equals("") || content.equals("null")
                            || content.equals("undefined") || content.equals("nan")
                            || content.equals("0")) {
                        outputVo.put(false);
                    } else {
                        outputVo.put(true);
                    }
                    break;
                case 4: // 转换小数
                    if ("".equals(str)) {
                        outputVo.put(0.0d);
                    } else if (isNumber(str)) {
                        BigDecimal temp_bd = new BigDecimal(str);
                        if (str.contains(".")) {
                            String[] tmp_string_array = str.split("\\.");
                            long temp_value = new BigDecimal(tmp_string_array[0]).longValue();
                            if (temp_value > Integer.MAX_VALUE || temp_value < Integer.MIN_VALUE) {
                                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数的数值大小超过支持范围，数值支持的范围是：[" + Integer.MIN_VALUE + "，" + Integer.MAX_VALUE + "]，参数1：" + param1);
                            }
                            if (tmp_string_array[1].length() > 6) {
                                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数的小数位数最大支持6位，参数1：" + param1);
                            }
                        } else {
                            if (temp_bd.longValue() > Integer.MAX_VALUE || temp_bd.longValue() < Integer.MIN_VALUE) {
                                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数的数值大小超过支持范围，数值支持的范围是：[" + Integer.MIN_VALUE + "，" + Integer.MAX_VALUE + "]，参数1：" + param1);
                            }
                        }
                        outputVo.put(temp_bd.doubleValue());
                    } else {
                        throw new ServerFuncException("函数【" + funcCode + "】的参数不正确，参数1：" + param1 + "，参数2：" + param2 + "，转换为整数，参数1必须是数字");
                    }
                    break;
                default:
                    throw new ServerFuncException("函数【" + funcCode + "】的第2个参数只能是：1-整数,2-字符串,3-布尔值,4-小数，参数2：" + param2);
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2, e);
        }
        return outputVo;
    }

    private boolean isNumber(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
