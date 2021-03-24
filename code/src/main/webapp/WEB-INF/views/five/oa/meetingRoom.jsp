
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
            <span>会议室管理</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span>会议室管理</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
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
                <label style="margin-left: 20px;">会议室名称：<input type="search" class="form-control input-inline input-sm" placeholder="会议室名称" ng-model="vm.pageInfo.roomName"></label>
                <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
            </div>
        </div>
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="min-width: 200px;">会议室名称</th>
                    <th style="width: 130px;">会议室容量</th>
                    <th style="width: 220px;">会议室城市</th>
                    <th style="width: 220px;">会议室所在楼</th>
                    <th style="width: 220px;">会议室所在层</th>
                    <th>会议室设备</th>
                    <th style="width: 70px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="item.name" style="color: blue" ng-click="vm.show(item.meetingroom_id);"></td>
                    <td ng-bind="item.capacity"></td>
                    <td ng-bind="item.city"></td>
                    <td ng-bind="item.building"></td>
                    <td ng-bind="item.floor"></td>
                    <td ng-bind="item.equipmentNames"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.meetingroom_id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.remove(item.meetingroom_id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

