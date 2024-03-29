###INTERFACE LOG
##### 用户登陆相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/login-interface.html](http://123.56.45.223:8080/echo//html/interfaces/login-interface.html "用户相关接口文档")
##### 店铺相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/merchant-interface.html](http://123.56.45.223:8080/echo//html/interfaces/merchant-interface.html "店铺相关接口文档")
##### 用户操作相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/user-interface.html](http://123.56.45.223:8080/echo//html/interfaces/user-interface.html "用户相关接口文档")
##### 用户订单相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/order-interface.html](http://123.56.45.223:8080/echo//html/interfaces/order-interface.html "用户订单相关接口文档")
##### 商品评价相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/comment-order-interface.html](http://123.56.45.223:8080/echo//html/interfaces/comment-order-interface.html "商品评价相关接口文档")
##### 单人任务相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/single-task-interface.html](http://123.56.45.223:8080/echo//html/interfaces/single-task-interface.html "单人任务相关接口文档")
##### 多人任务相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/mul-task-interface.html](http://123.56.45.223:8080/echo//html/interfaces/mul-task-interface.html "多人任务相关接口文档")
##### 用户技能相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/user-ability-interface.html](http://123.56.45.223:8080/echo//html/interfaces/user-ability-interface.html "用户技能相关接口文档")
#### 2015-9-7
- 增加用户退出的接口，请查看[http://123.56.45.223:8080/echo//html/interfaces/login-interface.html](http://123.56.45.223:8080/echo//html/interfaces/login-interface.html "用户相关接口文档")-接口11。
- 调用需要sc_no的接口,如果发现sc_no不一致，执行回话清除动作。
- 商品查询接口mer/searchMerItem.do不再需要SC_NO。
- 商品查询接口,对于传入的参数中含有`ITEM_ID`的请求, 返回的dataset中的`BELONG_ITEM`用来表示该商品是否为用户自己店铺的商品；1-是，0-非。  
- 增加用户注册时初始化用户扩展表。
- 增加申请店铺时初始化店铺扩展表。
- 增加升级商品为团购商品的接口。 
- 增加获取店铺信息，以及获取店铺下商品信息的接口。   
#### 2015-9-8
- 调用更新用户信息的接口，返回参数中包含了数据库执行更新后的用户信息。
- 调用更新店铺信息的接口，返回参数中包含了数据库执行更新后的店铺信息。
### 2015-9-10
- 店铺表增加店铺信息完善的最新更新时间戳
- 对于执行增删改的操作,后台拦截异常; 以及后台程序的异常, 统一以9992的异常码返回。
### 2015-9-12
- 增加查询店铺列表接口
- 订单表结构变更
### 2015-9-13
- 增加机构参数`MER_ITEM_MAX_QTY`,用来限制店铺上传商品的最大数量。
### 2015-9-14
- 查询具体店铺接口`mer/getMerDetail`增加用户是否收藏该店铺，`IS_COLL`不为空
- 查询商品接口`mer/searchMerItem`,增加查询具体商品时,返回该商品是否被当前用户所收藏的标志字段`IS_COLL`为1
- 增加用户收藏、删除店铺和商品的接口。注意：客户端收藏店铺时，判断用户是否已经收藏。请查看[http://123.56.45.223:8080/echo//html/interfaces/user-interface.html](http://123.56.45.223:8080/echo//html/interfaces/user-interface.html "用户相关接口文档")
  + user/addMerColl
  + user/addItemColl
  + user/deleteMerColl
  + user/deleteItemColl
  + user/getMerColl
  + user/getItemColl
### 2015-9-17
- 更新商品信息增加商品原价`ORI_PRICE`的维护
- 商品升级为团购商品, 不再需要商品现价`CURR_PRICE`参数
### 2015-9-22
- 增加下单接口
- 增加取消订单接口
### 2015-9-27
- 订单相关接口请查看[http://123.56.45.223:8080/echo//html/interfaces/order-interface.html](http://123.56.45.223:8080/echo//html/interfaces/order-interface.html "用户订单相关接口文档")
- 评论相关接口清查看[http://123.56.45.223:8080/echo//html/interfaces/comment-order-interface.html](http://123.56.45.223:8080/echo//html/interfaces/comment-order-interface.html "商品评价相关接口文档")
- 增加支付和退款订单接口
- 增加消费、关闭、删除、获取订单接口
- 增加商品评论相关接口
### 2015-9-28
- 增加店铺删除商品的接口
### 2015-9-29
- 删除订单接口`order/delOrder.do`, 增加两种状态1.已付款，未消费不能删除订单2.已消费，未关闭订单不能删除
- 查询店铺列表接口`mer/getMerList.do`,增加消费者当前经纬度，计算周围店铺的距离，按照由近到远返回列表
### 2015-9-30
- 框架加入定时器
- 查询附件店铺, 优化计算GPS距离计算
- 修复获取店铺下商品, 已删除商品出现的bug
### 2015-10-1
- 在用户关闭订单后，将本次消费产生的金额累计到店铺所在的用户账户
- 定时器自动关闭已消费未关闭的订单，此时积分的返还由配置文件中的参数起作用
### 2015-10-2
- 获取NO_CO时返回初始化数据时，同时返回初始化基本信息数据
### 2015-10-3
- order/getOrders接口ITEM_INFO字段返回购买商品信息
### 2015-10-4-作废
- "user/getUserExpandInfo",// 获取用户扩展信息
- "task/searchTaskList.do",// 查询任务：查询任务列表
- "task/searchTaskById.do"// 查询任务：查询单个任务
- "task/addTask.do",// 增加任务 - 发布者
- "task/deleteTask.do",// 删除任务 - 发布者
- "task/updateTask.do",// 修改任务：修改任务信息、修改任务前期状态 - 发布者
- "task/bideTask.do",// 竞标任务：竞标人发起
- "task/cancelBideTask.do",// 取消竞标：竞标人发起
- "task/searchBidTaskByUserId.do"// 查询用户竞标任务列表
### 2015-10-6
- "order/payAction"用户付款接口
- "order/backPay"用户退款接口
- 增加调用beecloud网平台账户充值接口
### 2015-10-7
- "user/getUserExpandInfo.do"获取余额、积分、能力
### 2015-10-12
- "order/addMallOrder.do"增加积分购买商品，直接下单并处于支付状态接口
- 订单表T_ORDERS增加FROM_TYPE：10-金钱订单，20-金币订单
- 积分订单，用户去平台消费时，直接关闭订单，无需用户关闭
- 返回的订单信息中增加了FROM_TYPE 
### 2015-10-13
- 发布单人任务相关的15个接口
- 单人任务相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/single-task-interface.html](http://123.56.45.223:8080/echo//html/interfaces/single-task-interface.html "单人任务相关接口文档")
### 2015-10-20
- 获取开通城市列表接口：用户登陆相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/login-interface.html](http://123.56.45.223:8080/echo//html/interfaces/login-interface.html "用户相关接口文档") 接口12
- 任务留言和删除接口：单人任务相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/single-task-interface.html](http://123.56.45.223:8080/echo//html/interfaces/single-task-interface.html "单人任务相关接口文档") 接口16，17
### 2015-10-22
- 多人任务相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/mul-task-interface.html](http://123.56.45.223:8080/echo//html/interfaces/mul-task-interface.html "多人任务相关接口文档")
### 2015-10-30
- 用户技能相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/user-ability-interface.html](http://123.56.45.223:8080/echo//html/interfaces/user-ability-interface.html "用户技能相关接口文档")
- 系统每日进行定时任务，判断`T_ABLITY_ORDERS.STATUS`表中DEADLINE超期的订单，根据订单状态为10，60的将付款从系统账户返还到用户，40的讲付款从系统账户付款到任务完成者