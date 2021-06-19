package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.metadata.api.IDataObject;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.platform.plugin.util.VdsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 二维数组json还原为实体<br>
 * <br>
 * 代码示例：VRestoreJsonToEntityFunc("json内容","BR_IN_PARENT.entityCode")<br>
 * 参数1: 由VConvertEntityToJson/VConvertEntityToJsonFunc函数生成的二维数组json信息。（字符串类型）<br>
 * 参数2: 还原数据的目标实体编码。（字符串类型）<br>
 * 无返回值。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:59
 */
public class VRestoreJsonToEntityFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.VRestoreJsonToEntityFunc.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(VRestoreJsonToEntityFunc.class);

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
            //获取参数
            Object jsonStr = CheckStringNull(param1, 0);
            Object entityCode = CheckStringNull(param2, 1);

            //参数类型转换
            String sourceJson = jsonStr.toString();
            IDataView dataView = VDS.getIntance().getFormulaEngine().eval(entityCode.toString());

            //json字符串转json对象
            List sourceList;
            try {
                sourceList = (List) VdsUtils.json.fromJsonList(sourceJson);
            } catch (Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数的值格式不是json格式，转换成json时失败，传入值=" + sourceJson);
            }

            if (sourceList != null) {
                for (int i = 0; i < sourceList.size(); i++) {
                    //获取每行记录
                    Map<String, Object> item = (Map<String, Object>) sourceList.get(i);
                    //插入一条数据，作为当前记录
                    IDataObject dataObject = dataView.insertDataObject();
                    //设置当前记录的字段值
                    for (String fieldCode : item.keySet()) {
                        Object fieldValue = item.get(fieldCode);
                        if (fieldValue instanceof Map || fieldValue instanceof List) {//防止某个值是一个json的格式
                            fieldValue = VdsUtils.json.toJson(fieldValue);
                        }
                        dataObject.set(fieldCode, fieldValue);
                    }
                }
            }

            outputVo.put(""); //改函数本没有返回值
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }

    /**
     * 判断Object类型的变量是否为空。针对字符串判断的
     *
     * @param object 来源类型
     * @param index  参数顺序
     * @return
     */
    public String CheckStringNull(Object object, int index) {
        String result = "";
        if (object != null && object.toString().equals("")) {
            result = object.toString();
        } else {
            throw new ServerFuncException("函数【" + funcCode + "】的第" + (index + 1) + "个参数不能为空");
        }
        return result;
    }
}
