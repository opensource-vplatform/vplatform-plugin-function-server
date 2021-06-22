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
 * 取连续的流水号<br>
 * <br>
 * 代码示例：GenerateSequenceNumber ("123456", "INC") 返回值是："1" 。<br>
 * 参数1：流水号种子，字符串类型。根据该种子生成流水号，最长128位字符串，建议64位或者更短。<br>
 * 参数2：生成模式，字符串类型。<br>
 * 支持模式：<br>
 * INC：最大号+1；<br>
 * REUSE：重用废弃的流水号，当没有废号可用时，同INC；<br>
 * ASSIGNERR：用给定的值作为流水号，如果该值已被占用，那么生成失败；<br>
 * ASSIGNINC：用给定的值作为流水号，如果该值已被占用，那么同INC；<br>
 * ASSIGNREUSE：用给定的值作为流水号，如果该值已被占用，那么同REUSE；<br>
 * 参数3：需要使用的废弃流水号，整数类型。当且仅当参数2为ASSIGNERR、ASSIGNINC、ASSIGNREUSE才有效。<br>
 * 返回值：流水号，整数类型<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:41
 */
public class GenerateSequenceNumberFunc implements IFunction {

    // 函数编码
    private String funcCode = ServerFuncCommonUtils.GenerateSequenceNumber.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(GenerateSequenceNumberFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            int size = context.getInputSize();
            if(size == 2) {
                param1 = context.getInput(0);
                param2 = context.getInput(1);
            } else if(size == 3) {
                param1 = context.getInput(0);
                param2 = context.getInput(1);
                param3 = context.getInput(2);

            } else {
                throw new ServerFuncException("函数【" + funcCode + "】需要2个或者3个参数，当前参数个数：" + size);
            }

            String key = stringParse(param1);
            String model = stringParse(param2);
            Integer abandonNumber = size>2 ? integerParse(stringParse(param3)) : null;

            if (key==null || model==null) {
                throw new ServerFuncException("函数【" + funcCode + "】的参数1、参数2不得为空。");
            }

            ISerialNumber serialNumber = VDS.getIntance().getSerialNumber(key);

            int number = serialNumber.generateSequenceNumber(ISerialNumber.Model.parse(model), abandonNumber);

            outputVo.put(number);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参值1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3, e);
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
