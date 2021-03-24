<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-settings"></i> 项目分期管理
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="vm.isShow">
                <i class="fa fa-plus"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th>项目阶段</th>
                    <th>分期名称</th>
                    <th>合同号</th>
                    <th>负责人</th>
                    <th>执行负责人</th>
                    <th style="width: 80px;">协同设计</th>
                    <th style="width: 150px;">修改时间</th>
                    <th style="width: 80px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="step in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="step.stageName" class="data_title" ng-click="vm.show(step.id);"></td>
                    <td ng-bind="step.stepName"></td>
                    <td ng-bind="step.contractNo"></td>
                    <td ng-bind="step.chargeManName"></td>
                    <td ng-bind="step.exeChargeManName"></td>
                    <td>
                        <span class="label label-sm label-success" style="cursor: pointer;" ng-if="step.cp"
                              ng-click="vm.showCp(step.id);">已开启</span>
                        <span class="label label-sm label-default" ng-if="!step.cp"
                              ng-click="vm.showCp(step.id);">已关闭</span>
                    </td>
                    <td ng-bind="step.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.show(step.id);" title="详情"></i>
                        <i class="fa fa-trash" ng-click="vm.remove(step.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>




