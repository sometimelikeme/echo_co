##Comment Order Interface
####基本地址：http: //123.56.45.223:8080/echo/
####获取一个商品的所有评论信息,请查看mer/searchMerItem.do接口
###接口1： 根据用户ID来获取对某一个订单中某一个商品的评论
- url: comment/getSingleCommentByUserId.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ITEM_ID: '10038',// required商品ID`
		`USER_ID: '100012',//required用户ID`
		`ORDER_ID: '45c298a408df410eac0a1b2c8e6a86d9'//required订单ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 本接口返回dataset中包含T_ITEM_COMMENTS中所有字段。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":{T_ITEM_COMMENTS中所有字段}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口2： 增加/修改商品评论
- url: comment/addUpdateComment.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ITEM_ID: '10038',// required商品ID`
		`USER_ID: '100012',//required用户ID`
		`ORDER_ID: '45c298a408df410eac0a1b2c8e6a86d9',//required订单ID``
		`ITEM_POINT: '10038',// required评分`
		`COMENT_TITLE: '',// required评论标题`
		`COMMENT_DESC: '',//required评论内容`
		`NOTE: '45c298a408df410eac0a1b2c8e6a86d9'//required商家回复``
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 新增评论、修改评论、店铺回复统一调用本接口！
	+ 本接口返回dataset中包含T_ITEM_COMMENTS中所有字段。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":{T_ITEM_COMMENTS中所有字段}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口3： 删除商品评论
- url: comment/deleteComment.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ITEM_ID: '10038',// required商品ID`
		`USER_ID: '100012',//required用户ID`
		`ORDER_ID: '45c298a408df410eac0a1b2c8e6a86d9'//required订单ID`
		`ut:'10'// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 直接删除评论信息。
- 返回值：
+ 成功： `{"status":"0000","msg":"成功"}`
+ 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`

