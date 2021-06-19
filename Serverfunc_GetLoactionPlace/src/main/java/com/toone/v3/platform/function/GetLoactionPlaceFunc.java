package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.platform.plugin.util.VdsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author xugang
 * @Date 2021/5/31 21:56
 */
public class GetLoactionPlaceFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.GetLoactionPlace.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(GetLoactionPlaceFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object wd = null;
        Object jd = null;
        Object param3 = null;
        try {
            int size = context.getInputSize();
            if (size == 2) {
                wd = context.getInput(0);
                jd = context.getInput(1);
            } else if (size == 3) {
                wd = context.getInput(0);
                jd = context.getInput(1);
                param3 = context.getInput(2);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】需要2个或者3个参数，当前参数个数：" + size);
            }

            double latitude = getDoubleData(wd, 1);
            double longitude = getDoubleData(jd, 2);
            String format = size == 3 && param3 != null && !param3.toString().equals("") ? param3.toString() : "";
            String address = "";
            if (format.equals("") || format.matches("^[gpcdsn]+$")) {
                String locationInfo = GetLocationMsgByBaidu(latitude, longitude);
                address = format.equals("") ? getCityName(locationInfo) : getDetailAddress(locationInfo, format);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】的第3个参数格式不正确！只能包含[g、p、c、d、s、n]。");
            }

            outputVo.put(address);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + wd + "参数2：" + jd + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }

    /**
     * 根据经度纬度获取所在市（使用百度API）
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return
     */
    public static String GetLocationMsgByBaidu(double latitude, double longitude) {
        String addr = "";
        String url = String.format("http://api.map.baidu.com/geocoder/v2/?ak=abwTiSUtIxCLMlLFUs4GI29BlDRnyoag&callback=renderReverse&location=%s,%s&output=json&pois=1", latitude, longitude);
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            log.error("函数[GetLoactionPlace]：创建API接口对象错误！\n" + e.getMessage());
            handleBaseException();
        }
        InputStreamReader insr = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                insr = new InputStreamReader(httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                while ((data = br.readLine()) != null) {
                    addr = addr + data;
                }
            }
        } catch (IOException e) {
            throw new ServerFuncException("函数[GetLoactionPlace]：获取数据失败\n" + e.getMessage());
        } finally {
            try {
                insr.close();
            } catch (IOException e) {
                log.error("函数[GetLoactionPlace]：流关闭错误！" + e.getMessage());
            }
        }
        return addr;
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
        if (source == null) throw new ServerFuncException("函数[GetLoactionPlace]第" + index + "个参数不能为空");
        if (source instanceof String) {
            if (source != null && !source.toString().equals("")) {
                result = Double.parseDouble(source.toString());
            } else {
                throw new ServerFuncException("函数[GetLoactionPlace]第" + index + "个参数不能为空");
            }
        } else {
            result = Double.parseDouble(source.toString());
        }
        if (index % 2 == 0 && (result > 180 || result < -180))
            throw new ServerFuncException("函数[GetLoactionPlace]第" + index + "个参数不属于经度范围(-180~180),当前值：" + source);
        if (index % 2 == 1 && (result > 90 || result < -90))
            throw new ServerFuncException("函数[GetLoactionPlace]第" + index + "个参数不属于纬度范围(-90~90),当前值：" + source);
        return result;
    }

    /**
     * 解析地理位置信息，获取城市名称。
     *
     * @param locationInfo 地理位置信息
     * @return
     */
    public static String getCityName(String locationInfo) {
        String result = "";
        if (locationInfo != null && !locationInfo.equals("")) {
            //去掉没用的格式
            locationInfo = locationInfo.substring(0, locationInfo.length() - 1).replace("renderReverse&&renderReverse(", "");
            Map<String, Object> mmMap = VdsUtils.json.fromJson(locationInfo);
            if (mmMap != null && mmMap.containsKey("result")) {
                Map resultslist = (Map) mmMap.get("result");
                if (resultslist != null && resultslist.containsKey("addressComponent")) {
                    Map addressComponent = (Map) resultslist.get("addressComponent");
                    result = addressComponent.get("city").toString().replace("市", "");
                } else {
                    log.error("函数[GetLoactionPlace]：通过接口得到的数据格式不正确。没有包含[addressComponent]\n" + locationInfo);
                    handleBaseException();
                }
            } else {
                log.error("函数[GetLoactionPlace]：通过接口得到的数据格式不正确。没有包含[result]\n" + locationInfo);
                handleBaseException();
            }
        } else {
            log.error("函数[GetLoactionPlace]：通过接口得到的数据为空。\n" + locationInfo);
            handleBaseException();
        }
        return result;
    }

    /**
     * 解析地理位置信息，获取详细地址信息
     *
     * @param locationInfo 地理位置信息
     * @param type         地址格式
     * @return
     */
    @SuppressWarnings("serial")
    public static String getDetailAddress(String locationInfo, String type) {
        Map<String, String> map = new LinkedHashMap<String, String>() {
            {
                put("g", "country");
                put("p", "province");
                put("c", "city");
                put("d", "district");
                put("s", "street");
                put("n", "street_number");
            }
        };
        StringBuffer result = new StringBuffer();
        if (locationInfo != null && !locationInfo.equals("")) {
            //去掉没用的格式
            locationInfo = locationInfo.substring(0, locationInfo.length() - 1).replace("renderReverse&&renderReverse(", "");
            Map<String, Object> mmMap = VdsUtils.json.fromJson(locationInfo);
            if (mmMap != null && mmMap.containsKey("result")) {
                Map resultslist = (Map) mmMap.get("result");
                if (resultslist != null && resultslist.containsKey("addressComponent")) {
                    Map addressComponent = (Map) resultslist.get("addressComponent");
                    char[] type_array = type.toCharArray();
                    for (int i = 0; i < type_array.length; i++) {
                        if (map.containsKey(type_array[i] + "")) {
                            result.append(addressComponent.get(map.get(type_array[i] + "")).toString());
                        }
                    }
                } else {
                    log.error("函数[GetLoactionPlace]：通过接口得到的数据格式不正确。没有包含[addressComponent]\n" + locationInfo);
                    handleBaseException();
                }
            } else {
                log.error("函数[GetLoactionPlace]：通过接口得到的数据格式不正确。没有包含[result]\n" + locationInfo);
                handleBaseException();
            }
        } else {
            log.error("函数[GetLoactionPlace]：通过接口得到的数据为空。\n" + locationInfo);
            handleBaseException();
        }
        return result.toString();
    }

    /**
     * 抛出异常
     */
    public static void handleBaseException() {
        try {
            throw new ServerFuncException("函数[GetLoactionPlace]：网络不给力，获取不到数据。");
        } catch (Exception e) {
            throw new ServerFuncException("函数[GetLoactionPlace]：网络不给力，获取不到数据。");
        }

    }

    @SuppressWarnings("static-access")
    public static String GetLocationMsgByGoogle(double latitude, double longitude) {
        String addr = "";
        String result = "";
        String url = String.format(
                "http://maps.google.cn/maps/api/geocode/json?latlng=%s,%s&language=CN",
                latitude, longitude);
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                while ((data = br.readLine()) != null) {
                    addr = addr + data;
                }
                if (addr == null || addr.equals("")) {
                    System.err.println("函数-GetLoactionPlace：Google API取不到数据");
                    return "";
                }
                Map<String, String> mmMap = VdsUtils.json.fromJson(addr);
                List resultslist = (ArrayList) (Object) mmMap.get("results");
                if (resultslist != null && resultslist.size() > 0) {
                    List taList = ((ArrayList) (Object) ((Map) (Object) resultslist.get(0)).get("address_components"));
                    List iden = null;
                    for (int i = 1; i < taList.size(); i++) {
                        Map map = (Map) (Object) taList.get(i);
                        iden = (ArrayList) (Object) map.get("types");
                        if (iden.contains("locality")) {
                            result = (String) map.get("long_name");
                            result = result.replace("市", "");
                            return result;
                        }
                    }

                }
                //System.out.println(a.toString());
                insr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addr;
    }
}
