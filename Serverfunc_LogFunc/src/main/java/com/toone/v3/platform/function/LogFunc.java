package com.toone.v3.platform.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.console.api.IConsoleManager;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.business.preferencesmanager.api.IPreferencesManager;

/**
 * 打日志信息到控制台上。<br>
 * <br>
 * 代码示例:LogFunc("aaaa","error") 返回值为 true。<br>
 * 参数1--日志信息（字符串类型）；<br>
 * 参数2--日志类型（"debug","info","warn","error"）；<br>
 * 返回值为布尔类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/1 16:04
 */
public class LogFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.LogFunc.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(LogFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        try {
            VDS vds = VDS.getIntance();
            ServerFuncCommonUtils service = vds.getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 2);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            service.checkParamNull(funcCode, param1, param2);

            //2021-07-01，taoyz，增加配置，增加控制台输出
            IPreferencesManager preferencesManager = vds.getPreferencesManager();
            String outputToConsole = preferencesManager.getProperty("com.toone.v3.platform", "Serverfunc_LogFunc", funcCode, "outputToConsole");
            boolean outputToConsoleBoolean = false;
            if(outputToConsole!=null && "true".equalsIgnoreCase(outputToConsole)){
                outputToConsoleBoolean = true;
            }

            IConsoleManager consoleManager = vds.getConsoleManager();

            String msg = param1.toString();

            switch (param2.toString()) {
                case "debug":
                    log.debug(msg);
                    if(outputToConsoleBoolean){
                        consoleManager.output("Debug:"+msg);
                    }
                    outputVo.put(true);
                    break;
                case "info":
                    log.info(msg);
                    if(outputToConsoleBoolean){
                        consoleManager.output("Info:"+msg);
                    }
                    outputVo.put(true);
                    break;
                case "warn":
                    log.warn(msg);
                    if(outputToConsoleBoolean){
                        consoleManager.output("Warn:"+msg);
                    }
                    outputVo.put(true);
                    break;
                case "error":
                    log.error(msg);
                    if(outputToConsoleBoolean){
                        consoleManager.output("Error:"+msg);
                    }
                    outputVo.put(true);
                    break;
                default:
                    outputVo.put(false);
                    log.error("函数【" + funcCode + "】的第二个参数必须符合枚举：debug、info、warn、error，参数2：" + param2);
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(true);
            outputVo.put(false);
            log.error(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2, e);
        }
        return outputVo;
    }
}
