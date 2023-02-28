package com.toone.v3.platform.function;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.jdbc.api.model.ColumnType;
import com.yindangu.v3.business.jdbc.api.model.IColumn;
import com.yindangu.v3.business.metadata.api.IDataObject;
import com.yindangu.v3.business.metadata.api.IDataView;
import com.yindangu.v3.platform.plugin.util.VdsUtils;

/**
 * 列统计函数(TotalColumn、AvgColumn、MaxColumn、MinColumn几个列统计公用计算)<br/>
 * 几个项目的这个类请保持一致（可以互相复制）<br/>
 * AvgColumnFunc目录的为最原始
 * @author jiqj
 *
 */
public class ColumnCalculateBuilder {
	public static class Build{
		private String funcCode;
		private IColumn column;
		private IDataView dataView;

		private String filters;
		private Map<String,Object> params;
		
		private List<IDataObject> dataList;
		private boolean integerField;
		
		public String getFuncCode() {
			return funcCode;
		}
		public Build setFuncCode(String funcCode) {
			this.funcCode = funcCode;
			return this;
		}
		public IColumn getColumn() {
			return column;
		}
		public Build setColumn(IColumn column) {
			this.column = column;
			return this;
		}
		
		public ColumnCalculateBuilder build() {
			if(funcCode == null || column == null || dataView == null) {
				throw new ServerFuncException("函数名、统计列、实体不能为空");
			}
			
			if(VdsUtils.string.isEmpty(filters)) {
				dataList = dataView.select();
			}
			else if(params != null) {
				dataList = dataView.select(filters,params);
			}
			else {
				throw new ServerFuncException("二次过滤有条件时，参数对象不能为空，如果无条件，请传入Collections.emptyMap()");
			}
			
			integerField =(column.getColumnType() == ColumnType.Integer || column.getPrecision() ==0);
			return new ColumnCalculateBuilder(this);
		}
		public List<IDataObject> getDataList() {
			return dataList;
		}
		public Build setDataView(IDataView dataView) {
			this.dataView = dataView;
			return this;
		}
		public String getFilters() {
			return filters;
		}
		public Build setFilters(String filters) {
			this.filters = filters;
			return this;
		}
		public Map<String, Object> getParams() {
			return params;
		}
		public Build setParams(Map<String, Object> params) {
			this.params = params;
			return this;
		}
		/**是否整数字段,或者小数位为0的字段*/
		public boolean isIntegerField() {
			return integerField;
		}

	}
	public static Build newBuild(String functionCode) {
		Build b = new Build();
		return b.setFuncCode(functionCode);
	}
	private final Build build;
	private ColumnCalculateBuilder(Build builder) {
		build =builder;
	}

	/**
	 * 求和 
	 * @return Long/BigDecimal
	 */
	public Number total()  {
		return total0();
	}
	/**
	 * 平均值
	 * @return Long/BigDecimal
	 */
	public Number avg()  {
		List<IDataObject> mapList = build.getDataList();// data.select();
		int size = mapList.size();
		if(size ==0) {
			return Integer.valueOf(0);
		}
		
		Number total = this.total0();
		BigDecimal divisor = new BigDecimal(size);
		BigDecimal avg;
		
		if(build.isIntegerField()) {
			BigDecimal totalDec = new BigDecimal(total.longValue());
			avg = totalDec.divide(divisor); //除法运算
		}
		else {
			BigDecimal totalDec =(BigDecimal)total;
			avg = roundScale(totalDec.divide(divisor)); //除法运算
		}
		return avg; 
	}
	/**
	 * 平均值
	 * @return Long/BigDecimal
	 */
	public Number max()  {
		List<IDataObject> mapList = build.getDataList();// data.select();
		String columnName = build.getColumn().getColumnName();
		BigDecimal maxDec = BigDecimal.valueOf(0);
		long max = 0;
		boolean integerField =(build.isIntegerField()) ;
		
		for (IDataObject map : mapList) {
			Object value = map.get(columnName);
			if(integerField) {
				long n = convertLong(value);
				if(n > max) {
					max = n;
				}
			}
			else {
				BigDecimal n = convertDecimal(value);
				if(n.compareTo(maxDec)>0) {
					maxDec = n;
				}
			}
		}
		return (integerField ? Long.valueOf(max) : maxDec);
	}
	public Number min()  {
		List<IDataObject> mapList = build.getDataList();// data.select();
		String columnName = build.getColumn().getColumnName();
		BigDecimal minDec = BigDecimal.valueOf(0);
		long min = 0;
		boolean integerField =(build.isIntegerField()) ;
		
		for (IDataObject map : mapList) {
			Object value = map.get(columnName);
			if(integerField) {
				long n = convertLong(value);
				if(n < min) {
					min  = n;
				}
			}
			else {
				BigDecimal n = convertDecimal(value);
				if(n.compareTo(minDec) < 0) {
					minDec = n;
				}
			}
		}
		return (integerField ? Long.valueOf(min) : minDec);
	}
	
	private Number total0() {
		List<IDataObject> mapList = build.getDataList();// data.select();
		if(mapList.isEmpty()) {
			return Integer.valueOf(0);
		}
		boolean integerField =(build.isIntegerField()) ;
		String columnName = build.getColumn().getColumnName();
		BigDecimal totalDec = new BigDecimal(0);
		long totalLng = 0;
		
		for (IDataObject map : mapList) {
			Object value = map.get(columnName);
			if (value == null) {
				continue;//
			}
			else if(integerField) { //整数运算
				totalLng += convertLong(value);
			}
			else {
				BigDecimal augend = convertDecimal(value); 
				totalDec = totalDec.add(augend); //加法运算
			}
		}
		
		if(integerField) {
			return Long.valueOf(totalLng);
		}
		else {
			return roundScale(totalDec);//小数位
		}
	}

 
	private BigDecimal convertDecimal(Object value) { 
		BigDecimal n  ;
		if(value == null) {
			n = BigDecimal.valueOf(0);
		}
		else if(value instanceof BigDecimal) {
			n = (BigDecimal) value;
		}
		else if (value instanceof Number) {
			// if(value instanceof Double || value instanceof Integer || value instanceof
			// java.math.BigDecimal){
			n = new BigDecimal(((Number) value).doubleValue());
		} else {
			throw new ServerFuncException("函数【" + build.getFuncCode() 
				+ "】的统计字段不是数值类型："  
				+ build.getColumn().getColumnName());
		}
		return n; 
	}
	private long convertLong(Object value) {
		if(value == null) {
			return 0;
		}
		else if (value instanceof Number) {
			Number n = (Number)value;
			return n.longValue();
		} else {
			throw new ServerFuncException("函数【" + build.getFuncCode() 
					+ "】的统计字段不是数值类型："  
					+ build.getColumn().getColumnName());
		}
	}
 
	private BigDecimal roundScale(BigDecimal n) {
		if(n.equals(BigDecimal.ZERO) || n.compareTo(BigDecimal.ZERO) ==0) {
			return n;
		}
		int precision = build.getColumn().getPrecision();// 精度位数
		BigDecimal dec = n.setScale(precision, BigDecimal.ROUND_HALF_UP);
		return dec;
	}

}
