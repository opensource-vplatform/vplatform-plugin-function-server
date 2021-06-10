package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将一时间的单位转换成另一种单位后的时间数。<br>
 * <br>
 * 代码示例:DateConvert(30,"s","m")，返回值为0.5。<br>
 * 参数1--时间数(数值类型)；<br>
 * 参数2--原时间的单位(字符串类型)，s-秒,m-分,H-时,d-日； <br>
 * 参数3--目标时间的单位(字符串类型)，s-秒,m-分,H-时,d-日；<br>
 * 返回值为数值类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 20:24
 */
public class DateConvertFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.DateConvert.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(DateConvertFunc.class);

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

            // 校验参数1是否为正整数
            service.checkParamNumeric(funcCode, param1, 1);

            double time = Double.parseDouble(param1.toString());
            // 转成毫秒
            time = toMillis(time, param2);
            // 毫秒转成指定单位
            time = toUnit(time, param3);

            outputVo.setSuccess(true);
            outputVo.put(time);
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

    private double toUnit(double time, Object param3) throws ServerFuncException {
        //s-秒,m-分,H-时,d-日；
        if (param3 == null) {
            throw new ServerFuncException("函数【" + ServerFuncCommonUtils.DateConvert.Function_Code() + "】的第3个参数必须是枚举值：s：秒、m：分、H：时、d：日，当前值：NULL");
        }
        String str3 = param3.toString().trim();
        switch (str3) {
            case "s":
                return time / 1000;
            case "m":
                return time / (1000 * 60);
            case "H":
                return time / (1000 * 60 * 60);
            case "d":
                return time / (1000 * 60 * 60 * 24);
            default:
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.DateConvert.Function_Code() + "】的第3个参数必须是枚举值：s：秒、m：分、H：时、d：日，当前值：" + str3);
        }
    }

    private double toMillis(double time, Object param2) throws ServerFuncException {
        //s-秒,m-分,H-时,d-日；
        if (param2 == null) {
            throw new ServerFuncException("函数【" + ServerFuncCommonUtils.DateConvert.Function_Code() + "】的第2个参数必须是枚举值：s：秒、m：分、H：时、d：日，当前值：NULL");
        }
        String str2 = param2.toString().trim();
        switch (str2) {
            case "s":
                return time * 1000;
            case "m":
                return time * 1000 * 60;
            case "H":
                return time * 1000 * 60 * 60;
            case "d":
                return time * 1000 * 60 * 60 * 24;
            default:
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.DateConvert.Function_Code() + "】的第2个参数必须是枚举值：s：秒、m：分、H：时、d：日，当前值：" + str2);
        }
    }
}
