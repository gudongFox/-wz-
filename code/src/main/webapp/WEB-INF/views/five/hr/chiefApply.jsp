<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar"  id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>人资管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>{{vm.applyType}}认定申报表</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> {{vm.applyType}}认定申报表
                </div>
                <div class="actions">

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click=" vm.loadData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                <i class="fa fa-plus"></i> 创建 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
                </div>            </div>
            <div class="portlet-body">
                <div class="row">
                    <div class="col-md-12">

                        <label style="margin-right: 10px;">关键字：<input type="search"
                                                                       class="form-control input-inline input-sm"
                                                                       placeholder="申报年度|姓名"
                                                                       ng-model="vm.params.qYear"></label>

                        <label style="margin-right: 10px;">部门名称：<input type="search"
                                                                       class="form-control input-inline input-sm"
                                                                       placeholder="部门名称"
                                                                       ng-model="vm.params.qDeptName"></label>

                        <a class="btn green btn-sm" ng-click="vm.queryData();"><i
                                class="fa fa-search"></i> 查询 </a>
                    </div>
                </div>
                <div class="dataTables_wrapper no-footer">

                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px">#</th>
                                <th style="width: 90px">申报年度</th>
                                <th style="width: 90px">姓名</th>
                                <th>部门名称</th>
                                <th>职务</th>
                                <th style="width: 90px">任职时间</th>
                                <th >申请承担设计项目类型</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 160px">任务状态</th>
                                <th style="width: 60px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.checkYear+'年'"  class="data_title" ng-click="vm.show(item.id);"></td>
                                <td ng-bind="item.chiefUserName"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.position"></td>
                                <td ng-bind="item.titleTime"></td>

                                <td ng-bind="item.projectTypeApply"></td>

                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd '"></td>
                                <td ng-bind="item.processName"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-5" ng-click="vm.remove(item.id);" title="删除"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>

                </div>
            </div>
        </div>
    </div>


</div>


