package cn.com.smart.flow.form;

import java.util.Map;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.flow.bean.QueryFormData;

/**
 * 处理表单数据接口
 * @author lmq
 * @create 2015年8月5日
 * @version 1.0 
 * @since 
 *
 */
public interface IFormDataBusi {

	/**
	 * 获取表单数据
	 * @param orderId
	 * @param formId
	 * @param userId
	 * @return
	 */
	public SmartResponse<QueryFormData> getFormData(String orderId,String formId,String userId);
	
	/**
	 * 保存表单
	 * @param datas
	 * @param formId
	 * @param userId
	 * @param formState
	 * @return
	 */
	public String saveForm(Map<String,Object> datas,String formId,String userId,Integer formState);
	
	/**
	 * 更新表单数据
	 * @param datas
	 * @param formId
	 * @param formDataId
	 * @param userId
	 * @param formState
	 * @return
	 */
	public boolean updateForm(Map<String,Object> datas,String formId,String formDataId,String userId,Integer formState);
	
}
