package echo.sp.app.controller.mer;

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
import echo.sp.app.command.utils.MD5Util;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.MerOrderService;
import echo.sp.app.service.PubToolService;
import echo.sp.app.service.UserOrderService;

/**  
 * 商铺订单 
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class MerOrderController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(MerOrderController.class);
	
	@Autowired
	private MerOrderService merOrderService;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private UserOrderService userOrderService;
	
	@Autowired
	private PubToolService pubToolService;
	
	/**
	 * 获取订单列表
	 * 1.默认传递USER_ID则分页获取用户下的所有订单列表，同时通过调用参数区分历史订单STATUS='70'
	 * 2.增加参数MERCHANT_ID则分页获取店铺下的所有订单列表
	 * 3.增加参数ORDER_ALIAS_ID则根据订单别号获取店铺下的订单列表
	 * 4.传递ORDER_ID则根据订单号获取唯一的订单信息详情: dataset返回订单信息；dataset_line返回订单的商品信息。
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("order/getOrders")
	public void getOrders(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MerOrderController---getOrders---dataParm: " + dataParm);
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else {
				
				Object order_id = paramMap.get("ORDER_ID");
				
				if (order_id != null) {
					Map parmMap = new HashMap();
					parmMap.put("ORDER_ID", order_id);
					Map reMap = userOrderService.getOrderDetail(parmMap);
					super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, (Map) reMap.get("HEAD"), (List) reMap.get("LINE"));
					return;
				}
				
				
				Object page = paramMap.get("page"),// PAGE NUMBER
					   pageSize  = paramMap.get("pageSize");// MAX ROWS RETURN
				String sortString = "ORDER_TIME.desc";// 默认按照下单时间倒序排序
				int pageInt,pageSizeInt;
				
				PageBounds pageBounds;
				List resList = null;
				
				Object mer_id = paramMap.get("MERCHANT_ID");
				Object alia_id = paramMap.get("ORDER_ALIAS_ID");
				
				if (mer_id == null) {// 分页用户获取所有订单
					pageInt = Integer.parseInt(page.toString());
					pageSizeInt = Integer.parseInt(pageSize.toString());
					pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
					resList = PubTool.getResultList("MerOrderDAO.getOrdersByUser", paramMap, pageBounds, sqlSessionFactory);
				} else {// 店铺获取旗下订单
					if (alia_id == null) {// 分页获取
						pageInt = Integer.parseInt(page.toString());
						pageSizeInt = Integer.parseInt(pageSize.toString());
						pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
						resList = PubTool.getResultList("MerOrderDAO.getOrdersByMerchant", paramMap, pageBounds, sqlSessionFactory);
					} else {// 根据订单别号获取
						pageBounds = new PageBounds(Order.formString(sortString));
						resList = PubTool.getResultList("MerOrderDAO.getOrdersByAliaId", paramMap, pageBounds, sqlSessionFactory);
					}
				}
				
				// dataset存放匹配总数据量
				if (alia_id == null) {
					int totalCount = 0;
					if (PubTool.isListHasData(resList)) {
						totalCount = ((PageList) resList).getPaginator().getTotalCount();
					}
					paramMap = new HashMap();
					paramMap.put("totalCount", totalCount);
				} else {
					paramMap = null;
				}
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, resList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("MerOrderController---getOrders---interface error: ", e);
		}
	}
	
	
	/**
	 * 用户消费订单，店铺根据用户提供的订单别号和验证码来执行消费
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("order/consuOrder")
	public void consuOrder(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MerOrderController---consuOrder---dataParm: " + dataParm);
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id"),
				   mer_id = (String) paramMap.get("MERCHANT_ID"), 
				   s_mer_id = (String) session.getAttribute("MERCHANT_ID"),
				   order_id = (String) paramMap.get("ORDER_ID");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"20".equals(ut)) {// Only Merchant has access
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else if (mer_id == null || (mer_id != null && mer_id.equals(s_mer_id))) {// 防止店铺消费自己
				super.writeJson(response, "9996", "无效店铺", null, null);
			} else {
				Map parmMap = new HashMap();
				parmMap.put("ORDER_ID", order_id);
				Map orderMap = (Map) (userOrderService.getOrderDetail(parmMap).get("HEAD"));
				// 无效订单号
				if (orderMap == null) {
					super.writeJson(response, "9995", "无效订单号", null, null);
					return;
				}
				// 只有状态为支付状态方可消费
				if (!"30".equals(orderMap.get("STATUS").toString())) {
					super.writeJson(response, "9994", "订单未支付", null, null);
					return;
				}
				// 判断验证码
				if (!paramMap.get("CAPTCHA").toString().equals(MD5Util.getMD5String(orderMap.get("CAPTCHA").toString()))) {
					super.writeJson(response, "9993", "验证码错误", null, null);
					return;
				}
				// 修改订单为消费状态
				paramMap.put("CONSUME_TIME", DateUtils.getDateTime());
				merOrderService.updateOrderComsume(paramMap);
				
				orderMap = userOrderService.getOrderDetail(parmMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, (Map) orderMap.get("HEAD"), (List) orderMap.get("LINE"));
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("MerOrderController---consuOrder---interface error: ", e);
		}
	}
	
	
	/**
	 * 用户关闭订单
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("order/closeOrder")
	public void closeOrder(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MerOrderController---closeOrder---dataParm: " + dataParm);
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id"),
				   order_id = (String) paramMap.get("ORDER_ID");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"10".equals(ut)) {
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				Map parmMap = new HashMap();
				parmMap.put("ORDER_ID", order_id);
				Map orderMap = (Map) (userOrderService.getOrderDetail(parmMap).get("HEAD"));
				// 无效订单号
				if (orderMap == null) {
					super.writeJson(response, "9996", "无效订单号", null, null);
					return;
				}
				// 只有状态为消费状态方可关闭
				if (!"50".equals(orderMap.get("STATUS").toString())) {
					super.writeJson(response, "9995", "订单未消费，不可关闭", null, null);
					return;
				}
				// 修改订单为关闭状态
				paramMap.put("SHUT_TIME", DateUtils.getDateTime());
				// 用于计算返还积分
				paramMap.put("USER_ID", orderMap.get("USER_ID"));
				// 计算积分
				parmMap = new HashMap();
				parmMap.put("CANT_CODE", (String) session.getAttribute("CANT_CODE"));
				parmMap.put("PARM_NAME", "PER_POINT_AWARD_ORDER");
				String per_point = PubTool.getOrgParm(parmMap, pubToolService);
				String pointNum = "0";
				if (!"".equals(per_point)) {
					pointNum = new BigDecimal(orderMap.get("TOTAL_PAY").toString())
							.multiply(new BigDecimal(per_point))
							.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).toString();
				}
				paramMap.put("POINT_NUM", pointNum);
				paramMap.put("TIME1", DateUtils.getDateTime());
				paramMap.put("DATE1", DateUtils.getToday());
				merOrderService.updateOrderClose(paramMap);
				
				parmMap = new HashMap();
				parmMap.put("ORDER_ID", order_id);
				orderMap = userOrderService.getOrderDetail(parmMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, (Map) orderMap.get("HEAD"), (List) orderMap.get("LINE"));
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("MerOrderController---closeOrder---interface error: ", e);
		}
	}
	
	
	/**
	 * 删除订单
	 * 订单的任何环节都可以删除订单
	 * 删除后的订单可以在历史检索到
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("order/delOrder")
	public void delOrder(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MerOrderController---delOrder---dataParm: " + dataParm);
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id"),
				   order_id = (String) paramMap.get("ORDER_ID");
			
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"10".equals(ut)) {
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				Map parmMap = new HashMap();
				parmMap.put("ORDER_ID", order_id);
				Map orderMap = (Map) (userOrderService.getOrderDetail(parmMap).get("HEAD"));
				// 无效订单号
				if (orderMap == null) {
					super.writeJson(response, "9996", "无效订单号", null, null);
					return;
				}
				// 修改订单为删除状态
				paramMap.put("DEL_TIME", DateUtils.getDateTime());
				
				merOrderService.updateOrderForDelete(paramMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("MerOrderController---delOrder---interface error: ", e);
		}
	}
}
