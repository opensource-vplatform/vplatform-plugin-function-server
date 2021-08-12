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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 计算实体某个字段的总和<br>
 * <br>
 * 代码示例: TotalColumnFunc("BR_IN_PARENT.xiaoshu","price") 返回实体字段price的总和。<br>
 * 参数1--活动集实体(字符串类型)；<br>
 * 参数2--计算总和的字段名称(字符串类型)；<br>
 * 返回值类型：数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:57
 */
public class TotalColumnFunc implements IFunction {

    // 函数编码
    private final static String funcCode = TotalColumnRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(TotalColumnFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object entityname = null;
        Object columnname = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 2);
            entityname = context.getInput(0);
            columnname = context.getInput(1);
            String column = "";
            IDataView data;
            Double sum = 0.0;
            int sum2 = 0;
            int ctype = 0;
            if(entityname instanceof IDataView){
                data = (IDataView)entityname;
            }else if(entityname instanceof String){
                data = VDS.getIntance().getFormulaEngine().eval(entityname.toString());
            }else{
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须是字符串类型或者实体类型，参数1：" + entityname);
            }
            if(columnname instanceof String){
                column = columnname.toString();
            }else{
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须是字符串类型，参数2：" + columnname);
            }
            //精度位数
            int precision = 0;
            List mapList = data.getDatas();
            if(mapList.size() == 0){
                outputVo.put(0);
                outputVo.setSuccess(true);
                return outputVo;
            }
            else{
                IDataSetMetaData setMetaData = data.getMetadata();
                try {
                    IColumn vColumn = setMetaData.getColumn(column);
                    ColumnType type = vColumn.getColumnType();
                    precision = vColumn.getPrecision();
                    if(type==ColumnType.Integer){
                        ctype = 1;
                    }
                } catch (SQLException e) {
                    throw new ServerFuncException("函数【" + funcCode + "】计算失败，字段类型获取失败！" + e.toString());
                }
            }
            for (Object map : mapList) {
                if(map instanceof Map){
                    Map recordMap = (Map)map;
                    Object value = recordMap.get(column);
                    if(value!=null) {
                        if(value instanceof Double || value instanceof Integer || value instanceof java.math.BigDecimal){
                            sum =sum + Double.parseDouble(value.toString());
                        }else{
                            throw new ServerFuncException("函数【" + funcCode + "】的第2个参数对应的字段值必须是数字类型，参数2：" + columnname);
                        }
                    }
                }
            }
            if (ctype==1) {
                sum2 = ((Double)sum).intValue();
                outputVo.put(sum2);
            }else{
                BigDecimal b = new BigDecimal(sum);
                sum = b.setScale(precision,BigDecimal.ROUND_HALF_UP).doubleValue();
                outputVo.put(sum);
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + entityname + "，参数2：" + columnname + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + entityname + "，参数2：" + columnname, e);
        }
        return outputVo;
    }
}
