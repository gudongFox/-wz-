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
            <a ui-sref="oa.functionEntrance({moduleId:80})">收入管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">收入确认</span>
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
            <i class="icon-note"></i><span ng-bind="tableName">收入确认</span>
<%--              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>--%>
        </div>
        <div class="actions">
            <div ng-if="vm.item.processInstanceId!=''">
              <jsp:include page="../../common/common-actAction.jsp"/>
            </div>
            <div ng-if="vm.item.processInstanceId==''">
              <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                  <i class="fa fa-refresh"></i> 刷新 </a>
              <a href="javascript:;" class="btn btn-sm btn-default" ng-show="rightData.selectOpts.indexOf('修改')>=0||vm.item.refund"
                 ng-click="vm.save();" >
                  <i class="fa fa-save"></i> 保存 </a>
             <%-- <a href="javascript:;" class="btn btn-sm btn-default" ng-show="!vm.item.processEnd"
                 ng-click="vm.getNotarizeIncome(vm.item.id,vm.item.creator);" >
                  <i class="glyphicon glyphicon-ok"></i> 确认收入 </a>
              <a href="javascript:;" class="btn btn-sm btn-default" ng-show="vm.item.processEnd&&vm.item.incomeIds!=''"
                 ng-click="vm.getNotarizeIncome(vm.item.id,vm.item.creator);" >
                  <i class="glyphicon glyphicon-remove"></i> 取消确认 </a>--%>
              <a href="javascript:;" class="btn btn-sm btn-default"
                 ng-click="back();">
                  <i class="fa fa-arrow-left"></i> 返回 </a>
            </div>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
               <%-- <li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>--%>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">收入来源账户</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.sourceAccount" name="sourceAccount"  readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showIncomeModel();" ng-disabled="true"><i class="fa fa-cog" ></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">收入来源账户名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.fiveFinanceIncome.sourceName" name="targetAccount" required="true"   readonly/>
                                </div>
                            </div>
                           <%-- <div class="form-group">
                                <label class="col-md-2 control-label required">收入已认领金额(元)</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.incomeConfirmMoney" name="incomeConfirmMoney" required="true"  readonly/>
                                </div>

                                <label class="col-md-2 control-label required">收入正在认领金额(元)</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.incomeConfirmMoneyIng" name="incomeConfirmMoneyIng" required="true"   readonly/>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">收入接收账户</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.targetAccount" name="targetAccount" required="true"   readonly/>
                                </div>
                                <label class="col-md-2 control-label required">收入总额(元)</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.money" name="money" required="true"  readonly/>
                                </div>
<%--
                                <label class="col-md-2 control-label required">收入剩余金额(元)</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.incomeMoneyRemain" name="incomeConfirmMoneyIng" required="true"   readonly/>
                                </div>--%>
                            </div>
                            <div class="form-group">
                                <label  class="col-md-2 control-label">收入类型</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.type" name="type"  readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showTypeModel();" disabled><i class="fa fa-cog" ></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label  class="col-md-2 control-label">收款时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="incomeTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.incomeTime" name="incomeTime" required="true" ng-disabled="true">
                                        <span class="input-group-btn" >
												<button class="btn default" type="button" ng-disabled="true"><i class="fa fa-calendar"  ></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否退款</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in commonCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.refund" class="form-control form-control-sm"></select>
                                </div>
                                <label class="col-md-2 control-label ">收入备注</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.incomeRemark" name="incomeRemark" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-1 control-label" style="color: red">结转管控:</label>
                                <label class="col-md-1 control-label">认领部门名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="vm.item.processEnd"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label ">本年管控比</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.managerRate" name="managerRate"    ng-disabled="vm.item.processEnd"/>
                                </div>
                            </div>
                           <%-- <div class="form-group">
                                <label class="col-md-2 control-label required">本次认领金额（元）</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.money" name="money" required="true" ng-change="vm.showBigNum()" ng-disabled="vm.item.processEnd"/>
                                </div>

                                <label class="col-md-2 control-label required">金额大写</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.moneyMax" name="moneyMax" required="true"   readonly/>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">目标金额（元）</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.designTargetMoney" name="designTargetMoney"    ng-disabled="vm.item.processEnd"/>
                                </div>
                                <label class="col-md-2 control-label ">完成金额（元）</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.designSucessMoney" name="designSucessMoney"   ng-disabled="vm.item.processEnd"/>
                                </div>
                            </div>
                          <%--  <div class="form-group">
                                <label class="col-md-2 control-label required">总承包目标额（万元）</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.totalTargetMoney" name="totalTargetMoney" required="true" ng-change="vm.showBigNum();"   ng-disabled="vm.item.processEnd"/>
                                </div>
                                <label class="col-md-2 control-label required">总承包完成额（万元）</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.totalSucesstMoney" name="totalSucesstMoney" required="true" ng-change="vm.showBigNum();"   ng-disabled="vm.item.processEnd"/>
                                </div>
                            </div>--%>
<%--                            <div class="form-group">
                                <label class="col-md-2 control-label required">设计咨询(超比)</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.designAskRate" name="designAskRate" required="true" ng-change="vm.showBigNum();"   ng-disabled="vm.item.processEnd"/>
                                </div>
                                <label class="col-md-2 control-label required">总承包(超比)</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.totalRate" name="totalRate" required="true" ng-change="vm.showBigNum();"   ng-disabled="vm.item.processEnd"/>
                                </div>
                            </div>--%>

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
               <%-- <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>--%>
            </div>
        </div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 发票信息
            <span style="color:red;font-size: 12px">金额：万元</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showInvoiceModel();"
               ng-if="!vm.item.chooseInvoiceOrContract&&vm.item.creator==user.enLogin">
                <i class="fa fa-plus"></i> 选择发票 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addInvoice();"
               ng-if="!vm.item.chooseInvoiceOrContract&&vm.item.creator==user.enLogin">
                <i class="fa fa-plus"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>发票号</th>
                    <th>合同名称</th>
                    <th style="width: 120px">合同号</th>
                    <th>法人单位</th>
                    <th style="width: 100px">开票金额</th>
                    <th style="width: 100px">已收款金额</th>
                    <th style="width: 120px">发票来源</th>
                    <th style="width: 60px">创建人</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 150px">流程状态</th>
                    <th style="width: 55px">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.invoices">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="item.invoiceNo" class="data_title"  ng-click="vm.showInvoice(item.id);"></td>
                    <td ng-bind="item.contractName" ></td>
                    <td ng-bind="item.contractNo"></td>
                    <td ng-bind="item.legalDept" ></td>
                    <td ng-bind="item.invoiceMoney"></td>
                    <td ng-bind="item.invoiceGetMoney"></td>
                    <td ng-if="item.incomeConfirmId!=0" style="color: red">当前申请创建</td>
                    <td ng-if="item.incomeConfirmId==0" style="color: green">选择发票</td>
                    <td ng-bind="item.creatorName"></td>
                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                    <td>
                        <span ng-bind="item.processName"></span>
                    </td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showInvoice(item.id);" title="详情"></i>
                        <i class="fa fa-trash-o margin-right-5" ng-if="item.incomeId==0" ng-click="vm.removeInvoice(item.id);" title="删除"
                           ng-show="item.creator==user.userLogin"></i>
                        <i class="fa fa-trash-o margin-right-5" ng-if="item.incomeId!=0" ng-click="vm.removeReplenish(item.id);" title="补领删除"
                           ng-show="item.creator==user.userLogin&&!item.processEnd"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 合同信息
            <span style="color:red;font-size: 12px">金额：万元</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addContractIncome();"
               ng-if="!vm.item.chooseInvoiceOrContract&&vm.item.creator==user.enLogin">
                <i class="fa fa-plus"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>项目名称</th>
                    <th>合同名称</th>
                    <th style="width: 120px">合同号</th>
                    <th style="width: 120px">发票号</th>
                    <th class="hidden-md hidden-sm">合同金额</th>
                    <th>本次收入金额</th>
                    <th style="width: 100px">合同已领金额</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 120px">流程状态</th>
                    <th style="width: 55px">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.incomes">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="item.projectName"  class="data_title"  ng-click="vm.showContractIncome(item.id);"></td>
                    <td ng-bind="item.contractName"  ></td>
                    <td ng-bind="item.contractNo" class="hidden-md hidden-sm"></td>
                    <td ng-if="item.invoiceId==0&&item.invoiceReplenishId==0" style="color: red" ng-click="vm.replenishInvoice(item.id)">点击补领发票</td>
                    <td ng-if="item.invoiceId==0&&item.invoiceReplenishId!=0" style="color: grey" ng-click="vm.showInvoice(item.invoiceReplenishId)">已补领发票</td>
                    <td ng-if="item.invoiceId!=0" style="color: green" ng-click="vm.showInvoice(item.invoiceId)" ng-bind="item.invoiceNo"></td>
                    <td ng-bind="item.contractMoney" class="hidden-md hidden-sm"></td>
                    <td ng-bind="item.incomeMoney"></td>
                    <td ng-bind="item.contractIncomeMoney"></td>
                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                    <td>
                        <span ng-bind="item.processName"></span>
                    </td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showContractIncome(item.id);" title="详情"></i>
                        <i class="fa fa-trash-o margin-right-5" ng-click="vm.removeContractIncome(item.id);" title="删除"
                           ng-show="item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade draggable-modal" id="selectIncomeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">请选择收入 （金额：万元）</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qCustomer"/>
                    </div>
                </div>
                <table  style="margin-top: 10px" class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 35px;">#</th>
                        <th>收入备注</th>
                        <th>收入类型</th>
                        <th class="hidden-md hidden-sm">对方账户名称</th>
                        <th style="width: 100px">收入金额</th>
                        <th style="width: 100px">已认领金额</th>
                        <th style="width: 110px">正在认领金额</th>
                        <th style="width: 100px;">创建时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.incomes">
                        <td>
                            <input type="checkbox" ng-checked="vm.item.incomeId==item.id" class="cb_income"
                                   data-name="{{item}}" style="width: 15px;height: 15px;"/>
                        </td>
                        <td ng-bind="item.remark" class="data_title" ><strong ></strong>
                        <td ng-bind="item.type" ><strong ></strong>
                        </td>
                        <td ng-bind="item.sourceName" class="hidden-md hidden-sm"></td>
                        <td ng-bind="item.money"></td>
                        <td ng-bind="item.incomeConfirmMoney"></td>
                        <td ng-bind="item.incomeConfirmMoneyIng"></td>
                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                    </tr>
                    </tbody>
                </table>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectIncome();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="selectInvoiceModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg" style="width: 70%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">请选择已申请发票</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qCustomer"/>
                    </div>
                </div>
                <table  style="margin-top: 10px" class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 35px;">#</th>
                        <th>合同名称</th>
                        <th>合同号</th>
                        <th>发票号</th>
                        <th>发票额(万元)</th>
                        <th>发票已领额(万元)</th>
                      <%--  <th>发票正在申领额(万元)</th>--%>
                        <th style="width: 120px">状态</th>
                        <th style="width: 60px">创建人</th>
                        <th style="width: 100px;">创建时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.selectInvoices">
                        <td>
                            <input type="checkbox"  class="cb_invoice"
                                   data-id="{{item.id}}"  style="width: 15px;height: 15px;"/>
                        </td>
                        <td ng-bind="item.contractName" class="data_title"  ng-click="vm.show(item.id);"></td>
                        <td ng-bind="item.contractNo"></td>
                        <td ng-bind="item.invoiceNo" ></td>
                        <td ng-bind="item.invoiceMoney"></td>
                        <td ng-bind="item.invoiceGetMoney"></td>
                        <%--<td ng-bind="item.invoiceGetMoneyIng"></td>--%>
                        <td ng-if="item.incomeConfirmIds==''" style="color:red;">未认领</td>
                        <td ng-if="item.incomeConfirmIds!=''&&item.incomeConfirmId!=vm.item.id" style="color:green;">其他申请认领</td>
                        <td ng-if="item.incomeConfirmIds!=''&&item.incomeConfirmId==vm.item.id" style="color:green;">当前申请认领</td>
                        <td ng-bind="item.creatorName"></td>
                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                    </tr>
                    </tbody>
                </table>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectInvoice();">确认</button>
            </div>
        </div>
    </div>
</div>



