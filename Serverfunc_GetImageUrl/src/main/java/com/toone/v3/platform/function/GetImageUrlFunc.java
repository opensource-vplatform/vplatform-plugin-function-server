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
 * 获取图片的URL路径<br>
 * <br>
 * 代码示例:GetImageUrl("8a819ab551c2421c0151c3b597b80ddf")，返回值为图片的URL路径。<br>
 * 参数1--图片的文件id(字符串类型)；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/7 15:36
 */
public class GetImageUrlFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GetImageUrlRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GetImageUrlFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
            service.checkParamBlank(funcCode, param);

            String url = VDS.getIntance().getFileOperate().getUrlByFileId(param.toString(), true);
            outputVo.put(url);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
