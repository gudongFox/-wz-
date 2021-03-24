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
            <span ng-bind="tableName">供方管理</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">供方管理</span>
            <small style="font-size: 50%;" ng-bind="vm.item.name"></small>
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
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">供方编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.item.code" name="code" ng-disabled="!processInstance.firstTask"
                                           required="true" maxlength="50"/>
                                </div>

                                <label class="col-md-2 control-label required">纳税人识别号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                    <input type="text" class="form-control" placeholder="纳税人识别号" ng-disabled="!processInstance.firstTask" required="true"
                                           ng-model="vm.item.taxNo" name="taxNo" maxlength="20"/>
                                    <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.checkTaxNo(vm.item.taxNo)"
                                                ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i
                                                class="fa fa-refresh"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">入库类别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'供方入库类别'}:true" ng-disabled="!processInstance.firstTask"
                                            ng-model="vm.item.inType" class="form-control" name="inType"></select>
                                </div>

                                <label class="col-md-2 control-label">录入部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.item.deptId);" ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">社会统一信用代码</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="社会统一信用代码" ng-model="vm.item.creditCode" ng-disabled="!processInstance.firstTask"
                                           name="creditCode" required="true" maxlength="50"/>
                                </div>
                                <label class="col-md-2 control-label required">注册资金(万元)</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="注册资金" ng-model="vm.item.registeredFund" ng-disabled="!processInstance.firstTask"
                                           name ="registeredFund" required="true" maxlength="50"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">法定代表人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="法定代表人" ng-model="vm.item.legalPerson" name="legalPerson" ng-disabled="!processInstance.firstTask"
                                           name ="legalPerson"required="true" maxlength="50"/>
                                </div>
                                <label class="col-md-2 control-label ">法人联系电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="法人联系电话" ng-model="vm.item.legalPersonTel" name="legalPersonTel"  ng-disabled="!processInstance.firstTask"
                                           name="legalPersonTel" maxlength="50"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系人姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="联系人姓名" ng-model="vm.item.linkMan" ng-disabled="!processInstance.firstTask"
                                           name="linkMan" required="true" maxlength="50"/>
                                </div>
                                <label class="col-md-2 control-label required">联系人职务</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="联系人职务"
                                           ng-model="vm.item.linkTitle" name="linkTitle" required="true" ng-disabled="!processInstance.firstTask"
                                           maxlength="20"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系人电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="联系人电话"
                                           ng-model="vm.item.linkTel" name="linkTel" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">联系人邮箱</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="联系人邮箱"
                                           ng-model="vm.item.linkMail" name="linkMail" required="true" maxlength="50" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">开户银行</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="开户银行"
                                           ng-model="vm.item.depositBank" name="depositBank" maxlength="20"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label">银行账号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="银行账号"
                                           ng-model="vm.item.bankAccount" name="bankAccount"  ng-disabled="!processInstance.firstTask"
                                           maxlength="45"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">纳税主体资格</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'纳税主体资格'}:true" ng-disabled="!processInstance.firstTask"
                                            ng-model="vm.item.taxType" class="form-control" name="taxType"></select>
                                </div>

                                <label class="col-md-2 control-label required">供方名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" placeholder="供方名称" ng-model="vm.item.name" name="name" ng-disabled="!processInstance.firstTask"
                                               required="true" maxlength="50"/>
                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.checkSupplier(vm.item.name)"
                                                ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i
                                                class="fa fa-refresh"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">经营范围</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="经营范围" ng-disabled="!processInstance.firstTask"
                                           ng-model="vm.item.businessScope" name="businessScope" maxlength="70"/>
                                </div>
                                <label class="col-md-2 control-label required">供方单位性质</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'客户单位类型'}:true" ng-disabled="!processInstance.firstTask"
                                            ng-model="vm.item.supplierType" class="form-control"
                                            name="supplierType"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">系统内部编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="系统内部编号" ng-model="vm.item.systemInName" ng-disabled="!processInstance.firstTask"
                                           name="systemInName" maxlength="100"/>
                                </div>
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-4">
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
                     style="min-height: 620px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 620px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_4"
                     style="min-height: 460px;overflow-y: auto;overflow-x: hidden;">
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>分包/采购合同名称</th>
                                <th>分包/采购合同号</th>
                                <th>发包部门名称</th>
                                <th>主合同名称</th>
                                <th>主合同号</th>
                                <th style="width: 100px">主合同金额(万元)</th>
                                <th style="width: 80px">是否对内分包</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.list">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.subContractName"  class="data_title"  ng-click="vm.showContractLibrary(item.id);"><strong ></strong>
                                </td>
                                <td ng-bind="item.subContractNo" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.contractName"></td>
                                <td ng-bind="item.contractNo"></td>
                                <td ng-bind="item.contractMoney"></td>
                                <td class="text-center">
                                    <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.inCompany">是</span>
                                    <span class="label label-sm label-default" ng-if="!item.inCompany">否</span>
                                </td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.showContractLibrary(item.id);" title="详情"></i>
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
                            <a class="btn green btn-sm defaultBtn"  ng-click="vm.addUsedName();"><i class="fa fa-plus"></i>新增</a>
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
<%--<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-thumb-tack"></i> 合作项目
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>分包/采购合同名称</th>
                    <th>分包/采购合同号</th>
                    <th>发包部门名称</th>
                    <th>主合同名称</th>
                    <th>主合同号</th>
                    <th style="width: 100px">主合同金额(万元)</th>
                    <th style="width: 80px">是否对内分包</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 55px">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="item.subContractName"  class="data_title"  ng-click="vm.showContractLibrary(item.id);"><strong ></strong>
                    </td>
                    <td ng-bind="item.subContractNo" class="hidden-md hidden-sm"></td>
                    <td ng-bind="item.deptName"></td>
                    <td ng-bind="item.contractName"></td>
                    <td ng-bind="item.contractNo"></td>
                    <td ng-bind="item.contractMoney"></td>
                    <td class="text-center">
                        <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.inCompany">是</span>
                        <span class="label label-sm label-default" ng-if="!item.inCompany">否</span>
                    </td>
                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showContractLibrary(item.id);" title="详情"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>--%>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 供方资质
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addDetail();"
               ng-show="rightData.selectOpts.indexOf('修改')>=0">
                <i class="fa fa-plus"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th >资质类别</th>
                    <th >资质等级</th>
                    <th >资质编号</th>
                    <th >有效期</th>
                    <th style="width: 60px;"ng-show="rightData.selectOpts.indexOf('修改')>=0">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.supplierDetails">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.type" style="color: blue" ng-click="vm.showDetail(detail.id);" ></td>
                    <td ng-bind="detail.grade"></td>
                    <td ng-bind="detail.code"></td>
                    <td ng-bind="detail.validityTime"></td>
                    <td ng-show="rightData.selectOpts.indexOf('修改')>=0">
                        <i class="fa fa-edit margin-right-10" ng-click="vm.showDetail(detail.id);"
                           title="详情" ></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">供方资质详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="comment_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">资质类别</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="资质类别" name="baseName"
                                       ng-model="vm.detail.type" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">资质等级</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="资质等级" name="baseComment"
                                       ng-model="vm.detail.grade" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">编号</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="编号" name="baseComment"
                                       ng-model="vm.detail.code" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">有效期</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="有效期" name="baseComment"
                                       ng-model="vm.detail.validityTime" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">顺序</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       ng-model="vm.detail.seq" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">创建时间</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       value="{{vm.detail.gmtCreate | date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">修改时间</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       value="{{vm.detail.gmtModified | date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();" ng-show="!processInstance.taskUsers.indexOf(user.userLogin)">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade" id="usedNameModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">曾用名详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="comment_form1">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">变更前名称</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="变更前名称" name="oldName"
                                       ng-model="vm.usedName.oldName" disabled />
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
                                       ng-model="vm.usedName.seq"  disabled/>
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
<%--                <button type="button" class="btn blue" ng-click="vm.updateUsedName();">保存</button>--%>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../print/print.jsp"/>