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
            <span ng-bind="tableName">差旅费报销</span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> <span ng-bind="tableName">差旅费报销</span>
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
                            <div class="row">
                                <div class="col-md-12 col-xl-12">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <div class="panel-title">
                                                    <span href="#panelFormInfo" class="panel-toggle" data-toggle="collapse"
                                                          data-parent="#detail_form">
                                                        表单信息
                                                    </span>
                                            </div>
                                        </div>
                                        <div class="panel-body" id="panelFormInfo" class="panel-collapse collapse in">

                                            <div class="form-group">
                                                <label class="col-md-2 control-label required">出差开始日期</label>
                                                <div class="col-md-4">
                                                    <div class="input-group date date-picker-before-now" id="startTime">
                                                        <input type="text" class="form-control"
                                                               ng-model="vm.item.startTime" name="startTime" required="true" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"  >
                                                        <span class="input-group-btn">
                                                   <button class="btn default" type="button" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"><i class="fa fa-calendar"></i></button>
                                            </span>
                                                    </div>
                                                </div>
                                                <label class="col-md-2 control-label required">出差结束日期</label>
                                                <div class="col-md-4">
                                                    <div class="input-group date date-picker-before-now" id="endTime">
                                                        <input type="text" class="form-control"
                                                               ng-model="vm.item.endTime" name="endTime" required="true" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"  >
                                                        <span class="input-group-btn">
                                                   <button class="btn default" type="button" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"><i class="fa fa-calendar"></i></button>
                                            </span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label required">是否含培训费</label>
                                                <div class="col-md-4">
                                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                                            ng-model="vm.item.travelExpense" class="form-control" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"></select>
                                                </div>
                                                <label class="col-md-2 control-label required">是否特批</label>
                                                <div class="col-md-4">
                                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                                            ng-model="vm.item.extraApprove" class="form-control"
                                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"></select>
                                                </div>
                                            </div>
                                            <div class="form-group" ng-if="vm.item.extraApprove=='是'">
                                                <label class="col-md-2 control-label "></label>
                                                <div class="col-md-4">
                                                </div>
                                                <label class="col-md-2 control-label required">特批原因</label>
                                                <div class="col-md-4">
                                                    <input type="text" class="form-control" required ng-model="vm.item.extraApproveReason"
                                                           ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"/>
                                                </div>
                                            </div>
                                            <div class="form-group" style="background-color: #e5f2ff">
                                                <label class="col-md-2 control-label required">是否存在项目</label>
                                                <div class="col-md-4 control-label">
                                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                                            ng-model="vm.item.isProject" class="form-control" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"></select>
                                                </div>
                                                <label class="col-md-2 control-label required" ng-if="vm.item.isProject">是否科研项目</label>
                                                <div class="col-md-4 control-label" ng-if="vm.item.isProject">
                                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                                            ng-model="vm.item.scientific" class="form-control" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"></select>
                                                </div>

                                            </div>
                                            <div class="form-group" style="background-color: #e5f2ff" ng-if="vm.item.isProject">
                                                <label class="col-md-2 control-label">项目名称</label>
                                                <div class="col-md-4 control-label">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control"
                                                               ng-model="vm.item.projectName" name="projectName"
                                                               ng-disabled="true"/>
                                                        <span class="input-group-btn">
                                                            <button class="btn default" type="button"
                                                                    ng-click="vm.showSelectPreOrReviewModal('projectId')"
                                                                    ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"><i
                                                                    class="fa fa-cog"></i>
                                                            </button>
                                                        </span>
                                                    </div>
                                                </div>
                                                <label class="col-md-2 control-label">项目号</label>
                                                <div class="col-md-4 control-label">
                                                    <input type="text" class="form-control" ng-model="vm.item.projectNo"
                                                           ng-disabled="true"/>
                                                </div>

                                            </div>
                                            <div class="form-group" style="background-color: #e5f2ff" ng-if="vm.item.isProject">
                                                <label class="col-md-2 control-label required">项目负责人</label>
                                                <div class="col-md-4 control-label">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control" required
                                                               ng-model="vm.item.bussineManagerName" name="bussineManagerName" readonly/>
                                                        <span class="input-group-btn">
                                                            <button class="btn default" type="button" ng-click="vm.showUserModel('bussineManager');" ng-disabled="vm.item.bussineManagerName.length!=0&&!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"><i class="fa fa-user"></i></button>
                                                        </span>
                                                    </div>
                                                </div>
                                                <label class="col-md-2 control-label required">项目主管院长</label>
                                                <div class="col-md-4 control-label">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control" required
                                                               ng-model="vm.item.projectDeputyName" name="projectDeputyName" readonly/>
                                                        <span class="input-group-btn">
                                                            <button class="btn default" type="button" ng-click="vm.showUserModel('projectDeputy');" ng-disabled="vm.item.bussineManagerName.length!=0&&!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"><i class="fa fa-user"></i></button>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" style="background-color: #e5f2ff" ng-if="vm.item.isProject">
                                                <label class="col-md-2 control-label">项目类别</label>
                                                <div class="col-md-4 control-label">
                                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'五洲项目类型'}:true"
                                                            ng-model="vm.item.projectType" class="form-control" ng-disabled="true"></select>
                                                    <span style="color: red;font-size: 12px">如果项目类别与实际不符合，请联系各单位经营人员</span>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-md-2 control-label ">备注</label>
                                                <div class="col-md-10">
                                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label required">报销部门</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                                        <span class="input-group-btn" >
                                                <button class="btn default" type="button" ng-click="vm.showDeptModal(0);" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"><i class="fa fa-cog"></i></button>
                                             </span>
                                                    </div>
                                                </div>
                                                <label class="col-md-2 control-label required">单据号</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control" ng-model="vm.item.receiptsNumber" readonly
                                                               name="receiptsNumber" required="true" maxlength="20" placeholder="单据号" disabled/>
                                                        <span class="input-group-btn">
                                                          <button class="btn default" type="button"
                                                                  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"
                                                                  ng-click="vm.getReceiptsNumber();">
                                                            <i class="fa fa-refresh"></i></button>

                                                        </span>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-md-2 control-label">创建人</label>
                                                <div class="col-md-4">
                                                    <input type="text" class="form-control"
                                                           ng-model="vm.item.creatorName"
                                                           disabled="disabled"/>
                                                </div>
                                                <label class="col-md-2 control-label">创建时间</label>
                                                <div class="col-md-4">
                                                    <input type="text" class="form-control"
                                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}"
                                                           disabled="disabled"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12 col-xl-12">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                             <span href="#panelDetail" class="panel-toggle" data-toggle="collapse"
                                                   data-parent="#detail_form">
                                                      报销 明细信息
                                             </span>
                                            <a href="javascript:;" class="btn btn-sm btn-default"
                                               style="float: right;line-height: 0;" ng-click="vm.showDetailModel(0,'报销');"
                                               ng-show="processInstance.firstTask">
                                                <i class="fa fa-plus"></i> 新增 </a>
                                        </div>
                                        <div class="panel-body" id="panelDetail" class="panel-collapse collapse in">
                                            <%--     <div class="form-group">
                                                     <button type="button" class="btn btn-default  pull-right" ng-click="vm.showDetailModel(0);"><i class="fa fa-plus"></i> 新增</button>
                                                 </div>--%>
                                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                                    <thead>
                                                    <tr>
                                                        <th style="width: 50px;">序号</th>
                                                        <th style="width: 180px">列支部门</th>
                                                        <th style="width: 120px">列支人</th>
                                                        <th style="width: 160px">扣减预算类型</th>
                                                        <th>报销|补助</th>
                                                        <th>类别</th>
                                                        <th>出差天数(天)</th>
                                                        <th>报销标准(元/天)</th>
                                                        <th>报销金额（元）</th>
                                                        <th style="width: 60px;" >操作</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr ng-repeat="detail in vm.details|filter:{expenseType:'报销'}:true">
                                                        <td class="dt-right" ng-bind="$index+1"></td>
                                                        <td ng-bind="detail.deptName"></td>
                                                        <td ng-bind="detail.applicantName+'('+detail.applicant+')'"></td>
                                                        <td ng-bind="detail.budgetNo"></td>
                                                        <td ng-bind="detail.expenseType"
                                                            class="data_title" ng-click="vm.showDetailModel(detail.id,'');"></td>
                                                        <td ng-bind="detail.realType"></td>
                                                        <td ng-bind="detail.travelExpenseDays"></td>
                                                        <td ng-bind="detail.applyStandard"></td>
                                                        <td ng-bind="detail.applyMoney"></td>
                                                        <td>
                                                            <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id,'');" title="详情" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"></i>
                                                            <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除" ng-if="processInstance.firstTask"></i>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12 col-xl-12">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                             <span href="#panelDetail" class="panel-toggle" data-toggle="collapse"
                                                   data-parent="#detail_form">
                                                      补助 明细信息
                                             </span>

                                            <a href="javascript:;" class="btn btn-sm btn-default"
                                               style="float: right;line-height: 0;" ng-click="vm.showDetailModel(0,'补助');"
                                               ng-show="processInstance.firstTask">
                                                <i class="fa fa-plus"></i> 新增 </a>
                                        </div>
                                        <div class="panel-body" id="panelDetail1" class="panel-collapse collapse in">
                                            <%--     <div class="form-group">
                                                     <button type="button" class="btn btn-default  pull-right" ng-click="vm.showDetailModel(0);"><i class="fa fa-plus"></i> 新增</button>
                                                 </div>--%>
                                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                                <thead>
                                                <tr>
                                                    <th style="width: 50px;">序号</th>
                                                    <th style="width: 180px">列支部门</th>
                                                    <th style="width: 120px">列支人</th>
                                                    <th style="width: 160px">扣减预算类型</th>
                                                    <th>报销|补助</th>
                                                    <th>类别</th>
                                                    <th>出差天数(天)</th>
                                                    <th>补助标准(元/天)</th>
                                                    <th>补助金额（元）</th>
                                                    <th style="width: 60px;" >操作</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr ng-repeat="detail in vm.details|filter:{expenseType:'补助'}:true">
                                                    <td class="dt-right" ng-bind="$index+1"></td>
                                                    <td ng-bind="detail.deptName"></td>
                                                    <td ng-bind="detail.applicantName+'('+detail.applicant+')'"></td>
                                                    <td ng-bind="detail.budgetNo"></td>
                                                    <td ng-bind="detail.expenseType"
                                                        class="data_title" ng-click="vm.showDetailModel(detail.id,'');"></td>
                                                    <td ng-bind="detail.realType"></td>
                                                    <td ng-bind="detail.travelExpenseDays"></td>
                                                    <td ng-bind="detail.applyStandard"></td>
                                                    <td ng-bind="detail.applyMoney"></td>
                                                    <td>
                                                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id,'');" title="详情" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"></i>
                                                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除" ng-if="processInstance.firstTask"></i>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12 col-xl-12">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                             <span href="#panelRelated" class="panel-toggle" data-toggle="collapse"
                                                   data-parent="#detail_form">
                                                    抵扣信息
                                             </span>
                                            <%--                                            <a href="javascript:;" class="btn btn-sm btn-default"
                                                                                           style="margin-left:10px;;float: right;line-height: 0;"
                                                                                           ng-click="vm.showRefundModel();"
                                                                                           ng-show="processInstance.firstTask">
                                                                                            <i class="fa fa-plus"></i> 选择还款流程 </a>--%>
                                            <a href="javascript:;" class="btn btn-sm btn-default"
                                               style="float: right;line-height: 0;" ng-click="vm.showLoanModel();"
                                               ng-show="processInstance.firstTask&&vm.deductions.length==0">
                                                <i class="fa fa-plus"></i> 选择借款流程 </a>
                                        </div>
                                        <div class="panel-body" id="panelRelated" class="panel-collapse collapse in">
                                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                                <thead>
                                                <tr>
                                                    <th style="width: 50px;">序号</th>
                                                    <%--  <th>流程类型</th>--%>
                                                    <th>流程名称</th>
                                                    <th>借款金额（元）</th>
                                                    <th>剩余金额（元）</th>
                                                    <th>流程备注</th>
                                                    <th>创建时间</th>
                                                    <th style="width: 60px;">操作</th>
                                                </tr>
                                                </thead>
                                                <tr ng-repeat="detail in vm.deductions">
                                                    <td class="dt-right" ng-bind="$index+1"></td>
                                                    <%-- <td ng-bind="detail.relevanceType"></td>--%>
                                                    <td ng-bind="detail.relevanceName" class="data_title"
                                                        ng-click="vm.showRelevanceModel(detail);"></td>
                                                    <td ng-bind="detail.relevanceMoney"></td>
                                                    <td ng-bind="detail.relevanceRemainMoney"></td>
                                                    <td ng-bind="detail.relevanceRemark"></td>
                                                    <td ng-bind="detail.relevanceTime"></td>
                                                    <td>
                                                        <i class="fa fa-pencil margin-right-5"
                                                           ng-click="vm.showRelevanceModel(detail);" title="详情" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"></i>
                                                        <i class="fa fa-trash-o" ng-show="processInstance.firstTask"
                                                           ng-click="vm.removeDeduction(detail.id);" title="删除" ng-if="processInstance.firstTask"></i>
                                                    </td>
                                                </tr>
                                                <%--                                                <tr>
                                                                                                    <td style="width: 50px;"></td>
                                                                                                    <td>合计</td>
                                                                                                    <td ng-bind="vm.item.deductionMoney"></td>
                                                                                                    <td></td>
                                                                                                    <td></td>
                                                                                                    <td></td>
                                                                                                    <td style="width: 60px;"></td>
                                                                                                </tr>--%>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12 col-xl-12">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                             <span href="#panelDetail" class="panel-toggle" data-toggle="collapse"
                                                   data-parent="#detail_form">
                                                      付款信息
                                             </span>
                                            <span style="font-size: 12px;color: red"
                                                  ng-bind="'      报销合计: '+vm.item.totalApplyMoney+' 元   补助合计: '+vm.item.totalSubsidyMoney+' 元   抵扣金额: '+vm.item.loanRemainMoney+' 元  退补金额: '+vm.item.realMoney+' 元'"></span>
                                            <a href="javascript:;" class="btn btn-sm btn-default"
                                               style="float: right;line-height: 0;" ng-click="vm.showUserDetailModel(0);"
                                               ng-show="processInstance.firstTask">
                                                <i class="fa fa-plus"></i> 新增 </a>
                                        </div>
                                        <div class="panel-body" class="panel-collapse collapse in">
                                            <%--     <div class="form-group">
                                                     <button type="button" class="btn btn-default  pull-right" ng-click="vm.showDetailModel(0);"><i class="fa fa-plus"></i> 新增</button>
                                                 </div>--%>
                                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                                <thead>
                                                <tr>
                                                    <th style="width: 50px;">序号</th>
                                                    <th style="width: 250px">列支部门</th>
                                                    <th style="width: 160px">列支人</th>
                                                    <th style="width: 260px">分配金额(元)</th>
                                                    <th >备注</th>
                                                    <th style="width: 60px;" >操作</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr ng-repeat="detail in vm.userDetails">
                                                    <td class="dt-right" ng-bind="$index+1"></td>
                                                    <td ng-bind="detail.deptName"></td>
                                                    <td ng-bind="detail.userName+'('+detail.user+')'"
                                                        class="data_title" ng-click="vm.showUserDetailModel(detail.id);"></td>
                                                    <td ng-bind="detail.money"></td>
                                                    <td ng-bind="detail.remark"></td>
                                                    <td>
                                                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showUserDetailModel(detail.id);" title="详情" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"></i>
                                                        <i class="fa fa-trash-o" ng-click="vm.removeUserDetail(detail.id);" title="删除" ng-if="processInstance.firstTask"></i>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
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

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade" id="userDetailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">付款明细</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="userDetailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">列支部门</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.userDetail.deptName"
                                           name="deptName" required="true" disabled>
                                    <span class="input-group-btn">
                                    <button class="btn default" type="button" ng-click="vm.showDeptModal(2);"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"><i
                                            class="fa fa-cog"></i>
                                    </button>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">列支人</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.userDetail.userName"
                                           name="userName" required="true" disabled>
                                    <span class="input-group-btn">
                                    <button class="btn default" type="button" ng-click="vm.showUserModel('userName');"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"><i
                                            class="fa fa-cog"></i>
                                    </button>
                                    </span>
                                </div>
                            </div>
                        </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">分配金额</label>
                        <div class="col-md-7">
                            <div class="input-group">
                                <span class="input-group-addon">￥</span>
                                <input type="text" class="form-control" ng-model="vm.userDetail.money"
                                       name="money" required="true" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)">
                                <span class="input-group-addon">元</span>
                            </div>
                        </div>
                    </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">备注</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"/>
                            </div>
                        </div>

                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveUserDetail();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
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
                            <label class="col-md-4 control-label">列支部门</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.detail.deptName"
                                           name="deptName" required="true" disabled>
                                    <span class="input-group-btn">
                                    <button class="btn default" type="button" ng-click="vm.showDeptModal(1);"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"><i
                                            class="fa fa-cog"></i>
                                    </button>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">列支人</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.detail.applicantName"
                                           name="applicantName" required="true" disabled>
                                    <span class="input-group-btn">
                                    <button class="btn default" type="button" ng-click="vm.showUserModel('applicantName');"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"><i
                                            class="fa fa-cog"></i>
                                    </button>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">扣减预算类型</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.detail.budgetNo"
                                           name="budgetNo" required="true" disabled>
                                    <span class="input-group-btn">
                                        <button class="btn default" type="button" ng-click="vm.showPlanTypeModal();"
                                                ng-disabled="true"><i class="fa fa-cog"></i>
                                    </button>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">预算剩余金额</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                    <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control" ng-model="vm.detail.budgetBalance"
                                           name="budgetBalance" required="true" disabled>
                                    <span class="input-group-addon">万元</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label required">出差地点</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"  id ="txt_city" ng-change="vm.getTravelPlaceType()"
                                       ng-model="vm.detail.travelPlace" name="travelPlace"
                                ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"/>
                                <%--<div  class="city-select" id="single-select-1"></div>--%>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label required">出差地点类型</label>
                            <div class="col-md-7">
                                <select ng-options="s.codeValue as s.code for s in sysCodes | filter:{codeCatalog:'出差地点类型'}:true"
                                        ng-model="vm.detail.travelPlaceType" ng-change="vm.getApplyStandard()"
                                        class="form-control" ng-disabled="!(processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"></select>
                            </div>
                        </div>
<%--                        <div class="form-group">
                            <label class="col-md-4 control-label">起止时间</label>
                                <div class="col-md-7">
                                    <div class="input-group date-range-picker" >
                                        <input type="text" class="form-control" ng-change="vm.getApplyStandard()"
                                               ng-model="vm.detail.travelDuringDate" name="travelDuringDate" required="true" disabled>
                                        <span class="input-group-btn">
                                                   <button class="btn default" type="button" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"><i class="fa fa-calendar"></i></button>
                                            </span>
                                    </div>
                                </div>
                        </div>--%>


                        <div class="form-group">
                            <label class="control-label col-md-4">申报类别</label>
                            <div class="col-md-7">
                                <select ng-model="vm.detail.expenseType" class="form-control"
                                        ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)">
                                    <option value="报销">报销</option>
                                    <option value="补助">补助</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group" ng-if="vm.detail.expenseType=='报销'">
                            <label class="col-md-4 control-label required">报销类型</label>
                            <div class="col-md-7">
                                <select ng-options="s.codeValue as s.code for s in sysCodes | filter:{codeCatalog:'差旅费报销类型'}:true"
                                        ng-model="vm.detail.realType" ng-change="vm.getApplyStandard()"
                                        class="form-control" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"></select>
                            </div>
                        </div>
                        <div class="form-group" ng-if="vm.detail.expenseType=='补助'">
                            <label class="col-md-4 control-label required">补助类型</label>
                            <div class="col-md-7">
                                <select ng-options="s.codeValue as s.code for s in sysCodes | filter:{codeCatalog:'差旅费补助类型'}:true"
                                        ng-model="vm.detail.realType" ng-change="vm.getApplyStandard()"
                                        class="form-control" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">天数(天)</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.travelExpenseDays" ng-change="vm.getApplyStandard()"
                                       name="travelExpenseDays" required="true" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)">
                            </div>
                        </div>
                        <div class="form-group" ng-if="vm.detail.expenseType=='报销'">
                            <label class="col-md-4 control-label">报销标准</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                    <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control" ng-model="vm.detail.applyStandard"
                                           name="applyStandard" required="true" readonly>
                                    <span class="input-group-addon">元/天</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group" ng-if="vm.detail.expenseType!='报销'">
                            <label class="col-md-4 control-label">补助标准</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                    <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control" ng-model="vm.detail.applyStandard"
                                           name="applyStandard" required="true" disabled>
                                    <span class="input-group-addon">元/天</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">报销金额</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                    <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control" ng-model="vm.detail.applyMoney"
                                           name="applyMoney" required="true" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)">
                                    <span class="input-group-addon">元</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">财务确认</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                    <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control" ng-model="vm.detail.financialConfirmation"
                                           name="financialConfirmation" ng-disabled="!(processInstance.myRunningTaskName.indexOf('财务核算')>=0||processInstance.myRunningTaskName.indexOf('财务确认')>=0)">
                                    <span class="input-group-addon">元</span>
                                </div>
                            </div>
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
<div class="modal fade draggable-modal" id="selectLoanModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4>选择借款流程</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qProject"/>
                    </div>
                </div>
                <div class="table-scrollable"
                     style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>借款人</th>
                            <th class="hidden-md hidden-sm">借款部门</th>
                            <th>借款事由</th>
                            <th>借款金额（元）</th>
                            <th>借款剩余金额（元）</th>
                            <th style="width: 100px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.listLoans|filter:{projectName:vm.qProject}">
                            <td class="dt-right">
                                <input type="checkbox" class="cb_loan"
                                       data-name="{{item}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.applicantName" class="data_title" ><strong></strong>
                            </td>
                            <td ng-bind="item.deptName" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.item"></td>
                            <td ng-bind="item.totalApplyMoney"></td>
                            <td ng-bind="item.remainMoney"></td>
                            <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectLoanModel()">确认</button>
            </div>
        </div>
    </div>

</div>
<div class="modal fade draggable-modal" id="selectRefundModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4>选择还款流程</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qProject"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>报销人</th>
                            <th class="hidden-md hidden-sm">报销部门</th>
                            <th>单据号</th>
                            <th>单据状态</th>
                            <th>还款金额（元）</th>
                            <th style="width: 100px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.listRefunds">
                            <td class="dt-right">
                                <input type="checkbox"  class="cb_refund"
                                       data-name="{{item}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.applicantName"  class="data_title"  ><strong ></strong>
                            </td>
                            <td ng-bind="item.deptName" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.receiptsNumber"></td>
                            <td ng-bind="item.receiptsState"></td>
                            <td ng-bind="item.totalRefundMoney"></td>
                            <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectRefundModel()">确认</button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="../print/print-oaMaterialPurchaseDetail.jsp"/>


