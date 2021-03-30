<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="me.edProject">项目管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">全部运行项目</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> <span ng-bind="tableName">项目信息</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>

        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-12">
                <label style="margin-right: 10px;">项目名称：<input type="search"
                                                               class="form-control input-inline input-sm"
                                                               placeholder="项目名称"
                                                               ng-model="vm.params.q"></label>

                <a class="btn green btn-sm" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
<%--                <a class="btn btn-sm green" ng-click="vm.downloadModelExcel()" target="_blank"><i class="fa fa-cloud-download"></i> 导出合同信息 </a>--%>
            </div>
        </div>
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>项目名称</th>
                    <th style="width: 150px;">合同号</th>
                    <th style="width: 150px;" ng-if="vm.appCode=='wuzhou'" >项目号</th>
                    <th>部门名称</th>
<%--                    <th style="width: 100px;">项目类型</th>--%>
                    <th style="width: 120px;">项目负责人</th>
                    <th style="width: 120px;">执行负责人</th>
                    <th style="width: 110px;">立项日期</th>
                    <th style="width: 60px;">快捷菜单</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.pageInfo.list">
                    <td ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-click="vm.show(item);"  ng-bind="item.projectName"  class="data_title" ></td>
                    <td ng-bind="item.contractNo"></td>
                    <td ng-bind="item.businessContractDetail.projectNo" ng-if="vm.appCode=='wuzhou'" ></td>
                    <td ng-bind="item.deptName"></td>
<%--                    <td ng-bind="item.projectType"></td>--%>
                    <td ng-bind="item.chargeMenName"></td>
                    <td ng-bind="item.exeChargeMenName"></td>
                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                    <td>
                        <span class="label label-sm label-success" style="cursor: pointer;" ng-click="vm.show(item);" ng-show="item.ed">项目详情</span>
                        <span class="label label-sm label-default" style="cursor: pointer;" ng-click="vm.show(item);" ng-show="!item.ed">项目详情</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>
