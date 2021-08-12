package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.httpcommand.FormatType;
import com.yindangu.v3.business.plugin.business.api.httpcommand.IHttpCommand;
import com.yindangu.v3.business.plugin.business.api.httpcommand.IHttpContext;
import com.yindangu.v3.business.plugin.business.api.httpcommand.IHttpResultVo;

import java.util.HashMap;
import java.util.Map;

/**
 * 由于历史原因，该函数的功能已command的形式对外使用，因此这里也需要兼容这种场景
 *
 * @Author xugang
 * @Date 2021/6/16 9:16
 */
public class GetLocationPlaceCommand implements IHttpCommand {

    @Override
    public IHttpResultVo execute(IHttpContext context) {
        Map<String, Object> params = (Map<String, Object>) context.getToken().get("params");
        if (params == null) {
            throw new RuntimeException("Command【GetLocationPlace】的必须包含params参数");
        } else {
            boolean flag = true;
            String error_msg = "";
            String wd = (String) params.get("latitude");
            String jd = (String) params.get("longitude");
            String format = params.get("format") == null ? "" : params.get("format").toString();

            String cityName = "";
            if (format == "" || format.matches("^[gpcdsn]+$")) {
                double latitude = getDoubleData(wd, 1);
                double longitude = getDoubleData(jd, 2);
                GetLoactionPlaceFunc func = new GetLoactionPlaceFunc();
                //获取地理信息
                String locationInfo = func.GetLocationMsgByBaidu(latitude, longitude);
                //根据地理信息获取地址信息
                cityName = format.equals("") ? func.getCityName(locationInfo) : func.getDetailAddress(locationInfo, format);
                if (cityName == null || cityName.equals("")) {
                    flag = false;
                    error_msg = "经度[" + longitude + "],纬度[" + latitude + "]所指向的地点还没进行划分对应的地区信息，请确认经度纬度是否正确！\n" + locationInfo;
                }
            } else {
                flag = false;
                error_msg = "第三个参数格式不正确！只能包含[g、p、c、d、s、n]。";
            }

            Map<String, Object> resultData = new HashMap<String, Object>();
            resultData.put("error_msg", error_msg);
            resultData.put("isSuccess", flag);
            resultData.put("value", cityName);
            IHttpResultVo resultVo = context.newResultVo().setValue(resultData).setValueType(FormatType.Json);

            return resultVo;
        }
    }

    /**
     * 对传入的经度纬度进行判断及转类型
     *
     * @param source
     * @param index
     * @return
     */
    public Double getDoubleData(Object source, int index) {
        double result = 0.0;
        if (source == null) throw new ServerFuncException("Command【GetLocationPlace】第" + index + "个参数不能为空");
        if (source instanceof String) {
            if (source != null && !source.toString().equals("")) {
                result = Double.parseDouble(source.toString());
            } else {
                throw new ServerFuncException("Command【GetLocationPlace】第" + index + "个参数不能为空");
            }
        } else {
            result = Double.parseDouble(source.toString());
        }
        if (index % 2 == 0 && (result > 180 || result < -180))
            throw new ServerFuncException("Command【GetLocationPlace】第" + index + "个参数不属于经度范围(-180~180),当前值：" + source);
        if (index % 2 == 1 && (result > 90 || result < -90))
            throw new ServerFuncException("Command【GetLocationPlace】第" + index + "个参数不属于纬度范围(-90~90),当前值：" + source);
        return result;
    }

    /**
     * 抛出异常
     */
    public static void handleBaseException() {
        try {
            throw new ServerFuncException("Command【GetLocationPlace】网络不给力，获取不到数据。");
        } catch (Exception e) {
            throw new ServerFuncException("Command【GetLocationPlace】网络不给力，获取不到数据。");
        }

    }
}
