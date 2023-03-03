package com.toone.v3.platform.function;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.jdbc.api.model.IColumn;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;

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

        ColumnCalculateUtil util = new ColumnCalculateUtil(funcCode, context);
        try {
        	IDataView data = util.getDataView(0);
        	IColumn column = util.getColumnByDataView(1,data);

            // TODO 求和应该使用功大数计算BigDecimal
        	if(data.size() >0) {
	            String filter = util.getFilters(2);
	            Map<String,Object> params = null;
	            if(filter.length()>0){ //有条件
	            	params = util.getFilterParams(3);
	            }

	            Number rs =  ColumnCalculateBuilder.newBuild(funcCode)
            		.setColumn(column)
            		.setDataView(data)
            		.setFilters(filter)//二次过滤
            		.setParams(params)
            		.build()
            		.avg()
            	; 
	            outputVo.put(rs); 
            }
            else {
            	outputVo.put(Integer.valueOf(0));	
            }
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
        	String msg = util.getFullParams("计算有误:【" + e.getMessage() + "】");
            outputVo.setSuccess(false);
            //outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + entityname + "，参数2：" + columnname + ", " + e.getMessage());
            outputVo.setMessage(msg);
            log.error(msg, e);
        }
        return outputVo;
    }
    
    
}
