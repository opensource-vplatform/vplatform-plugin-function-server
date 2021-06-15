package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 按参数顺序合并字符串。可以任意多个参数。<br>
 * <br>
 * 代码示例:ConcatStr("hello"," ","world") 返回值为"hello world"。<br>
 * 参数1--字符串(字符串类型)；<br>
 * 参数n--字符串(字符串类型)；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 13:37
 */
public class ConcatStrFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.ConcatStr.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(ConcatStrFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        try {
            int size = context.getInputSize();
            if (size < 2) {
                throw new ServerFuncException("函数【" + funcCode + "】至少需要2个参数，当前参数个数：" + size);
            }

            StringBuilder result = new StringBuilder(1024);
            for (int i = 0; i < size; i++) {
                Object param = context.getInput(i);
                if (param == null) {
                    // nothing to do
                } else if (!(param instanceof String)) {
                    throw new ServerFuncException("函数【" + funcCode + "】的全部参数都必须是字符串类型，参数" + (i + 1) + "：" + param);
                } else {
                    result.append((String) param);
                }
            }

            outputVo.setSuccess(true);
            outputVo.put(result.toString());
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + ServerFuncCommonUtils.ConcatStr.Function_Code() + "】计算有误，msg=" + e.getMessage());
            log.error("函数【" + ServerFuncCommonUtils.ConcatStr.Function_Code() + "】计算失败", e);
        }
        return outputVo;
    }
}
