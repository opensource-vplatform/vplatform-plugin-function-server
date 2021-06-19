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
 * 求浮点数的整数部分<br>
 * <br>
 * 代码示例：Truncate(1.99)返回值为1。<br>
 * 参数1--指定的数（数字类型）；<br>
 * 返回值类型：整数类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:58
 */
public class TruncateFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.Truncate.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(TruncateFunc.class);

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

            BigDecimal result =new BigDecimal(param.toString()).setScale(0, BigDecimal.ROUND_DOWN);

            outputVo.put(result.toPlainString());
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
