package cn.com.smart.flow.ext;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.smart.flow.IProcessExecuteAware;
import cn.com.smart.flow.SnakerEngineFacets;
import cn.com.smart.flow.bean.SubmitFormData;
import cn.com.smart.flow.bean.entity.TFlowForm;
import cn.com.smart.flow.bean.entity.TFlowProcess;
import cn.com.smart.flow.dao.FlowFormDao;
import cn.com.smart.flow.dao.FlowProcessDao;
import cn.com.smart.flow.helper.ProcessHelper;
import cn.com.smart.utils.ListUtil;
import cn.com.smart.utils.StringUtil;

/**
 * 更新流程进度
 * @author lmq
 * @version 1.0
 * @since 1.0
 * 2015年11月16日
 */
@Component
public class UpdateProcessProgress implements IProcessExecuteAware {

	@Autowired
	private FlowFormDao flowFormDao;
	@Autowired
	private FlowProcessDao flowProcessDao;
	@Autowired
	private SnakerEngineFacets facet;
	
	@Override
	public void taskExeAfter(SubmitFormData formData, String userId,String orgId) {
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("processId", formData.getProcessId());
		List<TFlowProcess> processList = flowProcessDao.queryByField(param);
		if(ListUtil.isNotEmpty(processList)) {
			TFlowProcess process = processList.get(0);
			param.clear();
			if(StringUtil.isNotEmpty(formData.getOrderId())) {
				param.put("orderId", formData.getOrderId());
				List<TFlowForm> flowForms = flowFormDao.queryByField(param);
				if(ListUtil.isNotEmpty(flowForms)) {
					TFlowForm flowForm = flowForms.get(0);
					flowForm.setTotalNodeNum(process.getTotalNodeNum());
					int position = countCurrentPosition(process.getNodeNameCollection(), formData.getTaskName());
					flowForm.setExecuteNodeNum(position);
					float rate = 0f;
					if(position == 0) {
						rate = 0;
					} else if(process.getTotalNodeNum() == position) {
						rate = 100;
					} else {
						DecimalFormat decimalFormater = new DecimalFormat("#.00");
						double tmp = (position/(double)process.getTotalNodeNum())*100;
						rate = Float.parseFloat(decimalFormater.format(tmp));
					}
					flowForm.setProgress(rate);
					flowFormDao.update(flowForm);
				}
			}
		}
	}
	
	/**
	 * 计算当前节点位置
	 * @param nodeCollection 节点字符串集合
	 * @param currentNodeName 当前节点名称
	 * @return 返回节点所在位置；如果计算失败返回：0
	 */
	private int countCurrentPosition(String nodeCollection,String currentNodeName) {
		int count = 0;
		if(StringUtil.isNotEmpty(nodeCollection) && StringUtil.isNotEmpty(currentNodeName)) {
			String[] nodes = ProcessHelper.nodeStrToArray(nodeCollection);
			if(null != nodes && nodes.length>0) {
				for (int i = 0; i < nodes.length; i++) {
					if(nodes[i].equals(currentNodeName)) {
						count = i+1;
						break;
					}
				}
			}
		}
		return count;
	}

}
