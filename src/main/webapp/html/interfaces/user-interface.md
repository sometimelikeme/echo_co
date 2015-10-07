##User Interface
####基本地址：http: //123.56.45.223:8080/echo/
###接口1： 收藏店铺
- url: user/addMerColl.do
- 参数一： 收藏店铺参数对象
	`dataParm: {`
	`dataset: {`
		`MERCHANT_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required店铺ID`
		`ut: '10',// required登陆客户端`
		`USER_ID: '100012'//required用户ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 本接口只允许店铺端调用，即ut参数必须为10。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功"}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口2： 收藏商品
- url: user/addItemColl.do
- 参数一： 收藏商品参数对象
	`dataParm: {`
	`dataset: {`
		`ITEM_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required`
		`ut: '10',// required登陆客户端`
		`USER_ID: '100012'// required用户ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 本接口只允许店铺端调用，即ut参数必须为10。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功"}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口3： 取消收藏店铺
- url: user/deleteMerColl.do
- 参数一： 取消收藏店铺参数对象
	`dataParm: {`
	`dataset: {`
		`MERCHANT_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required店铺ID`
		`ut: '10',// required登陆客户端`
		`USER_ID: '100012'// required用户ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 本接口只允许店铺端调用，即ut参数必须为10。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功"}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口4： 取消收藏商品
- url: user/deleteItemColl.do
- 参数一： 取消收藏商品参数对象
	`dataParm: {`
	`dataset: {`
		`ITEM_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required`
		`ut: '10',// required登陆客户端`
		`USER_ID: '100012'// required用户ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 本接口只允许店铺端调用，即ut参数必须为10。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功"}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口5： 获取收藏店铺列表
- url: user/getMerColl.do
- 参数一： 取消收藏商品参数对象
	`dataParm: {`
	`dataset: {`
		`ut: '10',// required登陆客户端`
		`USER_ID: '100012',// required用户ID`
		`page: '100012',// required页数`
		`pageSize: '100012'// required数据量`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 本接口只允许店铺端调用，即ut参数必须为10。
	+ 本接口只支持分页查询。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","datasetline":"店铺表中所有字段"}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口6： 获取收藏商品列表
- url: user/getItemColl.do
- 参数一： 取消收藏商品参数对象
	`dataParm: {`
	`dataset: {`
		`ut: '10',// required登陆客户端`
		`USER_ID: '100012',// required用户ID`
		`page: '100012',// required页数`
		`pageSize: '100012'// required数据量`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 本接口只允许店铺端调用，即ut参数必须为10。
	+ 本接口只支持分页查询。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","datasetline":"商品表中所有字段"}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口7： 获取用户积分余额
- url: user/getUserExpandInfo.do
- 参数一： 取消收藏商品参数对象
	`dataParm: {`
	`dataset: {`
		`ut: '10',// required登陆客户端`
		`USER_ID: '100012',// required用户ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":"T_USERS_EXPAND表中信息"}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`