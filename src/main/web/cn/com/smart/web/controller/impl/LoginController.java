package cn.com.smart.web.controller.impl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.com.smart.bean.SmartResponse;
import cn.com.smart.web.bean.UserInfo;
import cn.com.smart.web.constant.IActionConstant;
import cn.com.smart.web.controller.base.BaseController;
import cn.com.smart.web.service.UserService;

import com.mixsmart.utils.StringUtils;

/**
 * 登录
 * @author lmq
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

	@Autowired
	private UserService userServ;
	
	@RequestMapping(method=RequestMethod.GET)
	public String index() throws Exception {
		return LOGIN;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView checkLogin(HttpSession session,ModelAndView model,
			String userName,String password,String code) throws Exception {
		boolean is = false;
		String msg = null;
		if(StringUtils.isNotEmpty(userName) 
				&& StringUtils.isNotEmpty(password) 
				&& StringUtils.isNotEmpty(code)) {
			Object codeStr = session.getAttribute(SESSION_CAPTCHA_LOGIN);
			if(null != codeStr && StringUtils.isNotEmpty(codeStr.toString()) 
					&& codeStr.toString().equalsIgnoreCase(code)) {
				SmartResponse<UserInfo> smartResp = userServ.login(userName, password);
				if(OP_SUCCESS.equals(smartResp.getResult())) {
					setUserInfo2Session(session, smartResp.getData());
					is = true;
				} else {
					msg = "用户名或密码输入错误";
				}
				smartResp = null;
			} else {
				msg = "验证码输入错误";
			}
		}
		if(is) {
			String beforeUri = StringUtils.handNull(session.getAttribute(IActionConstant.SESSION_LOGIN_BEFORE_URI));
			log.info("登录前的URL--["+beforeUri+"]--");
			if(StringUtils.isEmpty(beforeUri)) {
				beforeUri = "/index";
			} else {
				beforeUri = beforeUri.startsWith("/") ? beforeUri : ("/"+beforeUri);
			}
			RedirectView view =  new RedirectView(beforeUri, true, true, false);
			model.setView(view);
		} else {
			ModelMap modelMap = model.getModelMap();
			modelMap.put("userName", userName);
			modelMap.put("password", password);
			modelMap.put("code", code);
			modelMap.put("msg", msg);
		}
		return model;
	}
	
}
