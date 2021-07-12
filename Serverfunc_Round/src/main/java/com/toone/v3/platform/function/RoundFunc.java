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
 * 求浮点数的四舍五入值<br>
 * <br>
 * 代码示例：Round(3.555,2)返回值为3.56。<br>
 * 参数1--要舍入的数（数字类型）；<br>
 * 参数2--舍入的位数（非负整数类型，0表示四舍五入到整数部分）；<br>
 * 返回值类型：数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:46
 */
public class RoundFunc implements IFunction {

    // 函数编码
    private final static String funcCode = RoundRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(RoundFunc.class);

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
            service.checkParamNull(funcCode, param1, param2);

            if(!(param1 instanceof Number)){ //提高性能
	            try {
	                Double.parseDouble(param1.toString());
	            } catch(Exception e) {
	                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须是数字类型，参数1：" + param1);
	            }
            }
            int scale;
            try {
            	if(param2 instanceof Number) {
            		scale = ((Number)param2).intValue();
            	}
            	else {
            		scale = Integer.parseInt(param2.toString());
            	}
            } catch(Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须是整数类型，参数2：" + param2);
            }
            if (scale < 0) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须是非负整数，参数2：" + param2);
            }
            BigDecimal result ;
            if(param1 instanceof BigDecimal) {
            	result = (BigDecimal)param1; 
            }
            else {
            	result = new BigDecimal(param1.toString());
            }
            BigDecimal rs = result.setScale(scale, BigDecimal.ROUND_HALF_UP);
            outputVo.put(rs);
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

//    public boolean isNumeric(String str){
//        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
//        return str.matches(regex);
//    }
//
//    public boolean isInteger(String str){
//        String regex = "^\\+?[1-9][0-9]*$";
//        return str.matches(regex);
//    }
}
