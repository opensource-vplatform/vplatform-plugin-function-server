package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.file.api.model.IAppFileInfo;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据文件标识id复制文件;<br>
 * <br>
 * 代码示例：CopyFileByFileId("fileId")，返回复制后的文件id。<br>
 * 参数1--文件标识id(字符串类型)；<br>
 * 返回值为字符串类型。<br>
 * 注：如果文件标识id不存在，就返回"-1"。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 13:40
 */
public class CopyFileByFileIdFunc implements IFunction {

    private final String funcCode = ServerFuncCommonUtils.CopyFileByFileId.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(CopyFileByFileIdFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 1);

            param = context.getInput(0);

            service.checkParamBlank(funcCode, param);

            IAppFileInfo appFileInfo = VDS.getIntance().getFileOperate().copyFile(param.toString().trim());
            if(appFileInfo == null) {
                outputVo.setMessage("文件id【" + param + "】对应的文件不存在");
                outputVo.setSuccess(false);
            } else {
                outputVo.setSuccess(true);
                outputVo.put(appFileInfo.getId());
            }
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
