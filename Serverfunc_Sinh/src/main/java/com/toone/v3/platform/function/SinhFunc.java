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
 * 双曲正弦<br>
 * <br>
 * 代码示例:Sinh(1)  返回值为1.1752011936438014<br>
 * 参数数量:1 <br>
 * 参数1--以弧度为单位的角(数字类型) <br>
 * 返回值为小数<br>
 *
 * @Author xugang
 * @Date 2021/5/31 20:13
 */
public class SinhFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.Sinh.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(SinhFunc.class);

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

            double angle = Double.parseDouble(param.toString());
            BigDecimal result = BigDecimal.valueOf(Math.sinh(angle));

            outputVo.setSuccess(true);
            outputVo.put(result.toPlainString());
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
