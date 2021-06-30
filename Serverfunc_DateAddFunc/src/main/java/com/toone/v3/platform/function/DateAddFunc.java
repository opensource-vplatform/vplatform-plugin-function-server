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
import java.util.Calendar;
import java.util.Date;

/**
 * 将时间加上一定的时间间隔，返回计算后的时间字符串。<br>
 * <br>
 * 代码示例:DateAddFunc("2012-03-05 18:20:30",30,"H")，返回值为"2012-03-07 00:20:30"。<br>
 * 参数1--时间，格式为yyyy-MM-dd HH:mm:ss(时间类型或满足时间格式的字符串类型)；<br>
 * 参数2--增加的时间数(数字类型)； <br>
 * 参数3--日期数的单位(字符串类型)，y:年运算 M:月运算 d：日运算 H:小时运算 mi:分运算 s：秒运算<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 17:25
 */
public class DateAddFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.DateAddFunc.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(DateAddFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            // 校验参数个数
            service.checkParamSize(funcCode, context, 3);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            param3 = context.getInput(2);

            // 校验参数2是否为正整数
            service.checkParamNull(funcCode, param1, param2);

            SimpleDateFormat sdf = service.getSimpleDateFormat(funcCode, param1.toString(), 1);
            Double amountD = Double.parseDouble(param2.toString());
            int amount = amountD.intValue();
            int field = getDateType(funcCode, param3.toString(), 3);

            // 增加日期
            sdf.setLenient(false);
            Calendar cal = Calendar.getInstance();
            cal.clear();
            Date date = sdf.parse(param1.toString());
            cal.setTime(date);
            cal.add(field, amount);

            // 获取增加后的日期
            date = cal.getTime();
            String result = sdf.format(date);

            outputVo.setSuccess(true);
            outputVo.put(result);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 +"，参数3：" +param3 + "，" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3, e);
        }
        return outputVo;
    }

    public int getDateType(String funcName, Object obj, int index) throws ServerFuncException {
        if(obj == null) {
            throw new ServerFuncException("函数【" + funcName + "】的第" + index + "个参数必须是枚举值：y:年运算、M:月运算、d：日运算、H:小时运算、mi:分运算、s：秒运算，当前值：" + obj);
        } else {
            String str = obj.toString().trim();
            if("y".equalsIgnoreCase(str)) {
                return Calendar.YEAR;
            } else if("m".equalsIgnoreCase(str)) {
                return Calendar.MONTH;
            } else if("d".equalsIgnoreCase(str)) {
                return Calendar.DAY_OF_MONTH;
            } else if("h".equalsIgnoreCase(str)) {
                return Calendar.HOUR;
            } else if("mi".equalsIgnoreCase(str)) {
                return Calendar.MINUTE;
            } else if("s".equalsIgnoreCase(str)) {
                return Calendar.SECOND;
            } else {
                throw new ServerFuncException("函数【" + funcName + "】的第" + index + "个参数必须是枚举值：y:年运算、M:月运算、d：日运算、H:小时运算、mi:分运算、s：秒运算，当前值：" + str);
            }
        }
    }
}
