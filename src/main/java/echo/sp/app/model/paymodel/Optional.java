package echo.sp.app.model.paymodel;
/**   
 * 支付额外信息
 * @author Ethan   
 * @date 2015年9月25日 
 */
public class Optional {
	String ORDER_ID = "";// 订单号
	String RANDOM_ID = "";// 随机号
	String SECTION = "";// 当前随机号所在序列:支持枚举1,2,3,4
	String SIGN = "";// 签名
	String IS_CHARGE = "";// 是否为充值 1-充值 2-支付
	String USER_ID = "";// 充值用户ID
	public String getUSER_ID() {
		return USER_ID;
	}
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}
	public String getIS_CHARGE() {
		return IS_CHARGE;
	}
	public void setIS_CHARGE(String iS_CHARGE) {
		IS_CHARGE = iS_CHARGE;
	}
	public String getSIGN() {
		return SIGN;
	}
	public void setSIGN(String sIGN) {
		SIGN = sIGN;
	}
	public String getORDER_ID() {
		return ORDER_ID;
	}
	public void setORDER_ID(String oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}
	public String getRANDOM_ID() {
		return RANDOM_ID;
	}
	public void setRANDOM_ID(String rANDOM_ID) {
		RANDOM_ID = rANDOM_ID;
	}
	public String getSECTION() {
		return SECTION;
	}
	public void setSECTION(String sECTION) {
		SECTION = sECTION;
	}
}
