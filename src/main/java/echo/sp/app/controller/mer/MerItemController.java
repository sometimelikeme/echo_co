package echo.sp.app.controller.mer;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.page.PubTool;
import echo.sp.app.command.utils.DateUtils;
import echo.sp.app.controller.pub.ItemPub;
import echo.sp.app.model.Item;
import echo.sp.app.service.MerItemService;
import echo.sp.app.service.PubToolService;
import echo.sp.app.service.UserService;

/**   
 * 商铺商品
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class MerItemController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(MerItemController.class);
	
	@Autowired
	private MerItemService merItemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PubToolService pubToolService;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	
	/**
	 * 增加商品
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("mer/addMerItem")
	public void addMerItem(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MerItemController---addMerItem---dataParm: " + dataParm);
		}
		
		super.getParm(req, response);
		
		Map paramMap = data.getDataset();
		
		String mer_id = (String) paramMap.get("MERCHANT_ID"), 
			   s_mer_id = (String) session.getAttribute("MERCHANT_ID"), 
			   ut = (String) paramMap.get("ut"), 
			   user_id = (String) session.getAttribute("user_id");
		
		// 获取session中店铺ID和传输的店铺ID做比对
		if (mer_id == null || (mer_id != null && !mer_id.equals(s_mer_id))) {
			super.writeJson(response, Code.FAIL, "无效店铺", null, null);
		} else if (!"20".equals(ut)) {// 只允许店铺上传商品
			super.writeJson(response, "9998", "无效客户端", null, null);
		} else {
			
			Map parmMap = new HashMap();
			
			parmMap.put("USER_ID", user_id);
			Map merMap = userService.getMerchantInfo(parmMap);
			
			// 查询当前店铺是否为30状态，即审核通过状态
			if (merMap == null || (merMap != null && !"30".equals((String)merMap.get("STATUS")))) {
				super.writeJson(response, "9997", "店铺未审核，不能发布商品!", null, null);
				return;
			}
			
			parmMap = new HashMap();
			
			// 店铺上传商品是否需要管理员审核：1-是，0-否（默认）
			parmMap.put("CANT_CODE", session.getAttribute("CANT_CODE"));
			parmMap.put("PARM_NAME", "MER_ITEM_CHECK");
			// 商品状态：10-提交，20-审核中，30-审核通过，40-审核未通过;默认不审核
			String item_status = "1".equals(PubTool.getOrgParm(parmMap, pubToolService)) ? "10" : "30";
			
			if (logger.isDebugEnabled()) {
				logger.debug("MerItemController---addMerItem---item_status: " + item_status);
			}
			
			paramMap.put("USER_ID", user_id);// 用户ID
			paramMap.put("QTY_SOLD", "0");// 销量
			paramMap.put("ITEM_POINT", "0");// 总和评分
			paramMap.put("POINT_NUM", "0");// 评论次数
			paramMap.put("CREATE_TIME", DateUtils.getDateTime());// 增加时间
			paramMap.put("STATUS", item_status);// 状态
			
			Item item = new Item();
			item.setITEM_NAME((String)paramMap.get("ITEM_NAME"));
			item.setITEM_SHORT_NAME((String)paramMap.get("ITEM_SHORT_NAME"));
			item.setMERCHANT_ID(mer_id);
			item.setCATEGORY_ID((String)paramMap.get("CATEGORY_ID"));
			item.setIS_PREF((String)paramMap.get("IS_PREF"));
			item.setIS_SKILL((String)paramMap.get("IS_SKILL"));
			item.setIS_POINT((String)paramMap.get("IS_POINT"));
			item.setCURR_PRICE(new BigDecimal((String)paramMap.get("CURR_PRICE")));
			item.setORI_PRICE(new BigDecimal((String)paramMap.get("ORI_PRICE")));
			item.setHEAD_ICON((String)paramMap.get("HEAD_ICON"));
			item.setMAIN_ICON((String)paramMap.get("MAIN_ICON"));
			item.setINVENTORY(new BigDecimal((String)paramMap.get("INVENTORY")));
			item.setQTY_SOLD(new BigDecimal((String)paramMap.get("QTY_SOLD")));
			item.setITEM_POINT(new BigDecimal((String)paramMap.get("ITEM_POINT")));
			item.setPOINT_NUM(new BigDecimal((String)paramMap.get("POINT_NUM")));
			item.setCREATE_TIME((String)paramMap.get("CREATE_TIME"));
			item.setSTATUS((String)paramMap.get("STATUS"));
			item.setITEM_DESC((String)paramMap.get("ITEM_DESC"));
			
			int res = merItemService.addMerItem(item);
			
			parmMap = new HashMap();
			String CODE = "9996",
				   MSG = "提交失败";
			if (res == 1) {
				parmMap.put("ITEM_ID", item.getITEM_ID());
				CODE = Code.SUCCESS;
				MSG = Code.SUCCESS_MSG;
			}
			
			super.writeJson(response, CODE, MSG, parmMap, null);
		}
	}
	
	
	/**
	 * 修改商品
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("mer/updateMerItem")
	public void updateMerItem(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MerItemController---updateMerItem---dataParm: " + dataParm);
		}
		
		super.getParm(req, response);
		
		Map paramMap = data.getDataset();
		
		String mer_id = (String) paramMap.get("MERCHANT_ID"),
			   s_mer_id = (String) session.getAttribute("MERCHANT_ID"),
			   ut = (String)paramMap.get("ut");
		
		// 获取session中店铺ID和传输的店铺ID做比对
		if (mer_id == null || (mer_id != null && !mer_id.equals(s_mer_id))) {
			super.writeJson(response, Code.FAIL, "无效店铺", null, null);
		} else if (!"20".equals(ut)) {
			super.writeJson(response, "9998", "无效客户端", null, null);
		} else {
			
			paramMap.put("LAST_UPDATE", DateUtils.getDateTime());
			int res = merItemService.updateMerItem(paramMap);

			Map parmMap = new HashMap();
			parmMap.put("ITEM_ID", paramMap.get("ITEM_ID"));

			super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, parmMap, null);
		}
	}
	
	
	/**
	 * 查询商品接口
	 * 1.根据ITEM_ID,ITEM_NAME,ITEM_SHORT_NAME,MERCHANT_ID,CATEGORY_ID,IS_PREF,IS_SKILL,IS_POINT
	 *   条件来查询商品
	 *   根据ITEM_ID来查询商品的,接口返回商品的评论信息
	 * 2.提供分页查询功能
	 * 	  通过附加参数page,pageSize来执行分页查询,
	 *   sort参数来执行排序
	 *   同时返回查询的总记录数
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("mer/searchMerItem")
	public void searchMerItem(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MerItemController---searchMerItem---begin: " + dataParm);
		}
		
		super.getParm(req, response);
		
		Map paramMap = data.getDataset();
		
		// sqlSessionFactory injection
		Map resMap = ItemPub.searchMerItem(paramMap, sqlSessionFactory,
				merItemService, session.getAttribute("user_id").toString(),
				session.getAttribute("MERCHANT_ID").toString()); 
		
		String CODE = Code.FAIL,
			   MSG = "无匹配商品";
		if ((Boolean) resMap.get("hasData")) {
			CODE = Code.SUCCESS;
			MSG = Code.SUCCESS_MSG;
		}
		
		super.writeJson(response, CODE, MSG, (Map)resMap.get("paramMap"), (List)resMap.get("resList"));
	}
	
	/**
	 * 升级商品为团购商品
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("mer/updateToPreItem")
	public void updateToPreItem(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MerItemController---updateToPreItem---dataParm: " + dataParm);
		}
		
		super.getParm(req, response);
		
		Map paramMap = data.getDataset();
		
		String mer_id = (String) paramMap.get("MERCHANT_ID"),
			   s_mer_id = (String) session.getAttribute("MERCHANT_ID"),
			   ut = (String)paramMap.get("ut");
		
		if (mer_id == null || (mer_id != null && !mer_id.equals(s_mer_id))) {
			super.writeJson(response, Code.FAIL, "无效店铺", null, null);
		} else if (!"20".equals(ut)) {
			super.writeJson(response, "9998", "无效客户端", null, null);
		} else {
			
			paramMap.put("IS_PREF", "1");
			paramMap.put("LAST_UPDATE", DateUtils.getDateTime());
			merItemService.updateToPreItem(paramMap);

			super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, null, null);
		}
	}
	
	/**
	 * 查询商品
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("mer/delMerItem")
	public void delMerItem(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MerItemController---delMerItem---dataParm: " + dataParm);
		}
	}
}
