
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
            <span ng-bind="tableName">车辆维护</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span ng-bind="tableName">车辆维护</span>
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
                    <label style="margin-left: 22px;margin-top: 7px">车牌号：</label>
                </div>
                <div class="col-md-2 ">
                    <select ng-options="s.id as s.carNo for s in vm.oaCars"
                            ng-model="vm.params.carId" class="form-control">
                    </select>
                </div>
                <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>

                <a class="btn green btn-sm defaultBtn" ng-click="vm.showStatisticByUserLogin();"><i class="fa fa-search"></i> 统计 </a>
            </div>
        </div>
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="width: 120px">车辆牌号</th>
                    <th>维护类型</th>
                    <th>备注</th>
                    <th style="width: 50px;">创建人</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 150px">流程状态</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in  vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.carNo" style="color: blue" ng-click="vm.show(item.id);"></td>
                    <td ng-bind="item.type"></td>
                    <td ng-bind="item.remark"></td>
                    <td ng-bind="item.creatorName"></td>
                    <td ng-bind="item.gmtModified | date:'yyyy-MM-dd'"></td>
                    <td>
                        <span ng-bind="item.processName"></span>
                    </td>
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

