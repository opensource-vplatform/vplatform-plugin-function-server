package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;

/**
 * 右侧填充<br>
 * <br>
 * 代码示例:PadRight("abc",5,"1")，返回值为"abc11"。<br>
 * 参数1--原字符串(字符串类型)；<br>
 * 参数2--指定的长度(整型)；<br>
 * 参数3--填充字符串(字符串类型，长度只能为1)；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:44
 */
public class PadRightFunc implements IFunction {

    // 函数编码
    private final static String funcCode = PadRightRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(PadRightFunc.class);

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

            String str1 = param1 == null ? "" : (String) param1;

            if (!(param2 instanceof Integer || param2 instanceof Double)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须为整数类型类型，参数2：" + param2);
            }
            // 解析参数为整数类型的数值
            int num = 0;
            NumberFormat nf = NumberFormat.getInstance();
            try {
                num = Integer.parseInt(nf.format(param2).replace(",", ""));
            } catch (Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须为整数类型，参数2：" + param2);
            }
            String str2 = param3 == null ? "" : (String) param3;

            if (num <= str1.length()) {
                outputVo.put(str1);
            } else {
                if (str2.length() != 1) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第3个参数只能包含一个字符，参数3：" + param3);
                }

                int j = num - str1.length();
                for (int i = 0; i < j; i++) {
                    str1 += str2;
                }
                outputVo.put(str1);
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
}
