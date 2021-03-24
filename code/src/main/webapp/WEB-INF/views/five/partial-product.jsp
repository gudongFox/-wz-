<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-star"></i> 设计文件图纸验收清单
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
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
                        <th style="width: 35px;">#</th>
                        <th>分期名称</th>
                        <th style="width: 140px;">验收单号</th>
                        <th style="width: 90px;">验收日期</th>
                        <th style="width: 60px;">验收人</th>
                        <th style="width: 100px;">所属部门</th>
                        <th style="width: 60px;">创建人</th>
                        <th style="width: 100px;">创建时间</th>
                        <th style="width: 190px;">任务状态</th>
                        <th style="width: 50px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.list">
                        <td ng-bind="$index+1"></td>
                        <td ng-bind="item.stepName" class="data_title" ng-click="vm.show(item.id);"></td>
                        <td ng-bind="item.formNo"></td>
                        <td ng-bind="item.productDate"></td>
                        <td ng-bind="item.userName"></td>
                        <td ng-bind="item.deptName"></td>
                        <td ng-bind="item.creatorName"></td>
                        <td ng-bind="item.gmtModified|date:'yyyy-MM-dd'"></td>
                        <td ng-bind="item.processName"></td>
                        <td>
                            <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                            <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="item.creator==user.userLogin&&!item.processEnd"></i>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


