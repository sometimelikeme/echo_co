package echo.sp.app.service;

import java.util.Map;

/**
 * USER REGISTER,LOGIN 
 * @author Ethan
 *
 */
public interface UserService {
	
	// CHECK IF THE USER EXISTS 
	public int getCheckReg(Map parmMap);
	
	// REGIST USER AND LOGIN IN 
	public int addRegistAlg(Map parmMap);
	
	// LOGIN
	public String login(Map parmMap);
	
	// GET MERCHANTS INFO
	public Map getMerchantInfo(Map parmMap);
}
