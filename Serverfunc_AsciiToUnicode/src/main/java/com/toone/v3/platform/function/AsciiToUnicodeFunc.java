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
 * 将指定Ascii字符串转换成,返回Unicode字符串。<br>
 * <br>
 * 代码示例:AsciiToUnicode("同望Toone")返回值为"&#21516;&#26395;&#84;&#111;&#111;&#110;&#101;"。<br>
 * 参数1--字符串(字符串类型)<br>
 * 返回值为字符串类型。<br>
 *
 * @author xugang
 * @date 2021-05-24
 *
 */
public class AsciiToUnicodeFunc implements IFunction {

    private static final String funcCode = AsciiToUnicodeRegister.Plugin_Code;
    private static final Logger log = LoggerFactory.getLogger(AsciiToUnicodeFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {

        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 1);

            param = context.getInput(0);

            service.checkParamNull(funcCode, param, 1);

            String result;
            if(param instanceof String) {
                result = toUnicode((String )param);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数类型必须为字符串");
            }
            outputVo.setSuccess(true);
            outputVo.put(result);
        } catch(ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param + "，" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，请检查入参值：" + param, e);
        }

        return outputVo;
    }

    private String toUnicode(final String dataStr) {

        String retVal = "";
        for (int i = 0; i < dataStr.length(); i++) {
            char c = dataStr.charAt(i);
            String tmp = String.valueOf((int) c);

            retVal += "&#" + tmp + ";";
        }
        return retVal;
    }

}
