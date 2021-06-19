package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.toone.v3.platform.function.util.DecryptUtil;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对字符串进行解密<br>
 * <br>
 * 代码示例：DecryptFunc("12345","AES","xxxx")，返回值为解密后的字符串。<br>
 * 参数1：需要解密的字符串，字符串类型，必填。默认使用UTF-8编码<br>
 * 参数2：解密算法，字符串类型，必填，不区分大小写。目前只支持支持AES。<br>
 * 参数3：密钥，字符串类型，必填。<br>
 * 返回值类型：解密后的字符串，字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:33
 */
public class DecryptFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.DecryptFunc.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(DecryptFunc.class);

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
            service.checkParamNull(funcCode, param1, param2, param3);

            String content = (String) param1;
            String algorithm = (String) param1;
            String algorithmKey = (String) param1;

            if (algorithm.equalsIgnoreCase("aes")) {
                String res = DecryptUtil.decryptAES(content, algorithmKey);
                outputVo.put(res);
                outputVo.setSuccess(true);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】当前仅支持解密算法：AES，不支持解密算法：" + algorithm);
            }
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
