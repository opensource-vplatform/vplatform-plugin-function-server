package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.platform.plugin.util.VdsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * 对字符串基于64位解码，返回值为字符串。<br>
 * 代码示例:DecodeBASE64("PCF4bWw+PC8+","utf-8")，返回值为 "<!xml></>"。<br>
 * 参数1：所需解码的字符串(字符串类型)；<br>
 * 参数2：字符编码（字符串类型）；<br>
 * 返回值类型：字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:04
 */
public class DecodeBASE64Func implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.DecodeBASE64.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(DecodeBASE64Func.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 1);

            param1 = context.getInput(0);
            param2 = context.getInputSize() == 2 ? context.getInput(1) : "utf-8";

            service.checkParamNull(funcCode, param1, param2);

            String str = param1.toString();
            String charset = param2.toString();
            charset = charset.trim().equals("") ? "utf-8" : charset;

            String base64Str;
            try {
                byte[] bytes = VdsUtils.crypto.decodeBase64(str, charset);
                base64Str = new String(bytes, charset);
            } catch (UnsupportedEncodingException e) {
                throw new ServerFuncException("函数【" + funcCode + "】的base64解码失败，字符编码不正确，参数1：" + param1 + "参数2：" + param2);
            } catch (Exception e) {
                throw new ServerFuncException("函数【" + funcCode + "】的base64解码失败，参数1：" + param1 + "参数2：" + param2);
            }

            outputVo.setSuccess(true);
            outputVo.put(base64Str);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param1 + "，参数2：" + param2 + "，" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2, e);
        }
        return outputVo;
    }
}
