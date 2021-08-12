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
 * 获取客户端请求的IP地址<br>
 * <br>
 * 代码示例:GetIPAddressFunc()，返回当前客户端请求的IP地址字符串，注意返回的是服务器端获取到的请求IP地址，如果使用了代理服务器，则返回代理之前的真实客户端地址。<br>
 * 无参数；<br>
 * 返回值为字符串。<br>
 *
 * @Author xugang
 * @Date 2021/6/7 15:49
 */
public class GetIPAddressFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GetIPAddressRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GetIPAddressFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        try {
            String ip = VDS.getIntance().getSessionManager().getRealRemoteAddr();
            outputVo.put(ip);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，msg=" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
