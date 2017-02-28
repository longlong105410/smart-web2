package cn.com.smart.flow.bean;

import java.util.List;

/**
 * 查询已经保存的表单数据信息
 * @author lmq
 * @create 2015年6月6日
 * @version 1.0 
 * @since 
 *
 */
public class QueryFormData {

	private String name;
	
	private Object value;
	
	private int valueSize;
	
	private List<QueryFormData> nameMoreValues;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public List<QueryFormData> getNameMoreValues() {
		return nameMoreValues;
	}

	public void setNameMoreValues(List<QueryFormData> nameMoreValues) {
		this.nameMoreValues = nameMoreValues;
	}

	public int getValueSize() {
		return valueSize;
	}

	public void setValueSize(int valueSize) {
		this.valueSize = valueSize;
	}
	
	
}
