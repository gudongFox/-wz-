<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/5/15
  Time: 17:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>修改密码</title>
</head>
<script charset="UTF-8" src="/m/global/plugins/jquery.min.js"></script>
<script charset="UTF-8" src="/m/global/plugins/jquery-migrate.min.js"></script>
<link href="/m/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="/m/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
<link href="/m/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="/m/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
<link href="/m/global/plugins/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
<link href="/m/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker.min.css" rel="stylesheet"/>
<script charset="UTF-8" src="/m/global/plugins/bootstrap-toastr/toastr.min.js"></script>
<link href="/m/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet"/>

<link href="/m/global/plugins/jquery-file-upload/css/jquery.fileupload.css" rel="stylesheet"/>
<link href="/m/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet"/>
<link href="/m/jstree/themes/default/style.min.css" rel="stylesheet"/>

<link href="/m/global/css/plugins.css" rel="stylesheet" type="text/css"/>
<link href="/m/admin/layout/css/custom.css" rel="stylesheet" type="text/css"/>
<script src="/assets/js/jquery.cookie.js"></script>
<style>
    body{
        position:absolute; left:0px; top:0px;
        width:100%;
        height:100%;
        background-image: url("/m/admin/pages/media/bg/3.jpg");
        background-repeat: no-repeat;
        background-size:100%;
    }
</style>
<body >
    <div style="width: 400px;margin: 0 auto">
    <form class="login-form"  method="post" autocomplete="off" disableautocomplete>
        <h3 class="form-title" style="text-align: center">修改密码</h3>
        <div class="alert alert-danger display-hide">
            <button class="close" data-close="alert"></button>
            <span style="font-size: 20px;color: blue" id="message">${message}</span><br>
            <span style="color: red;font-size: 12px">密码要求：<span style="color: red;font-size: 12px">密码必须包含大写字母、小写字母、数字、特殊符号，且长度在8-16位</span></span>
        </div>
        <div class="form-group" >
            <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
            <label class="control-label visible-ie8 visible-ie9">用户名</label>
            <div class="input-icon" hidden>
                <input class="form-control placeholder-no-fix"  type="text" disabled
                       placeholder="用户名" value="${enLogin}" name="enLogin" id="txt_userLogin"/>
            </div>
            <div class="input-icon">
                <input class="form-control placeholder-no-fix"  type="text"
                       placeholder="用户名" value="${userNo}" id="txt_login"/>
            </div>
        </div>
        <div class="form-group" id="check" >
            <label class="control-label visible-ie8 visible-ie9">验证码</label>
            <div class="input-icon">
                <div class="input-group">
                    <input class="form-control placeholder-no-fix" type="text"
                           autocomplete="off" id="checkCode"  maxlength="6" minlength="6" placeholder="请输入6位验证码" name="checkCode"/>
                    <span class="input-group-btn"  >
                        <button class="btn default" type="button" onclick="sendCode();" id="checkCodeBtn" style="color: #0b94ea" >发送验证码</button>
                    </span>
                </div>
            </div>
        </div>
        <div class="form-group" >
            <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
            <label class="control-label visible-ie8 visible-ie9">新密码</label>
            <div class="input-icon">
                <input class="form-control placeholder-no-fix"  type="password"
                       placeholder="新密码" style="background-color: white" name="" id="pwd1" readonly onfocus="this.removeAttribute('readonly');" onblur="this.setAttribute('readonly',true);"/>

            </div>
        </div>
        <div class="form-group" style="margin-bottom: 20px;">
            <label class="control-label visible-ie8 visible-ie9">确认密码</label>
            <div class="input-icon">
                <input class="form-control placeholder-no-fix" type="password"  placeholder="确认密码"
                       name="" id="pwd2" style="background-color: white" readonly onfocus="this.removeAttribute('readonly');" onblur="this.setAttribute('readonly',true);"/>
            </div>
        </div>


        <button type="button" class="btn blue pull-right" onclick="getCookie();">
            确认
        </button>

    </form>
</div>
</body>

<script src="/assets/js/jquery.cookie.js"></script>
<%--<script src="/m/admin/pages/scripts/login-soft.js"></script>--%>


<script type="text/javascript">
    var message=document.getElementById("message");

    function getCookie() {
        var userLogin=document.getElementById("txt_login").value;
        var pwd1=document.getElementById("pwd1").value;
        var pwd2=document.getElementById("pwd2").value;
        var checkCode=document.getElementById("checkCode").value;

        if (userLogin.length>0){
            if(pwd1.length<8) {
                message.textContent="新的密码长度应不小于8!";
                return;
            }
            if(pwd1.length=="") {
                message.textContent="用户名不能为空!";
                return;
            }
            if(pwd1!=pwd2){
                document.getElementById("pwd2").value="";
                message.textContent="两次输入密码不一致!";
                return;
            }
            if (checkPass(pwd1)<4){
                document.getElementById("pwd2").value="";
                message.textContent="新密码复杂度不够，请重新设置！需6-16位，且包含大小写字母、数字和特殊字符!";
                return;
            }
            if(checkCode.length!=6){
                message.textContent="验证码不能为空且为6位数字!";
                return;
            }
            $.ajax({
                type: "POST",
                dataType: "json",
                url: "/hr/employee/resetPassword.json",
                data:{userLogin:userLogin,password:pwd1,checkCode:checkCode},
                success: function (d) {
                    if(d.ret){
                        if (d.data.indexOf("成功")>-1){
                            setTimeout(back ,1000);
                        }else {
                            message.textContent=d.data;
                            return;
                        }
                    } else {

                    }
                }
            });
        }else {
            message.textContent="用户名不能为空!";
            return;
        }


    }
    function back() {
        window.location.href="/login"
    }
    function checkPass(pass){
        if(pass.length <=8&&pass.length>=16){
            return 0;
        }
        var str = 0;
        if(pass.match(/([a-z])+/)){
            str++;
        }
        if(pass.match(/([0-9])+/)){
            str++;
        }
        if(pass.match(/([A-Z])+/)){
            str++;
        }
        if(pass.match(/[ _`~!@#$%^&*()\-+=|{}':;',\[\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+/)){
            str++;
        }
        /* if(pass.match(/[^a-zA-Z0-9]+/)){
            str++;
        }*/
        return str;
    }

    function sendCode() {

        if (flag) {
            var enLogin = document.getElementById("txt_login").value;
            var sign=false;
            if (enLogin==null||enLogin==''){
                message.textContent="用户名不能为空!";
            }else {
                $.ajax({
                    method: 'POST',
                    url: '/hr/employee/getMobile.json',
                    data: {
                        enLogin:enLogin,
                    }
                }).then(function (response) {
                    if (response.ret){
                        if (response.data.status){
                            message.textContent=response.data.message;
                            sendSMS(enLogin);
                        }else {
                            message.textContent=response.data.message;
                        }
                    }else {
                        message.textContent="获取手机号失败请确实用户名正确,并重试!";
                    }
                });
            }

        }
    }

    function sendSMS(enLogin) {
        if (flag) {
            // 获取验证码
            $.ajax({
                method: 'POST',
                url: '/sendCheckCode',
                data: {
                    enLogin:enLogin,
                }
            }).then(function (response) {
                if (response.data.status){
                    time=60;
                    $("#checkCodeBtn").text("请稍后");
                    //开启定时器
                    id = setInterval(showTime, 1000);
                    flag = false;
                    $("#checkCodeBtn").attr("onclick", "return false");
                    $("#checkCodeBtn").attr("style", "cursor: default;opacity: 0.2");
                }else {
                    toastr.error(response.data.message);
                }

            });
        }
    }

    var time = 60;
    var flag = true;
    var id;
    function showTime() {
        if (time > 0) {
            $("#checkCodeBtn").text("已发送" + time + "s");
            time--;
            $.cookie("total",time);
        } else {
            $("#checkCodeBtn").text("发送验证码");
            flag = true;
            time = 60;
            // 清除定时器
            clearInterval(id);
            total=$.cookie("total",time, { expires: -1 });
            $("#checkCodeBtn").attr("onclick", "sendCode()");
            $("#checkCodeBtn").attr("style","color: #0b94ea");
        }
    }

    function showCheckButton(e) {

        if($.cookie("total")!=undefined&&$.cookie("total")!='NaN'&&$.cookie("total")!='null'){
            //cookie存在倒计时}
            time= $.cookie("total");
            id = setInterval(showTime, 1000);
            flag = false;
            $("#checkCodeBtn").attr("onclick", "return false");
            $("#checkCodeBtn").attr("style", "cursor: default;opacity: 0.2");
        }else {
            $('#checkCodeBtn').attr("disabled", false);
        }
    }

    window.onload=showCheckButton;

</script>
</html>
