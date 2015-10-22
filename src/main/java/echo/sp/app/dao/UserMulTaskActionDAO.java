package echo.sp.app.dao;

import java.util.List;
import java.util.Map;

/**  
 * 多人任务 
 * @author Ethan   
 * @date 2015年10月22日 
 */
public interface UserMulTaskActionDAO {
	// 增加任务
	public int addTask(Map parmMap);
	// 根据任务号查询任务头表
	public Map getTaskHeadByTaskId(Map parmMap);
	// 根据任务号查询任务行表
	public List getTaskLineByTaskId(Map parmMap);
	// 修改任务
	public int updateTask(Map parmMap);
	// 删除任务
	public int deleteTask(Map parmMap);
	// 竞标任务-插入T_MUL_TASKS_LINE竞标人信息;
	public int addTaskBider(Map parmMap);
	// 竞标任务-修改T_MUL_TASKS.TASK_BID_STATUS = '20', TASK_BID_NUM数量自增1
	public int updateTaskBider(Map parmMap);
	// 中标者完成任务
	public int updateTaskDone(Map parmMap);
	// 未完成任务
	public int updateTaskUnFinish(Map parmMap);
	// 更新行表
	public int updateTaskLineDone(Map parmMap);
	// 根据任务号和竞标者ID查询信息
	public Map getTaskInfoByTaskUserId(Map parmMap);
	// 获取到期未关闭的任务
	public List getUnProcessTasks(Map parmMap);
	// 关闭任务
	public int updateTaskClose(Map parmMap);
	// 获取完成任务用户列表
	public List getDoneTaskers(Map parmMap);
	// 修改任务行表
	public int updateTaskLineClose(Map parmMap);
}
