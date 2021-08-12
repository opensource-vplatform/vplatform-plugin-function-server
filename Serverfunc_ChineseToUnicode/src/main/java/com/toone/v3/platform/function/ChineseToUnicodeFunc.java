package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 中文转Unicode<br>
 * <br>
 * 代码示例:ChineseToUnicode("同望unicode")返回值为"\u540c\u671b\u0075\u006e\u0069\u0063\u006f\u0064\u0065"。<br>
 * 参数1--字符串(字符串类型)<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:22
 */
public class ChineseToUnicodeFunc implements IFunction {

    // 函数编码
    private final String funcCode = ChineseToUnicodeRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(ChineseToUnicodeFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
            if (!(param instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须是字符串类型，参数1：" + param);
            }

            if (param == null) {
                outputVo.put("");
            } else {
                String str = (String) param;
                if (str.equals("")) {
                    outputVo.put("");
                } else {
                    outputVo.put(toUnicode(str));
                }
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param, e);
        }
        return outputVo;
    }

    private String toUnicode(String text) {
        char[] utfBytes = text.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }
}
