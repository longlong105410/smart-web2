package cn.com.smart.web.controller.impl;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.utils.StringUtil;
import cn.com.smart.web.bean.entity.TNUser;
import cn.com.smart.web.constant.enumdef.BtnPropType;
import cn.com.smart.web.constant.enumdef.SelectedEventType;
import cn.com.smart.web.controller.base.BaseController;
import cn.com.smart.web.filter.bean.UserSearchParam;
import cn.com.smart.web.helper.PageHelper;
import cn.com.smart.web.plugins.OrgUserZTreeData;
import cn.com.smart.web.service.OPService;
import cn.com.smart.web.service.UserService;
import cn.com.smart.web.tag.bean.CustomBtn;
import cn.com.smart.web.tag.bean.DelBtn;
import cn.com.smart.web.tag.bean.EditBtn;
import cn.com.smart.web.tag.bean.PageParam;
import cn.com.smart.web.tag.bean.RefreshBtn;
import cn.com.smart.web.tag.bean.SelectedEventProp;

/**
 * 用户
 * @author lmq
 *
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	private static final String VIEW_DIR = WEB_BASE_VIEW_DIR+"/user";
	
	@Autowired
	private UserService userServ;
	@Autowired
	private OPService opServ;
	
	@RequestMapping("/list")
	public ModelAndView list(HttpSession session,UserSearchParam searchParam,ModelAndView modelView,Integer page) throws Exception {
		page = null == page?1:page;
		page = PageHelper.getPage(page);
		String uri = "user/list"; 
		addBtn = new EditBtn("add","showPage/base_user_add", null, "添加用户", "600");
		editBtn = new EditBtn("edit","showPage/base_user_edit", "user", "修改用户信息", "600");
		delBtn = new DelBtn("op/del.json", "user", "确定要删除选中的用户吗？",uri,null, null);
		String paramStr = null;
		paramStr = (null != searchParam)?searchParam.getParamToString():null;
		uri += StringUtil.isNotEmpty(paramStr)?("?"+paramStr):"";
		uri = StringUtil.isContains(uri, "?")?uri+"&page=":uri+"?page=";
		uri += page;
		refreshBtn = new RefreshBtn(uri, "resource",null);
		CustomBtn customBtn = new CustomBtn("changepwd", "修改密码", "修改密码", "showPage/base_user_batchChangePwd","glyphicon-pencil",BtnPropType.SelectType.MULTI.getValue());
		customBtn.setWidth("500");
		customBtns = new ArrayList<CustomBtn>(1);
		customBtns.add(customBtn);
		
		if("0".equals(searchParam.getOrgId())) {
			searchParam.setOrgId(null);
		}
		searchParam.setOrgIds(StringUtil.list2Array(getUserInfoFromSession(session).getOrgIds()));
		pageParam = new PageParam(uri, null, page);
		
		SmartResponse<Object> smartResp = userServ.findAllObj(searchParam, getStartNum(page), getPerPageSize());
		ModelMap modelMap = modelView.getModelMap();
		modelMap.put("smartResp", smartResp);
		modelMap.put("addBtn", addBtn);
		modelMap.put("editBtn", editBtn);
		modelMap.put("delBtn", delBtn);
		modelMap.put("refreshBtn", refreshBtn);
		modelMap.put("pageParam", pageParam);
		modelMap.put("searchParam", searchParam);
		modelMap.put("customBtns", customBtns);
		
		addBtn = null;editBtn = null;delBtn = null;
		refreshBtn = null;pageParam = null;
		
		modelView.setViewName(VIEW_DIR+"/list");
		return modelView;
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public @ResponseBody SmartResponse<String> add(TNUser user) throws Exception {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		if(null != user) {
			smartResp = userServ.save(user);
		}
		return smartResp;
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public @ResponseBody SmartResponse<String>  edit(TNUser user) throws Exception {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		if(null != user) {
			smartResp = userServ.update(user);
		}
		return smartResp;
	}
	
	
	/**
	 * 安全退出
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpSession session,ModelAndView model) {
		 // 清除session
		/*Enumeration<String> names = session.getAttributeNames();
		session.removeAttribute(SESSION_USER_KEY);
		if(null != names) {
			while(names.hasMoreElements()) {
				session.removeAttribute(names.nextElement());
			}
		}*/
		session.invalidate();
		RedirectView view =  new RedirectView("/login", true, true, false);
		model.setView(view);
		return model;
	}
	
	/**
	 * 判断是否用户是否已经登录
	 * @param session
	 * @return
	 */
	@RequestMapping("/islogin")
	public @ResponseBody SmartResponse<String> islogin(HttpSession session) {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		if(null != getUserInfoFromSession(session)) {
			smartResp.setResult(OP_SUCCESS);
			smartResp.setMsg(OP_SUCCESS_MSG);
		}
		return smartResp;
	}
	
	
	
	/**
	 * 简单列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/simplist")
    public ModelAndView simplist(HttpSession session,UserSearchParam searchParam,
    		ModelAndView modelView,Integer page) throws Exception {
		page = null == page?1:page;
		page = PageHelper.getPage(page);
		String uri = "user/simplist";
		searchParam.setOrgIds(StringUtil.list2Array(getUserInfoFromSession(session).getOrgIds()));
		SmartResponse<Object> smartResp = opServ.getDatas("user_simp_list",searchParam, getStartNum(page), getPerPageSize());
		uri += (null != searchParam)?("?"+searchParam.getParamToString()):"";
		pageParam = new PageParam(uri, "#user-tab", page);
		selectedEventProp = new SelectedEventProp(SelectedEventType.OPEN_TO_TARGET.getValue(),"auth/userHas","#has-auth-list","id");	

		ModelMap modelMap = modelView.getModelMap();
		modelMap.put("smartResp", smartResp);
		modelMap.put("pageParam", pageParam);
		modelMap.put("searchParam", searchParam);
		modelMap.put("selectedEventProp", selectedEventProp);
		pageParam = null;
		
		modelView.setViewName(VIEW_DIR+"/simplist");
		return modelView;
	}
	
	@RequestMapping("/userInfo")
	public ModelAndView userInfo(ModelAndView modelView,String id) throws Exception {
		SmartResponse<Object> smartResp = new SmartResponse<Object>();
		if(!StringUtil.isEmpty(id)) {
		   smartResp = userServ.find(TNUser.class, id);
		}
		modelView.getModelMap().put("smartResp", smartResp);
		modelView.setViewName(VIEW_DIR+"/userInfo");
		return modelView;
	}
	
	@RequestMapping(value="changePwd",method=RequestMethod.POST)
	public @ResponseBody SmartResponse<String> changePwd(HttpSession session,String newPwd,
			String confirmNewPwd,String oldPwd) throws Exception {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		smartResp.setMsg("请输入旧密码和新密码");
		if(!StringUtil.isEmpty(newPwd) && !StringUtil.isEmpty(oldPwd) 
				&& !StringUtil.isEmpty(confirmNewPwd)) {
			smartResp = userServ.changePwd(getUserInfoFromSession(session).getId(), oldPwd, newPwd, confirmNewPwd);
			log.info(smartResp.getMsg());
		}
		return smartResp;
	}
	
	/**
	 * 批量修改密码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="batchChangePwd",method=RequestMethod.POST)
	public @ResponseBody SmartResponse<String> batchChangePwd(String id,String newPwd,String confirmNewPwd) throws Exception {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		smartResp.setMsg("请输入密码或确认密码");
		if(!StringUtil.isEmpty(newPwd) && !StringUtil.isEmpty(id)) {
			smartResp = userServ.batchChangePwd(id, newPwd,confirmNewPwd);
			log.info(smartResp.getMsg());
		} else {
			log.info("用户名ID或密码为空");
		}
		return smartResp;
	}
	
	
	/**
	 * 该用户拥有的角色列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/rolelist")
	public ModelAndView rolelist(UserSearchParam searchParam,ModelAndView modelView,Integer page) throws Exception {
		String uri = "user/rolelist";
		page = null == page?1:page;
		page = PageHelper.getPage(page);
		
		SmartResponse<Object> smartResp = opServ.getDatas("user_role_list",searchParam, getStartNum(page), getPerPageSize());
		String paramUri = uri + ((null != searchParam)?("?"+searchParam.getParamToString()):"");
		pageParam = new PageParam(paramUri, null, page);
		uri = uri+"?id="+searchParam.getId();
		addBtn = new EditBtn("add","user/addRole?id="+searchParam.getId(), null, "用户中添加角色", "600");
		delBtn = new DelBtn("op/moreParamDel.json?flag=u&userId="+searchParam.getId(), "roleUser", "确定要从该用户中删除选中的角色吗？",uri,"#user-role-tab", null);
		refreshBtn = new RefreshBtn(uri, null,"#user-role-tab");
		
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
	public ModelAndView addRole(UserSearchParam searchParam,ModelAndView modelView,Integer page) throws Exception {
		page = null == page?1:page;
		page = PageHelper.getPage(page);
		String uri = "user/addRole";
		SmartResponse<Object> smartResp = opServ.getDatas("user_addrole_list",searchParam, getStartNum(page), getPerPageSize());
		String paramUri = uri += (null != searchParam)?("?"+searchParam.getParamToString()):"";
		pageParam = new PageParam(paramUri, ".bootstrap-dialog-message", page);
		
		ModelMap modelMap = modelView.getModelMap();
		modelMap.put("smartResp", smartResp);
		modelMap.put("pageParam", pageParam);
		modelMap.put("searchParam", searchParam);
		pageParam = null;
		modelView.setViewName(VIEW_DIR+"/addRole");
		return modelView;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/saveRole",method=RequestMethod.POST)
	public @ResponseBody SmartResponse<String> saveRole(String submitDatas,String id) throws Exception {
		SmartResponse<String> smartResp = new SmartResponse<String>();
		if(!StringUtil.isEmpty(submitDatas) && !StringUtil.isEmpty(id)) {
			String[] values = submitDatas.split(",");
			smartResp = userServ.addRole2User(id, values);
			values = null;
			submitDatas = null;
		}
		return smartResp;
	}
	
	
	@RequestMapping("/orgTree")
	public @ResponseBody SmartResponse<OrgUserZTreeData> orgTree(HttpSession session) throws Exception {
		return userServ.getOrgUserZTree(getUserInfoFromSession(session).getOrgIds());
	}
}
