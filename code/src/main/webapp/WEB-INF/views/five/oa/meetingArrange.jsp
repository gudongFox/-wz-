
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:57})">会议管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">会议室管理</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span ng-bind="tableName">会议室管理</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                <i class="fa fa-plus"></i> 新建 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>

        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div >
                <div >
                    <label style="margin-left: 20px;">关键字：<input type="search" class="form-control input-inline input-sm" placeholder="关键字" ng-model="vm.roomName"></label>
                    <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
                </div>

            </div>
        </div>
        <div class="table-scrollable">
        <table class="table table-striped table-hover table-bordered table-advance no-footer">
            <thead>
            <tr>
                <th style="width: 40px;">#</th>
                <th style="min-width: 200px;">会议名称</th>
                <th style="width: 160px;">会议时间</th>
                <th style="width: 120px;">会议主持人</th>
                <th style="width: 120px;">参会领导</th>
                <th style="width: 140px;">参与人</th>
                <th style="width: 70px;">创建人</th>
                <th style="width: 160px;">创建时间</th>
                <th style="width: 200px;">流程状态</th>
                <th style="width: 50px;">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr ng-repeat="item in  vm.pageInfo.list">
                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                <td ng-bind="item.meetingName" style="color: blue" ng-click="vm.show(item.id);"></td>
                <td ng-bind="item.meetingTime"></td>
                <td ng-bind="item.hostManName"></td>
                <td ng-bind="item.chargeLeaderName"></td>
                <td ng-bind="item.attendUserName"></td>
                <td ng-bind="item.creatorName"></td>
                <td ng-bind="item.gmtModified | date:'yyyy-MM-dd HH:mm'"></td>
                <td ng-bind="item.processName"></td>
                <td>
                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                    <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="item.creator==user.userLogin"></i>
                </td>
            </tr>
            </tbody>
        </table>
    </div>

        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>

