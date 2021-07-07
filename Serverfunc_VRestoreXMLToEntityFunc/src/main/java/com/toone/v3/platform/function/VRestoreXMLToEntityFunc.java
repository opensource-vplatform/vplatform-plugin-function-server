package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.jdbc.api.model.ColumnType;
import com.yindangu.v3.business.jdbc.api.model.IColumn;
import com.yindangu.v3.business.metadata.api.IDataObject;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将XML数据还原为实体数据<br>
 * <br>
 * 代码示例：VRestoreXMLToEntityFunc("xml内容","BR_IN_PARENT.a1:BR_IN_PARENT.a2,BR_IN_PARENT.b1:BR_OUT_PARENT.b2")，表示将XML还原后的方法输入a1实体还原到方法输入a2实体，方法输入b1实体还原到方法输出b2实体。<br>
 * 参数1：由VConvertEntityToXML/VConvertEntityToXMLFunc函数生成的XML信息（字符串类型）；<br>
 * 参数2：还原数据的来源与目标实体映射关系（字符串类型，选填，整个参数不填时则按原编码完全匹配），映射格式：源1:目标1,源2:目标2（即多个映射采用逗号分隔），方法输入实体（BR_IN_PARENT.entityCode）、方法变量实体（BR_VAR_PARENT.entityCode）、方法输出实体（BR_OUT_PARENT.entityCode）；<br>
 * 备注：目标实体结构（字段和类型）必须和源实体一致，才能还原成功。<br>
 * 无返回值。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 10:00
 */
public class VRestoreXMLToEntityFunc implements IFunction {

    // 函数编码
    private final static String funcCode = VRestoreXMLToEntityRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(VRestoreXMLToEntityFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            int size = context.getInputSize();
            if (size == 1) {
                param1 = context.getInput(0);
            } else if (size == 2) {
                param1 = context.getInput(0);
                param2 = context.getInput(1);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】需要1个或者2个参数，当前参数个数：" + size);
            }
            String actionXML;
            String mappings;
            Map<String, String> mapp = new HashMap<String, String>();
            if (size == 1) {
                actionXML = CheckStringNull(param1, "第1个参数");
            } else if (size == 2) {
                actionXML = CheckStringNull(param1, "第1个参数");
                mappings = CheckStringNull(param2, "第2个参数");
                String[] mapping = mappings.split(",");
                for (int i = 0; i < mapping.length; i++) {
                    String[] eve = mapping[i].split(":");
                    if (eve.length == 2) {
                        String key = eve[0];
                        String value = eve[1];
                        mapp.put(key, value);
                    } else {
                        throw new ServerFuncException("函数【" + funcCode + "】的第2个参数格式错误");
                    }
                }
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】参数错误");
            }

            //根据xml信息获取记录数据和字段类型信息
            Map<String, Map<String, List<Map<String, Object>>>> tableDataMap = parseActionXMLToTableDataMap(actionXML);

            for (String dataSourceName : tableDataMap.keySet()) {
                IDataView dataView = null;
                if (size == 1) {
                    dataView = VDS.getIntance().getFormulaEngine().eval(dataSourceName);
                } else if (size == 2) {
                    String mappingDataSourceName = mapp.get(dataSourceName);
                    dataView = VDS.getIntance().getFormulaEngine().eval(mappingDataSourceName);
                }

                Map<String, List<Map<String, Object>>> allDatas = tableDataMap.get(dataSourceName);
                List<Map<String, Object>> datas = allDatas.get("datas");
                Map<String, Object> types = ((List<Map<String, Object>>) allDatas.get("types")).get(0);
                if (dataView != null && datas != null && datas.size() > 0) {
                    List<String> noExistField = new ArrayList<String>();
                    for (int i = 0; i < datas.size(); i++) {
                        //单行记录数据
                        Map<String, Object> data = datas.get(i);
                        if (data != null && data.keySet().size() > 0) {
                            IDataObject dataObject = dataView.insertDataObject();
                            for (String map : data.keySet()) {
                                if (noExistField.contains(map)) {//如果该列不存在的话，直接不处理
                                    continue;
                                }
                                IColumn vColumn;
                                try {
                                    vColumn = dataView.getMetadata().getColumn(map);
                                    if (vColumn != null) {
                                        ColumnType nowType = vColumn.getColumnType();
                                        ColumnType targetType = ColumnType.getColumnTypeByOldChar(types.get(map).toString());
                                        if (nowType == targetType) {
                                            Object value = data.get(map);
                                            if (value == null || value.toString().equals("")) {
                                                dataObject.set(vColumn.getColumnName(), null);
                                            } else {
                                                dataObject.set(vColumn.getColumnName(), value);
                                            }
                                        } else {
                                            noExistField.add(map);//保存类型不一致的列
                                            log.warn("函数【" + funcCode + "】列类型不一致：当前类型:" + nowType + ", 目标类型：" + targetType);
                                        }
                                    } else {
                                        noExistField.add(map);//保存不存在
                                        log.warn("函数【" + funcCode + "】找不到该列[" + map + "]");
                                    }

                                } catch (SQLException e) {
                                    noExistField.add(map);//保存不存在的列
                                    log.warn("函数【" + funcCode + "】还原的实体里不存在该列：" + map);
                                }

                            }
                        }
                    }
                }
            }

            outputVo.put("");
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2, e);
        }
        return outputVo;
    }

    /**
     * 判断Object类型的变量是否为空。针对字符串判断的
     *
     * @param object   来源类型
     * @param descript 参数描述
     * @return
     */
    private String CheckStringNull(Object object, String descript) {
        String result = "";
        if (object != null && !object.toString().equals("")) {
            result = object.toString();
        } else {
            throw new ServerFuncException("函数【" + funcCode + "】" + descript + "不能为空");
        }
        return result;
    }

    /**
     * 解析actionXML,将actionData解析成tableData数据
     *
     * @param actionXML
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, Map<String, List<Map<String, Object>>>> parseActionXMLToTableDataMap(String actionXML) {
        Document doc = null;
        try {
            doc = parse(actionXML);
        } catch (Exception e) {
            throw new RuntimeException("[WorkFlowXMLToTableData.evaluate]actionXML数据有误，请验证:" + actionXML, e);
        }

        // TODO:目前因为需要配合WorkFlowTableDataToXML函数，所以获取数据按照actionData节点进行
        Element actionData = getActionData(doc);

        if (null != actionData) {
            Map<String, Map<String, List<Map<String, Object>>>> tableDatasMap = new HashMap<String, Map<String, List<Map<String, Object>>>>();
            // 获取actionData下的所有表名
            List<Element> tableDataEles = actionData.elements();
            if (null != tableDataEles && !tableDataEles.isEmpty()) {
                for (Element tableDataEle : tableDataEles) {
                    Map<String, List<Map<String, Object>>> tableSingleDatasMap = new HashMap<String, List<Map<String, Object>>>();
                    String tableName = tableDataEle.getName();
                    List<Map<String, Object>> tableDatas = new ArrayList<Map<String, Object>>();
                    // 获取表名下所有记录
                    List<Element> datas = getDatas(tableDataEle);
                    if (null != datas && !datas.isEmpty()) {
                        for (Element data : datas) {
                            Map<String, Object> tableData = new HashMap<String, Object>();
                            // 获取记录下所有字段信息
                            List<Element> fields = data.elements();
                            if (null != fields && !fields.isEmpty()) {
                                for (Element field : fields) {
                                    String fieldName = field.getName();
                                    String fieldValue = field.getText();
                                    tableData.put(fieldName, fieldValue);
                                }
                            }
                            tableDatas.add(tableData);
                        }
                    }
                    tableSingleDatasMap.put("datas", tableDatas);

                    List<Map<String, Object>> fieldTypes = new ArrayList<Map<String, Object>>();
                    // 获取表名下所有类型数据
                    List<Element> types = getTypes(tableDataEle);
                    Map<String, Object> typeData = new HashMap<String, Object>();
                    if (null != types && !types.isEmpty()) {
                        for (Element type : types) {
                            //保存字段类型信息
                            if (type.selectNodes("code") != null && type.selectNodes("code").size() > 0) {
                                Element code = (Element) type.selectNodes("code").get(0);
                                if (type.selectNodes("type") != null && type.selectNodes("type").size() > 0) {
                                    Element fieldType = (Element) type.selectNodes("type").get(0);
                                    typeData.put(code.getText(), fieldType.getText());
                                    continue;
                                }
                            }
                        }
                        fieldTypes.add(typeData);
                    }
                    tableSingleDatasMap.put("types", fieldTypes);
                    tableDatasMap.put(tableName, tableSingleDatasMap);
                }
            }
            return tableDatasMap;
        }
        return null;
    }

    private Document parse(String xmldocumentstr) throws DocumentException {
        SAXReader reader = new SAXReader();
        reader.setEncoding("utf-8");
        StringReader str_reader = new StringReader(xmldocumentstr);
        Document document = reader.read(str_reader);
        str_reader.close();
        return document;
    }

    /**
     * 获取actionData节点
     *
     * @param doc
     * @return
     */
    @SuppressWarnings("unchecked")
    private Element getActionData(Document doc) {
        if (null == doc) {
            return null;
        }
        Element actionData = doc.getRootElement();
        if (null == actionData || !"actionData".equals(actionData.getName())) {
            List<Element> nodes = doc.selectNodes("//actionData");
            if (null == nodes || nodes.isEmpty()) {
                return null;
            }
            // 如果可能存在多个actionData节点时，只取第一个
            actionData = nodes.get(0);
        }
        return actionData;
    }

    /**
     * 获取datas节点,获取表名下所有记录
     *
     * @param tableDataEle
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Element> getDatas(Element tableDataEle) {
        if (null == tableDataEle) {
            return null;
        }
        List<Element> datas = null;
        Element datasEle = tableDataEle.element("datas");
        if (null != datasEle) {
            datas = datasEle.elements("data");
            if (datas == null || datas.isEmpty()) {
                // 获取表名下所有记录
                datas = tableDataEle.selectNodes(".//data");
            }
        } else {
            // 获取表名下所有记录
            datas = tableDataEle.selectNodes(".//data");
        }
        return datas;
    }

    /**
     * 获取field节点数据
     *
     * @param tableDataEle
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<Element> getTypes(Element tableDataEle) {
        if (null == tableDataEle) {
            return null;
        }
        List<Element> types = null;
        Element metaEle = tableDataEle.element("meta");
        if (null != metaEle) {
            types = metaEle.elements("field");
            if (types == null || types.isEmpty()) {
                // 获取表名下所有类型数据
                types = tableDataEle.selectNodes(".//field");
            }
        } else {
            // 获取表名下所有类型数据
            types = tableDataEle.selectNodes(".//field");
        }
        return types;
    }
}
