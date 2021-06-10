package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 返回e的指定次幂<br>
 * <br>
 * 代码示例:Exp(2)返回值为7.38905609893065。<br>
 * 参数1--幂（数字类型）；<br>
 * 返回值类型：数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:19
 */
public class ExpFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.Exp.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(ExpFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
            service.checkParamNull(funcCode, param);
            service.checkParamNumeric(funcCode, param);

            double result = Double.parseDouble(param.toString());
            result = Math.exp(result);
            if (Double.isInfinite(result)) {
                throw new ServerFuncException("函数【" + funcCode + "】指定数字【" + param + "】的幂函数运算数据超出计算机所表示的范围，无法计算");
            }

            outputVo.setSuccess(true);
            outputVo.put(new BigDecimal(result));
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
