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
 * 字符串替换<br>
 * <br>
 * 代码示例: ReplaceByIndex("abcdefg","12345",0,3)，返回:"12345defg"。<br>
 * 参数1:原始字符串（必填）；<br>
 * 参数2:替换字符串（必填）；<br>
 * 参数3:替换开始下标（包含,从0开始，必填不能忽略）；<br>
 * 参数4:替换结束下标（不包含,从0开始,可以忽略，忽略时表示替换到结尾）；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:46
 */
public class ReplaceByIndexFunc implements IFunction {

    // 函数编码
    private final static String funcCode = ReplaceByIndexRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(ReplaceByIndexFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        Object param4 = null;
        try {
            int size = context.getInputSize();
            if (size != 4 && size != 3) {
                throw new ServerFuncException("函数【" + funcCode + "】需要3个或者4个参数，当前参数个数：" + size);
            }
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            param3 = context.getInput(2);
            param4 = size == 4 ? context.getInput(3) : null;

            if (!(param1 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须是字符串类型，参数1：" + param1);
            }

            if (!(param2 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须是字符串类型，参数2：" + param2);
            }

            String origStr = (String) param1;
            String replaceStr = (String) param2;

            if (origStr == null || origStr.equals("")) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数不能为空，参数1：" + param1);
            }
            if (replaceStr == null || replaceStr.equals("")) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数不能为空，参数2：" + param2);
            }

            if (!(param3 instanceof Integer || param3 instanceof Double)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第3个必须是整数类型，参数3：" + param3);
            }
            // 解析参数为整数类型的数值
            int beginIndex = 0;
            NumberFormat nf = NumberFormat.getInstance();
            try {
                beginIndex = Integer.parseInt(nf.format(param3).replace(",", ""));
            } catch (Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】的第3个必须是整数类型，参数3：" + param3);
            }

            if (size == 4 && !(param4 instanceof Integer || param4 instanceof Double)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第4个必须是整数类型，参数4：" + param4);
            }
            // 解析参数为整数类型的数值
            int endIndex = 0;
            if (size == 4) {
                try {
                    endIndex = Integer.parseInt(nf.format(param4).replace(",", ""));
                } catch (Exception e) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第4个必须是整数类型，参数4：" + param4);
                }
            } else {
                endIndex = origStr.length();
            }

            if (beginIndex < 0) {
                beginIndex = 0;
            }
            if (endIndex < 0 || endIndex > origStr.length()) {
                endIndex = origStr.length();
            }
            String retStr = origStr;
            if (beginIndex < endIndex && beginIndex >= 0
                    && endIndex <= origStr.length()) {
                String s1 = origStr.substring(0, beginIndex);
                String s2 = origStr.substring(endIndex, origStr.length());

                retStr = s1 + replaceStr + s2;
            }

            outputVo.put(retStr);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + "，参数4：" + param4 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + "，参数4：" + param4, e);
        }
        return outputVo;
    }
}
