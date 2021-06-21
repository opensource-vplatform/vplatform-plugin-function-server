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
 * 向上取整函数<br>
 * <br>
 * 代码示例:Ceiling(2.555)返回值为3<br>
 * 参数数量:1<br>
 * 参数1--指定的数(小数类型)<br>
 * 返回值为整数<br>
 *
 * @Author xugang
 * @Date 2021/5/31 10:23
 */
public class CeilingFunc implements IFunction {

    private final static Logger log = LoggerFactory.getLogger(CeilingFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(ServerFuncCommonUtils.Ceiling.Function_Code(), context, 1);
            param = context.getInput(0);
            service.checkParamNull(ServerFuncCommonUtils.Ceiling.Function_Code(), param);

            double d;
            try {
                d = Double.parseDouble(param.toString());
            } catch (Exception e) {
                throw new ServerFuncException("函数【】的第1个参数必须是数字类型，参数1：" + param);
            }

            BigDecimal result = new BigDecimal(Math.ceil(d));
            outputVo.setSuccess(true);
            outputVo.put(result.toPlainString());
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + ServerFuncCommonUtils.Ceiling.Function_Code() + "】计算有误，请检查入参数1：" + param + "，" + e.getMessage());
            log.error("函数【" + ServerFuncCommonUtils.Ceiling.Function_Code() + "】计算失败，参数1：" + param, e);
        }
        return outputVo;
    }
}
