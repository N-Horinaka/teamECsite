package jp.co.internous.cassiopeia.model.session;

import java.io.Serializable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class LoginSession implements Serializable{
	private static final long serialVersionUID = -6133951535069768706L;

	private int userId;
	private int tmpUserId;
	private String userName;
	private String password;
	private boolean logined;
	
	//setter and getter
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUserId() {
		return userId;
	}
	
	public void setTmpUserId(int tmpUserId) {
		this.tmpUserId = tmpUserId;
	}
	public int getTmpUserId() {
		return tmpUserId;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
	public void setLogined(boolean logined) {
		this.logined = logined;
	}
	public boolean getLogined() {
		return logined;
	}
}