package echo.sp.app.dao;

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
}
