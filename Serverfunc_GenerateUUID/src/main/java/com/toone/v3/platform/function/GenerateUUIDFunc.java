package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.platform.plugin.util.VdsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成UUID<br>
 * <br>
 * 代码示例:GenerateUUID()返回一个uuid字符串。<br>
 * 无参数；<br>
 * 返回值为字符串。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:43
 */
public class GenerateUUIDFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GenerateUUIDRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GenerateUUIDFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        try {
            String uuid = VdsUtils.uuid.generate();
            outputVo.setSuccess(true);
            outputVo.put(uuid);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，msg=" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
