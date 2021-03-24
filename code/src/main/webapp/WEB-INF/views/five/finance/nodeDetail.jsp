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
            <span>票据管理</span>
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
            <i class="icon-note"></i>票据管理
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
                        项目信息 </a>
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
                                <label class="col-md-2 control-label required">票据来源账户</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.sourceAccount" name="sourceAccount" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">票据接收账户</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.targetAccount" name="targetAccount" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">票据金额（万元）</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.money" name="money" required="true" ng-change="vm.showBigNum();"   ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-2 control-label required">金额大写</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.moneyMax" name="moneyMax" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label  class="col-md-2 control-label">票据种类</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'票据种类'}:true"
                                            ng-model="vm.item.type" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label  class="col-md-2 control-label">票据状态</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'票据状态'}:true"
                                            ng-model="vm.item.state" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">部门名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"    disabled/>
                                </div>
                                <label  class="col-md-2 control-label">票据形式</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'票据形式'}:true"
                                            ng-model="vm.item.modality" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">票据号码</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.nodeNo" name="nodeNo" required="true"   />
                                </div>
                                <label  class="col-md-2 control-label">使用次数</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.useNum" name="useNum" required="true"   />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">开具银行</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.bank" name="bank" required="true"   />
                                </div>
                                <label  class="col-md-2 control-label">出票单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.drawDept" name="drawDept" required="true"   />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">抵付/承兑日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="userTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.userTime"  value="{{vm.item.userTime|date:'yyyy-MM-dd HH:mm'}}"  required="true" disabled="disabled">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">财务确认时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="financeVerifyTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.financeVerifyTime"   required="true" disabled="disabled">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">票据生效时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="nodeStartTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.nodeStartTime"    required="true" disabled="disabled">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">票据失效时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="nodeEndTime" >
                                        <input type="text" class="form-control" ng-change="vm.save()"
                                               ng-model="vm.item.nodeEndTime"   required="true" disabled="disabled">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>

                            </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">剩余到期时间（天）</label>
                            <div class="col-md-4" ng-click="vm.save()">
                                <input type="text" class="form-control"
                                       ng-model="vm.item.remainTime"  required="true" disabled="disabled">
                            </div>
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

