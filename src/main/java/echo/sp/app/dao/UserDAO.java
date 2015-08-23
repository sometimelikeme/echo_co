package echo.sp.app.dao;

import java.util.Map;

public interface UserDAO {
	// CHECK IF THE USER EXISTS 
    public int getCheckReg(Map<String, Object> parmMap);
}
