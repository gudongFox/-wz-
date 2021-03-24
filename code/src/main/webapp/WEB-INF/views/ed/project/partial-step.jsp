<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-settings"></i><span ng-bind="vm.treeNode.nodeName+' 分期管理'"></span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();"  ng-show="vm.isShow">
                <i class="fa fa-plus"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>分期名称</th>
                    <th>工程号</th>
                    <th>执行负责人</th>
                    <th>项目负责人</th>
                    <th style="width: 80px;">协同设计</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="step in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="step.stepName" class="data_title" ng-click="vm.show(step.id);"></td>
                    <td ng-bind="step.projectNo"></td>
                    <td><strong ng-bind="step.exeChargeManName"></strong></td>
                    <td ng-bind="step.chargeManName"></td>
                    <td>
                        <span class="label label-sm label-success" style="cursor: pointer;" ng-if="step.cp"
                              ng-click="vm.showCp(step.id);">已开启</span>
                        <span class="label label-sm label-default" ng-if="!step.cp"
                              ng-click="vm.showCp(step.id);">已关闭</span>
                    </td>
                    <td ng-bind="step.gmtCreate|date:'yyyy-MM-dd'"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.show(step.id);" title="详情"></i>
                        <i class="fa fa-trash" ng-click="vm.remove(step.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>




