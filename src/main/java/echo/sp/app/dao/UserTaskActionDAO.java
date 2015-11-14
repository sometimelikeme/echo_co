package echo.sp.app.dao;

import java.util.List;
import java.util.Map;

/**  
 * 任务流程 
 * @author Ethan   
 * @date 2015年10月4日 
 */
public interface UserTaskActionDAO {
	// 竞标任务-插入T_TASKS_LINE竞标人信息;
	public int addTaskBider(Map parmMap);
	// 竞标任务-修改T_TASKS.TASK_STATUS = '30', BID_NUM数量自增1
	public int updateTaskBider(Map parmMap);
	
	// 修改T_TASKS_LINE竞标人信息为取消竞标状态
	public int updateCancelTaskBider(Map parmMap);
	// BID_NUM数量自减少1，若发现此时BID_NUM为0，则修改TASK_STATUS为10状态
	public int updateTaskStatus(Map parmMap);
	
	// 修改任务状态选定他人中标任务，加入中标时间戳
	public int updateTaskStatusForSec(Map parmMap);
	// 修改投标人TASK_IS_BIDE为1
	public int updateBiderStatus(Map parmMap);
	
	// 中标人回退任务
	public int updateBiderBackTask(Map parmMap);
	// 发布者回退任务
	public int updatePuberBackTask(Map parmMap);
	
	// 中标者完成任务
	public int updateTaskDone(Map parmMap);
	// 更新行表
	public int updateTaskLineDone(Map parmMap);
	
	// 未完成任务
	public int updateTaskUnFinish(Map parmMap);
	
	// 结束任务
	public int updateTaskFinish(Map parmMap);
	
	// 奖励本次任务金额
	public int updateTaskLineFinish(Map parmMap);
	
	// 获取未处理任务列表
	public List getUnProcessTasks(Map parmMap);
	
	// 判断任务是否已经过期
	public int judgeTaskDead(Map parmMap); 
}
