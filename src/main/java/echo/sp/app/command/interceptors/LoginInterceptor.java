package echo.sp.app.command.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import echo.sp.app.command.model.SecCode;
import echo.sp.app.command.page.PubTool;
import echo.sp.app.command.utils.SessionPro;

/**    
 * INTERCEPTOR
 * @author Ethan   
 * @date 2015年8月10日 
 */
public class LoginInterceptor extends HandlerInterceptorAdapter{
	
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	
	// NORMAL ACCESS WITHOUT AN USER LOGIN STATUS.
	private static final String[] PUB_URL = {
		"login/getCode.do",// GET NORML CODE URI.
		"order/payOrder.do"// Support BeeCloud Call Back Interface  
	};
	
	// NORMAL ACCESS WITHOUT AN USER LOGIN STATUS.
	private static final String[] IGNORE_URI = {
		"login/checkReg.do",// 检查用户手机号是否已经注册
		"login/registAlg.do",// 注册账号并登陆系统
		"login/login.do",// 登陆系统
		"login/changePwd.do",// 修改密码
		"login/exit.do",// 退出
		"mer/searchMerItem.do",// 查询商品
		"mer/getMerList.do",// 查询店铺列表
		"mer/getMerDetail.do",// 查询店铺详情
		"task/searchTaskList.do",// 查询任务：查询任务列表
		"task/searchTaskById.do"// 查询任务：查询单个任务
	};
	
	// SECRET ACCESS NEED AN USER LOGIN STATUS.
	private static final String[] SECRET_URI = {
		"user/updateUserInfo.do",// 更新用户基本信息
		"user/updateUserIC.do",// 更新身份信息
		"user/updateUserAcc.do",// 更新账户信息
		"user/merApply.do",// 申请店铺权限
		"user/addMerColl.do",// 收藏店铺
		"user/addItemColl.do",// 收藏商品
		"user/deleteMerColl.do",// 删除收藏店铺
		"user/deleteItemColl.do",// 删除收藏商品
		"user/getMerColl.do",// 获取收藏店铺列表
		"user/getItemColl.do",// 获取收藏商品列表
		"user/getUserExpandInfo",// 获取用户扩展信息
		"mer/toMerchant.do",// 维护店铺信息
		"mer/addMerItem.do",// 增加商品
		"mer/updateMerItem.do",// 更新商品信息
		"mer/delMerItem.do",// 删除商品
		"mer/updateToPreItem.do",// 设置商品为团购商品
		"order/addOrder.do",// 下单
		"order/cancelOrder.do",// 取消
		"order/consuOrder.do",// 消费
		"order/closeOrder.do",// 结束
		"order/delOrder.do",// 删除
		"order/getOrders.do",// 获取订单列表-支持用户和店铺、以及根据订单别号等查询;根据订单号获取订单详情
		"comment/getSingleCommentByUserId.do",// 评论
		"comment/addUpdateComment.do",// 评论
		"comment/deleteComment.do",// 评论
		// 以下为任务相关
		"task/addTask.do",// 增加任务 - 发布者
		"task/deleteTask.do",// 删除任务 - 发布者
		"task/updateTask.do",// 修改任务：修改任务信息、修改任务前期状态 - 发布者
		"task/bideTask.do",// 竞标任务：竞标人发起
		"task/cancelBideTask.do",// 取消竞标：竞标人发起
		"task/chooseTasker.do",// 选择竞标人：发布者
		"task/bidBackTasker.do",// 中标人回退任务: 选定中标人之后，与中标人协商解除任务 - 中标人
		"task/backTasker.do",// 发布者回退任务: 选定中标人之后，与中标人协商解除任务 - 发布者
		"task/doneTask.do",// 完成任务: 竞标人发起
		"task/closeTask.do",// 结束任务: 发布者
		"task/searchBidTaskByUserId.do"// 查询用户竞标任务列表
	};
	 
    @SuppressWarnings("unused")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	
		boolean flag = false;// FINAL FILTER FLAG
		boolean find = false;// CHECK IF THE URL APPLYING EXISTS
		int reStatus = 451;// RESPONSE STATUS, UNKNOW ERROR DEFAULT
		
		String url = request.getRequestURL().toString();
		String no_co = request.getParameter("no_co");
		String sc_co = request.getParameter("sc_co");
		
		if (logger.isDebugEnabled()) {
			logger.debug("LoginInterceptor---preHandle---url: " + url + ";no_co: " + no_co + ";sc_co: " + sc_co);
		} 
		
		// FIRST GET THE NORMAL CODE, OR PAY INTERFACE
		if (isPubUrl(url, PUB_URL)) { 
			flag = true;
		} else if (no_co != null && sc_co != null) { // SECRET ACCESS.
			no_co = PubTool.processParm(no_co);
			sc_co = PubTool.processParm(sc_co);
			for (String s : SECRET_URI) {
				if (url.contains(s)) {
					find = true;
					break;
				}
			} 
			// GET USER FROM SESSION, COMPARE THE SECRET CODE.
			Object user_obj = request.getSession().getAttribute("user_id");
			if (user_obj == null) {
				reStatus = 449;// SESSION INVALID
			} else {
				// GET USER SC NO BASED ON USER ID
				String user_id = user_obj.toString();
				String user_sc_no = PubTool.processParm(SecCode.getKey(user_id));
				if (find && !"".equals(user_sc_no) && user_sc_no.equals(sc_co)) {
					flag = true;
				} else {
					SessionPro.clearSession(request);
					reStatus = 448;// RESPONSE 448, INVALID USER SECRET CODE 
				}
			}
		}  else if (no_co != null) {  // NORMAL APPLY
			for (String s : IGNORE_URI) { 
				if (url.contains(s)) {
					find = true;
					break;
				}
			}
			// COMPARE THE NORMAL CODE.
			String no_co_co = PubTool.processParm(SecCode.getKey("NO_CO"));
			if (find && !"".equals(no_co_co) && no_co_co.equals(no_co)) {
				flag = true;
			} else {
				reStatus = 447;// RESPONSE 447, NORMAL CODE WRONG
			}
		} else if (no_co == null) {
			reStatus = 450;// INVALID NORMAL CODE 
		} else {
			logger.error("LoginInterceptor---preHandle---url: " + url + ";no_co: " + no_co + ";sc_co: " + sc_co);
		}
		
		// IF NONE OF ABOVE EXISTS, PROCESS THIS ASK!
		// 451 UNKONW
		// 450 INVALID NORMAL CODE 
		// 449 INVALID SESSION
		// 448 INVALID USER SECRET CODE
		// 447 NORMAL CODE WRONG
        if (!flag) {
        	response.setStatus(reStatus);
        }
        
        return flag;
    }
 
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
    
    /**
     * 
     * @param url
     * @param urlArray
     * @return
     */
    private boolean isPubUrl(String url, String[] urlArray){
    	boolean b = false;
    	for (String s : urlArray) {
			if (url.contains(s)) {
				b = true;
				break;
			}
		} 
    	return b;
    }
}
