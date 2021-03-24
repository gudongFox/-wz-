<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-settings"></i> 项目期次管理
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                <i class="fa fa-plus"></i> 新增 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 40px;">#</th>
                    <th>期次名称</th>
                    <th style="width: 130px;">项目阶段</th>
<%--                    <th>合同号</th>--%>
                    <th style="width: 130px;">项目总师</th>
                    <th style="width: 130px;">项目经理</th>
                    <th style="width: 130px;">协同设计</th>
                    <th style="width: 150px;">修改时间</th>
                    <th style="width: 60px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="step in vm.list">
                    <td ng-bind="$index+1"></td>
                    <td ng-bind="step.stepName" class="data_title" ng-click="vm.show(step.id);"></td>
                    <td ng-bind="step.stageName" ></td>
<%--                    <td ng-bind="step.contractNo"></td>--%>
                    <td ng-bind="step.chargeManName"></td>
                    <td ng-bind="step.exeChargeManName"></td>
                    <td>
                        <span class="label label-sm label-success" ng-if="step.cp" style="cursor: pointer;"
                              ng-click="vm.showCp(step.id);">查看详情</span></td>
                    <td ng-bind="step.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
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




