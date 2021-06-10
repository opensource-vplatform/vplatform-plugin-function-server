package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 拼接格式<br>
 * <br>
 * 代码示例:Format("ab{0}cd{1}","12","3") 返回值为"ab12cd3"。<br>
 * 参数1--模式串(字符串类型)；<br>
 * 参数2--拼接串(各种类型)；<br>
 * 参数n--拼接串(各种类型)；<br>
 * 返回值为字符串。 <br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:30
 */
public class FormatFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.Format.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(FormatFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            int size = context.getInputSize();
            if (size < 2) {
                throw new ServerFuncException("函数【" + funcCode + "】至少需要2个参数，当前参数个数：" + size);
            } else {
                param = context.getInput(0);
                if (!(param instanceof String)) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第1个参数不能为null，且必须是字符串类型，参数1：" + param);
                } else {
                    String str = (String) param;
                    for (int i = 1; i < size; i++) {
                        Object input = context.getInput(i);
                        str = str.replace("{" + (i - 1) + "}", input == null ? "" : input.toString());
                    }
                    outputVo.setSuccess(true);
                    outputVo.put(str);
                }
            }
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
