package echo.sp.app.controller;

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
import echo.sp.app.command.utils.Encodes;
import echo.sp.app.command.utils.IdGen;
import echo.sp.app.command.utils.MD5Util;
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
public class UserController extends CoreController{
	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
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
		if (!"".equals(tel)) {
			Map parmMap = new HashMap();
			parmMap.put("TEL", tel);
			int res = userService.getCheckReg(parmMap);
			parmMap = new HashMap();
			parmMap.put("IS_EXIST", res > 0 ? "1" : "0");
			super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, parmMap, null);
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
			@RequestParam String tel, @RequestParam String pwd) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserController---registAlg---tel: " + tel + "; pwd: " + pwd);
		}
		
		Map parmMap = new HashMap();
		parmMap.put("TEL", tel);
		
		if (userService.getCheckReg(parmMap) == 1) {
			super.writeJson(response, "0001", "该用户已注册", null, null);
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
				int res = userService.addRegistAlg(parmMap);
				
				parmMap = new HashMap();
				String VERFIY = "0", 
					   SEC_CODE = "", 
					   CODE = Code.FAIL,// DEAULT REGISTRATION FAILS
					   MSG = Code.FAIL_MSG;
				if (res == 1) {
					sessionInit(req, user_id);
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
				logger.error("UserController---registAlg---interface error: ", e);
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
			@RequestParam String tel, @RequestParam String pwd) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserController---login---tel: " + tel + "; pwd: " + pwd);
		}
		try {
			
			pwd = MD5Util.getMD5String(Encodes.decodeBase64String(pwd));
			
			Map parmMap = new HashMap();
			parmMap.put("TEL", tel);
			parmMap.put("PWD", pwd);
			String user_id = userService.login(parmMap);
			
			int res = 0;
			if (user_id != null && !"".equals(user_id)) {
				res = 1;
			}
			
			parmMap = new HashMap();
			String VERFIY = "0",
				   SEC_CODE = "",
				   CODE = Code.FAIL,// DEAULT LOGIN FAILS
				   MSG = Code.FAIL_MSG;
			if (res == 1) { // LOGIN PARAMETERS MATCHES
				sessionInit(req, user_id);
				SEC_CODE = IdGen.uuid();// GENERATE USER SECRATE CODE
				SecCode.setKey(user_id, SEC_CODE);
				VERFIY = "1";
				CODE = Code.SUCCESS;
			    MSG = Code.SUCCESS_MSG;
			} else { // VERFITY IF THE PASSWORD IS INVALID THROUGH CHECK IF THE USER EXIST
				Map pMap = new HashMap();
				pMap.put("TEL", tel);
				if (userService.getCheckReg(pMap) > 0) {
					CODE = "9998";
					MSG = "密码错误";
				}
			}
			parmMap.put("VERFIY", VERFIY);
			parmMap.put("SEC_CODE", SEC_CODE);
			parmMap.put("USER_ID", user_id);
			
			super.writeJson(response, CODE, MSG, parmMap, null);
			
		} catch (Exception e) {
			logger.error("UserController---login---interface error: ", e);
		}
	}
	
	/**
	 * SESSION INITIALIZATION
	 * @param req
	 * @param user_id
	 */
	private void sessionInit(HttpServletRequest req, String user_id){
		HttpSession session = req.getSession();
		session.setAttribute("user_id", user_id);
	}
	
}
