package echo.sp.app.service;

import java.util.Map;

/**
 * USER REGISTER,LOGIN 
 * @author Ethan
 *
 */
public interface UserService {
	
	// CHECK IF THE USER EXISTS 
	public int getCheckReg(Map<String, Object> parmMap);
}
