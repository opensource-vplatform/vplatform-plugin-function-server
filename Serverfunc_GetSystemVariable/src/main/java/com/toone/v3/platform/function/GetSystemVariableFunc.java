package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.business.systemconstant.api.model.SystemConstantModel;
import com.yindangu.v3.business.systemvariable.api.model.SystemVariableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取系统变量、系统常量函数<br>
 * <br>
 * 代码示例:GetSystemVariable(BR_IN_PARENT.arg1) 返回值为 "EEEE"。<br>
 * 参数1--构件变量或者常量名（字符串类型）；<br>
 * 返回值类型为不定，由构件变量或者构件常量决定。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:58
 */
public class GetSystemVariableFunc implements IFunction {

    // 函数编码
    private String funcCode = ServerFuncCommonUtils.GetSystemVariable.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(GetSystemVariableFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            int size = context.getInputSize();
            if (size > 0) {
                param = context.getInput(0);
                String variableName = (String) param;

                // 2015-06-25 liangchaohui
                // 由于构件常量与构件变量是公用同一个标识符“@@”但取常量跟变量又是不同的接口
                // 通过与huangjr沟通，决定在本函数中取不到构件变量时，就取构件常量
                Object variableValue;
                SystemVariableModel systemVariable = VDS.getIntance().getSystemVariableManager().getSystemVariable(variableName);
                variableValue = systemVariable == null ? null : systemVariable.getValue();
                if (variableValue == null) {
                    SystemConstantModel systemConstant = VDS.getIntance().getSystemConstantManager().getSystemConstant(variableName);
                    if (systemConstant != null) {
                        variableValue = systemConstant.getValue();
                    }
                }

                outputVo.put(variableValue);
            } else {
                outputVo.put(null);
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参值1：" + param, e);
        }
        return outputVo;
    }
}
