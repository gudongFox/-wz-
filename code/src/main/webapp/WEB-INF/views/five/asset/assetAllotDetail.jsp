<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:86})">资产管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span >固定资产调拨</span>        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span >固定资产调拨</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();"  style="font-size: 14px;line-height: 1.6">
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

                            <div class="form-group" >
                                <label class="col-md-2 control-label required">申请人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.applicantName" name="applicantName"  ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('applicant');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user" ></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">申请部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal('deptName');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.assetName" name="assetName" required="true"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">资产编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.assetCode" name="assetCode" required="true"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">


                                <label class="col-md-2 control-label required">入账时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applyRefundTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.enterTime" name="enterTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">规格型号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.model" name="model" required="true"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">原使用单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.useUnit" name="useUnit" required="true"   disabled/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal('useUnit');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">原保管人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.useManName" name="useManName"  readonly />
                                        <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.showUserModel('useMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user" ></i></button>
                                     </span>

                                    </div>
                                    <p class="help-block" style="color: red" ng-show="processInstance.firstTask">请点击右侧按钮，选择原保管人</p >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">转入单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.transferUnit"
                                               name="transferUnit" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal('transferUnit');"
                                                    ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">现保管人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.reserveManName" name="reserveManName"  readonly/>
                                        <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.showUserModel('reserveMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user" ></i></button>
                                        </span>

                                    </div>
                                    <p class="help-block" style="color: red" ng-show="processInstance.firstTask">请点击右侧按钮，选择现保管人</p >
                                </div>
                            </div>
                            <%--<div class="form-group">
                                <label class="col-md-2 control-label required">接收人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.receiveName" name="receiveName"  ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                    <button class="btn default" type="button" ng-click="vm.showUserModel('receive');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user" ></i></button>
                                 </span>
                                    </div>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.remark" name="remark" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
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
<%--<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i>费用明细</div>
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
</div>--%>
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../print/print.jsp"/>
<%--
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
                            <input type="text" class="form-control" ng-model="vm.detail.applyMoney" name="applyMoney" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">在途补助</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.onRoadSubsidy" name="onRoadSubsidy" required="true">
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
</div>--%>
