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
 * 求指定实体的某个字段的最大值<br>
 * <br>
 * 代码示例:MaxColumn("EntityName","ColumnName") 返回值为实体"EntityName"的"ColumnName"字段的最大值。<br>
 * 参数1--实体名（字符串类型，需要加入前缀)；例如：BR_IN_PARENT:方法输入实体，BR_OUT_PARENT：方法输出实体，BR_VAR_PARENT：方法变量实体；<br>
 * 参数2--字段名称(字符串类型)；<br>
 * 返回值类型：数字类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:42
 */
public class MaxColumnFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.MaxColumn.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(MaxColumnFunc.class);

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
            IDataView data = null;
            BigDecimal resultValue = new BigDecimal(0);
            int resultValue2 = 0;
            int ctype = 0;
            if (entityname instanceof IDataView) {
                data = (IDataView) entityname;
            } else if (entityname instanceof String) {
                data = (IDataView) VDS.getIntance().getFormulaEngine().eval(entityname.toString());
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数类型必须是字符串类型或者实体类型，参数1：" + entityname);
            }
            if (columnname instanceof String) {
                column = columnname.toString();
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数类型必须是字符串类型，参数2：" + columnname);
            }
            List mapList = data.getDatas();
            if (mapList.size() == 0) {
                outputVo.put(0);
                outputVo.setSuccess(true);
                return outputVo;
            } else {
                IDataSetMetaData setMetaData = data.getMetadata();
                try {
                    IColumn vColumn = setMetaData.getColumn(column);
                    ColumnType type = vColumn.getColumnType();
                    if (type == ColumnType.Integer) {
                        ctype = 1;
                    }
                } catch (Exception e) {
                    throw new ServerFuncException("函数【" + funcCode + "】计算失败，字段类型获取失败！" + e.toString());
                }
            }
            resultValue = new BigDecimal(data.getDatas().get(0).get(column).toString());
            for (Object map : mapList) {
                if (map instanceof Map) {
                    Map recordMap = (Map) map;
                    Object value = recordMap.get(column);
                    if (value != null) {
                        if (value instanceof Double || value instanceof Integer || value instanceof java.math.BigDecimal) {
                            //比较获取最大值
                            BigDecimal valueBig = new BigDecimal(value.toString());
                            if (valueBig.compareTo(resultValue) > 0) {
                                resultValue = valueBig;
                            }
                        } else {
                            throw new ServerFuncException("函数【" + funcCode + "】的第2个参数对应的字段类型必须是数字类型");
                        }
                    }
                }
            }
            if (ctype == 1) {
                resultValue2 = resultValue.intValue();
                outputVo.put(resultValue2);
            } else {
                outputVo.put(resultValue);
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
