package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.platform.plugin.util.VdsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 将变量实体转换为二维数组json<br>
 * <br>
 * 代码示例:VConvertEntityToJsonFunc("BR_IN_PARENT.entityCode","fieldMapping1,fieldMapping2") 返回值为二维数组形式的json<br>
 * 参数1：要转换json的实体编码；（字符串类型）<br>
 * 参数2：字段编码，指实体中某些字段的值是json格式，需要特殊处理的，多个字段用逗号隔开（此参数可选）。【注】参数2不要理解成需要转成json的字段。<br>
 * 返回值类型：字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:59
 */
public class VConvertEntityToJsonFunc implements IFunction {

    // 函数编码
    private final static String funcCode = VConvertEntityToJsonRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(VConvertEntityToJsonFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        try {
            int size = context.getInputSize();
            if (size == 1) {
                param1 = context.getInput(0);
            } else if (size == 2) {
                param1 = context.getInput(0);
                param2 = context.getInput(1);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】需要1个或者2个参数，当前参数个数：" + size);
            }
            String funcResult = "";//保存函数结果

            //获取参数
            String entityCode = CheckStringNull(param1, 1);
            IDataView dataView = VDS.getIntance().getFormulaEngine().eval(entityCode);
            ;
            //获取实体数据
            List dataList = dataView.getDatas();
            //如果存在第二个参数
            if (size > 1 && dataList.size() > 0) {
                List result = new ArrayList();
                //判断第二个参数是否为空
                String jsonFieldStr = CheckStringNull(param2, 2);
                String[] fieldArray = jsonFieldStr.split(",");//分隔成数组
                List fileList = ConvertArrayToList(fieldArray);//转换成list
                //遍历取数据
                for (int i = 0; i < dataList.size(); i++) {
                    Object fieldValue = "";
                    Map<String, Object> convertMap = new LinkedHashMap<String, Object>();
                    Map<String, Object> item = (Map<String, Object>) dataList.get(i);
                    for (String string : item.keySet()) {
                        fieldValue = item.get(string);
                        if (fileList.contains(string)) {
                            fieldValue = VdsUtils.json.fromJsonList(item.get(string).toString());
                        }
                        convertMap.put(string, fieldValue);
                    }
                    result.add(convertMap);
                }
                funcResult = VdsUtils.json.toJson(result);//转json
            } else {
                funcResult = VdsUtils.json.toJson(dataList);//转json
            }

            outputVo.put(funcResult);
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
     * @param object 来源类型
     * @param index  参数排序
     * @return
     */
    private String CheckStringNull(Object object, int index) {
        String result = "";
        if (object != null && !object.toString().equals("")) {
            result = object.toString();
        } else {
            throw new ServerFuncException("函数【" + funcCode + "】的第" + index + "个参数不能为空");
        }
        return result;
    }

    /**
     * 将Array转成List
     *
     * @param sourceArray
     * @return
     */
    private List<String> ConvertArrayToList(String[] sourceArray) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < sourceArray.length; i++) {
            result.add(sourceArray[i]);
        }
        return result;
    }
}
