package cn.com.smart.web.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.com.smart.dao.impl.BaseDaoImpl;
import cn.com.smart.exception.DaoException;
import cn.com.smart.filter.HandleFilterParam;
import cn.com.smart.filter.bean.FilterParam;
import cn.com.smart.res.SQLResUtil;
import cn.com.smart.res.sqlmap.SqlMapping;
import cn.com.smart.utils.ArrayUtil;
import cn.com.smart.utils.StringUtil;
import cn.com.smart.web.bean.entity.TNOPAuth;
import cn.com.smart.web.bean.entity.TNResource;
import cn.com.smart.web.dao.IResourceDao;

/**
 * 资源DAO
 * @author lmq
 *
 */
@Repository("resourceDao")
public class ResourceDao extends BaseDaoImpl<TNResource> implements IResourceDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6009211225555208446L;
	
	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private OPAuthDao opAuthDao;
	
	private SqlMapping sqlMap;
	private Map<String, Object> params;
	
	public ResourceDao() {
		sqlMap = SQLResUtil.getBaseSqlMap();
	}

	@Override
	public boolean delete(Serializable id) throws DaoException {
		boolean is = false;
		if(null != id && !StringUtil.isEmpty(id.toString())) {
			String[] ids = id.toString().split(",");
			String sqls = sqlMap.getSQL("del_resource");
			if(!StringUtil.isEmpty(sqls)) {
				String[] sqlArray = sqls.split(";");
				params = new HashMap<String, Object>(1);
				params.put("resourceIds", ids);
				int count = 0;
				for (int i = 0; i < sqlArray.length; i++) {
					count += executeSql(sqlArray[i], params);
				}
				is = count>0?true:false;
				params = null;
				sqlArray= null;
			}
			if(is) {
				//删除菜单
				List<Object> menuIds = menuDao.queryMenuIdByResourceId(ids);
				if(null != menuIds && menuIds.size()>0) 
					menuDao.delete(ArrayUtil.arrayToString(menuIds.toArray(), ","));
				menuIds = null;
			}
			ids = null;
		}
		return is;
	}
	
	/**
	 * 
	 * @param searchParam
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<Object> queryObjPage(FilterParam searchParam,int start,int rows) throws DaoException {
		List<Object> lists = null;
		params = new HandleFilterParam(searchParam).getParams();
		String sql = sqlMap.getSQL("res_mgr_list");
		if(!StringUtil.isEmpty(sql)) {
			lists = queryObjSql(sql, params, start, rows);
		}
		searchParam = null;
		return lists;
	}
	
	/**
	 * 统计用户列表--
	 * @param searchParam 搜索参数
	 * @param start
	 * @param rows
	 * @return
	 */
	public long queryObjCount(FilterParam searchParam) throws DaoException {
		long total = 0;
		params = new HandleFilterParam(searchParam).getParams();
		String sql = sqlMap.getSQL("res_mgr_list");
		if(!StringUtil.isEmpty(sql)) {
			total = countSql(sql, params);
		}
		searchParam = null;
		return total;
	}
	
	
	/**
	 * 删除权限
	 * @param values 操作权限值
	 * @return
	 */
	public boolean deleteAuth(String[] values) throws DaoException  {
		boolean is = false;
		if(null != values && values.length>0) {
			List<TNResource> lists = findAll();
			List<TNResource> updateList = new ArrayList<TNResource>();
			String regex = null;
			boolean isDel = false;
			if(null != lists && lists.size()>0) {
				for (TNResource res : lists) {
					isDel = false;
					if(!StringUtil.isEmpty(res.getOpAuths())) {
						for (int i = 0; i < values.length; i++) {
							if(!StringUtil.isEmpty(res.getOpAuths()) 
									&& ArrayUtil.isArrayContains(res.getOpAuths(), values[i], ",")) {
								regex = ","+values[i]+",|"+values[i]+",|,"+values[i];
								res.setOpAuths(res.getOpAuths().replaceAll(regex, ""));
								isDel = true;
							}
						}//for
						if(isDel) {
							if(!StringUtil.isEmpty(res.getOpAuths())) {
							   res.setOpAuths(res.getOpAuths().replaceAll(",,", ""));
							   if(res.getOpAuths().length()>0 && res.getOpAuths().lastIndexOf(",")>-1)
							      res.setOpAuths(res.getOpAuths().substring(0,res.getOpAuths().length()-1));
							}
							updateList.add(res);
						}
					}//if
				}//for
			}
			if(updateList.size()>0) {
				is = update(updateList);
			}
			updateList = null;
		}
		return is;
	}
	
	/**
	 * 更新权限
	 * @param srcValue
	 * @param desValues
	 * @return
	 */
	public boolean updateAuth(String srcValue,String desValue) throws DaoException  {
		boolean is = false;
		if(!StringUtil.isEmpty(srcValue) && !StringUtil.isEmpty(desValue)) {
			List<TNResource> lists = findAll();
			List<TNResource> updateList = new ArrayList<TNResource>();
			if(null != lists && lists.size()>0) {
				for (TNResource res : lists) {
					if(!StringUtil.isEmpty(res.getOpAuths())) {
						if(ArrayUtil.isArrayContains(res.getOpAuths(), srcValue, ",")) {
						 	res.setOpAuths(res.getOpAuths().replaceAll(srcValue+",", desValue+","));
						 	updateList.add(res);
						}
					}//if
				}//for
			}
			if(updateList.size()>0) {
				is = update(updateList);
			}
			updateList = null;
		}
		return is;
	}
	
	
	@Override
	public List<TNResource> queryContainAuths(FilterParam searchParam) throws DaoException  {
		params = new HandleFilterParam(searchParam).getParams();
		String hql = "from "+TNResource.class.getName()+" where state=1 [and t.name like '%:name%'] order by createTime asc";
		List<TNResource> lists = queryHql(hql, params);
		if(null != lists && lists.size()>0) {
			String[] authValueArray = null;
			List<TNOPAuth> auths = null;
			for (TNResource res : lists) {
				if(!StringUtil.isEmpty(res.getOpAuths())) {
					authValueArray = res.getOpAuths().split(",");
					auths = opAuthDao.queryAuths(authValueArray);
					if(null != auths && auths.size()>0) {
						res.setAuths(auths);
					}
				}//if
			}//for
			authValueArray = null;
			auths = null;
		}
		params = null;
		return lists;
	}
}
