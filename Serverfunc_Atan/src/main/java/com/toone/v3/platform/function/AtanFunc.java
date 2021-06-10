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
 * 反正切<br>
 * <br>
 * 代码示例:Atan(1) 返回值为：45<br>
 * 参数数量:1 <br>
 * 参数1--指定的角度(数字类型) <br>
 * 返回值为小数，为指定值的角度<br>
 *
 * @Author xugang
 * @Date 2021/5/30 19:02
 */
public class AtanFunc implements IFunction {

    private final static Logger log = LoggerFactory.getLogger(AtanFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(ServerFuncCommonUtils.Atan.Function_Code(), context, 1);

            param = context.getInput(0);

            if (!(param instanceof Number)) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.Atan.Function_Code() + "】的第1个参数必须是数字类型，参数1：" + param);
            }

            double param1 = Double.parseDouble(param.toString().trim());
            BigDecimal result = new BigDecimal(service.radianToAngle(Math.atan(param1)));
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
            outputVo.setMessage("函数【" + ServerFuncCommonUtils.Atan.Function_Code() + "】计算有误，参数1：" + param + "，" + e.getMessage());
            log.error("函数【" + ServerFuncCommonUtils.Atan.Function_Code() + "】计算失败，请检查入参值：" + param, e);
        }
        return outputVo;
    }
}
