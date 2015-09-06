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
 * @date 2015年8月10日 
 */
public class LoginInterceptor extends HandlerInterceptorAdapter{
	
	// GET NORML CODE URI.
	private static String NORMAL_URI = "login/getCode.do"; 
	
	// NORMAL ACCESS WITHOUT AN USER LOGIN STATUS.
	private static final String[] IGNORE_URI = {"login/checkReg.do","login/registAlg.do","login/login.do", "login/changePwd.do"};
	
	// SECRET ACCESS NEED AN USER LOGIN STATUS.
	private static final String[] SECRET_URI = {
		"user/updateUserInfo.do",
		"user/updateUserIC.do",
		"user/updateUserAcc",
		"user/merApply.do",
		"mer/toMerchant.do",
		"mer/addMerItem.do",
		"mer/updateMerItem.do",
		"mer/searchMerItem.do",
		"mer/delMerItem.do"
	};
	 
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
		boolean flag = false;
		boolean find = false;
		boolean sessionInvalid = false;
		
		String url = request.getRequestURL().toString();
		String no_co = PubTool.processParm(request.getParameter("no_co"));
		String sc_co = PubTool.processParm(request.getParameter("sc_co"));
		
		if (url.contains(NORMAL_URI)) { // FIRST GET THE NORMAL CODE.
			flag = true;
		} else if (!"".equals(no_co) && !"".equals(sc_co)) { // SECRET ACCESS.
			for (String s : SECRET_URI) {
				if (url.contains(s)) {
					find = true;
					break;
				}
			}
			// GET USER FROM SESSION, COMPARE THE SECRET CODE.
			Object user_obj = request.getSession().getAttribute("user_id");
			if (user_obj == null) {
				sessionInvalid = true;
			} else {
				// GET USER SC NO BASED ON USER ID
				String user_id = user_obj.toString();
				String user_sc_no = PubTool.processParm(SecCode.getKey(user_id));
				if (find && !"".equals(user_sc_no) && user_sc_no.equals(sc_co)) {
					flag = true;
				}
			}
		}  else if (!"".equals(no_co)) {  
			for (String s : IGNORE_URI) { 
				if (url.contains(s)) {
					find = true;
					break;
				}
			}
			// COMPARE THE NORMAL CODE.
			String no_co_co = PubTool.processParm(SecCode.getKey("NO_CO"));
			if (find && !"".equals(no_co_co) && no_co_co.equals(no_co)) {
				flag = true;
			}
		}
		
		// IF NONE OF ABOVE EXISTS, PROCESS THIS ASK!
        if (!flag) {
        	// DEFAULT RESPONSE 448, INVALID USER SECRET CODE 
        	int reStatus = 448;
        	if (sessionInvalid) {
        		reStatus = 449;// SESSION INVALID
			}
        	response.setStatus(reStatus);
        }
        
        return flag;
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
