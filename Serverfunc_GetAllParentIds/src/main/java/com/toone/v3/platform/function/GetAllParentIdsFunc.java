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
    private final static String funcCode = GetAllParentIdsRegister.Plugin_Code;
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

            // 判断是否DataView对象
            if (runtimeParams instanceof IDataView) {
                StringBuilder sb = new StringBuilder(1024);
                IDataView dataView = (IDataView) runtimeParams;
                List<Map<String, Object>> listMap = dataView.getDatas();
                boolean self = true;
                if (param3 instanceof String) {
                    self = Boolean.valueOf(param3.toString());
                } else {
                    self = (Boolean) param3;
                }
                String separator = (String) param4;
                List<String> list = new ArrayList<String>();
                list.add(id);
                String str = findParentIds(listMap, list, separator, sb);
                if (self) {
                    if (str.length() > 0) {
                        str = str + id;
                    } else {
                        str = id;
                    }
                } else {
                    if (str.length() > 0) {
                        str = str.substring(0, str.length() - separator.length());
                    } else {
                        str = "";
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

    public static String findParentIds(List<Map<String, Object>> listMap, List<String> ids, String separator, StringBuilder sb) {
        List<String> idList = new ArrayList<String>();
        for (int i = 0; i < ids.size(); i++) {
            for (int j = 0; j < listMap.size(); j++) {
                if (listMap.get(j).get("id").equals(ids.get(i))) {
                    Object opid = listMap.get(j).get("PID");
                    if (opid != null) {
                        String pid = opid.toString();
                        if (!pid.equals("")) {
                            idList.add(pid);
                            sb.insert(0, pid + separator);
//                            sb = pid + separator + sb;
                        }
                    }
                }
            }
        }
        if (idList.size() > 0) {
            return findParentIds(listMap, idList, separator, sb);
        } else {
            return sb.toString();
        }
    }
}
