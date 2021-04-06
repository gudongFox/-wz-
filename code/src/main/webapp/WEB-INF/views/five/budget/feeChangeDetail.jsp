<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">财务管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>收费预算更改</span>
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
            <i class="icon-note"></i><span ng-bind="tableName">收费预算更改</span>
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName"
                   style="color: #35e0e1;"></small>

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
                     style="min-height: 120px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">预算项目</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName" name="projectName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectFee();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">填报部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" required="true" readonly/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();"
                                                    ng-disabled="!processInstance.firstTask"><i
                                                    class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">预算总金额</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <span class="input-group-addon">￥</span>
                                        <input type="text" class="form-control" ng-model="vm.item.totalBudgetMoney"
                                               name="totalBudgetMoney" readonly/>
                                        <span class="input-group-addon">万元</span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">预算年份</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker-year" id="budgetYear">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.budgetYear" required="true" disabled="disabled">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button"
                                                    ng-disabled="!processInstance.firstTask"><i
                                                    class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">流程标题</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.title" name="title"
                                           required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">变更内容</label>
                                <div class="col-md-10">
                                    <textarea rows="4" type="text" class="form-control"  ng-model="vm.item.changeContent" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"
                                           ng-disabled="!processInstance.firstTask"/>
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
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="portlet light" ng-show="vm.lastYeardetails.length>0">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i>
            <span ng-bind="vm.item.lastYear+' 预算明细'"></span>
        </div>

        <div class="tools">
            <a href="javascript:;" class="collapse" data-original-title="" title=""></a>
        </div>
    </div>
    <div class="portlet-body">
        <%--<table id="treeTable" class="table table-striped table-hover table-bordered  no-footer "></table>--%>
        <div id="lastTreeTable" class="table table-striped table-hover table-bordered  no-footer "></div>
    </div>
</div>
<div class="portlet box blue" ng-show="processInstance.myRunningTaskName.indexOf('预算管理专员')>=0">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> <span ng-bind="vm.item.budgetYear+' 预算明细'"></span>
            <span style="color: red;font-size: 12px;margin-left: 20px">修改后请点击保存</span>
        </div>

        <div class="tools">
            <a href="javascript:;" class="collapse" data-original-title="" title=""></a>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" style="font-size: 14px;line-height: 1.6"
               ng-click="vm.saveChange();" >
                <i class="fa fa-save"></i> 保存 </a>
            <a id="addBtn" class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0);">
                <i class="fa fa-plus"></i> 新增 </a>
            <a id="updateBtn" class="btn btn-sm btn-default" ng-click="vm.showDetailModel(1);">
                <i class="fa fa-edit"></i> 修改所选 </a>
            <a id="removeBtn" class="btn btn-sm btn-default" ng-click="vm.removeDetail();">
                <i class="fa fa-remove"></i> 删除所选 </a>
            <a id="expandAllBtn" class="btn btn-sm btn-default" ng-click="vm.expandAll();">
                <i class="glyphicon glyphicon-chevron-down"></i> 展开/折叠 </a>
        </div>
    </div>
    <div class="portlet-body">
        <%--<table id="treeTable" class="table table-striped table-hover table-bordered  no-footer "></table>--%>
        <div id="treeTable" class="table table-striped table-hover table-bordered  no-footer "></div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'" ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade draggable-modal" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">项目预算详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">预算类型</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.typeName" placeholder=""
                                       ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">是否为公共预算</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                        ng-model="vm.detail.publicBudget" class="form-control"
                                        ng-disabled="!processInstance.myRunningTaskName.indexOf('预算管理专员')>=0||vm.detail.isParent"></select>
                            </div>
                        </div>
                        <div class="form-group" ng-if="vm.detail.publicBudget">
                            <label class="col-md-3 control-label required">可用预算部门</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.detail.publicDeptIds"
                                           name="publicDeptIds" maxlength="100"
                                           disabled/>
                                    <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal1(vm.detail.publicDeptIds);">
                                                <i class="fa fa-cog"></i></button>
                                        </span>
                                </div>
                                <span class="help-block" >请点击后方按钮选择</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">上级类型</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.detail.parentName"
                                           placeholder="" disabled="disabled">
                                    <span class="input-group-btn"><button class="btn default" type="button"
                                                                          ng-click="vm.showParentTree(vm.detail)"
                                                                          ng-disabled="!processInstance.firstTask"><i
                                            class="fa fa-asterisk"></i></button></span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">上年预算</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control" ng-model="vm.detail.lastYearMoney">
                                    <span class="input-group-addon">万元</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">上年预计完成</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control" ng-model="vm.detail.lastYearSuccess">
                                    <span class="input-group-addon">万元</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">本年预算金额</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control" ng-model="vm.detail.budgetMoney">
                                    <span class="input-group-addon">万元</span>
                                </div>
                                <span style="color: red" ng-if="vm.detail.foreignKey!=0">数据来源:同步数据</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">本年预算占比</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.budgetProportion" disabled>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">排序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.detail.seq" ng-disabled="!processInstance.firstTask"
                                       placeholder="请输入整数">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.remark"  ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">创建人</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.item.creatorName"
                                       disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">创建时间</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control"
                                       value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-show="processInstance.myRunningTaskName.indexOf('预算管理专员')>=0" ng-click="vm.saveDetail();">
                    保存
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="detailParentTreeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择父级</h4>
            </div>
            <div class="modal-body">
                <div id="detail_parent_tree"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.chooseDetailParent();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade" id="selectFeeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">预算项目</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qFee"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px;overflow-y: auto">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th style="width: 160px;">项目名</th>
                            <th style="width: 160px;">部门名称</th>
                            <th style="width: 160px;">预算年份</th>
                            <th style="width: 180px;">预算总金额</th>
                            <th>备注</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="c in vm.fees">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.projectNo==c.id" class="cb_fee" data-name="{{c}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="c.title"></td>
                            <td ng-bind="c.deptName"></td>
                            <td ng-bind="c.budgetYear"></td>
                            <td ng-bind="c.budgetTotalMoney"></td>
                            <td ng-bind="c.remark"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectFee();">确认</button>
            </div>
        </div>
    </div>
</div>

