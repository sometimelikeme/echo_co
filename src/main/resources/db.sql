DROP DATABASE ECHO_DB;
CREATE DATABASE ECHO_DB;
USE ECHO_DB;
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
   NOTE1 VARCHAR(256),
   NOTE2 VARCHAR(256),
   PRIMARY KEY(DICT_ID,DICT_KEY)
);

--系统积分奖励表，用于向指定账户充值积分
CREATE TABLE T_POINT_AWARD(
  AWARD_ID VARCHAR(32) NOT NULL,
  USER_ID VARCHAR(32) NOT NULL,
  ADMIN_ID VARCHAR(32) NOT NULL,--管理员ID
  TIME1 VARCHAR(32) NOT NULL,--时间戳
  DATE1 CHAR(8),--日期
  POINT_NUM DECIMAL(18,8),--数量
  PRIMARY KEY(AWARD_ID)
);

--用户预支付金额记录表(充值)
--用户充值，beecloud回调增加用户余额
--用户提现
CREATE TABLE T_PRE_PAID(
  PRE_PAID_ID VARCHAR(32) NOT NULL,
  USER_ID VARCHAR(32) NOT NULL,--用户ID
  TIME1 VARCHAR(32) NOT NULL,--时间戳
  DATE1 CHAR(8),--日期
  PAYMENT DECIMAL(18,8),--充值/提现金额
  PAY_TYPE CHAR(2),--充值方式：10-支付宝，20-微信，30-银联
  ACCOUNT_ID VARCHAR(32),--申请提现的账号
  TRANS_ID VARCHAR(32),--调用第三方支付返回的ID号
  TRANS_TYPE CHAR(2),--10：充值；20：申请提现
  NOTE VARCHAR(256),--备注
  PRIMARY KEY(PRE_PAID_ID)
);

-----------------------用户系统---------------------
--用户基本信息表
CREATE TABLE T_USERS(
  USER_ID VARCHAR(32) NOT NULL,
  USER_NAME VARCHAR(32),
  USER_PWD  VARCHAR(32) NOT NULL,
  USER_TYPE CHAR(2) NOT NULL,--10:默认用户；20-店铺；30: 店铺城市代言；90： 管理员；99:超级管理员
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
  TOTAL_POINT DECIMAL(18,8),--可用的总积分
  TOTAL_MONEY DECIMAL(18,8),--可用的总金额
  TOTAL_ABLITY DECIMAL(18,8),--用户总能力值
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
  POINT_NUM DECIMAL(18,8),--获取积分(消费积分采用负数表示)
  MN_TYPE CHAR(2),--来源：10-任务,20-订单,30-技能购买,40-系统奖励
  TASK_ID VARCHAR(32),--对应的任务ID
  ORDER_ID VARCHAR(32),--对应的订单ID
  ABLI_ORDER_ID VARCHAR(32),--对应的技能订单ID
  AWARD_ID VARCHAR(32),--系统奖励积分<需要一个表记录系统奖励>
  STATUS CHAR(2),--增10减20
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
  MONEY_NUM DECIMAL(18,8),--获取金额(消费金额采用负数表示)
  MN_TYPE CHAR(2),--来源：10-任务,20-订单,30-技能购买,40-充值
  TASK_ID VARCHAR(32),--对应的任务ID
  ORDER_ID VARCHAR(32),--对应的订单ID
  ABLI_ORDER_ID VARCHAR(32),--对应的技能订单ID
  PRE_PAID_ID VARCHAR(32),--对应的预支付ID
  STATUS CHAR(2),--增10减20
  PRIMARY KEY(USER_ID,TIME1)
);

--用户能力获取明细表
--仅仅用于展示用户能力值
--可以通过做任务、出售自己技能或者系统奖励来获得
--注意：这里的ORDER_ID为T_USER_ABLITY_ORDERS.ORDER_ID
CREATE TABLE T_USERS_ABLITY(
  USER_ID VARCHAR(32) NOT NULL,
  TIME1 VARCHAR(32) NOT NULL,--获取时间戳
  DATE1 CHAR(8),--获取日期
  ABLITY_NUM DECIMAL(18,8),--获取能力
  TASK_ID VARCHAR(32),--对应的任务ID
  ORDER_ID VARCHAR(32),--对应的订单ID
  ABLI_ORDER_ID VARCHAR(32),--对应的技能订单ID
  AWARD_ID VARCHAR(32),--系统奖励积分
  STATUS CHAR(2),--增10减20
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

--用户关注/收藏技能达人表
CREATE TABLE T_USER_ABLITY_COLL(
  USER_ID VARCHAR(32) NOT NULL,
  A_USER_ID VARCHAR(32) NOT NULL,--关注人用户
  NOTE VARCHAR(128),--收藏说明
  TIME1 VARCHAR(32) NOT NULL,--收藏时间戳
  PRIMARY KEY(USER_ID,A_USER_ID)
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
  BUSI_TYPE VARCHAR(32),--商品店铺类型，维护到枚举表--保存商品大类枚举T_ITEMS_CATEGORY.CATEGORY_TYPE
  REG_TIME VARCHAR(32),--开店时间
  LAST_UPDATE VARCHAR(32),--最近更新时间 
  MER_LEVEL CHAR(2) DEFAULT '99',--店铺级别 级别越高被搜到的几率越大 查询到的排名越高
  PRIMARY KEY(MERCHANT_ID)
);


--店铺扩展表
CREATE TABLE T_MERCHANT_EXPAND(
  MERCHANT_ID VARCHAR(32) NOT NULL,
  TOTAL_POINT DECIMAL(4,2),--平均评分，由每个商品的评分汇总
  PRIMARY KEY (MERCHANT_ID)
);


-----------------------商品系统---------------------

--商品分类表
CREATE TABLE T_ITEMS_CATEGORY(
	CATEGORY_ID VARCHAR(32) NOT NULL,--二级分类枚举
	CATEGORY_NAME VARCHAR(32) NOT NULL,--二级分类名称
    CATEGORY_TYPE VARCHAR(32),--一级分类类型,此处是编码，应该在枚举表中定义所有类型T_DICT.DICT_KEY
	CATEGORY_STATUS CHAR(2),--状态：10-可用，20-不可用
    CATEGORY_DESC VARCHAR(128),--分类描述
  	CATEGORY_PIC VARCHAR(256),--分类图片
  	CRT_TIME VARCHAR(32),--创建时间
	NOTE VARCHAR(32),
	PRIMARY KEY (CATEGORY_ID)
); 

--商品表
--汇总商品评论信息到商品扩展表
CREATE TABLE T_ITEMS(
	ITEM_ID INTEGER AUTO_INCREMENT NOT NULL,
	ITEM_NAME VARCHAR(64) NOT NULL,--商品名称
	ITEM_SHORT_NAME VARCHAR(32),--商品简称
	MERCHANT_ID VARCHAR(32) NOT NULL,--所属店铺ID
	CATEGORY_ID VARCHAR(32) NOT NULL,--所属二级分类
	CATEGORY_TYPE VARCHAR(32),--所属一级分类
	IS_PREF CHAR(1),--是否为团购商品：1是,0-否
	IS_SKILL CHAR(1),--是否为技能商品：1是,0-否
	IS_POINT CHAR(1),--是否为积分商城商品：1是,0-否
	CURR_PRICE DECIMAL(9,2) NOT NULL,--商品现价
	ORI_PRICE DECIMAL(9,2) NOT NULL,--商品原价
	HEAD_ICON VARCHAR(256),--商品小图片
  	MAIN_ICON VARCHAR(256),--商品大图片
  	INVENTORY DECIMAL(9,2) NOT NULL,--商品库存量
  	QTY_SOLD DECIMAL(9,2) NOT NULL,--商品销量
  	ITEM_POINT DECIMAL(4,2),--商品综合评分
  	POINT_NUM DECIMAL(4,2),--评论次数
	CREATE_TIME VARCHAR(32),--创建时间
	LAST_UPDATE VARCHAR(32),--最近更新时间
	STATUS CHAR(2),--商品状态：10-提交，20-审核中，30-审核通过，40-审核未通过, 50-删除商品
	ITEM_DESC VARCHAR(512),--商品描述
  	PRIMARY KEY (ITEM_ID)
);
ALTER TABLE T_ITEMS AUTO_INCREMENT = 10000;

--商品评论表
--汇总商品评论信息到商品表
--用户对一个订单中的商品只能产生一条评论
CREATE TABLE T_ITEM_COMMENTS(
	ORDER_ID VARCHAR(32) NOT NULL,
	ITEM_ID INTEGER NOT NULL,
	USER_ID VARCHAR(32) NOT NULL,
	ITEM_POINT DECIMAL(4,2),--商品评分
	COMENT_TITLE VARCHAR(32),
	COMMENT_DESC VARCHAR(256),
	COMMENT_TIME VARCHAR(32),
	NOTE VARCHAR(256),--回复
	PRIMARY KEY (ORDER_ID,ITEM_ID,USER_ID)
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
	PAY_TYPE CHAR(2),--支付方式：10-支付宝,20-微信,30-银联,40-积分,50-平台支付
	TOTAL_PAY DECIMAL(18,8),--付款总金额/总积分
	FROM_TYPE CHAR(2),--10-金钱订单，20-金币订单
	ORDER_ALIAS_ID VARCHAR(32),--订单别号 生成12为订单数字码
    CAPTCHA VARCHAR(32),--验证码 6为数字和字母验证码/大小写都支持
	ORDER_TIME VARCHAR(32),--下单时间
	CANCEL_TIME VARCHAR(32),--取消时间
	PAY_TIME VARCHAR(32),--付款时间
	BACK_TIME VARCHAR(32),--退单时间
	CONSUME_TIME VARCHAR(32),--消费时间
	SHUT_TIME VARCHAR(32),--结束时间
	DEL_TIME VARCHAR(32),--删除时间
	CANCEL_TYPE CHAR(2),--订单取消类型：10:用户取消,20:系统取消,30-店铺取消(暂不开放)
	CANCEL_REASON VARCHAR(256),--取消原因
	BACK_REASON VARCHAR(256),--退单原因
	COMM_STATUS CHAR(1) DEFAULT '0',--评价状态：1:已评价,0:未评价
	NOTE VARCHAR(256),--订单备注
	PRIMARY KEY (ORDER_ID)
);

--订单行表
CREATE TABLE T_ORDERS_LINE(
	ORDER_ID VARCHAR(32) NOT NULL,--订单号
	ITEM_ID INTEGER NOT NULL,--商品ID
	ITEM_NAME VARCHAR(64) NOT NULL,--商品名称
	QTY_SOLD DECIMAL(9,2),--购买数量
	PAY_PRICE DECIMAL(9,2),--商品购买单价
	SINGLE_PAY DECIMAL(9,2),--商品购买总价
	PRIMARY KEY (ORDER_ID,ITEM_ID)
);

--支付信息日志表
CREATE TABLE T_ORDERS_PAY_LOG(
	ORDER_ID VARCHAR(32) NOT NULL,--订单号
	TRANS_ID VARCHAR(32) NOT NULL,--调用第三方支付返回的ID号
	TRANS_TYPE CHAR(2) NOT NULL,--10：支付；20：退款
	PAY_TYPE CHAR(2) NOT NULL,--支付方式：10-支付宝,20-微信,30-银联,40-积分
	TOTAL_PAYMENT DECIMAL(18,8),--支付总金额
	TIME_STAMP VARCHAR(32) NOT NULL,--支付时间
	NOTE VARCHAR(256),--备注
	PRIMARY KEY (ORDER_ID,TRANS_ID,TRANS_TYPE)
);

------------------------------------------------------------------
--微信支付日志表
CREATE TABLE T_WX_PAY_LOG(
	ORDER_ID VARCHAR(32) NOT NULL,--订单号/充值号
	TRANSACTION_ID VARCHAR(32) NOT NULL,--微信交易号
	OUT_TRADE_NO VARCHAR(32),--商家内部交易号
	OPENID VARCHAR(64),--买家的openid
	CASH_FEE DECIMAL(18,8),--现金付款额
	TOTAL_FEE DECIMAL(18,8),--商品总价，单位为元
	RESULT_CODE VARCHAR(32),--业务结果
	RETURN_CODE VARCHAR(32),--通信标示
	TIME_END VARCHAR(32),--交易结束时间
	PRIMARY KEY (ORDER_ID,TRANSACTION_ID)
);

--支付宝支付日志表
CREATE TABLE T_ALI_PAY_LOG(
	ORDER_ID VARCHAR(32) NOT NULL,--订单号/充值号
	TRADE_NO VARCHAR(32) NOT NULL,--支付宝交易号
	OUT_TRADE_NO VARCHAR(32),--商家内部交易号
	BUYER_EMAIL VARCHAR(32),--买家支付宝账号可以是email或者手机号
	BUYER_ID VARCHAR(32),--买家支付宝唯一用户号
	SELLER_ID VARCHAR(32),--卖家支付宝唯一用户号
	SUBJECT VARCHAR(128),--订单标题
	TOTAL_FEE DECIMAL(18,8),--商品总价，单位为元
	DISCOUNT DECIMAL(18,8),--折扣
	USE_COUPON CHAR(1),--买家是否使用了红包 （N/Y)
	TRADE_STATUS VARCHAR(32),--交易状态
	NOTIFY_TYPE VARCHAR(32),--通知类型
	GMT_CREATE VARCHAR(32),--交易创建时间
	PRIMARY KEY (ORDER_ID,TRADE_NO)
);

--银联支付日志表
CREATE TABLE T_UN_PAY_LOG(
	ORDER_ID VARCHAR(32) NOT NULL,--订单号/充值号
	QUERYID VARCHAR(32) NOT NULL,--银联交易流水号
	TRACENO VARCHAR(32),--银联系统跟踪号
	OUT_TRADE_NO VARCHAR(32),--商家内部交易号
	TXNAMT DECIMAL(18,8),--商品总价，单位为元
	RESPMSG VARCHAR(32),--交易返回码 00 代表成功
	RESPCODE VARCHAR(32),--卖家支付宝唯一用户号
	TXNTIME VARCHAR(32),--交易创建时间
	PRIMARY KEY (ORDER_ID,QUERYID)
);

---------------------------任务系统---------------------------

--任务分类板块表
CREATE TABLE T_SECTORS(
  	SECTOR_ID VARCHAR(32) NOT NULL,
  	SECTOR_NAME VARCHAR(32) NOT NULL,
  	SECTOR_TYPE VARCHAR(32),--板块类型,此处是编码，应该在枚举表中定义所有类型T_DICT.DICT_KEY
  	SECTOR_STATUS CHAR(2),--板块状态：10-可用，20-不可用
  	SECTOR_DESC VARCHAR(128),--板块描述
  	SECTOR_PIC VARCHAR(256),--板块图片
  	CRT_TIME VARCHAR(32),--创建时间
  	NOTE VARCHAR(32),
  	PRIMARY KEY (SECTOR_ID)
);

--竞标任务头表：每个任务可以有多人竞标, 而任务发布者只能选一位竞标者来执行任务
CREATE TABLE T_TASKS(
	TASK_ID VARCHAR(32) NOT NULL,--任务号
	USER_ID VARCHAR(32) NOT NULL,--任务发布者
	SECTOR_ID VARCHAR(32),--所属二级板块
	SECTOR_TYPE VARCHAR(32),--所属一级板块
	TASK_TITLE VARCHAR(64),--任务标题
	TASK_DESC VARCHAR(512),--任务描述
	TASK_DEADLINE VARCHAR(32),--任务截止时间
	TASK_TYPE CHAR(2),--10-金钱任务，20-金币任务
	TASK_TOTAL_PAID DECIMAL(18,8),--发布任务总酬劳
	TASK_ADDR VARCHAR(256),--发布任务地址
	TASK_LONGITUDE DECIMAL(18,8),--经度
  	TASK_LATITUDE DECIMAL(18,8),--维度
	TASK_ICON1 VARCHAR(256),--任务发布图片
	TASK_ICON2 VARCHAR(256),--任务发布图片
	TASK_ICON3 VARCHAR(256),--任务发布图片
	TASK_ICON4 VARCHAR(256),--任务发布图片
	TASK_AUDIT_STATUS CHAR(2),--枚举：10-审核中,20-审核通过,30-审核未通过
	TASK_BID_STATUS CHAR(2),--竞标任务状态枚举：10-发布,20-取消,30-他人投标任务,40-选定他人中标任务,49-中标人确认不做任务，50-退回任务,60-他人完成任务,61-任务发布者点击未完成，70-任务结束,80-删除任务
	TASK_CREATE_TIME VARCHAR(32),--发布时间
	TASK_CANCEL_TIME VARCHAR(32),--取消时间
	TASK_GET_TIME VARCHAR(32),--选中中标时间
	TAKS_UNDONE_TIME VARCHAR(32),--中标人回退任务时间
	TASK_BACK_TIME VARCHAR(32),--退回时间
	TASK_DONE_TIME VARCHAR(32),--完成时间
	TASK_CON_UNDONE_TIME VARCHAR(32),--任务发布者点击未完成时间
	TASK_FINISH_TIME VARCHAR(32),--结束时间
	TASK_DEL_TIME VARCHAR(32),--删除时间
	BID_NUM INT DEFAULT 0,--竞标人数
	TASK_AREA CHAR(20),--任务距离范围:10-10公里范围内；20-全城；30-全国
	PRIMARY KEY (TASK_ID)
);
--竞标任务行表
CREATE TABLE T_TASKS_LINE(
	TASK_ID VARCHAR(32) NOT NULL,--任务号
	USER_ID VARCHAR(32) NOT NULL,--任务竞标者
	BID_TIME VARCHAR(32),--投标时间
	CANCEL_TIME VARCHAR(32),--取消竞标时间 
	IS_BIDE CHAR(2),--是否中标：10-是,20-否
	STATUS CHAR(2),--枚举：10-参与竞标，20-取消竞标
	GET_PAID DECIMAL(18,8),--完成任务酬劳
	DONE_ICON1 VARCHAR(256),--任务完成图片
	DONE_ICON2 VARCHAR(256),--任务完成图片
	DONE_ICON3 VARCHAR(256),--任务完成图片
	DONE_ICON4 VARCHAR(256),--任务完成图片
	PRIMARY KEY (TASK_ID,USER_ID)
);

--非竞标任务头表：每个任务可以有多人竞标, 而任务发布者只能选一位竞标者来执行任务
--多人任务一旦发布，不允许取消
CREATE TABLE T_MUL_TASKS(
	TASK_ID VARCHAR(32) NOT NULL,--任务号
	USER_ID VARCHAR(32) NOT NULL,--任务发布者
	SECTOR_ID VARCHAR(32),--所属二级板块
	SECTOR_TYPE VARCHAR(32),--所属一级板块
	TASK_TITLE VARCHAR(64),--任务标题
	TASK_DESC VARCHAR(512),--任务描述
	TASK_DEADLINE VARCHAR(32),--任务截止时间
	TASK_TYPE CHAR(2),--10-金钱任务，20-金币任务
	TASK_TOTAL_PAID DECIMAL(18,8),--发布任务总酬劳
	TASK_SING_PAID DECIMAL(18,8),--发布任务单酬劳
	TASK_NEED_NUM INT,--任务所需人数
	TASK_BID_NUM INT DEFAULT 0,--实际参与人数
	TASK_DONE_NUM INT DEFAULT 0,--当前完成人数
	TASK_ADDR VARCHAR(256),--发布任务地址
	TASK_LONGITUDE DECIMAL(18,8),--经度
  	TASK_LATITUDE DECIMAL(18,8),--维度
	TASK_ICON1 VARCHAR(256),--任务发布图片
	TASK_ICON2 VARCHAR(256),--任务发布图片
	TASK_ICON3 VARCHAR(256),--任务发布图片
	TASK_ICON4 VARCHAR(256),--任务发布图片
	TASK_AUDIT_STATUS CHAR(2),--枚举：10-审核中,20-审核通过,30-审核未通过
	TASK_BID_STATUS CHAR(2),--竞标任务状态枚举：10-发布,20-他人开始执行任务,30-任务结束,40-删除任务
	TASK_CREATE_TIME VARCHAR(32),--发布时间
	TASK_BEGIN_TIME VARCHAR(32),--开始执行任务时间
	TASK_FINISH_TIME VARCHAR(32),--结束时间-默认为TASK_DEADLINE
	TASK_DEL_TIME VARCHAR(32),--删除时间
	TASK_AREA CHAR(20),--任务距离范围:10-10公里范围内；20-全城；30-全国
	PRIMARY KEY (TASK_ID)
);
--竞标任务行表
CREATE TABLE T_MUL_TASKS_LINE(
	TASK_ID VARCHAR(32) NOT NULL,--任务号
	USER_ID VARCHAR(32) NOT NULL,--任务参与者
	PATI_TIME VARCHAR(32),--参与时间
	DONE_TIME VARCHAR(32),--完成时间
	CONFIRM_TIME VARCHAR(32),--任务发布者确认完成时间
	UNDONE_TIME VARCHAR(32),--任务发布者确认未完成时间
	STATUS CHAR(2),--枚举：10-参与,20-完成,30-确认完成(暂时未用),40-确认未完成 ，50-系统确认完成
	DONE_PAID DECIMAL(18,8),--完成任务酬劳
	DONE_ICON1 VARCHAR(256),--任务完成图片
	DONE_ICON2 VARCHAR(256),--任务完成图片
	DONE_ICON3 VARCHAR(256),--任务完成图片
	DONE_ICON4 VARCHAR(256),--任务完成图片
	PRIMARY KEY (TASK_ID,USER_ID)
);

--任务留言表
CREATE TABLE T_TASKS_MESSAGE(
	MSG_ID VARCHAR(32) NOT NULL,
	TASK_ID VARCHAR(32) NOT NULL,
	USER_ID VARCHAR(32) NOT NULL,
	TIME1 VARCHAR(32),
	MESSAGE VARCHAR(256),
	REPLY VARCHAR(256),
	PRIMARY KEY (MSG_ID)
);

--任务评论表
--任务双方互评
CREATE TABLE T_TASKS_COMMENTS(
	TASK_ID VARCHAR(32) NOT NULL,--技能ID
	USER_ID VARCHAR(32) NOT NULL,--评论人
	TOTAL_POINT DECIMAL(4,2),--单次评分
	COMENT_TITLE VARCHAR(32),
	COMMENT_DESC VARCHAR(256),
	COMMENT_TIME VARCHAR(32),
	NOTE VARCHAR(256),
	PRIMARY KEY (TASK_ID,USER_ID)
);

---------------------------技能系统---------------------------
--用户技能表
--用户将自己的技能展示出来
--任务需求者可以根据展示的技能来购买用户的服务
--技能可以作为用户的一个属性
CREATE TABLE T_USER_ABLITY(
	ABLI_ID VARCHAR(32) NOT NULL,--技能ID
	USER_ID VARCHAR(32) NOT NULL,--技能发布者
	SECTOR_ID VARCHAR(32),--所属二级板块
	SECTOR_TYPE VARCHAR(32),--所属一级板块
	ABLI_TITLE VARCHAR(64),--技能标题
	ABLI_DESC VARCHAR(512),--技能描述
	ABLI_ASK_PAID DECIMAL(18,8),--技能酬劳
	ABLI_ADDR VARCHAR(256),--发布技能地址
	ABLI_LONGITUDE DECIMAL(18,8),--经度
  	ABLI_LATITUDE DECIMAL(18,8),--维度
	ABLI_ICON1 VARCHAR(256),--技能展示图片
	ABLI_ICON2 VARCHAR(256),--技能展示图片
	ABLI_ICON3 VARCHAR(256),--技能展示图片
	ABLI_ICON4 VARCHAR(256),--技能展示图片
	ABLI_BUY INT,--技能购买总量
	ABLI_COMMMENT INT,--评论量
	ABLI_POINT DECIMAL(4,2) DEFAULT 5.0,--技能综合评分
	ABLI_STATUS CHAR(2),--枚举：10-审核中,20-审核通过,30-审核未通过或者无效状态
	ABLI_CREATE_TIME VARCHAR(32),--创建时间
	ABLI_LAST_UPDATE VARCHAR(32),--最新更新时间
	PRIMARY KEY (ABLI_ID)
);

--用户技能评论表
CREATE TABLE T_USER_ABLITY_COMMENTS(
	ABLI_ID VARCHAR(32) NOT NULL,--技能ID
	ORDER_ID VARCHAR(32) NOT NULL,--技能ID
	USER_ID VARCHAR(32) NOT NULL,--评论人
	TOTAL_POINT DECIMAL(4,2),--单次评分
	COMENT_TITLE VARCHAR(32),
	COMMENT_DESC VARCHAR(256),
	COMMENT_TIME VARCHAR(32),
	REPLY VARCHAR(256),--回复
	PRIMARY KEY (ABLI_ID,ORDER_ID,USER_ID)
);

--用户购买技能记录表
CREATE TABLE T_ABLITY_ORDERS(
	ABLI_ORDER_ID VARCHAR(32) NOT NULL,--订单号
	ABLI_ID VARCHAR(32) NOT NULL,--购买技能ID
	USER_ID VARCHAR(32) NOT NULL,--购买者ID
	ORDER_TYPE CHAR(2) NOT NULL,--订单来源类型：10-手机客户端，20-网页
	STATUS CHAR(2) NOT NULL,--订单状态：10-付款，20-退单，30-开工，40-交付，50-确认交付，60-确认未交付，70-删除
	PAY_TYPE CHAR(2),--支付方式：10-支付宝,20-微信,30-银联,40-积分
	TOTAL_PAY DECIMAL(18,8),--付款总金额
	PAY_TIME VARCHAR(32),--付款时间
	BACK_TIME VARCHAR(32),--退单时间
	START_TIME VARCHAR(32),--开工时间
	DELI_TIME VARCHAR(32),--交付时间
	CONFIRM_TIME VARCHAR(32),--确认交付时间
	DECLINE_TIME VARCHAR(32),--确认未交付时间
	DEL_TIME VARCHAR(32),--删除时间
	BACK_REASON VARCHAR(256),--退单原因
	DECLINE_REASON VARCHAR(256),--确认未交付原因
	COMM_STATUS CHAR(1) DEFAULT '0',--评价状态：1:已评价,0:未评价
	NOTE VARCHAR(256),--订单备注
	PRIMARY KEY (ABLI_ORDER_ID)
);

--支付信息日志表
CREATE TABLE T_ABLITY_ORDERS_PAY_LOG(
	ABLI_ORDER_ID VARCHAR(32) NOT NULL,--订单号
	TRANS_ID VARCHAR(32) NOT NULL,--调用第三方支付返回的ID号
	TRANS_TYPE CHAR(2) NOT NULL,--10：支付；20：退款（暂时未启用）
	PAY_TYPE CHAR(2) NOT NULL,--支付方式：10-支付宝,20-微信,30-银联,40-积分
	TOTAL_PAYMENT DECIMAL(18,8),--支付总金额
	TIME_STAMP VARCHAR(32) NOT NULL,--支付时间
	NOTE VARCHAR(256),--备注
	PRIMARY KEY (ABLI_ORDER_ID,TRANS_ID,TRANS_TYPE)
);










