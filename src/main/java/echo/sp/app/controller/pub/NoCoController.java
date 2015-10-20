package echo.sp.app.controller.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.model.SecCode;
import echo.sp.app.command.page.PubTool;
import echo.sp.app.command.utils.Encodes;
import echo.sp.app.service.UserService;

/**   
 * GET THE NORMAL CODE FOR CLIENT
 * @author Ethan   
 * @date 2015年8月23日 
 */
@Controller
public class NoCoController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(NoCoController.class);
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("login/getCode")
	public void getCode(HttpServletResponse response, @RequestParam String no_co) {
		if (logger.isDebugEnabled()) {
			logger.debug("NoCoController---getCode---no_co: " + no_co);
		}
		if (SecCode.getKey("NO_CO_ORI") == null) {
			SecCode.setKey("NO_CO_ORI", SecCode.no_co_org);
		}
		if (SecCode.getKey("NO_CO_ORI").equals(no_co)) {
			String NO_CO = Encodes.encodeBase64(no_co);
			SecCode.setKey("NO_CO", NO_CO);
			Map resMap = new HashMap(); 
			resMap.put("no_co", NO_CO);
			resMap.putAll(getEnumMap());
			super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, resMap, null);
		} else {
			logger.error("NoCoController---getCode---bad no_co: " + no_co);
			response.setStatus(404);
		}
	}
	
	/**
	 * 获取系统初始化参数
	 * @return
	 */
	private Map getEnumMap() {
		PageBounds pageBounds;
		List resList;
		Map resMap = new HashMap();
		pageBounds = new PageBounds(Order.formString("DICT_KEY.asc"));
		// 获取店铺分类信息枚举
		resList = PubTool.getResultList("UserDAO.getMerType", new HashMap(), pageBounds, sqlSessionFactory);
		// 获取商品一级分类枚举
		resList.addAll(PubTool.getResultList("UserDAO.getCategoryType", new HashMap(), pageBounds, sqlSessionFactory));
		// 获取任务一级分类枚举
		resList.addAll(PubTool.getResultList("UserDAO.getSectorType", new HashMap(), pageBounds, sqlSessionFactory));
		resMap.put("T_DICT", resList);
		// 获取商品二级分类枚举
		pageBounds = new PageBounds();
		resList = PubTool.getResultList("UserDAO.getItemCategory", new HashMap(), pageBounds, sqlSessionFactory);
		resMap.put("T_ITEMS_CATEGORY", resList);
		// 获取任务二级分类枚举
		pageBounds = new PageBounds();
		resList = PubTool.getResultList("UserDAO.getTaskSectors", new HashMap(), pageBounds, sqlSessionFactory);
		resMap.put("T_SECTORS", resList);
		return resMap;
	}
	
	/**
	 * 获取开通城市列表
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("login/openCities")
	public void openCities(HttpServletRequest req, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug("NoCoController---openCities---begin:");
		}
		try {
			List list = userService.getOpenCities(new HashMap());
			super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, list);
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("NoCoController---openCities---interface error: ", e);
		}
	}
}
