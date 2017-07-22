package cn.com.smart.web.interceptor;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.com.smart.init.config.InitSysConfig;
import cn.com.smart.service.SmartContextService;
import cn.com.smart.web.bean.UserInfo;
import cn.com.smart.web.bean.entity.TNAccessLog;
import cn.com.smart.web.constant.IActionConstant;
import cn.com.smart.web.helper.HttpRequestHelper;
import cn.com.smart.web.service.AccessLogService;
import cn.com.smart.web.sso.SSOUtils;

import com.mixsmart.enums.YesNoType;
import com.mixsmart.utils.ArrayUtils;
import com.mixsmart.utils.LoggerUtils;
import com.mixsmart.utils.StringUtils;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * 访问控制拦截
 * @author lmq
 *
 */
public class ACLInterceptor implements HandlerInterceptor {
  
	private List<String> excludeMaps;
	
	private String resSuffix;
	
	private final static Logger log = LoggerFactory.getLogger(ACLInterceptor.class);
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception arg3)
			throws Exception {
		String currentUri = HttpRequestHelper.getCurrentUri(request);
		long startTime = (Long)request.getAttribute("startTime");
		Date responseTime = new Date();
		long endTime = responseTime.getTime();
		long useTime = endTime - startTime;
		LoggerUtils.debug(log, "请求["+currentUri+"]用时："+useTime+"毫秒");
		if(!isRes(currentUri)) {
			Object al = request.getAttribute("accessLog");
			if(null != al) {
				//保存访问日志
				TNAccessLog accessLog = (TNAccessLog)al;
				accessLog.setResponseTime(responseTime);
				accessLog.setUseTime(useTime);
				AccessLogService accessLogServ = SmartContextService.find(AccessLogService.class);
				LoggerUtils.debug(log, "正在保存访问日志...");
				accessLogServ.save(accessLog);
			}
			/*
			//更新访问日志；计算用时
			String accessLogId = StringUtils.handNull(request.getAttribute("accessLogId"));
			if(StringUtils.isNotEmpty(accessLogId)) {
				AccessLogService accessLogServ = SmartContextService.find(AccessLogService.class);
				accessLogServ.update(accessLogId, responseTime, useTime);
			}*/
			response.setHeader("Cache-Control","no-cache");
			response.setHeader("Pragrma","no-cache");
			response.setDateHeader("Expires",-1);
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView modelAndView) throws Exception {
		String currentUri = HttpRequestHelper.getCurrentUri(request);
		if(isRes(currentUri) || null == modelAndView) {
			return;
		}
		ModelMap modelMap = modelAndView.getModelMap();
		modelMap.put("project", InitSysConfig.getInstance().getProjectInfo());
		modelMap.put("currentUri", HttpRequestHelper.getCurrentUri(request));
		modelMap.put("currentUriParam", HttpRequestHelper.getCurrentUriParam(request));
		//请求参数添加到map里面
		Map<String,String[]> curParamMaps = request.getParameterMap();
		if(null != curParamMaps && curParamMaps.size()>0) {
			Set<Map.Entry<String, String[]>> items = curParamMaps.entrySet();
			for (Map.Entry<String, String[]> item : items) {
				if(item.getValue().length<2) {
					String value = item.getValue()[0];
					if(StringUtils.isNotEmpty(value) && value.startsWith("%")) {
						value = URLDecoder.decode(value, "UTF-8");
					}
					if(value.length()<100 && !modelMap.containsKey(item.getKey())) {
						modelMap.put(item.getKey(), value);
					}
				}
			}
		} //if;
		RedirectView redirectView = ((RedirectView)modelAndView.getView());
		if(null == redirectView || !redirectView.isRedirectView()) {
			if(isLogin(request)) {
				modelMap.put("userInfo", HttpRequestHelper.getUserInfoFromSession(request));
			}
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj) throws Exception {
		String currentUriParam = HttpRequestHelper.getCurrentUriParam(request);
		//获取域名或IP地址
		String serverName = request.getServerName();
		String domainForward = InitSysConfig.getInstance().getValue("domain.forward");
		//如果是通过IP或域名访问的地址，和配置文件中配置的IP或域名不一致；
		//则采用配置文件中的IP或域名访问（即：根据配置的域名或IP重新跳转）.
		if(YesNoType.YES.getStrValue().equals(domainForward)) {
			String domain = InitSysConfig.getInstance().getValue("domain.name");
			if(StringUtils.isNotEmpty(domain) && !serverName.equals(domain)) {
				String path = request.getContextPath();
				String basePath = request.getScheme()+"://"+domain+":"+request.getServerPort()+path+"/";
				response.sendRedirect(basePath+currentUriParam);
				return false;
			}
		}
		String currentUri = HttpRequestHelper.getCurrentUri(request);
		boolean isRes = isRes(currentUri);
		boolean is = false;
		TNAccessLog accessLog = null;
		Date currentTime = new Date();
		long startTime = currentTime.getTime();
		request.setAttribute("startTime", startTime);
		if(isLogin(request)) {
			is = true;
		} else {
			if(!isRes(currentUri)) {
				if(!isExclude(currentUri)) {
					HttpSession session = request.getSession();
					//获取SSO(单点登录)服务器地址
					String ssoServerUrl = SSOUtils.getSSOServerURL();
					String domainUrl = HttpRequestHelper.getDomain(request);
					String forward = request.getParameter("forward");
					if(StringUtils.isNotEmpty(forward)) {
						forward = URLEncoder.encode(forward,"UTF-8");
						session.setAttribute("forward", forward);
						String newCurrentUriParam = currentUri +"?forward="+forward;
						session.setAttribute(IActionConstant.SESSION_LOGIN_BEFORE_URI, newCurrentUriParam);
					} else {
						session.setAttribute(IActionConstant.SESSION_LOGIN_BEFORE_URI, currentUriParam);
					}
					if(log.isInfoEnabled()) {
						log.info("当前服务器地址：["+ domainUrl +"] --- 单点登录服务器地址：["+ ssoServerUrl +"]");
					}
					if(StringUtils.isNotEmpty(ssoServerUrl) && !ssoServerUrl.startsWith(domainUrl)) {
						String ssoRequestUri = "/sso/requestView";
						String contextPath = request.getContextPath();
						if(StringUtils.isNotEmpty(contextPath) && !"/".equals(contextPath)) {
							ssoRequestUri = contextPath+ssoRequestUri;
						}
						response.sendRedirect(ssoRequestUri);
					} else {
						String loginUri = "/login";
						String contextPath = request.getContextPath();
						if(StringUtils.isNotEmpty(contextPath) && !"/".equals(contextPath)) {
							loginUri = contextPath+"/login";
						}
						response.sendRedirect(loginUri);
					}
				} else {
					is = true;
				}
			} else {
				is = true;
			}
		}
		//从配置文件中获取是否记录日志的标识；如果未配置；默认记录访问日志
		YesNoType yesNo = YesNoType.getObjByStrValue(InitSysConfig.getInstance().getValue("is.access.log"));
		if(null == yesNo) {
			yesNo = YesNoType.YES;
		}
		//如果不是资源URL，并且记录访问日志
		if(!isRes && yesNo.getValue()) {
			String userAgentStr = request.getHeader("User-Agent");
			//记录访问日志
			accessLog = new TNAccessLog();
			accessLog.setUserAgent(userAgentStr);
			UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
			accessLog.setBrowser(userAgent.getBrowser().getName());
			if(null != userAgent.getBrowserVersion())
				accessLog.setBrowserVersion(userAgent.getBrowserVersion().getVersion());
			accessLog.setOs(userAgent.getOperatingSystem().getName());
			accessLog.setDeviceType(userAgent.getOperatingSystem().getDeviceType().getName());
			accessLog.setIp(HttpRequestHelper.getIP(request));
			accessLog.setRequestMethod(request.getMethod());
			accessLog.setUri(currentUri);
			String url = HttpRequestHelper.getCurrentUriParam(request);
			if(url.length() > 490) {
				url = url.substring(0, 490);
			}
			accessLog.setUrl(url);
			String param = request.getQueryString();
			if(StringUtils.isNotEmpty(param)) {
				param = URLDecoder.decode(param, "UTF-8");
				if(param.length() > 490) {
					param = param.substring(0, 490);
				}
			}
			accessLog.setParam(param);
			UserInfo userInfo = HttpRequestHelper.getUserInfoFromSession(request);
			if(null != userInfo) {
				accessLog.setUserId(userInfo.getId());
				accessLog.setUsername(userInfo.getUsername());
				accessLog.setLoginId(userInfo.getLoginId());
			}
			accessLog.setCreateTime(currentTime);
			//为了避免访问日志更新操作影响性能（数据量大的原因），这里不在保存访问日志，等到请求全部结束后，在保存；
			//保存方法在 afterCompletion 方法中实现
			/*
			LoggerUtils.debug(log, "正在保存访问日志");
			AccessLogService accessLogServ = SmartContextService.find(AccessLogService.class);
			accessLogServ.save(accessLog);
			request.setAttribute("accessLogId", accessLog.getId());*/
			request.setAttribute("accessLog", accessLog);
		}
		return is;
	}
	
	/**
	 * 检测请求的资源样式,js,图片等文件(css,js,img)
	 * @param currentUri
	 * @return
	 */
	private boolean isRes(String currentUri) {
		boolean isRes = false;
		if(StringUtils.isNotEmpty(resSuffix)) {
			String suffix = StringUtils.getFileSuffix(currentUri);
			if(StringUtils.isNotEmpty(suffix)) {
				if(ArrayUtils.isArrayContainsIgnoreCase(resSuffix, suffix, ",")) {
					isRes = true;
				}
			}
		}
		return isRes;
	}
	
	/**
	 * 是否排除
	 * @param currentUri
	 * @return
	 */
	private boolean isExclude(String currentUri) {
		boolean is = false;
		is = currentUri.startsWith("#");
		if(!is && null != excludeMaps && excludeMaps.size()>0) {
			for (String uri : excludeMaps) {
				if(StringUtils.isNotEmpty(uri) && uri.trim().length()>1 && uri.indexOf("*")>0) {
					uri = uri.replace("*", "");
					if(currentUri.startsWith(uri) || currentUri.endsWith(uri)) {
						is = true;
						break;
					}
				} else {
					if(currentUri.equals(uri)) {
						is = true;
						break;
					}
				}
			}
		}
		return is;
		
	}
	
	
	/**
	 * 判断用户是否登录
	 * @param request
	 * @return
	 */
	private boolean isLogin(HttpServletRequest request) {
		boolean is = false;
		HttpSession session = request.getSession();
		if(null != session) {
			is = (null != session.getAttribute(IActionConstant.SESSION_USER_KEY))?true:false;
		}
		return is;
	}
	

	public List<String> getExcludeMaps() {
		return excludeMaps;
	}

	public void setExcludeMaps(List<String> excludeMaps) {
		this.excludeMaps = excludeMaps;
	}

	public String getResSuffix() {
		return resSuffix;
	}

	public void setResSuffix(String resSuffix) {
		this.resSuffix = resSuffix;
	}
	
}
