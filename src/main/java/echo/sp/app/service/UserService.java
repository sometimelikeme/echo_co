package echo.sp.app.service;

import java.util.Map;

/**
 * USER REGISTER,LOGIN 
 * @author Ethan
 * @date 2015年8月29日 
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
	
	// CHANGE PASSWORD
 	public int updatePwd(Map parmMap);
 	
 	// CHECK IF MERCHANT
  	public int checkMerchant(Map parmMap);
 	
 	// UPGRADE TO MERCHANT
   	public int insertToMerchant(Map parmMap);
   	
   	// FULFILL MERCHANT INFORMATION
   	public int updateToMerchant(Map parmMap);
}
