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
 * 根据查询条件查询表数据，返回查询数结果。
 * 代码示例:GetTableData("name","entity1","true") 返回值为
 * 参数数量 参数1--字段名(字符串类型)
 * 参数2--数据库表名(字符串类型)
 * 参数3--查询条件(字符串类型)
 *
 * @Author xugang
 * @Date 2021/5/31 21:59
 */
public class GetTableDataFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GetTableDataRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GetTableDataFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 3);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            param3 = context.getInput(2);
            service.checkParamBlank(funcCode, param1, param2);

            String columnName = param1.toString();
            String tableName = param2.toString();
            String condition = param3 == null ? "" : param3.toString();

            String sql = "select " + columnName + " from " + tableName;
            if (!condition.trim().equals("")) {
                sql += " where " + condition;
            }
            IDataView dataView = VDS.getIntance().getDas().find(sql);

            StringBuilder retValue = new StringBuilder();
            if (dataView != null) {
                List<Map<String, Object>> returnValues = dataView.getDatas();
                if (returnValues != null && returnValues.size() > 0) {
                    for (Map<String, Object> returnValue : returnValues) {
                        Object columnValue = returnValue.get(columnName);
                        String returnFieldValue = "";
                        if (columnValue != null)
                            returnFieldValue = columnValue.toString();
                        if (retValue.toString().equals("")) {
                            retValue = new StringBuilder(returnFieldValue);
                        } else {
                            retValue.append(",").append(returnFieldValue);
                        }
                    }
                }
            }

            outputVo.put(retValue.toString());
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
