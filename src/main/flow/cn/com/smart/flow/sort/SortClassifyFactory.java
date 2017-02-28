package cn.com.smart.flow.sort;

import java.util.ArrayList;
import java.util.List;

import cn.com.smart.utils.ListUtil;
import cn.com.smart.utils.StringUtil;

/**
 * 分类排序实现工厂 <br /> 
 * 该分类排序为非线程安全 <br /> 
 * @author lmq
 * @version 1.0
 * @since 1.0
 * 2015年12月21日
 * @param <E>
 */
public class SortClassifyFactory<E> implements ISortClassify<E>{

	private List<SortClassifyBean<E>> sortClassifys;
	
	private int count = 0;

	@Override
	public E get(String key) {
		if(StringUtil.isEmpty(key) || ListUtil.isEmpty(sortClassifys)) {
			return null;
		}
		E entity = null;
		for (SortClassifyBean<E> bean : sortClassifys) {
			if(key.equals(bean.getName())) {
				entity = bean.getEntity();
				break;
			}
		}
		return entity;
	}

	@Override
	public void put(String key, E entity) {
		if(ListUtil.isEmpty(sortClassifys)) {
			sortClassifys = new ArrayList<SortClassifyBean<E>>();
		}
		SortClassifyBean<E> sortClassify = new SortClassifyBean<E>();
		sortClassify.setEntity(entity);
		sortClassify.setName(key);
		sortClassify.setSort(count);
		sortClassifys.add(sortClassify);
		count++;
	}

	@Override
	public void destory() {
		if(ListUtil.isNotEmpty(sortClassifys)) {
			sortClassifys.clear();
		}
		sortClassifys = null;
		count = 0;
	}

	@Override
	public List<SortClassifyBean<E>> getList() {
		return sortClassifys;
	}

	
}
