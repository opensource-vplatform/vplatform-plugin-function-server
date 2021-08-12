package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.metadata.api.IDataObject;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 获取实体变量首行记录字段值<br>
 * <br>
 * 代码示例:
 * 界面实体：GetFirstRowColumnValue("entity","id")，返回值为该实体变量首行记录的id字段值。<br>
 * 活动集实体：GetFirstRowColumnValue("BR_VAR_PARENT.interView","id")<br>
 * 参数1--实体名称。界面实体为字符串类型，其他变量实体需要加上变量的前缀<br>
 * 参数2--字段名(字符串类型)； <br>
 * 返回值类型：与参数二所选字段的类型一致。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:53
 */
public class GetFirstRowColumnValueFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GetFirstRowColumnValueRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GetFirstRowColumnValueFunc.class);

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

            if(param2.toString().trim().equals("")) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数不能为空");
            }

            IDataView dataView = null;
            if(param1 instanceof String) {
                Object runtimeParams = VDS.getIntance().getFormulaEngine().eval(param1.toString());
                if(runtimeParams == null) {
                    throw new ServerFuncException("函数【" + funcCode + "】获取实体变量失败，找不到对应的变量信息，参数1：" + param1);
                } else if(!(runtimeParams instanceof  IDataView)) {
                    throw new ServerFuncException("函数【" + funcCode + "】获取实体变量失败，该变量并非实体类型，参数1：" + param1);
                } else {
                    dataView = (IDataView) runtimeParams;
                }
            } else if(param1 instanceof IDataView) {
                dataView = (IDataView) param1;
            }

            if (null != dataView) {
                List<IDataObject> records = dataView.select();
                if (null != records && records.size() > 0) {
                    IDataObject record = dataView.select().get(0);
                    Object o = record.get(param2.toString());
                    outputVo.put(o);
                }
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
