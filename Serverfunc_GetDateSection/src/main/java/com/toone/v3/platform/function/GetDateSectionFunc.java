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
 * 获取日期的某一部分<br>
 * <br>
 * 代码示例:GetDateSection("2012-09-20",2)<br>
 * 参数1--给定的日期，格式yyyy-MM-dd或者yyyy-MM-dd HH:mm:ss(时间类型或满足时间格式的字符串类型)；<br>
 * 参数2--指定要返回的日期部分：0:全部，1:年，2：月，3：日，4：小时，6：分，7：秒；9：星期(整型)；<br>
 * 返回值可能为字符串（参数2为 0）或者整型，建议使用时用字符串接收返回值。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:48
 */
public class GetDateSectionFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.GetDateSection.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(GetDateSectionFunc.class);

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
            String strDate = param1.toString();

            Calendar calendar = Calendar.getInstance();
            Date date = null;
            try {
                SimpleDateFormat sdf = service.getSimpleDateFormat(funcCode, strDate, 1);
                date = sdf.parse(strDate);
                calendar.setTime(date);
            } catch(Exception e) {
                // 兼容历史
                calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1990-01-01 00:00:00"));
            }
            int dateSection;
            try {
                dateSection = Integer.parseInt(param2.toString());
            } catch (Exception e) {
                throw new ServerFuncException("函数【】的第2个参数必须是整形，参数2：" + param2);
            }
            switch (dateSection) {
                case 0:// 全部
                    outputVo.put(strDate + " " + getDate(date));
                    break;
                case 1:// 年
                    outputVo.put(calendar.get(Calendar.YEAR));
                    break;
                case 2:// 月
                    outputVo.put(supplyZero(calendar.get(Calendar.MONTH) + 1));
                    break;
                case 3:// 日
                    outputVo.put(supplyZero(calendar.get(Calendar.DAY_OF_MONTH)));
                    break;
                case 4:// 时
                    outputVo.put(supplyZero(calendar.get(Calendar.HOUR_OF_DAY)));
                    break;
                case 6:// 分
                    outputVo.put(supplyZero(calendar.get(Calendar.MINUTE)));
                    break;
                case 7:// 秒
                    outputVo.put(supplyZero(calendar.get(Calendar.SECOND)));
                    break;
                case 9:// 星期
                    outputVo.put(getDateNum(date));
                    break;
                default:
                    throw new ServerFuncException("函数【" + funcCode + "】的第2个参数不正确，不支持该转换类型：" + dateSection);
            }
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }

    private String supplyZero(int num){
        return num < 10 ? "0" + num : num + "";
    }

    private int getDateNum(Date date){
        //默认是第一天：星期天
        if (date == null) {
            return 0;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int num = calendar.get(Calendar.DAY_OF_WEEK);
        //跟前台保持一致，从0开始
        return num - 1;
    }

    private String getDate(Date date){
        String str;
        int num = getDateNum(date);
        switch(num){
            case 1:
                str = "一";
                break;
            case 2:
                str = "二";
                break;
            case 3:
                str = "三";
                break;
            case 4:
                str = "四";
                break;
            case 5:
                str = "五";
                break;
            case 6:
                str = "六";
                break;
            default:
                str = "日";
                break;
        }
        return "星期" + str;
    }
}
