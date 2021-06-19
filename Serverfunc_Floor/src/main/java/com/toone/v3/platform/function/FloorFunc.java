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
 * 向下取整函数<br>
 * <br>
 * 代码示例:Floor(2.55456)返回值为2<br>
 * 参数数量:1<br>
 * 参数1--小数的值(小数类型)<br>
 * 返回值为整数<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:29
 */
public class FloorFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.Floor.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(FloorFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
            service.checkParamNull(funcCode, param);

            double param1;
            try {
                param1 = Double.parseDouble(param.toString());
            } catch(Exception e) {
                throw new ServerFuncException("函数【】的第1个参数必须是数字类型，当前值：" + param);
            }

            BigDecimal result = new BigDecimal(Math.floor(param1));
            outputVo.setSuccess(true);
            outputVo.put(result);
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
