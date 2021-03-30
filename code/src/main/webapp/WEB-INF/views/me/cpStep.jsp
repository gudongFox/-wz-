
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>项目管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">项目档案管理</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span ng-bind="tableName">协同项目信息</span>
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
                                                               placeholder="关键字"
                                                               ng-model="vm.params.qProjectName"></label>

                <label style="margin-right: 10px;">分期名称：<input type="search"
                                                               class="form-control input-inline input-sm"
                                                               placeholder="关键字"
                                                               ng-model="vm.params.qStepName"></label>
                <label style="margin-right: 10px;">合同签订时间：<input type="search"
                                                               class="form-control input-inline input-sm"
                                                               placeholder="关键字"
                                                               ng-model="vm.params.qSignTime"></label>
                <a class="btn green btn-sm" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
            </div>
        </div>
        <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>项目名称</th>
                    <th>分期名称</th>

                    <th style="width: 70px;">项目类型</th>
                    <th style="width: 70px;">子项数量</th>
                    <th>部门名称</th>
                    <th style="width: 120px;">设计阶段</th>
                    <th style="width: 70px;">分期负责人</th>
<%--                    <th style="width: 90px;">创建日期</th>--%>
                    <th style="width: 120px;">签订日期</th>
                    <th style="width: 70px;">AUTOCAD</th>
                    <th style="width: 70px;">快捷菜单</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="item.projectName"></td>
                    <td ng-bind="item.stepName" ng-click="vm.showCp(item.id);" class="data_title" ></td>

                    <td ng-bind="item.projectType"></td>
                    <td ng-bind="item.buildCount"></td>
                    <td ng-bind="item.deptName"></td>
                    <td ng-bind="item.stageName"></td>
                    <td ng-bind="item.chargeManName"></td>
<%--                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>--%>
                    <td ng-bind="item.signTime"></td>
                    <td  ng-click="vm.updateCadHide(item.id)">
                        <span ng-if="item.cadHide"  class="data_title">隐藏</span>
                        <span ng-if="!item.cadHide" class="data_title">显示</span>
                    </td>
                    <td>
                        <span class="label label-sm label-success" style="cursor: pointer;" ng-click="vm.showCp(item.id);">协同设计</span>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>

    </div>
</div>

