package echo.sp.app.controller.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import echo.sp.app.command.page.PubTool;
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.Prop;
import echo.sp.app.service.MerOrderService;

/**   
 * Scheduled
 * @author Ethan   
 * @date 2015年9月30日 
 */
@Component 
public class MyTaskXml {
	
	private static final Logger logger = LoggerFactory.getLogger(MyTaskXml.class);
	
	@Autowired
	private MerOrderService merOrderService;
	
	/**
	 * 定时关闭已消费未关闭的订单
	 */
	public void closeOrder(){  
        if (logger.isDebugEnabled()) {
			logger.debug("MyTaskXml---closeOrder---begin");
		}
        
        // 获取已消费，未关闭的订单列表
        List consumeNotCloseOrderList = merOrderService.getConsumeOrders(new HashMap());
        
        if (PubTool.isListHasData(consumeNotCloseOrderList)) {
        	
			String currDate = DateUtils.getDateTime();
			Iterator iterList = consumeNotCloseOrderList.iterator();
			Map paramMap;
			String consule_time;
			double closeOrderGap = Double.parseDouble(Prop.getString("system.closeOrderGap"));// 设定关闭间隔/天
			String perPointAwardOrder = Prop.getString("system.closeOrderGap");// 未主动关闭订单的积分这算百分比
			
			while (iterList.hasNext()) {
				paramMap = (Map)iterList.next();
				consule_time = paramMap.get("CONSUME_TIME").toString();
				if (DateUtils.getDistanceOfTwoDate(DateUtils.parseStringDate(consule_time), DateUtils.parseStringDate(currDate)) >= closeOrderGap) {
					if (logger.isDebugEnabled()) {
						logger.debug("MyTaskXml---closeOrder---paramMap：" + paramMap);
					}
					// 自动关闭订单
					// 修改订单为关闭状态-时间
					paramMap.put("SHUT_TIME", DateUtils.getDateTime());
					// 按照未主动关闭订单的积分这算百分比计算积分
					String pointNum = new BigDecimal(paramMap.get("TOTAL_PAY").toString())
							.multiply(new BigDecimal(perPointAwardOrder))
							.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).toString();
					paramMap.put("POINT_NUM", pointNum);
					paramMap.put("TIME1", DateUtils.getDateTime());
					paramMap.put("DATE1", DateUtils.getToday());
					merOrderService.updateOrderClose(paramMap);
					if (logger.isDebugEnabled()) {
						logger.debug("MyTaskXml---closeOrder---done!");
					}
				}
			}
		}
    }  
}
