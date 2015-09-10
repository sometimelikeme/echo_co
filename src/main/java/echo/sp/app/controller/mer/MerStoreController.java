package echo.sp.app.controller.mer;

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
import echo.sp.app.service.MerStoreService;

/**   
 * 商铺
 * @author Ethan   
 * @date 2015年8月29日 
 */
@Controller
public class MerStoreController extends CoreController{
	
	private static final Logger logger = LoggerFactory.getLogger(MerStoreController.class);
	
	@Autowired
	private MerStoreService merStoreService;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	/**
	 * Get Merchant Detail Information
	 * @param req
	 * @param response
	 * @param dataParm
	 */
	@RequestMapping("mer/getMerDetail")
	public void getMerDetail(HttpServletRequest req, HttpServletResponse response, @RequestParam String dataParm) {
		if (logger.isDebugEnabled()) {
			logger.debug("MerStoreController---getMerDetail---dataParm: " + dataParm);
		}
		
		try {
			
			super.getParm(req, response);
			
			Map paramMap = data.getDataset();
			
			Object page = paramMap.get("page"),// PAGE NUMBER
				   pageSize  = paramMap.get("pageSize"),// MAX ROWS RETURN
				   sort = paramMap.get("sort"),// SORT INFO
				   mer_id = paramMap.get("MERCHANT_ID");
			String merchant_id = mer_id.toString();
			// 默认按照最新更改时间排序
			String sortString = sort == null ? "LAST_UPDATE.desc" : sort.toString();
			// Initializing page as 999999 which is impossible
			int pageInt = 999999;
			if (page != null && pageSize != null) {
				pageInt = Integer.parseInt(page.toString());
			}
			
			Map resMap = new HashMap();
			Map parmMap = new HashMap();
			parmMap.put("MERCHANT_ID", merchant_id);
			// 当且仅当调用参数不包含分页信息, 或者当前分页为第一页时；获取店铺基本信息
			if ((page == null && pageSize == null) || pageInt == 1) {
				resMap = merStoreService.getMerDetail(parmMap);
			}
			
			
			// Get Item Information of Merchant
			PageBounds pageBounds;
			
			Boolean isPage = false;
			
			if (page != null && pageSize != null) {
				isPage = true;
				int pageSizeInt = Integer.parseInt(pageSize.toString());
				pageBounds = new PageBounds(pageInt, pageSizeInt , Order.formString(sortString));
			} else if (!"".equals(sort)) {
				pageBounds = new PageBounds(Order.formString(sortString));
			} else {
				pageBounds = new PageBounds();
			}
			
			List resList = PubTool.getResultList("MerStoreDAO.getMerItems", parmMap, pageBounds, sqlSessionFactory);
			
			// Only Get When the First Page Asks.
			if (PubTool.isListHasData(resList) && isPage && pageInt == 1) {
	    		PageList pageList = (PageList) resList;
	    		int totalCount = pageList.getPaginator().getTotalCount();
	    		resMap.put("totalCount", totalCount);
			}
			
			super.writeJson(response, Code.SUCCESS, Code.SUCCESS_MSG, resMap, resList);
		} catch (Exception e) {
			logger.error("MerStoreController---getMerDetail---interface error: ",e);
			super.writeJson(response, "9992", "后台程序执行失败", null, null);
		}
	}
}
