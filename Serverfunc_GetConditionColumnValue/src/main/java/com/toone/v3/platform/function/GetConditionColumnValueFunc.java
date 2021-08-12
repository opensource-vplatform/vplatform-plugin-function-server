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

import java.util.HashMap;
import java.util.List;

/**
 * 按照条件获取对应的实体的字段信息<br>
 * <br>
 * 代码示例1：GetConditionColumnValue("BR_IN_PARENT.entityCode","name","age=1")。<br>
 * 代码示例2：GetConditionColumnValue("BR_VAR_PARENT.entityCode","name","wb='文本'")。<br>
 * 参数1--方法实体编码(字符串类型)，方法输入实体（BR_IN_PARENT.entityCode）、方法变量实体（BR_VAR_PARENT.entityCode）、方法输出实体（BR_OUT_PARENT.entityCode）；<br>
 * 参数2--方法实体中某个字段的编码(字符串类型)；<br>
 * 参数3--查询条件(字符串类型，如果值是字符串类型，则需要加单引号，参考示例2)；<br>
 * 若参数3中的条件值来源变量，则参数3需要用一个变量代替，如：<br>
 * GetConditionColumnValue("BR_VAR_PARENT.TableName","name",BR_VAR_PARENT.cs3)<br>
 * 其中BR_VAR_PARENT.cs3的值=ConcatStr("name=",BR_VAR_PARENT.cs3_z)<br>
 * 若BR_VAR_PARENT.cs3_z为字符串，拼接参数3时，还需要加上单引号，如：<br>
 * ConcatStr("name=","'",BR_VAR_PARENT.cs3_z,"'")<br>
 * 返回值类型：与参数二所选字段的类型一致。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:45
 */
public class GetConditionColumnValueFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GetConditionColumnValueRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GetConditionColumnValueFunc.class);

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
            service.checkParamNull(funcCode, param1, param2, param3);

            String dataSourceName = param1.toString();
            String columnName = param2.toString();
            String condition = param3.toString();

            IDataView dataView;
            if (dataSourceName.contains("BR_IN_PARENT.") || dataSourceName.contains("BR_VAR_PARENT.") || dataSourceName.contains("BR_OUT_PARENT.")) {
                dataView = VDS.getIntance().getFormulaEngine().eval(dataSourceName);
            } else {
                dataView = VDS.getIntance().getFormulaEngine().eval("BR_IN_PARENT." + dataSourceName);
                if (null == dataView) {
                    dataView = VDS.getIntance().getFormulaEngine().eval("BR_VAR_PARENT." + dataSourceName);
                }
                if (null == dataView) {
                    dataView = VDS.getIntance().getFormulaEngine().eval("BR_OUT_PARENT." + dataSourceName);
                }
            }

            if (null == dataView) {
                throw new ServerFuncException("函数【" + funcCode + "】执行失败，找不到对应的活动集变量:" + dataSourceName);
            }

            if (condition.trim().equals("")) {
                condition = "1=1";
            }

            List<IDataObject> datas = dataView.select(condition, new HashMap<String, Object>());

            if (datas == null || datas.size() == 0) {
                outputVo.put(null);
            } else {
                IDataObject data = datas.get(0);
                outputVo.put(data.get(columnName));
            }
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
