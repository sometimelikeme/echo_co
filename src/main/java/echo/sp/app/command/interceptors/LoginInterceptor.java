package echo.sp.app.command.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	
	// GET NORML CODE URI.
	private static String NORMAL_URI = "login/getCode.do"; 
	
	// NORMAL ACCESS WITHOUT AN USER LOGIN STATUS.
	private static final String[] IGNORE_URI = {
		"login/checkReg.do",
		"login/registAlg.do",
		"login/login.do", 
		"login/changePwd.do"
	};
	
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
    	
		boolean flag = false;// FINAL FILTER FLAG
		boolean find = false;// CHECK IF THE URL APPLYING EXISTS
		int reStatus = 100;// RESPONSE STATUS
		
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
				reStatus = 449;// SESSION INVALID
			} else {
				// GET USER SC NO BASED ON USER ID
				String user_id = user_obj.toString();
				String user_sc_no = PubTool.processParm(SecCode.getKey(user_id));
				if (find && !"".equals(user_sc_no) && user_sc_no.equals(sc_co)) {
					flag = true;
				} else {
					reStatus = 448;// RESPONSE 448, INVALID USER SECRET CODE 
				}
			}
		}  else if (!"".equals(no_co)) {  // NORMAL APPLY
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
			} else {
				reStatus = 447;// RESPONSE 447, INVALID NORMAL CODE 
			}
		} else {
			logger.error("LoginInterceptor---preHandle---url: " + url + ";no_co: " + no_co + ";sc_co: " + sc_co);
			reStatus = 450;// UNKNOW ERROR
		}
		
		// IF NONE OF ABOVE EXISTS, PROCESS THIS ASK!
        if (!flag) {
        	response.setStatus(reStatus);
        }
        
        return flag;
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
