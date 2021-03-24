<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance">综合办公</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span >个人非密信息导出审批</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span >个人非密信息导出审批</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator">
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.userName" name="userName"  required="true" disabled/>
                                </div>
                                <label class="col-md-2 control-label required">职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.userLogin" name="userLogin" required="true" disabled  >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"    disabled/>
                                </div>
                                <label class="col-md-2 control-label required">电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.phone" name="phone" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">导出硬盘序列号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.hardDiskNo" name="hardDiskNo"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">导出服务器地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.serviceAddress" name="serviceAddress"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">拟导出文件名称</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.fileName" required="true" name="fileName" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                                    <span class="help-block" ng-show="processInstance.firstTask">请携带硬盘更换确认单进行信息导出</span>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print-oaMessageExportDetail.jsp"/>
