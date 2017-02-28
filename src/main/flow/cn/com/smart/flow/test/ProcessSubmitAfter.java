/**
 * 
 */
package cn.com.smart.flow.test;

import org.springframework.stereotype.Component;

import cn.com.smart.flow.IProcessExecuteAware;
import cn.com.smart.flow.bean.SubmitFormData;

/**
 * @author lmq
 * @date 2015年8月15日
 * @since 1.0
 */
@Component
public class ProcessSubmitAfter implements IProcessExecuteAware {

	@Override
	public void taskExeAfter(SubmitFormData formData,String userId,String orgId) {
		System.out.println(formData.getTaskName());
	}

	
	
}
