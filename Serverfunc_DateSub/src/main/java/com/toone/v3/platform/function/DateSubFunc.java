package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 将时间减去一定的时间间隔，返回计算后的时间字符串。<br>
 * <br>
 * 代码示例:DateSub("2016-11-05 18:20:30",3,"M")返回值为"2016-08-05 18:20:30"。<br>
 * 参数1--转换的日期(时间类型或满足时间格式的字符串类型)，格式为yyyy-MM-dd HH:mm:ss；<br>
 * 参数2--减少的时间数(数字类型)；<br>
 * 参数3--时间数的单位(字符串类型)，s-秒,m-分,H-时,d-日,M-月,y-年；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:01
 */
public class DateSubFunc implements IFunction {

    // 函数编码
    private final static String funcCode = DateSubRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(DateSubFunc.class);

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

            service.checkParamBlank(funcCode, param1);

            // 校验参数2是否为正整数
//            service.checkParamInteger(funcCode, param2, 2);
//            int number = Integer.parseInt(param2.toString());

            if(param2 == null){
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数不能为空");
            }
            if (!(param2 instanceof Integer || param2 instanceof Double)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须为整数类型，参数2：" + param2);
            }

            // 解析参数为整数类型的数值
            int number;
            NumberFormat nf = NumberFormat.getInstance();
            try {
                number = Integer.parseInt(nf.format(param2).replace(",", ""));
            } catch (Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须为整数类型，参数2：" + param2);
            }

            if (number <= 0) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须大于0，参数2：" + param2);
            }

            number = -number;


            SimpleDateFormat sdf = service.getSimpleDateFormat(funcCode, param1, 1);
            sdf.setLenient(false);

            Calendar cal = Calendar.getInstance();
            cal.clear();
            Date date = sdf.parse(param1.toString());
            cal.setTime(date);
            String str3 = param3 == null ? "NULL" : param3.toString();
            if ("h".equalsIgnoreCase(str3)) {
                cal.add(Calendar.HOUR, number);
            } else if ("m".equals(str3)) {
                cal.add(Calendar.MINUTE, number);
            } else if ("s".equalsIgnoreCase(str3)) {
                cal.add(Calendar.SECOND, number);
            } else if ("d".equalsIgnoreCase(str3)) {
                cal.add(Calendar.DATE, number);
            } else if ("M".equals(str3)) {
                cal.add(Calendar.MONTH, number);
            } else if ("y".equalsIgnoreCase(str3)) {
                cal.add(Calendar.YEAR, number);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】的第3个参数必须是枚举值：s:秒、m:分、H：时、d：日、M：月、y：年，当前值：" + str3);
            }

            date = cal.getTime();
            String result = sdf.format(date);

            outputVo.setSuccess(true);
            outputVo.put(result);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + "，" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3, e);
        }
        return outputVo;
    }
}
