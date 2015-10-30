##用户技能相关接口文档
####基本地址：http: //123.56.45.223:8080/echo/
###接口1： 增加技能
- url: abli/addAbli.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35',// required `
		`SECTOR_ID:'100001',// required `	
		`SECTOR_TYPE:'10',// required `
		`ABLI_TITLE:'发传单',// required `
		`ABLI_DESC:'找人发广告传单',// required `
		`ABLI_ASK_PAID:'500',// required `
		`ABLI_ADDR:'解放路',// required `
		`ABLI_LONGITUDE:'117.333245',// required `
		`ABLI_LATITUDE:'36.231245',// required `
		`ABLI_ICON1:'',// required `
		`ABLI_ICON2:'',// required `
		`ABLI_ICON3:'',// required `
		`ABLI_ICON4:''// required `
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递`USER_ID`为了与SESSION中的`USER_ID`比较。
	+ 技能只有金钱，不存在积分。
	+ 机构参数`ABLI_TASK_CHECK`-用户发布技能是否需要审核：1-是，0-否（默认）
	+ 增加技能后默认`ABLI_BUY_COUNT`,购买技能量为0
	+ 技能的评论量`ABLI_COMMMENT`,默认为0
	+ 技能默认评分`ABLI_POINT`,默认为5.0
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{T_USER_ABLITY表中字段，以及发布者的名称USER_NAME，用户头像USER_ICON}}`
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
###接口2： 修改技能
- url: abli/updateAbli.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ABLI_ID:'c21375a7aef447239c74fc9e7926cd35',// required `
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35',// required `
		`ABLI_TITLE:'发传单',// required `
		`ABLI_DESC:'找人发广告传单',// required `
		`ABLI_ASK_PAID:'500',// required `
		`ABLI_ADDR:'解放路',// required `
		`ABLI_LONGITUDE:'117.333245',// required `
		`ABLI_LATITUDE:'36.231245',// required `
		`ABLI_ICON1:'',// required `
		`ABLI_ICON2:'',// required `
		`ABLI_ICON3:'',// required `
		`ABLI_ICON4:''// required `
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可。
	+ `SECTOR_ID`和`SECTOR_TYPE`不允许修改。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{T_USER_ABLITY表中字段，以及发布者的名称USER_NAME，用户头像USER_ICON}}`
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
###接口3： 删除技能
- url: abli/deleteAbli.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ABLI_ID:'c21375a7aef447239c74fc9e7926cd35',// required `
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可。
	+ 修改`ABLI_STATUS`为40,删除状态
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{T_USER_ABLITY表中字段，以及发布者的名称USER_NAME，用户头像USER_ICON}}`
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
###接口4： 查询技能列表
- url: abli/searchAblityList.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`SECTOR_ID: '',// optimal `
        `SECTOR_TYPE: '',// optimal`
        `ABLI_TITLE: '发',// optimal`
        `LONGITUDE:'117.333245',// required`
        `LATITUDE:'36.231245',// required`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35',// optimal`
        `page: '1',// required`
        `pageSize: '5',// required`
        `sort: 'DIST.desc,ABLI_BUY_COUNT.desc,ABLI_COMMMENT.desc,ABLI_POINT.desc,ABLI_LAST_UPDATE.desc'// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 若传递`USER_ID`则查询改用户发布的技能列表
	+ sort参数说明：DIST-距离，`ABLI_BUY_COUNT`技能购买量，`ABLI_COMMMENT`技能评论量，`ABLI_POINT`技能综合评分，`ABLI_LAST_UPDATE`技能最新更新时间
	+ asc表示正序，desc表示倒序
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[{T_USER_ABLITY表中所有字段，USER_NAME用户名称，USER_ICON用户头像，CANT_CODE用户地区}],"dataset":{totalCount:满足的总记录数}}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口5： 查询单个技能
- url: abli/searchAblityById.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ABLI_ID: '4e3652172ff8499d888959e2744a85ee',// required`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35',// required`
        `page: '1',// required`
        `pageSize: '5'// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 分页查询，仅仅在第一页中加入技能数据，满足的记录数totalCount， 以及第一页的技能留言
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[{T_USER_ABLITY_COMMENTS表中所有，评论人的名称USER_NAME，评论人的头像USER_ICON}],"dataset":{技能数据，满足的记录数totalCount}}`
 + 失败：` {
	status: "9997",
	msg: "无效设备！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口6： 购买技能
- url: abli/buyAbility.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ABLI_ID: '4e3652172ff8499d888959e2744a85ee',// required`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35',// required`
        `ORDER_TYPE:'10',// required`
        `TOTAL_PAY:'500',// required`
        `DEADLINE:'2015-11-01 08:24:32'// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可。
	+ 用户购买技能，直接从用户账户中扣除金额；若用户金额不足，提示9996
	+ 技能订单初始化状态STATUS为10,下单状态
	+ 付款方式PAY_TYPE为50，平台付款
	+ DEADLINE为客户端用户选择需要技能达人完成任务的截止日期，格式为`2015-11-01 08:24:32`
	+ 下单成功后，从用户账户扣除金额，将金额暂存到平台账户上。
	+ 返回数据dataset中分为`ABLI_ORDER`技能订单详情,`ABLI_INFO`所购买的技能详情
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{ABLI_ORDER:{T_ABLITY_ORDERS中所有字段，用户名称USER_NAME，头像USER_ICON},ABLI_INFO:{T_USER_ABLITY中所有字段，用户名称USER_NAME，头像USER_ICON}}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备！"
}`
 + 失败：` {
	status: "9996",
	msg: "余额不足，请充值！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口7： 技能拥有者拒绝
- url: abli/declineContract.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		 `ABLI_ORDER_ID: '3e4d0db57bef4158b79be3bb27c4aa7b',// required`
		 `USER_ID:'c21375a7aef447239c74fc9e7926cd35'，// required`
	     `BACK_REASON:'做不了'// 拒绝原因 required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可。
	+ 判断当前订单状态，若非10，提示-9996.
	+ 修改订单状态为20
	+ 返回数据dataset中分为`ABLI_ORDER`技能订单详情,`ABLI_INFO`所购买的技能详情
- 返回值：
+ 成功： `{"status":"0000","msg":"成功","dataset":{ABLI_ORDER:{T_ABLITY_ORDERS中所有字段，用户名称USER_NAME，头像USER_ICON},ABLI_INFO:{T_USER_ABLITY中所有字段，用户名称USER_NAME，头像USER_ICON}}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`

 + 失败：` {
	status: "9997",
	msg: "无效设备！"
}`
 + 失败：` {
	status: "9996",
	msg: "状态错误！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口8： 技能拥有者确认
- url: abli/confirmContract.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ABLI_ORDER_ID: '3e4d0db57bef4158b79be3bb27c4aa7b',// required`
		 `USER_ID:'c21375a7aef447239c74fc9e7926cd35'，// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可。
	+ 判断当前订单状态，若非10，提示-9996.
	+ 修改订单状态为30
	+ 返回数据dataset中分为`ABLI_ORDER`技能订单详情,`ABLI_INFO`所购买的技能详情
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{ABLI_ORDER:{T_ABLITY_ORDERS中所有字段，用户名称USER_NAME，头像USER_ICON},ABLI_INFO:{T_USER_ABLITY中所有字段，用户名称USER_NAME，头像USER_ICON}}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`
 + 失败：` {
	status: "9997",
	msg: "无效设备！"
}`
 + 失败：` {
	status: "9996",
	msg: "状态错误！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口9： 技能拥有者完成技能
- url: abli/doneAbility.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ABLI_ORDER_ID: '3e4d0db57bef4158b79be3bb27c4aa7b',// required`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35'// required`
 		`DONE_ICON1: ''，// required`
		`DONE_ICON2: ''，// required`
		`DONE_ICON3: ''，// required`
		`DONE_ICON4: ''，// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 判断当前订单状态，若非30，提示9996.
	+ 修改订单状态为40
	+ 用户可上传完成的图片
	+ 返回数据dataset中分为`ABLI_ORDER`技能订单详情,`ABLI_INFO`所购买的技能详情
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{ABLI_ORDER:{T_ABLITY_ORDERS中所有字段，用户名称USER_NAME，头像USER_ICON},ABLI_INFO:{T_USER_ABLITY中所有字段，用户名称USER_NAME，头像USER_ICON}}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`

 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9996",
	msg: "状态错误！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口10： 购买者确认完成 
- url: abli/confirmDone.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		 `ABLI_ORDER_ID: '3e4d0db57bef4158b79be3bb27c4aa7b',// required`
		 `USER_ID:'c21375a7aef447239c74fc9e7926cd35',// required 当前用户ID`
         `ABLI_USER_ID: 'c21375a7aef447239c74fc9e7926cd35'，// required 拥有技能的用户ID，`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 判断当前任务状态，若非40，提示9996.
	+ 修改任务为竞标者确认完成状态50。
	+ 从系统账户扣除此次购买技能的金额，减去平台收取费用，奖励给技能完成者
	+ 返回数据dataset中分为`ABLI_ORDER`技能订单详情,`ABLI_INFO`所购买的技能详情
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{ABLI_ORDER:{T_ABLITY_ORDERS中所有字段，用户名称USER_NAME，头像USER_ICON},ABLI_INFO:{T_USER_ABLITY中所有字段，用户名称USER_NAME，头像USER_ICON}}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`

 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9996",
	msg: "状态错误！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口11： 购买者确认未完成 
- url: abli/confirmUnDone.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ABLI_ORDER_ID: '3e4d0db57bef4158b79be3bb27c4aa7b',// required`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35'// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 判断当前订单状态，若非40，提示9996.
	+ 修改订单为发布者确认订单未完成状态60。
	+ 返回数据dataset中分为`ABLI_ORDER`技能订单详情,`ABLI_INFO`所购买的技能详情
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{ABLI_ORDER:{T_ABLITY_ORDERS中所有字段，用户名称USER_NAME，头像USER_ICON},ABLI_INFO:{T_USER_ABLITY中所有字段，用户名称USER_NAME，头像USER_ICON}}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`

 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9996",
	msg: "状态错误！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口12： 购买者删除订单
- url: abli/deleteOrder.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ABLI_ORDER_ID: '3e4d0db57bef4158b79be3bb27c4aa7b',// required`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35'// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 判断当前订单状态，若非80，提示9996.
	+ 修改订单状态为70
    + 返回数据dataset中分为`ABLI_ORDER`技能订单详情,`ABLI_INFO`所购买的技能详情
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{ABLI_ORDER:{T_ABLITY_ORDERS中所有字段，用户名称USER_NAME，头像USER_ICON},ABLI_INFO:{T_USER_ABLITY中所有字段，用户名称USER_NAME，头像USER_ICON}}}`
 + 失败：` {
	status: "9999",
	msg: "无效用户！"
}`

 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9996",
	msg: "状态错误"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口13： 查询订单详情
- url: abli/getAbliOrderById.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35',// required`
		`ABLI_ORDER_ID:'3e4d0db57bef4158b79be3bb27c4aa7b',// required`
		`ABLI_ID:'4e3652172ff8499d888959e2744a85ee'// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可。
	+ 返回数据dataset中分为`ABLI_ORDER`技能订单详情,`ABLI_INFO`所购买的技能详情
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{ABLI_ORDER:{T_ABLITY_ORDERS中所有字段，用户名称USER_NAME，头像USER_ICON},ABLI_INFO:{T_USER_ABLITY中所有字段，用户名称USER_NAME，头像USER_ICON}}}`
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
###接口14： 查询订单列表
- url: abli/getAbliOrders.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35',`
        `page:'1',`
        `pageSize:'5',`
        `sort:'PAY_TIME.desc',`
        `GET_TYPE:'1'// 若为1则前者获取购买我的技能的订单列表，否则获取我购买的订单信息列表`
`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[{T_ABLITY_ORDERS表中所有字段，USER_NAME用户名称，USER_ICON用户头像}]"dataset":{totalCount:}}`
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
###接口15：根据用户ID来获取对某一个订单中某一个技能的评论
- url: task/searchTaskList.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35',// required`
		`ABLI_ORDER_ID:'3e4d0db57bef4158b79be3bb27c4aa7b',// required`
		`ABLI_ID:'4e3652172ff8499d888959e2744a85ee'// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{T_USER_ABLITY_COMMENTS表中所有字段，USER_NAME用户名称，USER_ICON用户头像}}`
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
###接口16： 增加技能评论
- url: abli/addComment.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`ABLI_ORDER_ID: '3e4d0db57bef4158b79be3bb27c4aa7b',// required`
        `ABLI_ID: '4e3652172ff8499d888959e2744a85ee',// required`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35',// required`
        `TOTAL_POINT: '4',// required`
        `COMENT_TITLE: 'pretty good',// required`
        `COMMENT_DESC: 'pretty good',// required`
        `REPLY: 'good'// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{T_USER_ABLITY_COMMENTS表中所有字段，USER_NAME用户名称，USER_ICON用户头像}}`
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
###接口17： 删除评论
- url: abli/delComment.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID:'c21375a7aef447239c74fc9e7926cd35',// required`
		`ABLI_ORDER_ID:'3e4d0db57bef4158b79be3bb27c4aa7b',// required`
		`ABLI_ID:'4e3652172ff8499d888959e2744a85ee'// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
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