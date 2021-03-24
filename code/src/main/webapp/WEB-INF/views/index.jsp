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
    <link rel="stylesheet" type="text/css" href="/m/global/plugins/bootstrap-select/bootstrap-select.min.css"/>
    <link href="/m/global/plugins/jquery-file-upload/css/jquery.fileupload.css" rel="stylesheet"/>
    <link href="/m/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet"/>

    <%--日程控件--%>
    <link href="/m/global/plugins/bootstrap-daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css"/>
    <link href="/m/global/plugins/fullcalendar/fullcalendar.min.css" rel="stylesheet" type="text/css"/>
    <link href="/m/global/plugins/jqvmap/jqvmap/jqvmap.css" rel="stylesheet" type="text/css"/>
    <%-- 树形表格--%>
    <%--    <link rel="stylesheet" href="/assets/bootstrap-treetable/libs/v4/bootstrap.min.css" type="text/css" />--%>
    <link rel="stylesheet" href="/assets/bootstrap-treetable/bootstrap-treetable.css" type="text/css"/>

    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN THEME STYLES -->
    <link href="/m/global/css/components-wuzhou.css" id="style_components" rel="stylesheet" type="text/css"/>
    <link href="/m/global/css/plugins.css" rel="stylesheet" type="text/css"/>
    <link href="/m/admin/layout/css/layout_wuzhou.css" rel="stylesheet" type="text/css"/>
    <link id="style_color" href="/m/admin/layout/css/themes/light-wuzhou.css" rel="stylesheet" type="text/css"/>
    <link href="/m/admin/layout/css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="/assets/jstree-3.3.8/dist/themes/default/style.min.css" rel="stylesheet"/>

    <%--fancybox图片预览控件--%>
    <link href="/assets/fancybox/source/jquery.fancybox.css" rel="stylesheet"  media="screen" type="text/css" >
    <link rel="stylesheet" type="text/css" href="/assets/fancybox/source/helpers/jquery.fancybox-buttons.css?v=1.0.5" />


    <style>
        .control-label.required::after {
            content: '* ';
            color: red;
            margin-left: 2px;
        }

        .menuitem {
            position: relative;
        }

        .menuitem .selected {
            display: block;
            background-image: none;
            float: right;
            position: absolute;
            right: 0px;
            top: 8px;
            background: none;
            width: 0;
            height: 0;
            border-top: 12px solid transparent;
            border-bottom: 12px solid transparent;
            border-right: 12px solid #ffffff;
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
<body class="page-header-fixed page-quick-sidebar-over-content page-sidebar-fixed page-sidebar-closed-hide-logo page-container-bg-solid">
<!-- BEGIN HEADER -->
<div class="page-header navbar navbar-fixed-top">
    <!-- BEGIN HEADER INNER -->
    <div class="page-header-inner">
        <!-- BEGIN LOGO -->
        <div class="page-logo">
            <a ui-sref="five.home">
                <img src="/m/wuzhou_logo_light.png" alt="logo" style="height: 38px;margin-top: 5px;"
                     class="logo-default"/>
            </a>

            <div class="menu-toggler sidebar-toggler">
            </div>
        </div>
        <!-- END LOGO -->
        <!-- BEGIN HORIZANTAL MENU -->
        <!-- DOC: Remove "hor-menu-light" class to have a horizontal menu with theme background instead of white background -->
        <!-- DOC: This is desktop version of the horizontal menu. The mobile version is defined(duplicated) in the responsive menu below along with sidebar menu. So the horizontal menu has 2 seperate versions -->
        <div class="hor-menu hor-menu-light">
            <ul class="nav navbar-nav">
                <li ng-repeat="menu in menus | filter:{left:true,parentId:1,top:true}:true"
                    class="classic-menu-dropdown {{$index==0?'active':''}}" ng-init="parentIndex=$index" data-sub="0"
                    data-id="{{menu.id}}">
                    <a data-toggle="dropdown" href="javascript:;" ng-if="!menu.ishome && !menu.istask">
                        <i class="{{menu.icon}}"></i>
                        <span class="title" ng-bind="menu.name"></span>
                        <span class="open" ng-if="$index==0"></span>
                        <span class="selected" ng-if="$index==0"></span>
                    </a>
                    <a href="javascript:;" class="sys-menu" ng-if="menu.ishome || menu.istask">

                        <span class="title" ng-bind="menu.name"></span>
                        <span class="open" ng-if="$index==0"></span>
                        <span class="selected" ng-if="$index==0"></span>
                    </a>
                    <ul class="dropdown-menu pull-left" ng-if="!menu.ishome && !menu.istask">
                        <li data-stopPropagation="true" class="dropdown-submenu" ng-repeat="subMenu in menus |filter:{left:true,parentId:menu.id}:true" data-sub="1">
                            <%-- 流程审批用页面菜单--%>
                            <a data-stopPropagation="true" ui-sref="oa.functionEntrance({moduleId:{{subMenu.id}}})"
                               ng-if="subMenu.isextend">
                                <span style="margin-left: 10px" ng-bind="subMenu.name"></span>
                            </a>
                            <a data-stopPropagation="true" href="javascript:;" ng-if="!subMenu.isextend">
                                <i class="{{menu.icon}}"></i>
                                <span style="margin-left:10px" class="title" ng-bind="subMenu.name"></span>
                                <span class="arrow"></span>
                            </a>

                            <%--菜单重构 五洲要求 流程审批用页面菜单 注释代码为原菜单
                             <a href="javascript:;">
                                 <span class="title" ng-bind="subMenu.name"></span>
                                 <span class="arrow"></span>
                             </a>--%>
                            <ul class="dropdown-menu" ng-if="!subMenu.isextend">
                                <li ng-repeat="acl in subMenu.aclList">
                                    <a ui-sref="{{acl.uiSref}}" ng-if="acl.uiSref.length>0">
                                        <span style="margin-left:10px" ng-bind="acl.name"></span>
                                    </a>
                                    <a href="{{acl.url}}" ng-if="acl.uiSref.length==0">
                                        <span style="margin-left:10px" ng-bind="acl.name"></span>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li ng-repeat="acl in menu.aclList">
                            <a ui-sref="{{acl.uiSref}}" ng-if="acl.uiSref.length>0">
                                <span style="margin-left:10px" ng-bind="acl.name"></span>
                            </a>
                            <a href="{{acl.url}}" ng-if="acl.uiSref.length==0">
                                <span style="margin-left:10px" ng-bind="acl.name"></span>
                            </a>
                        </li>
                    </ul>
                    <ul class="classic-menu-dropdown" style="margin: 0" ng-if="menu.ishome || menu.istask ">
                        <li class="classic-menu-dropdown" ng-repeat="subMenu in menus |filter:{left:true,parentId:menu.id}:true" data-sub="1">
                            <%-- 流程审批用页面菜单--%>
                            <a ui-sref="{{subMenu.code}}" ng-if="subMenu.isextend">
                                <span ng-bind="subMenu.name"></span>
                            </a>
                            <a href="javascript:;" ng-if="!subMenu.isextend">
                                <i class="{{subMenu.icon}}"></i>
                                <span class="title" ng-bind="subMenu.name"></span>
                                <span class="arrow"></span>
                            </a>

                            <%--菜单重构 五洲要求 流程审批用页面菜单 注释代码为原菜单
                             <a href="javascript:;">
                                 <span class="title" ng-bind="subMenu.name"></span>
                                 <span class="arrow"></span>
                             </a>--%>
                            <ul class="dropdown-menu pull-left">
                                <li ng-repeat="acl in subMenu.aclList" class="{{(parentIndex+$index)==0?'active':''}}">
                                    <a ui-sref="{{acl.uiSref}}" ng-if="acl.uiSref.length>0">
                                        <span ng-bind="acl.name"></span>
                                    </a>
                                    <a href="{{acl.url}}" ng-if="acl.uiSref.length==0">
                                        <span ng-bind="acl.name"></span>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li ng-repeat="acl in menu.aclList" style="position: absolute; left:-9999px">
                            <a ui-sref="{{acl.uiSref}}" ng-if="acl.uiSref.length>0">
                                <span ng-bind="acl.name"></span>
                            </a>
                            <a href="{{acl.url}}" ng-if="acl.uiSref.length==0">
                                <span ng-bind="acl.name"></span>
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <!-- END HORIZANTAL MENU -->
        <!-- BEGIN HEADER SEARCH BOX -->
        <!-- DOC: Apply "search-form-expanded" right after the "search-form" class to have half expanded search box -->

        <!-- END HEADER SEARCH BOX -->
        <!-- BEGIN RESPONSIVE MENU TOGGLER -->
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
                        <img alt="" class="img-circle"
                             ng-src="{{user.avatar}}"
                             onerror="this.src='/assets/img/avatar.jpg'">
                        <span class="username username-hide-on-mobile" id="span_userName"></span>
                        <i class="fa fa-angle-down"></i>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-default">
                        <li>
                            <a  ng-click="showResetInformation();">
                                <i class="icon-user"></i> 个人信息 </a>
                        </li>
                        <li>
                            <a ui-sref="sys.schedule">
                                <i class="icon-calendar font-green-sharp"></i> 我的日程 </a>
                        </li>
                        <li>
                            <a ng-click="forceH5Mode();">
                                <i class="icon-screen-tablet"></i> 手机模式 </a>
                        </li>
                        <li>
                            <a href="/out">
                                <i class="icon-key"></i> 安全退出 </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <!-- END TOP NAVIGATION MENU -->
    </div>
    <!-- END HEADER INNER -->
</div>
<!-- END HEADER -->
<div class="clearfix">
</div>
<!-- BEGIN CONTAINER -->
<div class="page-container">
    <!-- BEGIN SIDEBAR -->
    <div class="page-sidebar-wrapper">
        <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
        <!-- DOC: Change data-auto-speed="200" to adjust the sub menu slide up/down speed -->
        <div class="page-sidebar navbar-collapse collapse" style="padding-top: 30px">
            <!-- BEGIN SIDEBAR MENU -->
            <!-- DOC: Apply "page-sidebar-menu-light" class right after "page-sidebar-menu" to enable light sidebar menu style(without borders) -->
            <!-- DOC: Apply "page-sidebar-menu-hover-submenu" class right after "page-sidebar-menu" to enable hoverable(hover vs accordion) sub menu mode -->
            <!-- DOC: Apply "page-sidebar-menu-closed" class right after "page-sidebar-menu" to collapse("page-sidebar-closed" class must be applied to the body element) the sidebar sub menu mode -->
            <!-- DOC: Set data-auto-scroll="false" to disable the sidebar from auto scrolling/focusing -->
            <!-- DOC: Set data-keep-expand="true" to keep the submenues expanded -->
            <!-- DOC: Set data-auto-speed="200" to adjust the sub menu slide up/down speed -->
            <ul class="page-sidebar-menu" data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">
                <!-- DOC: To remove the sidebar toggler from the sidebar you just need to completely remove the below "sidebar-toggler-wrapper" LI element -->
                <!-- DOC: To remove the search box from the sidebar you just need to completely remove the below "sidebar-search-wrapper" LI element -->
                <!-- 暂时不要搜索 by liuting
                <li class="sidebar-search-wrapper">
                    <form class="sidebar-search " action="/index" method="POST">
                        <a href="javascript:;" class="remove">
                            <i class="icon-close"></i>
                        </a>
                        <div class="input-group">
                            <input type="text" class="form-control" placeholder="搜索...">
                            <span class="input-group-btn">
							<a href="javascript:;" class="btn submit"><i class="icon-magnifier"></i></a>
							</span>
                        </div>
                    </form>
                </li>
                -->

                <li ng-repeat="menu in menus | filter:{left:true,parentId:1}:true"
                    class="menuitem-wrapper {{$index==0?'active open':''}}" ng-init="parentIndex=$index" data-sub="0"
                    data-id="{{menu.id}}">
                    <a href="javascript:;" ng-if="!menu.ishome && !menu.istask">
                        <i class="{{menu.icon}}"></i>
                        <span class="title" ng-bind="menu.name"></span>
                        <span class="open" ng-if="$index==0"></span>
                        <span class="selected" ng-if="$index==0"></span>
                    </a>
                    <a href="javascript:;" class="sys-menu" ng-if="menu.ishome || menu.istask">
                        <i class="{{menu.icon}}"></i>
                        <span class="title" ng-bind="menu.name"></span>
                        <span class="open" ng-if="$index==0"></span>
                        <span class="selected" ng-if="$index==0"></span>
                    </a>
                    <ul class="sub-menu" ng-if="!menu.ishome && !menu.istask">
                        <li ng-repeat="subMenu in menus |filter:{left:true,parentId:menu.id}:true" data-sub="1">
                            <%-- 流程审批用页面菜单--%>
                            <a class="menuitem" ui-sref="oa.functionEntrance({moduleId:{{subMenu.id}}})"
                               ng-if="subMenu.isextend">
                                <i class="{{subMenu.icon}}" ng-if="subMenu.icon!=''"></i>
                                <span style="margin-left: 10px" ng-bind="subMenu.name"></span>
                            </a>
                            <a href="javascript:;" ng-if="!subMenu.isextend">
                                <i class="{{subMenu.icon}}"></i>
                                <span style="margin-left:10px" class="title" ng-bind="subMenu.name"></span>
                                <span class="arrow"></span>
                            </a>


                            <%--菜单重构 五洲要求 流程审批用页面菜单 注释代码为原菜单
                             <a href="javascript:;">
                                 <span class="title" ng-bind="subMenu.name"></span>
                                 <span class="arrow"></span>
                             </a>--%>
                            <ul class="sub-menu" ng-if="!subMenu.isextend">
                              <%--21.1.11 xxin 三层菜单--%>
                                <li ng-repeat="subMenu1 in menus |filter:{left:true,parentId:menu.id,parentId:subMenu.id}:true" data-sub="1">
                                        <%-- 流程审批用页面菜单--%>
                                        <a class="menuitem" ui-sref="oa.functionEntrance({moduleId:{{subMenu1.id}}})"
                                           ng-if="subMenu1.isextend">
                                            <i class="{{subMenu1.icon}}" ng-if="subMenu1.icon!=''"></i>
                                            <span style="margin-left: 10px" ng-bind="subMenu1.name"></span>
                                        </a>
                                        <a href="javascript:;" ng-if="!subMenu1.isextend">
                                            <i class="{{subMenu1.icon}}"></i>
                                            <span style="margin-left:10px" class="title" ng-bind="subMenu1.name"></span>
                                            <span class="arrow"></span>
                                        </a>

                                        <%--菜单重构 五洲要求 流程审批用页面菜单 注释代码为原菜单
                                         <a href="javascript:;">
                                             <span class="title" ng-bind="subMenu.name"></span>
                                             <span class="arrow"></span>
                                         </a>--%>
                                        <ul class="sub-menu" ng-if="!subMenu1.isextend">
                                            <li ng-repeat="acl in subMenu1.aclList" class="{{(parentIndex+$index)==0?'active':''}}">
                                                <a class="menuitem" ui-sref="{{acl.uiSref}}" ng-if="acl.uiSref.length>0">
                                                    <i class="{{acl.icon}}" ng-if="acl.icon!=''"></i><i class="fa fa-star" ng-if="acl.icon==''"></i>
                                                    <span style="margin-left:10px" ng-bind="acl.name"></span>
                                                </a>
                                                <a class="menuitem" href="{{acl.url}}" ng-if="acl.uiSref.length==0">
                                                    <i class="{{acl.icon}}" ng-if="acl.icon!=''"></i><i class="fa fa-star" ng-if="acl.icon==''"></i>
                                                    <span style="margin-left:10px" ng-bind="acl.name"></span>
                                                </a>
                                            </li>
                                        </ul>
                                    </li>

                                <li ng-repeat="acl in subMenu.aclList" class="{{(parentIndex+$index)==0?'active':''}}">
                                    <a class="menuitem" ui-sref="{{acl.uiSref}}" ng-if="acl.uiSref.length>0">
                                        <i class="{{acl.icon}}" ng-if="acl.icon!=''"></i><i class="fa fa-star" ng-if="acl.icon==''"></i>
                                        <span style="margin-left:10px" ng-bind="acl.name"></span>
                                    </a>
                                    <a class="menuitem" href="{{acl.url}}" ng-if="acl.uiSref.length==0">
                                        <i class="{{acl.icon}}" ng-if="acl.icon!=''"></i><i class="fa fa-star" ng-if="acl.icon==''"></i>
                                        <span style="margin-left:10px" ng-bind="acl.name"></span>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li ng-repeat="acl in menu.aclList" class="{{(parentIndex+$index)==0?'active':''}}">
                            <a class="menuitem" ui-sref="{{acl.uiSref}}" ng-if="acl.uiSref.length>0">
                                <i class="{{acl.icon}}" ng-if="acl.icon!=''"></i><i class="fa fa-star" ng-if="acl.icon==''"></i><span style="margin-left:10px" ng-bind="acl.name"></span>
                            </a>
                            <a class="menuitem" href="{{acl.url}}" ng-if="acl.uiSref.length==0">
                                <i class="{{acl.icon}}" ng-if="acl.icon!=''"></i><i class="fa fa-star" ng-if="acl.icon==''"></i><span style="margin-left:10px" ng-bind="acl.name"></span>
                            </a>
                        </li>
                    </ul>
                    <ul class="sub-menu" style="margin: 0" ng-if="menu.ishome || menu.istask ">
                        <li ng-repeat="subMenu in menus |filter:{left:true,parentId:menu.id}:true" data-sub="1">
                            <%-- 流程审批用页面菜单--%>
                            <a style="color:#848484" ui-sref="{{subMenu.code}}" ng-if="subMenu.isextend">
                                <span ng-bind="subMenu.name"></span>
                            </a>
                            <a style="color:#848484" href="javascript:;" ng-if="!subMenu.isextend">
                                <i class="{{subMenu.icon}}"></i>
                                <span class="title" ng-bind="subMenu.name"></span>
                                <span class="arrow"></span>
                            </a>

                            <%--菜单重构 五洲要求 流程审批用页面菜单 注释代码为原菜单
                             <a href="javascript:;">
                                 <span class="title" ng-bind="subMenu.name"></span>
                                 <span class="arrow"></span>
                             </a>--%>
                            <ul class="sub-menu">
                                <li ng-repeat="acl in subMenu.aclList" class="{{(parentIndex+$index)==0?'active':''}}">
                                    <a class="menuitem" ui-sref="{{acl.uiSref}}" ng-if="acl.uiSref.length>0">
                                        <span ng-bind="acl.name"></span>
                                    </a>
                                    <a class="menuitem" href="{{acl.url}}" ng-if="acl.uiSref.length==0">
                                        <span ng-bind="acl.name"></span>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li ng-repeat="acl in menu.aclList" style="position: absolute; left:9999px">
                            <a class="menuitem" ui-sref="{{acl.uiSref}}" ng-if="acl.uiSref.length>0">
                                <span ng-bind="acl.name"></span>
                            </a>
                            <a class="menuitem" href="{{acl.url}}" ng-if="acl.uiSref.length==0">
                                <span ng-bind="acl.name"></span>
                            </a>
                        </li>
                    </ul>
                </li>


            </ul>
            <!-- END SIDEBAR MENU -->
        </div>
    </div>
    <!-- END SIDEBAR -->
    <!-- BEGIN CONTENT -->


    <div class="page-content-wrapper">
        <div class="page-content">
            <div class="alert alert-danger hidden" id="printInstallNotice">
                <strong>打印服务未正确安装!</strong> 请点击这里<a href='/m/lodop/CLodop_Setup_for_Win32NT.exe' target='_self'>下载安装</a>，安装后请刷新浏览器。
            </div>
            <jsp:include page="common/selectEmployeeModal.jsp"/>
            <div class="row">
                <div class="col-md-12">
                    <div class="full-height-content full-height-content-scrollable">
                        <div class="full-height-content-body" style="height: 100% !important;">
                            <div ui-view></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- END CONTENT -->
    <!-- BEGIN QUICK SIDEBAR -->
    <!-- END QUICK SIDEBAR -->
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
<span ng-controller="FootController">
    <span ng-controller="CommonBaseController"></span>
</span>

<!--
<div class="page-footer" ng-controller="FootController" style="text-align: center;background-color: #369">
    <div class="page-footer-inner text-center">
        2020 &copy; 综合业务管理平台
        <%--${version}--%>
    </div>
    <div class="scroll-to-top" ng-controller="CommonBaseController">
        <i class="icon-arrow-up"></i>
    </div>
</div>
-->

<div class="modal fade" id="personalInformationModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">个人信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" autocomplete="off">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label required">新的密码</label>
                            <div class="col-md-9">
                                <input type="password" class="form-control" ng-model="pwd1" placeholder="请输入新的密码!" autocomplete="off" disableautocomplete>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">确认密码</label>
                            <div class="col-md-9">
                                <input type="password" class="form-control" ng-model="pwd2" placeholder="请再次输入新的密码!" autocomplete="off" disableautocomplete>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label required">个人签名</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="user.signUrl"
                                           disabled="disabled"/>
                                    <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span class="btn blue fileinput-button btn-sm">
                                        <i class="fa fa-cloud-upload" style="height:15px "></i>
                                        <span>上传DWG</span>
                                        <input type="file" name="file" class="uploadUserAttach"
                                               data-url="/hr/employee/receiveSignDwg.json" accept=".dwg"/>
                                    </span>
                                    <a ng-href="{{'/common/attach/download/'+user.signAttachId}}" target="_blank">
                                       <span id="btnDownloadSign" class="btn green fileoutput-button btn-sm">
                                                         <i class="fa fa-cloud-download" title="下载"></i>
                                                    <span>下载</span>
                                     </span>
                                  </a>
                                     </span>
                                </div>
                                <span class="help-block" style="font-size :14px;color: red;">请使用
                                                 <a href="/assets/doc/std/sign.dwg"><strong
                                                         style="color: blue">签名模板</strong></a>在规定位置合理签名,保存为2007格式。<br/>DWG出图电子签名使用</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">签字图片</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="user.signPicUrl"
                                           disabled="disabled"/>
                                    <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUploadSignPic" class="btn blue fileinput-button btn-sm">
                            <i class="fa fa-cloud-upload" style="height:15px "></i>
                            <span>上传PNG</span>
                            <input type="file" name="file" class="uploadUserAttach"
                                   data-url="/hr/employee/receiveSignPic.json" accept="image/png"/>
                                   </span>
                                        <a ng-href="{{'/common/attach/download/'+user.signPicAttachId}}"
                                           target="_blank">
                                        <span id="btnDownloadSignPic" class="btn green fileoutput-button btn-sm">
                                     <i class="fa fa-cloud-download"
                                        title="下载"></i>
                                            <span>下载</span>
                                        </span>
                                            </a>
                                        </span>
                                </div>
                                <span class="help-block"
                                      style="font-size :14px;color: red;">请上传png图片签名。WEB电子签名打印使用</span>
                            </div>

                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">员工头像</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="user.avatar"
                                           disabled="disabled"/>
                                    <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUploadHead" class="btn blue fileinput-button btn-sm uploadFile">
                            <i class="fa fa-cloud-upload" style="height:15px "></i>
                            <span>上传图片</span>
                            <input type="file" name="file" class="uploadUserAttach" data-url="/hr/employee/receiveHead.json"
                                   accept="image/*"/>
                                   </span>
                                        <a ng-href="{{'/common/attach/download/'+user.headAttachId}}"
                                           target="_blank">
                                        <span class="btn green fileoutput-button btn-sm">
                                     <i class="fa fa-cloud-download"
                                        title="下载"></i>
                                            <span>下载</span>
                                        </span>
                                            </a>
                                        </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="updateUserInformation();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<%--<div class="modal" id="jsTreeSelectUserModal">--%>
<%--    <div class="modal-dialog">--%>
<%--        <div class="modal-content">--%>
<%--            <div class="modal-header">--%>
<%--                <button type="button" class="close" data-dismiss="modal" aria-label="Close"></button>--%>
<%--                <h4 class="modal-title" id="jsTreeUser_title">选择用户</h4>--%>
<%--            </div>--%>
<%--            <div class="modal-body">--%>
<%--                <div class="input-group">--%>
<%--                    <input type="text" class="form-control" placeholder="用户名称" id="jsTreeUser_key"/>--%>
<%--                    <span class="input-group-addon" id="jsTreeUser_search">--%>
<%--											<i class="fa fa-user"></i>--%>
<%--											</span>--%>
<%--                </div>--%>

<%--                <div id="jsTreeUser_tree" style="margin-top:10px;max-height: 350px;overflow-y: auto;"></div>--%>
<%--            </div>--%>
<%--            <div class="modal-footer">--%>
<%--                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>--%>
<%--                <button type="button" class="btn btn-primary" id="jsTreeUser_btn">保存</button>--%>
<%--            </div>--%>
<%--        </div><!-- /.modal-content -->--%>
<%--    </div><!-- /.modal -->--%>
<%--</div>--%>

<jsp:include page="common/edFileHistoryModal.jsp"></jsp:include>
<jsp:include page="common/selectEmployeeModal.jsp"></jsp:include>
<div ng-include="'/web/v1/modals.html'"></div>

<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
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
<script charset="utf-8" src="/assets/jstree-3.3.8/dist/jstree.min.js"/>
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
<script charset="utf-8" src="/assets/page/controllers.five.budget.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/controllers.five.technology.js?version=${version}"></script>
<script charset="utf-8" src="/assets/page/customRoute.js?version=${version}"></script>
<script ang="javascript"   charset="utf-8" src="/assets/page/mccApp.js?version=${version}"></script>

<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
