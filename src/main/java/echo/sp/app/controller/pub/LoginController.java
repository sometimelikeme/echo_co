package echo.sp.app.controller.pub;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.model.SecCode;
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.Encodes;
import echo.sp.app.command.utils.IdGen;
import echo.sp.app.command.utils.MD5Util;
import echo.sp.app.command.utils.SessionPro;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.command.utils.ValidUtils;
import echo.sp.app.service.UserService;

/**
 * USER LOGIN CONTROLLER
 * 1.CHECK IF REGISTER EXISTS
 * 2.REGIST USER AND LOGIN
 * 3.LOGIN
 * @author Ethan
 * 2015-8-26
 */
@Controller
public class LoginController extends CoreController{
	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	/**
	 * CHECK IF THE USER EXISTS 
	 * @param response
	 * @param tel
	 */
	@RequestMapping("login/checkReg")
	public void checkReg(HttpServletResponse response, @RequestParam String tel) { 
		if (logger.isDebugEnabled()) {
			logger.debug("UserController---checkReg---begin: " + tel);
		}
		try {
			if (!"".equals(tel) && ValidUtils.isMobileNO(tel)) {
				Map parmMap = new HashMap();
				parmMap.put("TEL", tel);
				int res = userService.getCheckReg(parmMap);
				parmMap = new HashMap();
				parmMap.put("IS_EXIST", res > 0 ? "1" : "0");
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, parmMap, null);
			} else {
				super.writeJson(response, "9998", "无效手机号码", null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserController---checkReg---interface error: ", e);
		}
		
	}
	
	
	/**
	 * REGIST USER AND LOGIN IN 
	 * @param req
	 * @param response
	 * @param tel
	 * @param pwd
	 */
	@RequestMapping("login/registAlg")
	public void registAlg(HttpServletRequest req, HttpServletResponse response,
			@RequestParam String tel, @RequestParam String pwd, @RequestParam String ut, @RequestParam String ct_code) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserController---registAlg---tel: " + tel + "; pwd: " + pwd);
		}
		
		// 限制只有用户端可以注册
		// 限制用户必须在移动设备上注册
		// 检测手机号
		// 检测密码长度
		if (!"10".equals(ut)) {
			super.writeJson(response, Code.FAIL, "无效客户端注册", null, null);
		} else if ("10".equals(ut) && !UserAgentUtils.isMobileOrTablet(req)) {
			super.writeJson(response, "9998", "无效注册设备", null, null);
		} else if (!ValidUtils.isMobileNO(tel)) {
			super.writeJson(response, "9997", "无效手机号码", null, null);
		} else if ("".equals(pwd) || pwd.length() < 6) {
			super.writeJson(response, "9996", "无效密码", null, null);
		} else {
			
			Map parmMap = new HashMap();
			parmMap.put("TEL", tel);
			
			if (userService.getCheckReg(parmMap) == 1) {
				super.writeJson(response, "9995", "该用户已注册", null, null);
			} else {
				try {
					pwd = MD5Util.getMD5String(Encodes.decodeBase64String(pwd));
					
					// GENERATE UNIQUE USER ID
					String user_id = IdGen.uuid();
					
					parmMap = new HashMap();
					parmMap.put("USER_ID", user_id);
					parmMap.put("USER_PWD", pwd);
					parmMap.put("USER_TYPE", "10");// DEFAULT USER TYPE
					parmMap.put("TEL_NUMBER", tel);
					parmMap.put("IN_VALID", "1");// DEFAULT VALID
					parmMap.put("CANT_CODE", ct_code);// DISTRICT CODE
					parmMap.put("REG_TIME", DateUtils.getDateTime());
					int res = userService.addRegistAlg(parmMap);
					
					parmMap = new HashMap();
					String VERFIY = "0", 
						   SEC_CODE = "", 
						   CODE = "9994",// DEAULT REGISTRATION FAILS
						   MSG = "注册失败";
					if (res == 1) {
						// SESSION INITIALIZATON
						parmMap.putAll(sessionInit(req, user_id, ut));
						SEC_CODE = IdGen.uuid();// GENERATE USER SECRATE CODE
						SecCode.setKey(user_id, SEC_CODE);
						VERFIY = "1";
						CODE = Code.SUCCESS;
						MSG = Code.SUCCESS_MSG;
					}
					parmMap.put("VERFIY", VERFIY);
					parmMap.put("SEC_CODE", SEC_CODE);
					parmMap.put("USER_ID", user_id);
					
					super.writeJson(response, CODE, MSG, parmMap, null);
					
				} catch (Exception e) {
					super.writeJson(response, "9992", "后台程序执行失败", null, null);
					logger.error("UserController---registAlg---interface error: ", e);
				}
			}
		}
		
	}
	
	/**
	 * REGIST USER AND LOGIN IN 
	 * @param req
	 * @param response
	 * @param acc
	 * @param pwd
	 */
	@RequestMapping("login/login")
	public void login(HttpServletRequest req, HttpServletResponse response,
			@RequestParam String tel, @RequestParam String pwd, @RequestParam String ut) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserController---login---tel: " + tel + "; pwd: " + pwd);
		}
		
		// 限制用户必须在移动设备上登陆
		if ("10".equals(ut) && !UserAgentUtils.isMobileOrTablet(req)) {
			super.writeJson(response, "9998", "无效登陆设备", null, null);
		} else if (!ValidUtils.isMobileNO(tel)) {
			super.writeJson(response, "9997", "无效手机号码", null, null);
		} else if ("".equals(pwd) || pwd.length() == 0) {
			super.writeJson(response, "9996", "无效密码", null, null);
		} else {
			try {
				
				pwd = MD5Util.getMD5String(Encodes.decodeBase64String(pwd));
				
				Map parmMap = new HashMap();
				parmMap.put("TEL", tel);
				parmMap.put("PWD", pwd);
				String user_id = userService.login(parmMap);
				
				int res = 0;
				if (user_id != null && !"".equals(user_id)) {
					res = 1;
					// 店铺登陆情况下,检查当前用户是否已经是店铺账号
					if ("20".equals(ut)) {
						parmMap = new HashMap();
						parmMap.put("USER_ID", user_id);
						if (userService.checkMerchant(parmMap) == 0) {
							super.writeJson(response, "9992", "您还未申请店铺", null, null);
							return;
						}
					}
				}
				
				parmMap = new HashMap();
				String VERFIY = "0",
					   SEC_CODE = "",
					   CODE = "9995",// DEAULT LOGIN FAILS
					   MSG = "账号不存在";
				if (res == 1) { // LOGIN PARAMETERS MATCHES
					// SESSION INITIALIZATON
					parmMap.putAll(sessionInit(req, user_id, ut));
					// GENERATE USER SECRATE CODE
					SEC_CODE = IdGen.uuid();
					SecCode.setKey(user_id, SEC_CODE);
					VERFIY = "1";
					CODE = Code.SUCCESS;
				    MSG = Code.SUCCESS_MSG;
				} else { // VERFITY IF THE PASSWORD IS INVALID THROUGH CHECK IF THE USER EXIST
					Map pMap = new HashMap();
					pMap.put("TEL", tel);
					if (userService.getCheckReg(pMap) > 0) {
						CODE = "9994";
						MSG = "密码错误";
					}
				}
				parmMap.put("VERFIY", VERFIY);
				parmMap.put("SEC_CODE", SEC_CODE);
				parmMap.put("USER_ID", user_id);
				
				super.writeJson(response, CODE, MSG, parmMap, null);
				
			} catch (Exception e) {
				super.writeJson(response, "9992", "后台程序执行失败", null, null);
				logger.error("UserController---login---interface error: ", e);
			}
		}
		
	}
	
	/**
	 * CHANGE PASSWORD
	 * @param req
	 * @param response
	 * @param acc
	 * @param pwd
	 */
	@RequestMapping("login/changePwd")
	public void changePwd(HttpServletRequest req, HttpServletResponse response,
			@RequestParam String tel, @RequestParam String pwd, @RequestParam String ut) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserController---login---tel: " + tel + "; pwd: " + pwd);
		}
		
		// 限制用户必须在移动设备上登陆
		if (!ValidUtils.isMobileNO(tel)) {
			super.writeJson(response, "9997", "无效手机号码", null, null);
		} else if ("".equals(pwd) || pwd.length() == 0) {
			super.writeJson(response, "9996", "无效密码", null, null);
		} else {
			try {
				
				pwd = MD5Util.getMD5String(Encodes.decodeBase64String(pwd));
				
				Map parmMap = new HashMap();
				parmMap.put("TEL", tel);
				parmMap.put("PWD", pwd);
				
				int res = userService.updatePwd(parmMap);
				
				Map paramMap = new HashMap();
				String VERFIY = "0", 
					   SEC_CODE = "", 
					   CODE = "9995",
					   MSG = "密码修改失败",
					   user_id = "";
				if (res == 1) {
					user_id = userService.login(parmMap);
					// SESSION INITIALIZATON
					paramMap.putAll(sessionInit(req, user_id, ut));
					// GENERATE USER SECRATE CODE
					SEC_CODE = IdGen.uuid();
					SecCode.setKey(user_id, SEC_CODE);
					VERFIY = "1";
					CODE = Code.SUCCESS;
					MSG = Code.SUCCESS_MSG;
				}
				paramMap.put("VERFIY", VERFIY);
				paramMap.put("SEC_CODE", SEC_CODE);
				paramMap.put("USER_ID", user_id);
				
				super.writeJson(response, CODE, MSG, paramMap, null);
				
			} catch (Exception e) {
				super.writeJson(response, "9992", "后台程序执行失败", null, null);
				logger.error("UserController---changePwd---interface error: ", e);
			}
		}
		
	}
	
	/**
	 * EXIT WIHT SESSION CLEAR 
	 * @param response
	 * @param tel
	 */
	@RequestMapping("login/exit")
	public void exit(HttpServletRequest req, HttpServletResponse response) {
		SessionPro.clearSession(req);
		super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
	}
	
	
	/**
	 * SESSION INITIALIZATION
	 * @param req
	 * @param user_id
	 */
	private Map sessionInit(HttpServletRequest req, String user_id, String ut){
		
		HttpSession session = req.getSession();
		
		Map resMap = new HashMap();
		
		Map parmMap = new HashMap();
		parmMap.put("USER_ID", user_id);
		parmMap.put("IN_VALID", "1");
		
		// Get User Base Information
		resMap = userService.getUserInfo(parmMap);
		
		// Get Merchant Base Information
		Map merMap = userService.getMerchantInfo(parmMap);
		if (merMap != null) {
			resMap.putAll(merMap);
			session.setAttribute("MERCHANT_ID", resMap.get("MERCHANT_ID").toString());
		}
		    
		session.setAttribute("user_id", user_id);
		session.setAttribute("CANT_CODE", resMap.get("CANT_CODE"));// District Number
		
		return resMap;
	}
}
