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
 * 空值空字符串处理<br>
 * <br>
 * 代码示例:IsNullOrEmptyFunc("")，返回 true。<br>
 * 参数1--被检查的值（字符串类型）；<br>
 * 返回值为布尔类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/16 10:26
 */
public class IsNullOrEmptyFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.IsNullOrEmptyFunc.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(IsNullOrEmptyFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);

            boolean flag = false;
            if (param == null) {
                flag = true;
            } else if (param instanceof String) {
                if (((String) param).length() == 0) {
                    flag = true;
                }
            }

            outputVo.put(flag);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
