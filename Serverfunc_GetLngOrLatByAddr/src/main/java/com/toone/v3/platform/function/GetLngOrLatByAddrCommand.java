package com.toone.v3.platform.function;

import com.yindangu.v3.business.plugin.business.IContext;
import com.yindangu.v3.business.plugin.business.api.httpcommand.FormatType;
import com.yindangu.v3.business.plugin.business.api.httpcommand.IHttpCommand;
import com.yindangu.v3.business.plugin.business.api.httpcommand.IHttpContext;
import com.yindangu.v3.business.plugin.business.api.httpcommand.IHttpResultVo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 由于历史原因，该函数的功能已command的形式对外使用，因此这里也需要兼容这种场景
 *
 * @Author xugang
 * @Date 2021/6/7 16:40
 */
public class GetLngOrLatByAddrCommand implements IHttpCommand {

    @Override
    public IHttpResultVo execute(IHttpContext context) {
        Map<String, Object> params = (Map<String, Object>) context.getToken().get("params");
        if(params == null) {
            throw new RuntimeException("Command【GetLngOrLatByAddr】的必须包含params参数");
        } else {
            boolean flag = false;
            String error_msg = "";
            String address = (String) params.get("address");
            String type = (String) params.get("type");
            Double result = 0.0;
            if(address == null || address.equals("")) {
                error_msg = "Command【GetLngOrLatByAddr】获取key：address的值为空！";
                flag = false;
            } else if(type == null || (!type.equals("lng") && !type.equals("lat"))) {
                error_msg = "Command【GetLngOrLatByAddr】传入的类型不正确，类型只能lng或lat，当前值为："+type;
                flag = false;
            } else {
                GetLngOrLatByAddrFunc func = new GetLngOrLatByAddrFunc();
                Map<String, BigDecimal> resultMap = func.getLatAndLngByAddress(address);
                result = resultMap.get(type).doubleValue();
                result = resultMap.get(type).doubleValue();
                if(result==null || result==404){
                    error_msg = "Command【GetLngOrLatByAddr】根据当前地址无法确定经纬度";
                    flag = false;
                }else{
                    error_msg = "";
                    flag = true;
                }
            }

            Map<String,Object> resultData =new HashMap<String, Object>();
            resultData.put("error_msg", error_msg);
            resultData.put("isSuccess", flag);
            resultData.put("value", result);
            context.newResultVo().setValue(resultData).setValueType(FormatType.Json);
        }


        return null;
    }
}
