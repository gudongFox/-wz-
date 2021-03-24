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
    <title>文件详情</title>
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
    <link rel="shortcut icon" href="/m/favicon.ico"/>
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
<body class="page-full-width" ng-app="fileApp" ng-controller="FileShowController">

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
                        <i class="icon-clock"></i>  <span style="font-size: 16px" ng-bind="'文件详情 '+item.fileName"></span>
                    </div>
                    <div class="actions" style="float:right">
                        <a href="javascript:;" class="btn btn-sm btn-default" ng-click="refresh()"
                           style="font-size: 14px;line-height: 1.6">
                            <i class="fa fa-refresh"></i> 刷新 </a>
                        <a href="javascript:;" class="btn btn-sm btn-default"  style="font-size: 14px"
                           ng-click="saveFile();" >
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
                                    历史版本</a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="tab_basic"
                                 style="min-height: 260px;overflow-y: auto;overflow-x: hidden;">
                                <div class="row">
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>文件名称<span style="color: red;margin-left: 2px;">*</span></label>
                                        <input type="text" class="form-control" ng-model="item.fileName"
                                               placeholder="请输入名称">
                                    </div>
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;" ng-if="tplConfig.showFileType">
                                        <label>文件类型<span style="color: red;margin-left: 2px;">*</span></label>
                                        <select class="form-control" ng-options="s.name as s.name for s in fileTypes"
                                                ng-model="item.fileType"></select>
                                    </div>
                                </div>
                                <div class="row" ng-show="item.businessKey.indexOf('co_')>=0">
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>子项名称</label>
                                        <input type="text" class="form-control" ng-model="item.buildName"
                                               disabled="disabled">
                                    </div>
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>专业名称</label>
                                        <input type="text" class="form-control"
                                               ng-value="item.majorName" disabled="disabled">
                                    </div>
                                </div>
                                <div class="row" ng-show="item.businessKey.indexOf('co_')>=0">
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;" ng-show="item.sourceId>0">
                                        <label>电子传递来源</label>
                                        <input type="text" class="form-control" ng-model="item.sourceFileName"
                                               disabled="disabled">
                                    </div>
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;" ng-show="item.extractId>0">
                                        <label>图层提取来源</label>
                                        <input type="text" class="form-control"
                                               ng-value="item.extractName" disabled="disabled">
                                    </div>
                                </div>
                                <div class="row" ng-show="item.businessKey.indexOf('co_')>=0">
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>锁定人</label>
                                        <input type="text" class="form-control" ng-model="item.lockedName"
                                               disabled="disabled">
                                    </div>
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>文件路径</label>
                                        <input type="text" class="form-control"
                                               ng-value="item.dirPath" disabled="disabled">
                                    </div>
                                </div>
                                <div class="form-group" style="margin-bottom: 0.5rem;"
                                     ng-show="item.signId">
                                    <label ng-bind="'专家打码('+item.signer+')'">专家签章<span
                                            style="color: red;margin-left: 2px;">*</span></label>
                                    <div class="input-group">
                                        <input type="text" class="form-control"
                                               ng-value="item.signTime|date:'yyyy-MM-dd HH:mm:ss'"
                                               disabled="disabled"/>
                                        <div class="input-group-append"
                                             ng-click="downloadAttach(item.signId);"><span
                                                class="input-group-text"><i
                                                class="la la-download"></i></span></div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>创建人</label>
                                        <input type="text" class="form-control" ng-model="item.creatorName"
                                               disabled="disabled">
                                    </div>
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>创建时间</label>
                                        <input type="text" class="form-control"
                                               ng-value="item.gmtCreate|date:'yyyy-MM-dd HH:mm:ss'" disabled="disabled">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>文件排序<span style="color: red;margin-left: 2px;">*</span></label>
                                        <input type="number" class="form-control" ng-model="item.seq"
                                               placeholder="请输入名称">
                                    </div>
                                    <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>备注</label>
                                        <input type="text" class="form-control" ng-model="item.remark"
                                               placeholder="">
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane" id="tab_act"
                                 style="min-height: 260px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 200px;">上传时间</th>
                                        <th style="width: 90px;">上传人</th>
                                        <th style="width: 120px;">文件大小</th>
                                        <th>备注</th>
                                        <th style="width: 80px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr role="row" ng-repeat="history in historyList">
                                        <td ng-bind="history.gmtCreate|date:'yyyy-MM-dd HH:mm:ss'"></td>
                                        <td ng-bind="history.creator"></td>
                                        <td ng-bind="history.sizeName"></td>
                                        <td ng-bind="history.remark"></td>
                                        <td>
                                            <i class="fa fa-download" style="margin-left: 5px;cursor: pointer;" title="下载"
                                               ng-click="downloadAttach(history.id);"/>
                                            <i class="fa fa-trash" style="margin-left: 5px;cursor: pointer;" title="删除"
                                               ng-click="removeHistory(history.id);" ng-show="history.creator==user.enLogin"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div ng-include="'/web/v1/tpl/commonEdMark.html'"  ng-init="markTplTitle='意见标注'" ng-if="tplConfig.markRoleNames.length>0"></div>
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
    var fileId ='${fileId}';
    var enLogin ='${enLogin}';
    var user ={enLogin:enLogin,userLogin:enLogin};
    var businesskey ='';
    var fileApp = angular.module('fileApp', ['ui.router','services.act','controllers.act', 'services.common', 'controllers.common']);

    fileApp.controller('FileShowController', function ($scope,actProcessQueryService, commonCodeService,actTaskQueryService,actProcessHandleService,actTaskHandleService, commonEdMarkService,commonUserService, commonFormDataService,commonFileService,commonDirService) {

        $scope.user = user;
        $scope.init = function () {
            $scope.loadData();
            $scope.loadHistory();
            commonCodeService.selectAll().then(function (value) {
                if(value.data.ret){
                    $scope.sysCodes=value.data.data;
                }
            });
        }
        $scope.loadData=function(){
            commonFileService.getModelById(fileId).then(function (value) {
                $scope.item = value.data.data;
                businessKey = $scope.item.businessKey;
                commonFormDataService.getTplConfig("", businessKey, enLogin).then(function (value) {
                    $scope.tplConfig = value.data.data;
                    if($scope.tplConfig.showFileType) {
                        commonFileService.listFileType(user.tenetId, $scope.tplConfig.fileTypeCode).then(function (value) {
                            $scope.fileTypes = value.data.data;
                        })
                    }
                });
            });
        }

        $scope.loadHistory=function(){
            commonFileService.listHistory(fileId).then(function (value) {
                $scope.historyList=value.data.data;
            })
        }

        $scope.removeHistory=function (attachId) {
            bootbox.confirm("确认要删除该版本吗?",function (result) {
                if(result){
                    commonFileService.removeHistory($scope.item.id,attachId,user.enLogin).then(function (value) {
                        if(value.data.ret){
                            $scope.loadHistory();
                            toastr.success("删除成功");
                        }
                    });
                }
            })
        }
        $scope.downloadFile=function(id){
            $scope.commonDownload("/common/attach/download/file/"+id);
        }
        $scope.downloadAttach=function(attachId){
            $scope.commonDownload("/common/attach/download/"+attachId);
        }

        $scope.saveFile=function() {
            if( $scope.item.seq===undefined){
                toastr.error("排序必须为数字类型不能为空!");
                return;
            }
            commonFileService.save( $scope.item.id, $scope.item.fileName, $scope.item.fileType, $scope.item.seq, $scope.item.remark,user.enLogin).then(function (value) {
                if(value.data.ret){
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