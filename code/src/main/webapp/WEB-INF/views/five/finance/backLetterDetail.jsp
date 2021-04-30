<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:62})">财务管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">保函管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> <span ng-bind="tableName">保函管理</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator">
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>

    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_detail_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 280px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">填报人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.userName" name="userName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('user');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label required">填报人部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group form-">
                                <label class="col-md-2 control-label required">开具、撤销、续期</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'相关按钮'}:true"
                                            ng-model="vm.item.relateType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName" name="projectName" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">项目号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo" name="projectNo" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractName" name="contractName" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo" name="contractNo" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">标书编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.bidNo" name="bidNo" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">是否为联合体</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.combo" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.combo=='是'">
                                <label class="col-md-2 control-label" >联合体名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.comboName" name="comboName" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.relateType=='开具'">
                                <label class="col-md-2 control-label required">保函类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'保函类型'}:true"
                                            ng-model="vm.item.backLetterType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">保函金额(元)</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.cash" name="cash" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.relateType=='开具'">
                                <label class="col-md-2 control-label required">手续费（元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.poundage" name="poundage" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.relateType=='开具'">
                                <div class="col-md-1" >
                                    <label class="control-label">
                                        <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked> 选填1
                                    </label>
                                </div>
                                <label class="col-md-1 control-label ">担保开始日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="assureDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.assureDate" name="assureDate" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">担保结束日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="assureDateEnd">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.assureDateEnd" name="assureDateEnd" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.relateType=='开具'">
                                <div class="col-md-1">
                                    <label class="control-label">
                                        <input type="radio" name="optionsRadios" id="optionsRadios2" value="option1" > 选填2
                                    </label>
                                </div>
                                <label class="col-md-1 control-label ">自开立之日起<br/>至结束月份</label>
                                <span class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.assureMonth" name="assureMonth" ng-disabled="!processInstance.firstTask" />
                                </span>
                            </div>
                            <div class="form-group" ng-if="vm.item.relateType=='续期'">
                                <div class="col-md-1" >
                                    <label class="control-label">
                                        <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" checked> 选填1
                                    </label>
                                </div>
                                <label class="col-md-1 control-label ">续期开始日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="continueDateEnd">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.continueDateEnd" name="continueDateEnd" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label ">续期结束日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="continueDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.continueDate" name="continueDate" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group" ng-if="vm.item.relateType=='续期'">
                                <div class="col-md-1">
                                    <label class="control-label">
                                        <input type="radio" name="optionsRadios" id="optionsRadios2" value="option1" > 选填2
                                    </label>
                                </div>
                                <label class="col-md-1 control-label ">自开立之日起<br/>至结束月份</label>
                                <span class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.continueMonth" name="continueMonth" ng-disabled="!processInstance.firstTask" />
                                </span>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">保函编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.backLetterNo" name="backLetterNo" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">开立保函合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.backContractNo" name="backContractNo" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">担保银行</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.assureBank" name="assureBank" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">保函开立日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="backLetterDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.backLetterDate" name="backLetterDate" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">失效日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="cancelDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.cancelDate" name="cancelDate" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
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
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print-oaMaterialPurchaseDetail.jsp"/>


