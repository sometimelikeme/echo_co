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

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;

import echo.sp.app.command.core.CoreController;
import echo.sp.app.command.model.Code;
import echo.sp.app.command.page.PubTool;
import echo.sp.app.command.utils.DateUtils;
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
		
		// PAGE PARAMETERS
		Object page = paramMap.get("page"),// PAGE NUMBER
			   pageSize  = paramMap.get("pageSize"),// MAX ROWS RETURN
			   sort = paramMap.get("sort"),// SORT INFO
			   item_id = paramMap.get("ITEM_ID");
		
		String sortString = sort == null ? "" : sort.toString();
		
		// PageList类是继承于ArrayList的，这样Dao中就不用为了专门分页再多写一个方法
		// 使用PageBounds这个对象来控制结果的输出，常用的使用方式一般都可以通过构造函数来配置。
		// new PageBounds();//默认构造函数不提供分页，返回ArrayList
		// new PageBounds(int limit);//取TOPN操作，返回ArrayList
		// new PageBounds(Order... order);//只排序不分页，返回ArrayList
		// new PageBounds(int page, int limit);//默认分页，返回PageList
		// new PageBounds(int page, int limit, Order... order);//分页加排序，返回PageList
		// new PageBounds(int page, int limit, List<Order> orders, boolean containsTotalCount);
		// 使用containsTotalCount来决定查不查询totalCount，即返回ArrayList还是PageList
		
		PageBounds pageBounds;
					
		List resList = null;
		Boolean isPage = false;
		
		if (page != null && pageSize != null) {// PAGE CONTROL
			isPage = true;
			int pageInt = Integer.parseInt(page.toString()),
				pageSizeInt = Integer.parseInt(pageSize.toString());
			pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
			resList = PubTool.getResultList("MerItemDAO.searchMerItem", paramMap, pageBounds, sqlSessionFactory);
		} else if (!"".equals(sort)) {// SORT WIHIOUT PAGE CONTROL
			pageBounds = new PageBounds(Order.formString(sortString));
		} else {// SEARCH ALL
			pageBounds = new PageBounds();
		}
		
		resList = PubTool.getResultList("MerItemDAO.searchMerItem", paramMap, pageBounds, sqlSessionFactory);
		
		String CODE = "9999",
			   MSG = "无匹配商品";
		if (PubTool.isListHasData(resList)) {
			CODE = Code.SUCCESS;
			MSG = Code.SUCCESS_MSG;
		}
		
        // 处理策略一：如执行分页查询,则将总页数参数放入MAP对象中
        // 处理策略二: 若传入的是ITEM_ID切当前有数据集，则将当前商品放入MAP中,评论放入LIST
		// 当非上述两种情况，直接将查询出来的商品列表放入dataset_line中，dataset对象为空，数据格式请参照分页的结果
        if (isPage) {
        	
        	// Get totalCount
    		PageList pageList = (PageList) resList;
    		int totalCount = pageList.getPaginator().getTotalCount();
            
            if (logger.isDebugEnabled()) {
    			logger.debug("MerItemController---searchMerItem---pageList---totalCount: " + totalCount);
    		}
            
        	paramMap = new HashMap();
        	paramMap.put("totalCount", totalCount);
		} else if (item_id != null && !"".equals(item_id.toString())
				&& PubTool.isListHasData(resList) && resList.size() == 1) {// 标志当前执行查询的为具体商品
			paramMap = (Map) resList.get(0);
			sortString = "COMMENT_TIME.desc";// 默认按照时间倒序排序
			pageBounds = new PageBounds(Order.formString(sortString));
			Map parmMap = new HashMap();
			parmMap.put("ITEM_ID", item_id);
			resList = PubTool.getResultList("MerItemDAO.searchItemComments", parmMap, pageBounds, sqlSessionFactory);
		} else {
			paramMap = null;// 清空
		}
       
		super.writeJson(response, CODE, MSG, paramMap, resList);
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
