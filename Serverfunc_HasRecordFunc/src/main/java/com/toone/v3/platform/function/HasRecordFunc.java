package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.metadata.api.IDAS;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 检查指定的表是否有数据<br>
 * <br>
 * 代码示例:HasRecord("Entity1") 返回值为 3。<br>
 * 参数1--数据库表名(字符串类型);<br>
 * 返回值为整数类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/16 10:25
 */
public class HasRecordFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.HasRecord.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(HasRecordFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
            service.checkParamBlank(funcCode, param);

            try {
                String sql = String.format("select count(*) as dataCount from %s", param.toString());
                IDAS das = VDS.getIntance().getDas();
                IDataView dv = das.find(sql);
                Number dataCount = (Number) dv.getDatas().get(0).get("dataCount");
                outputVo.put(dataCount.intValue() + "");
            } catch (Exception e) {
                log.error("函数【" + funcCode + "】计算失败，参数1：" + param, e);
                outputVo.put("-1");
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param, e);
        }
        return outputVo;
    }
}
