package echo.sp.app.controller.user;

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
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.model.paymodel.WX;
import echo.sp.app.service.MerItemService;
import echo.sp.app.service.UserOrderService;
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
	
	/**
	 * 用户下单接口
	 * 参数说明请参考接口说明
	 * 1. 插入头表数据，批量插入行表数据
	 * 2. 更新库存信息，商品销量
	 * 3. 执行结束后查询订单信息返回
	 * 注意：若用户发生退单和退款情况，亦更新库存信息，商品销量
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
				
				List itemList = data.getDataset_line();
				
				if (!PubTool.isListHasData(itemList)) {
					super.writeJson(response, "9995", "订单参数缺乏商品信息", null, null);
				}
				
				// Generate order id and order time
				String order_id = IdGen.uuid();
				paramMap.put("ORDER_ID", order_id);
				paramMap.put("ORDER_TIME", DateUtils.getDateTime());
				
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
					
					inventory_de = new BigDecimal(resMap.get("INVENTORY").toString());
					
					qty_sold_de = new BigDecimal(resMap.get("QTY_SOLD").toString());
					
					qty_sold = new BigDecimal(temMap.get("QTY_SOLD").toString());
					
					if (inventory_de.compareTo(qty_sold) < 0) {
						super.writeJson(response, "9994", "商品：" + temMap.get("ITEM_NAME") + " 库存不足", null, null);
						isBreak = true;
						break;
					}
					
					temMap.put("INVENTORY", inventory_de.subtract(qty_sold));
					temMap.put("QTY_SOLD_REAL", qty_sold_de.add(qty_sold));
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
	 * 本方法提供BeeCloud的支付或者退款的回调接口
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
			
			
			// 验证签名
			if (!sign.equals(MD5Util.getMessageDigest(Prop.getString("beecloud.appID") + Prop.getString("beecloud.appSecret") + timestamp))) {
				writer.write(fail);
				return;
			}
			
			
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
				writer.write(fail);
				return;
			}
			
			
			// 根据transactionId订单号来查询订单的支付状态
			// 这里的支付状态包括支付和退款两种状态的判断
			// 如果当前订单为已支付,直接返回success;主要是为了处理beecloud的如下问题：
			// 注意：同一条订单可能会发送多条支付成功的webhook消息，这是由渠道触发的(比如渠道的重试)，同一个订单的重复的支付成功的消息应该被忽略。退款同理。
			// 提供根据订单号获取订单状态的接口
			Map parmMap = new HashMap();
			parmMap.put("ORDER_ID", transactionId);
			Map orderMap = (Map) (userOrderService.getOrderDetail(parmMap).get("HEAD"));
			String order_status = orderMap.get("STATUS").toString();
			// 支付状态成功，遇到重复推送; 退款状态成功，遇到重复推送
			if (("PAY".equals(transactionType) && "30".equals(order_status)) || ("REFUND".equals(transactionType) && "40".equals(order_status))) {
				writer.write(success);
				return;
			}
			
			
			Map payLogMap = new HashMap();
			payLogMap.put("ORDER_ID", transactionId);
			payLogMap.put("TIME_STAMP", timestamp);
			payLogMap.put("TOTAL_PAYMENT", PubTool.parseCentToYu(transactionFee));
				
			gson = new Gson();
			
			Map payMap = new HashMap();
			payMap.put("ORDER_ID", transactionId);
			
			String pay_type = "";
			
			if ("WX".equals(channelType)) {
				
				pay_type = "20";
				
				WX wx = gson.fromJson(messageDetail, WX.class);
				
				payLogMap.put("TRANS_ID", wx.getTransaction_id());
				
				payMap = getWxMap(wx);
				
			} else if ("ALI".equals(channelType)) {
				
				pay_type = "10";
				
				ALI ali = gson.fromJson(messageDetail, ALI.class);
				
				payLogMap.put("TRANS_ID", ali.getTrade_no());
				
				payMap = getAliMap(ali);
				
			} else if ("UN".equals(channelType)) {
				
				pay_type = "30";
				
				UN un = gson.fromJson(messageDetail, UN.class);
				
				payLogMap.put("TRANS_ID", un.getQueryId());
				
				payMap = getUnMap(un);
				
			}
			
			// 支付类型
			payLogMap.put("PAY_TYPE", pay_type);
			
			// 判断支付(支付/退款); 生成支付对象参数
			payLogMap.put("TRANS_TYPE", "PAY".equals(transactionType) ? "10" : "20");
			
			// 保存微信支付信息到微信日志信息表 T_WX_PAY_LOG
			// 保存支付宝支付信息到支付宝日志信息表 T_ALI_PAY_LOG
			// 保存银联支付信息到银联日志信息表 T_UN_PAY_LOG, 放到后台一个事务
			// 将支付信息保存到支付信息日志表-T_ORDERS_PAY_LOG
			// 生成订单别号和唯一码-对于支付！
			
			parmMap = new HashMap();
			parmMap.put("payLogMap", payLogMap);
			parmMap.put("payMap", payMap);
			
			writer.write(success);
			response.getWriter().flush();
		} catch (Exception e) {
			logger.error("UserOrderController---payOrder---interface error: ", e);
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
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
