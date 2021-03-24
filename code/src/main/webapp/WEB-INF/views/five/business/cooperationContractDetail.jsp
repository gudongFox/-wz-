<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>内部协作</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">内部协作协议合同</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.deptName"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">内部协作协议合同</span>
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" >
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
                     style="min-height: 140px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label ">内部合作项目名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.cooperationProjectName"
                                               name="cooperationProjectName" required="true" readonly/>
                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.showSelectDelegationModel()"
                                                ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label ">内部合作项目号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.cooperationProjectNo" name="cooperationProjectNo"
                                           required="true" readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>委托方</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.delegationDeptName"
                                               name="delegationDeptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal('delegation')" disabled> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required"><strong>协作方</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.cooperationDeptName"
                                               name="cooperationDeptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal('cooperation')" disabled> <i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required ">内部合作项目类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.cooperationProjectType"
                                           name="cooperationProjectType" required="true" readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required ">内部合作合同名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractName"
                                           name="contractName" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required ">内部合作合同类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同类型'}:true"
                                            ng-model="vm.item.contractType" required="true" class="form-control"
                                            ng-disabled="!(processInstance.firstTask)"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">签订时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="delegationTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.signTime" name="signTime" required="true" disabled >
                                        <span class="input-group-btn">
                                           <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                       </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">内部合作合同号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractNo" name="contractNo" required="true"    readonly/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.getInteriorContractNo();"
                                                    ng-disabled="!processInstance.firstTask"> <i class="fa fa-refresh"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required ">收费金额(万元)</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.chargeMoney"
                                           name="chargeMoney" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">收费日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="chargeMoneyTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.chargeMoneyTime" name="chargeMoneyTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                           <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                       </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">协作金额(万元)</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.cooperationMoney"
                                           name="cooperationMoney" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">协作内容及要求</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control" rows="3" ng-model="vm.item.cooperationContent"
                                           name="cooperationContent" required="true" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">协作费用结算要求</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control" rows="3" ng-model="vm.item.cooperationMoneyClose"
                                              name="cooperationMoneyClose" required="true" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">流程描述</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.title" name="title" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"   ng-disabled="!processInstance.firstTask"/>

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
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade draggable-modal" id="selectDelegationModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">合作经营项目</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                   <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="项目名称"
                               ng-model="vm.qProjectName"/>
                    </div>
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="项目号"
                               ng-model="vm.qProjectNo"/>
                    </div>
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="委托方"
                               ng-model="vm.qDelegationDeptName"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th >内部合作项目名称</th>
                            <th style="width: 90px;">内部合作项目号</th>
                            <th style="width: 180px;">委托方</th>
                            <th style="width: 180px;">协作方</th>
                            <th>备注</th>
                            <th style="width: 60px">创建人</th>
                            <th style="width: 100px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.delegations |filter:{interiorProjectName:vm.qProjectName}
                        |filter:{interiorProjectNo:vm.qProjectNo}|filter:{delegationDeptName:vm.qDelegationDeptName}">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.contractId==item.id" class="cb_delegation"
                                       data-name="{{item}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.interiorProjectName" class="data_title"  ng-click="vm.show(item.id);"></td>
                            <td ng-bind="item.interiorProjectNo"></td>
                            <td ng-bind="item.delegationDeptName"></td>
                            <td ng-bind="item.cooperationDeptName"></td>
                            <td ng-bind="item.remark"></td>
                            <td ng-bind="item.creatorName"></td>
                            <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectDelegation()">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">项目详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label required">合同名称</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.detail.contractName" name="contractName" required="true"   ng-disabled="!processInstance.firstTask||vm.detail.contractId!=0"/>
                                    <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.showContractLibraryModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">合同号</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.contractNo" ng-disabled="!processInstance.firstTask||vm.detail.contractId!=0" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">项目名称</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.projectName" ng-disabled="!processInstance.firstTask||vm.detail.contractId!=0" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">项目号</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.projectNo" ng-disabled="!processInstance.firstTask||vm.detail.contractId!=0" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">合同客户名称</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.customerName" ng-disabled="!processInstance.firstTask||vm.detail.contractId!=0" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">签订时间</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.signTime" ng-disabled="!processInstance.firstTask||vm.detail.contractId!=0"  >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">合同额</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.contractMoney" ng-disabled="!processInstance.firstTask||vm.detail.contractId!=0" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">协作单位名称</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.assistOutDept" ng-disabled="!processInstance.firstTask" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" >应付款</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control" ng-model="vm.detail.shouldPayMoney" ng-disabled="!processInstance.firstTask">
                                    <span class="input-group-addon">万元</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" >实付款</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control" ng-model="vm.detail.realPayMoney" ng-disabled="!processInstance.firstTask">
                                    <span class="input-group-addon">万元</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.remark" ng-disabled="!processInstance.firstTask">
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

<jsp:include page="../print/print.jsp"/>