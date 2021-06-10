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
 * 求指定数字以10为底的对数<br>
 * <br>
 * 代码示例:Log10(100) 返回值为2<br>
 * 参数数量:1<br>
 * 参数1--指定数字(数字类型)<br>
 * 返回值为小数<br>
 *
 * @Author xugang
 * @Date 2021/6/1 16:04
 */
public class Log10Func implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.Log10.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(Log10Func.class);

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

            double p = Double.parseDouble(param.toString());
            if(p <= 0) {
                throw new ServerFuncException("函数【" + funcCode + "】的参数必须大于0，当前入参1：" + p);
            }
            BigDecimal result = BigDecimal.valueOf(Math.log10(p));

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
