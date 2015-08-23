package echo.sp.app.dao;

import java.util.Map;

public interface UserDAO {
	
	// CHECK IF THE USER EXISTS 
    public int getCheckReg(Map parmMap);
    
    // REGIST USER AND LOGIN IN 
 	public int registAlg(Map parmMap);
 	
    // LOGIN
  	public int login(Map parmMap);
}
