package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.business.session.apiserver.ISession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 校验输入的验证码是否正确。<br>
 * <br>
 * 代码示例:CheckCertCodeFun("EEDS")，返回值为 True。<br>
 * 参数1--原字符串(字符串类型)；<br>
 * 返回值为布尔类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 10:58
 */
public class CheckCertCodeFunFunc implements IFunction {
    private final static Logger log = LoggerFactory.getLogger(CheckCertCodeFunFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(ServerFuncCommonUtils.CheckCertCodeFun.Function_Code(), context, 1);
            param = context.getInput(0);

            if (!(param instanceof String)) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.CheckCertCodeFun.Function_Code() + "】的第1个参数必须为字符串，参数1：" + param);
            }

            ISession session = VDS.getIntance().getSessionManager().getCurrentSession();
            String certCode = session.getAttribute("CERT_CODE");
            if (certCode == null || certCode.trim().equals("") || ((String) param).trim().equals("")) {
                outputVo.put(false);
            } else {
                if (certCode.trim().equals(((String) param).trim())) {
                    outputVo.put(true);
                } else {
                    outputVo.put(false);
                }
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + ServerFuncCommonUtils.CheckCertCodeFun.Function_Code() + "】计算有误，参数1：" + param + ", " + e.getMessage());
            log.error("函数【" + ServerFuncCommonUtils.CheckCertCodeFun.Function_Code() + "】计算失败，参数1：" + param, e);
        }
        return outputVo;
    }
}
