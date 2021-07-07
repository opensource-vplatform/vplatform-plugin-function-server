package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 短日期<br>
 * <br>
 * 代码示例:ShortDateNow()，返回值为"2015-11-03"。<br>
 * 无参数；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:55
 */
public class ShortDateNowFunc implements IFunction {

    // 函数编码
    private final static String funcCode = ShortDateNowRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(ShortDateNowFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String res = sdf.format(new Date());
            outputVo.put(res);
            outputVo.setSuccess(true);
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误" + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
