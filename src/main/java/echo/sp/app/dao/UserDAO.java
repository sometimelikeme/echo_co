package echo.sp.app.dao;

import java.util.Map;

public interface UserDAO {
	
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
}
