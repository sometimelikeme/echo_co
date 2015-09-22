package echo.sp.app.controller.user;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.page.PubTool;
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.IdGen;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.MerItemService;
import echo.sp.app.service.UserOrderService;

/**  
 * User Order
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class UserOrderController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserOrderController.class);
	
	@Autowired
	private UserOrderService userOrderService;
	
	@Autowired
	private MerItemService merItemService;
	
	/**
	 * Make Order
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("order/addOrder")
	public void addOrder(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserOrderController---addOrder---dataParm: " + dataParm);
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   mer_id = (String) paramMap.get("MERCHANT_ID"),
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id"),
				   s_mer_id = (String) session.getAttribute("MERCHANT_ID");
			
			// Get and compare with user id in session
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"10".equals(ut)) {// Only user has access
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else if (mer_id != null && mer_id.equals(s_mer_id)) {
				super.writeJson(response, "9996", "不能购买本店商品", null, null);
			} else {
				
				List itemList = data.getDataset_line();
				
				if (!PubTool.isListHasData(itemList)) {
					super.writeJson(response, "9995", "订单参数缺乏商品信息", null, null);
				}
				
				// Generate order id and order time
				String order_id = IdGen.uuid();
				paramMap.put("ORDER_ID", order_id);
				paramMap.put("ORDER_TIME", DateUtils.getDateTime());
				
				// Compare inventory and quantity
				String inventory;
				String qty_sold;
				Map temMap;
				Boolean isBreak = false;
				for (int i = 0; i < itemList.size(); i++) {
					
					temMap = (Map) itemList.get(i);
					
					inventory = merItemService.getItemInvtentory(temMap);
					
					qty_sold = temMap.get("QTY_SOLD").toString();
					
					if (inventory == null || (inventory != null && new BigDecimal(inventory).compareTo(new BigDecimal(qty_sold)) < 0)) {
						super.writeJson(response, "9994", "商品：" + temMap.get("ITEM_NAME") + " 库存不足", null, null);
						isBreak = true;
						break;
					}
					
					temMap.put("ORDER_ID", order_id);
				}
				
				if (isBreak) {
					return;
				}
				
				Map parmMap = new HashMap();
				parmMap.put("head", paramMap);
				parmMap.put("line", itemList);
				
				userOrderService.addOrder(parmMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserOrderController---addOrder---interface error: ", e);
		}
	}
}
