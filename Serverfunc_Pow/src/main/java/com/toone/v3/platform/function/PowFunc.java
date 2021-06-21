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
 * 指定数字的指定次幂<br>
 * <br>
 * 代码示例:Pow(3,2) 返回值为9<br>
 * 参数数量:2<br>
 * 参数1--要乘幂的数(小数类型)<br>
 * 参数2--幂(小数类型)<br>
 * 返回值为小数<br>
 *
 * @Author xugang
 * @Date 2021/6/1 16:05
 */
public class PowFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.Pow.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(PowFunc.class);

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


            double result = 0 ;
            double arg1 = Double.parseDouble(param1.toString());
            double arg2 = Double.parseDouble(param2.toString());
            result = Math.pow(arg1, arg2);
            if(Double.isNaN(result)){
                throw new ServerFuncException("指定数字的幂函数无法计算，请尽量修改幂为正整数类型");
            }
            if(Double.isInfinite(result)){
                throw new ServerFuncException("指定数字的幂函数运算数据超出计算机所表示的范围，无法计算");
            }

            outputVo.setSuccess(true);
            outputVo.put(new BigDecimal(result).toPlainString());
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
