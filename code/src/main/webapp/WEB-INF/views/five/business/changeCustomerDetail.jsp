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
            <a ui-sref="business.customer" ng-bind="tableName">客户信息变更</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.customerName"></span>
        </li>

    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-user-following"></i> <span ng-bind="tableName">客户信息变更</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>>
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_basic" data-toggle="tab" aria-expanded="true">
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
                <div class="tab-pane active" id="tab_basic" style="height: 280px;overflow-y: auto;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">客户名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.customerName" name="customerName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectCustomer();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">申请部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"  readonly
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  readonly
                                                    ng-click="vm.showDeptModal(vm.item.deptId);vm.opt='所属部门'"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">变更内容</label>
                                <div class="col-md-10">
                                    <textarea rows="4" type="text" class="form-control"  ng-model="vm.item.changeContent" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" placeholder="备注" ng-model="vm.item.remark" ng-disabled="!processInstance.firstTask" name="remark" maxlength="100"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">修改时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>

                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 460px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 460px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="portlet box blue" ng-show="processInstance.myRunningTaskName.indexOf('档案人员')>=0">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-thumb-tack"></i> 需要变更的客户信息
            <span style="color: red;font-size: 12px;margin-left: 20px">修改后请点击保存</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" style="font-size: 14px;line-height: 1.6"
               ng-click="vm.saveChange();" >
                <i class="fa fa-save"></i> 保存 </a>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form" id="detail_form1" >
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label required">客户编号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.customer.code"
                               required="true" maxlength="50"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"/>
                    </div>
                    <label class="col-md-2 control-label required">客户名称</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="客户名称" ng-model="vm.customer.name"
                               required="true" maxlength="50"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">联系人姓名</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="联系人姓名" ng-model="vm.customer.linkMan"
                               name="linkMan" required="true" maxlength="50"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"/>
                    </div>
                    <label class="col-md-2 control-label required">客户地址</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="客户地址" ng-model="vm.customer.address" name="address" required
                               maxlength="50"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">联系人职务</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="联系人职务"
                               ng-model="vm.customer.linkTitle" name="linkTitle" required="true"
                               maxlength="20"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"/>
                    </div>
                    <label class="col-md-2 control-label required">联系人电话</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="联系人电话"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"
                               ng-model="vm.customer.linkTel" name="linkTel" required="true" maxlength="20"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">客户类别</label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'客户类别'}:true"
                                ng-model="vm.customer.customerScope" class="form-control"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"
                                name="customerScope"></select>
                    </div>
                    <label class="col-md-2 control-label required">单位类型</label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'客户单位类型'}:true"
                                ng-model="vm.customer.customerType" class="form-control"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"
                                name="customerType"></select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">传真号码</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="传真号码" ng-model="vm.customer.linkFax"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"
                               name="linkFax" maxlength="20"/>
                    </div>

                    <label class="col-md-2 control-label">电子邮箱</label>
                    <div class="col-md-4">
                        <input type="email" class="form-control" placeholder="Email"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"
                               ng-model="vm.customer.linkMail" name="linkMail" maxlength="50"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">开户银行</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="开户银行"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"
                               ng-model="vm.customer.depositBank" name="depositBank" maxlength="20"/>
                    </div>
                    <label class="col-md-2 control-label">银行账号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="银行账号"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"
                               ng-model="vm.customer.bankAccount" name="bankAccount" minlength="15"
                               maxlength="20"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">纳税主体资格</label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'纳税主体资格'}:true"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"
                                ng-model="vm.customer.taxType" class="form-control" name="taxType"></select>
                    </div>
                    <label class="col-md-2 control-label">纳税人识别号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="纳税人识别号"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"
                               ng-model="vm.customer.taxNo" name="taxNo" maxlength="20"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">系统内部编号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="系统内部编号" ng-model="vm.item.systemInName" ng-disabled="!processInstance.firstTask"
                               name="systemInName" maxlength="100"/>
                    </div>
                    <label class="col-md-2 control-label">录入部门</label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.item.deptName"
                                   name="deptName" maxlength="100"  ng-disabled="!processInstance.firstTask"
                                   disabled/>
                            <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-disabled="!processInstance.firstTask"
                                                    ng-click="vm.showDeptModal(vm.item.deptId);vm.opt='所属部门'"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">备注</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" placeholder="备注" ng-model="vm.item.remark" ng-disabled="!processInstance.firstTask"
                               name="remark" maxlength="100"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">创建时间</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control"
                               value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                    </div>
                    <label class="col-md-2 control-label">修改时间</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control"
                               value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                    </div>
                </div>

            </div>
        </form>
    </div>
</div>
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

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
                <div class="table-scrollable" style="max-height: 400px;overflow-y: auto">
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
<jsp:include page="../print/print.jsp"/>