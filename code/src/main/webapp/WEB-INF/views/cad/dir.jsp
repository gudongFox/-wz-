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
    <title>文件夹详情</title>
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
<body class="page-full-width" ng-app="dirApp" ng-controller="DirShowController">

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
                        <i class="icon-clock"></i>  <span style="font-size: 16px" ng-bind="'文件夹详情 '+currentDir.cnName"></span>
                    </div>

                    <div class="actions" style="float:right">
                        <a href="javascript:;" class="btn btn-sm btn-default" ng-click="refresh()"
                           style="font-size: 14px;line-height: 1.6">
                            <i class="fa fa-refresh"></i> 刷新 </a>
                        <a href="javascript:;" class="btn btn-sm btn-default"  style="font-size: 14px"
                           ng-click="saveDir();" >
                            <i class="fa fa-save"></i> 保存 </a>
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
                                    文件列表</a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="tab_basic"
                                 style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                                <div class="form-group" style="margin-bottom: 0.5rem;">
                                    <label>文件名称<span style="color: red;margin-left: 2px;">*</span></label>
                                    <input type="text" class="form-control" ng-model="currentDir.cnName" ng-disabled="currentDir.creator!=user.userLogin"
                                           placeholder="请输入名称">
                                </div>
                                <div class="form-group" style="margin-bottom: 0.5rem;">
                                    <label>文件夹大小<span style="color: red;margin-left: 2px;">*</span></label>
                                    <input  class="form-control" ng-model="currentDir.sizeName"ng-disabled="currentDir.creator!=user.userLogin"
                                           placeholder="">
                                </div>
                                <div class="form-group" style="margin-bottom: 0.5rem;">
                                    <label>文件排序<span style="color: red;margin-left: 2px;">*</span></label>
                                    <input type="number" class="form-control" ng-model="currentDir.seq"ng-disabled="currentDir.creator!=user.userLogin"
                                           placeholder="">
                                </div>
                                <div class="form-group" style="margin-bottom: 0.5rem;" ng-if="currentDir.buildId">
                                    <label>子项名称<span style="color: red;margin-left: 2px;">*</span></label>
                                    <input type="number" class="form-control" ng-model="currentDir.buildId"ng-disabled="currentDir.creator!=user.userLogin"
                                           placeholder="">
                                </div>
                                <div class="form-group" style="margin-bottom: 0.5rem;" ng-if="currentDir.buildId">
                                    <label>专业名称<span style="color: red;margin-left: 2px;">*</span></label>
                                    <input type="number" class="form-control" ng-model="currentDir.majorName"ng-disabled="currentDir.creator!=user.userLogin"
                                           placeholder="">
                                </div>
                                <div class="form-group" style="margin-bottom: 0.5rem;">
                                    <label>创建人</label>
                                    <input type="text" class="form-control" ng-model="currentDir.creatorName"
                                           disabled="disabled">
                                </div>
                                <div class="form-group" style="margin-bottom: 0.5rem;">
                                    <label>创建时间</label>
                                    <input type="text" class="form-control"
                                           ng-value="currentDir.gmtCreate|date:'yyyy-MM-dd HH:mm:ss'" disabled="disabled">
                                </div>
                                <div class="form-group" style="margin-bottom: 0.5rem;">
                                    <label>备注</label>
                                    <input type="text" class="form-control" ng-model="currentDir.remark" ng-disabled="currentDir.creator!=user.userLogin"
                                           placeholder="">
                                </div>
                            </div>
                            <div class="tab-pane" id="tab_act"
                                 style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">
                                            <label>#</label>
                                        </th>
                                        <th>文件名</th>
                                        <th style="width: 150px;" ng-show="tplConfig.showFileType"
                                            class="hidden-sm">文件类型</th>
                                        <th style="width: 90px;">上传人</th>
                                        <th style="width: 170px;" class="hidden-sm">修改时间</th>
                                        <th style="width: 100px;">大小</th>

                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="file in files">
                                        <td>
                                            <span ng-bind="$index+1"></span>
                                        </td>
                                        <td style="word-break:break-all;">
                                            <img ng-src="{{'/assets/img/'+file.commonAttach.extensionName+'.png'}}"
                                                 onerror="this.src='/assets/img/doc.png'"
                                                 style="width: 25px;height: 25px;" alt="">
                                            <span style="font-size: 10px;margin-right: 5px;"></span>
                                            <span ng-bind="file.fileName"></span>
                                        </td>
                                        <td ng-show="tplConfig.showFileType" ng-bind="file.fileType" class="hidden-sm"></td>
                                        <td ng-bind="file.creatorName"></td>
                                        <td class="hidden-sm">
                                        <span ng-bind="file.gmtModified|date:'yyyy-MM-dd HH:mm'"
                                              style="{{file.commonFileProperty.signAttachId>0?'color:green;font-weight:bold;':''}}"></span>
                                        </td>
                                        <td ng-bind="file.commonAttach.sizeName"></td>

                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
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
    var dirId ='${dirId}';
    var enLogin ='${enLogin}';
    var user ={enLogin:enLogin,userLogin:enLogin};
    var businesskey ='';
    var dirApp = angular.module('dirApp', ['ui.router','services.act','controllers.act', 'services.common', 'controllers.common']);

    dirApp.controller('DirShowController', function ($scope,$rootScope,actProcessQueryService, commonCodeService,actTaskQueryService,actProcessHandleService,actTaskHandleService, commonUserService, commonFormDataService,commonFileService,commonDirService) {

        $rootScope.user = user;


        $scope.init = function () {

            $scope.loadData(dirId);
            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $rootScope.sysCodes=value.data.data;
                }
            });

        }
        $scope.loadData=function(dirId){
            if(dirId>0){
                commonDirService.getModelById(dirId).then(function (value) {
                    $scope.currentDir=value.data.data;
                    businessKey = $scope.currentDir.businessKey;
                    if(businessKey.indexOf('co_')===0){
                        $scope.tplConfig.editable=$scope.currentDir.buildId>0;
                    }
                    commonDirService.listBreadcrumb(businessKey, dirId).then(function (value) {
                        $scope.breadcrumbs = value.data.data;
                    })
                    commonDirService.listData(businessKey,dirId).then(function (value) {
                        $scope.dirs = value.data.data;
                    })
                    commonFileService.listData(businessKey,dirId).then(function (value) {
                        $scope.files = value.data.data;
                    });

                    commonFormDataService.getTplConfig("", businessKey, enLogin).then(function (value) {
                        $rootScope.tplConfig = value.data.data;
                    });
                })
            }
        }
        $scope.saveDir=function() {
            if( $scope.currentDir.seq===undefined){
                toastr.error("排序必须为数字类型不能为空!");
                return;
            }

            commonDirService.save( $scope.currentDir.id,  $scope.currentDir.cnName,  $scope.currentDir.seq,  $scope.currentDir.remark, user.enLogin).then(function (value) {
                $("#commonDirModal").modal("hide");
                if (value.data.ret) {
                    toastr.success("保存成功");
                    $scope.init();
                }
            })
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