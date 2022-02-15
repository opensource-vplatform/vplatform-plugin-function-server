package com.toone.v3.platform.function;

//import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.metadata.api.IDataObject;
//import com.yindangu.v3.business.metadata.api.IDataRows;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
//import com.yindangu.v3.platform.plugin.util.VdsCollectionUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 将实体某个字段拼接成一个字符串返回<br>
 * <br>
 * 代码示例:ListToStringFunc("BR_IN_PARENT.aa","code",",",false)返回值为拼接后的字符串。<br>
 * 参数1--实体名称（字符串类型）；<br>
 * 参数2--字段名称（字符串类型）；<br>
 * 参数3--拼接时的分隔符（字符串类型）；<br>
 * 参数4--是否去重复（布尔类型），可省略，默认为false，如果参数=true，则去除重复记录。<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/6/16 10:27
 */
public class ListToStringFunc implements IFunction {

    // 函数编码
    private final static String funcCode = ListToStringRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(ListToStringFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        try {
            int size = context.getInputSize();
            if (size < 3) {
                throw new ServerFuncException("函数【ListToStringFunc】至少需要3个参数，当前参数个数：" + size);
            }
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            param3 = context.getInput(2);
            // 实体名必须是带前缀的表达式
            String dataViewName = (String) param1;
            String columnName = (String) param2;
            String separator = (String) param3;
            boolean isUnique = false;
            if (size == 4) {
                Object o = context.getInput(3);
                if (o instanceof Boolean) {
                    isUnique = (Boolean) o;
                } else if (o instanceof String) {
                    isUnique = Boolean.parseBoolean((String) o);
                }
            }

            Object value = VDS.getIntance().getFormulaEngine().eval(dataViewName);
            String  retStr;
            if (value == null) {
                throw new ServerFuncException("函数【" + funcCode + "】在上下文中未找到指定对象：name=" + dataViewName);
            } else if (!(value instanceof IDataView)) {
                throw new ServerFuncException("函数【" + funcCode + "】指定名称的对象不是合法的内存表类型：name=" + dataViewName + ", 类型="
                        + value.getClass().getSimpleName());
            }

            IDataView dataview = (IDataView) value;
            if(false && columnName.startsWith("jiqjtest")) {//测试性能代码
            	long startTime = System.currentTimeMillis();
	            IDataView dv = VDS.getIntance().getDas().find("SELECT ID ,ORGNO ,ORGNAME ,ENABLEFLAG ,REMARK ,ADDRESS , " + 
	            		"	TELEPHONE ,ORGCHARGE ,FAX ,DEPTTYPENO ,POSTCODE ,EMAIL , " + 
	            		"	QQ ,INITIALS ,ORGNO as code " + 
	            		"FROM vbase_organization.V_SYS_ORG");
	            long endTime = System.currentTimeMillis();
	            log.info("加载数据库18310记录耗时:" + (endTime - startTime));
	            
	            String code ="code";
	            if("jiqjtest-v1".equals(columnName)) {
	            	retStr = executeListToStringV1(dv, code, separator, isUnique);
	            }
	            else if("jiqjtest-v1b".equals(columnName)) {
	            	retStr = executeListToStringV1b(dv, code, separator, isUnique);
	            }
	            else {
	            	retStr = executeListToStringV2(dv, code, separator, isUnique);
	           }
            }
            else {
            	retStr = executeListToStringV1b(dataview, columnName, separator, isUnique);
            }
            outputVo.put(retStr);
            outputVo.setSuccess(true);
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
    /**
     * 列转字符串处理(v2版本-2021.10.27版)
     * @param dv
     * @param columnName 列名
     * @param separator 分隔符
     * @param isUnique 是否唯一
     * @return
     */
    private String executeListToStringV2(IDataView dv,String columnName,String separator,boolean isUnique) {
    	/*long startTime = System.currentTimeMillis();
    	IDataRows rows = dv.getDataRows();
    	int idx = rows.findColumn(columnName);
    	if(idx ==-1) {
    		throw new ServerFuncException("函数【" + funcCode + "】计算失败，表中不存列表名" + columnName + "的列");
    	}

        long startTime2 = System.currentTimeMillis();
    	Collection<String> rds = (isUnique? new HashSet<>(): new ArrayList<>());
    	int size =0;
    	while(rows.next()) {
    		Object o = rows.getObject(idx);
    		String s ;
            if (o != null && (s = o.toString()).length()>0) {
            	rds.add(s);
            }
            size ++;
    	}
    	
    	String result = "";
    	if(rds.size()>0){
    		int splen = separator.length();
    		StringBuilder sb = new StringBuilder();
    		for(String s : rds) {
    			if(splen>0){
    				sb.append(separator);
    			}
    			sb.append(s);
    		}
    		result =(splen >0 ? sb.substring(splen) : sb.toString());
    	}
    	
        long endTime = System.currentTimeMillis();
        log.info("列转字符串处理(v2版本-2021.10.27版)耗时:" + (endTime - startTime)+ ",创建对象："+ (startTime2 - startTime)+",recordSize=" + size);
        return result;*/
    	return "遍历性能比 {@link #select()}只高 2-5% ,暂时不开放";
    }
    /**
     * 列转字符串处理(v1版本-toone版)
     * @deprecated 字符串 相加 严重影响性能 IDataView 的记录数为2w，并200次，CPU 100%，访问无响应
     * @param dv
     * @param columnName 列名
     * @param separator 分隔符
     * @param isUnique 是否唯一
     * @return
     */
    private String executeListToStringV1( IDataView dv,String columnName,String separator,boolean isUnique) {
    	long startTime = System.currentTimeMillis();
    	
    	List<Map<String, Object>> records = dv.getDatas();
    	long startTime2 = System.currentTimeMillis();
    	
    	int size =( records== null ? 0 :  records.size());
        String retStr = "";
        if (size > 0) {
            List<Object> list = new ArrayList<Object>();
            for (int i = 0; i < records.size(); i++) {
                Map<String, Object> map = records.get(i);
                if (!map.containsKey(columnName)) {
                    throw new ServerFuncException("函数【" + funcCode + "】计算失败，表中不存列表名" + columnName + "的列");
                } else {
                    Object o = map.get(columnName);
                    if (o != null) {
                        if (isUnique && !list.contains(o)) {
                            list.add(o);
                            String temValue = o.toString();
                            if (!temValue.equals("")) {
                                if (retStr == "") {
                                    retStr = temValue;
                                } else {
                                    retStr = retStr + separator + temValue;
                                }
                            }
                        } else if (!isUnique) {
                            String temValue = o.toString();
                            if (!temValue.equals("")) {
                                if (retStr == "") {
                                    retStr = temValue;
                                } else {
                                    retStr = retStr + separator + temValue;
                                }
                            }
                        }
                    }
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("列转字符串处理(v1版本-toone版)耗时:" + (endTime - startTime) + ",其中转换map时间(" + (startTime2-startTime) +"),recordSize=" + size);
        return retStr.toString();
    }
    /**
     * 列转字符串处理(v1版本-toone版)
     * @param dv
     * @param columnName 列名
     * @param separator 分隔符
     * @param isUnique 是否唯一
     * @return
     */
    private String executeListToStringV1b( IDataView dv,String columnName,String separator,boolean isUnique) {
    	long startTime = System.currentTimeMillis();
    	int size=0;
    	try {
    		List<IDataObject> records = dv.select();
    	    	
	    	size =( records== null ? 0 :  records.size());
	    	String retStr = "";
	    	if(size ==0) {
	    		return retStr;
	    	}
        
	    	Collection<String> rds = (isUnique? new HashSet<>(): new ArrayList<>()); 
	    	for(IDataObject rows : records) {
	    		Object o = rows.get(columnName);
	    		String s ;
	            if (o != null && (s = o.toString()).length()>0) {
	            	rds.add(s);
	            }
	            //size ++;
	    	}
	    	 
	    	if(rds.size()>0){
	    		int splen = separator.length();
	    		StringBuilder sb = new StringBuilder();
	    		for(String s : rds) {
	    			if(splen>0){
	    				sb.append(separator);
	    			}
	    			sb.append(s);
	    		}
	    		retStr =(splen >0 ? sb.substring(splen) : sb.toString());
	    	}
    		return retStr;
    	}
    	finally {
	        long endTime = System.currentTimeMillis();
	        log.info("列转字符串处理(v1b版本-toone版)耗时:" + (endTime - startTime) + ",recordSize=" + size);
    	}
    }
}
