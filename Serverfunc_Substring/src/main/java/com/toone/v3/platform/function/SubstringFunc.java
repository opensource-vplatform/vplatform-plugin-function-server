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
 * 子链提取<br>
 * <br>
 * 代码示例:Substring("abcdefg",2,3)，返回值为"cde"。<br>
 * 参数1--原字符串(字符串类型)；<br>
 * 参数2--开始提取子串的索引位置(整型，不能小于0，第1个字符索引为0)；<br>
 * 参数3--提取子串的长度(整型，大于0)；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:56
 */
public class SubstringFunc implements IFunction {

    // 函数编码
    private final static String funcCode = SubstringRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(SubstringFunc.class);

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

            if (!(param1 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须为字符串类型，参数1：" + param1);
            }
            String str = (String) param1;

            if (str.equals("")) {
                outputVo.put("");
            } else {
                if (!(param2 instanceof Integer || param2 instanceof Double)) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须为整数类型，参数2：" + param2);
                }
                // 解析参数为整数类型的数值
                int beginIndex = 0;
                NumberFormat nf = NumberFormat.getInstance();
                try {
                    beginIndex = Integer.parseInt(nf.format(param2).replace(",", ""));
                } catch (Exception e) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须为整数类型，参数2：" + param2);
                }

                if (!(param3 instanceof Integer || param3 instanceof Double)) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第3个参数必须为整数类型，参数3：" + param3);
                }
                int lengSize = 0;
                try {
                    lengSize = Integer.parseInt(nf.format(param3).replace(",", ""));
                } catch (Exception e) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第3个参数必须为整数类型，参数3：" + param3);
                }

                String s = "";
                if (beginIndex > str.length()) {
                    s = str;
                } else if (lengSize + beginIndex > str.length()) {
                    s = str.substring(beginIndex, str.length());
                } else {
                    s = str.substring(beginIndex, beginIndex + lengSize);
                }

                outputVo.put(s);
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
