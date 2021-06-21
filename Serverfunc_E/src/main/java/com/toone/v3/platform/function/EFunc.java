package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 自然对数的底数<br/>
 * <br>
 * 代码示例:E() 返回值为2.718281828459045<br>
 * 无参数<br>
 * 返回值为小数<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:10
 */
public class EFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.E.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(EFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        try {
            BigDecimal result = BigDecimal.valueOf(Math.E);
            outputVo.setSuccess(true);
            outputVo.put(result.toPlainString());
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
