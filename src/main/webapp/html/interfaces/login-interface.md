##Login Interface
####基本地址：http: //123.56.45.223:8080/echo/
###接口1： 获取NORMAL KEY
- url: login/getCode.do
- 参数一： no_co 测试环境用ABCDEFG
- 返回值：
- `{
	status: "0000",
	msg: "成功",
	dataset: {
		no_co: "QUJDREVGRw=="
	}
}`
###接口2： 判断当前用户已注册
- url: login/checkReg.do
- 参数一： tel 手机号
- 参数二： no_co （QUJDREVGRw==）公钥
- 返回值：
 + 成功： `{
	status: "0000",
	msg: "成功",
	dataset: {
		IS_EXIST: "0" // 0表示未注册，1表示已经注册
	}
}
}`
 + 失败：` {
	status: "9998",
	msg: "无效手机号码",
}`
###接口3： 注册用户并登陆
- url: login/registAlg.do
- 参数一： tel 手机号
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： pwd 密码 这里要base64加密后的
- 参数四： ct_code 所在地区编码
- 参数五： 登陆客户端 - 10: 用户客户端； 20 - 店铺客户端
- 返回值： 
 + 成功： `{
	status: "0000",
	msg: "成功",
	dataset: {
		SEC_CODE: "2c26438d1cf344d7a261db03637f947c", // 当前登陆账户的秘钥
		VERFIY: "1", // 注册并登陆成功
		USER_ID: "1b8799e3813f4fa19f24c85e42fd5e3d" // 当前用户ID
	}
}`
 + 失败：` {
	status: "9999",
	msg: "无效客户端注册"
}`
 + 失败：` {
	status: "9998",
	msg: "无效注册设备"
}`
 + 失败：` {
	status: "9997",
	msg: "无效手机号码"
}`
 + 失败：` {
	status: "9996",
	msg: "无效密码"
}`
 + 失败：` {
	status: "9995",
	msg: "该用户已注册"
}`
 + 失败：` {
	status: "9994",
	msg: "注册失败",
}`
###接口4： 用户和店铺登陆
- url: login/login.do
- 参数一： tel 手机号
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： pwd 密码 这里要base64加密后的
- 参数四： 登陆客户端 - 10: 用户客户端； 20 - 店铺客户端
- 返回值： 
 + 成功： ` {
	status: "0000",
	msg: "成功",
	dataset: {
		SEC_CODE: "2c26438d1cf344d7a261db03637f947c", // 当前登陆账户的秘钥
		VERFIY: "1", // 登陆成功
		USER_ID: "1b8799e3813f4fa19f24c85e42fd5e3d", // 当前用户ID
		MERCHANT_ID： '' // 若为店铺登陆 则增加店铺ID
		MERCHANT_TYPE： // 店铺类型：10-普通店铺，20-技能店铺，30-城市店铺，99-积分商城
	}
}`
 + 失败：` {
	status: "9998",
	msg: "无效注册设备"
}`
 + 失败：` {
	status: "9997",
	msg: "无效手机号码"
}`
 + 失败：` {
	status: "9996",
	msg: "无效密码"
}`
 + 失败： ` {
	status: "9995",
	msg: "账号不存在",
	dataset: {
		SEC_CODE: "",
		VERFIY: "0"
	}
}`
 + 失败： ` {
	status: "9994",
	msg: "密码错误",
	dataset: {
		SEC_CODE: "",
		VERFIY: "0"
	}
}`
 + 失败：` {
	status: "9993",
	msg: "您还未申请店铺"
}`
###接口5： 修改密码并登陆
- url: login/changePwd.do
- 参数一： tel 手机号
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： pwd 密码 这里要base64加密后的
- 参数四： 登陆客户端 - 10: 用户客户端； 20 - 店铺客户端
- 返回值： 
 + 成功： ` {
	status: "0000",
	msg: "成功",
	dataset: {
		SEC_CODE: "f9af9275556f42d1aa9223853a339259",
		VERFIY: "1"
	}
}`
 + 失败：` {
	status: "9997",
	msg: "无效手机号码"
}`
 + 失败：` {
	status: "9996",
	msg: "无效密码"
}`
 + 失败：` {
	status: "9995",
	msg: "密码修改失败"
}`
###接口6： 申请店铺权限
- url: user/merApply.do
- 参数一： user_id 用户ID
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 参数四： 登陆客户端 - 10: 用户客户端； 20 - 店铺客户端
- 注意： 用户申请店铺后，状态为10-提交，表示该账号可以登陆店铺端。
- 返回值： 
 + 成功： ` {
	status: "0000",
	msg: "成功",
	dataset: {
		MERCHANT_ID: "1cc2d9e2a9cc4e4983b76d03e432d345" // 店铺ID
	}
}`
 + 失败：` {
	status: "9999",
	msg: "无效客户端" // 只能在用户客户端申请
}`
 + 失败：` {
	status: "9998",
	msg: "无效设备" // 只能在移动端
}`
 + 失败：` {
	status: "9997",
	msg: "无效账号" // 账号参数为空
}`
 + 失败：` {
	status: "9996",
	msg: "该用户已经是店铺用户"
}`
 + 失败：` {
	status: "9995",
	msg: "申请失败"
}`
###接口7： 店铺信息维护
- url: mer/toMerchant.do
- 参数一： 店铺信息参数对象 
	`dataParm: {`
	`dataset: {`
		`mer_id: '1cc2d9e2a9cc4e4983b76d03e432d345',`
		`ut: '20',// 登陆客户端`
		`mer_name: 'MY STORE',// 店铺名称`
		`mer_type: '10',// 店铺类型`
		`mer_ower: 'ethan',// 拥有者`
		`head_icon: '',// 小图标`
		`main_icon: '',// 背景图`
		`cant_code: '',// 地区号`
		`longtitude: '123.11',// 经度`
		`lantitude: '123.11',// 维度`
		`addr: 'test add',// 地址 `
		`ability: 'coding',// 技能 `
		`desc: 'i am a coder',// 描述 `
		`open_hour: '1-24',// 开放时间`
		`busi_type: 'code' // 经营类型`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 注意： 信息提交后，状态为20-审核中状态，等后台管理员审核成功后方可发布商品。
- 返回值： 
 + 成功： `  {
	"status": "0000",
	"msg": "成功",
	"dataset": {
		"MERCHANT_ID": "1cc2d9e2a9cc4e4983b76d03e432d345"
	}
}`
 + 失败：` {
	status: "9999",
	msg: "无效客户端" // 只能在用户客户端申请
}`
 + 失败：` {
	status: "9998",
	msg: "无效设备" // 只能在移动端
}`
 + 失败：` {
	status: "9997",
	msg: "无效账号" // 账号参数为空
}`
 + 失败：` {
	status: "9996",
	msg: "提交失败",
}`
###接口8： 用户基本信息维护
- url: user/updateUserInfo.do
- 参数一： 用户基本信息维护参数对象 
	`dataParm: {`
	`dataset: {`
		`USER_ID: '5e2957a3eab740399e362ff6d2033c09',`
		`ut: '10',// 登陆客户端`
		`USER_NAME: 'Ethan LI',// 名称 `
		`USER_ICON: '1.png',// 用户头像`
		`NICKNAME: 'ethan',// 简称 `
		`GENDER: '1',// 性别 1男 2女`
		`TEL_REC: '0531-88888888',// 备用电话`
		`USER_ADDR:encodeURI('浪潮路1036号'),// 地址，`
		`EMAIL:'programccc@163.com',`
		`ABILITY:encodeURI('转发获利')// 技能描述 `
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 返回值： 
 + 成功： `  {
	"status": "0000",
	"msg": "成功",
}`
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
	msg: "无效邮箱",
}`
###接口9： 用户身份信息维护
- url: user/updateUserIC.do
- 参数一： 用户身份信息维护参数对象 
	`dataParm: {`
	`dataset: {`
		`USER_ID: '5e2957a3eab740399e362ff6d2033c09',`
		`ut: '10',// 登陆客户端`
		`IDEN_CARD: '410521112312312358',// 身份证号`
		`IDEN_ICON: '1.png'// 身份证扫描件`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 返回值： 
 + 成功： `  {
	"status": "0000",
	"msg": "成功",
}`
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
	msg: "身份证不合法",
}`
###接口10： 用户账户信息维护
- url: user/updateUserAcc.do
- 参数一： 用户账户信息维护参数对象 
	`dataParm: {`
	`dataset: {`
		`USER_ID: '5e2957a3eab740399e362ff6d2033c09',`
		`ut: '10',// 登陆客户端`
		`ZFB_ACCOUNT:'12345677890',// 支付宝账号`
		`WX_ACCOUNT:'PROGRAMCCC',// 微信账号`
		`YL_ACCOUNT:'123153545345345',// 银联账号`
		`PAY_PWD:'ssdde23124ggg'// 平台支付密码，采用base64编码传输`
	`}`
	`}`
- 参数二： no_co （QUJDREVGRw==）公钥
- 参数三： sc_co 用户私钥
- 返回值： 
 + 成功： `  {
	"status": "0000",
	"msg": "成功",
}`
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
###接口11： 退出接口
- url: login/exit.do
- 参数一：no_co
- 注意：调用需要sc_no的接口，如果发现sc_no不对，也会清除会话！
- 返回值： 
 + 成功： `  {
	"status": "0000",
	"msg": "成功",
}`