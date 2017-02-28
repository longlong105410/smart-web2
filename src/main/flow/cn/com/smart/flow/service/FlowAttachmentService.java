package cn.com.smart.flow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import cn.com.smart.exception.DaoException;
import cn.com.smart.flow.bean.SubmitFormData;
import cn.com.smart.flow.bean.entity.TFlowAttachment;
import cn.com.smart.service.impl.MgrServiceImpl;
import cn.com.smart.utils.StringUtil;

/**
 * 流程附件
 * @author lmq
 * @create 2015年6月23日
 * @version 1.0 
 * @since 
 *
 */
@Service
public class FlowAttachmentService extends MgrServiceImpl<TFlowAttachment> {
	
	/**
	 * 更新附件信息
	 * @param data
	 * @param userId
	 * @return
	 */
	public boolean updateAtt(SubmitFormData data,String userId) {
		boolean is = false;
		if(null != data && !StringUtil.isEmpty(userId)) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("formId", data.getFormId());
			param.put("userId", userId);
			try {
				List<TFlowAttachment> processAtts = super.findByParam(param).getDatas();
				if(null != processAtts && processAtts.size()>0) {
					for (TFlowAttachment processAtt : processAtts) {
						processAtt.setOrderId(data.getOrderId());
						processAtt.setTaskId(data.getTaskId());
						processAtt.setTaskKey(data.getTaskKey());
						processAtt.setFormId("");
						processAtt.setUserId("");
					}
					is = OP_SUCCESS.equals(super.update(processAtts).getResult());
				}
				processAtts = null;
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return is;
	}
}
