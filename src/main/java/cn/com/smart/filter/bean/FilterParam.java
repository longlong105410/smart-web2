package cn.com.smart.filter.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.stereotype.Component;

import com.mixsmart.utils.StringUtils;

/**
 * 过滤参数
 * @author lmq
 *
 */
@Component
public class FilterParam implements IFilterParam {

	protected String id;
	
	protected String name;
	
	protected String state;
	
	protected String type;
	
	//过滤角色用
	protected String[] roleIds;
	
	//过滤数据权限用
	protected String[] orgIds;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		try {
			this.name = URLDecoder.decode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
	/**
	 * 参数转化为字符串 
	 * @see FilterParam#getParamToString()
	 * @param varName
	 * @return 返回转化结果
	 */
	@Deprecated
	public String getParamToString(String varName) {
		StringBuilder paramBuff = new StringBuilder();
		varName = StringUtils.handNull(varName);
		if(StringUtils.isNotEmpty(varName)) {
			varName +=".";
		}
		if(StringUtils.isNotEmpty(id)) {
			paramBuff.append(varName+"id="+id);
		}
		if(StringUtils.isNotEmpty(name)) {
			paramBuff.append("&"+varName+"name="+name);
		}
		if(StringUtils.isNotEmpty(state)) {
			paramBuff.append("&"+varName+"state="+state);
		}
		if(StringUtils.isNotEmpty(type)) {
			paramBuff.append("&"+varName+"type="+type);
		}
		String paramStr = paramBuff.toString();
		if(StringUtils.isNotEmpty(paramStr) && paramStr.startsWith("&")) {
			paramStr = paramStr.substring(1);
		}
		return paramStr;
	}
	
	/**
	 * 参数转化为字符串
	 * 注：页面分页时，不用改方法也支持根据条件分页
	 * @return 返回转化结果
	 */
	@Deprecated
	public String getParamToString() {
		return getParamToString(null);
	}

	public String[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}

	public String[] getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String[] orgIds) {
		this.orgIds = orgIds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

