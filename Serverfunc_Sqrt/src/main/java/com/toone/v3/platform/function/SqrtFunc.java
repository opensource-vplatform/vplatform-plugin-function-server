package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 数字的平方根<br>
 * <br>
 * 代码示例:Sqrt(4)返回值为2。<br>
 * 参数1--指定的数字(数字类型，必须大于0)；<br>
 * 返回值类型：数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:55
 */
public class SqrtFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.Sqrt.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(SqrtFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
            service.checkParamNull(funcCode, param);
            //处理入参是科学计数法的问题
            BigDecimal result;
            try {
                if (param instanceof Double) {
                    result = new BigDecimal((Double) param);
                } else if (param instanceof Integer) {
                    result = new BigDecimal((Integer) param);
                } else if (param instanceof Long) {
                    result = new BigDecimal((Long) param);
                } else if (param instanceof BigDecimal) {
                    result = (BigDecimal) param;
                } else if (param instanceof String) {
                    result = new BigDecimal((Double.parseDouble(param.toString())));
                } else {
                    result = new BigDecimal((Double) param);
                }
            } catch (NumberFormatException e) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须为数字类型，参数1：" + param);
            }
            if (result.doubleValue() < 0) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须大于0，参数1：" + param);
            }
            //return Math.sqrt(param);
            //处理科学技术法显示问题
            result = new BigDecimal(Math.sqrt(result.doubleValue()));
//            String rs = result.toPlainString();
//            //处理小数位数
//            if (rs.indexOf(".") != -1 && rs.length() > 18) {
//                String[] rsArr = rs.split("\\.");
//                rs = rsArr[0] + "." + rsArr[1].substring(0, (17 - rsArr[0].length()));
//            }

            outputVo.put(result);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
