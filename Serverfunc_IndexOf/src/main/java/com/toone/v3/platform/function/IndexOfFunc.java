package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;

/**
 * 查找指定字符串的位置<br>
 * <br>
 * 代码示例:IndexOf("abcabbaab","bc",0) 返回值为1。<br>
 * 参数1--原字符串(字符串类型)；<br>
 * 参数2--指定字符串(字符串类型)；<br>
 * 参数3--起始检查位置(整数类型)，可忽略，忽略则从头开始检查；<br>
 * 返回值为整数类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/16 10:25
 */
public class IndexOfFunc implements IFunction {

    // 函数编码
    private final static String funcCode = IndexOfRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(IndexOfFunc.class);

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

            if (!(param1 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须为字符串类型，参数1：" + param1);
            }
            String str = "";
            if(param1 != null){
                str = (String) param1;
            }

            if(param2 == null){
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数不能为空，参数2：null");
            }
            if (!(param2 instanceof String)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须为字符串类型，参数2：" + param2);
            }
            String subStr = (String) param2;

            if (!(param3 instanceof Integer || param3 instanceof Double)) {
                throw new ServerFuncException("函数【" + funcCode + "】的第3个参数必须为整数类型，参数3：" + param3);
            }
            int index = 0;
            // 解析参数为整数类型的数值
            NumberFormat nf = NumberFormat.getInstance();
            try {
                index = Integer.parseInt(nf.format(param3).replace(",", ""));
            } catch (Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】的第3个参数必须为整数类型，参数3：" + param3);
            }

            int indexOf = str.indexOf(subStr, index);
            outputVo.put(indexOf);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3, e);
        }
        return outputVo;
    }
}
