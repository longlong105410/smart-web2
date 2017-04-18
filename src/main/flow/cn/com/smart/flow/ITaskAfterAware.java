package cn.com.smart.flow;

import cn.com.smart.flow.bean.SubmitFormData;

/**
 * 任务处理结束后，需要后续处理的接口
 * @author lmq  2017年3月29日
 * @version 1.0
 * @since 1.0
 */
public interface ITaskAfterAware {

	/**
	 * 后续继续执行的方法;
	 * 注：该方法与流程任务处理的方法不在同一事物中
	 * @param submitFormData
	 */
	void execute(SubmitFormData submitFormData);
	
}
