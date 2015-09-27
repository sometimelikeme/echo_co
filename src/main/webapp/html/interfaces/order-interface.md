##Order Interface
####基本地址：http: //123.56.45.223:8080/echo/
###接口1： 下单
- url: order/addOrder.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`MERCHANT_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required购买商品所在店铺ID`
		`USER_ID: '100012',//required用户ID`
		`ORDER_TYPE: '10',//required订单来源类型：10-手机客户端，20-网页`
		`TOTAL_PAY: ''//required购买总价格`
	`},`
	`dataset_line:[`
	`{`
	`ITEM_ID: '10113',// required商品ID`
	`ITEM_NAME: '',// required商品名称`
	`QTY_SOLD: '',//required购买数量`
	`PAY_PRICE: '',//required购买单价`
	`SINGLE_PAY: '',//required购买总价`
	`}`
	`]'
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 本接口只允许店铺端调用，即ut参数必须为10。
	+ 传递MERCHANT_ID是为了防止用户购买自己店铺的商品，这里的防止要在客户端来实现。
	+ 购买商品的总价由客户端计算。
	+ 本接口可实现一个店铺购买多个商品，dataset_line中放入多个商品信息即可。
	+ 接口最终返回数据库中该订单的信息。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[{"ORDER_ID":"2c6aa0b9f3784bd4867005018ead5c47","SINGLE_PAY":20.10,"PAY_PRICE":20.10,"ITEM_NAME":"二锅头","ITEM_ID":10035,"QTY_SOLD":3.00},{"ORDER_ID":"2c6aa0b9f3784bd4867005018ead5c47","SINGLE_PAY":20.20,"PAY_PRICE":10.10,"ITEM_NAME":"老白干","ITEM_ID":10036,"QTY_SOLD":2.00}],"dataset":{"ORDER_TYPE":"10","USER_ID":"47bdfdc734c9429381e7e2793fe28cc8","ORDER_TIME":"2015-09-22 21:03:35","STATUS":"10","TOTAL_PAY":1000.00,"ORDER_ID":"2c6aa0b9f3784bd4867005018ead5c47","COMM_STATUS":"0","MERCHANT_ID":"9f9cdc2a6ff44a50a334d1fe183c3b44"}}`
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
	status: "9996",
	msg: "无法购买自身店铺的商品"
}`
 + 失败：` {
	status: "9995",
	msg: "订单参数缺乏商品信息"
}`
 + 失败：` {
	status: "9994",
	msg: "商品：XXX 库存不足"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口2： 取消订单
- url: order/cancelOrder.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`ut: '10',// required登陆客户端`
		`ORDER_ID: '100012',// required订单ID`
		`CANCEL_TYPE: '10,// required取消类型`
		`CANCEL_REASON: ''// required取消原因`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 本接口只允许店铺端调用，即ut参数必须为10。
	+ 取消类型请参考数据结构
	+ 取消原因请客户端设置一个枚举，若没，请置空传递。
	+ 接口最终返回数据库中该订单的信息。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[],"dataset":{"CANCEL_REASON":"","ORDER_TYPE":"10","USER_ID":"47bdfdc734c9429381e7e2793fe28cc8","ORDER_TIME":"2015-09-22 18:30:42","CANCEL_TIME":"2015-09-22 21:28:04","CANCEL_TYPE":"10","STATUS":"20","ORDER_ID":"e41373c00ed24008a77600a79fe5cc4c","COMM_STATUS":"0","MERCHANT_ID":"9f9cdc2a6ff44a50a334d1fe183c3b44"}}`
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
	status: "9996",
	msg: "只能取消处于下单状态的订单"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口3： 获取订单信息
- url: order/getOrders.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`STATUS: '70',// optimal若增加该参数，且值为70，则亦返回删除的订单`
		`ORDER_ID: '45c298a408df410eac0a1b2c8e6a86d9',// optimal根据订单号获取订单信息，dataset返回订单信息；dataset_line返回订单的商品信息`
		`MERCHANT_ID: '9f9cdc2a6ff44a50a334d1fe183c3b44',// optimal分页获取店铺下的所有订单列表，返回的dataset中totalCount指定总订单量，dataset_line返回本次请求的订单数据;需要配合page，pageSize使用`
		`page: '1',// optimal`
		`pageSize: '1',// optimal`
		`ORDER_ALIAS_ID: '114825653850'// optimal订单别号，需要配合MERCHANT_ID一起查询订单信息，dataset_line返回所有匹配查询的订单数据`
		
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 1. 若仅仅传递了ORDER_ID，则dataset返回订单信息；dataset_line返回订单的商品信息。若增加STATUS参数，且值为70，则亦返回删除的订单。
	+ 2. 若仅仅传递了USER_ID，以及分页参数page、pageSize；则分页返回该用户下所有订单信息列表-用于用户端。
	+ 3. 若在2的基础上传递了MERCHANT_ID，则分页查询本店铺下的所有订单信息列表-用于店铺端。
	+ 4. 若传递了USER_ID，MERCHANT_ID， ORDER_ALIAS_ID； 则根据订单别号查询本店铺下的订单信息。（理论大概率上只返回dataset_line中只有一条数据，注意这里不需要传递分页参数）-用于店铺端
	+ 返回数据字段请参照数据库表结构，订单默认按照下单时间倒序排序。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[],"dataset":{}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口4： 用户消费订单，店铺根据用户提供的订单别号和验证码来执行消费
- url: order/consuOrder.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required店铺用户ID`
		`ORDER_ID: '45c298a408df410eac0a1b2c8e6a86d9',// required订单号`
		`MERCHANT_ID: '9f9cdc2a6ff44a50a334d1fe183c3b44',// required店铺ID`
		`CAPTCHA: 'f1c94f93d2013380b79c780323b8dee6',// required验证码-请用MD5加密传输`
		`ut: '20'// required必须为20`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 验证码通过验证后，修改订单状态为消费状态。
	+ 返回dataset中包含订单基本信息，dataset_line包含订单商品的基本信息。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[],"dataset":{}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端！"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备！"
}`
 + 失败：` {
	status: "9996",
	msg: "无效店铺！"
}`
 + 失败：` {
	status: "9995",
	msg: "无效订单号！"
}`
 + 失败：` {
	status: "9994",
	msg: "订单未支付！"
}`
 + 失败：` {
	status: "9993",
	msg: "验证码错误！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口5： 用户关闭订单
- url: order/closeOrder.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required店铺用户ID`
		`ORDER_ID: '45c298a408df410eac0a1b2c8e6a86d9',// required订单号`
		`ut: '10'// required必须为10`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 执行修改订单状态为订单关闭状态。
	+ 根据机构参数的百分比，计算出本次订单应该奖励的积分；生成奖励积分记录，同时汇总到用户可用的总积分中
	+ 返回dataset中包含订单基本信息，dataset_line包含订单商品的基本信息。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[],"dataset":{}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端！"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备！"
}`
 + 失败：` {
	status: "9996",
	msg: "无效订单号"
}`
 + 失败：` {
	status: "9995",
	msg: "订单未消费，不可关闭"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口6： 用户删除订单
- url: order/delOrder.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required店铺用户ID`
		`ORDER_ID: '45c298a408df410eac0a1b2c8e6a86d9',// required订单号`
		`ut: '10'// required必须为10`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 执行修改订单状态为订单关闭状态。
	+ 订单的任何环节都可以删除订单
	+ 删除后的订单可以在历史检索到
	+ 此接口不再返回订单信息。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功"}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9998",
	msg: "无效客户端！"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备！"
}`
 + 失败：` {
	status: "9996",
	msg: "无效订单号"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`

