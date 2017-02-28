package cn.com.smart.web.plugins;

import cn.com.smart.utils.StringUtil;

/**
 * ZTree插件---组织机构与岗位树形数据结构类
 * @author lmq
 *
 */
public class OrgPositionZTreeData extends ZTreeData {

	@Override
	public String getIconSkin() {
		if(!isParent && !"position".equals(flag)) {
			iconSkin = "org-leaf";
		} else if("position".equals(flag)) {
			iconSkin = "position";
		}
		return super.getIconSkin();
	}
	
	@Override
	public Boolean getNocheck() {
		nocheck = true;
		if(!StringUtil.isEmpty(checkFlag) && checkFlag.equals(flag)) {
			nocheck = false;
		}
		return super.getNocheck();
	}
	
}
