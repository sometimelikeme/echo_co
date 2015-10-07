package echo.sp.app.dao;

import java.util.List;
import java.util.Map;

/**   
 * 用户任务
 * @author Ethan   
 * @date 2015年8月29日 
 */
public interface UserTaskDAO {
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
	// 取消任务
	public int updateTaskForCancel(Map parmMap);
}
