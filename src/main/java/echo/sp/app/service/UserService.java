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
	public int registAlg(Map parmMap);
	
	// LOGIN
	public int login(Map parmMap);
	
}
