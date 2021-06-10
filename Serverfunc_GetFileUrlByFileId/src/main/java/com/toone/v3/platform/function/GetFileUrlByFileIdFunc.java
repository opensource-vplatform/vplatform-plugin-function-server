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
 * 根据文件ID获取文件url<br>
 * <br>
 * 代码示例：GetFileUrlByFileId("9f51cb5f88a65347581a0e15e343fca9")，返回文件url。<br>
 * 参数1--文件id(字符串类型)；<br>
 * 返回值类型：字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:52
 */
public class GetFileUrlByFileIdFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.GetFileUrlByFileId.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(GetFileUrlByFileIdFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
            service.checkParamBlank(funcCode, param);

            String url = VDS.getIntance().getFileOperate().getUrlByFileId(param.toString(), false);
            outputVo.put(url);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
