package cn.com.smart.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.com.smart.dao.impl.BaseDaoImpl;
import cn.com.smart.exception.DaoException;
import cn.com.smart.res.SQLResUtil;
import cn.com.smart.res.sqlmap.SqlMapping;
import cn.com.smart.utils.StringUtil;
import cn.com.smart.web.bean.entity.TNMinWindow;

/**
 * 小窗口管理
 * @author lmq
 *
 */
@Repository("minWinDao")
public class MinWindowDao extends BaseDaoImpl<TNMinWindow>{

	private static final long serialVersionUID = 1L;
	
	private SqlMapping sqlMap;
	private Map<String, Object> params;
	
	public MinWindowDao() {
		sqlMap = SQLResUtil.getBaseSqlMap();
	}
	
	public List<Object> queryItems(String name) throws DaoException {
		List<Object> lists = null;
		String sql = sqlMap.getSQL("min_win_item");
		if(!StringUtil.isEmpty(name)) {
			sql = sqlMap.getSQL("min_win_item_link");
			params = new HashMap<String, Object>();
			params.put("name", "%"+name+"%");
		}
		if(!StringUtil.isEmpty(sql)) {
			lists = queryObjSql(sql,params);
		} else {
			throw new DaoException("SQL语句为空");
		}
		return lists;
	}

}
