package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取对象属性值<br>
 * <br>
 * 代码示例:GetProperty("Entity1","name")，返回值为该属性值。<br>
 * 参数1--对象(字符串类型)；<br>
 * 参数2--属性名(字符串类型)；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:38
 */
public class GetPropertyFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.GetProperty.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(GetPropertyFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object bean = null;
        String propName = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 2);

            bean = context.getInput(0);
            propName = (String) context.getInput(1);
            Object returnValue;

            try {
                returnValue = PropertyUtils.getProperty(bean, propName);
            } catch (Exception e) {
                throw new RuntimeException("函数【" + funcCode + "】计算失败，" + propName + "对象或属性不存在、请检查对象是否存在此对象或属性!");
            }

            outputVo.put(returnValue);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + bean + "，参数2：" + propName + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + bean + "，参数2：" + propName, e);
        }
        return outputVo;
    }
}
