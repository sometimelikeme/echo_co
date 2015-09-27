##Order Interface
####基本地址：http: //123.56.45.223:8080/echo/
###接口1： 下单
- url: order/addOrder.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`MERCHANT_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required购买商品所在店铺ID`
		`ut: '10',// required登陆客户端`
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
