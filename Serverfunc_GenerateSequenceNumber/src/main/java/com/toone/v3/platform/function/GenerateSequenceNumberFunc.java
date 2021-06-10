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
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

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
