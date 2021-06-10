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
 * 检查指定的字符串中是否包含另一指定的字符串，包含则返回True。<br>
 * <br>
 * 代码示例:Contains("V平台","平")，返回值为True。<br>
 * 参数1--原字符串(字符串类型)；<br>
 * 参数2--指定的字符串(字符串类型)。<br>
 * 返回值为布尔值。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 13:37
 */
public class ContainsFunc implements IFunction {

    private final String funcCode = ServerFuncCommonUtils.Contains.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(ContainsFunc.class);

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

            String str1;
            if (param1 == null) {
                str1 = "";
            } else if (!(param1 instanceof String)) {
                throw new ServerFuncException("函数【】的第1个参数必须是字符串类型，参数1：" + param1);
            } else {
                str1 = (String) param1;
            }
            String str2;
            if (param2 == null) {
                str2 = "";
            } else if (!(param2 instanceof String)) {
                throw new ServerFuncException("函数【】的第2个参数必须是字符串类型，参数1：" + param2);
            } else {
                str2 = (String) param2;
            }

            outputVo.put(str1.indexOf(str2));
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
