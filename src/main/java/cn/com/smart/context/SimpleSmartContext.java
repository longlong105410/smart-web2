package cn.com.smart.context;

import java.util.List;

/**
 * 
 * @author lmq
 * @version 1.0
 * @since 1.0
 * 2015年8月22日
 */
public class SimpleSmartContext implements ISmartContext {

	@Override
	public void put(String name, Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void put(String name, Class<?> clazz) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isExist(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T find(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> finds(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public <T> T findByName(String name, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

}
