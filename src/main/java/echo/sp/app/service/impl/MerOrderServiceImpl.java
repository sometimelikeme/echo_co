package echo.sp.app.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.command.page.PubTool;
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.dao.MerOrderDAO;
import echo.sp.app.dao.UserDAO;
import echo.sp.app.service.MerOrderService;

/**   
 * 商铺订单
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Service
public class MerOrderServiceImpl implements MerOrderService {
	
	private static final Logger logger = LoggerFactory.getLogger(MerOrderServiceImpl.class);
	
	@Autowired
	private MerOrderDAO merOrderDAO;
	
	@Autowired
	private UserDAO userDAO;

	@Override
	public int updateOrderComsume(Map parmMap) {
		return merOrderDAO.updateOrderComsume(parmMap);
	}

	@Override
	public int updateOrderClose(Map parmMap) {
		int returnInt = 0;
    	try {
    		// 关闭订单
    		merOrderDAO.updateOrderClose(parmMap);
    		// 返还积分
    		merOrderDAO.insertUserPoint(parmMap);
    		// 汇总积分
    		String totalPoint = merOrderDAO.getTotalPoint(parmMap);
    		BigDecimal pointNumBig = new BigDecimal(parmMap.get("POINT_NUM").toString());
    		BigDecimal totalPointBig;
    		if (PubTool.processNullAndEmpty(totalPoint)) {
    			totalPointBig = pointNumBig;
			} else {
				totalPointBig = new BigDecimal(totalPoint).add(pointNumBig);
			}
    		parmMap.put("TOTAL_POINT", totalPointBig);
    		merOrderDAO.updateUserTotalPoint(parmMap);
    		// 返还金额
    		Map merMap = merOrderDAO.getMerUserIdAndPay(parmMap);
    		BigDecimal payment = new BigDecimal(merMap.get("TOTAL_PAY").toString());
			BigDecimal total_money_Big = new BigDecimal(userDAO.getUserExpandInfo(merMap).get("TOTAL_MONEY").toString());
			merMap.put("TOTAL_MONEY", total_money_Big.add(payment));
			userDAO.updateUserMoney(merMap);
			// 金额记录
			Map tranMap = new HashMap();
			tranMap.put("USER_ID", merMap.get("USER_ID"));
			tranMap.put("TIME1", DateUtils.getDateTime());
			tranMap.put("DATE1", DateUtils.getToday());
			tranMap.put("MONEY_NUM", payment);
			tranMap.put("TASK_ID", "");
			tranMap.put("ORDER_ID", parmMap.get("ORDER_ID"));
			tranMap.put("MN_TYPE", "20");
			tranMap.put("ABLI_ORDER_ID", "");
			tranMap.put("PRE_PAID_ID", "");
			tranMap.put("STATUS", "10");
			userDAO.insertUserMoneyRecord(tranMap);
    		
    		returnInt = 1;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public Map getSingleCommentByUserId(Map parmMap) {
		return merOrderDAO.getSingleCommentByUserId(parmMap);
	}

	
	/**
	 * 删除评论
	 * 增加评论
	 * 修改订单评论状态
	 */
	@Override
	public int addComment(Map parmMap) {
		int returnInt = 0;
    	try {
    		merOrderDAO.deleteComment(parmMap);
    		merOrderDAO.addComment(parmMap);
    		merOrderDAO.updateOrderComment(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int deleteComment(Map parmMap) {
		int returnInt = 0;
    	try {
    		merOrderDAO.deleteComment(parmMap);
    		returnInt = 1;
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return returnInt;
	}

	@Override
	public int updateOrderForDelete(Map parmMap) {
		return merOrderDAO.updateOrderForDelete(parmMap);
	}

	@Override
	public List getConsumeOrders(Map parmMap) {
		return merOrderDAO.getConsumeOrders(parmMap);
	}

	@Override
	public List getOrderDetailByOrderId(Map parmMap) {
		return merOrderDAO.getOrderDetailByOrderId(parmMap);
	}
}
