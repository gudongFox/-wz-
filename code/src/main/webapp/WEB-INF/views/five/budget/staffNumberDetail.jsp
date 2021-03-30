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
            <span ng-bind="tableName">职工人数预算</span>
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
            <i class="icon-note"></i><span ng-bind="tableName">职工人数预算</span>
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
                                <label class="col-md-2 control-label">填报部门</label>
                                <div class="col-md-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                    <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                </div>
                                </div>
                                <label class="col-md-2 control-label">预算总人数</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.budgetTotalMoney" name="budgetTotalMoney" readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">预算年份</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker-year" id="budgetYear">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.budgetYear"   required="true" disabled="disabled">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">流程标题</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.title" name="title" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"  ng-disabled="!processInstance.firstTask"/>
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
    <div class="portlet-title" >
        <div class="caption">
            <i class="fa fa-file"></i> <span ng-bind="vm.item.lastYear+' 预算明细'"></span>
        </div>
        <div class="tools">
            <a href="javascript:;" class="collapse" data-original-title="" title=""></a>
        </div>
    </div>
    <div class="portlet-body" >
        <%--<table id="treeTable" class="table table-striped table-hover table-bordered  no-footer "></table>--%>
        <div id="lastTreeTable" class="table table-striped table-hover table-bordered  no-footer "></div>
    </div>
</div>
<div class="portlet light">
    <div class="portlet-title" >
        <div class="caption">
            <i class="fa fa-file"></i> <span ng-bind="vm.item.budgetYear+' 预算明细'"></span>
        </div>
        <div class="tools">
            <a href="javascript:;" class="collapse" data-original-title="" title=""></a>
        </div>
        <div class="actions">
            <a id="addBtn"  class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0);"
               ng-if="processInstance.firstTask">
                <i class="fa fa-plus"></i> 新增 </a>
            <a id="updateBtn"  class="btn btn-sm btn-default" ng-click="vm.showDetailModel(1);"
               ng-if="processInstance.firstTask">
                <i class="fa fa-edit"></i> 修改所选 </a>
            <a id="removeBtn"  class="btn btn-sm btn-default" ng-click="vm.removeDetail();"
               ng-if="processInstance.firstTask">
                <i class="fa fa-remove"></i> 删除所选 </a>
            <a  id="expandAllBtn" class="btn btn-sm btn-default" ng-click="vm.expandAll();"
                ng-if="processInstance.firstTask">
                <i class="glyphicon glyphicon-chevron-down"></i> 展开/折叠 </a>
        </div>
    </div>
    <div class="portlet-body">
        <%--<table id="treeTable" class="table table-striped table-hover table-bordered  no-footer "></table>--%>
        <div id="treeTable" class="table table-striped table-hover table-bordered  no-footer "></div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade draggable-modal" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">项目预算详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label required" ng-if="vm.detail.isParent">统计类型</label>
                            <label class="col-md-3 control-label required"  ng-if="!vm.detail.isParent">部门名称</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.detail.typeName" ng-disabled="!vm.detail.isParent"
                                           name="typeName" maxlength="100"/>
                                    <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"
                                                    ng-click="vm.showDeptModal(vm.opt='deptId')"> <i
                                                    class="fa fa-cog" ></i></button>
                                        </span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label required">上级类型</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.detail.parentName"
                                           placeholder="" disabled="disabled">
                                    <span class="input-group-btn" ><button class="btn default" type="button" ng-click="vm.showParentTree(vm.detail)" ng-disabled="!processInstance.firstTask"><i class="fa fa-asterisk"></i></button></span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">上年从业人数</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.lastYearEmployee" readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">本年从业人数</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.employeeNumber" ng-disabled="!processInstance.firstTask||vm.detail.isParent">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">上年职工人数</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.lastYearStaff" readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">本年职工人数</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.staffNumber" ng-disabled="!processInstance.firstTask||vm.detail.isParent">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">排序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.detail.seq"
                                       placeholder="请输入整数">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.remark">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">创建人</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.item.creatorName" disabled="disabled"/>
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
                <button type="button" class="btn blue" ng-click="vm.saveDetail();">保存</button>
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

