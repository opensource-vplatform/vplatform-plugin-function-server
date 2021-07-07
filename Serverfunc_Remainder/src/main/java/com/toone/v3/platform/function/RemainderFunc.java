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
 * 余数<br>
 * <br>
 * 代码示例：Remainder(5,2)返回值为1。<br>
 * 参数1--被除数（数字类型）；<br>
 * 参数2--除数（数字类型）；<br>
 * 返回值类型：数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:45
 */
public class RemainderFunc implements IFunction {

    // 函数编码
    private final static String funcCode = RemainderRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(RemainderFunc.class);

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
            service.checkParamNumeric(funcCode, param1, param2);

            if (param1.toString().equals("0"))
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数不能为0");

            double arg1 = Double.parseDouble(param1.toString());
            double arg2 = Double.parseDouble(param2.toString());
            double result = arg1 % arg2;
            //return result;
            //处理科学计数法显示问题
            BigDecimal result1 = new BigDecimal(result);
            outputVo.put(result1.toPlainString());
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2, e);
        }
        return outputVo;
    }
}
