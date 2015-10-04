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
}
