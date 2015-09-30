###INTERFACE LOG
##### 用户登陆相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/login-interface.html](http://123.56.45.223:8080/echo//html/interfaces/login-interface.html "用户相关接口文档")
##### 店铺相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/merchant-interface.html](http://123.56.45.223:8080/echo//html/interfaces/merchant-interface.html "店铺相关接口文档")
##### 用户操作相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/user-interface.html](http://123.56.45.223:8080/echo//html/interfaces/user-interface.html "用户相关接口文档")
##### 用户订单相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/order-interface.html](http://123.56.45.223:8080/echo//html/interfaces/order-interface.html "用户订单相关接口文档")
##### 商品评价相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/comment-order-interface.html](http://123.56.45.223:8080/echo//html/interfaces/comment-order-interface.html "商品评价相关接口文档")
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