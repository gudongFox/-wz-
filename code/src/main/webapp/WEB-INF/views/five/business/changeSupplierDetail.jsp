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
            <a ui-sref="business.customer" ng-bind="tableName">供方信息变更</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.supplierName"></span>
        </li>

    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-user-following"></i> <span ng-bind="tableName">供方信息变更</span>
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
                                <label class="col-md-2 control-label required" style="font-weight: bold;">供方名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.supplierName" name="supplierName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectSupplier();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
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

<div class="portlet box blue" ng-show="processInstance.myRunningTaskName.indexOf('经营发展部')>=0">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-thumb-tack"></i> 需要变更的供方信息
            <span style="color: red;font-size: 12px;margin-left: 20px">修改后请点击保存</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" style="font-size: 14px;line-height: 1.6"
               ng-click="vm.saveChange();" >
                <i class="fa fa-save"></i> 保存 </a>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal form" role="form" id="detail_form1">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label required">供方编号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.supplier.code" name="code" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                               required="true" maxlength="50"/>
                    </div>
                    <label class="col-md-2 control-label required">供方名称</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="供方名称" ng-model="vm.supplier.name" name="name" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                               required="true" maxlength="50"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">入库类别</label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'供方入库类别'}:true" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                                ng-model="vm.supplier.inType" class="form-control" name="inType"></select>
                    </div>

                    <label class="col-md-2 control-label">录入部门</label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.supplier.deptName"
                                   name="deptName" maxlength="100"
                                   disabled/>
                            <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.supplier.deptId);" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">社会统一信用代码</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="社会统一信用代码" ng-model="vm.supplier.creditCode" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                               name="creditCode" required="true" maxlength="50"/>
                    </div>
                    <label class="col-md-2 control-label required">注册资金(万元)</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="注册资金" ng-model="vm.supplier.registeredFund" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                               name ="registeredFund" required="true" maxlength="50"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">法定代表人</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="法定代表人" ng-model="vm.supplier.legalPerson" name="legalPerson" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                               name ="legalPerson"required="true" maxlength="50"/>
                    </div>
                    <label class="col-md-2 control-label ">法人联系电话</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="法人联系电话" ng-model="vm.supplier.legalPersonTel" name="legalPersonTel"  ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                               name="legalPersonTel" maxlength="50"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">联系人姓名</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="联系人姓名" ng-model="vm.supplier.linkMan" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                               name="linkMan" required="true" maxlength="50"/>
                    </div>
                    <label class="col-md-2 control-label required">联系人职务</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="联系人职务"
                               ng-model="vm.supplier.linkTitle" name="linkTitle" required="true" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                               maxlength="20"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">联系人电话</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="联系人电话"
                               ng-model="vm.supplier.linkTel" name="linkTel" required="true" maxlength="20" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"/>
                    </div>
                    <label class="col-md-2 control-label required">联系人邮箱</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="联系人邮箱"
                               ng-model="vm.supplier.linkMail" name="linkMail" required="true" maxlength="50" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">开户银行</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="开户银行"
                               ng-model="vm.supplier.depositBank" name="depositBank" maxlength="20"  ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"/>
                    </div>
                    <label class="col-md-2 control-label">银行账号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="银行账号"
                               ng-model="vm.supplier.bankAccount" name="bankAccount" minlength="15" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                               maxlength="20"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">纳税主体资格</label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'纳税主体资格'}:true" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                                ng-model="vm.supplier.taxType" class="form-control" name="taxType"></select>
                    </div>
                    <label class="col-md-2 control-label">纳税人识别号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="纳税人识别号" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                               ng-model="vm.supplier.taxNo" name="taxNo" maxlength="20"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">经营范围</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="经营范围" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                               ng-model="vm.supplier.businessScope" name="businessScope" maxlength="70"/>
                    </div>
                    <label class="col-md-2 control-label required">供方单位性质</label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'客户单位类型'}:true" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
                                ng-model="vm.supplier.supplierType" class="form-control"
                                name="supplierType"></select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">备注</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" placeholder="备注" ng-model="vm.supplier.remark" ng-disabled="processInstance.myRunningTaskName.indexOf('经营发展部')<0"
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
                <div class="table-scrollable" style="max-height: 400px;overflow-y: auto">
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

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>


<jsp:include page="../print/print.jsp"/>





