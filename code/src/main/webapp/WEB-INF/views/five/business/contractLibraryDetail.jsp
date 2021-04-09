<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>经营管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">合同库</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">合同库</span>
            <small style="font-size: 50%;" ng-bind="vm.item.projectName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="rightData.selectOpts.indexOf('修改')>=0" style="font-size: 14px;line-height: 1.6"
               ng-click="vm.save();" >
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6"><%-- ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>
                <i class="fa fa-print"></i> 打印 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        合同信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_6" data-toggle="tab" aria-expanded="false">
                        客户信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_7" data-toggle="tab" aria-expanded="false">
                        评审信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_8" data-toggle="tab" aria-expanded="false">
                        财务相关 </a>
                </li>
                <li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        补充情况 </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        分包情况 </a>
                </li>
                <li class="">
                    <a href="#tab_15_4" data-toggle="tab" aria-expanded="false">
                        发票情况 </a>
                </li>
                <li class="">
                    <a href="#tab_15_5" data-toggle="tab" aria-expanded="false">
                        收入情况 </a>
                </li>

            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">备案项目名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.recordProjectName"
                                               name="recordProjectName" required="true"  ng-disabled="rightData.selectOpts.indexOf('修改')<0"
                                               />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showRecordModal();"
                                                    ng-disabled="rightData.selectOpts.indexOf('修改')<0"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="true">请点击后方按钮选择</span>
                                </div>

                                <label class="col-md-2 control-label required">项目号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo" name="projectNo" required="true"    ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           name="projectName" required="true" ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                                <label class="col-md-2 control-label required">是否为补充合同</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.main" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                                </div>

                            </div>
                            <div class="form-group" ng-if="vm.item.main">
                                <label class="col-md-2 control-label required" >主合同名称</label>
                                <div class="col-md-4" >
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.mainContractLibraryName"
                                               name="mainContractLibraryName" required="true"
                                               disabled/>
                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.showContractLibraryModal()"
                                                ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i
                                                class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label required" >主合同号</label>
                                <div class="col-md-4" >
                                    <input type="text" class="form-control" ng-model="vm.item.mainContractLibraryNo" name="mainContractLibraryNo" required="true"    disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">合同名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractName" name="contractName" required="true"    ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                                <label class="col-md-2 control-label required" style="font-weight: bold;">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo" name="contractNo" required="true"    ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">承接部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.opt='deptId')" ng-disabled="rightData.selectOpts.indexOf('修改')<0"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">投资额（万元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.investMoney" name="investMoney" required="true"    ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同额（万元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractMoney" name="contractMoney" required="true"    ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">投资来源</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'投资来源'}:true"
                                            ng-model="vm.item.investSource" class="form-control"
                                            ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select> </div>
                                <label class="col-md-2 control-label required">项目性质</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'五洲项目类型'}:true"
                                            ng-model="vm.item.projectNature" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0"
                                            ></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同类型'}:true"
                                            ng-model="vm.item.contractType" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0"
                                           ></select>
                                </div>
                                <label class="col-md-2 control-label required">签约日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="signTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.signTime" required="true"
                                               ng-disabled="rightData.selectOpts.indexOf('修改')<0">
                                        <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                            ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i
                                            class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">建设规模</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionScale" name="constructionScale"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                                <label class="col-md-2 control-label required">建设地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionArea" name="constructionArea" required="true"    ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                            <label class="col-md-2 control-label required">是否有保函</label>
                            <div class="col-md-4">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否开具保函'}:true"
                                        ng-model="vm.item.backletter" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                            </div>
                            <label class="col-md-2 control-label" ng-if="vm.item.backletter">保函金额(万元)</label>
                            <div class="col-md-4">
                                <input  ng-if="vm.item.backletter" ng-model="vm.item.backletterMoney" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0" >
                            </div>
                        </div>
                            <div class="form-group">
                            <label class="col-md-2 control-label" >合同收入(万元)</label>
                            <div class="col-md-4">
                                <input   ng-model="vm.item.contractIncomeMoney" class="form-control" ng-disabled="true" >
                            </div>
                            <label class="col-md-2 control-label" >已开具发票额(万元)</label>
                            <div class="col-md-4">
                                <input   ng-model="vm.item.contractInvoiceMoney" class="form-control" ng-disabled="true" >
                            </div>
                        </div>
                            <div class="form-group">
                            <label class="col-md-2 control-label" >应收款(万元)</label>
                            <div class="col-md-4">
                                <input  ng-model="vm.item.shouldContractMoney" class="form-control" ng-disabled="true" >
                            </div>
                            <label class="col-md-2 control-label" >预收款(万元)</label>
                            <div class="col-md-4">
                                <input  ng-model="vm.item.preContractMoney" class="form-control" ng-disabled="true" >
                            </div>
                        </div>
                            <div class="form-group" ng-if="vm.item.stampTaxId!=0">
                                <label class="col-md-2 control-label" >印花税-税目</label>
                                <div class="col-md-4">
                                    <input   ng-model="vm.item.fiveFinanceStampTaxDetail.taxType" class="form-control" ng-disabled="true" >
                                </div>
                                <label class="col-md-2 control-label" >印花税-税额(万元)</label>
                                <div class="col-md-4">
                                    <input   ng-model="vm.item.fiveFinanceStampTaxDetail.stampTaxMoney" class="form-control" ng-disabled="true" >
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <input ng-model="vm.item.remark" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0">
                                </div>
                        </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane " id="tab_15_2"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div class="table-scrollable" style="max-height: 400px">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>项目名称</th>
                                <th style="width: 150px">合同号</th>
                                <th style="width: 150px">项目号</th>
                                <th style="width: 90px">合同额（万元）</th>
                                <th >客户名称</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 30px;">补充合同标识</th>
                                <th style="width: 30px;">补录标识</th>
                                <th style="width: 30px;">对内分包标识</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.mainReviews">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.projectName"  class="data_title"  ng-click="vm.jumpMainReview(item.id);"><strong ></strong>
                                </td>
                                <td ng-bind="item.contractNo" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.projectNo" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.investMoney|currency : '￥'" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.customerName" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td class="text-center">
                                    <span class="label label-sm label-success"  ng-if="item.main">是</span>
                                    <span class="label label-sm label-default" ng-if="!item.main">否</span>
                                </td>
                                <td class="text-center">
                                    <span class="label label-sm label-success"  ng-if="item.preContractId!=0" ng-click="vm.showPreContract(item.preContractId);">是</span>
                                    <span class="label label-sm label-default" ng-if="item.preContractId==0">否</span>
                                </td>
                                <td class="text-center">
                                    <span class="label label-sm label-success"  ng-if="item.subpackageId!=0" ng-click="vm.showSubpackage(item.subpackageId);">是</span>
                                    <span class="label label-sm label-default" ng-if="item.subpackageId==0">否</span>
                                </td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.jumpMainReview(item.id);" title="详情"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="tab-pane " id="tab_15_3"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div class="table-scrollable" style="max-height: 400px">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>分包/采购合同名称</th>
                                <th>分包/采购合同号</th>
                                <th class="hidden-md hidden-sm">供方名称</th>
                                <th>发包部门名称</th>
                                <th>主合同名称</th>
                                <th style="width: 100px">主合同金额(万元)</th>
                                <th style="width: 60px">经办人</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.subpackages">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.subContractName"  class="data_title"  ng-click="vm.jumpSubpackage(item.id);"></td>
                                <td ng-bind="item.subContractNo"  class="data_title"  ng-click="vm.jumpSubpackage(item.id);"></td>
                                <td ng-bind="item.supplierName" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.contractName"></td>
                                <td ng-bind="item.contractMoney"></td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.jumpSubpackage(item.id);" title="详情"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="tab-pane " id="tab_15_4"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div class="table-scrollable" style="max-height: 400px">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>发票号</th>
                                <th>发票抬头</th>
                                <th>开票时间</th>
                                <th>发票金额（万元）</th>
                                <th>发票已收入金额（万元）</th>
                                <th style="width: 60px">创建人</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 60px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.invoices">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.invoiceNo"  class="data_title"  ng-click="vm.jumpInvoice(item.id);"></td>
                                <td ng-bind="item.invoiceHeadName" ></td>
                                <td ng-bind="item.invoiceTime"></td>
                                <td ng-bind="item.invoiceMoney"></td>
                                <td ng-bind="item.invoiceGetMoney"></td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.jumpInvoice(item.id);" title="详情"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="tab-pane " id="tab_15_5"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div class="table-scrollable" style="max-height: 400px">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>收入备注</th>
                                <th style="width: 100px">收入金额（万元）</th>
                                <th style="width: 100px" >合同已领金额（万元）</th>
                                <th style="width: 100px" >发票信息</th>
                                <th style="width: 60px">创建人</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 60px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.incomes">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.remark"  class="data_title"  ng-click="vm.jumpIncome(item.id);"></td>
                                <td ng-bind="item.incomeMoney" ></td>
                                <td ng-bind="item.contractIncomeMoney"></td>
                                <td ng-if="item.invoiceId==0" style="color: red" >无发票</td>
                                <td ng-if="item.invoiceId!=0" style="color: green" ng-click="vm.showInvoice(item.invoiceId)" ng-bind="item.invoiceNo"></td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.jumpIncome(item.id);" title="详情"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="tab-pane " id="tab_15_6"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">客户名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.customerName" name="customerName"  required="true" ng-disabled="user.userLogin!=vm.item.creator" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectCustomer();" ng-disabled="user.userLogin!=vm.item.creator"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">客户编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.item.customerCode" ng-disabled="user.userLogin!=vm.item.creator"
                                           required="true" maxlength="50"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">客户地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="" ng-model="vm.item.customerAddress"  ng-disabled="user.userLogin!=vm.item.creator"/>
                                </div>
                                <label class="col-md-2 control-label required">联系人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.linkMan" name="linkMan" required="true" ng-disabled="user.userLogin!=vm.item.creator" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系人职务</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.linkTitle" name="linkTitle" required="true"   ng-disabled="user.userLogin!=vm.item.creator"/>
                                </div>
                                <label class="col-md-2 control-label required">联系电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.linkTel" name="linkTel" required="true"   ng-disabled="user.userLogin!=vm.item.creator"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane " id="tab_15_7"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">评审级别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'评审级别'}:true"
                                            ng-model="vm.item.reviewLevel" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                                </div>
                                <label class="col-md-2 control-label " ng-if="vm.item.reviewLevel=='公司级'">参加评审人员</label>
                                <div class="col-md-4" ng-if="vm.item.reviewLevel=='公司级'">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.reviewUserName" name="reviewUserName"   ng-disabled="rightData.selectOpts.indexOf('修改')<0" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('reviewUser');" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span class="help-block" ng-show="true">经营发展部人员（合同）选择评审人员</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">总设计师</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.totalDesignerName" name="totalDesignerName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('totalDesigner');" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">项目经理</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectManagerName" name="projectManagerName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('projectManager');" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同分管副总</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.businessChargeLeaderName" name="businessChargeLeaderName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('businessChargeLeader');" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">项目主管院长</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectChargeManName" name="projectChargeManName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('projectCharge');" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">外贸标记</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'外贸标记'}:true"
                                            ng-model="vm.item.foreignTradeMark" class="form-control"
                                            ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                                </div>
                                <label class="col-md-2 control-label required">民用标记</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'民用标记'}:true"
                                            ng-model="vm.item.civilMark" class="form-control"
                                            ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">军品标记</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'军品标记'}:true"
                                            ng-model="vm.item.militaryMark" class="form-control"
                                            ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                                </div>
                                <label class="col-md-2 control-label required">是否含合同附件</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.attach" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                                </div>

                            </div>
                            <%--<div class="form-group">
                                <label class="col-md-2 control-label required">项目专业分类 一级</label>
                                <div class="col-md-4">
                                        <input type="text" class="form-control" ng-model="vm.item.projectMajorTypeFirst" name="projectMajorTypeSecond"
                                               ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label required">项目专业分类 二级</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectMajorTypeSecond" name="projectMajorTypeSecond"
                                           ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目专业分类 三级</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectMajorTypeThird" name="projectMajorTypeThird"
                                           ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>--%>
                        </div>
                    </form>
                </div>
                <div class="tab-pane " id="tab_15_8"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form5">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>签约日期</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.fiveBusinessContractReview.signTime" required="true"
                                               ng-disabled="user.userLogin!=vm.item.creator">
                                        <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                            ng-disabled="user.userLogin!=vm.item.creator"><i
                                            class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">合同盖章页扫描附件</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.fiveBusinessContractReview.contractAttachUrl"
                                               name="contractAttachUrl" maxlength="100" disabled="disabled"/>
                                        <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUploadHead" class="btn blue fileinput-button btn-sm" ng-disabled="user.userLogin!=vm.item.creator">
                                          <i class="fa fa-cloud-upload" style="height:15px "></i>
                                        <span>上传</span>
                                             <input type="file" name="multipartFile" id="contractAttachUrl" accept="*" multiple="multiple"/>
                                   </span>
                                        <a ng-href="{{vm.item.fiveBusinessContractReview.contractAttachUrl}}" target="_blank">
                                        <span id="btnDownloadHead" class="btn green fileoutput-button btn-sm">
                                     <i class="fa fa-cloud-download"
                                        title="下载"></i>
                                            <span>下载</span>
                                        </span>
                                            </a>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">合同额(万元)</label>
                                <div class="col-md-4">
                                    <input ng-model="vm.item.fiveBusinessContractReview.contractMoney" class="form-control" ng-disabled="user.userLogin!=vm.item.creator">
                                </div>
                                <label class="col-md-2 control-label required" style="color: red">税目</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同印花税税目'}:true"
                                            ng-model="vm.item.fiveBusinessContractReview.taxType" class="form-control" ng-change="vm.save()"
                                            ng-disabled="user.userLogin!=vm.item.creator"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label" >税率</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.fiveBusinessContractReview.taxNum"  ng-disabled="user.userLogin!=vm.item.creator">
                                        <span class="input-group-addon">%</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label" >印花税额(万元)</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <span class="input-group-addon">￥</span>
                                        <input type="text" class="form-control" ng-model="vm.item.fiveBusinessContractReview.stampTaxMoney"  readonly>
                                        <span class="input-group-addon">万元</span>
                                    </div>
                                    <span style=" margin-left:40px;font-size: 13px;color: red" >{{(vm.item.fiveBusinessContractReview.stampTaxMoney*10000).toFixed(2)+'元'}}</span>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                </div>
            </div>
    </div>
</div>
<%--<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-thumb-tack"></i> 客户信息
        </div>
        <div  class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="vm.addCustomer(vm.item.id);" >
                <i class="fa fa-save"></i> 保存客户信息 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="vm.showCustomer(vm.item.customerId);" >
                <i class="fa fa-save"></i> 完善客户信息 </a>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label required" style="font-weight: bold;">客户名称</label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.item.customerName" name="customerName"  required="true" ng-disabled="user.userLogin!=vm.item.creator" />
                            <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectCustomer();" ng-disabled="user.userLogin!=vm.item.creator"><i class="fa fa-user"></i></button>
                                        </span>
                        </div>
                    </div>
                    <label class="col-md-2 control-label required">客户编号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.item.customerCode" ng-disabled="user.userLogin!=vm.item.creator"
                               required="true" maxlength="50"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">客户地址</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="" ng-model="vm.item.customerAddress"  ng-disabled="user.userLogin!=vm.item.creator"/>
                    </div>
                    <label class="col-md-2 control-label required">联系人</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkMan" name="linkMan" required="true" ng-disabled="user.userLogin!=vm.item.creator" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">联系人职务</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkTitle" name="linkTitle" required="true"   ng-disabled="user.userLogin!=vm.item.creator"/>
                    </div>
                    <label class="col-md-2 control-label required">联系电话</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkTel" name="linkTel" required="true"   ng-disabled="user.userLogin!=vm.item.creator"/>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>--%>


<div class="modal fade" id="selectCustomerModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">客户名称</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qCustomer"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>客户名称</th>
                            <th>客户编号</th>
                            <th>客户地址</th>
                            <th>录入部门</th>
                            <th>联系人</th>
                            <th>联系电话</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="c in vm.customers|filter:{name:vm.qCustomer}">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.customerName==c.name" class="cb_customer" data-name="{{c}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="c.name"></td>
                            <td ng-bind="c.code"></td>
                            <td ng-bind="c.address"></td>
                            <td ng-bind="c.deptName"></td>
                            <td ng-bind="c.linkMan"></td>
                            <td ng-bind="c.linkTel"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectCustomer();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="selectRecordModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4>选择项目信息备案</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="项目名称"
                               ng-model="vm.qProject"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>项目名称</th>
                            <th style="width: 80px;">项目类型</th>
                            <th>客户名称</th>
                            <th>所属部门</th>
                            <th style="width: 110px;">投资额(万元)</th>
                            <th>参与人</th>
                            <th style="width: 40px;">是否已录入合同</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="record in vm.listRecord|filter:{projectName:vm.qProject}" >
                            <td>
                                <input type="checkbox" ng-checked="vm.item.recordId==record.id" class="cb_record"
                                       data-name="{{record}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="record.projectName"  class="data_title"  ng-click="vm.show(item.id);"></td>
                            <td ng-bind="record.projectType"></td>
                            <td ng-bind="record.customerName"></td>
                            <td ng-bind="record.deptName"></td>
                            <td ng-bind="record.projectInvest|currency : '￥'"></td>
                            <td ng-bind="record.businessUserName"></td>
                            <td class="text-center">
                                <span class="label label-sm label-success" style="cursor: pointer;" ng-if="record.contractReviewId!=0">是</span>
                                <span class="label label-sm label-default" ng-if="record.contractReviewId==0">否</span>
                            </td>
                        </tr>

                        </tbody>
                    </table>
                </div>


            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectRecordModel()">确认</button>
            </div>
        </div>
    </div>

</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>
