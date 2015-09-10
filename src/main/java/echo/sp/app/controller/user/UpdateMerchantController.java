package echo.sp.app.controller.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.IdGen;
import echo.sp.app.command.utils.RandomUtil;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.MerStoreService;
import echo.sp.app.service.UserService;

/**   
 * 用户升级为店铺
 * @author Ethan   
 * @date 2015年8月28日 
 */
@Controller
public class UpdateMerchantController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(UpdateMerchantController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MerStoreService merStoreService;
	
	@RequestMapping("user/merApply")
	public void merApply(HttpServletRequest req, HttpServletResponse response, 
			@RequestParam String user_id, @RequestParam String ut) {
		if (logger.isDebugEnabled()) {
			logger.debug("UpdateMerchantController---merApply---begin");
		}
		
		try {
			if (!"10".equals(ut)) {
				super.writeJson(response, Code.FAIL, "无效客户端", null, null);
			} else if ("10".equals(ut) && !UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9998", "无效设备", null, null);
			} else if ("".equals(user_id) || user_id.length() == 0) {
				super.writeJson(response, "9997", "无效账号", null, null);
			} else {
				
				// 检查当前用户是否已经是店铺账号
				Map parmMap = new HashMap();
				parmMap.put("USER_ID", user_id);
				int res = userService.checkMerchant(parmMap);
				
				if (res > 0) {
					super.writeJson(response, "9996", "该用户已经是店铺用户", null, null);
				} else {
					parmMap = new HashMap();
					String mer_id = IdGen.uuid();
					parmMap.put("MERCHANT_ID", mer_id);
					parmMap.put("USER_ID", user_id);
					parmMap.put("MERCHANT_NAME", RandomUtil.generateUpperString(6));
					parmMap.put("MERCHANT_TYPE", "10");
					parmMap.put("STATUS", "10");
					parmMap.put("REG_TIME", DateUtils.getDateTime());
					
					res = userService.insertToMerchant(parmMap);
					
					String CODE = "9995",
						   MSG = "申请失败";
					if (res == 1) {
						CODE = Code.SUCCESS;
						MSG = Code.SUCCESS_MSG;
					}
					
					super.writeJson(response, CODE, MSG, parmMap, null);
				}
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UpdateMerchantController---merApply---interface error: ", e);
		}
	}
	
	/**
	 * 更新商铺详细信息
	 * @param response
	 * @param tel
	 */
	@RequestMapping("mer/toMerchant")
	public void toMerchant(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UpdateMerchantController---toMerchant---begin: " + dataParm);
		}
		
		try {
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			String mer_id = (String)paramMap.get("mer_id"),
				   ut = (String)paramMap.get("ut");
			
			if (!"20".equals(ut)) {
				super.writeJson(response, Code.FAIL, "无效客户端", null, null);
			} else if ("20".equals(ut) && !UserAgentUtils.isMobileOrTablet(req)) {
				super.writeJson(response, "9998", "无效设备", null, null);
			} else if ("".equals(mer_id) || mer_id.length() == 0) {
				super.writeJson(response, "9997", "无效账号", null, null);
			} else {
				
				String mer_name = (String)paramMap.get("mer_name"),
					   mer_type = (String)paramMap.get("mer_type"),
					   mer_ower = (String)paramMap.get("mer_ower"),
					   head_icon = (String)paramMap.get("head_icon"),
					   main_icon = (String)paramMap.get("main_icon"),
					   cant_code = (String)paramMap.get("cant_code"),
					   longtitude = (String)paramMap.get("longtitude"),
					   lantitude = (String)paramMap.get("lantitude"),
					   addr = (String)paramMap.get("addr"),
					   ability = (String)paramMap.get("ability"),
					   desc = (String)paramMap.get("desc"),
					   open_hour = (String)paramMap.get("open_hour"),
					   status = "20",// 审核状态
					   busi_type = (String)paramMap.get("busi_type");
				
				paramMap = new HashMap();
				
				paramMap.put("MERCHANT_ID", mer_id);
				paramMap.put("MERCHANT_NAME", mer_name);
				paramMap.put("MERCHANT_TYPE", mer_type);
				paramMap.put("MERCHANT_OWNER", mer_ower);
				paramMap.put("HEAD_ICON", head_icon);
				paramMap.put("MAIN_ICON", main_icon);
				paramMap.put("CANT_CODE", cant_code);
				paramMap.put("LONGITUDE", longtitude);
				paramMap.put("LATITUDE", lantitude);
				paramMap.put("MERCHANT_ADDR", addr);
				paramMap.put("ABILITY", ability);
				paramMap.put("BUSI_DESC", desc);
				paramMap.put("OPEN_HOURS", open_hour);
				paramMap.put("STATUS", status);
				paramMap.put("BUSI_TYPE", busi_type);
				paramMap.put("LAST_UPDATE", DateUtils.getDateTime());
				
				int res = userService.updateToMerchant(paramMap); 
				
				if (logger.isDebugEnabled()) {
					logger.debug("UpdateMerchantController---toMerchant---res: " + res);
				}
				
				paramMap = new HashMap();
				String CODE = "9996",
					   MSG = "提交失败";
				if (res == 1) {
					paramMap.put("MERCHANT_ID", mer_id);
					paramMap = merStoreService.getMerDetail(paramMap);
					CODE = Code.SUCCESS;
					MSG = Code.SUCCESS_MSG;
				}
				
				super.writeJson(response, CODE, MSG, paramMap, null);
			}
		} catch (Exception e) {
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
			logger.error("UpdateMerchantController---toMerchant---interface error: ", e);
		}
	}
}
