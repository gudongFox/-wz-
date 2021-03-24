<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="zh-cmn-Hans">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>
    <title>${name} | 协同设计</title>
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
    <link href="/m/global/plugins/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
    <link href="/m/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker.min.css" rel="stylesheet"/>

    <link href="/m/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet"/>

    <link href="/m/global/plugins/jquery-file-upload/css/jquery.fileupload.css" rel="stylesheet"/>
    <link href="/m/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet"/>
    <link href="/m/jstree/themes/default/style.min.css" rel="stylesheet"/>

    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN THEME STYLES -->
    <link href="/m/global/css/components-wuzhou.css" id="style_components" rel="stylesheet" type="text/css"/>
    <link href="/m/global/css/plugins.css" rel="stylesheet" type="text/css"/>
    <link href="/m/admin/layout/css/layout_wuzhou.css" rel="stylesheet" type="text/css"/>
    <link id="style_color" href="/m/admin/layout/css/themes/light-wuzhou.css" rel="stylesheet" type="text/css"/>
    <link href="/m/admin/layout/css/custom.css" rel="stylesheet" type="text/css"/>

    <style>
        .control-label.required::after {
            content: '* ';
            color: red;
            margin-left: 2px;
        }
    </style>

    <!-- END THEME STYLES -->
    <link rel="shortcut icon" href="/m/${code}.ico"/>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-header-fixed-mobile" and "page-footer-fixed-mobile" class to body element to force fixed header or footer in mobile devices -->
<!-- DOC: Apply "page-sidebar-closed" class to the body and "page-sidebar-menu-closed" class to the sidebar menu element to hide the sidebar by default -->
<!-- DOC: Apply "page-sidebar-hide" class to the body to make the sidebar completely hidden on toggle -->
<!-- DOC: Apply "page-sidebar-closed-hide-logo" class to the body element to make the logo hidden on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-hide" class to body element to completely hide the sidebar on sidebar toggle -->
<!-- DOC: Apply "page-sidebar-fixed" class to have fixed sidebar -->
<!-- DOC: Apply "page-footer-fixed" class to the body element to have fixed footer -->
<!-- DOC: Apply "page-sidebar-reversed" class to put the sidebar on the right side -->
<!-- DOC: Apply "page-full-width" class to the body element to have full width page without the sidebar menu -->
<body class="page-header-fixed page-quick-sidebar-over-content page-sidebar-closed-hide-logo page-container-bg-solid" style="background-color:#f7f8f9">



<!-- BEGIN HEADER -->
<div class="page-header navbar navbar-fixed-top">
    <!-- BEGIN HEADER INNER -->
    <div class="page-header-inner">
        <!-- BEGIN LOGO -->
        <div class="page-logo " >

            <a ui-sref="five.home">
                <img src="/m/wuzhou_logo_light.png" alt="logo" style="width: 280px;margin-top: -1px;" class="logo-default"/>
            </a>
            <%--            <div class="menu-toggler sidebar-toggler">--%>
            <%--            </div>--%>
        </div>

        <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse"
           data-target=".navbar-collapse">
        </a>
        <!-- END RESPONSIVE MENU TOGGLER -->
        <!-- BEGIN TOP NAVIGATION MENU -->
        <div class="top-menu">
            <ul class="nav navbar-nav pull-right">
                <li class="dropdown dropdown-user">
                    <a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown"
                       data-close-others="true">
                        <img alt="" class="img-circle" ng-src="{{'/sys/attach/download/'+user.headAttachId}}"  onerror="this.src='/m/admin/layout/img/avatar9.jpg'"/>
                        <span class="username username-hide-on-mobile" id="span_userName"></span>
                        <i class="fa fa-angle-down"></i>
                    </a>

                </li>
            </ul>
        </div>
        <!-- END TOP NAVIGATION MENU -->
    </div>
    <!-- END HEADER INNER -->
</div>
<!-- END HEADER -->

<!-- BEGIN CONTAINER -->
<div class="page-container">

    <!-- END SIDEBAR -->
    <!-- BEGIN CONTENT -->
    <div class="page-content-wrapper justify-content-center">
        <div  style="width: 90%;margin: 0 auto; margin-top: 30px;" >
            <div ui-view></div>
        </div>
    </div>
    <!-- END CONTENT -->
    <!-- BEGIN QUICK SIDEBAR -->
    <!-- END QUICK SIDEBAR -->
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
<!--

-->
<span ng-controller="FootController">
    <span ng-controller="CommonBaseController"></span>
</span>


<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->


<script charset="UTF-8" src="/assets/js/city.js"></script>
<script charset="UTF-8" src="/assets/js/bank.js"></script>

<!--[if lt IE 9]>
<script charset="UTF-8" src="/m/global/plugins/respond.min.js"></script>
<script charset="UTF-8" src="/m/global/plugins/excanvas.min.js"></script>
<![endif]-->
<script charset="UTF-8" src="/m/global/plugins/jquery.min.js"></script>
<script charset="UTF-8" src="/m/global/plugins/jquery-migrate.min.js"></script>
<%--cmcu通用过滤--%>
<script charset="UTF-8" src="/assets/js/table.filter.js"></script>

<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->

<%--<script charset="utf-8" src="/assets/kindeditor/kindeditor-all.js" ></script>--%>

<script charset="utf-8" src="/assets/js/city.js"></script>
<script charset="utf-8" src="/assets/js/bank.js"></script>

<!--[if lt IE 9]>
<script charset="utf-8" src="/m/global/plugins/respond.min.js"></script>
<script charset="utf-8" src="/m/global/plugins/excanvas.min.js"></script>
<![endif]-->
<script charset="utf-8" src="/m/global/plugins/jquery.min.js"></script>
<script charset="utf-8" src="/m/global/plugins/jquery-migrate.min.js"></script>
<%--cmcu通用过滤--%>
<script charset="utf-8" src="/assets/js/table.filter.js"></script>

<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->

<%--<script charset="utf-8" src="/assets/kindeditor/kindeditor-all.js" ></script>--%>

<script charset="utf-8" src="/m/global/plugins/jquery-ui/jquery-ui.min.js"></script>
<script charset="utf-8" src="/m/global/plugins/bootstrap/js/bootstrap.min.js"></script>
<%--<script src="https://cdn.bootcdn.net/ajax/libs/bootstrap-table/1.16.0/bootstrap-table.js"></script>--%>
<%--<script charset="utf-8" src="/m/global/plugins/bootstrap-table-master/src/bootstrap-table.js"></script>--%>

<script charset="utf-8" src="/m/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js"></script>
<script charset="utf-8" src="/m/global/plugins/bootstrap-select/bootstrap-select.min.js"></script>
<script charset="utf-8" src="/m/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js"></script>
<script charset="utf-8" src="/m/global/plugins/jquery.blockui.min.js"></script>
<script charset="utf-8" src="/m/global/plugins/uniform/jquery.uniform.min.js"></script>
<script charset="utf-8" src="/m/global/plugins/bootstrap-toastr/toastr.min.js"></script>
<script charset="utf-8" src="/m/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.min.js"></script>
<script charset="utf-8" src="/m/global/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"></script>

<!-- 输入框联想补全 -->
<link href="/m/global/plugins/jQuery-Autocomplete-master/content/styles.css" rel="stylesheet"/>
<script type="text/javascript" src="/m/global/plugins/jQuery-Autocomplete-master/scripts/jquery.mockjax.js"></script>
<script type="text/javascript" src="/m/global/plugins/jQuery-Autocomplete-master/src/jquery.autocomplete.js"></script>
<script type="text/javascript" src="/m/global/plugins/jQuery-Autocomplete-master/scripts/countries.js"></script>
<script type="text/javascript" src="/m/global/plugins/jQuery-Autocomplete-master/scripts/demo.js"></script>
<!-- 汉字转拼音 -->
<script type="text/javascript" src="/assets/js/Convert_Pinyin.js"></script>
<!-- 加水印 -->
<script type="text/javascript" src="/assets/js/canvaswm.js"></script>

<!--fancybox图片预览 -->
<script type="text/javascript" src="/assets/fancybox/source/jquery.fancybox.js"></script>
<script type="text/javascript" src="/assets/fancybox/source/jquery.fancybox.pack.js"></script>
<script type="text/javascript" src="/assets/fancybox/source/helpers/jquery.fancybox-buttons.js?v=1.0.5"></script>

<script charset="utf-8" src="/m/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script charset="utf-8"
        src="/m/global/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>

<script charset="utf-8" src="/assets/js/jquery-validation-1.19.0/dist/jquery.validate.js"></script>
<%-- <script charset="utf-8" src="/assets/js/jquery-validation-1.19.0/dist/additional-methods.min.js"></script>--%>
<script charset="utf-8" src="/assets/js/jquery-validation-1.19.0/dist/localization/messages_zh.js"></script>
<script charset="utf-8" src="/assets/js/bootbox.js"></script>

<script charset="utf-8" src="/m/global/plugins/jquery-file-upload/js/vendor/jquery.ui.widget.js"></script>
<script charset="utf-8" src="/m/global/plugins/jquery-file-upload/js/jquery.iframe-transport.js"></script>
<script charset="utf-8" src="/m/global/plugins/jquery-file-upload/js/jquery.fileupload.js"></script>
<script charset="utf-8" src="/m/global/plugins/jquery-file-upload/js/jquery.fileupload-process.js"></script>
<script charset="utf-8" src="/m/global/plugins/jquery-file-upload/js/jquery.fileupload-validate.js"></script>
<script charset="utf-8" src="/assets/js/vakata-jstree-3.3.8/dist/jstree.min.js"/>
<%--树形表格--%>
<script type="text/javascript" src="/assets/bootstrap-treetable/libs/v4/popper.min.js"></script>
<%--<script type="text/javascript" src="/assets/bootstrap-treetable/libs/v4/bootstrap.min.js"></script>--%>
<script type="text/javascript" src="/assets/bootstrap-treetable/bootstrap-treetable.js"></script>


<!-- END CORE PLUGINS -->
<script charset="utf-8" src="/m/global/scripts/metronic.js"></script>
<script charset="utf-8" src="/m/admin/layout/scripts/layout.js"></script>
<script>
    //兼容IE9
    window.console = window.console || (function () {
        var c = {};
        c.log = c.warn = c.debug = c.info = c.error = c.time = c.dir = c.profile = c.clear = c.exception = c.trace = c.assert = function () {
        };
        return c;
    })();

    var refreshTimer = undefined;
    var lodop;
    var contentHeight = 1000;
    jQuery(document).ready(function () {
        Metronic.init();
        Layout.init();
        setTimeout(function () {
            $(".menuitem").click(function () {
                var currentName = $(this).attr("ui-sref");
                resetNgMenuState(currentName);
            });
            $(".sys-menu").click(function () {
                var $m = $(this);
                setTimeout(function () {
                    $m.next('ul').find('a').click();
                }, 0)

            });

            //横向栏点击
            $('.hor-menu .navbar-nav li > a').click(function(e){
                var current = $(this).parents('.classic-menu-dropdown');
                $('.classic-menu-dropdown').each(function () {
                    $(this).removeClass("active");
                    if($(this)[0].innerText!=current[0].innerText)
                        $(this).removeClass("open");
                });
                $(this).parents('.classic-menu-dropdown').addClass("active");
                if($(this).attr('data-stopPropagation')=='true') {
                    e.stopPropagation();
                }
            });

            var ua = navigator.userAgent;
            if(ua.toLowerCase().indexOf('pad')>=0) {
                $(".sidebar-toggler").click();
            }

        }, 2500);

        // var ua = navigator.userAgent;
        // if(ua.toLowerCase().indexOf('pad')>=0) {
        //     $(".sidebar-toggler").click();
        // }
        /*图片附件查看 fancybox初始化方法*/
        $(".fancybox").fancybox({
            'type':'image',
            autoScale:false,
            closeClick:true,
            hideOnOverlayClic:false,
            minWidth:600,
            //maxWidth:400,
            minHeight:400,
            //maxHeight:300,

        });
    });

    function resetNgMenuState(currentName) {
        if (currentName) {
            $(".page-sidebar-menu li").each(function () {
                $(this).removeClass("active");
                $(this).removeClass("open");
                //$(this).children("a").children(".arrow").remove();
                $(this).children("a").children(".selected").remove();
            });

            var moduleId = "";
            $(".menuitem").each(function () {
                var name = $(this).attr("ui-sref");
                if (name == currentName) {
                    $(this).parent().addClass("active");
                    if ($(this).parent().parent().parent().attr("data-sub") == "1") {
                        moduleId = $(this).parent().parent().parent().parent().parent().attr("data-id");
                        if ($(this).parent().parent().parent().parent().parent().children("a").children(".arrow").length == 0) {
                            //$("<span class='arrow'></span><span class ='selected'><?span>").insertAfter($(this).parent().parent().parent().parent().parent().children("a").children("a .title"))
                            $(this).append($("<span class ='selected'></span>"));
                            $(this).parent().parent().parent().parent().parent().addClass("active open");
                            $(this).parent().parent().parent().addClass("open");
                        }
                    } else {
                        moduleId = $(this).parent().parent().parent().attr("data-id");
                        if ($(this).parent().parent().parent().children("a").children(".arrow").length == 0) {
                            if ($(this).parents('.menuitem-wrapper').find('.sys-menu').length > 0) {
                                $("<span class ='selected'></span>").insertAfter($(this).parent().parent().parent().children("a").children("a .title"));
                            } else {
                                $(this).append($("<span class ='selected'></span>"));
                            }
                            $(this).parent().parent().parent().addClass("active open");
                        }
                    }
                } else {
                    $(this).parent().removeClass("active");
                }
            });


        }
    }



</script>
<script charset="utf-8" src="/m/lodop/LodopFuncs.js"></script>
<script charset="utf-8" src="/assets/js/angular.js"></script>
<script charset="utf-8" src="/assets/js/angular-ui-router.min.js"></script>
<script charset="utf-8" src="/assets/js/stateEvents.js"></script>
<script charset="utf-8" src="/assets/js/my-directives.js"></script>
<script charset="utf-8" src="/assets/js/cmcu-common.js"></script>
<script charset="utf-8" src="/assets/js/moment.js"></script>
<%--套红--%>
<script charset="utf-8" src="/assets/js/cmcu-upload.js"></script>
<%--日程控件--%>
<script charset="utf-8" src="/m/global/plugins/fullcalendar/fullcalendar.min.js"></script>
<script charset="utf-8" src="/m/global/plugins/fullcalendar/lang/zh-cn.js"></script>


<script charset="utf-8" src="/editor-app/custom/js/services.act.js?version=${version}"></script>
<script charset="utf-8" src="/editor-app/custom/js/basic.act.js?version=${version}"></script>
<script charset="utf-8" src="/editor-app/custom/js/controllers.act.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.hr1.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.hr2.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.sys.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.ed.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.business.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.five.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.five.home.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.five.supervise.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.five.finance.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.five.budget.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.five1.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.five.oa.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.five.hr.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.five.asset.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.oa1.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.oa2.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/route.common.js?version=${version}"></script>

<script charset="utf-8" src="/assets/page/fiveOaRoute.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/services.common.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.common.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.web.js?version=${version}"></script>

<script charset="utf-8" src="/assets/page/controllers.business.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.hr1.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.hr2.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.sys.js?version=${version}"></script>
<script lang="javascript"  charset="utf-8" src="/assets/page/controllers.mcc.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.ed.js?version=${version}"></script>

<script charset="utf-8" src="/assets/page/controllers.ed.input.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.five.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.five1.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.five.business.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.five.hr.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.five.asset.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.me.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.oa2.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.five.oa.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.oa.notice.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.five.home.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.five.supervise.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.five.finance.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.five.budget.js?version=${version}"></script><%--跳转路由及设置--%>
<script charset="utf-8" src="/assets/page/controllers.five.technology.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/customRoute.js?version=${version}"></script>
<%--跳转路由及设置--%>
<script ang="javascript"   charset="utf-8" src="/assets/page/wuzhou.plot.js?version=${version}"></script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
