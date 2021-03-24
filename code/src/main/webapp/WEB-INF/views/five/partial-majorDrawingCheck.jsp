<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-user"></i> 专业图纸验收清单
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                <i class="fa fa-plus"></i> 创建 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.downExcel();">
                <i class="fa fa-download"></i> 导出 </a>
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
                        <th>项目名称</th>
                        <th style="width: 120px;">设计阶段</th>
                        <th style="width: 100px;">验收单号</th>
                        <th style="width: 100px;">日期</th>
                        <th style="width: 120px;">交验人</th>
                        <th style="width: 80px;">电话</th>
                        <th style="width: 60px;">创建人</th>
                        <th style="width: 100px;">创建时间</th>
                        <th style="width: 150px;">任务状态</th>
                        <th style="width: 60px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.list">
                        <td ng-bind="$index+1"></td>
                        <td class="data_title" ng-click="vm.showDetail(item.id);">
                            <span ng-bind="item.projectName" ></span>
                        </td>
                        <td ng-bind="item.stageName"></td>
                        <td ng-bind="item.formNo"></td>
                        <td ng-bind="item.checkTime"></td>
                        <td ng-bind="item.applyManName"></td>
                        <td ng-bind="item.applyPhone"></td>
                        <td ng-bind="item.creatorName"></td>
                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                        <td ng-bind="item.processName"></td>
                        <td>
                            <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetail(item.id);" title="详情"></i>
                            <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="item.creator==user.userLogin&&!item.processEnd"></i>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


