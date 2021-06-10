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
 * 双曲正切<br>
 * <br>
 * 代码示例:Tanh(1)返回值为0.7615941559557647 <br>
 * 参数数量:1 <br>
 * 参数1--指定数字(数字类型) <br>
 * 返回值为小数<br>
 *
 * @Author xugang
 * @Date 2021/5/31 20:13
 */
public class TanhFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.Tanh.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(TanhFunc.class);

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
            //取临界值
            if (param1 > 19.061547465398) {
                outputVo.put(1);
            } else if (param1 < -19.061547465398) {
                outputVo.put(-1);
            } else {
                BigDecimal result = BigDecimal.valueOf(Math.tanh(param1));
                outputVo.put(result.toPlainString());
            }

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
