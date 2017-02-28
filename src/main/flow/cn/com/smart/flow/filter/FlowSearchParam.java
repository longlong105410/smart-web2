package cn.com.smart.flow.filter;

import cn.com.smart.filter.bean.FilterParam;
import cn.com.smart.utils.StringUtil;

/**
 * 流程搜索对象参数
 * @author lmq
 * @create 2015年6月18日
 * @version 1.0 
 * @since 
 *
 */
public class FlowSearchParam extends FilterParam {

	private String orgId;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Override
	public String getParamToString() {
		StringBuilder paramBuff = new StringBuilder();
		String param = super.getParamToString();
		if(!StringUtil.isEmpty(orgId)) {
			paramBuff.append("orgId="+orgId);
		}
		if(!StringUtil.isEmpty(paramBuff.toString())) {
			param = StringUtil.isEmpty(param)?paramBuff.toString():"&"+paramBuff.toString();
		}
		paramBuff = null;
		return param;
	}
	
	
}
