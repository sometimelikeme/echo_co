package echo.sp.app.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    		if (totalPoint == null || "".equals(totalPoint)) {
    			totalPointBig = pointNumBig;
			} else {
				totalPointBig = new BigDecimal(totalPoint).add(pointNumBig);
			}
    		parmMap.put("TOTAL_POINT", totalPointBig);
    		merOrderDAO.UpdateUserTotalPoint(parmMap);
    		
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
}
