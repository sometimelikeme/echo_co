package echo.sp.app.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.service.UserService;

@Controller
public class UserController extends CoreController{
	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping("checkReg")
	public void checkReg(HttpServletResponse response, @RequestParam String tel) {
		if (!"".equals(tel)) {
			Map parmMap = new HashMap();
			parmMap.put("TEL", tel);
			int res = userService.getCheckReg(parmMap);
			parmMap = new HashMap();
			parmMap.put("IS_EXIST", res > 0 ? "1" : "0");
			super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, parmMap, null);
		}
	}
}
