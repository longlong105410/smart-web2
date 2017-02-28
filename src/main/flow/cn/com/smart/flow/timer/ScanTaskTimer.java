package cn.com.smart.flow.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snaker.engine.IQueryService;
import org.snaker.engine.access.Page;
import org.snaker.engine.access.QueryFilter;
import org.snaker.engine.core.ServiceContext;
import org.snaker.engine.entity.Task;
import org.snaker.engine.model.TaskModel;
import org.springframework.beans.factory.annotation.Autowired;

import cn.com.smart.flow.SnakerEngineFacets;
import cn.com.smart.flow.bean.entity.TFlowForm;
import cn.com.smart.flow.service.FlowFormService;
import cn.com.smart.flow.trigger.TriggerFactory;
import cn.com.smart.init.config.InitSysConfig;
import cn.com.smart.web.service.OPService;

import com.mixsmart.enums.YesNoType;
import com.mixsmart.utils.CollectionUtils;
import com.mixsmart.utils.StringUtils;

/**
 * 扫描任务定时器
 * @author lmq <br />
 * 2016年10月3日
 * @version 1.0
 * @since 1.0
 */
public class ScanTaskTimer {
	
	private static final Logger logger = LoggerFactory.getLogger(ScanTaskTimer.class);
	
	@Autowired
	private OPService opServ;
	@Autowired
	private SnakerEngineFacets facets;
	@Autowired
	private FlowFormService flowFormServ;
	@Autowired
	private TriggerFactory<Task> triggerFactory;
	
	public void run() {
		if(logger.isInfoEnabled()) {
			logger.info("正在执行任务扫描定时器....");
		}
		int intPage = 1;
		Page<Task> page = new Page<Task>();
		page.setPage(intPage);
		page.setPageSize(100);
		IQueryService query = ServiceContext.getEngine().query();
		QueryFilter queryFilter = new QueryFilter();
		List<Task> tasks = query.getActiveTasks(page, queryFilter);
		Map<String, TaskModel> taskModelCache = new HashMap<String, TaskModel>();
		String autoAllTask = InitSysConfig.getInstance().getValue("flow.task.expire.all.auto");
		YesNoType yesNo = YesNoType.getObjByStrValue(autoAllTask);
		boolean isAllTaskAuto = (null == yesNo)?false:yesNo.getValue();
		while(CollectionUtils.isNotEmpty(tasks)) {
			if(logger.isInfoEnabled()) {
				logger.info("执行第"+intPage+"次");
			}
			for (Task task : tasks) {
				TaskModel taskModel = task.getModel();
				if(null == taskModel) {
					TFlowForm flowForm = flowFormServ.getFlowFormByOrderId(task.getOrderId());
					if(null != flowForm) {
						//缓存任务模型，从缓存中获取任务模型，如果没有，则从数据库中查询(流程ID和任务KEY能唯一任务模型)
						//不同的任务，对应的任务模型可能是同一个的，为了避免重复查询，采用了临时缓存
						String cacheKey = flowForm.getProcessId() + "_"+ task.getTaskName();
						taskModel = taskModelCache.get(cacheKey);
						if(null == taskModel) {
							taskModel = facets.getTaskModel(flowForm.getProcessId(), task.getTaskName());
							taskModelCache.put(cacheKey, taskModel);
						}
						task.setModel(taskModel);
					}
				}//if
				if(isAllTaskAuto) {
					if(null != taskModel && StringUtils.isNotEmpty(taskModel.getExpireTime()) 
							&& !"0".equals(taskModel.getExpireTime()) && 
							StringUtils.isNum(taskModel.getExpireTime()) 
							&& YesNoType.YES.getStrValue().equals(taskModel.getAutoExecute())) {
						triggerFactory.getTrigger("autoTask").execute(task);
					}
				} else {
					if(null != taskModel && StringUtils.isNotEmpty(task.getExpireTime()) 
							&& YesNoType.YES.getStrValue().equals(taskModel.getAutoExecute())) {
						triggerFactory.getTrigger("autoTask").execute(task);
					}
				}
			}//for
			intPage++;
			page.setPage(intPage);
			tasks = ServiceContext.getEngine().query().getActiveTasks(page, queryFilter);
		}
		if(logger.isInfoEnabled()) {
			logger.info("任务扫描定时器执行结束....");
		}
		taskModelCache.clear();
		taskModelCache = null;
	}

	
}
