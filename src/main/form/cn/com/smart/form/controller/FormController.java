package cn.com.smart.form.controller;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.snaker.engine.helper.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.filter.bean.FilterParam;
import cn.com.smart.form.bean.entity.TForm;
import cn.com.smart.form.service.FormService;
import cn.com.smart.web.bean.RequestPage;
import cn.com.smart.web.constant.enums.BtnPropType;
import cn.com.smart.web.service.OPService;
import cn.com.smart.web.tag.bean.ALink;
import cn.com.smart.web.tag.bean.CustomBtn;
import cn.com.smart.web.tag.bean.DelBtn;
import cn.com.smart.web.tag.bean.PageParam;
import cn.com.smart.web.tag.bean.RefreshBtn;

import com.mixsmart.utils.StringUtils;

/**
 * 表单控制器
 * @author lmq
 *
 */
@Controller
@RequestMapping("/form")
public class FormController extends BaseFormController {
	
	private static final String VIEW_DIR = "form";
	
	@Autowired
	private FormService formServ;
	@Autowired
	private OPService opServ;
	
	/**
	 * 表单设计器
	 * @param modelView
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/designer")
	public ModelAndView designer(ModelAndView modelView,String id) throws Exception {
		
		if(StringUtils.isNotEmpty(id)) {
			SmartResponse<Object> smartResp = formServ.find(TForm.class, id);
			if(OP_SUCCESS.equals(smartResp.getResult())) {
				modelView.getModelMap().put("form", (TForm)smartResp.getData());
			}
		}
		modelView.setViewName(VIEW_DIR+"/designer");
		return modelView;
	}
	
	/**
	 * 处理表单
	 * @param session
	 * @param form
	 * @param parseForm
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/designer/processor")
	public @ResponseBody SmartResponse<String> processor(HttpSession session,TForm form,String parseForm) throws Exception {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		if(null != form && StringUtils.isNotEmpty(parseForm)) {
			Map<String,Object> mapData = JsonHelper.fromJson(parseForm, Map.class);
			form.setCreator(getUserInfoFromSession(session).getId());
			smartResp = formServ.parseForm(form,mapData);
		}
		return smartResp;
	}
	
	/**
	 * 表单列表
	 * @param session
	 * @param modelView
	 * @param searchParam
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public ModelAndView list(HttpSession session,ModelAndView modelView,FilterParam searchParam,RequestPage page) throws Exception {
		SmartResponse<Object> smartResp = opServ.getDatas("form_mgr_list",searchParam,page.getStartNum(), page.getPageSize());
		String uri = "form/list";
		CustomBtn customBtn = new CustomBtn("edit_designer", "表单设计器", "修改表单设计", "form/designer");
		customBtn.setSelectedType(BtnPropType.SelectType.ONE.getValue());
		customBtn.setBtnIcon("glyphicon-pencil");
		customBtn.setOpenStyle(BtnPropType.OpenStyle.OPEN_SELF.getValue());
		customBtns = new ArrayList<CustomBtn>(1);
		customBtns.add(customBtn);
		
		delBtn = new DelBtn("op/del.json","form", "确定要删除选中的表单吗，删除后数据将无法恢复？",uri,null, null);
		refreshBtn = new RefreshBtn(uri, null,null);
		pageParam = new PageParam(uri, null, page.getPage(), page.getPageSize());
		
		alinks = new ArrayList<ALink>();
		ALink link = new ALink();
		link.setUri("form/show");
		link.setDialogTitle("预览表单");
		link.setDialogWidth("");
		alinks.add(link);
		link = null;

		ModelMap modelMap = modelView.getModelMap();
		modelMap.put("smartResp", smartResp);
		modelMap.put("customBtns", customBtns);
		modelMap.put("searchParam", searchParam);
		modelMap.put("delBtn", delBtn);
		modelMap.put("refreshBtn", refreshBtn);
		modelMap.put("pageParam", pageParam);
		modelMap.put("alinks", alinks);
		customBtn = null;delBtn = null;
		refreshBtn = null;pageParam = null;
		alinks = null;
		
		modelView.setViewName(VIEW_DIR+"/list");
		return modelView;
	}
	
	/**
	 * 查看表单
	 * @param modelView
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/show")
	public ModelAndView show(ModelAndView modelView,String id) throws Exception {
		SmartResponse<Object> smartResp = new SmartResponse<Object>();
		if(StringUtils.isNotEmpty(id)) {
			smartResp = formServ.find(TForm.class, id);
		}
		modelView.getModelMap().put("smartResp", smartResp);
		modelView.setViewName(VIEW_DIR+"/show");
		return modelView;
	}
	
}
