<%@ page import="java.security.MessageDigest"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>spay demo</title>
</head>
<body>
<button id="test">test online</button>
</div>
</body>
<!--1.添加控制台活的的script标签-->
<script id='spay-script' src='https://jspay.beecloud.cn/1/pay/jsbutton/returnscripts?appId=60e2b980-b218-415d-b0ff-0691c130f721'></script>
<script type="text/javascript">
    function asyncPay() {
        if (typeof BC == "undefined"){
            if( document.addEventListener ){
                document.addEventListener('beecloud:onready', bcPay, false);
            }else if (document.attachEvent){
                document.attachEvent('beecloud:onready', bcPay);
            }
        }else{
            bcPay();
        }
    }
    document.getElementById("test").onclick = function() {
        asyncPay();
    };
    function bcPay() {
        /**
         * click调用错误返回：默认行为console.log(err)
         */
        BC.err = function(data) {
            //注册错误信息接受
            alert(data["ERROR"]);
        }
        /**
         * 3. 调用BC.click 接口传递参数
         */
        BC.click({
            "title": "water",
            "amount": "1",
            "out_trade_no": "1111111111", //唯一订单号
            "trace_id" : "2222222222",
            "sign" : "2717d7de2bed3fa74f6443817f5db3df",
            "debug": true,
            "return_url" : "http://payservice.beecloud.cn/spay/result.php",
            /**
             * optional 为自定义参数对象，目前只支持基本类型的key ＝》 value, 不支持嵌套对象；
             * 回调时如果有optional则会传递给webhook地址，webhook的使用请查阅文档
             */
            "optional": {"test": "willreturn"}
        });

    }
</script>
</html>