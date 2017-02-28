package cn.com.smart.web.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.smart.dao.impl.BaseDaoImpl;
import cn.com.smart.exception.DaoException;
import cn.com.smart.res.SQLResUtil;
import cn.com.smart.res.sqlmap.SqlMapping;
import cn.com.smart.utils.StringUtil;
import cn.com.smart.web.bean.entity.TNUserSetMenu;

/**
 * 
 * @author lmq
 *
 */
@Repository("userSetMenuDao")
public class UserSetMenuDao extends BaseDaoImpl<TNUserSetMenu>{

	private static final long serialVersionUID = 1L;
	
	private SqlMapping sqlMap;
	
	public UserSetMenuDao() {
		sqlMap = SQLResUtil.getBaseSqlMap();
	}

	/**
	 * 个人设置菜单关联查询
	 * @return
	 */
	public List<Object> queryRelated() throws DaoException {
		List<Object> lists = null;
		String sql = sqlMap.getSQL("user_setting_list");
		if(!StringUtil.isEmpty(sql)) {
			lists = queryObjSql(sql);
		}
		return lists;
		
	}
	
	/**
	 * 个人设置菜单关联查询
	 * @return
	 */
	public List<TNUserSetMenu> queryVaidMenu() throws DaoException {
		List<TNUserSetMenu> lists = null;
		String hql = sqlMap.getSQL("user_setting_valid_menu");
		if(!StringUtil.isEmpty(hql)) {
			lists = queryHql(hql);
		}
		return lists;
		
	}
} 
