<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">综合办公</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="five.superviseYear">年度重点任务督办</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.noticeTitle"></span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> <span ng-bind="tableName">年度重点任务督办</span>
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
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
                                <label class="col-md-2 control-label required">督办事项</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.fileHeader" name="fileHeader" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">



                                <label class="col-md-2 control-label required">督办类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'督办类型'}:true"
                                            ng-model="vm.item.superviseType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">承办单位/部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true" ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>

<%--                                <label class="col-md-2 control-label required">事项</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.superviseItems"  ng-disabled="false"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">办结时限</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applyTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.superviseTime" name="superviseTime" required="true" ng-disabled="false"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="false"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label required">反馈周期</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'反馈周期'}:true"
                                            ng-model="vm.item.dispatchType" class="form-control"
                                            ng-disabled="false"   ></select>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">批示领导</label>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.companyLeaderName" name="companyLeaderName"  required="true" ng-disabled="!processInstance.firstTask"/>
                                            <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('companyLeader');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                        </div>
                                    </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label ">批示领导意见</label>
                                <div class="col-md-10">
                                    <textarea type="text" rows="3" class="form-control" ng-model="vm.item.companyLeaderOpinion"  name="companyLeaderOpinion" ng-disabled="!processInstance.myRunningTaskName.indexOf('公司领导')>-1">
                                    </textarea>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">督办人</label>
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
            <i class="fa fa-file"></i> 督办子项</div>
        <div class="actions">
<%--            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0);"
               ng-show="processInstance.myTaskFirst">--%>
                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                        <i class="fa fa-plus"></i> 创建 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                        <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th>督办类型</th>
                    <th>任务来源</th>
                    <th>任务内容</th>
                    <th>办理人</th>
                    <th>办结时限</th>
                    <th>办理部门</th>
                    <th>反馈周期</th>
                    <th>办理进度</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.superviseType" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.taskSource"></td>
                    <td ng-bind="detail.taskDefinition"></td>
                    <td ng-bind="detail.transactorName"></td>
                    <td ng-bind="detail.timeLimit"></td>
                    <td ng-bind="detail.deptName"></td>
                    <td ng-bind="detail.feedbackTime"></td>
                    <td ng-bind="detail.transactionPlan"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除" ng-show="processInstance.firstTask"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>


<%--<jsp:include page="../../common/common-actRelevance.jsp"/>--%>


<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>


