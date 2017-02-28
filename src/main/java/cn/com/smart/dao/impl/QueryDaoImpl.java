package cn.com.smart.dao.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

import cn.com.smart.bean.BaseBean;
import cn.com.smart.dao.IQueryDao;
import cn.com.smart.exception.DaoException;
import cn.com.smart.utils.StringUtil;

/**
 * 查询Dao实现类
 * @author lmq
 * @version 1.0
 * @since JDK版本大于等于1.6
 * 
 * 2015年8月22日
 * @param <T>
 */
public abstract class QueryDaoImpl<T extends BaseBean> extends SuperDao<T> implements IQueryDao<T>{
	
	private static final Logger log = Logger.getLogger(QueryDaoImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public T find(Serializable id) throws DaoException {
		T t = null;
		if(null == id || StringUtil.isEmpty(id.toString())) {
	    	return t;
	    }
		log.info("通过主键ID["+id+"]查询数据");
		try {
			 t = (T) getSession().get(clazz, id);
		    log.info("通过主键ID["+id+"]查询数据[成功]");
		} catch (Exception e) {
			log.info("通过主键ID["+id+"]查询数据[失败]");
			e.printStackTrace();
			t = null;
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E find(Class<E> claszp,Serializable id) throws DaoException {
		E t = null;
		if(null == claszp || null == id || StringUtil.isEmpty(id.toString())) {
	    	return t;
	    }
		log.info("通过主键ID["+id+"]查询数据");
		try {
			 t = (E) getSession().get(claszp, id);
		    log.info("通过主键ID["+id+"]查询数据[成功]");
		} catch (Exception e) {
			log.info("通过主键ID["+id+"]查询数据[失败]");
			e.printStackTrace();
			t = null;
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		}
		return t;
	}
	
	@Override
	public List<T> find(Serializable[] idArray) throws DaoException {
		List<T> lists = null;
		try {
			if(null == idArray || idArray.length<1) {
		    	return lists;
		    }
			log.info("多个主键批量查询数据");
			if(idArray.length>0) {
				String hql = " from "+clazz.getName()+" where id in (:idArray)";
				Map<String, Object> param = new HashMap<String, Object>(1);
				param.put("idArray", idArray);
				lists = queryHql(hql,param);
				param = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		}
		return lists;
	}
	
	@Override
	public List<Object> find(Class<?> claszp,Serializable[] idArray) throws DaoException {
		List<Object> lists = null;
		try {
			if(null == claszp || null == idArray || idArray.length<1) {
		    	return lists;
		    }
			log.info("多个主键批量查询数据");
			if(idArray.length>0) {
				String hql = " from "+claszp.getName()+" where id in (:idArray)";
				Map<String, Object> param = new HashMap<String, Object>(1);
				param.put("idArray", idArray);
				lists = queryObjHql(hql,param);
				param = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		}
		return lists;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T find(String hql, Map<String, Object> param) throws DaoException {
		T t = null;
		if(StringUtil.isEmpty(hql)) {
	    	return null;
	    }
		try {
			Query query = getQuery(hql, param, false);
			t = (T) query.uniqueResult();
			log.info("查询数据HQL["+hql+"]--成功--");
		} catch (Exception e) {
			log.info("查询数据HQL["+hql+"]--失败--");
			e.printStackTrace();
			t = null;
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		} finally {
			param = null;
		}
		return t;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() throws DaoException {
		List<T> lists = null;
		log.info("获取表中的所有数据");
		try {
			lists = (List<T >) getQuery(" from "+clazz.getName(),false).list();
		    log.info("获取表中的所有数据[成功]");
		} catch (Exception e) {
			log.info("获取表中的所有数据[失败]");
			e.printStackTrace();
			lists = null;
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		}
		return lists;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll(Class<?> claszp) throws DaoException {
		List<T> lists = null;
		if(null == claszp) {
	    	return lists;
	    }
		log.info("获取表中的所有数据");
		try {
			lists = (List<T>) getQuery(" from "+claszp.getName(),false).list();
		    log.info("获取表中的所有数据[成功]");
		} catch (Exception e) {
			log.info("获取表中的所有数据[失败]");
			e.printStackTrace();
			lists = null;
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		}
		return lists;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> findObjAll(Class<?> claszp) throws DaoException {
		List<Object> lists = null;
		if(null == claszp) {
	    	return lists;
	    }
		log.info("获取表中的所有数据");
		try {
			lists = (List<Object>) getQuery(" from "+claszp.getName(), false).list();
		    log.info("获取表中的所有数据[成功]");
		} catch (Exception e) {
			log.info("获取表中的所有数据[失败]");
			e.printStackTrace();
			lists = null;
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		}
		return lists;
	}
	
	@Override
	public List<T> queryByField(Map<String, Object> param) throws DaoException {
		List<T> list = null;
		String hql = combinHQL(param);
		log.info("通过HQL查询数据["+hql+"]");
		list = queryHql(hql, param);
		hql = null;
		param = null;
		return list;
	}
	
	@Override
	public List<T> queryByField(Map<String, Object> param,String orderBy) throws DaoException {
		List<T> list = null;
		String hql = combinHQL(param);
		if(!StringUtil.isEmpty(orderBy)) {
			hql += " order by "+orderBy;
		}
		log.info("通过HQL查询数据["+hql+"]");
		list = queryHql(hql, param);
		hql = null;
		param = null;
		return list;
	}
	
	@Override
	public List<T> queryByField(Map<String, Object> param, int start, int rows,String orderBy) throws DaoException {
		List<T> list = null;
		String hql = combinHQL(param);
		if(!StringUtil.isEmpty(orderBy)) {
			hql += " order by "+orderBy;
		}
		log.info("通过HQL查询数据["+hql+"]");
		list = queryHql(hql, param, start, rows);
		hql = null;
		param = null;
		return list;
	}
	
	@Override
	public List<T> queryHql(String hql) throws DaoException {
		return queryHql(hql, null);
	}
	
	@Override
	public List<T> queryHql(String hql, Map<String, Object> param) throws DaoException {
		return queryHql(hql, param, null, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> queryHql(String hql,Map<String, Object> param,Integer start,Integer rows) throws DaoException {
		List<T> list = null;
		if(StringUtil.isEmpty(hql)) {
	    	return list;
	    }
		try {
			Query query = getQuery(hql, param, false);
			if(null != start && null != rows) {
				query.setFirstResult(start);
				query.setMaxResults(rows);
			}
			list = query.list();
			log.info("通过HQL查询数据[成功]");
		} catch (Exception e) {
			log.info("通过HQL查询数据[失败]");
			e.printStackTrace();
			list = null;
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		} finally {
			param = null;
		}
		return list;
	}
	
	@Override
	public List<Object> queryObjHql(String hql) throws DaoException {
		return queryObjHql(hql,null);
	}
	
	@Override
	public List<Object> queryObjHql(String hql, Map<String, Object> param) throws DaoException {
		return queryObjHql(hql, param, null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> queryObjHql(String hql,Map<String, Object> param,Integer start,Integer rows) throws DaoException {
		List<Object> list = null;
		if(StringUtil.isEmpty(hql)) {
	    	return list;
	    }
		try {
			Query query = getQuery(hql, param, false);
			if(null != start && null != rows) {
				query.setFirstResult(start);
				query.setMaxResults(rows);
			}
			list = query.list();
			log.info("通过HQL查询数据[成功]");
		} catch (Exception e) {
			log.info("通过HQL查询数据[失败]");
			e.printStackTrace();
			list = null;
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		} finally {
			param = null;
		}
		return list;
	}
	
	@Override
	public List<T> querySql(String sql) throws DaoException {
		return querySql(sql, null);
	}

	@Override
	public List<T> querySql(String sql, Map<String, Object> param) throws DaoException {
		return querySql(sql, param, null, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> querySql(String sql, Map<String, Object> param,Integer start, Integer rows) throws DaoException {
		List<T> list = null;
		if(StringUtil.isEmpty(sql)) {
	    	return null;
	    }
		try {
			SQLQuery query = (SQLQuery)getQuery(sql, param, true);
			if(null != start && null != rows) {
				query.setFirstResult(start);
				query.setMaxResults(rows);
			}
			query.addEntity(clazz);
			list = query.list();
			log.info("通过SQL查询数据[成功]");
		} catch (Exception e) {
			log.info("通过SQL查询数据[失败]");
			e.printStackTrace();
			list = null;
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		} finally {
			param = null;
		}
		return list;
	}

	@Override
	public Long count(String hql) throws DaoException {
		return count(hql, null);
	}
	
	@Override
	public Long count(Map<String,Object> param) throws DaoException {
		String hql = combinHQL(param);
		return count(hql,param);
	}
	
	@Override
	public Long count(String hql, Map<String, Object> param) throws DaoException {
		long total = 0;
		if(StringUtil.isEmpty(hql)) {
	    	return total;
	    }
		if(!StringUtil.isEmpty(hql)) {
			hql = "select count(*) "+hql;
		}
		try {
			Query query = getQuery(hql, param, false);
			Object obj = query.uniqueResult();
			total = Long.parseLong(obj.toString());
		} catch (Exception e) {
			log.info("统计数据HQL["+hql+"]--[异常]--["+e.getMessage()+"]");
			e.printStackTrace();
			throw new DaoException(e.getLocalizedMessage(), e.getCause());
		} finally {
			param = null;
		}
		return total;
	}
	
	/**
	 * 组合HQL语句
	 * @param param 参数
	 * @return 返回组合后的HQL语句
	 */
	private String combinHQL(Map<String,Object> param) {
		StringBuilder hqlBuilder = new StringBuilder();
		hqlBuilder.append("from "+clazz.getName()+" ");
		if(null != param && param.size()>0) {
			hqlBuilder.append(" where ");
			int count = 0;
			for (String key : param.keySet()) {
				if(count > 0) {
					hqlBuilder.append(" and ");
				}
				if(null != param.get(key) && param.get(key).getClass().isArray()) {
					hqlBuilder.append(key+" in (:"+key+")");
				} else {
					hqlBuilder.append(key+"=:"+key);
				}
				count++;
			}
		}//end if
		return hqlBuilder.toString();
	}
}
