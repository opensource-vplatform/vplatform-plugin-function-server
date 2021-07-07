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
 * 是否以指定串开头<br>
 * <br>
 * 代码示例:StartsWith("V平台","V平")返回值为True。<br>
 * 参数1--被检查字符串(字符串类型)；<br>
 * 参数2--指定的字符串(字符串类型)；<br>
 * 返回值为布尔值。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:56
 */
public class StartsWithFunc implements IFunction {

    // 函数编码
    private final static String funcCode = StartsWithRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(StartsWithFunc.class);

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

            if (!(param1 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须为字符串类型，参数1：" + param1);
            }
            if (!(param2 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须为字符串类型，参数2：" + param2);
            }

            String str = (String) param1;
            String str1 = (String) param2;
            boolean res = str.startsWith(str1);

            outputVo.put(res);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2, e);
        }
        return outputVo;
    }
}
