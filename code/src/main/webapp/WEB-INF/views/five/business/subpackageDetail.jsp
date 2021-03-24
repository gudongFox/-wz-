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
            <span ng-bind="tableName">分包评审</span>
            <i class="fa fa-angle-right"></i>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">分包评审</span>
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
                        项目信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_4" data-toggle="tab" aria-expanded="true">
                        财务相关 </a>
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
                                <label class="col-md-2 control-label required">是否为补充合同</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.isReplenish" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.isReplenish">
                                <label class="col-md-2 control-label required" >分包合同名称</label>
                                <div class="col-md-4" >
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.mainContractLibraryName"
                                               name="mainContractLibraryName" required="true"
                                               disabled/>
                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.showSelectMainLibraryModal()"
                                                ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required" >分包合同号</label>
                                <div class="col-md-4" >
                                    <input type="text" class="form-control" ng-model="vm.item.mainContractLibraryNo" name="mainContractLibraryNo" required="true"    disabled/>
                                </div>
                            </div>
                            <div  class="form-group" ng-if="!vm.item.isReplenish">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">主合同名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractName"
                                               name="contractName" required="true" readonly>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showContractLibraryModal();"
                                                    ng-disabled="!processInstance.firstTask"> <i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required" style="font-weight: bold;">主合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo" name="contractNo"  required="true" disabled />
                                </div>
                            </div>
                            <div class="form-group" ng-if="!vm.item.isReplenish">
                                <label class="col-md-2 control-label required">主合同额（万元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractMoney" name="contractMoney" required="true"   disabled/>
                                </div>
                                <label class="col-md-2 control-label required">主合同类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同类型'}:true"
                                            ng-model="vm.item.contractType" class="form-control"
                                            disabled></select>
                                </div>
                            </div>
                            <div class="form-group" ng-if="!vm.item.isReplenish">
                                <label class="col-md-2 control-label required">项目性质</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'五洲项目类型'}:true"
                                            ng-model="vm.item.projectNature" class="form-control"
                                            disabled></select>
                                </div>
                                <label class="col-md-2 control-label required">发包部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"
                                                    ng-click="vm.showDeptModal(vm.opt='deptId')"> <i
                                                    class="fa fa-cog" ></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block" >请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required" ng-if="!vm.item.isReplenish"><strong>分包合同名称</strong></label>
                                <label class="col-md-2 control-label required" ng-if="vm.item.isReplenish"><strong>补充分包合同名称</strong></label>
                                <div class="col-md-4">
                                    <input   class="form-control" ng-model="vm.item.subContractName" name="subContractName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">分包合同类型</label>
                                <div class="col-md-4">
                                    <%--<input type="text" class="form-control" ng-model="vm.item.subContractType" name="subContractType" required="true"   ng-disabled="!processInstance.firstTask"/>--%>
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'分包合同类型'}:true"
                                            ng-model="vm.item.subContractType" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">分包合同额（万元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.subContractMoney" name="subContractMoney" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required" style="font-weight: bold;">分包合同号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.subContractNo" name="subContractNo" required="true"    readonly/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.getSubContractNo();"
                                                    ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-refresh"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required" >院级评审人员</label>
                                <div class="col-md-4" >
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptReviewUserName" required="true" name="deptReviewUserName"  readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('deptReviewUser');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.firstTask">发起人选择院级评审人员</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">评审级别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'评审级别'}:true"
                                            ng-model="vm.item.reviewLevel" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.reviewLevel=='公司级'">公司级评审人员</label>
                                <div class="col-md-4" ng-if="vm.item.reviewLevel=='公司级'">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.reviewUserName" required="true" name="reviewUserName"  ng-disabled="true" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('reviewUser');" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部人员（合同）')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.firstTask">经营发展部人员（合同）选择评审人员</span>
                                </div>

                            </div>

                            <div class="form-group" >
                                <label class="col-md-2 control-label required" >分管院长</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptChargeMenName" name="deptChargeMenName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('deptChargeMen');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required" >合同分管副总</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractChargeLeaderName" name="contractChargeLeaderName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('contractChargeLeader');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">是否对内分包</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否中标'}:true"
                                            ng-model="vm.item.inCompany" class="form-control" ng-disabled="!processInstance.firstTask" ></select>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.inCompany">对内承接部门</label>
                                <div class="col-md-4">
                                    <div class="input-group" ng-if="vm.item.inCompany">
                                        <input type="text" class="form-control" ng-model="vm.item.inDeptName"
                                               name="inDeptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"
                                                    ng-click="vm.showDeptModal(vm.opt='inDeptId')"> <i
                                                    class="fa fa-cog" ></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block" ng-if="vm.item.inCompany" >请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group" ng-if="!vm.item.inCompany">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">供方名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.supplierName" name="supplierName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectSupplier();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">统一社会信用代码</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="统一社会信用代码" ng-model="vm.item.supplierCreditCode" ng-disabled="!processInstance.firstTask"
                                           required="true" maxlength="50"/>
                                </div>
                            </div>
                            <div class="form-group" ng-if="!vm.item.inCompany">
                                <label class="col-md-2 control-label required">供方联系人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.supplierLinkMan" name="supplierLinkMan" required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">供方联系人电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.supplierLinkTel" name="supplierLinkTel" required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">分包合同主要情况说明</label>
                                <div class="col-md-10">
                                    <textarea type="text" rows="3" class="form-control" ng-model="vm.item.subContractDesc"
                                              name="subContractDesc" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">是否有履约保证金</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否中标'}:true"
                                            ng-model="vm.item.cashDeposit" class="form-control" ng-disabled="!processInstance.firstTask" ></select>
                                </div>
                                <label class="col-md-2 control-label" ng-if="vm.item.cashDeposit">履约保证金额(万元)</label>
                                <div class="col-md-4">
                                    <input ng-if="vm.item.cashDeposit" ng-model="vm.item.cashDepositMoney" class="form-control" ng-disabled="!processInstance.firstTask" >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">是否有履约保函</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否开具保函'}:true"
                                            ng-model="vm.item.backletter" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label" ng-if="vm.item.backletter">履约保函金额(万元)</label>
                                <div class="col-md-4">
                                    <input  ng-if="vm.item.backletter" ng-model="vm.item.backletterMoney" class="form-control" ng-disabled="!processInstance.firstTask" >
                                </div>
                            </div>
                            <%--<div class="form-group" >
                                <label class="col-md-2 control-label">合同状态</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'合同状态'}:true" ng-disabled="!processInstance.firstTask"
                                            ng-model="vm.item.contractStatus" class="form-control" name="contractStatus"></select>
                                </div>

                                <label class="col-md-2 control-label">合同是否已签章</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.sign" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label  class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark"  name="remark" maxlength="200"
                                           ng-disabled="!processInstance.firstTask" placeholder="" />
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
                <div class="tab-pane " id="tab_15_4"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form5">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>签约日期</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="signTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.signTime" required="true"
                                               ng-disabled="processInstance.myRunningTaskName.indexOf('合同上传')<0&&processInstance.myRunningTaskName.indexOf('确认')<0">
                                        <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('合同上传')<0&&processInstance.myRunningTaskName.indexOf('确认')<0"><i
                                            class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">合同盖章扫描附件</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractAttachUrl"
                                               name="contractAttachUrl" maxlength="100" disabled="disabled"/>
                                        <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUploadHead" class="btn blue fileinput-button btn-sm" ng-disabled="processInstance.myRunningTaskName.indexOf('合同上传')<0&&processInstance.myRunningTaskName.indexOf('确认')<0">
                                          <i class="fa fa-cloud-upload" style="height:15px "></i>
                                        <span>上传</span>
                                             <input type="file" name="multipartFile" id="contractAttachUrl" accept="*" multiple="multiple"/>
                                   </span>
                                        <a ng-href="{{vm.item.contractAttachUrl}}" target="_blank">
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
                                    <input ng-model="vm.item.subContractMoney" class="form-control" ng-disabled="processInstance.myRunningTaskName.indexOf('印花税')<0&&processInstance.myRunningTaskName.indexOf('盖章合同上传')<0">
                                </div>
                                <label class="col-md-2 control-label required" style="color: red">税目</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同印花税税目'}:true"
                                            ng-model="vm.item.taxType" class="form-control" ng-change="vm.save()"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('印花税')<0"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label" >税率</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.taxNum"  ng-disabled="vm.item.taxType!='其他'||processInstance.myRunningTaskName.indexOf('印花税')<0">
                                        <span class="input-group-addon">%</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label" >印花税额(万元)</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <span class="input-group-addon">￥</span>
                                        <input type="text" class="form-control" ng-model="vm.item.stampTaxMoney"  readonly>
                                        <span class="input-group-addon">万元</span>
                                    </div>
                                     <span style=" margin-left:40px;font-size: 13px;color: red" >{{(vm.item.stampTaxMoney*10000).toFixed(2)+'元'}}</span>
                                </div>
                            </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<div class="modal fade" id="selectSupplierModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">供方信息</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qSupplier"/>
                    </div>
                </div>
                <div class="table-scrollable" style="overflow-y: auto;overflow-x: hidden;max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th style="width: 75px;">供方编号</th>
                            <th>供方名称</th>
                            <th>供方地址</th>
                            <th>录入部门</th>
                            <th style="width: 120px;">单位类型</th>
                            <th style="width: 80px;">联系人</th>
                            <th style="width: 110px;">联系电话</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.suppliers|filter:{name:vm.qSupplier}">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.supplierName==item.name" class="cb_supplier" data-name="{{item}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.code"></td>
                            <td ng-bind="item.name"></td>
                            <td ng-bind="item.address"></td>
                            <td ng-bind="item.deptName"></td>
                            <td ng-bind="item.supplierType"></td>
                            <td ng-bind="item.linkMan"></td>
                            <td ng-bind="item.linkTel"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectSupplier();">确认</button>
            </div>
        </div>
    </div>
</div>

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
                            <%--                            <th style="width: 60px">评审级别</th>--%>
                            <th style="width: 120px">签约日期</th>
                            <%--                                        <th style="width: 100px;">创建时间</th>--%>
                            <%--                            <th style="width: 100px;">合同来源</th>--%>
                            <th style="width: 30px;">补充合同</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.librarys |filter:{contractName:vm.qContract}|filter:{projectName:vm.qProject}|filter:{contractNo:vm.qContractNo}|filter:{projectNo:vm.qProjectNo}
                        ">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.contractLibraryId==item.id" class="cb_library"
                                       data-name="{{item}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.contractName"><strong ></strong></td>
                            <td ng-bind="item.projectName"><strong ></strong></td>
                            <td ng-bind="item.contractNo" ></td>
                            <td ng-bind="item.projectNo" ></td>
                            <td ng-bind="item.contractMoney " ></td>
                            <%--                            <td ng-bind="item.reviewLevel"></td>--%>
                            <td ng-bind="item.signTime"></td>
                            <%--                                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>--%>
                            <%--                            <td>
                                                            <strong   ng-if="item.contractPreId!=0&&item.contractReviewId==0"><span style="color: red" ng-click="vm.opt=1;vm.showNew(item.contractPreId)">超前任务单</span></strong>
                                                            <strong   ng-if="item.contractPreId!=0&&item.contractReviewId!=0"><span style="color: green" ng-click="vm.opt=1;vm.showNew(item.contractPreId)">超前任务单(已补录)</span></strong>
                                                            <strong   ng-if="item.contractReviewId!=0&&item.contractPreId==0"><span style="color: blue" ng-click="vm.opt=0;vm.showNew(item.contractReviewId)">合同评审</span></strong>
                                                            <strong  ng-if="item.contractReviewId==0&&item.preContractId==0" ><span style="color: grey">其他</span></strong>
                                                        </td>--%>
                            <td class="text-center">
                                <span class="label label-sm label-success"  ng-if="item.main">是</span>
                                <span class="label label-sm label-default" ng-if="!item.main">否</span>
                            </td>
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
<jsp:include page="../print/print.jsp"/>


