package echo.sp.app.command.utils;

import javax.servlet.http.HttpServletRequest;

/**   
 * @author Ethan   
 * @date 2015年9月7日 
 */
public class SessionPro{
	
	public static void clearSession(HttpServletRequest req) {
		req.getSession().invalidate();
	}
}
