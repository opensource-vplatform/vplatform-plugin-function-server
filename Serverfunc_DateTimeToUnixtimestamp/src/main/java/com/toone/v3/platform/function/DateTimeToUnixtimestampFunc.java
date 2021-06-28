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
 * 根据日期时间转成时间戳。<br>
 * <br>
 * 代码示例:DateTimeToUnixtimestamp("2018-08-28 16:14:13"),返回值为:1535444053<br>
 * 参数1--日期时间,格式必须为:yyyy-MM-dd HH:mm:ss(字符串类型),必填<br>
 * 返回值为字符串类型<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:03
 */
public class DateTimeToUnixtimestampFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.DateTimeToUnixtimestamp.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(DateTimeToUnixtimestampFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
//            service.checkParamBlank(funcCode, param);

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(param.toString());
                long second = date.getTime() / 1000;
                outputVo.put(second + "");
            } catch (Exception e) {
//                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须满足格式：yyyy-MM-dd HH:mm:ss，当前值：" + param.toString());
                // 兼容历史
                outputVo.put("");
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param + "，" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param, e);
        }
        return outputVo;
    }
}
