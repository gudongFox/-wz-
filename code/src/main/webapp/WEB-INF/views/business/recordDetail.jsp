<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>

        <li>
            <span>项目信息备案</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.projectName"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>项目信息备案
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
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
                                <label class="col-md-2 control-label required" style="font-weight: bold;">客户名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.customerName" name="customerName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectCustomer();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">客户地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="" ng-model="vm.item.customerAddress"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.linkMan" name="linkMan" required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">联系人电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.linkTel" name="linkTel" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div  class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName" name="projectName"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">联系人职务</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.linkTitle" name="linkTitle" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">建设地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectAddress" name="projectAddress" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">建设规模</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectScale" name="projectScale" required="true"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">

                                <label class="col-md-2 control-label required">业务内容</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同类型'}:true"
                                            ng-model="vm.item.businessContent" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">项目类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'设计作业类型'}:true"
                                            ng-model="vm.item.projectType" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">投资来源</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'投资来源'}:true"
                                            ng-model="vm.item.investSource" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>

                                </div>
                                <label class="col-md-2 control-label required">投资额度</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectInvest" name="projectInvest" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否开具保函</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否开具保函'}:true"
                                            ng-model="vm.item.tenderBond" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label">项目备注</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.remark"  name="remark" maxlength="200"
                                           ng-disabled="!processInstance.firstTask" placeholder="" />
                                </div>
                            </div>
                            <div class="form-group" ng-show="vm.item.tenderBond">
                                <label class="col-md-2 control-label">保函额度</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.tenderBondMoney" name="tenderBondMoney" ng-disabled="!processInstance.firstTask" />
                                </div>

                                <label class="col-md-2 control-label">保函方式</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'保函方式'}:true"
                                            ng-model="vm.item.tenderBondType" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">参与经营人员</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.businessUserName" name="businessUserName"  required="true" disabled />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">所属部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.item.deptId)"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block" >请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">计划开标时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="bidPlanOpen">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.bidPlanOpen" name="bidPlanOpen" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">实际开标时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="bidRealOpen">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.bidRealOpen" name="bidRealOpen" required="true" ng-disabled="!(processInstance.myTaskName.indexOf('结果')>=0)"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!(processInstance.myTaskName.indexOf('结果')>=0)"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>


                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">是否投标</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否中标'}:true"
                                            ng-model="vm.item.attend" class="form-control"  ng-disabled="!(processInstance.myTaskName.indexOf('结果')>=0)"></select>
                                </div>
                                <label class="col-md-2 control-label">是否中标</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否中标'}:true"
                                            ng-model="vm.item.win" class="form-control" ng-disabled="!(processInstance.myTaskName.indexOf('结果')>=0)"></select>
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
                        <p style="color: red;">备注：项目招投标结束后,由经办人据实填写中标情况.</p>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="../common/common-actFlow.jsp"/>

<div class="modal fade" id="selectEmployeeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">参与经营人员</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">


                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.qEmployee"/>
                        </div>
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="部门名称"
                                   ng-model="vm.qEmployeeDeptName"/>
                        </div>
                    </div>
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">序号</th>
                                <th>姓名</th>
                                <th>登录名</th>
                                <th>所属部门</th>
                                <th>员工专业</th>
                                <th>联系电话</th>
                                <th>员工类别</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.employees|filter:{deptName:vm.qEmployeeDeptName}|filter:{userName:vm.qEmployee}">
                                <td>
                                    <input type="checkbox" class="cb_employee" ng-checked="vm.item.businessUser.indexOf(item.userLogin)>=0" data-name="{{item.userLogin+'_'+item.userName}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="item.userName"></td>
                                <td ng-bind="item.userLogin"></td>
                                <td>
                                    <span ng-bind="item.deptName"></span>
                                </td>
                                <td ng-bind="item.specialty"></td>
                                <td ng-bind="item.mobile"></td>
                                <td ng-bind="item.userType">
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectUser();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade" id="selectCustomerModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
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
                            <th>录入部门</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="c in vm.customers|filter:{name:vm.qCustomer}">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.customerName==c.name" class="cb_customer" data-name="{{c}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="c.name"></td>
                            <td ng-bind="c.deptName"></td>
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

<div class="modal fade draggable-modal" id="deptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择所属部门</h4>
            </div>
            <div class="modal-body">
                <div id="dept_select_tree" style="max-height:500px;overflow-y: auto;overflow-x: hidden;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDept();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<jsp:include page="print/print-recordDetail.jsp"/>

