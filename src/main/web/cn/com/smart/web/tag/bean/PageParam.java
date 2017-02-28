package cn.com.smart.web.tag.bean;

/**
 * 页面参数
 * @author lmq
 *
 */
public class PageParam extends BaseBtn {
	
	private int page;
	
	private String target;
	
	public PageParam(String uri,String target,int page) {
		this.uri = uri;
		this.target = target;
		this.page = page;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
}
