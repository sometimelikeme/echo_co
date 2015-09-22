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

	@Override
	public int addOrder(Map parmMap) {
		int returnInt = 0;
    	try {
    		if (userOrderDAO.addOrderHead((Map)parmMap.get("head")) > 0) {
    			List lineList = (List)parmMap.get("line");
    			if (userOrderDAO.addOrderLine(lineList) > 0) {
    				if (userOrderDAO.modifyItemQty(lineList) > 0) {
    					returnInt = 1;
					}
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
}
