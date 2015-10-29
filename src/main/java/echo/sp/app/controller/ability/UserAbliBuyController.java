package echo.sp.app.controller.ability;

import java.math.BigDecimal;
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
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.page.PubTool;
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.IdGen;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.PubToolService;
import echo.sp.app.service.UserAblityService;
import echo.sp.app.service.UserService;

/**   
 * @author Ethan   
 * @date 2015年10月29日 
 */
@Controller
public class UserAbliBuyController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserAbliBuyController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PubToolService pubToolService;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private UserAblityService userAblityService;
	
	
	/**
	 * 购买技能
	 * 产生技能订单
	 * 产生用户支付
	 * 产生系统金额
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/buyAbility")
	public void buyAbility(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAbliBuyController---buyAbility---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
				// 判断用户余额
				BigDecimal payment = new BigDecimal(paramMap.get("TOTAL_PAY").toString());
				BigDecimal total_money_Big = new BigDecimal(userService.getUserExpandInfo(paramMap).get("TOTAL_MONEY").toString());
				if (total_money_Big.compareTo(payment) < 0) {
					super.writeJson(response, "9996", "余额不足，请充值！", null, null);
					return;
				}
				
				String order_id = IdGen.uuid();
				paramMap.put("ABLI_ORDER_ID", order_id);
				paramMap.put("TOTAL_MONEY", total_money_Big);
				
				userAblityService.addBuyAbility(paramMap);
				
				// ABLI_ORDER 技能订单详情
				// ABLI_INFO 技能详情
				paramMap = userAblityService.getAbliOrderById(paramMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAbliBuyController---buyAbility---interface error: ", e);
		}
	}
	
	
	/**
	 * 技能拥有者拒绝
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/declineContract")
	public void declineContract(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAbliBuyController---declineContract---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
				Map resMap = (Map)userAblityService.getAbliOrderById(paramMap).get("ABLI_ORDER");
				
				if ("10".equals(resMap.get("STATUS").toString())) {
					super.writeJson(response, "9996", "状态错误！", null, null);
				}
				
				paramMap.put("BACK_TIME", DateUtils.getDateTime());
				
				userAblityService.updateDeclineContract(paramMap);
				
				// ABLI_ORDER 技能订单详情
				// ABLI_INFO 技能详情
				paramMap = userAblityService.getAbliOrderById(paramMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAbliBuyController---declineContract---interface error: ", e);
		}
	}
	
	
	/**
	 * 技能拥有者确认
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/confirmContract")
	public void confirmContract(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAbliBuyController---confirmContract---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
				Map resMap = (Map)userAblityService.getAbliOrderById(paramMap).get("ABLI_ORDER");
				
				if ("10".equals(resMap.get("STATUS").toString())) {
					super.writeJson(response, "9996", "状态错误！", null, null);
				}
				
				paramMap.put("START_TIME", DateUtils.getDateTime());
				
				userAblityService.updateConfirmContract(paramMap);
				
				// ABLI_ORDER 技能订单详情
				// ABLI_INFO 技能详情
				paramMap = userAblityService.getAbliOrderById(paramMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAbliBuyController---confirmContract---interface error: ", e);
		}
	}
	
	
	/**
	 * 技能拥有者完成技能
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/doneAbility")
	public void doneAbility(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAbliBuyController---doneAbility---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
				Map resMap = (Map)userAblityService.getAbliOrderById(paramMap).get("ABLI_ORDER");
				
				if ("30".equals(resMap.get("STATUS").toString())) {
					super.writeJson(response, "9996", "状态错误！", null, null);
				}
				
				paramMap.put("DELI_TIME", DateUtils.getDateTime());
				
				userAblityService.updateDoneAbility(paramMap);
				
				// ABLI_ORDER 技能订单详情
				// ABLI_INFO 技能详情
				paramMap = userAblityService.getAbliOrderById(paramMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAbliBuyController---doneAbility---interface error: ", e);
		}
	}
	
	
	/**
	 * 购买者确认完成 
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/confirmDone")
	public void confirmDone(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAbliBuyController---confirmDone---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
				Map resMap = (Map)userAblityService.getAbliOrderById(paramMap).get("ABLI_ORDER");
				
				if ("40".equals(resMap.get("STATUS").toString())) {
					super.writeJson(response, "9996", "状态错误！", null, null);
				}
				
				userAblityService.updateConfirmDone(paramMap);
				
				// ABLI_ORDER 技能订单详情
				// ABLI_INFO 技能详情
				paramMap = userAblityService.getAbliOrderById(paramMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAbliBuyController---confirmDone---interface error: ", e);
		}
	}
	
	
	/**
	 * 查询订单详情
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/getAbliOrderById")
	public void getAbliOrderById(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAbliBuyController---getAbliOrderById---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
				// ABLI_ORDER 技能订单详情
				// ABLI_INFO 技能详情
				paramMap = userAblityService.getAbliOrderById(paramMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAbliBuyController---getAbliOrderById---interface error: ", e);
		}
	}
	
	
	/**
	 * 查询订单列表
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("abli/getAbliOrders")
	public void getAbliOrders(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserAbliBuyController---getAbliOrders---begin");
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				String sortString = paramMap.get("sort").toString();
				int pageInt = Integer.parseInt(paramMap.get("page").toString());// PAGE NUMBER
				int pageSizeInt = Integer.parseInt(paramMap.get("pageSize").toString());// MAX ROWS RETURN
				PageBounds pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
				// 前者获取购买我的订单的信息列表，后者获取我购买的订单信息列表
				String exeString = "UserAblityDAO." + ("1".equals(paramMap.get("sort").toString()) ? "getAbliBuyOrders" : "getAbliOrders");
				List resList = PubTool.getResultList(exeString, paramMap, pageBounds, sqlSessionFactory);
				Map resMap = new HashMap();
				int totalCount = 0;
				if (PubTool.isListHasData(resList)) {
		    		totalCount = ((PageList) resList).getPaginator().getTotalCount();
				}
				resMap.put("totalCount", totalCount);
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, resMap, resList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserAbliBuyController---getAbliOrders---interface error: ", e);
		}
	}
}
