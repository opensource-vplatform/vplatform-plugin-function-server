package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.jdbc.api.model.ColumnType;
import com.yindangu.v3.business.jdbc.api.model.IColumn;
import com.yindangu.v3.business.jdbc.api.model.IDataSetMetaData;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 获取实体的某个字段的最小值<br>
 * <br>
 * 代码示例：MinColumn("BR_IN_PARENT.student","grade")，返回值为方法输入实体"student"的"grade"字段的最小值。<br>
 * 参数1--实体名(字符串类型，需要加入前缀)； 例如：BR_IN_PARENT:方法输入实体，BR_OUT_PARENT：方法输出实体，BR_VAR_PARENT：方法变量实体；<br>
 * 参数2--字段名称(字符串类型)；<br>
 * 返回值类型：数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:42
 */
public class MinColumnFunc implements IFunction {

    // 函数编码
    private final static String funcCode = MinColumnRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(MinColumnFunc.class);

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
            		.min()
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
        	String msg = util.getFullParams("计算有误");
            outputVo.setSuccess(false);
            //outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + entityname + "，参数2：" + columnname + ", " + e.getMessage());
            outputVo.setMessage(msg);
            log.error(msg, e);
        }
        return outputVo;
    }
    
}
