package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检查指定字符串是否包含中文字符;<br>
 * <br>
 * 代码示例:CheckChinese("a你好bc") 返回值为True。<br>
 * 参数1--待检查字符串(字符串类型)；<br>
 * 返回值为布尔类型<br>
 *
 * @Author xugang
 * @Date 2021/5/31 10:59
 */
public class CheckChineseFunc implements IFunction {

    private final static Logger log = LoggerFactory.getLogger(CheckChineseFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(ServerFuncCommonUtils.CheckChinese.Function_Code(), context, 1);

            param = context.getInput(0);

            if (param == null || !(param instanceof String)) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.CheckChinese.Function_Code() + "】的第1个参数必须为字符串类型，且不能为null，参数1：" + param);
            }

            if(isContainsChinese((String) param)) {
                outputVo.put(true);
            } else {
                outputVo.put(false);
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + ServerFuncCommonUtils.CheckChinese.Function_Code() + "】计算有误，参数1：" + param + ", " + e.getMessage());
            log.error("函数【" + ServerFuncCommonUtils.CheckChinese.Function_Code() + "】计算失败，参数1：" + param, e);
        }
        return outputVo;
    }

    /**
     * 正则判断是否中文
     *
     * @param str
     * @return
     */
    private boolean isContainsChinese(String str) {
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(str);
        boolean flg = false;
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }
}
