package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * 返回服务器当前的日期和时间，格式为yyyy-MM-dd HH:mm:ss<br>
 * <br>
 * 代码示例:DateTimeNow()返回值为"2011-10-19 12:03:44"。<br>
 * 无参数；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:02
 */
public class DateTimeNowFunc implements IFunction {

    // 函数编码
    private final static String funcCode = DateTimeNowRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(DateTimeNowFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String result = sdf.format(System.currentTimeMillis());

            outputVo.setSuccess(true);
            outputVo.put(result);
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
