###INTERFACE LOG
##### 用户相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/login-interface.html](http://123.56.45.223:8080/echo//html/interfaces/login-interface.html "用户相关接口文档")
##### 店铺相关接口文档：[http://123.56.45.223:8080/echo//html/interfaces/merchant-interface.html](http://123.56.45.223:8080/echo//html/interfaces/merchant-interface.html "店铺相关接口文档")
#### 2015-9-7
- 增加用户退出的接口，请查看[http://123.56.45.223:8080/echo//html/interfaces/login-interface.html](http://123.56.45.223:8080/echo//html/interfaces/login-interface.html "用户相关接口文档")-接口11。
- 调用需要sc_no的接口,如果发现sc_no不一致，执行回话清除动作。
- 商品查询接口mer/searchMerItem.do不再需要SC_NO。
- 商品查询接口,对于传入的参数中含有ITEM_ID的请求, 返回的dataset中的BELONG_ITEM用来表示该商品是否为用户自己店铺的商品；1-是，0-非。 
- 增加用户注册时初始化用户扩展表。
- 增加申请店铺时初始化店铺扩展表。
- 增加升级商品为团购商品的接口。 
- 增加获取店铺信息，以及获取店铺下商品信息的接口。   
#### 2015-9-8
- 调用更新用户信息的接口，返回参数中包含了数据库执行更新后的用户信息。
- 调用更新店铺信息的接口，返回参数中包含了数据库执行更新后的店铺信息。