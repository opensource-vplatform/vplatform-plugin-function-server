package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密<br>
 * <br>
 * 代码示例：MD5Encrypt("Example")，返回值加密后的字符串。<br>
 * 参数1--加密串(字符串类型)；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:42
 */
public class MD5EncryptFunc implements IFunction {

    // 函数编码
    private final static String funcCode = MD5EncryptRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(MD5EncryptFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);

            String desVal = (String) param;
            if (desVal == null || desVal.equals("")) {
                outputVo.put("");
            } else {
                outputVo.put(md5Hash(desVal));
            }

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

    private String md5Hash(String input) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] out = m.digest(input.getBytes());
            return new String(Base64.encode(out));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
