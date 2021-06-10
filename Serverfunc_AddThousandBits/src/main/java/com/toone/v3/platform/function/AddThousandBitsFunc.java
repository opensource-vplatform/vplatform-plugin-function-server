package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 给数字添加千分位。<br>
 * 代码示例：AddThousandBits(1234567.7654321) 返回值：1,234,567.765,432,1。<br>
 * 参数一：需要添加千分位的数字（数字类型）；<br>
 * 返回值类型：字符串类型。<br>
 *
 * @author xugang
 * @date 2021-05-24
 */
public class AddThousandBitsFunc implements IFunction {

    private final static Logger log = LoggerFactory.getLogger(AddThousandBitsFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(ServerFuncCommonUtils.AddThousandBits.Function_Code(), context, 1);
            param = context.getInput(0);
            if(!(param instanceof Number)) {
                throw new ServerFuncException("函数【" + ServerFuncCommonUtils.AddThousandBits.Function_Code() + "】的第1个参数必须是数字类型，参数1：" + param);
            }

            BigDecimal source = new BigDecimal(param.toString());
            String sourceValue = source.toPlainString();
            String[] arrayValue = sourceValue.split("\\.");
            String result = null;
            if (arrayValue.length == 1) {
                result = sourceValue.replaceAll("(?<=\\d)(?=(?:\\d{3})+$)", ",");
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < arrayValue[1].length(); i++) {
                    char a = arrayValue[1].charAt(i);
                    sb.append(a);
                    if ((i + 1) % 3 == 0) {
                        if (arrayValue[1].length() != i + 1)
                            sb.append(",");
                    }
                }
                result = arrayValue[0].replaceAll("(?<=\\d)(?=(?:\\d{3})+$)", ",") + "." + sb.toString();
            }

            outputVo.setSuccess(true);
            outputVo.put(result);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + ServerFuncCommonUtils.AddThousandBits.Function_Code() + "】计算有误，参数1：" + param + "，" + e.getMessage());
            log.error("函数【" + ServerFuncCommonUtils.AddThousandBits.Function_Code() + "】计算失败，参数1：" + param, e);
        }

        return outputVo;
    }
}
