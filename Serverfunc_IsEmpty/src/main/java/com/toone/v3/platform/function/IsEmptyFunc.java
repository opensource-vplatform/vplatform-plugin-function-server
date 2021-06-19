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
 * 检查是否为空值<br>
 * <br>
 * 代码示例:IsEmpty("")返回值为True。<br>
 * 参数1--被检查的字符串(可以是实体字段,控件值,变量等但是必须数据类型要为字符串类型)<br>
 * 返回值为布尔值<br>
 *
 * @Author xugang
 * @Date 2021/6/16 10:26
 */
public class IsEmptyFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.IsEmpty.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(IsEmptyFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);

            if (null == param) {
                outputVo.put(true);
            } else {
                if (!(param instanceof String)) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须是字符串类型，参数1：" + param);
                } else {
                    String str = (String) param;
                    if (str == null || "".equals(str)) {
                        outputVo.put(true);
                    } else {
                        outputVo.put(false);
                    }
                }
            }

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
