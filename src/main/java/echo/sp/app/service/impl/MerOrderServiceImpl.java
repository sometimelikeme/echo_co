package echo.sp.app.service.impl;

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
}
