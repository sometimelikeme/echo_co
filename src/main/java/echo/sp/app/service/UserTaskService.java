package echo.sp.app.service;

import java.util.Map;

/**  
 * 增加任务
 * @author Ethan   
 * @date 2015年8月29日 
 */
public interface UserTaskService {
	// 增加任务
	public int addTask(Map parmMap);
	// 根据任务号查询任务
	public Map getTaskInfoByTaskId(Map parmMap);
	// 修改任务
	public int updateTask(Map parmMap);
	// 删除任务
	public int deleteTask(Map parmMap);
	// 取消任务
	public int updateTaskForCancel(Map parmMap);
}
