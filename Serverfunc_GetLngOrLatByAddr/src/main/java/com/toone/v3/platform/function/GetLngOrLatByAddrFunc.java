package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据地址获取经度或纬度<br>
 * <br>
 * 代码示例：GetLngOrLatByAddr("广东省珠海市港湾大道科技五路19号","lng")，取相应地址的经度，若第二个参数为lat，则取纬度。<br>
 * 参数1：地址（字符串，必填），目前仅限国内地址，地址结构（（省/市/区/街道/门牌号））越完整，地址内容越准确，解析的坐标精度越高。<br>
 * 参数2：lng或lat，其中lng为取经度，lat为取纬度（字符串，必填）<br>
 * 返回值为数字类型<br>
 * 注：使用此函数前，需要在业务系统控制台配置相关参数<br>
 * 1.前往控制台（system/console） -- 系统维护 -- 配置管理 -- 百度鹰眼服务 配置ak<br>
 * 2.ak的获取：前往百度开放平台创建应用后获取，注意在创建时【应用类型】需选【服务端】（http://lbsyun.baidu.com/apiconsole/key）<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:56
 */
public class GetLngOrLatByAddrFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.GetLngOrLatByAddr.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(GetLngOrLatByAddrFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 2);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            service.checkParamBlank(funcCode, param1, param2);

        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }
}
