package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据指定格式，将时间格式化为字符串返回。<br>
 * <br>
 * 代码示例:DateToString("yyyy-MM-dd HH:mm:ss",DateTimeNow())，返回值为"2012-04-19 12:03:44"。<br>
 * 参数1--格式串(字符串类型)；<br>
 * 参数2--时间(时间类型或满足时间格式的字符串类型)；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:04
 */
public class DateToStringFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.DateToString.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(DateToStringFunc.class);

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

            service.checkParamBlank(funcCode, param1, param2);

            SimpleDateFormat sdf = service.getSimpleDateFormat(funcCode, param2, 2);

            Date date = sdf.parse(param2.toString());
            try {
                String result = new SimpleDateFormat(param1.toString()).format(date);
                outputVo.setSuccess(true);
                outputVo.put(result);
            } catch (Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数不符合时间格式要求，当前值：" + param1.toString());
            }
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + "，" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2, e);
        }
        return outputVo;
    }
}
