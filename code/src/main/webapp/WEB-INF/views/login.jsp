<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String ret = request.getParameter("ret");
    if (StringUtils.isNotBlank(ret)) {
        ret = URLEncoder.encode(ret);
    } else {
        ret = "";
    }
    String error = "";
    if (request.getAttribute("error") != null) {
        error = request.getAttribute("error").toString();
    }
    String enLogin = request.getParameter("enLogin");
    if (StringUtils.isEmpty(enLogin)) enLogin = "";

%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="zh-cmn-Hans">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>
    <title>${name} | 用户登录</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <link href="/m/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="/m/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
    <link href="/m/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <link href="/m/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN PAGE LEVEL STYLES -->
    <link href="/m/admin/pages/css/login-soft.css" rel="stylesheet" type="text/css"/>
    <!-- END PAGE LEVEL SCRIPTS -->
    <!-- BEGIN THEME STYLES -->
    <link href="/m/global/css/components.css" id="style_components" rel="stylesheet" type="text/css"/>
    <link href="/m/global/css/plugins.css" rel="stylesheet" type="text/css"/>
    <link href="/m/admin/layout/css/layout.css" rel="stylesheet" type="text/css"/>
    <link id="style_color" href="/m/admin/layout/css/themes/darkblue.css" rel="stylesheet" type="text/css"/>
    <link href="/m/admin/layout/css/custom.css" rel="stylesheet" type="text/css"/>
    <!-- END THEME STYLES -->
    <link rel="shortcut icon" href="/m/${code}.ico"/>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="login">
<!-- BEGIN LOGO -->
<div class="logo">
    <a href="/login">
        <img src="/m/${code}_logo.png" style="width: 300px;" alt=""/>
    </a>
</div>
<!-- END LOGO -->
<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
<div class="menu-toggler sidebar-toggler">
</div>
<!-- END SIDEBAR TOGGLER BUTTON -->
<!-- BEGIN LOGIN -->
<div class="content" style="padding-top: 20px;padding-bottom: 20px;">
    <!-- BEGIN LOGIN FORM -->
    <form class="login-form" action="/login?ret=<%=ret%>" method="post" autocomplete="off">
        <h3 class="form-title">系统登录</h3>
        <div class="alert alert-danger display-hide">
            <button class="close" data-close="alert"></button>
            <span>
			请输入您的用户名和密码. </span>
        </div>
        <div class="form-group" style="margin-bottom: 20px;">
            <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
            <label class="control-label visible-ie8 visible-ie9">用户名</label>
            <div class="input-icon">
                <i class="fa fa-user"></i>
                <input class="form-control placeholder-no-fix" autofocus type="text" autocomplete="off"
                       placeholder="登录名" name="enLogin" id="txt_login" value="<%=enLogin%>"/>
            </div>
        </div>
        <div class="form-group" style="margin-bottom: 20px;">
            <label class="control-label visible-ie8 visible-ie9">密码</label>
            <div class="input-icon">
                <i class="fa fa-lock"></i>
                <input class="form-control placeholder-no-fix" type="password"
                       autocomplete="off" data-max-length="50" placeholder="您的密码" name="password"/>
            </div>
        </div>
        <div class="form-actions">
            <%--<input type="checkbox" name="remember" />
            <label class="checkbox">
               记住我 </label>--%>
            <button type="submit" class="btn blue pull-right">
                登录 <i class="m-icon-swapright m-icon-white"></i>
            </button>
        </div>
        <%--<div class="login-options">
            <h4>设计管理 协同设计</h4>
        </div>--%>
        <div class="create-account">
            <p style="text-align: right;padding-right: 10px;">
              <%--  <a href="/nochange/${code}_协同设计.exe" style="color:blue;"  target="_blank">协同设计软件包</a>
                <a href="/nochange/${code}_操作说明.doc" style="color:blue;"  target="_blank">操作手册</a>
                <a href="/nochange/${code}_培训视频.mp4" style="color:blue;"  target="_blank">培训视频</a>--%>
            </p>
        </div>
    </form>
    <!-- END LOGIN FORM -->
</div>
<!-- END LOGIN -->
<!-- BEGIN COPYRIGHT -->
<div class="copyright">
    2019 &copy; ${name}  <a href="/nochange/chrome.exe" style="color:white;"  target="_blank">推荐浏览器</a>
</div>
<!-- END COPYRIGHT -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="/m/global/plugins/respond.min.js"></script>
<script src="/m/global/plugins/excanvas.min.js"></script>
<![endif]-->
<script src="/m/global/plugins/jquery.min.js"></script>
<script src="/m/global/plugins/jquery-migrate.min.js"></script>
<script src="/m/global/plugins/bootstrap/js/bootstrap.min.js"></script>
<script src="/m/global/plugins/jquery.blockui.min.js"></script>
<script src="/m/global/plugins/uniform/jquery.uniform.min.js"></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="/m/global/plugins/jquery-validation/js/jquery.validate.min.js"></script>
<script src="/m/global/plugins/jquery-validation/js/localization/messages_zh.js"></script>
<script src="/m/global/plugins/backstretch/jquery.backstretch.min.js"></script>
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="/m/global/scripts/metronic.js"></script>
<script src="/m/admin/layout/scripts/layout.js"></script>
<script src="/m/admin/pages/scripts/login-soft.js"></script>
<!-- END PAGE LEVEL SCRIPTS -->
<script>

    var error = '<%=error%>';
    jQuery(document).ready(function () {
        Metronic.init(); // init metronic core components
        Layout.init(); // init current layout
        Login.init();
        // init background slide images
        $.backstretch([
                "/m/admin/pages/media/bg/2.jpg",
                "/m/admin/pages/media/bg/1.jpg",
                "/m/admin/pages/media/bg/3.jpg",
                "/m/admin/pages/media/bg/4.jpg"
            ], {
                fade: 1000,
                duration: 8000
            }
        );

        if (error != '') {
            $('.alert-danger', $('.login-form')).show();
            $(".alert-danger span").html(error);
        }
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>