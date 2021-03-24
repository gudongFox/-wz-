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
            <a ui-sref="" ng-bind="tableName">客户信息管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.name"></span>
        </li>

    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-user-following"></i> <span ng-bind="tableName">客户信息管理</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="vm.item.processEnd&&user.userLogin=='2736'" style="font-size: 14px;line-height: 1.6"
               ng-click="vm.save();" >
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6">
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
                <li class="">
                    <a href="#tab_15_4" data-toggle="tab" aria-expanded="false">
                        合作项目
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_5" data-toggle="tab" aria-expanded="false">
                        曾用名管理
                    </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_basic" style="height: 480px;overflow-y: auto;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">纳税人识别号</label>
                               
                                <div class="col-md-4"  ng-if="!vm.item.processEnd">
                                    <input type="text" class="form-control" placeholder="纳税人识别号"  ng-disabled="!processInstance.firstTask"
                                           ng-model="vm.item.taxNo" name="taxNo" maxlength="20"  required="true"/>
                                </div>
                                <div class="col-md-4"  ng-if="vm.item.processEnd">
                                    <input type="text" class="form-control" placeholder="纳税人识别号(补录)"  ng-disabled="user.userLogin!='2736'"
                                           ng-model="vm.item.taxNo" name="taxNo" maxlength="20"  required="true"/>
                                </div>
                                <label class="col-md-2 control-label">纳税主体资格</label>
                               
                                <div class="col-md-4" ng-if="!vm.item.processEnd">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'纳税主体资格'}:true"  ng-disabled="!processInstance.firstTask"
                                            ng-model="vm.item.taxType" class="form-control" name="taxType"></select>
                                </div>
                                <div class="col-md-4" ng-if="vm.item.processEnd">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'纳税主体资格'}:true"  ng-disabled="user.userLogin!='2736'"
                                            ng-model="vm.item.taxType" class="form-control" name="taxType"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">客户名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control"  placeholder="客户名称" ng-model="vm.item.name"
                                               required="true" maxlength="50"  ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.checkCustomer(vm.item.name)"
                                                ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i
                                                class="fa fa-refresh"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label ">客户编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.item.code"
                                           maxlength="50"  ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系人姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="联系人姓名" ng-model="vm.item.linkMan"
                                           name="linkMan" required="true" maxlength="50"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">客户地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="客户地址" ng-model="vm.item.address" name="address" required
                                           maxlength="50"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系人职务</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="联系人职务"
                                           ng-model="vm.item.linkTitle" name="linkTitle" required="true"
                                           maxlength="20"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">联系人电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="联系人电话"  ng-disabled="!processInstance.firstTask"
                                           ng-model="vm.item.linkTel" name="linkTel" required="true" maxlength="20"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">客户类别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'客户类别'}:true"
                                            ng-model="vm.item.customerScope" class="form-control"  ng-disabled="!processInstance.firstTask"
                                            name="customerScope"></select>
                                </div>
                                <label class="col-md-2 control-label required">单位类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'客户单位类型'}:true"
                                            ng-model="vm.item.customerType" class="form-control"  ng-disabled="!processInstance.firstTask"
                                            name="customerType"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">传真号码</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="传真号码" ng-model="vm.item.linkFax"  ng-disabled="!processInstance.firstTask"
                                           name="linkFax" maxlength="20"/>
                                </div>

                                <label class="col-md-2 control-label">电子邮箱</label>
                                <div class="col-md-4">
                                    <input type="email" class="form-control" placeholder="Email"  ng-disabled="!processInstance.firstTask"
                                           ng-model="vm.item.linkMail" name="linkMail" maxlength="50"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">开户银行</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="开户银行"  ng-disabled="!processInstance.firstTask"
                                           ng-model="vm.item.depositBank" name="depositBank" maxlength="20"/>
                                </div>
                                <label class="col-md-2 control-label">银行账号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="银行账号"  ng-disabled="!processInstance.firstTask"
                                           ng-model="vm.item.bankAccount" name="bankAccount" minlength="15"
                                           maxlength="45"/>
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
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 460px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 460px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_4"
                     style="min-height: 460px;overflow-y: auto;overflow-x: hidden;">
                    <div class="table-scrollable">
                        <table class="table table-striped table-bordered table-advance table-hover">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>项目名称</th>
                                <th>部门名称</th>
                                <th>合同号</th>
                                <th>项目性质</th>
                                <th style="width: 130px;">合同金额（万元）</th>
                                <th style="width: 120px;">签订日期</th>
                                <th style="width: 50px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="project in vm.list">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="project.projectName"></td>
                                <td ng-bind="project.deptName"></td>
                                <td ng-bind="project.contractNo"></td>
                                <td ng-bind="project.projectNature"></td>
                                <td ng-bind="project.contractMoney|currency : '￥'"></td>
                                <td ng-bind="project.signTime"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.showContractLibrary(project.id);"
                                       title="详情"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="tab-pane" id="tab_15_5"
                     style="min-height: 460px;">
                    <div class="row">
                        <div class="col-md-12">
                            <a class="btn green btn-sm defaultBtn" ng-show="vm.item.processEnd" ng-click="vm.addUsedName();"><i class="fa fa-plus"></i>新增</a>
                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table class="table table-striped table-bordered table-advance table-hover">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>变更前名称</th>
                                <th>变更后名称</th>
                                <th>变更原因</th>
                                <th style="width: 120px;">变更人</th>
                                <th style="width: 120px;">变动时间</th>
                                <th style="width: 80px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="name in vm.usedNames">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="name.oldName"></td>
                                <td ng-bind="name.name"></td>
                                <td ng-bind="name.remark"></td>
                                <td ng-bind="name.creatorName"></td>
                                <td ng-bind="name.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.showUsedName(name.id);"
                                       title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-5" ng-click="vm.removeUsedName(name.id);"
                                       title="删除"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="usedNameModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">曾用名详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="comment_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">变更前名称</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="变更前名称" name="oldName"
                                       ng-model="vm.usedName.oldName" disabled/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">变更后名称</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="变更后名称" name="baseComment"
                                       ng-model="vm.usedName.name" disabled/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">变更原因</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="变更原因" name="remark"
                                       ng-model="vm.usedName.remark" disabled/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">顺序</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       ng-model="vm.usedName.seq" disabled/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">创建时间</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       value="{{vm.usedName.gmtCreate | date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">修改时间</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       value="{{vm.usedName.gmtModified | date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateUsedName();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<%--<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-thumb-tack"></i> 合作项目
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-bordered table-advance table-hover">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>项目名称</th>
                    <th>部门名称</th>
                    <th>合同号</th>
                    <th>项目性质</th>
                    <th style="width: 130px;">合同金额（万元）</th>
                    <th style="width: 120px;">签订日期</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="project in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="project.projectName"></td>
                    <td ng-bind="project.deptName"></td>
                    <td ng-bind="project.contractNo"></td>
                    <td ng-bind="project.projectNature"></td>
                    <td ng-bind="project.contractMoney|currency : '￥'"></td>
                    <td ng-bind="project.signTime"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showContractLibrary(project.id);"
                           title="详情"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>--%>
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../print/print.jsp"/>