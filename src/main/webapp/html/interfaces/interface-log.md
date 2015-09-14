###INTERFACE LOG
##### 用户相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/login-interface.html](http://123.56.45.223:8080/echo//html/interfaces/login-interface.html "用户相关接口文档")
##### 店铺相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/merchant-interface.html](http://123.56.45.223:8080/echo//html/interfaces/merchant-interface.html "店铺相关接口文档")
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
- 增加用户收藏、删除店铺和商品的接口
  + user/addMerColl
  + user/addItemColl
  + user/deleteMerColl
  + user/deleteItemColl
