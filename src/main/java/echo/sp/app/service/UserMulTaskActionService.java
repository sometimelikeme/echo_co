package echo.sp.app.service;

import java.util.List;
import java.util.Map;

/**  
 * 多人任务  
 * @author Ethan   
 * @date 2015年10月22日 
 */
public interface UserMulTaskActionService {
	// 增加任务
	public int addTask(Map parmMap);
	// 根据任务号查询任务
	public Map getTaskInfoByTaskId(Map parmMap);
	// 修改任务
	public int updateTask(Map parmMap);
	// 删除任务
	public int deleteTask(Map parmMap);
	// 竞标任务
	public int addTaskBider(Map parmMap);
	// 中标者完成任务
	public int updateTaskDone(Map parmMap);
	// 未完成任务
	public int updateTaskUnFinish(Map parmMap);
	// 根据任务号和竞标者ID查询信息
	public Map getTaskInfoByTaskUserId(Map parmMap);
	// 获取到期未关闭的任务
	public List getUnProcessTasks(Map parmMap);
	// 关闭任务
	public int updateTaskClose(Map parmMap);
	// 获取完成任务用户列表
	public List getDoneTaskers(Map parmMap);
	// 定时关闭任务服务
	public int updateTaskProcess(List taskList);
	// 判断任务是否过期
	public int judgeMulTaskDead(Map parmMap);
}
