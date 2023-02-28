package com.toone.v3.platform.function;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.formula.api.IFormulaEngine;
import com.yindangu.v3.business.jdbc.api.model.IColumn;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.platform.plugin.util.VdsUtils;

/**
 * 列统计函数(TotalColumn、AvgColumn、MaxColumn、MinColumn几个列统计公用工具)<br/>
 * @author jiqj
 *
 */
public class ColumnCalculateUtil {
	private final String functionCode;
	private final IFuncContext context;
	
	private String entityName,columnName;
	private String filters,params;
	
	public ColumnCalculateUtil(String funcCode,IFuncContext ctx) {
		this.functionCode =funcCode;
		context = ctx;
	}
	
	/**
	 * 获取dataView
	 * @param inputIndex 参数下标
	 * @param required
	 * @return
	 */
	public IDataView getDataView(int inputIndex) throws ServerFuncException {
		Object entityname = getInputCheckNull(inputIndex);// (0);
		IDataView data;
		if (entityname instanceof IDataView) {
			data = (IDataView) entityname;
		} 
		else if (entityname instanceof String) {
			entityName = entityname.toString().trim();
			data = (entityName.length()==0 ? null : VDS.getIntance().getFormulaEngine().eval(entityName));
			if (data == null) {
				throw errorMessage(inputIndex, "实体名不存在：" , entityName);
			}
		} 
		else {
			throw errorMessage(inputIndex, "必须是字符串类型或者实体类型:" , entityname.toString());
		}
		return data;
	}
	/**
	 * 取得参数，并且检查null
	 * @param i
	 * @return
	 */
	private Object getInputCheckNull(int i) {
		Object o = context.getInput(i);// (0);
		if (o == null) {
			throw errorMessage(i , "不能为空");
		}
		return o;
	}
	/**
	 * 函数【" + functionCode + "】的第n个参数xxx
	 * @param i
	 * @param msg
	 * @return
	 */
	private ServerFuncException errorMessage(int i,String... msg) {
		StringBuilder sb = getErrorMessage(i,msg);
		return new ServerFuncException( sb.toString());
	}
	/**
	 * 函数【" + functionCode + "】的第n个参数xxx
	 * @param i
	 * @param msg
	 * @return
	 */
	private StringBuilder getErrorMessage(int i,String... msg) {
		StringBuilder sb = new StringBuilder();
		if(i>=0) {
			sb.append("函数【" ).append(functionCode).append( "】的第").append(i+1)
				.append("个参数");
		}
		else {
			sb.append("函数【" ).append(functionCode).append( "】");
		}
		for(String s : msg) {
			sb.append(s);
		}
		return sb;
	}
	/**
	 * 获取dataView
	 * @param inputIndex 参数下标
	 * @param required
	 * @return
	 */
	public IColumn getColumnByDataView(int inputIndex, IDataView dataView) throws SQLException {
		columnName = getInputCheckNull(inputIndex).toString();// (0);
		if (dataView == null) {
			throw errorMessage(-1,"实体为空");
		}
		IColumn column = dataView.getMetadata().getColumn(columnName);
		if (column == null) {
			throw errorMessage(-1,"实体字段名不存在：" , columnName);
		}
		return column;
	}
	
	/**
	 * 返回如果没有参数，返回空字符串
	 * @return
	 */
	public String getFilters(int inputIndex){
		if(filters!=null) {
			return filters;
		}
		
		Object filter = context.getInput(inputIndex);
		filters =(filter == null ?"":filter.toString().trim()); 
		return filters;
	}
	
	/**
     * 取得参数，并且通过表达式引擎获取实际的值
     * @param context
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> getFilterParams(int inputIndex){
    	Object ps = context.getVObject().getInput(inputIndex);
    	Map<String,Object> pars;
    	if(ps == null) {
    		pars = null;
    	}
    	else if(ps instanceof String){ 
    		//开发系统输入: {\"m1\":\"01\",\"m2\":\"02\"}
    		//运行时也是得到这个样的字符串 {\"m1\":\"01\",\"m2\":\"02\"}
    		//所以需要编码base64
			String json = ((String)ps).trim();
			if(json.length() ==0) {
				pars = Collections.emptyMap();
			}
			else if(json.indexOf('{')>=0) {
				throw errorMessage(inputIndex,"的条件参数必须是json字符串必须是base64编码");
			}
			else {
				byte[] bys = VdsUtils.crypto.decodeBase64(json, StandardCharsets.UTF_8.name());
				String js = new String(bys,StandardCharsets.UTF_8);
				params = js;
				pars = VdsUtils.json.fromJson(js);	
			} 
    	}
    	else if(ps instanceof Map) {
    		pars = (Map)ps;
    		params="map";
    	}
    	else {
    		throw errorMessage(inputIndex,"的条件参数必须是字符串/或者map类型");
    	}
    	if(pars == null || pars.isEmpty()) {
    		return Collections.emptyMap();
    	}
    	
    	Map<String,Object> rs = new HashMap(pars.size());
    	IFormulaEngine eng =VDS.getIntance().getFormulaEngine();
    	for(Entry<String, Object> e : pars.entrySet()) {
    		Object v = eng.eval(e.getValue().toString());
    		rs.put(e.getKey(), v);
    	}
    	
    	return rs;
    }
    
    public String getFullParams(String title) {
    	StringBuilder sb = getErrorMessage(-1, title 
    			,",参数1",entityName
    			,",参数2",columnName
    			,",参数3",filters
    			,",参数4",params
    			);
    	return sb.toString();
    	//log.error("函数【" + funcCode + "】计算失败，参数1：" + entityname + "，参数2：" + columnname, e);
    	
    }

}
