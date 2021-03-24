<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>
    <title>任务详情</title>
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
    <!-- BEGIN THEME STYLES -->
    <link href="/m/global/css/components.css" id="style_components" rel="stylesheet" type="text/css"/>
    <link href="/m/global/css/plugins.css" rel="stylesheet" type="text/css"/>
    <link href="/m/admin/layout/css/layout.css" rel="stylesheet" type="text/css"/>
    <link id="style_color" href="/m/admin/layout/css/themes/darkblue.css" rel="stylesheet" type="text/css"/>
    <link href="/m/admin/layout/css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="/m/jstree/themes/default/style.min.css" rel="stylesheet"/>
    <link href="/m/global/plugins/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
    <!-- END THEME STYLES -->
    <link rel="shortcut icon" href="/m/mcc.ico"/>
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
<body class="page-full-width" ng-app="taskApp" ng-controller="TaskShowController">

<div class="clearfix" ng-controller="CommonBaseController">
</div>
<!-- BEGIN CONTAINER -->
<div class="page-container">
    <!-- BEGIN CONTENT -->
    <div class="page-content-wrapper">
        <div class="page-content"  style="background-color: #F1F3FA !important;">
            <!-- BEGIN PAGE CONTENT-->
            <div class="portlet  box blue">
                <div class="portlet-title">
                    <div class="caption">
                        <i class="icon-clock"></i>  <span style="font-size: 18px" ng-bind="template.formDesc"></span>
                        <small ng-if="!processInstance.myRunningTaskName"
                               ng-bind="processInstance.currentTaskName"></small>
                        <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName"
                               style="color: #35e0e1;"></small>
                    </div>

                    <div class="actions" style="float:right">
                        <a href="javascript:;" class="btn btn-sm btn-default" ng-click="refresh()"
                           style="font-size: 14px;line-height: 1.6">
                            <i class="fa fa-refresh"></i> 刷新 </a>
                        <span ng-include="'/web/v1/tpl/actAction.html'" ng-controller="CommonActActionController" ></span>
                    </div>
                </div>
                <div class="portlet-body">
                    <div class="tabbable-line">
                        <ul class="nav nav-tabs ">
                            <li class="active">
                                <a href="#tab_basic" data-toggle="tab" aria-expanded="true">
                                    基础信息 </a>
                            </li>
                            <li class="">
                                <a href="#tab_act" data-toggle="tab" aria-expanded="false">
                                    流程信息
                                </a>
                            </li>
                            <li class="">
                                <a href="#tab_pic" data-toggle="tab" aria-expanded="false">
                                    流程图片 </a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="tab_basic"
                                 style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                                <div class="form-group row" style="margin-bottom: 0.5rem;">
                                    <div ng-repeat="item in group.items"
                                         ng-class="{true:'col-md-6',false:'col-md-12'}[item.commonFormDetail.inputSize==6]"
                                         style="margin-bottom: 0.5rem;" ng-if="!item.commonFormDetail.detailHidden">
                                        <label>
                                            <span ng-bind="item.commonFormDetail.inputText"></span> <span style="color: red;" ng-if="item.commonFormDetail.required">*</span>
                                        </label>

<%--                                        <input ng-if="item.commonFormDetail.inputType=='text'" type="text" class="form-control"--%>
<%--                                               placeholder="{{item.commonFormDetail.inputTip}}" ng-model="item.inputValue"--%>
<%--                                               ng-disabled="!item.commonFormDetail.editable"/>--%>
                                        <input type="text" class="form-control"
                                               placeholder="{{item.commonFormDetail.inputTip}}" ng-model="item.inputValue"
                                               ng-disabled="!item.commonFormDetail.editable"/>

                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane" id="tab_act"
                                 style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                                <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                            </div>
                            <div class="tab-pane" id="tab_pic"
                                 style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                                <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div ng-if="tplConfig.markRoleNames.length>0" ng-include="'/web/v1/tpl/commonEdMark.html'"  ng-init="markTplTitle='意见标注'"></div>
            <div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
            <!-- END PAGE CONTENT-->
        </div>
    </div>
    <!-- END CONTENT -->
</div>
<!-- END CONTAINER -->
<!-- BEGIN FOOTER -->
<!-- END FOOTER -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="/m/global/plugins/respond.min.js"></script>
<script src="/m/global/plugins/excanvas.min.js"></script>
<![endif]-->
<script src="/m/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="/m/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="/m/global/plugins/uniform/jquery.uniform.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<script src="/m/global/scripts/metronic.js" type="text/javascript"></script>
<script src="/m/admin/layout/scripts/layout.js" type="text/javascript"></script>
<script charset="UTF-8" src="/assets/js/moment.js"></script>
<script src="/m/global/plugins/bootstrap-toastr/toastr.min.js"></script>
<script src="/m/jstree/jstree.min.js"></script>
<script src="/assets/js/bootbox.js"></script>
<script src="/assets/js/cmcu-common.js"></script>
<script src="/assets/js/angular.js"></script>
<script src="/assets/js/angular-ui-router.min.js"></script>
<script src="/assets/page/controllers.common.js?version=${version}"></script>
<script src="/assets/page/services.common.js?version=${version}"></script>
<script src="/editor-app/custom/js/basic.act.js?version=${version}"></script>
<script src="/editor-app/custom/js/services.act.js?version=${version}"></script>
<script src="/editor-app/custom/js/controllers.act.js?version=${version}"></script>
<script>
    jQuery(document).ready(function () {
        Metronic.init();
        Layout.init();
    });

    var APP_VERSION=1;
    var businessKey ='${businessKey}';
    var enLogin ='${enLogin}';
    var user ={enLogin:enLogin,userLogin:enLogin};
    var taskApp = angular.module('taskApp', ['ui.router','services.act','controllers.act', 'services.common', 'controllers.common']);

    taskApp.controller('TaskShowController', function ($scope,$rootScope,actProcessQueryService, commonCodeService,actTaskQueryService,actProcessHandleService,actTaskHandleService, commonUserService, commonFormDataService,commonFileService,commonDirService) {

        $rootScope.user = user;

        $scope.init = function () {
            $scope.loadData();
            $scope.loadProcessInstance();
            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $rootScope.sysCodes=value.data.data;
                }
            });
        }
        $scope.loadData = function () {
            commonFormDataService.getModelByBusinessKey(businessKey, enLogin).then(function (value) {
                $scope.data = value.data.data.data;
                $scope.template = value.data.data.template;
                $scope.groupList = value.data.data.groupList;
                $scope.group =$scope.groupList[0];
            });
        }

        $scope.loadProcessInstance = function () {

            commonFormDataService.getTplConfig("", businessKey, enLogin).then(function (value) {
                $rootScope.tplConfig = value.data.data;
                actProcessQueryService.getCustomProcessInstance("", businessKey, enLogin).then(function (value) {
                    $rootScope.processInstance = value.data.data;
                    $scope.refreshTime = new Date().getTime();
                    $rootScope.processInstanceId = $scope.processInstance.instance.id;
                    actTaskQueryService.listHistoricTaskInstance($scope.processInstanceId).then(function (value) {
                        $rootScope.tasks = value.data.data;
                    });

                    //判断意见模块
                    var roleNames = $rootScope.tplConfig.markRoleNames;
                    if(roleNames!=""){
                        $scope.showMarkModel = true;
                        for(var i=0;i<roleNames.length;i++){
                            if($rootScope.processInstance.myRunningTaskName.indexOf(roleNames[i]) == 0){
                                $scope.markAddAble = true;
                            }
                        }
                    }
                })


            });


        }

        //流程意见
        $scope.showTaskComments = function (task) {
            $scope.task = task;
            $('#commentsModal').modal('show');
        }


        $scope.refresh = function () {
            $scope.init(true);
        }

        $scope.init();
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>