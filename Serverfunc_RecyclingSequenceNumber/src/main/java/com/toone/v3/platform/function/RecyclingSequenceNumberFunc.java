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
 * 作废流水号<br>
 * <br>
 * 代码示例：RecyclingSequenceNumber("123456", 89)<br>
 * 参数1：流水号种子，字符串类型。最长128位字符串，建议64位或者更短<br>
 * 参数2：需要废弃的流水号，整数类型。<br>
 * 返回值类型：无返回值。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:45
 */
public class RecyclingSequenceNumberFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.RecyclingSequenceNumber.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(RecyclingSequenceNumberFunc.class);

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

            String key = stringParse(param1);
            Integer number = integerParse(stringParse(param2));

            ISerialNumber serialNumber = VDS.getIntance().getSerialNumber(key);
            ISerialNumber.RecycleState recycleState = serialNumber.recycleSequenceNumber(number);

            outputVo.put(recycleState.name());
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

    private String stringParse(Object obj) {
        return (obj == null) ? null : obj.toString();
    }

    private Integer integerParse(String s) {
        try {
            return (s == null) ? null : Integer.valueOf(s);
        } catch (NumberFormatException e) {
            log.warn("函数【" + funcCode + "】String转integer发生错误，忽略该参数 msg={}", e.getMessage());
            return null;
        }
    }
}
