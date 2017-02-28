package cn.com.smart.web.controller.impl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.com.smart.dao.impl.BaseDaoImpl;
import cn.com.smart.utils.StringUtil;
import cn.com.smart.web.controller.base.BaseController;

/**
 * 显示页面
 * @author lmq
 *
 */
@Controller
@RequestMapping("/showPage")
public class ShowPageController extends BaseController {

	@RequestMapping("/{pagePath}")
	public ModelAndView index(ModelAndView modelView,@PathVariable String pagePath,
			String id,String busiName,String op) throws Exception {
		if(!StringUtil.isEmpty(pagePath)) {
			String[] params = pagePath.split("_");
			if(params.length>0) {
				String dir = "";
				String viewPage = StringUtil.filterFilePath(params[params.length-1]);
				if(params.length<5 && params.length>0){
					for (int i = 0; i < (params.length-1); i++) {
						dir += StringUtil.filterFilePath(params[i])+"/";
					}
					viewPage = dir+viewPage;
				}
				modelView.setViewName(viewPage);
			}
		}
		ModelMap modelMap = modelView.getModelMap();
		modelMap.put("id", id);
		if(!StringUtil.isEmpty(op)) {
			int sortOrder = 0;
			BaseDaoImpl<?> dao = getBaseDao(busiName);
			if(ADD.equals(op)) {
				sortOrder =	(null != dao)?dao.getSortOrder(id):sortOrder;
				modelMap.put("sortOrder", sortOrder);
			} else if(EDIT.equals(op) && null != dao) {
				Object objBean = dao.find(id);
				modelMap.put("objBean", objBean);
			}
		}
		
		return modelView;
	}
	
}
