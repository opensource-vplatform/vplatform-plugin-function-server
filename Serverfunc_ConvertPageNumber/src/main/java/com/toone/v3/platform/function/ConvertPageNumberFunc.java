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
 * 转换页码，将记录开始数转换成页码。<br>
 * <br>
 * 代码示例:ConvertPageNumber(3,4) 返回值为1。<br>
 * 参数1--记录开始数(整型)；<br>
 * 参数2--分页显示数(整型)；<br>
 * 返回值为整数类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 13:39
 */
public class ConvertPageNumberFunc implements IFunction {

    private final String funcCode = ServerFuncCommonUtils.ConvertPageNumber.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(ConvertPageNumberFunc.class);

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

            int recordstart;
            int pagesize;
            if (param1 instanceof Double) {
                recordstart = ((Double) param1).intValue();
            } else if (param1 instanceof Integer) {
                recordstart = (int) param1;
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数必须为整数类型，参数1：" + param1);
            }
            // 负数表示不分页，搞不懂什么逻辑，反正历史就这样
            if (recordstart == 0) {
                throw new ServerFuncException("函数【" + funcCode + "】的第1个参数不能为0，参数1：" + param1);
            }
            if (param2 instanceof Double) {
                pagesize = ((Double) param2).intValue();
            } else if (param2 instanceof Integer) {
                pagesize = (int) param2;
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须为整数类型，参数2：" + param2);
            }
            if (pagesize <= 0) {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数必须大于0，参数2：" + param2);
            }
            int pagenum = (recordstart - 1) / pagesize + 1;

            outputVo.put(pagenum);
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
}
