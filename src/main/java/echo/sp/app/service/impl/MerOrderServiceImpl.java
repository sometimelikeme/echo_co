package echo.sp.app.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import echo.sp.app.command.page.PubTool;
import echo.sp.app.dao.MerOrderDAO;
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

	@Override
	public int updateOrderComsume(Map parmMap) {
		return merOrderDAO.updateOrderComsume(parmMap);
	}

	@Override
	public int updateOrderClose(Map parmMap) {
		int returnInt = 0;
    	try {
    		merOrderDAO.updateOrderClose(parmMap);
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
    		
    		// 将本次交易产生的费用返还到店铺账户中
    		// 获取用户消费所在店铺的UER_ID
    		Map merMap = merOrderDAO.getMerUserIdAndPay(parmMap);
    		String moneyNum = merMap.get("TOTAL_PAY").toString();
    		parmMap.put("MER_USER_ID", merMap.get("USER_ID"));
    		parmMap.put("MONEY_NUM", merMap.get("TOTAL_PAY"));
    		// 汇总总金额
    		String totalMoney = merOrderDAO.getTotalMoney(parmMap);
    		BigDecimal moneyNumBig = new BigDecimal(moneyNum);
    		BigDecimal totalmoneyBig;
    		if (PubTool.processNullAndEmpty(totalMoney)) {
    			totalmoneyBig = pointNumBig;
			} else {
				totalmoneyBig = new BigDecimal(totalMoney).add(moneyNumBig);
			}
    		parmMap.put("TOTAL_MONEY", totalmoneyBig);
    		merOrderDAO.updateUserTotalMoney(parmMap);
    		
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
