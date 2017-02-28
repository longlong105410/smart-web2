package cn.com.smart.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.smart.bean.BaseBean;
import cn.com.smart.dao.impl.BaseDaoImpl;
import cn.com.smart.exception.DaoException;
import cn.com.smart.utils.StringUtil;
import cn.com.smart.web.bean.entity.TNDict;
import cn.com.smart.web.cache.impl.DictMemoryCache;
import cn.com.smart.web.dao.IRalteDictDao;

/**
 * 关联数据字段实现类
 * @author lmq
 * @create 2015年6月29日
 * @version 1.0 
 * @since 
 *
 */
public class RalteDictDaoImpl<T extends BaseBean> extends BaseDaoImpl<T> implements IRalteDictDao<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8855376211496019849L;
	
	@Autowired
	private DictMemoryCache dictCache;

	@Override
	public void relateDict(List<T> ts, String busiValue) throws DaoException {
		if(null != ts && ts.size()>0 && !StringUtil.isEmpty(busiValue)) {
			List<TNDict> dicts = getDictDatas(busiValue);
			if(null != dicts && dicts.size()>0) {
				asgmtValueByDict(ts, dicts);
			} else {
				throw new DaoException("数据字典表中没有查找到["+busiValue+"]相关的数据");
			}
		} else {
			throw new DaoException("参数不能为空");
		}
	}

	@Override
	public void relateDict(T t, String busiValue) throws DaoException {
		if(null != t && !StringUtil.isEmpty(busiValue)) {
			List<TNDict> dicts = getDictDatas(busiValue);
			if(null != dicts && dicts.size()>0) {
				asgmtValueByDict(t, dicts);
			} else {
				throw new DaoException("数据字典表中没有查找到["+busiValue+"]相关的数据");
			}
		} else {
			throw new DaoException("参数不能为空");
		}
	}
	
	@Override
	public void relateDict(List<T> ts, String[] busiValues) throws DaoException {
		if(null != ts && ts.size()>0 && null != busiValues && busiValues.length>0) {
			Map<String,List<TNDict>> dictMaps = new HashMap<String, List<TNDict>>();
			List<TNDict> dicts = null;
			for (String busiValue : busiValues) {
				dicts = getDictDatas(busiValue);
				dictMaps.put(busiValue, dicts);
			}
			if(dictMaps.size()>0) {
				asgmtValueByDict(ts, dictMaps);
			}
			dictMaps = null;
			ts = null;
			dicts = null;
		}
	}

	@Override
	public void relateDict(T t, String[] busiValues) throws DaoException {
		if(null != t && null != busiValues && busiValues.length>0) {
			Map<String,List<TNDict>> dictMaps = new HashMap<String, List<TNDict>>();
			List<TNDict> dicts = null;
			for (String busiValue : busiValues) {
				dicts = getDictDatas(busiValue);
				dictMaps.put(busiValue, dicts);
			}
			if(dictMaps.size()>0) {
				asgmtValueByDict(t, dictMaps);
			}
			dictMaps = null;
			t = null;
			dicts = null;
		}
		
	}

	
	@Override
	public void asgmtValueByDict(List<T> ts, List<TNDict> dicts) {
		//业务过程
		//具体的实现方法由继承该类的子类来实现
	}

	@Override
	public void asgmtValueByDict(T t, List<TNDict> dicts) {
		//业务过程
		//具体的实现方法由继承该类的子类来实现
	}
	
	@Override
	public void asgmtValueByDict(List<T> ts, Map<String, List<TNDict>> dictMaps) {
		//业务过程
		//具体的实现方法由继承该类的子类来实现
	}

	@Override
	public void asgmtValueByDict(T t, Map<String, List<TNDict>> dictMaps) {
		//业务过程
		//具体的实现方法由继承该类的子类来实现
	}
	
	
	/**
	 * 从数据字典中获取数据
	 * @param busiValue
	 * @return
	 * @throws DaoException
	 */
    private List<TNDict> getDictDatas(String busiValue) {
		return dictCache.getItems(busiValue);
	}

	
}
