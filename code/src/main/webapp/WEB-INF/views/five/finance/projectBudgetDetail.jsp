<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>财务管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>项目预算</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.contractName"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>项目预算
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
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
                                <label class="col-md-2 control-label required">合同名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractName" name="contractName" required="true"   readonly/>
                                        <span class="input-group-btn" >
                                 <button class="btn default" type="button" ng-click="vm.showContractLibraryModal()" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                             </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo" name="contractNo" required="true" readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName" name="projectName" required="true" readonly/>
                                </div>
                                <label class="col-md-2 control-label required">项目性质</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectType" name="projectType" required="true" readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目总预算</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.budgetTotalMoney" name="budgetTotalMoney" required="true" readonly/>
                                </div>
                                <label class="col-md-2 control-label required">利润率</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.profitProportion" name="profitProportion" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">责任成本</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.dutyCost" name="dutyCost" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">责任成本占比</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.dutyCostProportion" name="dutyCostProportion" required="true" readonly/>
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
<div class="portlet light">
    <div class="portlet-title" >
        <div class="caption">
            <i class="fa fa-file"></i> 预算明细
        </div>
        <div class="actions">
            <a id="addBtn"  class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0);"
               ng-if="processInstance.firstTask">
                <i class="fa fa-plus"></i> 新增 </a>
            <a id="updateBtn"  class="btn btn-sm btn-default" ng-click="vm.showDetailModel(1);"
               ng-if="processInstance.firstTask">
                <i class="fa fa-edit"></i> 修改所选 </a>
            <a id="removeBtn"  class="btn btn-sm btn-default" ng-click="vm.removeDetail();"
               ng-if="processInstance.firstTask">
                <i class="fa fa-remove"></i> 删除所选 </a>
            <a  id="expandAllBtn" class="btn btn-sm btn-default" ng-click="vm.expandAll();"
               ng-if="processInstance.firstTask">
                <i class="glyphicon glyphicon-chevron-down"></i> 展开/折叠 </a>
        </div>
    </div>
    <div class="portlet-body">
        <%--<table id="treeTable" class="table table-striped table-hover table-bordered  no-footer "></table>--%>
            <div id="treeTable" class="table table-striped table-hover table-bordered  no-footer "></div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade draggable-modal" id="selectLibraryModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">选择合同库中的合同</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                   <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="合同名称"
                               ng-model="vm.qContract"/>
                    </div>
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="项目名称"
                               ng-model="vm.qProject"/>
                    </div>
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="合同号"
                               ng-model="vm.qContractNo"/>
                    </div>
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="项目号"
                               ng-model="vm.qProjectNo"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>合同名称</th>
                            <th>项目名称</th>
                            <th>合同号</th>
                            <th>项目号</th>
                            <th style="width: 80px">合同额（万元）</th>
                            <th style="width: 80px">开具发票额（万元）</th>
                            <th style="width: 80px">合同收入额（万元）</th>
                            <th style="width: 120px">签约日期</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.librarys |filter:{contractName:vm.qContract}|filter:{projectName:vm.qProject}|filter:{contractNo:vm.qContractNo}|filter:{projectNo:vm.qProjectNo}">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.contractLibraryId==item.id" class="cb_library"
                                       data-name="{{item}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.contractName"><strong ></strong></td>
                            <td ng-bind="item.projectName"><strong ></strong></td>
                            <td ng-bind="item.contractNo" ></td>
                            <td ng-bind="item.projectNo" ></td>
                            <td ng-bind="item.contractMoney " ></td>
                            <td ng-bind="item.contractInvoiceMoney |currency : '￥'" ></td>
                            <td ng-bind="item.contractIncomeMoney |currency : '￥'" ></td>
                            <td ng-bind="item.signTime"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectModel()">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">项目预算详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">预算类型</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.typeName" placeholder="" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">采购号</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.purchaseNo" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">上级类型</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.detail.parentName"
                                           placeholder="" disabled="disabled">
                                    <span class="input-group-btn" ><button class="btn default" type="button" ng-click="vm.showParentTree(vm.detail)" ng-disabled="!processInstance.firstTask"><i class="fa fa-asterisk"></i></button></span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">预算金额</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.budgetMoney" ng-disabled="vm.detail.isParent">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">预算占比</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.budgetProportion" disabled>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">排序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.detail.seq"
                                       placeholder="请输入整数">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.remark">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">创建人</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.item.creatorName" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">创建时间</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control"
                                       value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
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

<div class="modal fade draggable-modal" id="detailParentTreeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择父级</h4>
            </div>
            <div class="modal-body">
                <div id="detail_parent_tree"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.chooseDetailParent();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

