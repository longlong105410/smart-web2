package cn.com.smart.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import cn.com.smart.utils.StringUtil;
import cn.com.smart.web.bean.UserInfo;
import cn.com.smart.web.constant.enumdef.BtnPropType;
import cn.com.smart.web.service.OPAuthService;
import cn.com.smart.web.tag.bean.CustomBtn;

/**
 * 自定义按钮标签
 * @author lmq
 *
 */
public class CustomBtnTag extends BtnTag {
	
	private static final long serialVersionUID = 3300996504651197696L;
	
	private CustomBtn customBtn;
	private String title;
    private String width="600";
    private String btnIcon = "glyphicon-plus";
    private String paramName = "id";
    private String openStyle = BtnPropType.OpenStyle.OPEN_POP.getValue();

    private String beforeCheck;
    
	@Override
   	public int doEndTag() throws JspException {
   		return EVAL_PAGE;
   	}

   	@Override
   	public int doStartTag() throws JspException {
   		try {
   			JspWriter out = this.pageContext.getOut();
   			if(null == customBtn) {
   				customBtn = new CustomBtn(id, title, name, uri, width, btnIcon,selectedType, btnStyle, paramName);
   				customBtn.setOpenStyle(openStyle);
   				customBtn.setBeforeCheck(beforeCheck);
   			} else {
   				if(StringUtil.isEmpty(customBtn.getBtnStyle()))
   					customBtn.setBtnStyle(btnStyle);
   				if(StringUtil.isEmpty(customBtn.getName()))
   					customBtn.setName(name);
   				if(StringUtil.isEmpty(customBtn.getWidth()))
   					customBtn.setWidth("600");
   				if(StringUtil.isEmpty(customBtn.getBtnIcon()))
   					customBtn.setBtnIcon(btnIcon);
   				if(StringUtil.isEmpty(customBtn.getParamName()))
   					customBtn.setParamName(paramName);
   				if(StringUtil.isEmpty(customBtn.getSelectedType()))
   					customBtn.setSelectedType(selectedType);
   				if(StringUtil.isEmpty(customBtn.getOpenStyle()))
   					customBtn.setOpenStyle(openStyle);
   			}
   			UserInfo userInfo = getUserInfo();
   			OPAuthService opAuthServ = (OPAuthService)getService("opAuthServ");
   			if(opAuthServ.isAuth(currentUri, customBtn, userInfo.getRoleIds())) {
   			   out.println("<button type='button' id='"+customBtn.getId()+"' class='btn "+customBtn.getBtnStyle()+" "+customBtn.getOpenStyle()+" param' data-selected-type='"+StringUtil.handNull(customBtn.getSelectedType())+"' data-uri='"+StringUtil.handNull(customBtn.getUri())+"' data-title='"+StringUtil.handNull(customBtn.getTitle())+"' data-value='' data-param-name='"+StringUtil.handNull(customBtn.getParamName())+"' data-width='"+customBtn.getWidth()+"' ><i class='glyphicon "+customBtn.getBtnIcon()+"'></i> "+customBtn.getName()+"</button>");
   			}
   			userInfo = null;
   		} catch (Exception e) {
   			throw new JspException(e.getMessage());
   		}
   		return SKIP_BODY;
   	}

   	@Override
   	public void release() {
   		super.release();
   		customBtn = null;
   	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public CustomBtn getCustomBtn() {
		return customBtn;
	}

	public void setCustomBtn(CustomBtn customBtn) {
		this.customBtn = customBtn;
	}

	public String getBtnIcon() {
		return btnIcon;
	}

	public void setBtnIcon(String btnIcon) {
		this.btnIcon = btnIcon;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getOpenStyle() {
		return openStyle;
	}

	public void setOpenStyle(String openStyle) {
		this.openStyle = openStyle;
	}

	public String getBeforeCheck() {
		return beforeCheck;
	}

	public void setBeforeCheck(String beforeCheck) {
		this.beforeCheck = beforeCheck;
	}
}
