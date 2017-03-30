package cn.com.smart.web.filter.bean;

import org.springframework.stereotype.Component;

import cn.com.smart.filter.bean.FilterParam;
import cn.com.smart.utils.StringUtil;

/**
 * 用户列表搜索类
 * @author lmq
 *
 */
@Component
public class UserSearchParam extends FilterParam {

	private String orgId;
	
	private String info;
	
	public UserSearchParam() {
		super();
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	@Override
	@Deprecated
	public String getParamToString() {
		StringBuilder paramBuff = new StringBuilder();
		String param = super.getParamToString();
		if(!StringUtil.isEmpty(orgId)) {
			paramBuff.append("orgId="+orgId);
		}
		if(!StringUtil.isEmpty(info)) {
			paramBuff.append("&info="+info);
		}
		if(!StringUtil.isEmpty(paramBuff.toString())) {
			param = StringUtil.isEmpty(param)?paramBuff.toString():"&"+paramBuff.toString();
		}
		paramBuff = null;
		return param;
	}
	
}
