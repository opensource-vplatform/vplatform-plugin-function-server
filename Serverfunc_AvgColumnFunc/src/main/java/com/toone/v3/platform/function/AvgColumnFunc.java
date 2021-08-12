package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 计算实体某个字段的平均值。<br>
 * <br>
 * 代码示例:AvgColumnFunc("BR_IN_PARENT.xiaoshu","price") 返回实体字段price的平均值。<br>
 * 参数1--活动集实体(字符串类型)；<br>
 * 参数2--计算平均值的字段名称(字符串类型))；<br>
 * 返回值类型：数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 8:34
 */
public class AvgColumnFunc implements IFunction {

    private final static String funcCode = AvgColumnRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(AvgColumnFunc.class);

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

            IDataView dv;
            if(param1 instanceof IDataView) {
                dv = (IDataView) param1;
            } else if(param1 instanceof String) {
                dv = VDS.getIntance().getFormulaEngine().eval(param1.toString());
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】的第一个参数必须是字符串类型或者实体类型，当前值：" + param1);
            }

            String columnName = param2.toString();

            List<Map<String, Object>> datas = dv.getDatas();

            // TODO 求和应该使用功大数计算BigDecimal
            int count = 0;
            double sum = 0.0d;
            for(Map<String, Object> data : datas) {
                Object value = data.get(columnName);
                if(value!=null) {
                    if(value instanceof Double || value instanceof Integer || value instanceof java.math.BigDecimal){
                        sum =sum + Double.parseDouble(value.toString());
                    }else{
                        throw new ServerFuncException("函数【" + funcCode + "】的第2个参数对应的字段值必须是数字类型，参数2：" + param2);
                    }
                }
                count++;
            }

            if(count > 0) {
                outputVo.put(sum/count);
            } else {
                outputVo.put(0.0d);
            }
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + "，" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2, e);
        }

        return outputVo;
    }
}
