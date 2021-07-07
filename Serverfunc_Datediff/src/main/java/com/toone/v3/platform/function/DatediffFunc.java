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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 返回两个日期之间的时间间隔；<br>
 * <br>
 * 代码示例: Datediff("2012-11-25 01:00:32","2012-11-24 05:55:33","d")，返回值为 0.7951273148148148。<br>
 * 参数1--原日期(时间类型或满足时间格式的字符串类型)；<br>
 * 参数2--目标日期(时间类型或满足时间格式的字符串类型)；<br>
 * 参数3--差值的单位(字符串类型)，s-秒,m-分,H-时,d-日；<br>
 * 注：时间间隔=原日期-目标日期<br>
 * 返回值为数值类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:01
 */
public class DatediffFunc implements IFunction {

    // 函数编码
    private final static String funcCode = DatediffRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(DatediffFunc.class);

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
            service.checkParamNull(funcCode, context, param1, param2, param3);

            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

            String srcTime = (String) param1;

            String destTime = (String) param2;

            String timeUnit = (String) param3;

            if (checkYYYMMDD(srcTime)) {
                srcTime = srcTime + " 00:00:00";
            } else if (srcTime.length() == 8 && isValidDate(df, srcTime)) {
                srcTime = srcTime.substring(0, 4) + "-" + srcTime.substring(4, 6) + "-" + srcTime.substring(6, 8) + " 00:00:00";
            }
            if (checkYYYMMDD(destTime)) {
                destTime = destTime + " 00:00:00";
            } else if (destTime.length() == 8 && isValidDate(df, destTime)) {
                destTime = destTime.substring(0, 4) + "-" + destTime.substring(4, 6) + "-" + destTime.substring(6, 8) + " 00:00:00";
            }

            if (!checkYYYYMMDDhhmmss(srcTime)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数是时间字符串，格式不符合要求，参数1：" + srcTime);
            }
            if (!checkYYYYMMDDhhmmss(destTime)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数是时间字符串，格式不符合要求，参数2：" + destTime);
            }

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            double diffTime = sf.parse(srcTime).getTime() - sf.parse(destTime).getTime();

            // 时间差毫秒与指定时间之间的转换
            double result = diffTime / getTimeUnit2MI(timeUnit);

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

    private boolean isValidDate(SimpleDateFormat dateFormat, String s) {
        try {
            dateFormat.parse(s);
            return true;
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
    }

    /**
     * 校验时间是否YYY-MM-DD格式
     *
     * @param s 格式字符串
     * @return 校验格式
     */
    private boolean checkYYYMMDD(String s) {
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}?$");
        Matcher matcher = pattern.matcher(s);

        return matcher.find();
    }

    /**
     * 校验时间是否YYY-MM-D hh:mm:ss格式
     *
     * @param s 格式字符串
     * @return 校验格式
     */
    private boolean checkYYYYMMDDhhmmss(String s) {
        Pattern pattern = Pattern
                .compile("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{2}:\\d{2}:\\d{2}?$");
        Matcher matcher = pattern.matcher(s);

        return matcher.find();
    }

    /**
     * 单位与毫秒之间的转换
     *
     * @param timeUnit
     *            时间类型
     * @return 返回进制
     */
    private double getTimeUnit2MI(String timeUnit) throws ServerFuncException {
        if ("d".equalsIgnoreCase(timeUnit)) {
            return TIMEUNIT_D;
        } else if ("h".equalsIgnoreCase(timeUnit)) {
            return TIMEUNIT_H;
        } else if ("m".equalsIgnoreCase(timeUnit)) {
            return TIMEUNIT_M;
        } else if ("s".equalsIgnoreCase(timeUnit)) {
            return TIMEUNIT_S;
        } else
            throw new ServerFuncException("函数【" + this.funcCode + "】的第3个参数是时间单位，必须满足：s-秒,m-分,H-时,d-日，参数3：" + timeUnit);

    }

    // 天与毫秒之间的转换
    private static final double TIMEUNIT_D = (24 * 60 * 60 * 1000);
    // 时与毫秒之间的转换
    private static final double TIMEUNIT_H = (60 * 60 * 1000);
    // 分钟与毫秒之间的转换
    private static final double TIMEUNIT_M = (60 * 1000);
    // 秒与毫秒之间的转换
    private static final double TIMEUNIT_S = 1000;
}
