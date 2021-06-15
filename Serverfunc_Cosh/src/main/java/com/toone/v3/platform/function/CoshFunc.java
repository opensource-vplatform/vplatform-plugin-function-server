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
 * @Author xugang
 * @Date 2021/5/31 13:40
 */
public class CoshFunc implements IFunction {

    private final static Logger log = LoggerFactory.getLogger(CoshFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;

        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(ServerFuncCommonUtils.Cosh.Function_Code(), context, 1);

            param = context.getInput(0);

            double param1;
            try {
                param1 = Double.parseDouble(param.toString());
            } catch(Exception e) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.Cosh.Function_Code() + "】的第1个参数必须是数字类型，参数1：" + param);
            }

            BigDecimal result = BigDecimal.valueOf(Math.cosh(param1));

            outputVo.setSuccess(true);
            outputVo.put(result.toPlainString());
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + ServerFuncCommonUtils.Cosh.Function_Code() + "】计算有误，请检查入参值：" + param + ", " + e.getMessage());
            log.error("函数【" + ServerFuncCommonUtils.Cosh.Function_Code() + "】计算失败，请检查入参值：" + param, e);
        }

        return outputVo;
    }
}
