package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

/**
 * 将字符串作为URI组件进行编码。<br>
 * <br>
 * 代码示例:EncodeURIComponent("www.baidu.com/s?wd=同望科技")，返回值为："www.baidu.com%2Fs%3Fwd%3D%E5%90%8C%E6%9C%9B%E7%A7%91%E6%8A%80"。<br>
 * 参数1：需要编码的字符串(字符串类型)；<br>
 * 返回值类型：字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:14
 */
public class EncodeURIComponentFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.EncodeURIComponent.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(EncodeURIComponentFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
            service.checkParamNull(funcCode, param);

            String uri = param.toString();
            String result = URLEncoder.encode(uri, "UTF-8");

            outputVo.setSuccess(true);
            outputVo.put(result);
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
