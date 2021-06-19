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

import java.util.*;

/**
 * 树数据根据权重向根节点汇总<br>
 * <br>
 * 代码示例:TreeDataUpwardCollect("BR_IN_PARENT.workEntity","ca,cb,cc","weight","type:1,pidField:PID,treeCodeField:InnerCode,orderField:orderNo,isLeafField:isLeaf,busiFilterField:busCode")。<br>
 * 参数1--实体编码(字符串类型)，方法输入实体（BR_IN_PARENT.entityCode）、方法变量实体（BR_VAR_PARENT.entityCode）、方法输出实体（BR_OUT_PARENT.entityCode）；<br>
 * 参数2--统计字段编码集合(字符串类型)，如有多个统计字段用,分隔；<br>
 * 参数3--权重字段编码（字符串类型）；<br>
 * 参数4--树结构配置项（字符串类型），仅支持层级树结构，其中pidField为父节点字段，treeCodeField为层级码字段，orderField为排序字段，isLeafField为叶子节点字段，busiFilterField为树过滤字段；<br>
 * 无返回值，函数处理完成后，最终汇总结果保存在传入的实体中。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:57
 */
public class TreeDataUpwardCollectFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.TreeDataUpwardCollect.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(TreeDataUpwardCollectFunc.class);

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
            service.checkParamBlank(funcCode, param1, param2, param3, param4);

            String dataSourceName = (String) param1;
            String priceFieldStr = (String) param2;
            String weightFieldStr = (String) param3;
            String[] priceFieldArr = priceFieldStr.split(",");
            String[] weightFieldArr = weightFieldStr.split(",");

            int priceFieldArrLength = priceFieldArr.length;
            boolean isSameLength = (priceFieldArr.length == weightFieldArr.length);
            if (priceFieldArr.length > 1 && weightFieldArr.length > 1 && !isSameLength) {
                throw new ServerFuncException("函数【" + funcCode + "】执行失败，统计字段与权重字段数量不匹配");
            }

            String treeStruct = (String) param4;
            String[] treeStructArr = treeStruct.split(",");
            Map<String, String> treeMap = new HashMap<String, String>(treeStructArr.length);
            for (String treeField : treeStructArr) {
                String[] treeFieldArr = treeField.split(":");
                treeMap.put(treeFieldArr[0], treeFieldArr[1]);
            }

            String pidField = treeMap.containsKey("pidField") ? (String) treeMap.get("pidField") : "";
            String innerCodeField = treeMap.containsKey("treeCodeField") ? (String) treeMap.get("treeCodeField") : "";
            String orderField = treeMap.containsKey("orderField") ? (String) treeMap.get("orderField") : "";
            String isLeafField = treeMap.containsKey("isLeafField") ? (String) treeMap.get("isLeafField") : "";
            String groupField = treeMap.containsKey("busiFilterField") ? (String) treeMap.get("busiFilterField") : "";

            if (innerCodeField == null || innerCodeField.equals("")) {
                throw new ServerFuncException("函数【" + funcCode + "】执行失败，树结构编码【treeCodeField】为空，将检查");
            }

            if (isLeafField == null || isLeafField.equals("")) {
                throw new ServerFuncException("函数【" + funcCode + "】函数执行失败，树结构叶子节点标识【isLeafField】为空，将检查");
            }
            boolean isGroupFieldEmpty = false;
            if (groupField == null || groupField.equals("")) {
                isGroupFieldEmpty = true;
            }

            IDataView dataView = null;
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

            int innerCodeRule = 5;
            int maxLevel = 1;
            List<Map<String, Object>> datas = dataView.getDatas();
            List<Map<String, Object>> rootDataList = new ArrayList<Map<String, Object>>();
            Map<Integer, Set<String>> level_ids = new HashMap<Integer, Set<String>>();
            Map<String, String> innerCodeGroup_id = new HashMap<String, String>(datas.size());// innerCode + "#" + group
            Map<String, String> id_innerCodeGroup = new HashMap<String, String>(datas.size());// innerCode + "#" + group,
            Map<String, Set<String>> parent_InnerCodeGroup_id = new HashMap<String, Set<String>>();// innerCode + "#" + group

            Set<String> existedIds = new HashSet<String>();
            Map<String, Map<String, Object>> id_dataMap = new HashMap<String, Map<String, Object>>(datas.size());

            List<String> groupValList = new ArrayList<String>();
            for (Map<String, Object> data : datas) {
                String id = (String) data.get("id");
                String group = isGroupFieldEmpty ? "" : (String) data.get(groupField);
                String innerCode = (String) data.get(innerCodeField);
                innerCodeGroup_id.put(innerCode + "#" + group, id);
                id_innerCodeGroup.put(id, innerCode + "#" + group);

                int innerCodeLength = innerCode.length();
                int level = innerCodeLength / innerCodeRule;
                if (level_ids.containsKey(level)) {
                    Set<String> levelids = level_ids.get(level);
                    levelids.add(id);
                } else {
                    Set<String> levelids = new HashSet<String>();
                    levelids.add(id);
                    level_ids.put(level, levelids);
                }

                if (level > maxLevel) {
                    maxLevel = level;
                }

                if (level > 1) {
                    String parent_InnerCode = innerCode.substring(0, (innerCodeLength - innerCodeRule));
                    String parent_InnerCodeGroup = parent_InnerCode + "#" + group;
                    if (parent_InnerCodeGroup_id.containsKey(parent_InnerCodeGroup)) {
                        Set<String> subIds = parent_InnerCodeGroup_id.get(parent_InnerCodeGroup);
                        subIds.add(id);
                    } else {
                        Set<String> subIds = new HashSet<String>();
                        subIds.add(id);
                        parent_InnerCodeGroup_id.put(parent_InnerCodeGroup, subIds);
                    }
                } else if (level == 1) {
                    rootDataList.add(data);
                }

                id_dataMap.put(id, data);

                // 对叶子的处理
                boolean isLeaf = toBooleanObj(data.get(isLeafField));
                if (isLeaf) {
                    existedIds.add(id);
                }

                // 分组字段收集
                if (group != null && !group.equals("") && !groupValList.contains(group)) {
                    groupValList.add(group);
                }
            }

            for (int level = (maxLevel - 1); level > 0; level--) {
                Set<String> ids = level_ids.get(level);
                for (String id : ids) {
                    if (existedIds.contains(id)) {
                        continue;
                    }
                    String innerCodeGroup = id_innerCodeGroup.get(id);
                    Map<String, Object> data = id_dataMap.get(id);
                    Set<String> subIds = parent_InnerCodeGroup_id.get(innerCodeGroup);
                    for (int index = 0; index < priceFieldArrLength; index++) {
                        String priceField = priceFieldArr[index];
                        String weigthField = null;
                        if (isSameLength) {
                            weigthField = weightFieldArr[index];
                        } else {
                            weigthField = weightFieldArr[0];
                        }
                        double price = 0;
                        for (String subId : subIds) {
                            if (existedIds.contains(subId)) {
                                Map<String, Object> subData = id_dataMap.get(subId);
                                double subPrice = 0;
                                double subWeight = 0;
                                if (subData.get(weigthField) == null || subData.get(weigthField).toString().equals("")) {
                                    throw new ServerFuncException("函数【" + funcCode + "】执行失败，数据id为【" + subId + "】的数据,权重字段【" + weigthField + "】未设置，请检查");
                                }

                                try {
                                    subPrice = subData.get(priceField) == null ? 0 : toDouble(subData.get(priceField).toString());
                                    subWeight = toDouble(subData.get(weigthField).toString());
                                } catch (Exception e) {
                                    throw new ServerFuncException("函数【" + funcCode + "】执行失败，数据id为【" + subId + "】的数据,价格字段【" + priceField + "】或权重字段【" + weigthField + "】获取数值失败，请检查");
                                }

                                double subTotal = subPrice * subWeight;
                                price = price + subTotal;
                            }
                        }
                        data.put(priceField, price);
                    }
                    existedIds.add(id);
                }
            }
            dataView.clear();
            dataView.insertDataObject(rootDataList);

            outputVo.put("");//该函数本没有返回值
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + "，参数4：" + param4 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + "，参数4：" + param4, e);
        }
        return outputVo;
    }

    private Boolean toBooleanObj(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (obj instanceof Number) {// 所有的数字型全部这样判断
            return ((Number) obj).intValue() > 0 ? true : false;
        } else if (obj instanceof String) {
            return Boolean.parseBoolean(obj.toString());
        } else {
            throw new RuntimeException("转换Boolean类型错误！目前只支持Boolean,Number,String类型");
        }
    }

    private double toDouble(String ls) {
        if (ls == null || ls.equals("")) {
            return 0;
        }
        double ld = 0;
        try {
            ld = Double.parseDouble(ls);
        } catch (NumberFormatException e) {
            ld = 0;
        }
        return ld;
    }
}
