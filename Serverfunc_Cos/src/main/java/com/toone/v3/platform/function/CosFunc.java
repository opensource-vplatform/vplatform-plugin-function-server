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
 * 余弦,返回值为指定角度的余弦值。<br>
 * <br>
 * 代码示例:Cos(1)返回值为0.9998476952。<br>
 * 参数1--指定的角度(小数类型)；<br>
 * 返回值类型：数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 13:40
 */
public class CosFunc implements IFunction {

    private final static String funcCode = CosRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(CosFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;

        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);

            param = context.getInput(0);

            double param1;
            try {
                param1 = Double.parseDouble(param.toString());
            } catch(Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须是数字类型，参数1：" + param);
            }

            BigDecimal result = BigDecimal.valueOf(Math.cos(service.angleToRadian(param1)));
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
            outputVo.setMessage("函数【" + funcCode + "】计算有误，请检查入参值：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，请检查入参值：" + param, e);
        }

        return outputVo;
    }
}
