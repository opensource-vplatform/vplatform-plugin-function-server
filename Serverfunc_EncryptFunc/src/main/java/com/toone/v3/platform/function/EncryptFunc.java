package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.toone.v3.platform.function.encrypt.EncryptFactory;
import com.toone.v3.platform.function.encrypt.api.IEncrypt;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对字符串进行加密。<br>
 * <br>
 * 代码示例：EncryptFunc("12345","md5","xxxx")，返回值为对应加密方式加密后的字符串。<br>
 * 参数1：需要加密的字符串，字符串类型，必填。默认使用UTF-8编码<br>
 * 参数2：加密算法，字符串类型，必填，不区分大小写。支持以下类型：<br>
 *        md5：基于RFC 1321。安全性一般，性能高，不可逆，返回32位16进制。主要用于一致性验证、数字签名等<br>
 *        SHA-1：基于NIST's FIPS 180-4，安全比md5高，性能比md5慢，不可逆，返回32位16进制。<br>
 *        SHA-256：基于NIST's FIPS 180-4，安全比SHA-1高，性能比SHA-1慢，不可逆，返回64位16进制。<br>
 *        SHA-384：基于NIST's FIPS 180-4，安全比SHA-256高，性能比SHA-256慢，不可逆，返回96位16进制。<br>
 *        SHA-512：基于NIST's FIPS 180-4，安全比SHA-384高，性能比SHA-384慢，不可逆，返回128位16进制。<br>
 *        AES：基于PKCS #5。对称加密，可逆，安全性高，是一种区块加密标准。广泛用于金融财务、在线交易等领域。<br>
 * 参数3：密钥，字符串类型，必填；当且仅当参数2为AES，请填写正确密钥，其他参数任意填写。<br>
 *        AES密钥要求：长度要求为16byte，不足将补0，超过16byte将自动截取。不建议使用中文作为密钥，推荐大小字符以及数字的组合。<br>
 * 返回值类型：加密后的字符串，字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:17
 */
public class EncryptFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.EncryptFunc.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(EncryptFunc.class);

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

            if(param1 == null || param1.toString().equals("")) {
                outputVo.put("");
                outputVo.setSuccess(true);
            } else {
                String str = param1.toString();
                if(param2 == null) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第2个参数不能为null");
                } else {
                    IEncrypt provider = EncryptFactory.getProvider(param2.toString(), param3, funcCode);
                    CharSequence encryptStr = provider.encrypt(str, param3.toString());
                    outputVo.put(encryptStr.toString());
                    outputVo.setSuccess(true);
                }
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
