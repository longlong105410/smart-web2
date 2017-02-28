package cn.com.smart.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import cn.com.smart.utils.StringUtil;
import cn.com.smart.web.bean.UserInfo;
import cn.com.smart.web.constant.enumdef.BtnPropType;
import cn.com.smart.web.service.OPAuthService;
import cn.com.smart.web.tag.bean.DelBtn;

/**
 * 删除按钮标签
 * @author lmq
 *
 */
public class DelBtnTag extends BtnTag {
	
	private static final long serialVersionUID = 3300996504651197696L;
	
	private DelBtn delBtn;
    private String msg;
	private String refreshUri;
	private String target;
	private String delAfter; 

	@Override
   	public int doEndTag() throws JspException {
   		return EVAL_PAGE;
   	}

   	@Override
   	public int doStartTag() throws JspException {
   		try {
   			id = "del";
   			name = StringUtil.isEmpty(name)?"删除":name;
   			if(!StringUtil.isEmpty(selectedType) && 
   					BtnPropType.SelectType.NONE.getValue().equals(selectedType))
   				selectedType = BtnPropType.SelectType.MULTI.getValue();
   			JspWriter out = this.pageContext.getOut();
   			if(null == delBtn)
   				delBtn = new DelBtn(uri, busi, msg, refreshUri, target, delAfter, btnStyle, name);
   			else {
   				if(StringUtil.isEmpty(delBtn.getBtnStyle()))
   					delBtn.setBtnStyle(btnStyle);
   				if(StringUtil.isEmpty(delBtn.getName()))
   					delBtn.setName(name);
   				if(StringUtil.isEmpty(delBtn.getSelectedType())) {
   					delBtn.setSelectedType(selectedType);
   				}
   			}
   			UserInfo userInfo = getUserInfo();
   			OPAuthService authServ = (OPAuthService)getService("opAuthServ");
   			if(authServ.isAuth(currentUri, delBtn, userInfo.getRoleIds())) {
   				out.println("<button type='button' class='btn "+delBtn.getBtnStyle()+" del param' data-selected-type='"+StringUtil.handNull(delBtn.getSelectedType())+"' data-uri='"+StringUtil.handNull(delBtn.getUri())+"' data-busi='"+StringUtil.handNull(delBtn.getBusi())+"' data-msg='"+StringUtil.handNull(delBtn.getMsg())+"' data-value='' data-refresh-uri='"+StringUtil.handNull(delBtn.getRefreshUri())+"' data-target='"+StringUtil.handNull(delBtn.getTarget())+"' data-delAfter='"+StringUtil.handNull(delBtn.getCallback())+"' ><i class='glyphicon glyphicon-trash'></i> "+delBtn.getName()+"</button>");
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
   		delBtn = null;
   	}

	public DelBtn getDelBtn() {
		return delBtn;
	}

	public void setDelBtn(DelBtn delBtn) {
		this.delBtn = delBtn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getBusi() {
		return busi;
	}

	public void setBusi(String busi) {
		this.busi = busi;
	}

	public String getBtnStyle() {
		return btnStyle;
	}

	public void setBtnStyle(String btnStyle) {
		this.btnStyle = btnStyle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getRefreshUri() {
		return refreshUri;
	}

	public void setRefreshUri(String refreshUri) {
		this.refreshUri = refreshUri;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getDelAfter() {
		return delAfter;
	}

	public void setDelAfter(String delAfter) {
		this.delAfter = delAfter;
	}
	
   	
}
