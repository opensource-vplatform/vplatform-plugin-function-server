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
 * 检查字符串是否仅为空白字符组成<br>
 * <br>
 * 代码示例:IsWhiteOrSpace("     ")，返回值为True。<br>
 * 参数1--被检查的字符串(可以是实体字段,控件值,变量等但是必须数据类型要为字符串类型)；<br>
 * 返回值为布尔值。<br>
 *
 * @Author xugang
 * @Date 2021/6/16 10:27
 */
public class IsWhiteOrSpaceFunc implements IFunction {

    // 函数编码
    private final static String funcCode = IsWhiteOrSpaceRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(IsWhiteOrSpaceFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
            if (!(param instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须为字符串类型，参数1：" + param);
            }

            String str = (String) param;
            str = lTrim(rTrim(str));
            boolean flag = str.length() == 0;

            outputVo.put(flag);
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

    private String lTrim(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    private String rTrim(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
