package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 三元运算函数<br>
 * <br>
 * 代码示例:V3If(1==1,20,30) 返回值为 20。<br>
 * 参数1--条件表达式（布尔类型）；<br>
 * 参数2--条件表达式为true时返回值（各种类型）；<br>
 * 参数3--条件表达式为false时返回值（各种类型）；<br>
 * 返回值类型不定，由参数2，或者参数3决定。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:59
 */
public class V3IfFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.V3If.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(V3IfFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 3);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            param3 = context.getInput(2);
            if (!(param1 instanceof Boolean)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须为boolean类型，param1：" + param1);
            }

            boolean evalFlag = (Boolean) param1;

            if (evalFlag) {
                outputVo.put(param1);
            } else {
                outputVo.put(param2);
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3, e);
        }
        return outputVo;
    }
}
