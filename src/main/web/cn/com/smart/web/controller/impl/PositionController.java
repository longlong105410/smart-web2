package cn.com.smart.web.controller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.filter.bean.FilterParam;
import cn.com.smart.utils.StringUtil;
import cn.com.smart.web.bean.entity.TNPosition;
import cn.com.smart.web.constant.enumdef.SelectedEventType;
import cn.com.smart.web.controller.base.BaseController;
import cn.com.smart.web.filter.bean.UserSearchParam;
import cn.com.smart.web.helper.PageHelper;
import cn.com.smart.web.plugins.OrgPositionZTreeData;
import cn.com.smart.web.service.OPService;
import cn.com.smart.web.service.PositionService;
import cn.com.smart.web.tag.bean.DelBtn;
import cn.com.smart.web.tag.bean.EditBtn;
import cn.com.smart.web.tag.bean.PageParam;
import cn.com.smart.web.tag.bean.RefreshBtn;
import cn.com.smart.web.tag.bean.SelectedEventProp;

/**
 * 岗位
 * @author lmq
 *
 */
@Controller
@RequestMapping("/position")
public class PositionController extends BaseController {

	private static final String VIEW_DIR = WEB_BASE_VIEW_DIR+"/position";
	
	@Autowired
	private PositionService posServ;
	@Autowired
	private OPService opServ;
	
	@RequestMapping("/list")
	public ModelAndView list(HttpSession session,ModelAndView modelView,String orgId,Integer page) throws Exception {
		page = null == page?1:page;
		page = PageHelper.getPage(page);
		
		Map<String,Object> params = null;
		orgId = "0".equals(orgId)?null:orgId;
		params = new HashMap<String, Object>();
		if(!StringUtil.isEmpty(orgId)) {
			params.put("orgId", orgId);
		}
		
		params.put("orgIds", StringUtil.list2Array(getUserInfoFromSession(session).getOrgIds()));
		SmartResponse<Object> smartResp = opServ.getDatas("position_mgr_list",params,getStartNum(page),getPerPageSize());
		params = null;
		String uri = "position/list?orgId="+StringUtil.handNull(orgId);
		addBtn = new EditBtn("add","showPage/base_position_add?id="+StringUtil.handNull(orgId),null, "添加职位", "600");
		editBtn = new EditBtn("edit","showPage/base_position_edit", "position", "修改职位", "600");
		delBtn = new DelBtn("op/del", "position", "确定要删除选中的职位吗？",uri,"#position-list", null);
		refreshBtn = new RefreshBtn(uri, "position","#position-list");
		pageParam = new PageParam(uri, "#position-list", page);
		
		ModelMap modelMap = modelView.getModelMap();
		modelMap.put("smartResp", smartResp);
		modelMap.put("addBtn", addBtn);
		modelMap.put("editBtn", editBtn);
		modelMap.put("delBtn", delBtn);
		modelMap.put("refreshBtn", refreshBtn);
		modelMap.put("pageParam", pageParam);
		
		addBtn = null;editBtn = null;delBtn = null;
		refreshBtn = null;pageParam = null;
		
		modelView.setViewName(VIEW_DIR+"/list");
		return modelView;
	}
	
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody SmartResponse<String> add(TNPosition position) throws Exception {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		if(null != position) {
			smartResp = posServ.save(position);
		}
		return smartResp;
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public @ResponseBody SmartResponse<String> edit(TNPosition position) throws Exception {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		if(null != position) {
			smartResp = posServ.update(position);
		}
		return smartResp;
	}
	
	@RequestMapping("/simplist")
	public ModelAndView simplist(HttpSession session,UserSearchParam searchParam,
    		ModelAndView modelView,Integer page) throws Exception {
		page = null == page?1:page;
		page = PageHelper.getPage(page);
		String uri = "position/simplist";
		searchParam.setOrgIds(StringUtil.list2Array(getUserInfoFromSession(session).getOrgIds()));
		SmartResponse<Object> smartResp = opServ.getDatas("position_simp_list",searchParam, getStartNum(page), getPerPageSize());
		uri += (null != searchParam)?("?"+searchParam.getParamToString()):"";
		pageParam = new PageParam(uri, "#position-tab", page);
		selectedEventProp = new SelectedEventProp(SelectedEventType.OPEN_TO_TARGET.getValue(),"auth/positionHas","#has-auth-list","id");	

		ModelMap modelMap = modelView.getModelMap();
		modelMap.put("smartResp", smartResp);
		modelMap.put("pageParam", pageParam);
		modelMap.put("searchParam", searchParam);
		modelMap.put("selectedEventProp", selectedEventProp);
		pageParam = null;
		
		modelView.setViewName(VIEW_DIR+"/simplist");
		return modelView;
	}
	
	/**
	 * 该用户拥有的角色列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/rolelist")
	public ModelAndView rolelist(HttpSession session,UserSearchParam searchParam,
    		ModelAndView modelView,Integer page) throws Exception {

		page = null == page?1:page;
		page = PageHelper.getPage(page);
		String uri = "position/rolelist";
		searchParam.setOrgIds(StringUtil.list2Array(getUserInfoFromSession(session).getOrgIds()));
		SmartResponse<Object> smartResp = opServ.getDatas("position_role_list",searchParam, getStartNum(page), getPerPageSize());
		String paramUri = uri + ((null != searchParam)?("?"+searchParam.getParamToString()):"");
		pageParam = new PageParam(paramUri, null, page);
		
		uri = uri+"?id="+searchParam.getId();
		
		addBtn = new EditBtn("add","position/addRole?id="+searchParam.getId(), null, "该岗位中添加角色", "600");
		delBtn = new DelBtn("op/moreParamDel?flag=p&positionId="+searchParam.getId(), "rolePosition", "确定要从该岗位中删除选中的角色吗？",uri,"#position-role-tab", null);
		refreshBtn = new RefreshBtn(uri, null,"#position-role-tab");
		
		ModelMap modelMap = modelView.getModelMap();
		modelMap.put("smartResp", smartResp);
		modelMap.put("pageParam", pageParam);
		modelMap.put("searchParam", searchParam);
		modelMap.put("addBtn", addBtn);
		modelMap.put("delBtn", delBtn);
		modelMap.put("refreshBtn", refreshBtn);
		pageParam = null;
		
		modelView.setViewName(VIEW_DIR+"/rolelist");
		return modelView;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addRole")
	public ModelAndView addRole(FilterParam searchParam,ModelAndView modelView,Integer page) throws Exception {
		page = null == page?1:page;
		page = PageHelper.getPage(page);
		String uri = "position/addRole";
		SmartResponse<Object> smartResp = opServ.getDatas("position_addrole_list",searchParam, getStartNum(page), getPerPageSize());
		String paramUri = uri += (null != searchParam)?("?"+searchParam.getParamToString()):"";
		pageParam = new PageParam(paramUri, ".bootstrap-dialog-message", page);
		
		ModelMap modelMap = modelView.getModelMap();
		modelMap.put("smartResp", smartResp);
		modelMap.put("pageParam", pageParam);
		modelMap.put("searchParam", searchParam);
		
		modelView.setViewName(VIEW_DIR+"/addRole");
		return modelView;
	}
	
	@RequestMapping(value="/saveRole",method=RequestMethod.POST)
	public @ResponseBody SmartResponse<String> saveUser(String submitDatas,String id) throws Exception {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		if(!StringUtil.isEmpty(submitDatas) && !StringUtil.isEmpty(id)) {
			String[] values = submitDatas.split(",");
			smartResp = posServ.addRole2Position(id, values);
		}
		return smartResp;
	}
	
	
	@RequestMapping("/orgTree")
	public @ResponseBody SmartResponse<OrgPositionZTreeData> orgTree(HttpSession session) throws Exception {
		return posServ.getOrgPositionZTree(getUserInfoFromSession(session).getOrgIds());
	}
}
