package echo.sp.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.dao.UserOrderDAO;
import echo.sp.app.service.UserOrderService;

/**   
 * 用户订单
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Service
public class UserOrderServiceImpl implements UserOrderService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserOrderServiceImpl.class);
	
	@Autowired
    private UserOrderDAO userOrderDAO;

	/**
	 * 执行订单头表和行表
	 */
	@Override
	public int addOrder(Map parmMap) {
		int returnInt = 0;
    	try {
    		if (userOrderDAO.addOrderHead((Map)parmMap.get("head")) > 0) {
    			List lineList = (List)parmMap.get("line");
    			if (userOrderDAO.addOrderLine(lineList) > 0) {
    				// if (userOrderDAO.modifyItemQty(lineList) > 0) {
    					returnInt = 1;
					// }
    			};
    		}
		} catch (Exception e) {
			//  默认spring事务只在发生未被捕获的 runtimeexcetpion时才回滚
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public Map getOrderDetail(Map parmMap) {
		
		Map resMap = new HashMap();
		
		resMap.put("HEAD", userOrderDAO.getOrderHead(parmMap));
		resMap.put("LINE", userOrderDAO.getOrderLine(parmMap));
		
		return resMap;
		
	}

	@Override
	public int updateOrderForCancel(Map parmMap) {
		return userOrderDAO.updateOrderForCancel(parmMap);
	}

	@Override
	public int updateOrderPay(Map parmMap) {
		int returnInt = 0;
    	try {
    		
    		Map payLogMap = (Map)parmMap.get("payLogMap");
    		Map payMap = (Map)parmMap.get("payMap");
    		Map upMap = (Map)parmMap.get("upMap");
    		
    		String pay_type = parmMap.get("pay_type").toString();
    		
    		if ("10".equals(pay_type)) {
				userOrderDAO.insertToAliLog(parmMap);
			} else if ("20".equals(pay_type)) {
				userOrderDAO.insertToWxLog(parmMap);
			} else if ("30".equals(pay_type)) {
				userOrderDAO.insertToUnLog(parmMap);  
			}
    		
    		userOrderDAO.insertToPayLog(payLogMap);
    		
    		userOrderDAO.updateOrderPay(upMap);
    		
			returnInt = 1; 
			
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}
}
