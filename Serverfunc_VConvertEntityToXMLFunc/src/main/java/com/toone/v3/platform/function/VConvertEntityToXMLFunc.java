package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.jdbc.api.model.IColumn;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 将变量实体转换为XML字符串<br>
 * <br>
 * 代码示例:VConvertEntityToXMLFunc("BR_IN_PARENT.a","BR_OUT_PARENT.b") 返回拼装后的XML内容。<br>
 * 参数1-N: 实体编码。（字符串类型）<br>
 * 返回值类型：字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:59
 */
public class VConvertEntityToXMLFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.VConvertEntityToXMLFunc.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(VConvertEntityToXMLFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        try {
            int size = context.getInputSize();
            if (size < 1) {
                throw new ServerFuncException("函数【】至少需要1个参数，当前参数个数：" + size);
            }

            //循环获取参数
            Document doc = DocumentHelper.createDocument();
            // TODO:目前暂时使用actionData作为根节点，以便配合WorkFlowXMLToTableData函数进行还原
            Element root = doc.addElement("actionData");
            for (int i = 0; i < size; i++) {
                Object param = context.getInput(i);
                if (param == null || param.toString().equals("")) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第" + (i + 1) + "个参数不能为空");
                }
                parseTableDatasMap2ActionXml(param.toString(), root);
            }
            String funcResult = doc.asXML();

            outputVo.put(funcResult);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }

    /** H2状态字段 */
    private static String	H2_STATE_FIELD	= "H_2_S_T_A_T_E";
    /** H2保存旧id的字段 */
    private static String	H2_ORGID_FIELD	= "H_2_O_R_G_I_D";
    /** H2保存来源SQL的字段,来源SQL的hashcode值 */
    private static String	H2_SOURCE_FIELD	= "H_2_S_O_U_R_C_E";
    /** H2保存来源pk的字段,来源pk的hashcode值 */
    private static String	H2_PK_FIELD		= "H_2_P_K";
    /** 租户字段 */
    private static String H2_TenantID_Field="V3_TenantID";
    /**
     * 表信息转换成xml
     *
     * @param datasourceName 数据源名称
     * @param root           根节点
     */
    public void parseTableDatasMap2ActionXml(String datasourceName, Element root) throws SQLException {
        IDataView dataView = VDS.getIntance().getFormulaEngine().eval(datasourceName);
        if (dataView != null) {
            Element tableEle = root.addElement(datasourceName);

            // 抽取元数据到XML中
            Set<String> columnNames = dataView.getMetadata().getColumnNames();
            List<IColumn> fieldSet = new ArrayList<>();
            for (String name : columnNames) {
                if(H2_STATE_FIELD.equalsIgnoreCase(name) || H2_ORGID_FIELD.equalsIgnoreCase(name) || H2_SOURCE_FIELD.equalsIgnoreCase(name) || H2_PK_FIELD.equalsIgnoreCase(name) || H2_TenantID_Field.equalsIgnoreCase(name)) {
                    continue;
                }
                fieldSet.add(dataView.getMetadata().getColumn(name));
            }
//            ArrayList<VColumn> fieldSet = ((IH2MetaData)dataView.getMeta()).getNoHiddenColumns();
            addMetaFieldData(tableEle, fieldSet);

            // 抽取数据到XML中
            List<Map<String, Object>> datas = dataView.getDatas();
            addTableData(tableEle, datas);
        } else {
            log.error("函数【" + funcCode + "】" + datasourceName + "]对应的实体为空");
        }
    }

    /**
     * 添加字段数据信息
     *
     * @param tableEle
     * @param fieldSet
     */
    private static void addMetaFieldData(Element tableEle, List<IColumn> fieldSet) {
        if (null == fieldSet || fieldSet.isEmpty()) {
            return;
        }
        Element metaEle = tableEle.addElement("meta");
        for (IColumn vColumn : fieldSet) {
            String fieldCode = vColumn.getColumnName();
            String fieldName = vColumn.getColumnName();
            String type = vColumn.getColumnType().toString();
            Element fieldEle = metaEle.addElement("field");
            fieldEle.addElement("code").addText(fieldCode);
            fieldEle.addElement("name").addText(fieldName);
            fieldEle.addElement("type").addText(type);
        }
    }

    /**
     * 添加记录数据信息
     *
     * @param tableEle
     * @param tableDatas
     */
    private static void addTableData(Element tableEle, List<Map<String, Object>> tableDatas) {
        if (null == tableDatas || tableDatas.isEmpty()) {
            return;
        }
        Element datasEle = tableEle.addElement("datas");
        for (Map<String, Object> tableData : tableDatas) {
            Element dataEle = datasEle.addElement("data");
            for (Map.Entry<String, Object> entry : tableData.entrySet()) {
                String fieldName = entry.getKey();
                Object valueObj = entry.getValue();
                String value = "";
                if (null != valueObj) {
                    value = valueObj.toString();
                }
                // 对内存可能含有CDATA的数据进行修改
                value = value.replaceAll("]]>", "]]]]>><![CDATA[");
                dataEle.addElement(fieldName).addCDATA(value);
            }
        }
    }

}
