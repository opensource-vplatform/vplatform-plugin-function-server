package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 获取轨迹里程<br>
 * <br>
 * 代码示例：GetDistance("BR_IN_PARENT.locationEntity","lng","lat")，根据经纬度数据顺序集合，自动计算轨迹里程，例如返回轨迹里程为6200.30（单位为米）<br>
 * 参数1：实体编码（字符串，必填），必须带前缀，实体可以是方法输入(BR_IN_PARENT.entityCode)、方法输出(BR_OUT_PARENT.entityCode)、方法变量(BR_VAR_PARENT.entityCode)<br>
 * 参数2：经度字段编码（字符串，必填）<br>
 * 参数3：纬度字段编码（字符串，必填）<br>
 * 返回值为数字类型<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:49
 */
public class GetDistanceFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GetDistanceRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GetDistanceFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 3);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            param3 = context.getInput(2);
            service.checkParamBlank(funcCode, param1, param2, param3);

            double total = 0.0d;
            Object runtimeParams = VDS.getIntance().getFormulaEngine().eval(param1.toString());
            if (runtimeParams == null) {
                throw new ServerFuncException("函数【" + funcCode + "】根据实体编码获取不到对应的实体变量，请注意需要加上变量范围前缀，参数1：" + param1);
            } else if (runtimeParams instanceof IDataView) {
                IDataView dataView = (IDataView) runtimeParams;
                List<Map<String, Object>> datas = dataView.getDatas();
                Double latLast = null;
                Double lngLast = null;
                for (Map<String, Object> data : datas) {
                    BigDecimal blng = (BigDecimal) data.get(param2.toString());
                    BigDecimal blat = (BigDecimal) data.get(param3.toString());
                    if (blng != null && blat != null) {
                        double lng = blng.doubleValue();
                        double lat = blat.doubleValue();
                        if (latLast != null && lngLast != null) {
                            double dis = getDistance(lng, lat, lngLast, latLast);
                            total += dis;
                        }
                        lngLast = lng;
                        latLast = lat;
                    }
                }
            }
            outputVo.put(total);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }

    private final double EARTH_RADIUS = 6371d;

    public double getDistance(double long1, double lat1, double long2, double lat2) {

        double Lat1 = rad(lat1); // 纬度
        double Lat2 = rad(lat2);
        double a = Lat1 - Lat2;//两点纬度之差
        double b = rad(long1) - rad(long2); //经度之差
        double s = 2 * Math.asin(Math
                .sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));//计算两点距离的公式
        s = s * EARTH_RADIUS * 1000;//弧长乘地球半径（半径为米）
        s = Math.round(s * 10000d) / 10000d;//精确距离的数值
        return s;
    }

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
