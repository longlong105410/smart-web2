package cn.com.smart.web.interceptor;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

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
import cn.com.smart.web.constant.IActionConstant;
import cn.com.smart.web.helper.HttpRequestHelper;
import cn.com.smart.web.sso.SSOUtils;

import com.mixsmart.enums.YesNoType;
import com.mixsmart.utils.ArrayUtils;
import com.mixsmart.utils.StringUtils;

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
		//System.out.println(obj.toString());
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object obj, ModelAndView modelAndView) throws Exception {
		
		String currentUri = HttpRequestHelper.getCurrentUri(request);
		if(!isRes(currentUri)) {
			if(null != modelAndView) {
				ModelMap modelMap = modelAndView.getModelMap();
				modelMap.put("project", InitSysConfig.getInstance().getProjectInfo());
				modelMap.put("currentUri", HttpRequestHelper.getCurrentUri(request));
				modelMap.put("currentUriParam", HttpRequestHelper.getCurrentUriParam(request));
				//请求参数添加到map里面
				Map<String,String[]> curParamMaps = request.getParameterMap();
				if(null != curParamMaps && curParamMaps.size()>0) {
					for (String key : curParamMaps.keySet()) {
						String[] values = curParamMaps.get(key);
						if(values.length<2) {
							if(values[0].length()<100 && !modelMap.containsKey(key)) {
								if(StringUtils.isNotEmpty(values[0]) && values[0].startsWith("%")) {
									modelMap.put(key, URLDecoder.decode(values[0], "UTF-8"));
								} else {
									modelMap.put(key, values[0]);
								}
							}
						} //else {
							//modelMap.put(key, values);
						//}
					}
				} //if;
				RedirectView redirectView = ((RedirectView)modelAndView.getView());
				if(null == redirectView || !redirectView.isRedirectView()) {
					if(isLogin(request)) {
						modelMap.put("userInfo", HttpRequestHelper.getUserInfoFromSession(request));
					}
				}
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
		boolean is = false;
		if(isLogin(request)) {
			is = true;
		} else {
			String currentUri = HttpRequestHelper.getCurrentUri(request);
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
						//session.setAttribute(IActionConstant.SESSION_LOGIN_BEFORE_URI, currentUri);
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
