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
 * 正弦<br>
 * <br>
 * 代码示例:Sin(1)  返回值为  0.0174524064<br>
 * 参数数量:1 <br>
 * 参数1--指定的角度(小数类型) <br>
 * 返回值为小数<br>
 *
 * @Author xugang
 * @Date 2021/5/31 20:12
 */
public class SinFunc implements IFunction {

    // 函数编码
    private final static String funcCode = SinRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(SinFunc.class);

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

            double param1 = Double.parseDouble(param.toString());
            BigDecimal result = BigDecimal.valueOf(Math.sin(service.angleToRadian(param1)));
            result = result.setScale(10, BigDecimal.ROUND_HALF_UP);

            if (new BigDecimal(result.intValue()).compareTo(result) == 0) {
                outputVo.put(result.intValue());
            } else {
                outputVo.put(result.doubleValue());
            }
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，请检查参数1：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，请检查参数1：" + param, e);
        }

        return outputVo;
    }
}
