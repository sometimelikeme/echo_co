##Merchant Interface
####基本地址：http: //123.56.45.223:8080/echo/
###接口1： 增加商品
- url: mer/addMerItem.do
- 参数一： 商品信息参数对象
	`dataParm: {`
	`dataset: {`
		`MERCHANT_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// 店铺ID`
		`ut: '20',// 登陆客户端`
		`ITEM_NAME: 'iphone6',// 商品名称`
		`ITEM_SHORT_NAME: 'iphone6',// 商品简称`
		`CATEGORY_ID: '100',// 商品所述二级分类ID`
		`CATEGORY_TYPE: '100',// 商品所述一级分类ID`
		`IS_PREF: '0',// 是否为团购商品`
		`IS_SKILL: '0',// 是否为技能商品`
		`IS_POINT: '0',// 是否为积分商城商品`
		`CURR_PRICE: '123.11',// 当前价格`
		`ORI_PRICE: '123.11',// 原价`
		`HEAD_ICON: '',// 商品小图标`
		`MAIN_ICON: '',// 商品大图标`
		`INVENTORY: '100',// 库存量`
		`ITEM_DESC: 'wonderful phone'// 商品描述`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递MERCHANT_ID为了与SESSION中的MERCHANT_ID比较，一致的情况下，方可上传商品；防止恶意上传商品到其他店铺。
	+ 本接口只允许店铺端调用，即ut参数必须为20。
	+ 需要查询店铺的审核状态，只有审核成功的店铺方可上传商品。
	+ 默认店铺上传商品无需审核，若设置了机构参数，则需要审核商品。
	+ 初始化商品的销量为0，总评分和总评论次数为0。
	+ 若商品没有打折，请传递原价和现价相同的参数值。
	+ 商品上传成功后返回商品的ITEM_ID。
	+ 参数key值与数据库字段值相同，所有参数值为必须；若未填，请置空。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{商品表中的商品基本信息：key为字段名称大写}}`
 + 失败：` {
	status: "9999",
	msg: "无效店铺"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端"
}`
 + 失败：` {
	status: "9997",
	msg: "店铺未审核，不能发布商品!"
}`
 + 失败：` {
	status: "9996",
	msg: "提交失败"
}`
 + 失败：` {
	status: "9995",
	msg: "您的店铺已经达到商品最大限量!",
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口2： 修改商品
- url: mer/updateMerItem.do
- 参数一： 商品信息参数对象
	`dataParm: {`
	`dataset: {`
		`MERCHANT_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// 店铺ID`
		`ut: '20',// 登陆客户端`
		`ITEM_ID: '10012',// 商品ID`
		`ITEM_NAME: 'iphone6',// 商品名称`
		`ITEM_SHORT_NAME: 'iphone6',// 商品简称`
		`CATEGORY_ID: '100',// 商品所述二级分类ID`
		`CATEGORY_TYPE: '100',// 商品所述一级分类ID`
		`IS_PREF: '1',// 是否为团购商品`
		`IS_SKILL: '0',// 是否为技能商品`
		`IS_POINT: '0',// 是否为积分商城商品`
		`CURR_PRICE: '123.11',// 当前价格`
		`ORI_PRICE: '123.11',// 原价`
		`HEAD_ICON: '',// 商品小图标`
		`MAIN_ICON: '',// 商品大图标`
		`INVENTORY: '100',// 库存量`
		`ITEM_DESC: 'wonderful phone8'// 商品描述`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递MERCHANT_ID为了与SESSION中的MERCHANT_ID比较，一致的情况下，方可上传商品；防止恶意上传商品到其他店铺。
	+ 本接口只允许店铺端调用，即ut参数必须为20。
	+ 商品上传成功后返回商品的ITEM_ID。
	+ 参数key值与数据库字段值相同，所有参数值为必须；若未修改，请与原来保持一致。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{商品表中的商品基本信息：key为字段名称大写}}`
 + 失败：` {
	status: "9999",
	msg: "无效店铺"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口3： 查询商品
- url: mer/searchMerItem.do
- 参数一： 商品信息参数对象
	`dataParm: {`
	`dataset: {`
		`MERCHANT_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// optimal店铺ID`
		`ITEM_ID: '10012',// optimal商品ID`
		`ITEM_NAME: 'iphone6',// optimal商品名称-支持模糊查询`
		`ITEM_SHORT_NAME: 'iphone6',// optimal商品简称-支持模糊查询`
		`CATEGORY_ID: '100',// 商品所述二级分类ID`
		`CATEGORY_TYPE: '100',// 商品所述一级分类ID`
		`IS_PREF: '1',// optimal是否为团购商品`
		`IS_SKILL: '0',// optimal是否为技能商品`
		`IS_POINT: '0',// optimal是否为积分商城商品`
		`page: '1'// optimal分页查询-请求页数-获取商品列表:该分页参数只针对获取商品；若传递ITEM_ID,该参数不用`
		`pageSize: '10'// optimal每页的最大数据量-获取商品列表:该分页参数只针对获取商品；若传递ITEM_ID,该参数不用`
		`pageComm: '1'// optimal评论分页查询-请求页数-获取商品列表:需要传递ITEM_ID配合使用`
		`pageSizeComm: '10'// optimal评论每页的最大数据量-获取商品列表:需要传递ITEM_ID配合使用`
		`IS_MER_INFO_NEED: '1'// optimal 增加该参数 则查询某个商品时，返回的MAP中增加其所在店铺的信息`
		`DISTANCE: ''// required 10-10公里以内，20-全城，30-全国`
	    `CANT_CODE: ''// optimal 若查询全城和10公里需要传递地区号`
		`sort: 'DIST.asc,ITEM_POINT.desc,POINT_NUM.desc,LAST_UPDATE.desc'// required required DIST.asc,ITEM_POINT.desc,POINT_NUM.desc,LAST_UPDATE.desc`
		`LONGITUDE: '',// required 当前消费者的经度`
		`LATITUDE: ''// required 当前消费者的维度`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 注意：
	+ 传递MERCHANT_ID可获取当前店铺下的所有商品。
	+ 本接口未做客户端限制，因为用户也需要调用该接口。
	+ 参数key值与数据库字段值相同，参数是可选，非必须。
	+ 返回字段中DIST表示该商品所述店铺距离
	+ 本接口返回3种类型数据：
	  + 若传入的参数中含有ITEM_ID，切当前有数据集，则将当前商品放入dataset中,商品评论放入dataset_line，默认按照评论日期倒序排序；同时dataset中的BELONG_ITEM用来表示该商品是否为用户自己店铺的商品；增加成功返回数据格式如下(查询商品接口mer/searchMerItem,增加查询具体商品时,返回该商品是否被当前用户所收藏的标志字段`IS_COLL`为1), 同时dataset中增加totalCount来计算总评论数,第一请求获取到商品信息和第一批分页评论信息，后面的分页请求不再返回商品信息：
	    `{"status":"0000","msg":"成功",`
	    `"dataset_line":[{"COMENT_TITLE":"挺好",//评论标题 ``"ORDER_ID":"a70f119a890f467b8d7a632c2fabd873",// 订单号 ``"COMMENT_ID":"a70f119a890f467b8d7a632c2fabd873",//评论ID ``"USER_ID":"a70f119a890f467b8d7a632c2fabd873",`
	    `"COMMENT_DESC":"真不错",``"ITEM_ID":10012,// 商品ID "ITEM_POINT":3.5,// 评分 "COMMENT_TIME":"2015-08-31 16:06:23"// 时间戳}],``"dataset":{"MAIN_ICON":"xxx","POINT_NUM":0.00,"HEAD_ICON":"xxx","CREATE_TIME":"2015-08-31 16:06:23","ITEM_ID":10012,"STATUS":"30","IS_PREF":"1","IS_SKILL":"0","QTY_SOLD":0.00,"INVENTORY":200.00,"ORI_PRICE":5200.00,"ITEM_NAME":"iphone6S","ITEM_SHORT_NAME":"iphone6S","IS_POINT":"0","CATEGORY_ID":"100","ITEM_POINT":0.00,"MERCHANT_ID":"a70f119a890f467b8d7a632c2fabd873","CURR_PRICE":5266.00,BELONG_ITEM: '1'// 1-是，0-否}}`
	 + 当带有page和pageSize参数的时候，接口认为是执行分页查询，则将总页数参数放入dataset对象中，查询出来的商品列表放入dataset_line：
	 	`{"status":"0000","msg":"成功",`
		`"dataset_line":[{"ITEM_DESC":"www","MAIN_ICON":"xxx","POINT_NUM":0.00,"HEAD_ICON":"xxx","CREATE_TIME":"2015-09-01 10:16:19","ITEM_ID":10023,"STATUS":"30","IS_PREF":"1","IS_SKILL":"0","QTY_SOLD":0.00,"INVENTORY":200.00,"ORI_PRICE":5200.00,"ITEM_NAME":"iphone6S","ITEM_SHORT_NAME":"iphone6S","IS_POINT":"0","CATEGORY_ID":"100","ITEM_POINT":0.00,"MERCHANT_ID":"a70f119a890f467b8d7a632c2fabd873","CURR_PRICE":5266.00},{"MAIN_ICON":"xxx","POINT_NUM":0.00,"HEAD_ICON":"xxx","CREATE_TIME":"2015-09-01 08:35:28","ITEM_ID":10022,"STATUS":"30","IS_PREF":"1","IS_SKILL":"0","QTY_SOLD":0.00,"INVENTORY":200.00,"ORI_PRICE":5200.00,"ITEM_NAME":"iphone6","ITEM_SHORT_NAME":"iphone6","IS_POINT":"0","CATEGORY_ID":"100","ITEM_POINT":0.00,"MERCHANT_ID":"a70f119a890f467b8d7a632c2fabd873","CURR_PRICE":4566.00},{"MAIN_ICON":"xxx","POINT_NUM":0.00,"HEAD_ICON":"xxx","CREATE_TIME":"2015-09-01 08:35:27","ITEM_ID":10021,"STATUS":"30","IS_PREF":"1","IS_SKILL":"0","QTY_SOLD":0.00,"INVENTORY":200.00,"ORI_PRICE":5200.00,"ITEM_NAME":"iphone6","ITEM_SHORT_NAME":"iphone6","IS_POINT":"0","CATEGORY_ID":"100","ITEM_POINT":0.00,"MERCHANT_ID":"a70f119a890f467b8d7a632c2fabd873","CURR_PRICE":4566.00}],`
		`"dataset":{"totalCount":12}}`
	 + 当非上述两种情况，直接将查询出来的商品列表放入dataset_line中，dataset对象为空，数据格式请参照分页的结果。
	 	
- 返回值：
 + 成功：请参照注意三种成功返回的数据格式 
 + 失败：` {
	status: "9999",
	msg: "无匹配商品" 
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口4： 更新商品为团购商品
- url: mer/updateToPreItem.do
- 参数一： 更新商品为团购商品参数对象
	`dataParm: {`
	`dataset: {`
		`MERCHANT_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// 店铺ID`
		`ut: '20',// 登陆客户端`
		`ITEM_ID: '100012'// 商品名称`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递MERCHANT_ID为了与SESSION中的MERCHANT_ID比较，一致的情况下，方可上传商品；防止恶意上传商品到其他店铺。
	+ 本接口只允许店铺端调用，即ut参数必须为20。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功"，"dataset":{商品表中的商品基本信息：key为字段名称大写}}`
 + 失败：` {
	status: "9999",
	msg: "无效店铺"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口5： 获取店铺页面数据接口
- url: mer/getMerDetail.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`MERCHANT_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// 店铺ID`
		`page: '1'// 分页查询-请求页数`
		`pageSize: '10'// 每页的最大数据量`
		`sort: 'CREATE_TIME.desc'// 按照什么排序，字段名后加.asc升序排序，.desc倒序排序`
		`item_need: '1'// 是否返回商品信息,1-是,0-否`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 注意：
   + item_need不传或者不为1的情况下,仅仅返回店铺信息。
   + 如果不传递page和pageSize参数，接口返回店铺基本信息和店铺下所有商品的基本信息
   + 如果传递page和pageSize参数，当page==1时，接口返回店铺基本信息和第一页请求的pageSize数据量的数据，以及查询的商品数据量公有多少条totalCount；page!=1时，接口返回的数据不再包含店铺基本信息，只返回本次分页请求的商品信息条数。
   + 返回的dataset中如果`IS_COLL`不为空，表示该店铺已经被用户所收藏
- 返回值：
 + 成功： `{"status":"0000","msg":"成功",dataset:{},datasetline:[]}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口6： 查询店铺列表
- url: mer/getMerList.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`page: '1'，// required 分页查询-请求页数`
		`pageSize: '10'，// required 每页的最大数据量`
		`MERCHANT_NAME: ''，// optimal 根据店铺名称模糊查询`
		`SHORT_NAME: ''，// optimal 根据店铺简称模糊查询`
		`MERCHANT_TYPE: ''，// optimal 店铺类型`
		`MERCHANT_ADDR: ''，// optimal 根据店铺地址模糊查询`
		`ABILITY: ''，// optimal 根据店铺技能模糊查询`
		`BUSI_DESC: '',// optimal 根据店铺描述模糊查询`
		`LONGITUDE: '',// required 当前消费者的经度`
		`LATITUDE: ''// required 当前消费者的维度`
		`DISTANCE: ''// required 10-10公里以内，20-全城，30-全国`
		`BUSI_TYPE: ''// optimal 商品店铺类型，维护到枚举表--保存商品大类枚举T_ITEMS_CATEGORY.CATEGORY_TYPE`
	    `CANT_CODE: ''// optimal 若查询全城和10公里需要传递地区号`
		`ORDER_BY: 'DIST.asc,TOTAL_POINT.desc,MER_LEVEL.asc,LAST_UPDATE.desc'// required DIST.asc,TOTAL_POINT.desc,MER_LEVEL.asc,LAST_UPDATE.desc`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 注意：返回的DIST为距离店铺距离，单位为米
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[{"LAST_UPDATE":"2015-09-12 14:49:53","MERCHANT_OWNER":"","MAIN_ICON":"http://echoapp.oss-cn-beijing.aliyuncs.com/9f9cdc2a6ff44a50a334d1fe183c3b44_shoppic.jpg","HEAD_ICON":"http://echoapp.oss-cn-beijing.aliyuncs.com/9f9cdc2a6ff44a50a334d1fe183c3b44_shopicon.jpg","BUSI_TYPE":"","USER_ID":"704aef6642e447eea5a7163dce4c73a5","OPEN_HOURS":"","BUSI_DESC":"","MERCHANT_ADDR":"","CANT_CODE":"","STATUS":"30","LATITUDE":0E-8,"TEL_NUMBER":"","MER_LEVEL":"99","REG_TIME":"2015-09-10 19:54:36","ABILITY":"","MERCHANT_TYPE":"","LONGITUDE":0E-8,"MERCHANT_ID":"9f9cdc2a6ff44a50a334d1fe183c3b44","MERCHANT_NAME":"测试","DIST":'100'}],"dataset":{"totalCount":1}}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口7： 删除商品
- url: mer/delMerItem.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`MERCHANT_ID: '50e532a0d6a244d58e43c0edc8247ee2'，// required`
		`ut: '20',// required`
		`ITEM_ID: ''// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_no 私钥
- 返回值：
 + 成功： `{"status":"0000","msg":"成功"}`
 + 失败：` {
	status: "9999",
	msg: "无效店铺",
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端",
}`
 + 失败：` {
	status: "9997",
	msg: "该商品含有未完成的订单",
	"dataset_line":[为完成的订单信息，以及下单者的电话TEL_NUMBER和名称USER_NAME]
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`