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
 * utf-8字符集的MD5加密<br>
 * <br>
 * 代码示例：MD5EncryptByUTF8("同望")，返回值为：wd7VFY1HGgjdEK2kbS7L+w==<br>
 * 参数1：需要加密的字符串（字符串类型）<br>
 * 返回值为字符串类型<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:42
 */
public class MD5EncryptByUTF8Func implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.MD5EncryptByUTF8.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(MD5EncryptByUTF8Func.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 1);
            param = context.getInput(0);
            String desVal = param == null ? "" : param.toString();
            String encryVal = "";
            if (!desVal.equals("")) {
                encryVal = md5HashByEncode(desVal, "utf-8");
            }

            outputVo.put(encryVal);
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

    private String md5HashByEncode(String input, String charType) throws Exception {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] out = m.digest(input.getBytes(charType));
            return new String(Base64.encode(out));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
