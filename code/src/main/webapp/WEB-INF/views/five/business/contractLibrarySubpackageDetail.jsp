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
            <span ng-bind="tableName">分包/采购合同库</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">分包/采购合同库</span>
            <small style="font-size: 50%;" ng-bind="vm.item.projectName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="rightData.selectOpts.indexOf('修改')>=0" style="font-size: 14px;line-height: 1.6"
               ng-click="vm.save();" >
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" >
                <i class="fa fa-print"></i> 打印 </a>
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
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        供方信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        评审信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_4" data-toggle="tab" aria-expanded="false">
                        财务相关 </a>
                </li>
                <li class="">
                    <a href="#tab_15_5" data-toggle="tab" aria-expanded="false">
                        补充情况 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否采购合同'}:true"
                                            ng-model="vm.item.purchase" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                                </div>
                            </div>
                            <div  class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">主合同名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractName"
                                               name="contractName" required="true" disabled  title="{{vm.item.contractName}}">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showContractLibraryModal();"
                                                    ng-disabled="rightData.selectOpts.indexOf('修改')<0"> <i
                                                    class="fa fa-cog"></i></button>
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
                            <div class="form-group">
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
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目性质</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'五洲项目类型'}:true"
                                            ng-model="vm.item.projectNature" class="form-control"
                                            disabled></select>
                                </div>
                                <label class="col-md-2 control-label required">发包/采购部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="rightData.selectOpts.indexOf('修改')<0"
                                                    ng-click="vm.showDeptModal(vm.opt='deptId')"> <i
                                                    class="fa fa-cog" ></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block" >请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>分包合同名称</strong></label>
                                <div class="col-md-4">
                                    <input   class="form-control" ng-model="vm.item.subContractName" name="subContractName" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                                <label class="col-md-2 control-label required">分包合同类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.subContractType" name="subContractType" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">分包合同额（万元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.subContractMoney" name="subContractMoney" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                                <label class="col-md-2 control-label required">分包合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.subContractNo" name="subContractNo" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">是否对内分包</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否中标'}:true"
                                            ng-model="vm.item.inCompany" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0" ></select>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.inCompany">对内承接部门</label>
                                <div class="col-md-4">
                                    <div class="input-group" ng-if="vm.item.inCompany">
                                        <input type="text" class="form-control" ng-model="vm.item.inDeptName"
                                               name="inDeptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="rightData.selectOpts.indexOf('修改')<0"
                                                    ng-click="vm.showDeptModal(vm.opt='inDeptId')"> <i
                                                    class="fa fa-cog" ></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block" ng-if="vm.item.inCompany" >请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">分包合同主要情况说明</label>
                                <div class="col-md-10">
                                    <textarea type="text" rows="3" class="form-control" ng-model="vm.item.subContractDesc"
                                              name="subContractDesc" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">是否有履约保证金</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否中标'}:true"
                                            ng-model="vm.item.cashDeposit" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0" ></select>
                                </div>
                                <label class="col-md-2 control-label" ng-if="vm.item.cashDeposit">履约保证金额(万元)</label>
                                <div class="col-md-4">
                                    <input ng-if="vm.item.cashDeposit" ng-model="vm.item.cashDepositMoney" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0" >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">是否有履约保函</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否开具保函'}:true"
                                            ng-model="vm.item.backletter" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                                </div>
                                <label class="col-md-2 control-label" ng-if="vm.item.backletter">履约保函金额(万元)</label>
                                <div class="col-md-4">
                                    <input  ng-if="vm.item.backletter" ng-model="vm.item.backletterMoney" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0" >
                                </div>
                            </div>
                            <div class="form-group" >
                                <label class="col-md-2 control-label">合同状态</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractStatus" name="contractStatus" ng-disabled="rightData.selectOpts.indexOf('修改')<0" />
                                </div>

                                <label class="col-md-2 control-label">合同是否已签章</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.sign" class="form-control"
                                            ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label  class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark"  name="remark" maxlength="200"
                                           ng-disabled="rightData.selectOpts.indexOf('修改')<0" placeholder="" />
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
                <div class="tab-pane " id="tab_15_2"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form2">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">供方名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.supplierName" name="supplierName"  required="true" ng-disabled="!rightData.selectOpts.indexOf('修改')>=0" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectSupplier();" ng-disabled="!rightData.selectOpts.indexOf('修改')>=0"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">统一社会信用代码</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="统一社会信用代码" ng-model="vm.item.supplierCreditCode" ng-disabled="!rightData.selectOpts.indexOf('修改')>=0"
                                           required="true" maxlength="50"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.supplierLinkMan" name="supplierLinkMan" required="true" ng-disabled="!rightData.selectOpts.indexOf('修改')>=0" />
                                </div>
                                <label class="col-md-2 control-label required">联系人电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.supplierLinkTel" name="supplierLinkTel" required="true" ng-disabled="!rightData.selectOpts.indexOf('修改')>=0" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">开户银行</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.depositBank" name="depositBank" required="true"   ng-disabled="!rightData.selectOpts.indexOf('修改')>=0"/>
                                </div>
                                <label class="col-md-2 control-label required">银行账号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.bankAccount" name="bankAccount" required="true"   ng-disabled="!rightData.selectOpts.indexOf('修改')>=0"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane " id="tab_15_3"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form3">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">评审级别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'评审级别'}:true"
                                            ng-model="vm.item.reviewLevel" class="form-control" ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.reviewLevel=='公司级'">公司级评审人员</label>
                                <div class="col-md-4" ng-if="vm.item.reviewLevel=='公司级'">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.reviewUserName" required="true" name="reviewUserName"   readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('reviewUser');" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.firstTask">经营发展部人员（合同）选择评审人员</span>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.reviewLevel=='院级'">院级评审人员</label>
                                <div class="col-md-4" ng-if="vm.item.reviewLevel=='院级'">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptReviewUserName" required="true" name="deptReviewUserName"  readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('deptReviewUser');" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.firstTask">发起人选择院级评审人员</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required" >部门领导会签</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptChargeMenName" name="deptChargeMenName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('deptChargeMen');" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">合同分管副总</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractChargeLeaderName" name="contractChargeLeaderName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('contractChargeLeader');" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane " id="tab_15_4"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form4">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>签约日期</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="signTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.businessSubpackage.signTime" required="true"
                                               disabled>
                                        <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                            disabled><i
                                            class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">合同盖章扫描附件</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.businessSubpackage.contractAttachUrl"
                                               name="contractAttachUrl" maxlength="100" disabled="disabled"/>
                                        <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUploadHead" class="btn blue fileinput-button btn-sm" disabled>
                                          <i class="fa fa-cloud-upload" style="height:15px "></i>
                                        <span>上传</span>
                                             <input type="file" name="multipartFile" id="contractAttachUrl" accept="*" multiple="multiple"/>
                                   </span>
                                        <a ng-href="{{vm.item.businessSubpackage.contractAttachUrl}}" target="_blank">
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
                                    <input ng-model="vm.item.businessSubpackage.subContractMoney" class="form-control" disabled>
                                </div>
                                <label class="col-md-2 control-label required" style="color: red">税目</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同印花税税目'}:true"
                                            ng-model="vm.item.businessSubpackage.taxType" class="form-control"
                                            disabled></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label" >税率</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.businessSubpackage.taxNum"  disabled>
                                        <span class="input-group-addon">%</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label" >印花税额(万元)</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <span class="input-group-addon">￥</span>
                                        <input type="text" class="form-control" ng-model="vm.item.businessSubpackage.stampTaxMoney"  readonly>
                                        <span class="input-group-addon">万元</span>
                                    </div>
                                    <span style=" margin-left:40px;font-size: 13px;color: red" >{{(vm.item.businessSubpackage.stampTaxMoney*10000).toFixed(2)+'元'}}</span>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane " id="tab_15_5"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div class="table-scrollable" style="max-height: 400px">
                        <table  class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="min-width: 200px;">合同名称</th>
                                <th style="width: 130px">合同号</th>
                                <th>供方名称</th>
                                <th style="width: 160px">发包部门名称</th>
                                <th>主合同名称</th>
                                <th style="width: 100px">合同金额</th>
                                <th style="width: 60px">经办人</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 150px">流程状态</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.item.reviews" >
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.subContractName"  class="data_title"  ng-click="vm.showReview(item.id);">
                                </td>
                                <td ng-bind="item.subContractNo" ></td>
                                <td ng-bind="item.supplierName" ></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.contractName"></td>
                                <td ng-bind="item.subContractMoney"></td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <span ng-bind="item.processName"></span>
                                </td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.showReview(item.id,item.purchase);" title="详情"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                </form>
                </div>
            </div>
        </div>
    </div>
</div>
<%--<div class="portlet box blue" ng-if="!vm.item.inCompany" >
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-thumb-tack"></i> 供方信息
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="vm.addSupplier(vm.item.id);" >
                <i class="fa fa-save"></i> 保存供方信息 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="vm.showSupplier(vm.item.supplierId);" >
                <i class="fa fa-save"></i> 完善供方信息 </a>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label required" style="font-weight: bold;">供方名称</label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.item.supplierName" name="supplierName"  required="true" ng-disabled="!rightData.selectOpts.indexOf('修改')>=0" />
                            <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectSupplier();" ng-disabled="!rightData.selectOpts.indexOf('修改')>=0"><i class="fa fa-user"></i></button>
                                        </span>
                        </div>
                    </div>
                    <label class="col-md-2 control-label required">统一社会信用代码</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="统一社会信用代码" ng-model="vm.item.supplierCreditCode" ng-disabled="!rightData.selectOpts.indexOf('修改')>=0"
                               required="true" maxlength="50"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">联系人</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.supplierLinkMan" name="supplierLinkMan" required="true" ng-disabled="!rightData.selectOpts.indexOf('修改')>=0" />
                    </div>
                    <label class="col-md-2 control-label required">联系人电话</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.supplierLinkTel" name="supplierLinkTel" required="true" ng-disabled="!rightData.selectOpts.indexOf('修改')>=0" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">开户银行</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.depositBank" name="depositBank" required="true"   ng-disabled="!rightData.selectOpts.indexOf('修改')>=0"/>
                    </div>
                    <label class="col-md-2 control-label required">银行账号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.bankAccount" name="bankAccount" required="true"   ng-disabled="!rightData.selectOpts.indexOf('修改')>=0"/>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>--%>

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
                <div class="table-scrollable" style="max-height: 400px">
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
<jsp:include page="../print/print.jsp"/>
