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
            <span>发票应收款催款</span>
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
            <i class="icon-note"></i>发票应收款催款
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
                                <label class="col-md-2 control-label required">发票号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.invoiceNo" name="invoiceNo" disabled/>
                                        <span class="input-group-btn" ng-disabled="!processInstance.firstTask">
                                            <button class="btn default" type="button" ng-click="vm.showInvoice();"  ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">开票金额</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.invoiceMoney" name="invoiceMoney" required="true"   readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">发票已领金额（万元）</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.invoiceGetMoney" name="invoiceGetMoney" required="true"   readonly/>
                                </div>
                                <label class="col-md-2 control-label required">发票正在申领金额（万元）</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.invoiceGetMoneyIng" name="invoiceGetMoneyIng" required="true"   readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同名称</label>
                                <div class="col-md-4">
                                  <input type="text"  class="form-control" ng-model="vm.item.contractName" name="contractName" required="true"   readonly/>
                                 </div>
                                <label class="col-md-2 control-label required">合同号</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.contractNo" name="contractNo" required="true"   readonly/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">法人单位</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.legalDept" name="legalDept" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">所属部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" readonly><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">催收责任人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.remindReceiveManName" name="remindReceiveManName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('remindReceiveMan');" readonly><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.remark" name="remark" required="true"  ng-disabled="!processInstance.firstTask"/>
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

<div class="modal fade draggable-modal" id="showInvoiceModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg" >
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">选择要催收的发票:</h4>
                <span style="size: 13px;">金额：(万元)</span>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qCustomer"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px">
                    <table  style="margin-top: 10px" class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>合同名称</th>
                            <th>合同号</th>
                            <th>发票号</th>
                            <th>发票额</th>
                            <th>发票已领额</th>
                            <th>发票正在申领额</th>
                            <th style="width: 60px">创建人</th>
                            <th style="width: 100px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.listInvoice">
                            <td>
                                <input type="checkbox"  class="cb_invoice" ng-checked="item.id==vm.item.invoiceId"
                                       data-name="{{item}}"  style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.contractName" class="data_title"  ng-click="vm.show(item.id);"></td>
                            <td ng-bind="item.contractNo"></td>
                            <td ng-bind="item.invoiceNo" ></td>
                            <td ng-bind="item.invoiceMoney"></td>
                            <td ng-bind="item.invoiceGetMoney"></td>
                            <td ng-bind="item.invoiceGetMoneyIng"></td>
                            <td ng-bind="item.creatorName"></td>
                            <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveInvoice();">确认</button>
            </div>
        </div>
    </div>
</div>


