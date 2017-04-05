package cn.com.smart.flow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mixsmart.utils.StringUtils;

import cn.com.smart.exception.DaoException;
import cn.com.smart.flow.bean.SubmitFormData;
import cn.com.smart.flow.bean.entity.TFlowAttachment;
import cn.com.smart.flow.dao.FlowAttachmentDao;
import cn.com.smart.service.impl.MgrServiceImpl;
import cn.com.smart.utils.StringUtil;
import cn.com.smart.web.bean.entity.TNAttachment;

/**
 * 流程附件
 * @author lmq
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
		if(null != data && StringUtils.isNotEmpty(userId)) {
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
	
	/**
	 * 获取附件列表根据流程实例ID
	 * @param orderId 流程实例id
	 * @return 返回附件实体列表
	 */
	public List<TNAttachment> getAttachmentsByOrderId(String orderId) {
		return getDao().queryAttachmentByOrderId(orderId);
	}


	@Override
	public FlowAttachmentDao getDao() {
		return (FlowAttachmentDao)super.getDao();
	}
}
