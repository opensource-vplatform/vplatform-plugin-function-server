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
 * 替代字符串内的内容<br>
 * <br>
 * 代码示例:ReplaceFunc("abcabbaab","ab","123")返回值为123c123ba123。<br>
 * 参数1--原字符串(字符串类型)；<br>
 * 参数2--模式串(字符串类型)；<br>
 * 参数3--替代串(字符串类型)；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:46
 */
public class ReplaceFunc implements IFunction {

    // 函数编码
    private final static String funcCode = ReplaceRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(ReplaceFunc.class);

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
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须是字符串类型，参数1：" + param1);
            }
            if (!(param2 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须是字符串类型，参数2：" + param2);
            }
            if (!(param3 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第3个参数必须是字符串类型，参数3：" + param3);
            }

            String str = (String) param1;
            String str2 = (String) param2;
            String str3 = (String) param3;

            String s = str.replace(str2, str3);

            outputVo.put(s);
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
