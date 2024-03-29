package echo.sp.app.dao;

import java.util.List;
import java.util.Map;

/**
 * USER REGISTER,LOGIN 
 * @author Ethan
 * @date 2015年8月29日 
 */
public interface UserDAO {
	
	// CHECK IF THE USER EXISTS 
    public int getCheckReg(Map parmMap);
    
    // REGIST USER AND LOGIN IN 
 	public int addRegistAlg(Map parmMap);
 	
 	// INIT USER EXPAND INFORMATION
  	public int addUserExpand(Map parmMap);
 	
    // LOGIN
  	public String login(Map parmMap);
  	
  	// GET MERCHANTS INFO
 	public Map getMerchantInfo(Map parmMap);
 	
 	// CHANGE PASSWORD
 	public int updatePwd(Map parmMap);
 	
 	// CHECK IF MERCHANT
   	public int checkMerchant(Map parmMap);
 	
 	// UPGRADE TO MERCHANT
  	public int insertToMerchant(Map parmMap);
  	
  	// INIT MERCHANT EXPAND INFORMATION
   	public int insertToMerchantExpand(Map parmMap);
  	
	// FULFILL MERCHANT INFORMATION
   	public int updateToMerchant(Map parmMap);
   	
   	// 登陆后获取用户基本信息
   	public Map getUserInfo(Map parmMap);
   	
   	// 完善用户基本信息
 	public int updateUserInfo(Map parmMap);
 	
 	// 完善用户身份证信息
 	public int updateUserIC(Map parmMap);
 	
 	// FULFILL USER BANK ACCOUNT
 	public int updateUserAcc(Map parmMap);
 	
 	// 获取用余额、积分、能力
 	public Map getUserExpandInfo(Map parmMap);
 	
 	// 修改用户余额
  	public int updateUserMoney(Map parmMap);
  	
  	// 生成用户金额交易记录
   	public int insertUserMoneyRecord(Map parmMap); 
   	
   	// 修改用户积分
   	public int updateUserPoint(Map parmMap);
   	
   	// 生成用户积分交易记录
	public int insertUserPointRecord(Map parmMap);
	
	// 获取开通城市列表
	public List getOpenCities(Map parmMap);
}
