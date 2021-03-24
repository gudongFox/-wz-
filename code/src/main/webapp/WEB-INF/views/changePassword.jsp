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

<body >
<div style="width: 400px;margin: 0 auto">
    <form class="login-form"  method="post" autocomplete="off" disableautocomplete>
        <h3 class="form-title" style="text-align: center">修改密码</h3>
        <div class="alert alert-danger display-hide">
            <button class="close" data-close="alert"></button>
            <span style="font-size: 20px;color: blue">${message}</span><br>
            <span style="color: red;font-size: 12px">密码要求：<span style="color: red;font-size: 12px">密码必须包含大写字母、小写字母、数字、特殊符号，且长度在6-16位</span></span>
        </div>
        <div class="form-group" style="margin-bottom: 20px;">
            <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
            <label class="control-label visible-ie8 visible-ie9">用户名</label>
            <div class="input-icon" hidden>
                <input class="form-control placeholder-no-fix"  type="text" disabled
                       placeholder="用户名" value="${userLogin}" id="span_userName"/>
            </div>
            <div class="input-icon">
                <input class="form-control placeholder-no-fix"  type="text" disabled
                       placeholder="用户名" value="${userNo}" id="span_userNo"/>
            </div>
        </div>
        <div class="form-group" style="margin-bottom: 20px;">
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
        <div class="modal-footer">
            <button type="button" class="btn default" data-dismiss="modal">关闭</button>
            <button type="button" class="btn blue" onclick="getCookie();">确认</button>
        </div>

    </form>
</div>
</body>
<script type="text/javascript">
  /*  function myfun() {
        if(location.href.indexOf("#reloaded")==-1){
            location.href=location.href+"#reloaded";
            location.reload();
        }
    }
    /!*用window.onload调用myfun()*!/
    window.onload=myfun;//不要括号*/
  document.onkeydown=keyDownSearch;

  function keyDownSearch(e) {
      // 兼容FF和IE和Opera
      var theEvent = e || window.event;
      var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
      if (code == 13) {
          getCookie();//具体处理函数
          return false;
      }
      return true;
  }

  function getCookie() {
        var userLogin=document.getElementById("span_userName").value;
        var pwd1=document.getElementById("pwd1").value;
        var pwd2=document.getElementById("pwd2").value;
        console.log(pwd1);

        if (pwd1.length>0){
            if(pwd1.length<6) {
                toastr.error("新的密码长度应不小于6!");
                return;
            }
            if(pwd1!=pwd2){
                document.getElementById("pwd2").value="";
                toastr.error("两次输入密码不一致!");
                return;
            }
            if (checkPass(pwd1)<4){
                document.getElementById("pwd2").value="";
                toastr.error("新密码复杂度不够，请重新设置！需6-16位，且包含大小写字母、数字和特殊字符。");
                return;
            }
            $.ajax({
                type: "POST",
                dataType: "json",
                url: "/hr/employee/resetPassword.json",
                data:{userLogin:userLogin,password:pwd1},
                success: function (d) {

                    if(d.ret){
                        toastr.success("密码修改成功!");
                        setTimeout(back ,1000);

                    } else {
                        toastr.error("System Error");
                    }
                }
            });
        }
    }
     function back() {
         window.location.href="/login"
     }

    function checkPass(pass){
        if(pass.length <=6&&pass.length>=16){
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
</script>
</html>
