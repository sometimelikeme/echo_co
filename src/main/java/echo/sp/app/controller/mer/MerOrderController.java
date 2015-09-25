package echo.sp.app.controller.mer;

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
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.MerOrderService;
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
	
	/**
	 * 获取订单列表
	 * 1.默认传递USER_ID则分页获取用户下的所有订单列表
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
			} else if (!"10".equals(ut)) {// Only user has access
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
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
				
				if (mer_id == null) {// 分页用户获取所有订单
					pageInt = Integer.parseInt(page.toString());
					pageSizeInt = Integer.parseInt(pageSize.toString());
					pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
					resList = PubTool.getResultList("MerOrderDAO.getOrdersByUser", paramMap, pageBounds, sqlSessionFactory);
				} else {// 店铺获取旗下订单
					Object alia_id = paramMap.get("ORDER_ALIAS_ID");
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
				int totalCount = 0;
				if (PubTool.isListHasData(resList)) {
					totalCount = ((PageList) resList).getPaginator().getTotalCount();
				}
				paramMap = new HashMap();
				paramMap.put("totalCount", totalCount);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, paramMap, resList);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("MerOrderController---getOrders---interface error: ", e);
		}
	}
}
