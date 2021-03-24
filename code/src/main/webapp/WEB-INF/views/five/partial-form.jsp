<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="{{vm.treeData.icon}}"></i> <span ng-bind="vm.treeData.nodeName"></span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.newData();">
                <i class="fa fa-plus"></i> 创建 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="dataTables_wrapper no-footer">
            <div class="table-scrollable">
                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 30px;">#</th>
                        <th ng-repeat="head in vm.data.heads track by $index" ng-bind="head.text" ng-style="head.style"></th>
                        <th style="width: 70px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr role="row" class="odd" ng-repeat="item in vm.data.list">
                        <td ng-bind="$index+1"></td>
                        <td ng-repeat="property in item.propertyList  track by $index" ng-bind="property.text" ng-style="property.style"></td>
                        <td>
                            <i class="fa fa-edit" style="margin-left: 5px;cursor: pointer;" title="编辑" ng-click="vm.showDetail(item.id);"/>
                            <i class="fa fa-trash" style="margin-left: 5px;cursor: pointer;" title="删除" ng-click="vm.remove(item.id);" ng-show="!item.processEnd"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


