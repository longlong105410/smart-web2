package cn.com.smart.filter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import cn.com.smart.filter.bean.FilterParam;

import com.mixsmart.utils.StringUtils;

/**
 * 处理搜索参数 过滤参数值为空的 <br />
 * parseParam() 方法获取bean中的属性及值
 * @author lmq
 * 
 */
public class HandleFilterParam {

	private FilterParam searchParam;

	Map<String, Object> params = null;

	/**
	 * 处理搜索参数
	 * @param searchParam
	 */
	public HandleFilterParam(FilterParam searchParam) {
		this.searchParam = searchParam;
		if(null != searchParam) {
			params = new HashMap<String, Object>();
			parseParam(searchParam.getClass());
		}
	}

	/**
	 * 解析参数
	 * @param clasz
	 */
	protected void parseParam(Class<?> clasz) {
		if(null != clasz) {
			Field[] fields = clasz.getDeclaredFields();
			if(null != fields && fields.length > 0) {
				try {
					for(Field field : fields) {
						if(field.getModifiers() == Modifier.PRIVATE || field.getModifiers() == Modifier.PROTECTED || field.getModifiers() == Modifier.PUBLIC) {
							String fieldName = field.getName();
							String methodName = "get"+ StringUtils.firstToUppercase(fieldName);
							Method method = clasz.getDeclaredMethod(methodName);
							if(null != method) {
								Object value = method.invoke(searchParam);
								if(null != value && StringUtils.isNotEmpty(value.toString())) {
									params.put(fieldName, value);
								}
							}
							method = null;
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Class<?> superClasz = clasz.getSuperclass();
			if (null != superClasz) {
				parseParam(superClasz);
			}
		}
	}

	/**
	 * 获取参数
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getParams() {
		return params.isEmpty()?null:params;
	}

}
