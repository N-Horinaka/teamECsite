package jp.co.internous.cassiopeia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import jp.co.internous.cassiopeia.model.domain.MstUser;
import jp.co.internous.cassiopeia.model.form.UserForm;
import jp.co.internous.cassiopeia.model.mapper.MstUserMapper;
import jp.co.internous.cassiopeia.model.mapper.TblCartMapper;
import jp.co.internous.cassiopeia.model.session.LoginSession;

@RestController
@RequestMapping("/cassiopeia/auth")
public class AuthController {

	@Autowired
	private LoginSession loginSession;	

	@Autowired
	private MstUserMapper mstUserMapper;
	
	@Autowired
	private TblCartMapper tblCartMapper;
	
	private Gson gson = new Gson();
	
	//login method
	@RequestMapping(value="/login")
	public String login(@RequestBody UserForm form){
		//find record of the user
		MstUser mstUser = mstUserMapper.findByUserNameAndPassword(form.getUserName(), form.getPassword());

		//define a variable tmpUserId
		int tmpUserId = loginSession.getTmpUserId();
		//re-link cart information of tmpUserId to userId by using tmpUserId
		if (mstUser != null && tmpUserId != 0) {
			int count = tblCartMapper.findCountByUserId(tmpUserId);
			if (count > 0) {
				tblCartMapper.updateUserId(mstUser.getId(), tmpUserId);
			}
		}

		//set sessions
		if (mstUser != null) {
			loginSession.setTmpUserId(0);
			loginSession.setUserId(mstUser.getId());
			loginSession.setUserName(mstUser.getUserName());
			loginSession.setPassword(mstUser.getPassword());
			loginSession.setLogined(true);
		} else {
			loginSession.setUserId(0);
			loginSession.setUserName(null);
			loginSession.setPassword(null);
			loginSession.setLogined(false);
		}
		
		return gson.toJson(mstUser);
	}
	
	//logout method
	@RequestMapping(value="/logout")
	public String logout(){
		//delete session
		loginSession.setTmpUserId(0);
		loginSession.setUserId(0);
		loginSession.setUserName(null);
		loginSession.setPassword(null);
		loginSession.setLogined(false);
		
		return "";
	}	
	
	//resetPassword method
	@RequestMapping(value="/resetPassword")
	public String resetPassword(@RequestBody UserForm form){
		//define variables
		String message = "パスワードが再設定されました。";
		String newPassword = form.getNewPassword();
		String newPasswordConfirm = form.getNewPasswordConfirm();
		
		//find record of the user
		MstUser mstUser = mstUserMapper.findByUserNameAndPassword(form.getUserName(), form.getPassword());
		
		//if sentences
		if (mstUser == null) {
			return "現在のパスワードが正しくありません。";
		}
		if (mstUser.getPassword().equals(newPassword)) {
			return "現在のパスワードと同一の文字列が入力されました。";
		}
		if (!newPassword.equals(newPasswordConfirm)) {
			return "新パスワードと確認用パスワードが一致しません。";
		}
		
		//update password of the user
		mstUserMapper.updatePassword(mstUser.getUserName(), form.getNewPassword());	
		
		//set new password in login session
		loginSession.setPassword(form.getNewPassword());
	
		return message;
	}	
}