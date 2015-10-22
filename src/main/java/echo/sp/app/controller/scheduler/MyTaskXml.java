package echo.sp.app.controller.scheduler;

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
import echo.sp.app.service.UserTaskActionService;
import echo.sp.app.service.UserTaskService;

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
	
	@Autowired
	private UserTaskActionService userTaskActionService;
	
	@Autowired
	private UserTaskService userTaskService;
	
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
	
	
	/**
	 * 定时关闭逾期的任务
	 */
	public void closeTask(){  
        if (logger.isDebugEnabled()) {
			logger.debug("MyTaskXml---closeTask---begin");
		}
        
        // 获取未关闭的任务列表
        List taskList = userTaskActionService.getUnProcessTasks(new HashMap());
        
        if (PubTool.isListHasData(taskList)) {
        	
			String currDate = DateUtils.getDateTime();
			Iterator iterList = taskList.iterator();
			Map paramMap;
			Map taksMap;
			String consule_time;
			String task_bid_status;
			String bid_user_id;
			
			while (iterList.hasNext()) {
				paramMap = (Map)iterList.next();
				consule_time = paramMap.get("TASK_DEADLINE").toString();// 截止日期
				if (DateUtils.getDistanceOfTwoDate(DateUtils.parseStringDate(consule_time), DateUtils.parseStringDate(currDate)) > 0) {
					if (logger.isDebugEnabled()) {
						logger.debug("MyTaskXml---closeTask---paramMap：" + paramMap);
					}
					task_bid_status = paramMap.get("TASK_BID_STATUS").toString();
					paramMap.put("TASK_FINISH_TIME", currDate);
					if ("60".equals(task_bid_status)) {
						// 关闭任务
						// 将此次任务发生的金额打到任务中标者账户
						// 获取中标人ID
						paramMap.put("IS_BIDE", "10");
						taksMap = userTaskService.getTaskInfoByTaskId(paramMap);
						paramMap.put("BIDE_USER_ID", ((Map) ((List) taksMap
								.get("TASK_LINE")).get(0)).get("USER_ID")
								.toString());
					    userTaskActionService.updateTaskFinish(paramMap);
					} else if ("10".equals(task_bid_status)// 10-发布
							|| "20".equals(task_bid_status)// 20-取消
							|| "30".endsWith(task_bid_status)// 30-他人投标任务
							|| "40".equals(task_bid_status)// 40-选定他人中标任务
							|| "49".equals(task_bid_status)// 49-中标人确认不做任务
							|| "50".equals(task_bid_status)// 50-退回任务
							|| "61".equals(task_bid_status)) {// 61-任务发布者点击未完成
						// 关闭任务
						// 将此次任务发生的金额打到任务发布者账户
						paramMap.put("BIDE_USER_ID", paramMap.get("USER_ID"));
						paramMap.put("RETURN_MONEY", "1");// 退款平台不收取费用
						userTaskActionService.updateTaskFinish(paramMap);
					}
					if (logger.isDebugEnabled()) {
						logger.debug("MyTaskXml---closeTask---done!");
					}
				}
			}
		}
    } 
	
	
	/**
	 * 定时关闭逾期的多人任务
	 */
	public void closeMulTask(){  
        if (logger.isDebugEnabled()) {
			logger.debug("MyTaskXml---closeMulTask---begin");
		}
       
    }  
}
