package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.toone.v3.platform.function.encrypt.Strategy;
import com.toone.v3.platform.function.encrypt.api.DensityStrategy;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 按照加密策略，对字符串进行加密。<br>
 * <br>
 * 代码示例:EncryptionFunc("MD5","eee11") 返回值为"cF8FevsV8QeVyD/X1ClR3A=="。<br>
 * 参数1--加密策略(字符串类型)，支持：MD5，Base64，AES；<br>
 * 参数2--需要加密的字符串(字符串类型)；<br>
 * 返回值为字符串。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:17
 */
public class EncryptionFunc implements IFunction {

    // 函数编码
    private final static String funcCode = EncryptionRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(EncryptionFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            int size = context.getInputSize();
            if(size == 2 || size == 3) {
                param1 = context.getInput(0);
                param2 = context.getInput(1);
                if(size == 3) {
                    param3 = context.getInput(2);
                }
                service.checkParamNull(funcCode, param1, param2);
                Strategy strategy = Strategy.get(param1.toString());
                if(strategy == null) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第1个参数只能使用枚举值：MD5，Base64，AES，参数1：" + param1);
                }
                DensityStrategy densityStrategy = DensityStrategy.Factory.getDensityStrategyInstance(strategy);
                String encryption = densityStrategy.encryption(param2.toString(), (String) param3);
                outputVo.put(encryption);
                outputVo.setSuccess(true);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】需要2个或者3个参数，当前参数个数：" + size);
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
