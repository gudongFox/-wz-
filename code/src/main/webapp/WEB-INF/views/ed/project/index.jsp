
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" style="margin: -10px 0px 5px 0px;padding:0px;background-color: #F1F3FA;">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="dashboard">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="me.edProject">质量管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>贯标项目管理</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> 项目信息
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-2">
                <input type="text" class="form-control input-sm" placeholder="关键字"
                       ng-model="vm.params.qName"/>
            </div>
            <div class="col-md-4">
                <a class="btn green btn-sm" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
            </div>
        </div>

        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th>合同号</th>
                    <th>项目名称</th>
                    <th>部门名称</th>
                    <th>项目类型</th>
                    <th>贯标阶段</th>
                    <th>项目负责人</th>
                    <th>执行负责人</th>
                    <th style="width: 150px;">创建日期</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.contractNo"></td>
                    <td ng-bind="item.projectName"></td>
                    <td ng-bind="item.deptName"></td>
                    <td ng-bind="item.projectType"></td>
                    <td ng-bind="item.stageNames"></td>
                    <td ng-bind="item.chargeMenName"></td>
                    <td ng-bind="item.exeChargeMenName"></td>
                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <span class="label label-sm label-success" style="cursor: pointer;" ng-click="vm.edit(item);">设计管理</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>

