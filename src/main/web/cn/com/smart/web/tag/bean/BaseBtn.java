package cn.com.smart.web.tag.bean;

/**
 * 操作按钮基类
 * @author lmq
 *
 */
public class BaseBtn {

	protected String id;
	
	/**
	 * 序号
	 */
	protected Integer sort;
	
	protected String uri;
	
	protected String busi;
	
	protected String btnStyle="btn-default";
	
	protected String name;
	
	protected String selectedType;
	
	public BaseBtn() {
		
	}
	
	public BaseBtn(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getBusi() {
		return busi;
	}

	public void setBusi(String busi) {
		this.busi = busi;
	}

	public String getBtnStyle() {
		return btnStyle;
	}

	public void setBtnStyle(String btnStyle) {
		this.btnStyle = btnStyle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}
