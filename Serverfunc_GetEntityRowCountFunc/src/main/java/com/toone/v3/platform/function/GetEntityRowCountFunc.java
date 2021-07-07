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

import java.util.HashMap;

/**
 * 根据条件从实体获取记录数<br>
 * <br>
 * 代码示例:GetEntityRowCountFunc("BR_IN_PARENT.entityCode","wb='A'")，返回方法输入实体[entityCode]中字段wb的值是A的记录数。<br>
 * 参数1：实体编码(字符串类型)。实体可以是方法输入(BR_IN_PARENT.entityCode)、方法输出(BR_OUT_PARENT.entityCode)、方法变量(BR_VAR_PARENT.entityCode)；<br>
 * 参数2：筛选条件(字符串类型)，其运算结果应该是布尔值。如果该参数省略，则返回实体总记录数；<br>
 * 若参数2中的条件值来源变量，则参数2需要用一个变量代替，如：<br>
 * GetEntityRowCountFunc("BR_VAR_PARENT.TableName",BR_VAR_PARENT.cs2)<br>
 * 其中BR_VAR_PARENT.cs2的值=ConcatStr("name=",BR_VAR_PARENT.cs2_z)<br>
 * 若BR_VAR_PARENT.cs2_z为字符串，拼接参数2时，还需要加上单引号，如：<br>
 * ConcatStr("name=","'",BR_VAR_PARENT.cs2_z,"'")<br>
 * 返回值类型：整型。 <br>
 * 注：筛选条件中的字符串需要用单引号引起来。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:51
 */
public class GetEntityRowCountFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GetEntityRowCountRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GetEntityRowCountFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            int size = context.getInputSize();
            if(size != 1 && size != 2) {
                throw new ServerFuncException("函数【" + funcCode + "】只能有1个或者2个参数，当前参数个数：" + size);
            } else {
                param1 = context.getInput(0);
                if(size == 2) {
                    param2 = context.getInput(1);
                }
                service.checkParamBlank(funcCode, param1);
                String dataViewName = param1.toString();
                String condition = param2 == null ? "" : param2.toString();
                Object runtimeParams = VDS.getIntance().getFormulaEngine().eval(dataViewName);
                if(runtimeParams instanceof IDataView) {
                    IDataView dataView = (IDataView) runtimeParams;
                    size = dataView.select(condition, new HashMap<String, Object>()).size();
                    outputVo.put(size);
                    outputVo.setSuccess(true);
                } else {
                    throw new ServerFuncException("函数【" + funcCode + "】获取实体发生错误，获取不到对应的实体变量，请注意需要加上变量范围前缀，参数1：" + param1);
                }
            }
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
