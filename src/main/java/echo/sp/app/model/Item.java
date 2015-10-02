package echo.sp.app.model;

import java.math.BigDecimal;

/**   
 * @author Ethan   
 * @date 2015年8月31日 
 */
public class Item {
	private Integer ITEM_ID;
	private String ITEM_NAME;
	private String ITEM_SHORT_NAME;
	private String MERCHANT_ID;
	private String CATEGORY_ID;// 二级分类
	private String CATEGORY_TYPE;// 一级分类
	private String IS_PREF;// 团购
	private String IS_SKILL;// 技能
	private String IS_POINT;// 积分商城
	private BigDecimal CURR_PRICE;// 现价
	private BigDecimal ORI_PRICE;// 原价
	private String HEAD_ICON;// 小图标
	private String MAIN_ICON;// 大图标
	private BigDecimal INVENTORY;// 库存
	private BigDecimal QTY_SOLD;// 销量
	private BigDecimal ITEM_POINT;// 评分
	private BigDecimal POINT_NUM;// 评次
	private String CREATE_TIME;
	private String STATUS;
	private String ITEM_DESC;
	public String getITEM_DESC() {
		return ITEM_DESC;
	}
	public void setITEM_DESC(String iTEM_DESC) {
		ITEM_DESC = iTEM_DESC;
	}
	public Integer getITEM_ID() {
		return ITEM_ID;
	}
	public void setITEM_ID(Integer iTEM_ID) {
		ITEM_ID = iTEM_ID;
	}
	public String getITEM_NAME() {
		return ITEM_NAME;
	}
	public void setITEM_NAME(String iTEM_NAME) {
		ITEM_NAME = iTEM_NAME;
	}
	public String getITEM_SHORT_NAME() {
		return ITEM_SHORT_NAME;
	}
	public void setITEM_SHORT_NAME(String iTEM_SHORT_NAME) {
		ITEM_SHORT_NAME = iTEM_SHORT_NAME;
	}
	public String getMERCHANT_ID() {
		return MERCHANT_ID;
	}
	public void setMERCHANT_ID(String mERCHANT_ID) {
		MERCHANT_ID = mERCHANT_ID;
	}
	public String getCATEGORY_ID() {
		return CATEGORY_ID;
	}
	public void setCATEGORY_ID(String cATEGORY_ID) {
		CATEGORY_ID = cATEGORY_ID;
	}
	public String getCATEGORY_TYPE() {
		return CATEGORY_TYPE;
	}
	public void setCATEGORY_TYPE(String cATEGORY_TYPE) {
		CATEGORY_TYPE = cATEGORY_TYPE;
	}
	public String getIS_PREF() {
		return IS_PREF;
	}
	public void setIS_PREF(String iS_PREF) {
		IS_PREF = iS_PREF;
	}
	public String getIS_SKILL() {
		return IS_SKILL;
	}
	public void setIS_SKILL(String iS_SKILL) {
		IS_SKILL = iS_SKILL;
	}
	public String getIS_POINT() {
		return IS_POINT;
	}
	public void setIS_POINT(String iS_POINT) {
		IS_POINT = iS_POINT;
	}
	public BigDecimal getCURR_PRICE() {
		return CURR_PRICE;
	}
	public void setCURR_PRICE(BigDecimal cURR_PRICE) {
		CURR_PRICE = cURR_PRICE;
	}
	public BigDecimal getORI_PRICE() {
		return ORI_PRICE;
	}
	public void setORI_PRICE(BigDecimal oRI_PRICE) {
		ORI_PRICE = oRI_PRICE;
	}
	public String getHEAD_ICON() {
		return HEAD_ICON;
	}
	public void setHEAD_ICON(String hEAD_ICON) {
		HEAD_ICON = hEAD_ICON;
	}
	public String getMAIN_ICON() {
		return MAIN_ICON;
	}
	public void setMAIN_ICON(String mAIN_ICON) {
		MAIN_ICON = mAIN_ICON;
	}
	public BigDecimal getINVENTORY() {
		return INVENTORY;
	}
	public void setINVENTORY(BigDecimal iNVENTORY) {
		INVENTORY = iNVENTORY;
	}
	public BigDecimal getQTY_SOLD() {
		return QTY_SOLD;
	}
	public void setQTY_SOLD(BigDecimal qTY_SOLD) {
		QTY_SOLD = qTY_SOLD;
	}
	public BigDecimal getITEM_POINT() {
		return ITEM_POINT;
	}
	public void setITEM_POINT(BigDecimal iTEM_POINT) {
		ITEM_POINT = iTEM_POINT;
	}
	public BigDecimal getPOINT_NUM() {
		return POINT_NUM;
	}
	public void setPOINT_NUM(BigDecimal pOINT_NUM) {
		POINT_NUM = pOINT_NUM;
	}
	public String getCREATE_TIME() {
		return CREATE_TIME;
	}
	public void setCREATE_TIME(String cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
}
