package echo.sp.app.command.utils;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**   
 * @author Ethan   
 * @date 2015年9月7日 
 */
public class SessionPro{
	
	public static void clearSession(HttpServletRequest req) {
		// req.getSession().invalidate();
		HttpSession session = req.getSession();
		Enumeration em = session.getAttributeNames();
		while (em.hasMoreElements()) {
			session.removeAttribute(em.nextElement().toString());
		}
	}
}
