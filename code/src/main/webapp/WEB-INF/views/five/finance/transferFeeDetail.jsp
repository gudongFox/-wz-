<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:62})">财务管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>费用申请</span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>费用申请
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
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_detail_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 280px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请人</label>
                                <div class="col-md-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.applicantName" name="applicantName"  required="true" disabled />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('applicant');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">申请部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"  disabled/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">申请时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applicantTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.applicantTime|date:'yyyy-MM-dd'" name="applicantTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">流程标题</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.title" name="title" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否有项目</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.project" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.project=='是'">
                                <label class="col-md-2 control-label required">项目类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'五洲财务项目类型'}:true"
                                            ng-model="vm.item.projectType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">转出行名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.outBankName" name="outBankName" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">转出行账号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.outBankAccount" name="outBankAccount" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">转入行名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.inBankName" name="inBankName" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">转出行账号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.inBankAccount" name="inBankAccount" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">单据号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.accountNumber" name="accountNumber" ng-disabled="!processInstance.firstTask"/>
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
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i>费用明细
            <span style="margin-left: 30px;color: red;font-size: 12px">合计:</span>&nbsp;&nbsp;&nbsp;
            申请金额合计:&nbsp;<span ng-bind="vm.item.applyMoney"/>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0);"
               ng-show="processInstance.firstTask">
                <i class="fa fa-cog"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th>费用计划类型</th>
                    <th>费用项目</th>
                    <th>事由</th>
                    <th>申请金额（万元）</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.chargePlan" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.chargeProject"></td>
                    <td ng-bind="detail.item"></td>
                    <td ng-bind="detail.applyMoney"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">费用明细</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">费用计划类型</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.chargePlan" name="chargePlan" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">费用项目</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.chargeProject" name="chargeProject" required="true" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">事由</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.item" name="item" required="true" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">申请金额</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.applyMoney" name="applyMoney" required="true" >
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<jsp:include page="../print/print-oaMaterialPurchaseDetail.jsp"/>


