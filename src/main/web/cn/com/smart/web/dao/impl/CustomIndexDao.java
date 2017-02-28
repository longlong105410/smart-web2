package cn.com.smart.web.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.com.smart.dao.impl.BaseDaoImpl;
import cn.com.smart.exception.DaoException;
import cn.com.smart.res.SQLResUtil;
import cn.com.smart.res.sqlmap.SqlMapping;
import cn.com.smart.utils.StringUtil;
import cn.com.smart.web.bean.entity.TNCustomIndex;

/**
 * 自定义首页
 * @author lmq
 *
 */
@Repository
public class CustomIndexDao extends BaseDaoImpl<TNCustomIndex> {

	private static final long serialVersionUID = 1L;
	
	private SqlMapping sqlMap;
    private Map<String, Object> params;
	
	public CustomIndexDao() {
		sqlMap = SQLResUtil.getBaseSqlMap();
	}
	
	/**
	 * 获取用户首页布局
	 * @param userId
	 * @return
	 */
	public TNCustomIndex queryIndexLayout(String userId) throws DaoException  {
		TNCustomIndex cusIndex = null;
		if(!StringUtil.isEmpty(userId)) {
			String hql = sqlMap.getSQL("user_index_layout");
			if(!StringUtil.isEmpty(hql)) {
				params = new HashMap<String, Object>();
				params.put("userId", userId);
				List<TNCustomIndex> lists = queryHql(hql, params);
				if(null != lists && lists.size()>0) {
					cusIndex = lists.get(0);
				}
				lists = null;
			} else {
				throw new DaoException("[user_index_layout]值为空");
			}
			params= null;
		}
		return cusIndex;
	}

	@Override
	public boolean delete(Serializable id) throws DaoException {
		boolean is = false;
		if(null != id && !StringUtil.isEmpty(id.toString())) {
			String sql = sqlMap.getSQL("del_cus_index");
			if(!StringUtil.isEmpty(sql)) {
				String[] sqls = sql.split(";");
				params = new HashMap<String, Object>(1);
				params.put("cusIndexId", id.toString());
				for (int i = 0; i < sqls.length; i++) {
					executeSql(sqls[i], params);
				}
				is = true;
				params = null;
			} else {
				throw new DaoException("[del_cus_index]值为空");
			}
		}
		return is;
	}
}
