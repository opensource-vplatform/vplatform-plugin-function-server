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
 * 最大值函数<br>
 * <br>
 * 代码示例:Max(3,5)返回值为5<br>
 * 参数数量:2<br>
 * 参数1--比较值1(小数类型),参数2--比较值2(小数类型)<br>
 * 返回值为小数<br>
 *
 * @Author xugang
 * @Date 2021/6/1 16:04
 */
public class MaxFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.Max.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(MaxFunc.class);

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

            BigDecimal bd1 = new BigDecimal(param1.toString());
            BigDecimal bd2 = new BigDecimal(param2.toString());
            if( bd1.compareTo(bd2) > 0){
                outputVo.put(param1);
            }else{
                outputVo.put(param2);
            }
            outputVo.setSuccess(true);
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
