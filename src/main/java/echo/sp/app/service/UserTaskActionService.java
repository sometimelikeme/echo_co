package echo.sp.app.service;

import java.util.List;
import java.util.Map;

/** 
 * 用户任务流程  
 * @author Ethan   
 * @date 2015年10月4日 
 */
public interface UserTaskActionService {
	// 竞标任务
	public int addTaskBider(Map parmMap);
	// 取消竞标
	public int updateTaskBider(Map parmMap);
	// 选择中标人
	public int updateChooseTasker(Map parmMap);
	// 中标人回退任务
	public int updateBiderBackTask(Map parmMap);
	// 发布者回退任务
	public int updatePuberBackTask(Map parmMap);
	// 中标者完成任务
	public int updateTaskDone(Map parmMap);
	// 未完成任务
	public int updateTaskUnFinish(Map parmMap);
	// 结束任务
	public int updateTaskFinish(Map parmMap);
	// 结束任务
	public int updateTaskFinishPoint(Map parmMap);
	// 获取未处理任务列表
	public List getUnProcessTasks(Map parmMap);
}
