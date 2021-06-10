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
 * 反余弦<br>
 * 代码示例:Acos(1) 返回值为0<br>
 * <br>
 * 参数数量:1 参数1--指定的角度(数字类型)<br>
 * 返回值为小数，为指定值的角度<br>
 *
 * @author xugang
 * @date 2021-05-24
 */
public class AcosFunc implements IFunction {

    private static final Logger log = LoggerFactory.getLogger(AcosFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;

        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(ServerFuncCommonUtils.Acos.Function_Code(), context, 1);

            param = context.getInput(0);

            if(!(param instanceof Number)) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.Acos.Function_Code() + "】的第1个参数必须是数字类型，参数1：" + param);
            }

            double param1 = Double.parseDouble(param.toString());
            if (param1 > 1 || param1 < -1)
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.Acos.Function_Code() + "】的第1个参数必须必须在1到-1之间");

            BigDecimal result = BigDecimal.valueOf(service.radianToAngle(Math.acos(param1)));
            result = result.setScale(10, BigDecimal.ROUND_HALF_UP);

            outputVo.setSuccess(true);
            outputVo.put(result.toPlainString());
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + ServerFuncCommonUtils.Acos.Function_Code() + "】计算有误，参数1：" + param + "，" + e.getMessage());
            log.error("函数【" + ServerFuncCommonUtils.Acos.Function_Code() + "】计算失败，请检查入参值：" + param, e);
        }

        return outputVo;
    }
}
