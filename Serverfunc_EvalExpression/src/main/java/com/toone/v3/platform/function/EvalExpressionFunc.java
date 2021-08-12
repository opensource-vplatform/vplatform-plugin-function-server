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
 * 校验执行表达式的函数<br>
 * <br>
 * 代码示例:EvalExpression("@@userName") 返回系统变量userName的值。 <br>
 * 参数1--为字符串表达式。<br>
 * 返回值为不定类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:19
 */
public class EvalExpressionFunc implements IFunction {

    // 函数编码
    private final static String funcCode = EvalExpressionRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(EvalExpressionFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
            service.checkParamBlank(funcCode, param);

            Object result = VDS.getIntance().getFormulaEngine().eval(param.toString());
            outputVo.put(result);
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
}
