package echo.sp.app.command.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import echo.sp.app.command.model.SecCode;
import echo.sp.app.command.page.PubTool;

/**   
 * INTERCEPTOR
 * @author Ethan   
 * @date 2015��8��10�� 
 */
public class LoginInterceptor extends HandlerInterceptorAdapter{
	
	// GET NORML CODE URI.
	private static String NORMAL_URI = "/getCode.do";
	
	// NORMAL ACCESS WITHOUT AN USER LOGIN STATUS.
	private static final String[] IGNORE_URI = {"/regist.do","/registalg.do","login.do"};
	
	// SECRET ACCESS NEED AN USER LOGIN STATUS.
	private static final String[] SECRET_URI = {};
	 
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
		boolean flag = false;
		boolean find = false;
		
		String url = request.getRequestURL().toString();
		String no_co = PubTool.processParm(request.getParameter("no_co"));
		String sc_co = PubTool.processParm(request.getParameter("sc_co"));
		
		if (url.indexOf(NORMAL_URI) > 0) { // FIRST GET THE NORMAL CODE.
			flag = true;
		} else if (!"".equals(no_co) && !"".equals(sc_co)) { // SECRET ACCESS.
			for (String s : SECRET_URI) {
				if (url.contains(s)) {
					find = true;
					break;
				}
			}
			// GET USER FROM SESSION, COMPARE THE SECRET CODE.
			String user_id = (String) request.getSession().getAttribute("user_id");
			if (find && SecCode.getKey(user_id).equals(sc_co)) {
				flag = true;
			}
		}  else if (!"".equals(no_co)) {  // COMPARE THE NORMAL CODE.
			for (String s : IGNORE_URI) { 
				if (url.contains(s)) {
					find = true;
					break;
				}
			}
			if (find && SecCode.getKey("NO_CO").equals(no_co)) {
				flag = true;
			}
		}
		
		// IF NONE OF ABOVE EXISTS, PROCESS THIS ASK!
        if (!flag) {
            // T_supplier_user user = LoginController.getLoginUser(request);
            // if (user != null) flag = true;
        }
        
        return flag;
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
