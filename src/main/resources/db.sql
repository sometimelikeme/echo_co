CREATE DATABASE ECHO_DB
-----------------------系统基础表-------------------
--行政区划表
CREATE TABLE T_CITIES(
	CANT_CODE VARCHAR(20) NOT NULL,--编码
	CANT_NAME VARCHAR(250) NOT NULL,--名称
	SHORT_NAME VARCHAR(120),--简称
	CANT_TYPE VARCHAR(1) NOT NULL,--区划类型
	SUPER_CODE VARCHAR(20) NOT NULL,--上级组织机构编码
	COUNTRY_CODE VARCHAR(10) NOT NULL,--国家
	CANT_NOTE VARCHAR(250),--说明
	IN_USE VARCHAR(1) NOT NULL,--是否在用：1-是，0-否
	PRIMARY KEY(CANT_CODE)
);

--机构参数表
CREATE TABLE T_ORG_PARM(
   CANT_CODE VARCHAR(20) NOT NULL,--地市
   PARM_NAME VARCHAR(32) NOT NULL,--参数名称
   PARM_VALUE VARCHAR(32) NOT NULL,--参数值
   NOTE VARCHAR(64) NOT NULL,---参数说明
   IN_USE VARCHAR(1) NOT NULL,--是否在用：1-是，0-否
   PRIMARY KEY(CANT_CODE,PARM_NAME)
);

--字典表（枚举）
CREATE TABLE T_DICT(
   DICT_ID VARCHAR(32) NOT NULL,
   DICT_NAME VARCHAR(32) NOT NULL,
   DICT_KEY VARCHAR(32) NOT NULL,
   DICT_VALUE VARCHAR(32) NOT NULL,
   NOTE VARCHAR(64),
   PRIMARY KEY(DICT_ID,DICT_KEY)
);

--系统积分奖励表，用于向指定账户充值积分
CREATE TABLE T_POINT_AWARD(
  AWARD_ID VARCHAR(32) NOT NULL,
  USER_ID VARCHAR(32) NOT NULL,
  ADMIN_ID VARCHAR(32) NOT NULL,--管理员ID
  TIME1 VARCHAR(32) NOT NULL,--时间戳
  DATE1 CHAR(8),--日期
  POINT_NUM DECIMAL(7,3),--数量
  PRIMARY KEY(AWARD_ID)
);

--用户预支付金额表
CREATE TABLE T_PRE_PAID(
  PRE_PAID_ID VARCHAR(32) NOT NULL,
  USER_ID VARCHAR(32) NOT NULL,
  TIME1 VARCHAR(32) NOT NULL,--时间戳
  DATE1 CHAR(8),--日期
  MONEY_NUM DECIMAL(7,3),--充值金额
  PAYMENT_TYPE CHAR(2) NOT NULL,--充值方式：10-支付宝，20-微信，30-银联
  PRIMARY KEY(PRE_PAID_ID)
);

-----------------------用户系统---------------------
--用户基本信息表
CREATE TABLE T_USERS(
  USER_ID VARCHAR(32) NOT NULL,
  USER_NAME VARCHAR(32),
  USER_PWD  VARCHAR(32) NOT NULL,
  USER_TYPE CHAR(2) NOT NULL,--10:默认用户；20-店铺；30: 店铺城市代言：99-管理员
  USER_ICON VARCHAR(256),--用户头像，默认取系统无头像图片
  NICKNAME VARCHAR(32),--别名
  GENDER CHAR(1),--0：Male; 1- Female
  TEL_NUMBER VARCHAR(16),--手机号
  TEL_REC VARCHAR(16),--备用电话
  IDEN_CARD VARCHAR(20),--身份证号码
  IDEN_ICON VARCHAR(256),--身份证图片
  CANT_CODE VARCHAR(20),--地区ID
  USER_ADDR VARCHAR(256),--地址
  EMAIL VARCHAR(64),--邮箱
  INVITER VARCHAR(32),--邀请人用户ID
  REG_TIME VARCHAR(32),--注册时间
  IN_VALID VARCHAR(1) NOT NULL,--是否有效：1-有效，0-无效
  ZFB_ACCOUNT VARCHAR(64),--支付宝账号
  WX_ACCOUNT VARCHAR(32),--微信账号
  YL_ACCOUNT VARCHAR(20),--银联账号
  PAY_PWD VARCHAR(32),--支付密码
  ABILITY VARCHAR(32),--技能标签
  PRIMARY KEY(USER_ID)
);

--用户基本信息扩展表
--当用户的基本信息越来越多时，操作用户基本表的消耗太大，对于用户的分数，操作的频率非常高，所以作为一个扩展,关联查询
--同时汇总用户的总积分和总金额
CREATE TABLE T_USERS_EXPAND(
  USER_ID VARCHAR(32) NOT NULL,
  TOTAL_POINT DECIMAL(7,3),--可用的总积分
  TOTAL_MONEY DECIMAL(7,3),--可用的总金额
  PRIMARY KEY(USER_ID)
);

--用户积分消费明细表
--明细表记录了用户做任务获取积分、购买店铺商品获取积分
--系统直接奖励积分
--积分商城消费积分的记录
CREATE TABLE T_USERS_POINT(
  USER_ID VARCHAR(32) NOT NULL,
  TIME1 VARCHAR(32) NOT NULL,--获取时间戳
  DATE1 CHAR(8),--获取日期
  POINT_NUM DECIMAL(7,3),--获取积分(消费积分采用负数表示)
  TASK_ID VARCHAR(32),--对应的任务ID
  ORDER_ID VARCHAR(32),--对应的订单ID
  AWARD_ID VARCHAR(32),--系统奖励积分<需要一个表记录系统奖励>
  PRIMARY KEY(USER_ID,TIME1)
);

--用户金额消费明细表
--用户做任务获取金额、作为商铺卖出物品获取金额
--用户预支付获取金额
--用户在店铺消费或者发布任务消费金额
CREATE TABLE T_USERS_MONEY(
  USER_ID VARCHAR(32) NOT NULL,
  TIME1 VARCHAR(32) NOT NULL,--获取时间戳
  DATE1 CHAR(8),--获取日期
  MONEY_NUM DECIMAL(7,3),--获取金额(消费金额采用负数表示)
  TASK_ID VARCHAR(32),--对应的任务ID
  ORDER_ID VARCHAR(32),--对应的订单ID
  PRE_PAID_ID VARCHAR(32),--对应的预支付ID
  PRIMARY KEY(USER_ID,TIME1)
);

--用户店铺收藏表
CREATE TABLE T_USER_MER_COLL(
  USER_ID VARCHAR(32) NOT NULL,
  MERCHANT_ID VARCHAR(32) NOT NULL,
  NOTE VARCHAR(128),--收藏说明
  TIME1 VARCHAR(32) NOT NULL,--收藏时间戳
  PRIMARY KEY(USER_ID,MERCHANT_ID)
);

--用户商品收藏表
CREATE TABLE T_USER_ITEM_COLL( 
  USER_ID VARCHAR(32) NOT NULL,
  ITEM_ID INTEGER NOT NULL,
  NOTE VARCHAR(128),--收藏说明
  TIME1 VARCHAR(32) NOT NULL,--收藏时间戳
  PRIMARY KEY(USER_ID,ITEM_ID)
);

-----------------------店铺系统---------------------

---店铺表
CREATE TABLE T_MERCHANTS(
  MERCHANT_ID VARCHAR(32) NOT NULL,
  USER_ID VARCHAR(32) NOT NULL,
  MERCHANT_NAME VARCHAR(64) NOT NULL,
  SHORT_NAME VARCHAR(32),
  MERCHANT_TYPE CHAR(2) NOT NULL,--店铺类型：10-普通店铺，20-技能店铺，30-城市店铺，99-积分商城
  MERCHANT_OWNER VARCHAR(16),--负责人
  TEL_NUMBER VARCHAR(16),--联系电话
  HEAD_ICON VARCHAR(256),--店铺列表展示图片（小）
  MAIN_ICON VARCHAR(256),--店铺详情背景图片
  CANT_CODE VARCHAR(20),--地区ID
  LONGITUDE DECIMAL(18,8),--经度
  LATITUDE DECIMAL(18,8),--维度
  MERCHANT_ADDR VARCHAR(256),--地址
  ABILITY VARCHAR(32),--技能标签
  BUSI_DESC VARCHAR(256),--经营内容描述
  OPEN_HOURS VARCHAR(64),--经营时间段
  STATUS CHAR(2),--店铺状态:10-已提交，20-审核中，30-审核通过
  BUSI_TYPE VARCHAR(32),--商品店铺类型，维护到枚举表
  REG_TIME VARCHAR(32),--开店时间
  LAST_UPDATE VARCHAR(32),--最近更新时间
  PRIMARY KEY(MERCHANT_ID)
);


--店铺扩展表
CREATE TABLE T_MERCHANT_EXPAND(
  MERCHANT_ID VARCHAR(32) NOT NULL,
  TOTAL_POINT DECIMAL(2,2),--平均评分，由每个商品的评分汇总
  PRIMARY KEY (MERCHANT_ID)
);


-----------------------商品系统---------------------

--商品分类表
CREATE TABLE T_ITEMS_CATEGORY(
	CATEGORY_ID VARCHAR(6) NOT NULL,
	CATEGORY_NAME VARCHAR(64) NOT NULL,--分类名称
	PRIMARY KEY (CATEGORY_ID)
); 

--商品表
--汇总商品评论信息到商品扩展表
CREATE TABLE T_ITEMS(
	ITEM_ID INTEGER AUTO_INCREMENT NOT NULL,
	ITEM_NAME VARCHAR(64) NOT NULL,--商品名称
	ITEM_SHORT_NAME VARCHAR(32),--商品简称
	MERCHANT_ID VARCHAR(32) NOT NULL,--所属店铺ID
	CATEGORY_ID VARCHAR(6) NOT NULL,--所属分类
	IS_PREF CHAR(1),--是否为团购商品：1是,0-否
	IS_SKILL CHAR(1),--是否为技能商品：1是,0-否
	IS_POINT CHAR(1),--是否为积分商城商品：1是,0-否
	CURR_PRICE DECIMAL(9,2) NOT NULL,--商品现价
	ORI_PRICE DECIMAL(9,2) NOT NULL,--商品原价
	HEAD_ICON VARCHAR(256),--商品小图片
  	MAIN_ICON VARCHAR(256),--商品大图片
  	INVENTORY DECIMAL(6,2) NOT NULL,--商品库存量
  	QTY_SOLD DECIMAL(6,2) NOT NULL,--商品销量
  	ITEM_POINT DECIMAL(2,2),--商品综合评分
  	POINT_NUM DECIMAL(2,2),--评论次数
	CREATE_TIME VARCHAR(32),--创建时间
	LAST_UPDATE VARCHAR(32),--最近更新时间
	STATUS CHAR(2),--商品状态：10-提交，20-审核中，30-审核通过，40-审核未通过, 50-删除商品
	ITEM_DESC VARCHAR(512),--商品描述
  	PRIMARY KEY (ITEM_ID)
);
ALTER TABLE T_ITEMS AUTO_INCREMENT = 10000;

--商品评论表
--汇总商品评论信息到商品表
CREATE TABLE T_ITEM_COMMENTS(
	COMMENT_ID VARCHAR(32) NOT NULL,
	ORDER_ID VARCHAR(32) NOT NULL,
	ITEM_ID INTEGER NOT NULL,
	USER_ID VARCHAR(32) NOT NULL,
	ITEM_POINT DECIMAL(2,1),--商品评分
	COMENT_TITLE VARCHAR(32),
	COMMENT_DESC VARCHAR(256),
	COMMENT_TIME VARCHAR(32),
	PRIMARY KEY (COMMENT_ID)
);

-----------------------订单系统---------------------


--订单表
--一个订单只能针对一个店铺的商品
--可以购买一个店铺的多个商品,形成一个订单
CREATE TABLE T_ORDERS(
	ORDER_ID VARCHAR(32) NOT NULL,--订单号
	MERCHANT_ID VARCHAR(32) NOT NULL,--所属店铺ID
	USER_ID VARCHAR(32) NOT NULL,--消费者ID
	ORDER_TYPE CHAR(2) NOT NULL,--订单来源类型：10-手机客户端，20-网页
	STATUS CHAR(2) NOT NULL,--订单状态：10-下单，20-取消，30-付款，40-退单，50-消费，60-结束，70-删除
	PAYMENT_TYPE CHAR(2),--支付方式：10-支付宝,20-微信,30-银联,40-积分
	TOTAL_PAYMENT DECIMAL(9,2),--付款总金额
	ORDER_ALIAS_ID VARCHAR(32),--订单别号 生成12为订单数字码
    CAPTCHA VARCHAR(32),--验证码 4为数字和字母验证码/大小写都支持-MD5加密
	ORDER_TIME VARCHAR(32),--下单时间
	CANCEL_TIME VARCHAR(32),--取消时间
	PAY_TIME VARCHAR(32),--付款时间
	BACK_TIME VARCHAR(32),--退单时间
	CONSUME_TIME VARCHAR(32),--消费时间
	SHUT_TIME VARCHAR(32),--结束时间
	DEL_TIME VARCHAR(32),--删除时间
	CANCEL_TYPE CHAR(2),--订单取消类型：10:用户取消,20:系统取消
	CANCEL_REASON VARCHAR(256),--取消原因
	BACK_REASON CHAR(256),--退单原因
	COMM_STATUS CHAR(1) DEFAULT '0',--评价状态：1:已评价,0:未评价
	NOTE CHAR(256),--订单备注
	PRIMARY KEY (ORDER_ID)
);

--订单行表
CREATE TABLE T_ORDERS_LINE(
	ORDER_ID VARCHAR(32) NOT NULL,--订单号
	ITEM_ID INTEGER NOT NULL,--商品ID
	QTY_SOLD DECIMAL(6,2),--购买数量
	PAY_PRICE DECIMAL(9,2),--商品购买单价
	SINGLE_PAYMENT DECIMAL(9,2),--商品购买总价
	PRIMARY KEY (ORDER_ID,ITEM_ID)
);

-----------------------任务系统---------------------

--任务板块表
CREATE TABLE T_SECTORS(
  	SECTOR_ID VARCHAR(32) NOT NULL,
  	SECTOR_NAME VARCHAR(32) NOT NULL,
  	SECTOR_TYPE VARCHAR(4),--板块类型,此处是编码，应该在枚举表中定义所有类型
  	SECTOR_STATUS CHAR(2),--板块状态：10-可用，20-不可用
  	SECTOR_DESC VARCHAR(128),--板块描述
  	SECTOR_PIC VARCHAR(256),--板块图片
  	CRT_TIME VARCHAR(32) ,--创建时间
  	PRIMARY KEY (SECTOR_ID)
);

--任务头表
CREATE TABLE T_TASKS(
	TASK_ID VARCHAR(32) NOT NULL,--任务号
	TASK_TITLE VARCHAR(64),--任务标题
	USER_ID VARCHAR(32) NOT NULL,--发布者
	SECTOR_ID VARCHAR(32),--所属板块
	TASK_TYPE CHAR(2),--枚举:T_DICT枚举表存放任务的类型,类似任务的标签
	TOTAL_PAID DECIMAL(7,6) NOT NULL,--任务总酬劳
	SIGNAL_PAID DECIMAL(7,6) NOT NULL,--单份任务酬劳
	BID_NEED INT NOT NULL DEFAULT 1,--做任务需要几份线，默认为1
	TASK_ADDR VARCHAR(256),--发布任务地址
	LONGITUDE DECIMAL(18,8),--经度
  	LATITUDE DECIMAL(18,8),--维度
	TASK_ICON VARCHAR(256),--任务图片
	TASK_STATUS CHAR(2) NOT NULL,--枚举：10-审核中,20-审核通过,30-审核未通过
	CREATE_TIME VARCHAR(32) NOT NULL,--创建时间
	END_TIME VARCHAR(32) NOT NULL,--结束时间
	PRIMARY KEY (TASK_ID)
);

--任务行表
CREATE TABLE T_TASKS_LINE(
	TASK_ID VARCHAR(32) NOT NULL,--任务号
	USER_ID VARCHAR(32) NOT NULL,--任务投标人ID
	BID_STATUS CHAR(2) NOT NULL,--枚举：10-投标,20-中标,30-做完任务,40-任务发布者确认,50-任务结束
	BID_TIME VARCHAR(32) NOT NULL,--投标时间
	GET_TIME VARCHAR(32) NOT NULL,--中标时间
	DONE_TIME VARCHAR(32) NOT NULL,--做完任务时间
	CONFI_TIME VARCHAR(32) NOT NULL,--任务发布者确认时间
	END_TIME VARCHAR(32) NOT NULL,--任务结束时间
	DONE_ICON1 VARCHAR(256) NOT NULL,--做完截图1
	DONE_ICON2 VARCHAR(256) NOT NULL,--做完截图2
	DONE_ICON3 VARCHAR(256) NOT NULL,--做完截图3
	TASK_PAID DECIMAL(7,6) NOT NULL,--任务酬劳
	PRIMARY KEY (TASK_ID,USER_ID)
);










