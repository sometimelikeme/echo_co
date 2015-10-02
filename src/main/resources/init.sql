--店铺类型枚举-demo
INSERT INTO T_DICT VALUES('T_MERCHANTS.MERCHANT_TYPE','店铺类型','10','普通店铺','');
INSERT INTO T_DICT VALUES('T_MERCHANTS.MERCHANT_TYPE','店铺类型','20','技能店铺','');
INSERT INTO T_DICT VALUES('T_MERCHANTS.MERCHANT_TYPE','店铺类型','30','城市店铺','');
INSERT INTO T_DICT VALUES('T_MERCHANTS.MERCHANT_TYPE','店铺类型','40','积分商城','');
--商品一级分类-demo
INSERT INTO T_DICT VALUES('T_ITEMS_CATEGORY.CATEGORY_TYPE','商品一级分类','10','酒水','一级分类图片地址');
INSERT INTO T_DICT VALUES('T_ITEMS_CATEGORY.CATEGORY_TYPE','商品一级分类','20','美食','一级分类图片地址');
INSERT INTO T_DICT VALUES('T_ITEMS_CATEGORY.CATEGORY_TYPE','商品一级分类','30','酒店','一级分类图片地址');
INSERT INTO T_DICT VALUES('T_ITEMS_CATEGORY.CATEGORY_TYPE','商品一级分类','40','娱乐','一级分类图片地址');
INSERT INTO T_DICT VALUES('T_ITEMS_CATEGORY.CATEGORY_TYPE','商品一级分类','50','技能','一级分类图片地址');
--商品分类枚举-demo
INSERT INTO T_ITEMS_CATEGORY VALUES('100001', '白酒', '10', '10','酒水-白酒','','','');
INSERT INTO T_ITEMS_CATEGORY VALUES('100002', '红酒', '10', '10','酒水-红酒','','','');
INSERT INTO T_ITEMS_CATEGORY VALUES('100003', '清酒', '10', '10','酒水-清酒','','','');
INSERT INTO T_ITEMS_CATEGORY VALUES('200001', '火锅', '20', '10','美食-火锅','','','');
INSERT INTO T_ITEMS_CATEGORY VALUES('200002', '自助', '20', '10','美食-助','','','');
--任务一级分类-demo
INSERT INTO T_DICT VALUES('T_SECTORS.SECTOR_TYPE','任务一级分类','10','兼职','一级分类图片地址');
INSERT INTO T_DICT VALUES('T_SECTORS.SECTOR_TYPE','任务一级分类','20','学习','一级分类图片地址');
INSERT INTO T_DICT VALUES('T_SECTORS.SECTOR_TYPE','任务一级分类','30','陪练','一级分类图片地址');
INSERT INTO T_DICT VALUES('T_SECTORS.SECTOR_TYPE','任务一级分类','40','教练','一级分类图片地址');
INSERT INTO T_DICT VALUES('T_SECTORS.SECTOR_TYPE','任务一级分类','50','活动','一级分类图片地址');
--任务二级分类-demo
INSERT INTO T_SECTORS VALUES('100001', '发传单', '10', '10','兼职-发传单','','','');
INSERT INTO T_SECTORS VALUES('100002', '打电话', '10', '10','兼职-打电话','','','');
INSERT INTO T_SECTORS VALUES('100003', '促销', '10', '10','兼职-促销','','','');
INSERT INTO T_SECTORS VALUES('100004', '志愿者', '10', '10','兼职-志愿者','','','');
INSERT INTO T_SECTORS VALUES('100005', '家教', '10', '10','兼职-家教','','','');
  
--机构参数
--店铺信息维护是否需要审核
INSERT INTO T_ORG_PARM VALUES('120000','MER_APPLY_CHECK','0','更新维护店铺信息是否需要审核：1-是，0-否（默认）','1');
--店铺上传上传商品是否需要审核
INSERT INTO T_ORG_PARM VALUES('120000','MER_ITEM_CHECK','0','店铺上传商品是否需要管理员审核：1-是，0-否（默认）','1');
--店铺上传商品的最大数量,默认无限制
INSERT INTO T_ORG_PARM VALUES('120000','MER_ITEM_MAX_QTY','5','店铺上传商品店铺上传商品的最大数量,默认无限制','1');
--按照订单价格百分比返还积分
INSERT INTO T_ORG_PARM VALUES('120000','PER_POINT_AWARD_ORDER','5','按照订单价格奖励积分的百分比','1');


