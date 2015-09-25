package echo.sp.app.model.paymodel;
/**  
 * 支付宝返回参数对象 
 * @author Ethan   
 * @date 2015年9月25日 
 */
public class ALI {
	private String bc_appid = "";
	private String discount = "";// 折扣; EP: 0
	private String payment_type = "";
	private String subject = "";// 订单标题; EP: 白开水 
	private String trade_no = "";// 支付宝交易号; EP: 2014040311001004370000361525 *
	private String buyer_email = "";// 买家支付宝账号可以是email或者手机号; EP: 13758698870 *
	private String gmt_create = "";// 交易创建时间; EP: 2008-10-22 20:49:31 *
	private String notify_type = "";// 通知类型; EP: trade_status_sync
	private String quantity = "";// 购买数量; EP: 1
	private String out_trade_no = "";// 商家内部交易号; EP: test_no *
	private String seller_id = "";// 卖家支付宝唯一用户号; EP: 2088911356553910
	private String notify_time = "";
	private String body = "";
	private String trade_status = "";// 交易状态; EP: WAIT_BUYER_PAY
	private String is_total_fee_adjust = "";
	private String total_fee = "";// 商品总价，单位为元; EP: 0.01 *
	private String seller_email = "";
	private String price = "";// 商品单价，单位为元; EP: 0.01 
	private String buyer_id = "";// 买家支付宝唯一用户号; EP: 2088802823343621
	private String notify_id = "";
	private String use_coupon = "";// 买家是否使用了红包 （N/Y); EP: N
	private String sign_type = "";
	private String sign = "";
	public String getBc_appid() {
		return bc_appid;
	}
	public void setBc_appid(String bc_appid) {
		this.bc_appid = bc_appid;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
	public String getBuyer_email() {
		return buyer_email;
	}
	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}
	public String getGmt_create() {
		return gmt_create;
	}
	public void setGmt_create(String gmt_create) {
		this.gmt_create = gmt_create;
	}
	public String getNotify_type() {
		return notify_type;
	}
	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getNotify_time() {
		return notify_time;
	}
	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTrade_status() {
		return trade_status;
	}
	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}
	public String getIs_total_fee_adjust() {
		return is_total_fee_adjust;
	}
	public void setIs_total_fee_adjust(String is_total_fee_adjust) {
		this.is_total_fee_adjust = is_total_fee_adjust;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getSeller_email() {
		return seller_email;
	}
	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getBuyer_id() {
		return buyer_id;
	}
	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}
	public String getNotify_id() {
		return notify_id;
	}
	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}
	public String getUse_coupon() {
		return use_coupon;
	}
	public void setUse_coupon(String use_coupon) {
		this.use_coupon = use_coupon;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
