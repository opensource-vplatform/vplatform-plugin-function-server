package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;

/**
 * 倒序查找指定<br>
 * <br>
 * 代码示例:LastIndexOf("abcabbcadabc","bc",9)，返回值为5。<br>
 * 参数1--原字符串(字符串类型)；<br>
 * 参数2--查找字符串(字符串类型)；<br>
 * 参数3--检查截止位置(整数类型),可忽略，忽略则检查到末尾；<br>
 * 返回值为整数类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/16 10:27
 */
public class LastIndexOfFunc implements IFunction {

    // 函数编码
    private final static String funcCode = LastIndexOfRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(LastIndexOfFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            int size = context.getInputSize();
            if (size == 2) {
                param1 = context.getInput(0);
                param2 = context.getInput(1);
            } else if (size == 3) {
                param1 = context.getInput(0);
                param2 = context.getInput(1);
                param3 = context.getInput(2);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】需要2个或者3个参数，当前参数个数：" + size);
            }

            if (!(param1 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须是字符串类型，参数1：" + param1);
            }

            if (param2 == null) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须是字符串类型，参数1：" + param1);
            }

            if (!(param2 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须是字符串类型，参数2：" + param2);
            }

            String str = (String) param1;

            String subStr = (String) param2;
            if (size == 3 && param3 != null) {
                if (!(param3 instanceof Integer || param3 instanceof Double)) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须是整数类型，参数3：" + param3);
                }
                // 解析参数为整数类型的数值
                int index;
                NumberFormat nf = NumberFormat.getInstance();
                try {
                    index = Integer.parseInt(nf.format(param3).replace(",", ""));
                } catch (Exception e) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须是整数类型，参数3：" + param3);
                }
                int i = str.lastIndexOf(subStr, index);
                outputVo.put(i);
            } else {
                int i = str.lastIndexOf(subStr);
                outputVo.put(i);
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
