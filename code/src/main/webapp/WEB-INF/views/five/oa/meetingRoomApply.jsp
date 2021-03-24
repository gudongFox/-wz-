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
            <span ng-bind="tableName">会议室申请</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> <span ng-bind="tableName">会议室申请</span>
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
            <div>
                <label style="margin-left: 20px;">查询日期：<input type="text" id="endDate"
                                                              class="form-control input-inline input-sm date-picker-start-now"
                                                              placeholder="查询日期" ng-model="vm.now"></label>
                <a class="btn green btn-sm" ng-click="vm.loadData();"><i class="fa fa-search"></i> 查询 </a>
            </div>
        </div>
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 40px;">#</th>
                    <th style="width: 120px;">预定人</th>
                    <th style="width: 200px;">会议室</th>
                    <th style="width: 150px;">开始时间</th>
                    <th style="width: 150px;">结束时间</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in  vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td>
                        <img alt="" class="img-circle" ng-src="{{item.bookUserInfo.thumb_avatar}}" onerror="this.src='/assets/img/avatar.jpg'" style="height: 22px;width: 22px;margin-top: -5px;margin-right: 10px;" />
                        <span ng-bind="item.bookUserInfo.name" title="">罗冬</span>
                    </td>
                    <td ng-bind="item.meetingRoom.name"></td>
                    <td ng-bind="item.startTime | date:'yyyy-MM-dd HH:mm'"></td>
                    <td ng-bind="item.endTime | date:'yyyy-MM-dd HH:mm'"></td>
                    <td></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

