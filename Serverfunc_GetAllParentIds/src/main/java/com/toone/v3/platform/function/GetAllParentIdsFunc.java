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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 根据树节点id查出它所有的父节点集合组装成约定的字符串<br>
 * <br>
 * 代码示例: GetAllParentIds("a","BR_IN_PARENT.entityCode",True,";")，id为a，父节点有b、c，分隔符为;，若包含自身节点，则返回b;c;a，若不包含自身节点，则返回b;c，返回节点顺序为从高到低。
 * 参数1--节点id值（字符串类型）；
 * 参数2--实体编码(字符串类型)，实体可以是方法输入(BR_IN_PARENT.entityCode)、方法输出(BR_OUT_PARENT.entityCode)、方法变量(BR_VAR_PARENT.entityCode) ；
 * 参数3--是否包含自身节点（布尔类型）；
 * 参数4--分隔符（字符串类型）；
 * 返回值类型：字符串类型。
 *
 * @Author xugang
 * @Date 2021/5/31 21:44
 */
public class GetAllParentIdsFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.GetAllParentIds.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(GetAllParentIdsFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        Object param4 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 4);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            param3 = context.getInput(2);
            param4 = context.getInput(3);
            service.checkParamBlank(funcCode, param1, param2);

            String id = param1.toString();
            Object runtimeParams = VDS.getIntance().getFormulaEngine().eval(param2.toString());
            if (runtimeParams instanceof IDataView) {
                IDataView dataView = (IDataView) runtimeParams;
                List<Map<String, Object>> datas = dataView.getDatas();
                String separator = param4 == null ? " " : param4.toString();

                List<String> ids = new ArrayList<>();
                ids.add(id);
                String str = findParentIds(datas, ids, separator);
                if(param3 == null || !"true".equalsIgnoreCase(param3.toString())) {
                    if(str.length()>0){
                        str=str+id;
                    }else{
                        str=id;
                    }
                } else {
                    if(str.length()>0){
                        str=str.substring(0, str.length()-separator.length());
                    }else{
                        str="";
                    }
                }
                outputVo.put(str);
            } else {
                outputVo.put("");
            }
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + "，参数4：" + param4 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }

    private String findParentIds(List<Map<String, Object>> datas, List<String> ids, String separator) {
        StringBuilder buf = new StringBuilder(1024);
        List<String> idList = new ArrayList<>();
        for (String id : ids) {
            for (Map<String, Object> data : datas) {
                if (data.get("id").equals(id)) {
                    Object opid = data.get("PID");
                    if (opid != null) {
                        String pid = opid.toString();
                        if (!pid.equals("")) {
                            idList.add(pid);
                            buf.append(pid).append(separator);
                        }
                    }
                }
            }
        }
        if (idList.size() > 0) {
            return findParentIds(datas, idList, separator);
        } else {
            return buf.toString();
        }
    }
}
