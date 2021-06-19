package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unicode转Ascii<br>
 * <br>
 * 代码示例:UnicodeToAscii("&#21516;&#26395;&#84;&#111;&#111;&#110;&#101;")返回值为"同望Toone"。<br>
 * 参数1--字符串(字符串类型)<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:58
 */
public class UnicodeToAsciiFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.UnicodeToAscii.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(UnicodeToAsciiFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);

            if (!(param instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个必须为字符串类型，参数1：" + param);
            }

            String str = (String) param;

            if (str.equals("")) {
                outputVo.put("");
            } else {
                String retVal = this.toAscii(str);
                outputVo.put(retVal);
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }

    private String toAscii(final String text) {
        StringBuilder retVal = new StringBuilder();
        Pattern pt = Pattern.compile("&#(\\d+);");
        Matcher match = pt.matcher(text);
        while (match.find()) {
            String tmp = match.group();
            tmp = tmp.replace("&#", "").replace(";", "");
            System.out.println(tmp);
            int intValue = Integer.parseInt(tmp);
            retVal.append((char) intValue);
        }

        return retVal.toString();
    }
}
