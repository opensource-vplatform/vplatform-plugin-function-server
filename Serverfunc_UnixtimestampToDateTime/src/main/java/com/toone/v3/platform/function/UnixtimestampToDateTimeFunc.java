package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 根据时间戳转成日期时间。<br>
 * <br>
 * 代码示例:UnixtimestampToDateTime("1535444053","yyyy-MM-dd HH:mm:ss"),返回值为:2018-08-28 16:14:13<br>
 * 参数1--时间戳(字符串类型),单位是秒,必填<br>
 * 参数2--日期时间格式(字符串类型),格式例如:yyyy-MM-dd HH:mm:ss、yyyy/MM/dd HH:mm:ss、yyyy年MM月dd日 HH时mm分ss秒,默认格式:yyyy-MM-dd HH:mm:ss<br>
 * 返回值为字符串类型<br>
 *
 * @Author xugang
 * @Date 2021/6/1 17:10
 */
public class UnixtimestampToDateTimeFunc implements IFunction {

    // 函数编码
    private final static String funcCode = UnixtimestampToDateTimeRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(UnixtimestampToDateTimeFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        try {
            int size = context.getInputSize();
            String stamp;
            String format = "";
            if(size == 1) {
                stamp = (String) context.getInput(0);
                param1 = stamp;
            } else if(size == 2) {
                stamp = (String) context.getInput(0);
                format = (String) context.getInput(1);
                param1 = stamp;
                param2 = format;
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】只能存在1个或者2个参数，当前参数个数：" + size);
            }
            if(stamp == null || stamp.equals("")) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数不能为空");
            }
            if(format == null || "".equals(format)) {
                format = "yyyy-MM-dd HH:mm:ss";
            }

            long unixTimestamp = Long.parseLong(stamp) * 1000;
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            String result = sdf.format(new Date(unixTimestamp));

            outputVo.setSuccess(true);
            outputVo.put(result);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" +param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" +param2, e);
        }
        return outputVo;
    }
}
