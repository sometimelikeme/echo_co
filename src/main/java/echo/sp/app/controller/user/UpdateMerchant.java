package echo.sp.app.controller.user;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.command.utils.IdGen;
import echo.sp.app.command.utils.UserAgentUtils;
import echo.sp.app.service.UserService;

/**   
 * @author Ethan   
 * @date 2015年8月28日 
 */
public class UpdateMerchant extends CoreController{
	
	@Autowired
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UpdateMerchant.class);
	
	/**
	 * 用户申请为商铺
	 * @param response
	 * @param tel
	 */
	@RequestMapping("user/toMerchant")
	public void toMerchant(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("UpdateMerchant---UpdateMerchant---begin: " + data);
		}
		
		super.getParm(req, response);
		
		Map paramMap = data.getDataset();
		
		String user_id = (String)paramMap.get("user_id"),
			   ut = (String)paramMap.get("ut");
		
		if (!"10".equals(ut)) {
			super.writeJson(response, Code.FAIL, "无效客户端", null, null);
		} else if ("10".equals(ut) && !UserAgentUtils.isMobileOrTablet(req)) {
			super.writeJson(response, "9998", "无效设备", null, null);
		} else if ("".equals(user_id) || user_id.length() == 0) {
			super.writeJson(response, "9997", "无效账号", null, null);
		} else {
			String mer_id = IdGen.uuid(),
				   mer_name = (String)paramMap.get("mer_name"),
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
				   status = "10",
				   busi_type = (String)paramMap.get("busi_type"),
				   reg_time = (String)paramMap.get("reg_time");
			
			paramMap = new HashMap();
			
			paramMap.put("MERCHANT_ID", mer_id);
			paramMap.put("USER_ID", user_id);
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
			paramMap.put("REG_TIME", DateUtils.getDateTime());
			
			int res = userService.insertToMerchant(paramMap);
			
			if (logger.isDebugEnabled()) {
				logger.debug("UpdateMerchant---UpdateMerchant---res: " + res);
			}
			
			paramMap = new HashMap();
			String CODE = "9996",
				   MSG = "申请失败";
			if (res == 1) {
				paramMap.put("MERCHANT_ID", mer_id);
				CODE = Code.SUCCESS;
				MSG = Code.SUCCESS_MSG;
			}
			
			super.writeJson(response, CODE, MSG, paramMap, null);
		}
	}
	
}
