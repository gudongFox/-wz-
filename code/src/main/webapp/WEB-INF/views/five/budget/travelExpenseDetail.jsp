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
            <span>差旅费报销</span>
            <i class="fa fa-angle-right"></i>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> 差旅费报销
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
                                <label class="col-md-2 control-label required">报销人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.applicantName" name="applicantName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('applicant');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label required">报销部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">出差开始日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="startTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.startTime" name="startTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">出差结束日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="endTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.endTime" name="endTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">出差地点</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.travelPlace" name="travelPlace" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">项目</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目类别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'财务项目类别'}:true"
                                            ng-model="vm.item.projectType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">是否含培训费</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.travelExpense" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否需要超标审批</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否需要超标审批'}:true"
                                            ng-model="vm.item.exceedStandard" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label ">超标项目</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.exceedProject" name="exceedProject" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">单据号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.receiptsNumber" name="receiptsNumber" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label ">附单数据</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.attachedList" name="attachedList" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">报销时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applyRefundTime">
                                        <input type="text" class="form-control"
                                                ng-model="vm.item.applyRefundTime" name="applyRefundTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label ">退补金额</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.refundAmount" name="refundAmount" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">冲抵及还款合计</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.chargeAgainst" name="chargeAgainst" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label ">统计</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.count" name="count" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.library" name="library" ng-disabled="!processInstance.firstTask"/>
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
            <span style="margin-left: 30px;color: red;font-size: 15px">合计:</span>&nbsp;&nbsp;&nbsp;
            合计报销金额:&nbsp;<span ng-bind="vm.item.totalApplyMoney"/>&nbsp;&nbsp;&nbsp;
            合计在途补助:&nbsp;<span ng-bind="vm.item.totalOnRoadSubsidy"/> &nbsp;&nbsp;&nbsp;
            合计金额:&nbsp;<span ng-bind="vm.item.totalCount"/>&nbsp;&nbsp;&nbsp;
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
                    <th>项目类别</th>
                    <th>报销项目</th>
                    <th>列支部门</th>
                    <th>列支人</th>
                    <th>出差天数(天)</th>
                    <th>在途时间(天)</th>
                    <th>报销标准</th>
                    <th>报销金额</th>
                    <th>在途补助</th>
                    <th>金额小计</th>
                    <th>财务确认</th>
                    <th>会计科目</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.projectType" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.applyRefundProject"></td>
                    <td ng-bind="detail.deptName"></td>
                    <td ng-bind="detail.applicantName"></td>
                    <td ng-bind="detail.travelExpenseDays"></td>
                    <td ng-bind="detail.onRoadTime"></td>
                    <td ng-bind="detail.applyStandard"></td>
                    <td ng-bind="detail.applyMoney"></td>
                    <td ng-bind="detail.onRoadSubsidy"></td>
                    <td ng-bind="detail.count"></td>
                    <td ng-bind="detail.financialConfirmation"></td>
                    <td ng-bind="detail.accountSubject"></td>
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
                            <label class="col-md-4 control-label">项目类别</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.projectType" name="projectType" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">报销项目</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.applyRefundProject" name="applyRefundProject" required="true" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">列支部门</label>
                        <div class="col-md-7">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">列支人</label>
                        <div class="col-md-7">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.item.applicantName" name="applicantName"  required="true" ng-disabled="!processInstance.firstTask" />
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('applicant');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label">出差天数(天)</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.travelExpenseDays" name="travelExpenseDays" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">在途时间(小时)</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.onRoadTime" name="onRoadTime" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">报销标准</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.applyStandard" name="applyStandard" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">报销金额</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.applyMoney" name="applyMoney" ng-change="vm.coutFinalPrice();" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">在途补助</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.onRoadSubsidy" name="onRoadSubsidy" ng-change="vm.coutFinalPrice();" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">金额小计</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.count" name="count" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">财务确认</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.financialConfirmation" name="financialConfirmation" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">会计科目</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.accountSubject" name="accountSubject" required="true">
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


