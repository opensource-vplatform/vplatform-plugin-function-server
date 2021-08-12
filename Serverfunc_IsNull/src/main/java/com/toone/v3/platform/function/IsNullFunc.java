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
 * 检查输入的参数是否为空值,不为空返回原值，为空返回默认值。<br>
 * <br>
 * 代码示例:IsNull(arg,defaultVal)第一个参数不为空直接返回第一个参数,为空时返回默认值。<br>
 * 参数1--被检查的值；参数2--为空时的缺省值；<br>
 * 返回值为返回参数的数据类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/16 10:26
 */
public class IsNullFunc implements IFunction {

    // 函数编码
    private final static String funcCode = IsNullRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(IsNullFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object checkVal = null;
        Object defaultVal = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 2);
            checkVal = context.getInput(0);
            defaultVal = context.getInput(1);

            if (checkVal instanceof String) {
                if (((String) checkVal).equals("")) {
                    outputVo.put(defaultVal);
                } else {
                    outputVo.put(checkVal);
                }
            } else {
                if (null == checkVal) {
                    outputVo.put(defaultVal);
                } else {
                    outputVo.put(checkVal);
                }
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + checkVal + "，参数2：" + defaultVal + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + checkVal + "，参数2：" + defaultVal, e);
        }
        return outputVo;
    }
}
