--店铺类型枚举
INSERT INTO t_dict VALUES('T_MERCHANTS.MERCHANT_TYPE','店铺类型','10','普通店铺','');
INSERT INTO t_dict VALUES('T_MERCHANTS.MERCHANT_TYPE','店铺类型','20','技能店铺','');
INSERT INTO t_dict VALUES('T_MERCHANTS.MERCHANT_TYPE','店铺类型','30','城市店铺','');
INSERT INTO t_dict VALUES('T_MERCHANTS.MERCHANT_TYPE','店铺类型','40','积分商城','');
--商品分类枚举
INSERT INTO T_ITEMS_CATEGORY(CATEGORY_ID, CATEGORY_NAME)
  VALUES('100', '普通商品');
INSERT INTO T_ITEMS_CATEGORY(CATEGORY_ID, CATEGORY_NAME)
  VALUES('101', '话费');
INSERT INTO T_ITEMS_CATEGORY(CATEGORY_ID, CATEGORY_NAME)
  VALUES('102', '电影');
INSERT INTO T_ITEMS_CATEGORY(CATEGORY_ID, CATEGORY_NAME)
  VALUES('103', '餐饮');
INSERT INTO T_ITEMS_CATEGORY(CATEGORY_ID, CATEGORY_NAME)
  VALUES('104', '酒店');
INSERT INTO T_ITEMS_CATEGORY(CATEGORY_ID, CATEGORY_NAME)
  VALUES('105', 'KTV');
INSERT INTO T_ITEMS_CATEGORY(CATEGORY_ID, CATEGORY_NAME)
  VALUES('300', '技能商品');
  
--机构参数
--店铺信息维护是否需要审核
INSERT INTO T_ORG_PARM VALUES('120000','MER_APPLY_CHECK','0','更新维护店铺信息是否需要审核：1-是，0-否（默认）','1');
--店铺上传上传商品是否需要审核
INSERT INTO T_ORG_PARM VALUES('120000','MER_ITEM_CHECK','0','店铺上传商品是否需要管理员审核：1-是，0-否（默认）','1');
--店铺上传商品的最大数量,默认无限制
INSERT INTO T_ORG_PARM VALUES('120000','MER_ITEM_MAX_QTY','5','店铺上传商品店铺上传商品的最大数量,默认无限制','1');
--按照订单价格百分比返还积分
INSERT INTO T_ORG_PARM VALUES('120000','PER_POINT_AWARD_ORDER','5','按照订单价格奖励积分的百分比','1');


