package echo.sp.app.controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.page.PubTool;
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.IdGen;
import echo.sp.app.command.utils.MD5Util;
import echo.sp.app.command.utils.Prop;
import echo.sp.app.command.utils.RandomUtil;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.model.paymodel.WX;
import echo.sp.app.service.MerItemService;
import echo.sp.app.service.UserOrderService;
import echo.sp.app.service.UserService;
import echo.sp.app.model.paymodel.*;

/**  
 * User Order
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class UserOrderController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UserOrderController.class);
	
	@Autowired
	private UserOrderService userOrderService;
	
	@Autowired
	private MerItemService merItemService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 用户下单接口
	 * 参数说明请参考接口说明
	 * 1. 插入头表数据，批量插入行表数据
	 * 2. 执行结束后查询订单信息返回
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("order/addOrder")
	public void addOrder(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserOrderController---addOrder---dataParm: " + dataParm);
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   mer_id = (String) paramMap.get("MERCHANT_ID"),
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id"),
				   s_mer_id = (String) session.getAttribute("MERCHANT_ID");
			
			// Get and compare with user id in session
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"10".equals(ut)) {// Only user has access
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else if (mer_id != null && mer_id.equals(s_mer_id)) {
				super.writeJson(response, "9996", "不能购买本店商品", null, null);
			} else {
				
				// Dataset_line存放购买商品信息
				List itemList = data.getDataset_line();
				
				if (!PubTool.isListHasData(itemList)) {
					super.writeJson(response, "9995", "订单参数缺乏商品信息", null, null);
				}
				
				// Generate order id and order time
				String order_id = IdGen.uuid();
				paramMap.put("ORDER_ID", order_id);
				paramMap.put("ORDER_TIME", DateUtils.getDateTime());
				paramMap.put("ORDER_ALIAS_ID", RandomUtil.generateNumString(12));
				
				// Compare inventory and quantity
				// Compute New Inventory
				Map temMap;
				Map resMap;
				Boolean isBreak = false;
				BigDecimal inventory_de;// 库存量
				BigDecimal qty_sold_de;// 销量
				BigDecimal qty_sold;// 需求量
				for (int i = 0; i < itemList.size(); i++) {
					
					temMap = (Map) itemList.get(i);
					
					resMap = merItemService.getItemInvtentory(temMap);
					
					if (resMap == null) {
						super.writeJson(response, "9994", "商品：" + temMap.get("ITEM_NAME") + " 库存不足", null, null);
						isBreak = true;
					}
					
					inventory_de = new BigDecimal(resMap.get("INVENTORY").toString());// 当前库存
					
					qty_sold_de = new BigDecimal(resMap.get("QTY_SOLD").toString());// 当前销量
					
					qty_sold = new BigDecimal(temMap.get("QTY_SOLD").toString());// 本次订单销量
					
					if (inventory_de.compareTo(qty_sold) < 0) {
						super.writeJson(response, "9994", "商品：" + temMap.get("ITEM_NAME") + " 库存不足", null, null);
						isBreak = true;
						break;
					}
					
					temMap.put("ORDER_ID", order_id);
				}
				
				if (isBreak) {
					return;
				}
				
				Map parmMap = new HashMap();
				parmMap.put("head", paramMap);
				parmMap.put("line", itemList);
				
				userOrderService.addOrder(parmMap);
				
				parmMap = new HashMap();
				parmMap.put("ORDER_ID", order_id);
				
				Map reMap = userOrderService.getOrderDetail(parmMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, (Map) reMap.get("HEAD"), (List) reMap.get("LINE"));
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserOrderController---addOrder---interface error: ", e);
		}
	}
	
	/**
	 * 取消订单
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("order/cancelOrder")
	public void cancelOrder(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserOrderController---cancelOrder---dataParm: " + dataParm);
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			// Get and compare with user id in session
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"10".equals(ut)) {// Only user has access
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
				Map reMap = userOrderService.getOrderDetail(paramMap);
				
				Map orderMap = (Map) reMap.get("HEAD");
				
				if (!"10".equals(orderMap.get("STATUS").toString())) {
					super.writeJson(response, "9996", "只能取消处于下单状态的订单", null, null);
					return;
				}
				
				paramMap.put("CANCEL_TIME", DateUtils.getDateTime());
				
				userOrderService.updateOrderForCancel(paramMap);
				
				reMap = userOrderService.getOrderDetail(paramMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, (Map) reMap.get("HEAD"), (List) reMap.get("LINE"));
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserOrderController---cancelOrder---interface error: ", e);
		}
	}
	
	
	/**
	 * 付款订单
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("order/payAction")
	public void payAction(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UserOrderController---payAction---dataParm: " + dataParm);
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String user_id = (String) paramMap.get("USER_ID"), 
				   ut = (String) paramMap.get("ut"), 
				   s_user_id = (String) session.getAttribute("user_id");
			
			// Get and compare with user id in session
			if (user_id == null || "".equals(user_id) || (user_id != null && !user_id.equals(s_user_id))) {
				super.writeJson(response, Code.FAIL, "无效用户！", null, null);
			} else if (!"10".equals(ut)) {// Only user has access
				super.writeJson(response, "9998", "无效客户端", null, null);
			} else if (!UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9997", "无效设备", null, null);
			} else {
				
				// 判断订单状态
				Map reMap = userOrderService.getOrderDetail(paramMap);
				
				Map orderMap = (Map) reMap.get("HEAD");
				
				if (!"10".equals(orderMap.get("STATUS").toString())) {
					super.writeJson(response, "9996", "订单状态不正确, 无法支付！", null, null);
					return;
				}
				
				// 判断用户余额
				BigDecimal payment = new BigDecimal(paramMap.get("MONEY_NUM").toString());
				BigDecimal total_money_Big = new BigDecimal(userService.getUserExpandInfo(paramMap).get("TOTAL_MONEY").toString());
				if (total_money_Big.compareTo(payment) < 0) {
					super.writeJson(response, "9995", "余额不足，请充值！", null, null);
					return;
				}
				
				// 用户金额消费明细表参数集
				Map tranMap = new HashMap();
				tranMap.put("USER_ID", user_id);
				tranMap.put("TIME1", DateUtils.getDateTime());
				tranMap.put("DATE1", DateUtils.getToday());
				tranMap.put("MONEY_NUM", paramMap.get("MONEY_NUM"));
				tranMap.put("TASK_ID", "");
				tranMap.put("ORDER_ID", paramMap.get("ORDER_ID"));
				tranMap.put("ABLI_ORDER_ID", "");
				tranMap.put("PRE_PAID_ID", "");
				tranMap.put("STATUS", "1");
				tranMap.put("MN_TYPE", "20");
				tranMap.put("TOTAL_MONEY", total_money_Big);
				
				// 产生用户金额消费记录
				// 将此次用户金额划到系统账户
				// 修改订单状态
				// 处理库存
				userOrderService.updatePayAction(tranMap);
				
				reMap = userOrderService.getOrderDetail(paramMap);
				
				super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, (Map) reMap.get("HEAD"), (List) reMap.get("LINE"));
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UserOrderController---payAction---interface error: ", e);
		}
	}

	/**
	 * 在BeeCloud获得渠道的确认信息（包括支付成功，退款成功）后，会通过主动推送的方式将确认消息推送给客户的server。如果客户需要接收此类消息来实现业务逻辑，需要:
	 * 1. 开通公网可以访问的IP地址和端口
	 * 2. 接收BeeCloud Webhook服务器发起的HTTP POST请求报文。如果需要对传输加密，请使用支持HTTPS的webhook的url地址。
	 * 注意：同一条订单可能会发送多条支付成功的webhook消息，这是由渠道触发的(比如渠道的重试)，同一个订单的重复的支付成功的消息应该被忽略。退款同理。
	 * 
	 * 推送机制：BeeCloud在"收到渠道的确认结果"后的1秒时刻主动向用户server发送消息
	 * 推送重试机制：用户server接收到某条消息时如果未返回字符串"success", BeeCloud将认为此条消息未能被成功处理, 将触发推送重试机制;
	 *    		      如果用户server一直未能返回字符串"success"，BeeCloud将在"收到渠道的确认结果"后的2秒，4秒，8秒，...，2^17秒（约36小时）时刻重发该条消息；
	 *             如果在以上的某一时刻，用户server返回了字符串"success"则不再重发该消息 。
	 * 
	 * 处理消息后给BeeCloud返回结果:用户返回"success"字符串给BeeCloud代表-"正确接收并确认了本次状态数据的结果"，其他所有返回都代表需要继续重传本次的状态数据。
	 * 
	 * 推送接口标准:
	 * 		HTTP 请求类型 : POST
	 *  	HTTP 数据格式 : JSON
	 *  	HTTP Content-type : application/json         
	 *     
	 * Beecloud-webhook: https://github.com/beecloud/beecloud-webhook    
	 *   
	 * 字段说明-参数含义:        
	 * @param sign	服务器端通过计算appID + appSecret + timestamp的MD5生成的签名(32字符十六进制),
	 * 				请在接受数据时自行按照此方式验证sign的正确性，不正确不返回success即可
	 * @param timestamp 服务端的时间（毫秒），用以验证sign, MD5计算请参考sign的解释
	 * @param channelType WX/ALI/UN/KUAIQIAN/JD 分别代表微信/支付宝/银联/块钱/京东
	 * @param transactionType PAY/REFUND 分别代表支付和退款的结果确认
	 * @param transactionId	交易单号，对应支付请求的bill_no或者退款请求的refund_no-调用支付时传递的out_trade_no
	 * @param transactionFee 交易金额，是以分为单位的整数，对应支付请求的total_fee或者退款请求的refund_fee
	 * @param messageDetail {orderId:xxx…..} 用一个map代表处理结果的详细信息，例如支付的订单号，金额， 商品信息
	 * @param optional 附加参数，为一个JSON格式的Map，客户在发起购买或者退款操作时添加的附加信息
	 * optional 为自定义参数对象，目前只支持基本类型的key ＝》 value, 不支持嵌套对象；
	 * 回调时如果有optional则会传递给webhook地址，webhook的使用请查阅文档
	 * 本接口提供BeeCloud的支付或者退款的回调接口
	 * 生成由BeeCloud返回的支付日志
	 */
	@RequestMapping("order/payOrder")
	public void payOrder(HttpServletRequest req, HttpServletResponse response,
			@RequestParam String sign, 
			@RequestParam String timestamp,
			@RequestParam String channelType,
			@RequestParam String transactionType,
			@RequestParam String transactionId,
			@RequestParam String transactionFee,
			@RequestParam String messageDetail, 
			@RequestParam String optional) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("UserOrderController---payOrder---sign: " + sign
					+ "; timestamp: " + timestamp 
					+ "; channelType: " + channelType 
					+ "; transactionType: " + transactionType
					+ "; transactionId: " + transactionId
					+ "; transactionFee: " + transactionFee
					+ "; messageDetail: " + messageDetail 
					+ "; optional: " + optional);
		}
		
		response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        
		PrintWriter writer = null;
		
		try {
			
			writer = response.getWriter();
			
			String fail = "fail";
			String success = "success";
			
			
			/* ----------------------------------------验证签名-------------------------------------- */
			if (!sign.equals(MD5Util.getMessageDigest(Prop.getString("beecloud.appID") + Prop.getString("beecloud.appSecret") + timestamp))) {
				logger.error(DateUtils.getDateTime() + ": 签名认证失败!");
				writer.write(fail);
				response.getWriter().flush();
				return;
			}
			
			/* --------------------------------------验证自定义签名----------------------------------- */
			// 增加额外的额外验证自定义签名验证
			// 根据section动态产生签名
			// 签名算法同验证签名
			Gson gson = new Gson();
			Optional optionalObj = gson.fromJson(optional, Optional.class);
			
			String appid = Prop.getString("beecloud.appID");
			String appSecret = Prop.getString("beecloud.appSecret");
			String order_id = optionalObj.getORDER_ID();
			String random_id = optionalObj.getRANDOM_ID();
			String section = optionalObj.getSECTION();
			
			String genSign = "";
			
			if ("1".equals(section)) {
				genSign = random_id + appid + order_id + appSecret;
			} else if ("2".equals(section)) {
				genSign = appid + random_id + order_id + appSecret;
			} else if ("3".equals(section)) {
				genSign = appid + order_id + random_id + appSecret;
			} else if ("3".equals(section)) {
				genSign = appid + order_id + appSecret + random_id; 
			}
			
			if (!optionalObj.getSIGN().equals(MD5Util.getMessageDigest(genSign))) {
				logger.error(DateUtils.getDateTime() + ": 自定义签名认证失败!");
				writer.write(fail);
				response.getWriter().flush();
				return;
			}
			
			/* -------------------------------第三方支付（AWU）的信息------------------------------ */
			Map payMap = serilizePayMap(channelType, messageDetail);
			String tran_id = payMap.get("tran_id").toString();// 第三方支付流水号
			String pay_type = payMap.get("pay_type").toString();// 支付方式：10-支付宝，20-微信，30-银联
			payMap.remove("tran_id");
			payMap.remove("pay_type");
			payMap.put("ORDER_ID", transactionId);
			
			/* ---------------------------------判断为充值还是支付-------------------------------- */
			// 根据optional参数中IS_CHARGE来判断是充值还是支付订单
			String is_charge = optionalObj.getIS_CHARGE();
			transactionFee = PubTool.parseCentToYu(transactionFee);
			if ("1".equals(is_charge) && "PAY".equals(transactionType)) {// 充值
				// 1.根据transactionId来获取充值状态
				// 这里的支付状态只有支付状态PAY
				// 如果当前充值已经存在,直接返回success;主要是为了处理beecloud的如下问题：
				// 注意：同一条订单可能会发送多条支付成功的webhook消息，这是由渠道触发的(比如渠道的重试)，同一个订单的重复的支付成功的消息应该被忽略。退款同理。
				// 根据由客户端提供的唯一的transactionId，即T_PRE_PAID.PRE_PAID_ID来查看当前状态
				// 若存在，返回成功
				Map prePayMap = new HashMap();
				prePayMap.put("PRE_PAID_ID", transactionId);
				Map prePayResMap = userOrderService.getPrePayInfoById(prePayMap);
				if (prePayResMap != null && prePayResMap.get("PRE_PAID_ID") != null) {
					writer.write(success);
					response.getWriter().flush();
					return;
				}
				// 2.将通过第三方支付（AWU）的信息保存到对应的日志表中
				// 3.将支付记录保存到用户预支付金额记录表
				// 4:将支付记录信息保存到用户金钱交易日志表
				// 5.更新用户可消费金额T_USERS_EXPAND
				prePayMap.put("PAY_TYPE", pay_type);
				prePayMap.put("USER_ID", optionalObj.getUSER_ID());
				prePayMap.put("TIME1", DateUtils.getDateTime());
				prePayMap.put("DATE1", DateUtils.getToday());
				prePayMap.put("PAYMENT", transactionFee);
				prePayMap.put("ACCOUNT_ID", "");
				prePayMap.put("TRANS_ID", tran_id);
				prePayMap.put("TRANS_TYPE", "10");
				prePayMap.put("NOTE", "");
				
				Map reMap = new HashMap();
				reMap.put("prePayMap", prePayMap);
				reMap.put("payMap", payMap);
				userOrderService.insertPrePayInfo(reMap);
				// 返回成功
				writer.write(success);
				response.getWriter().flush();
			} else if ("2".equals(is_charge)) {// 商品订单支付
				// 根据transactionId订单号来查询订单的支付状态
				// 这里的支付状态包括支付和退款两种状态的判断
				// 如果当前订单为已支付,直接返回success;主要是为了处理beecloud的如下问题：
				// 注意：同一条订单可能会发送多条支付成功的webhook消息，这是由渠道触发的(比如渠道的重试)，同一个订单的重复的支付成功的消息应该被忽略。退款同理。
				// 提供根据订单号获取订单状态的接口
				Map parmMap = new HashMap();
				parmMap.put("ORDER_ID", transactionId);
				Map orderMap = (Map) (userOrderService.getOrderDetail(parmMap).get("HEAD"));
				// 无效订单号
				if (orderMap == null) {
					writer.write(fail);
					response.getWriter().flush();
					return;
				}
				String order_status = orderMap.get("STATUS").toString();
				Object pay_time = orderMap.get("PAY_TIME");// 用来判断更新订单时间
				// 支付状态成功，遇到重复推送; 退款状态成功，遇到重复推送
				if (("PAY".equals(transactionType) && "30".equals(order_status)) || ("REFUND".equals(transactionType) && "40".equals(order_status))) {
					writer.write(success);
					return;
				}
				// 只能当前状态为【下单】时才可以支付
				if ("PAY".equals(transactionType) && !"10".equals(order_status)) {
					logger.error(DateUtils.getDateTime() + ": 只能当前状态为【下单】时才可以支付!");
					writer.write(fail);
					response.getWriter().flush();
					return;
				}
				// 只能当前状态为【支付订单】时才可以退单
				if ("REFUND".equals(transactionType) && !"30".equals(order_status)) {
					logger.error(DateUtils.getDateTime() + ": 只能当前状态为【支付订单】时才可以退单!");
					writer.write(fail);
					response.getWriter().flush();
					return;
				}
				
				// 支付日志总表参数集
				Map payLogMap = new HashMap();
				payLogMap.put("ORDER_ID", transactionId);
				payLogMap.put("TIME_STAMP", timestamp);
				payLogMap.put("TOTAL_PAYMENT", transactionFee);
				
				// 支付类型
				payLogMap.put("PAY_TYPE", pay_type);
				// 判断支付(支付/退款); 生成支付对象参数
				payLogMap.put("TRANS_TYPE", "PAY".equals(transactionType) ? "10" : "20");
				
				// 组织更新订单状态参数集合
				Map upMap = new HashMap();
				upMap.put("ORDER_ID", transactionId);
				upMap.put("STATUS", "PAY".equals(transactionType) ? "30" : "40");
				upMap.put("PAY_TYPE", pay_type);
				// 生成支付的订单别号、验证码和时间戳
				String timeStamp = DateUtils.getDateTime();
				if ("PAY".equals(transactionType)) {// 付款
					upMap.put("CAPTCHA", RandomUtil.generateString(6));
					upMap.put("PAY_TIME", timeStamp);
				} else if ("REFUND".equals(transactionType)) {// 退款
					upMap.put("BACK_TIME", timeStamp);
				}
				
				// 保存微信支付信息到微信日志信息表 T_WX_PAY_LOG
				// 保存支付宝支付信息到支付宝日志信息表 T_ALI_PAY_LOG
				// 保存银联支付信息到银联日志信息表 T_UN_PAY_LOG
				// 将支付信息保存到支付信息日志表-T_ORDERS_PAY_LOG
				// 生成订单别号和唯一码-对于支付！
				// 在客户端收到支付成功后的消息后，调用获取订单的接口，获取订单信息
				parmMap = new HashMap();
				parmMap.put("payLogMap", payLogMap);
				parmMap.put("payMap", payMap);
				parmMap.put("upMap", upMap);
				
				userOrderService.updateOrderPay(parmMap);
				
				writer.write(success);
				response.getWriter().flush();
				
			} else {
				logger.error("未知支付!");
				writer.write(fail);
				response.getWriter().flush();
				return;
			}
		} catch (Exception e) {
			logger.error("UserOrderController---payOrder---interface error: ", e);
			writer.write("fail");
			try {
				response.getWriter().flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
	}
	
	
	/**
	 * 产生支付日志分表参数集
	 * 订单支付日志表中增加第三方的交易流水号
	 * @param channelType
	 * @param messageDetail
	 * @param payLogMap
	 * @return
	 */
	private Map serilizePayMap(String channelType, String messageDetail){
		
		Gson gson = new Gson();
		
		Map payMap = new HashMap();
		
		String pay_type = "";
		String tran_id = "";
		
		if ("WX".equals(channelType)) {
			
			pay_type = "20";
			
			WX wx = gson.fromJson(messageDetail, WX.class);
			
			tran_id = wx.getTransaction_id();
			
			payMap = getWxMap(wx);
			
		} else if ("ALI".equals(channelType)) {
			
			pay_type = "10";
			
			ALI ali = gson.fromJson(messageDetail, ALI.class);
			
			tran_id = ali.getTrade_no();
			
			payMap = getAliMap(ali);
			
		} else if ("UN".equals(channelType)) {
			
			pay_type = "30";
			
			UN un = gson.fromJson(messageDetail, UN.class);
			
			tran_id = un.getQueryId();
			
			payMap = getUnMap(un);
			
		}
		
		payMap.put("pay_type", pay_type);
		payMap.put("tran_id", tran_id);
		
		return payMap;
	}
	
	/**
	 * 组织微信支付信息
	 * @param wx
	 * @return
	 */
	private Map getWxMap(WX wx) {
		Map payMap = new HashMap();
		payMap.put("SINNAL", "WX");
		payMap.put("TRANSACTION_ID", wx.getTransaction_id());
		payMap.put("OPENID", wx.getOpenid());
		payMap.put("CASH_FEE", PubTool.parseCentToYu(wx.getCash_fee()));
		payMap.put("OUT_TRADE_NO", wx.getOut_trade_no());
		payMap.put("TOTAL_FEE", PubTool.parseCentToYu(wx.getTotal_fee()));
		payMap.put("RESULT_CODE", wx.getResult_code());
		payMap.put("TIME_END", wx.getTime_end());
		payMap.put("RETURN_CODE", wx.getReturn_code());
		return payMap;
	}
	
	/**
	 * 组织支付宝支付信息
	 * @param ali
	 * @return
	 */
	private Map getAliMap(ALI ali) {
		Map payMap = new HashMap();
		payMap.put("SINNAL", "ALI");
		payMap.put("DISCOUNT", PubTool.parseNumber(ali.getDiscount()));
		payMap.put("SUBJECT", ali.getSubject());
		payMap.put("TRADE_NO", ali.getTrade_no());
		payMap.put("BUYER_EMAIL", ali.getBuyer_email());
		payMap.put("GMT_CREATE", ali.getGmt_create());
		payMap.put("NOTIFY_TYPE", ali.getNotify_type());
		payMap.put("QUANTITY", ali.getQuantity());
		payMap.put("OUT_TRADE_NO", ali.getOut_trade_no());
		payMap.put("SELLER_ID", ali.getSeller_id());
		payMap.put("TRADE_STATUS", ali.getTrade_status());
		payMap.put("TOTAL_FEE", ali.getTotal_fee());
		payMap.put("PRICE", ali.getPrice());
		payMap.put("BUYER_ID", ali.getBuyer_id());
		payMap.put("USE_COUPON", ali.getUse_coupon());
		return payMap;
	}
	
	/**
	 * 组织银联支付信息
	 * @param un
	 * @return
	 */
	private Map getUnMap(UN un) {
		Map payMap = new HashMap();
		payMap.put("SINNAL", "UN");
		payMap.put("OUT_TRADE_NO", un.getOrderId());// 为了统一，写成OUT_TRADE_NO
		payMap.put("TRACENO", un.getTraceNo()); 
		payMap.put("QUERYID", un.getQueryId());
		payMap.put("RESPMSG", un.getRespMsg());
		payMap.put("TXNTIME", un.getTxnTime());
		payMap.put("RESPCODE", un.getRespCode());
		payMap.put("TXNAMT", un.getTxnAmt());
		return payMap;
	}
}
