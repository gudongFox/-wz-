
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">综合办公</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>会议室申请</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span>会议室申请</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                <i class="fa fa-plus"></i> 新建 </a>

        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div >
                <div class="col-md-1 ">
                <label style="margin-left: 22px;margin-top: 7px">会议室名称：</label>
                </div>

                <div class="col-md-2 ">
                    <select ng-options="s.id as s.roomName for s in vm.meetingRoom"
                            ng-model="vm.params.roomId" class="form-control">
                    </select>
                </div>
                <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>

                <a class="btn green btn-sm defaultBtn" ng-click="vm.showStatisticByUserLogin();"><i class="fa fa-search"></i> 统计 </a>
                <a  class="btn btn-sm default file input-button"
                    ng-href="{{'/oa/meetingRoomApply/downloadExcel.json'}}" target="_blank">
                    <i class="fa fa-file"></i> 导出数据 </a>
            </div>
        </div>
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>会议名称</th>
                    <th>会议室</th>
                    <th>开始时间</th>
                    <th>结束时间</th>
                    <th style="width: 50px;">创建人</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in  vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.applyReason" style="color: blue" ng-click="vm.show(item.id);"></td>
                    <td ng-bind="item.meetingRoomName"></td>
                    <td ng-bind="item.beginTime | date:'yyyy-MM-dd HH:mm'"></td>
                    <td ng-bind="item.endTime | date:'yyyy-MM-dd HH:mm'"></td>
                    <td ng-bind="item.creatorName"></td>
                    <td ng-bind="item.gmtModified | date:'yyyy-MM-dd'"></td>
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

