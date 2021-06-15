package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.business.preferencesmanager.api.IPreferencesManager;
import com.yindangu.v3.platform.plugin.util.VdsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

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

            String addr = param1.toString();
            String type = param2.toString();

            double result;
            if (type.equals("lng") || type.equals("lat")) {
                Map<String, BigDecimal> resultMap = getLatAndLngByAddress(addr);
                result = resultMap.get(type).doubleValue();
                if (result == 404) {
                    log.warn("函数【" + funcCode + "】根据当前地址无法确定经纬度！");
                }
                outputVo.put(result);
                outputVo.setSuccess(true);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】的第2个参数的值只能是lng或lat，当前值为：" + type);
            }

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

    public Double GetLngOrLat(String address, String type) {
        Double result = 0.0;
        String url = String.format("http://api.map.baidu.com/geocoder?address=%s&output=json", address);
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
                String addr = "";
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                while ((data = br.readLine()) != null) {
                    addr = addr + data;
                }
                Map<String, String> mmMap = VdsUtils.json.fromJson(addr);
                Map resultslist = (Map) (Object) mmMap.get("result");
                Map lngorlat = (Map) (Object) resultslist.get("location");
                if (type.equals("lng")) {
                    result = Double.parseDouble(lngorlat.get("lng").toString());
                }
                if (type.equals("lat")) {
                    result = Double.parseDouble(lngorlat.get("lat").toString());
                }

                insr.close();
            }
        } catch (IOException e) {
            throw new ServerFuncException("系统内部错误 - " + e.getMessage());
        }
        return result;
    }

    public Map<String, BigDecimal> getLatAndLngByAddress(String addr) {
        String address = "";
        String lat = "";
        String lng = "";
        try {
            address = java.net.URLEncoder.encode(addr, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            throw new ServerFuncException("地址不正确，地址：" + addr);
        }
        String ak = getAK();
        if (ak == null || ak.trim().equals("")) {
            throw new ServerFuncException("百度ak值不正确，请到控制台检查配置.");
        }
        String url = String.format("http://api.map.baidu.com/geocoder/v2/?"
                + "ak=%s&output=json&address=%s", ak, address);
        URL myURL = null;
        URLConnection httpsConn = null;
        //进行转码
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException("无法生成请求的地址" + e.getMessage(), e);
        }
        try {
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                if ((data = br.readLine()) != null) {
                    if (data.indexOf("\"lat\":") != -1 && data.indexOf("\"lng\":") != -1) {
                        lat = data.substring(data.indexOf("\"lat\":")
                                + ("\"lat\":").length(), data.indexOf("},\"precise\""));
                        lng = data.substring(data.indexOf("\"lng\":")
                                + ("\"lng\":").length(), data.indexOf(",\"lat\""));
                    } else {
                        lng = "404";
                        lat = "404";
                    }
                }
                insr.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("无法获取经度纬度信息，错误信息：" + e.getMessage(), e);
        }
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("lat", new BigDecimal(lat));
        map.put("lng", new BigDecimal(lng));
        return map;
    }

    /**
     * 获取百度ak（控制台配置）<br>
     * 这里完全使用反射的方式穿透到同望接口中，为了不暴露接口<br>
     * 强烈不建议任何人使用这样的方式
     *
     * @return ak
     */
    private String getAK() {
        try {
            IPreferencesManager preferencesManager = VDS.getIntance().getPreferencesManager();
            Field field = preferencesManager.getClass().getDeclaredField("manager");
            field.setAccessible(true);
            Object tooneManager = field.get(preferencesManager);
            Method getPropertyValue = tooneManager.getClass().getMethod("getPropertyValue", String.class, String.class, String.class, String.class);
            Object ak = getPropertyValue.invoke(tooneManager, "com.toone.v3.platform-Webrule_BaiduTrace", "baidutrace", "", "ak");

            return ak.toString();
        } catch (Exception e) {
            throw new ServerFuncException("获取配置信息百度ak配置失败", e);
        }
    }
}
