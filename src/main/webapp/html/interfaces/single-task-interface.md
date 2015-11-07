##Single Task Interface
####基本地址：http: //123.56.45.223:8080/echo/
###接口1： 发布任务 - 发布者
- url: task/addTask.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`SECTOR_ID: '100012',// required任务二级分类ID`
		`SECTOR_TYPE: '10',// required任务一级分类ID`
		`TASK_TITLE: ''，// required任务标题`
		`TASK_DESC: '10',// required任务描述`
		`TASK_DEADLINE: '2015-09-27 18:46:00',// required任务截止时间`
		`TASK_TYPE: '10',// required任务类别：10-金钱任务，20-金币任务`
		`TASK_TOTAL_PAID: '10',// required任务支付金额或积分`
		`TASK_ADDR: '10',// required任务地址`
		`TASK_LONGITUDE: '10',// required任务经度`
		`TASK_LATITUDE: '10',// required任务维度`
		`TASK_ICON1: '10',// required任务图片`
		`TASK_ICON2: '10',// required任务图片`
		`TASK_ICON3: '10',// required任务图片`
		`TASK_ICON4: '10',// required任务图片`
		`TASK_AREA: '10'// required任务距离范围:10-10公里范围内；20-全城；30-全国`
		`CANT_CODE: '10000'// required发布任务时所在城市地区码`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可发任务。
	+ 积分任务只能由平台发布。
	+ 若为金钱任务，则：
	  + 1.产生一条任务记录T_TASKS
	  + 2.用户产生一条交易记录T_USERS_MONEY
	  + 3.用户总金额减少T_USERS_EXPAND
	  + 4.系统虚拟账户总金额增加T_USERS_EXPAND
	  + 5.系统虚拟账户产生一条交易记录T_USERS_MONEY
	+ 若为金币任务，则只是产生一条任务记录。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset":{T_TASKS表中所有字段}}`
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
	msg: "余额不足，请充值！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口2： 删除任务 - 发布者
- url: task/deleteTask.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可。
	+ 非取消任务状态20，或者完成任务状态70，不能取消任务。
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
	status: "9996",
	msg: "任务进行中，不能删除！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口3： 修改任务：修改任务信息、修改任务前期状态 - 发布者
- url: task/updateTask.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`TASK_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required任务ID`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`SECTOR_ID: '100012',// optimal任务二级分类ID`
		`SECTOR_TYPE: '10',// optimal任务一级分类ID`
		`TASK_TITLE: ''，// optimal任务标题`
		`TASK_DESC: '10',// optimal任务描述`
		`TASK_DEADLINE: '2015-09-27 18:46:00',// optimal任务截止时间`
		`TASK_TYPE: '10',// optimal任务类别：10-金钱任务，20-金币任务`
		`TASK_TOTAL_PAID: '10',// optimal任务支付金额或积分`
		`TASK_ADDR: '10',// optimal任务地址`
		`TASK_LONGITUDE: '10',// optimal任务经度`
		`TASK_LATITUDE: '10',// optimal任务维度`
		`TASK_ICON1: '10',// optimal任务图片`
		`TASK_ICON2: '10',// optimal任务图片`
		`TASK_ICON3: '10',// optimal任务图片`
		`TASK_ICON4: '10',// optimal任务图片`
		`TASK_AREA: '10'// optimal任务距离范围:10-10公里范围内；20-全城；30-全国`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可。
	+ 接口判断当前任务状态，只有为发布状态10时，方可修改任务。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[任务竞标详情T_TASKS_LINE],"dataset":{任务详情T_TASKS}}`
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
	msg: "任务执行中，不能修改！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口4： 取消任务
- url: task/cancelTask.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 接口判断当前任务状态，只有为发布状态10时，方可取消任务。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[任务竞标详情T_TASKS_LINE],"dataset":{任务详情T_TASKS}}`
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
	msg: "任务进行中，不能取消！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口5： 竞标任务：竞标人发起
- url: task/bideTask.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 接口判断当前任务状态，只有为发布状态10时，方可竞标任务。
	+ 任务行表增加一条竞标者的记录T_TASKS_LINE
	+ 任务头表修改任务状态为竞标状态，增加总竞标人数。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[任务竞标详情T_TASKS_LINE],"dataset":{任务详情T_TASKS}}`
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
	msg: "任务执行中，不能竞标！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口6： 取消竞标：竞标人发起
- url: task/cancelBideTask.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 接口判断当前竞标人状态，如果已经是中标状态，则不允许取消-9996。
	+ 若用户未中标，则修改T_TASKS_LINE中用户状态为取消竞标状态；同时头表`T_TASKS.BID_NUM`自减少1，判断此时数量，若为0，则修改任务由中标状态为发布状态。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[任务竞标详情T_TASKS_LINE],"dataset":{任务详情T_TASKS}}`
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
	msg: "您已获得任务，请联系任务发布者取消！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口7： 选择竞标人：发布者
- url: task/chooseTasker.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
		`BIDE_USER_ID: 'accf734342494d0fa638233079a4cc82'// required选择的竞标人ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 判断当前任务状态，若不是他人投标状态，则提示-9996.
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[中标人信息T_TASKS_LINE],"dataset":{任务信息T_TASKS}}`
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
	msg: "任务进行中，无法选定竞标者！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口8： 中标人回退任务: 选定中标人之后，与中标人协商解除任务 - 中标人
- url: task/bidBackTasker.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可上传商品。
	+ 判断当前用户对任务的状态，若未中标，则提示9996.
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[中标人信息T_TASKS_LINE],"dataset":{任务信息T_TASKS}}`
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
	msg: "非中标状态，无法回退！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口9： 发布者回退任务: 选定中标人之后，与中标人协商解除任务 - 发布者
- url: task/backTasker.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 判断当前任务状态，若非49，提示9996.
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[中标人信息T_TASKS_LINE],"dataset":{任务信息T_TASKS}}`
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
	msg: "请联系中标人取消任务！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口10： 完成任务: 竞标人发起
- url: task/doneTask.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
		`BIDE_USER_ID: 'accf734342494d0fa638233079a4cc82'// required用户ID`
		`DONE_ICON1: '10',// required任务图片`
		`DONE_ICON2: '10',// required任务图片`
		`DONE_ICON3: '10',// required任务图片`
		`DONE_ICON4: '10',// required任务图片`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 判断当前任务状态，若非40，提示9996.
	+ 修改任务为竞标者完成状态，同时提交完成的图片。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[中标人信息T_TASKS_LINE],"dataset":{任务信息T_TASKS}}`
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
	msg: "任务状态错误，无法完成！"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口11： 未完成任务：发布者
- url: task/undoneTask.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 判断当前任务状态，若非60，提示9996.
	+ 修改任务为发布者确认任务未完成状态。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[中标人信息T_TASKS_LINE],"dataset":{任务信息T_TASKS}}`
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
	msg: "当前任务未完成，无法点击"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口12： 结束任务: 发布者
- url: task/closeTask.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
		`BIDE_USER_ID: 'accf734342494d0fa638233079a4cc82'// required竞标用户ID`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 判断当前任务状态，若非60，提示9996.
	+ 若为金钱任务，则：
	  + 1.修改任务状态为关闭状态，更新任务头行表
	  + 2.计算本次任务奖励完成这的金额，平台抽取费用
	  + 3.竞标用户产生一条交易记录T_USERS_MONEY
	  + 4.竞标用户总金额增加T_USERS_EXPAND
	  + 5.系统虚拟账户总金额减少T_USERS_EXPAND
	  + 6.系统虚拟账户产生一条交易记录T_USERS_MONEY
	+ 若为金币任务，则只是产生一条任务记录。
	  + 1.修改任务状态为关闭状态，更新任务头行表
	  + 2.竞标用户产生一条积分交易记录T_USERS_POINT
	  + 3.竞标用户总积分增加T_USERS_EXPAND
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[中标人信息T_TASKS_LINE],"dataset":{任务信息T_TASKS}}`
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
	msg: "当前任务未完成，无法关闭"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口13： 查询用户竞标任务列表
- url: task/searchBidTaskByUserId.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`page: '1'// required`
		`pageSize: 'accf734342494d0fa638233079a4cc82'// required`
		`LONGITUDE: '1'// required`
		`LATITUDE: 'accf734342494d0fa638233079a4cc82'// required`
		`IS_BIDE: 'accf734342494d0fa638233079a4cc82'// optimal`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 默认按照竞标距离-正序，竞标时间-倒序，任务创建时间-正序排序
	+ 返回字段包含任务表中所有数据、发任务人的头像和照片、本人竞标任务的时间和中标状态
	+ `IS_BIDE`若此字段传递，则筛选中标状态的任务
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[任务列表],"dataset":{totalCount}}`
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
###接口14： 查询任务：查询单个任务
- url: task/searchTaskById.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
		`page: '1',// required`
		`pageSize: '5'// required`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[任务行表]"dataset":{任务头表,totalCount:总留言数量，MSG_LIST：[当前分页留言]}}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口15：查询任务：查询任务列表
- url: task/searchTaskList.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// optimal用户ID`
		`page: '1'// required`
		`pageSize: 'accf734342494d0fa638233079a4cc82'// required`
		`LONGITUDE: '1'// required`
		`LATITUDE: 'accf734342494d0fa638233079a4cc82'// required`
		`SECTOR_ID: 'accf734342494d0fa638233079a4cc82'// optimal二级分类ID`
		`SECTOR_TYPE: 'accf734342494d0fa638233079a4cc82'// optimal一级分类ID`
		`TASK_TITLE: 'accf734342494d0fa638233079a4cc82'// optimal任务标题`
		`TASK_AREA: 'accf734342494d0fa638233079a4cc82'// optimal任务范围`"";
		`sort: DIST.asc,TASK_CREATE_TIME.desc,TASK_TOTAL_PAID.desc,BID_NUM.desc'// required`，
		`BID_STATUS: '10'// optimal 查询TASK_BID_STATUS为10，30的任务`
		`TASK_BID_STATUS: ''// optimal查询TASK_BID_STATUS为任务`
		`DEAD_TASK: '10'// optimal查询过期任务`
		`CANT_CODE: ''// optimal查询城市区号任务`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 注意：
	+ 传递USER_ID为了与SESSION中的USER_ID比较，一致的情况下，方可下单。
	+ 默认按照距离-正序，创建时间-倒序排序
	+ 返回任务表中所有数据，发任务人的头像和名称
- 返回值：
 + 成功： `{"status":"0000","msg":"成功","dataset_line":[任务行表],"dataset":{任务头表}}`
 + 失败：` {
	status: "9997",
	msg: "无效设备"
}`
 + 失败：` {
	status: "9992",
	msg: "后台程序执行失败",
}`
###接口16： 任务留言和回复
- url: task/leaveMsg.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`TASK_ID: 'accf734342494d0fa638233079a4cc82'// required任务ID`
		`MESSAGE: '1cc2d9e2a9cc4e4983b76d03e432d345',// required留言`
		`REPLY: 'accf734342494d0fa638233079a4cc82'// required回复`
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
###接口17： 删除任务留言
- url: task/delMsg.do
- 参数一： 参数对象
	`dataParm: {`
	`dataset: {`
		`USER_ID: '1cc2d9e2a9cc4e4983b76d03e432d345',// required用户ID`
		`delMsg: 'accf734342494d0fa638233079a4cc82'// required留言ID`
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