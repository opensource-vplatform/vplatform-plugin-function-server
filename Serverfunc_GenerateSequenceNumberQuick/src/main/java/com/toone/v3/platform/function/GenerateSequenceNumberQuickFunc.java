package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.business.sequence.api.ISerialNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 取流水号<br>
 * <br>
 * GenerateSequenceNumberQuick("123456") 返回值是："1"。<br>
 * 参数1：流水号种子，字符串类型。最长128位字符串，建议64位或者更短<br>
 * 返回值：流水号，整数类型<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:42
 */
public class GenerateSequenceNumberQuickFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.GenerateSequenceNumberQuick.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(GenerateSequenceNumberQuickFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);

            String key = (String) param;
            ISerialNumber serialNumber = VDS.getIntance().getSerialNumber(key);
            int number = serialNumber.generateSequenceNumberQuick();

            outputVo.put(number);
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
