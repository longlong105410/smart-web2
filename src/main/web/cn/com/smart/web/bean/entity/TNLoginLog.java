package cn.com.smart.web.bean.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import cn.com.smart.bean.BaseBeanImpl;
import cn.com.smart.bean.DateBean;

/**
 * 用户登录日志（实体Bean）
 * @author lmq
 * @version 1.0 2015年8月27日
 * @since 1.0
 *
 */
@Entity
@Table(name = "T_N_LOGIN_LOG")
public class TNLoginLog extends BaseBeanImpl implements DateBean {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -8368756499304249621L;
	private String id;
	private String userId;
	private String userName;
	private String state;
	private String msg;
	private Date createTime;
	private String ip;
	private String loginMode;
	
	/**
	 * 登陆模式为WEB
	 * */
	public static final String MODE_WEB = "web";


	@Id
	@Column(name = "id", length=50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "user_id", length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "state", length=2)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "msg",length=255)
	public String getMsg() {
		return this.msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time",updatable=false)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "ip",length=50)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 登录模式 <br />
	 * 默认为：web登录模式
	 * @return 返回登录模式
	 */
	@Column(name = "login_mode", length=127)
	public String getLoginMode() {
		return loginMode;
	}

	public void setLoginMode(String loginMode) {
		this.loginMode = loginMode;
	}

	@Column(name = "user_name", length=127)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	

}