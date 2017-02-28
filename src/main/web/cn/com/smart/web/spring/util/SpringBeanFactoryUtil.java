package cn.com.smart.web.spring.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * 
 * @author lmq
 *
 */
public class SpringBeanFactoryUtil extends SpringBeanAutowiringSupport {

	@Autowired
	private BeanFactory beanFactory;
	
	private static SpringBeanFactoryUtil instance; 
	
	static {
		instance = new SpringBeanFactoryUtil();
	}
	
	private SpringBeanFactoryUtil() {
		
	}

	public static SpringBeanFactoryUtil getInstance() {
		return instance;
	}
	
	public Object getBean(String name) {
		return beanFactory.getBean(name);
	}
}
