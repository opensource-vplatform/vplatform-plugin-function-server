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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xugang
 * @Date 2021/5/31 22:00
 */
public class GetZoomLevelFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GetZoomLevelRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GetZoomLevelFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            int size = context.getInputSize();
            if (size == 1) {
                param1 = context.getInput(0);
            } else if (size == 3) {
                param1 = context.getInput(0);
                param2 = context.getInput(1);
                param3 = context.getInput(2);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】需要1个或者3个参数，当前参数个数：" + size);
            }

            String dataViewName = (String) param1;// 传入实体
            String latCode = "lat";
            String lntCode = "lng";
            if (size == 3) {
                latCode = (String) param3;
                lntCode = (String) param2;
            }

            Object runtimeParams = VDS.getIntance().getFormulaEngine().eval(dataViewName);
            if (null == runtimeParams) {
                throw new ServerFuncException("函数【" + funcCode + "】获取实体发生错误，" + dataViewName
                        + "获取不到对应的实体变量，请注意需要加上变量范围前缀");
            }
            double minLat = 0;
            double minLng = 0;
            double maxLat = 0;
            double maxLng = 0;

            if (runtimeParams instanceof IDataView) {
                IDataView dataView = (IDataView) runtimeParams;
                List<Map<String, Object>> listMap = dataView.getDatas();
                for (int i = 0; i < listMap.size(); i++) {
                    Map<String, Object> data = listMap.get(i);
                    BigDecimal blng = (BigDecimal) data.get(latCode);
                    BigDecimal blat = (BigDecimal) data.get(lntCode);
                    if (blng != null && blat != null) {
                        double lng = blng.doubleValue();
                        double lat = blat.doubleValue();
                        if (i == 0) {
                            minLat = lat;
                            maxLat = lat;
                            minLng = lng;
                            maxLng = lng;
                        } else {
                            if (minLat > lat) {
                                minLat = lat;
                            }
                            if (maxLat < lat) {
                                maxLat = lat;
                            }
                            if (minLng > lng) {
                                minLng = lng;
                            }
                            if (maxLng < lng) {
                                maxLng = lng;
                            }
                        }
                    }
                }
            }
            double dis = GetDistance(minLng, minLat, maxLng, maxLat);
            outputVo.put(null);
            for (Map.Entry<String, Integer> map : zoomLevel.entrySet()) {
                Integer key = Integer.parseInt(map.getKey());
                if (key > dis) {
                    outputVo.put(map.getValue());
                    break;
                }
            }
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

    private static final double EARTH_RADIUS = 6371;

    public static final LinkedHashMap<String, Integer> zoomLevel = new LinkedHashMap<String, Integer>();

    static {
        zoomLevel.put("20", 19);
        zoomLevel.put("50", 18);
        zoomLevel.put("100", 17);
        zoomLevel.put("200", 16);
        zoomLevel.put("500", 15);
        zoomLevel.put("1000", 14);
        zoomLevel.put("2000", 13);
        zoomLevel.put("5000", 12);
        zoomLevel.put("10000", 11);
        zoomLevel.put("20000", 10);
        zoomLevel.put("25000", 9);
        zoomLevel.put("50000", 8);
        zoomLevel.put("100000", 7);
        zoomLevel.put("2000000", 6);
        zoomLevel.put("5000000", 5);
        zoomLevel.put("10000000", 4);
        zoomLevel.put("20000000", 3);
        zoomLevel.put("50000000", 2);
        zoomLevel.put("100000000", 1);
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double long1, double lat1, double long2,
                                     double lat2) {
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
}
