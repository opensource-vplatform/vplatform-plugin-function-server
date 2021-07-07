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
 * 获取字符串的长度<br>
 * <br>
 * 代码示例:GetLength("asccc")返回值为5。<br>
 * 参数1--指定的字符串(字符串类型)；<br>
 * 返回值为整数。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:55
 */
public class GetLengthFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GetLengthRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GetLengthFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);

            if(param != null) {
                outputVo.put(param.toString().length());
            } else {
                outputVo.put(0);
            }
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
