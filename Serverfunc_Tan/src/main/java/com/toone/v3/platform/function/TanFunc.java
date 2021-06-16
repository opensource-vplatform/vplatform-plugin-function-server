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
 * 正切<br>
 * <br>
 * 代码示例:Tan(1)  返回值为  0.017455064928217585 <br>
 * 参数数量:1 <br>
 * 参数1--指定的角度(小数类型) <br>
 * 返回值为小数<br>
 *
 * @Author xugang
 * @Date 2021/5/31 20:13
 */
public class TanFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.Tan.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(TanFunc.class);

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

            double pai = Math.PI;
            double judgeNum = (param1 - pai / 2) % pai;
            if (judgeNum == 0)
                throw new ServerFuncException("函数【" + funcCode + "】参数不符合要求");

            BigDecimal result = new BigDecimal(Math.tan(service.angleToRadian(param1)));
            result = result.setScale(10, BigDecimal.ROUND_HALF_UP);

            outputVo.setSuccess(true);
            outputVo.put(result.doubleValue());
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
