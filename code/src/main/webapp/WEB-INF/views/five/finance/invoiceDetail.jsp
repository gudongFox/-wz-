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
            <span>发票申请</span>
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
            <i class="icon-note"></i>发票申请
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
                                <label class="col-md-2 control-label required">法人单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.legalDept" name="legalDept" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">发票号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.invoiceNo" name="invoiceNo" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请人姓名</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.applyManName" name="applyManName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('applyMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label required">电话</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.userTel" name="userTel" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">所属部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>

                                <label  class="col-md-2 control-label">开票日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.invoiceTime" name="invoiceTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同单位名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractCustomer" name="contractCustomer" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">发票抬头名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.invoiceHeadName" name="invoiceHeadName" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">发票类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'发票类型'}:true" required="true"
                                                ng-model="vm.item.localInvoiceType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">纳税人识别号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.taxNo" name="taxNo" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">对方单位地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.customerAdderss" name="customerAdderss" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">对方电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.customerTel" name="customerTel" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">开户账号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.bankAccount" name="bankAccount" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">开户银行</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.bank" name="bank" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label  class="col-md-2 control-label">预计到款时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="incomeTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.receiveTime" name="receiveTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">催收责任人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.remindReceiveManName" name="remindReceiveManName"  required="true" ng-disabled="true" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('remindReceiveMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">发票备注内容</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control" ng-model="vm.item.invoiceRemark" name="invoiceRemark"  ng-disabled="!processInstance.firstTask"/>
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
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 发票合同明细
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal form" role="form" id="detail_form1">
            <div class="form-body">
                <div class="form-group">
                     <label class="col-md-2 control-label required">合同名称</label>
                     <div class="col-md-4">
                         <div class="input-group">
                           <input type="text" class="form-control" ng-model="vm.item.contractName" name="contractName" required="true"   readonly/>
                             <span class="input-group-btn" >
                                 <button class="btn default" type="button" ng-click="vm.showContractLibraryModal()" ng-disabled="!processInstance.firstTask||vm.item.incomeId!=0"><i class="fa fa-cog"></i></button>
                             </span>
                         </div>
                     </div>
                    <label class="col-md-2 control-label required">合同号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.contractNo" name="contractNo" required="true" readonly/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">合同额(万元)</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.contractMoney" name="contractMoney" required="true" readonly/>
                    </div>
                    <label class="col-md-2 control-label required">合同已开票额(万元)</label>
                    <div class="col-md-4">
                        <input  class="form-control" ng-model="vm.item.contractGetInvoice" name="contractGetInvoice" required="true"  readonly/>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-md-2 control-label required">应收额(万元)</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.preContractMoney" name="preContractMoney" required="true" readonly/>
                    </div>
                    <label class="col-md-2 control-label required">预收额(万元)</label>
                    <div class="col-md-4">
                        <input  class="form-control" ng-model="vm.item.shouldContractMoney" name="shouldContractMoney" required="true"  readonly/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">收款性质</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.receiveType" name="receiveType" required="true" ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label required">开票金额(万元)</label>
                    <div class="col-md-4">
                        <input  class="form-control" ng-model="vm.item.invoiceMoney" name="invoiceMoney" required="true"   ng-disabled="!processInstance.firstTask||vm.item.incomeId!=0||vm.item.incomeConfirmId!=0"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label ">发票已收入金额(万元)</label>
                    <div class="col-md-4">
                        <input  class="form-control" ng-model="vm.item.invoiceGetMoney" name="invoiceGetMoney" required="true" ng-change="vm.showBigNum();"   readonly/>
                    </div>
                    <label class="col-md-2 control-label required" ng-if="vm.item.incomeId!=0">冲抵预收款(万元)</label>
                    <div class="col-md-4" ng-if="vm.item.incomeId!=0">
                        <input  class="form-control" ng-model="vm.item.chargeAgainstPreMoney" name="chargeAgainstPreMoney" required="true"   ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">销售税额</label>
                    <div class="col-md-4">
                        <input  class="form-control" ng-model="vm.item.outPutTaxMoney" name="outPutTaxMoney" required="true" ng-change="vm.showBigNum();"   ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label required">承汇款号码</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.promiseRemittanceNo" name="promiseRemittanceNo" required="true" ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
<%--                <div class="form-group">

                    <label class="col-md-2 control-label required">票据金额</label>
                    <div class="col-md-4">
                        <input type="number" class="form-control" ng-model="vm.item.noteMoney" name="noteMoney" required="true" ng-change="vm.showBigNum();"   ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>--%>
                <div class="form-group">
                    <label class="col-md-2 control-label ">收款阶段</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.incomeStage" name="incomeStage"  ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label ">收款子项</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.incomeBuild" name="incomeBuild"  ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
            </div>
        </form>
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
                         <%--   <th>项目名称</th>--%>
                            <th>合同号</th>
                         <%--   <th>项目号</th>--%>
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
                           <%-- <td ng-bind="item.projectName"><strong ></strong></td>--%>
                            <td ng-bind="item.contractNo" ></td>
                           <%-- <td ng-bind="item.projectNo" ></td>--%>
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



