package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 比较两个字符串是否一致,返回比较结果，相等则返回True。<br>
 * <br>
 * 代码示例:Compare("ab","Ab",true) 返回值为True。<br>
 * 参数1--比较串(字符串类型)； 参数2--被比较串(字符串类型)； 参数3--是否忽略大小写，true为忽略大小写(布尔类型)；<br>
 * 返回值为布尔类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 12:38
 */
public class CompareFunc implements IFunction {

    private final static Logger log = LoggerFactory.getLogger(CompareFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(ServerFuncCommonUtils.Compare.Function_Code(), context, 3);

            param1 = context.getInput(0);
            param2 = context.getInput(1);
            param3 = context.getInput(2);

            String str1;
            String str2;
            if (param1 == null) {
                str1 = "";
            } else if (!(param1 instanceof String)) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.Compare.Function_Code() + "】的第1个参数必须是字符串类型，参数1：" + param1);
            } else {
                str1 = (String) param1;
            }
            if (param2 == null) {
                str2 = "";
            } else if (!(param2 instanceof String)) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.Compare.Function_Code() + "】的第2个参数必须是字符串类型，参数2：" + param2);
            } else {
                str2 = (String) param2;
            }
            boolean ignoreCase;
            if (!(param3 instanceof Boolean)) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.Compare.Function_Code() + "】的第3个参数必须是布尔类型，参数3：" + param3);
            } else {
                ignoreCase = (boolean) param3;
            }

            if (ignoreCase) {
                outputVo.put(str1.equalsIgnoreCase(str2));
            } else {
                outputVo.put(str1.equals(str2));
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + ServerFuncCommonUtils.Compare.Function_Code() + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + ", " + e.getMessage());
            log.error("函数【" + ServerFuncCommonUtils.Compare.Function_Code() + "】计算失败，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3, e);
        }
        return outputVo;
    }
}
