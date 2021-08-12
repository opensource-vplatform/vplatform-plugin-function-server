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

import java.util.Arrays;
import java.util.List;

/**
 * 将字符串的值添加到实体中指定的字段。<br>
 * <br>
 * 代码示例: ConvertVarToEntityColumn("今天;明天;后天","BR_IN_PARENT.entityCode","fieldCode",";")。<br>
 * 参数1：来源字符串（字符串类型，必填）；<br>
 * 参数2：实体编码（字符串类型，必填，必须带前缀，实体可以是方法输入(BR_IN_PARENT.entityCode)、方法输出(BR_OUT_PARENT.entityCode)、方法变量(BR_VAR_PARENT.entityCode)）；<br>
 * 参数3：字段编码（字符串类型，必填）；<br>
 * 参数4：参数1中多个数据之间的分隔符（字符串类型，必填）；<br>
 * 返回值类型：无返回值。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 13:39
 */
public class ConvertVarToEntityColumnFunc implements IFunction {

    private final static String funcCode = ConvertVarToEntityColumnRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(ConvertVarToEntityColumnFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object srcStr = null;
        Object entityCode = null;
        Object fieldCode = null;
        Object spiltStr = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 4);
            srcStr = context.getInput(0);
            entityCode = context.getInput(1);
            fieldCode = context.getInput(2);
            spiltStr = context.getInput(3);
            service.checkParamNull(funcCode, srcStr, entityCode, fieldCode, spiltStr);

            IDataView dataView = VDS.getIntance().getFormulaEngine().eval(entityCode.toString());

            if (srcStr.toString().equals("")) {
                throw new ServerFuncException("函数【" + funcCode + "】执行不正确，参数1不能为空，参数1：" + srcStr);
            }
            if (entityCode.toString().equals("")) {
                throw new ServerFuncException("函数【" + funcCode + "】执行不正确，参数2不能为空，参数2：" + entityCode);
            }
            if (fieldCode.toString().equals("")) {
                throw new ServerFuncException("函数【" + funcCode + "】执行不正确，参数3不能为空，参数3：" + fieldCode);
            }
            if (dataView == null) {
                throw new ServerFuncException("函数【" + funcCode + "】执行不正确，对应实体不存在，实体编码为参数2：" + entityCode);
            }
            spiltStr = replace(spiltStr.toString());
            String[] srcResult = srcStr.toString().split(spiltStr.toString());
            for (String src : srcResult) {
                //插入一条数据，作为当前记录
                IDataObject dataObject = dataView.insertDataObject();
                dataObject.set(fieldCode.toString(), src);
            }
            outputVo.setSuccess(true);
            outputVo.put("");
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + srcStr + "，参数2：" + entityCode + "，参数3：" + fieldCode + "，参数4：" + spiltStr + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + srcStr + "，参数2：" + entityCode + "，参数3：" + fieldCode + "，参数4：" + spiltStr, e);
        }
        return outputVo;
    }

    private String replace(String splitStr) {
        List<String> tmp = Arrays.asList(".", "{", "(", ")", "\\", "|", "*", "+", "^");
        char[] c = splitStr.toCharArray();
        StringBuilder buff = new StringBuilder(1024);
        for (char value : c) {
            String _c = value + "";
            if (tmp.contains(_c)) {
                buff.append("\\").append(_c);
            } else {
                buff.append(_c);
            }
        }
        return buff.toString();
    }
}
