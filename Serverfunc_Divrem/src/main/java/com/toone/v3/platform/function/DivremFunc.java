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
 * 整数商<br>
 * <br>
 * 代码示例:Divrem(54,8)返回值为6<br>
 * 参数数量:2<br>
 * 参数1--被除数(小数类型),参数2--除数(小数类型)<br>
 * 返回值为整数<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:08
 */
public class DivremFunc implements IFunction {

    // 函数编码
    private final static String funcCode = DivremRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(DivremFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 2);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            service.checkParamNull(funcCode, param1, param2);
            service.checkParamNumeric(funcCode, param1, param2);

            if (param2.toString().trim().equals("0")) {
                throw new ServerFuncException("函数【" + funcCode + "】的第二个参数不能为0");
            }

            BigDecimal result;
            BigDecimal bd1 = new BigDecimal(param1.toString());
            BigDecimal bd2 = new BigDecimal(param2.toString());
            result = bd1.divide(bd2, 6, BigDecimal.ROUND_HALF_UP);
            result = new BigDecimal("" + result).setScale(0, BigDecimal.ROUND_DOWN);

            outputVo.setSuccess(true);
            outputVo.put(result.intValue());
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2, e);
        }
        return outputVo;
    }
}
