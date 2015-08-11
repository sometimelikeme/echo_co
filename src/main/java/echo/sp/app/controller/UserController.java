package echo.sp.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import echo.sp.app.model.User;
import echo.sp.app.service.UserService;


@Controller
public class UserController {
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@RequestMapping("index")
    public String index(Model model){
        User user = new User();
        user.setNickname("jingwen");
        user.setState(2);
        userService.insertUser(user);
        
        logger.info("sssss - " + user);
        
        model.addAttribute("serverTime", "jingwen");
        return "index";
    }
	
	@RequestMapping("search")
    public String search(HttpServletRequest req,HttpServletResponse response){
        Map<String, Object> parmMap = new HashMap<String, Object>();
        parmMap.put("page", Integer.valueOf(req.getParameter("page")));
        parmMap.put("limit", Integer.valueOf(req.getParameter("limit")));
        logger.debug("parm : " + parmMap);
        List list = userService.getData(parmMap);
        logger.debug("List : " + list);
        req.setAttribute("ddd", list.toString());
        return "search";
    }
}
