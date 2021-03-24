<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-user"></i> 设计图纸验收清单
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
                        <th>项目名称</th>
                        <th style="width: 120px;">设计阶段</th>
                        <th style="width: 100px;">专业</th>
                        <th style="width: 100px;">交验日期</th>
                        <th style="width: 120px;">设计单位</th>
                        <th style="width: 80px;">验收人</th>
                        <th style="width: 60px;">交验人</th>
                        <th style="width: 100px;">更新时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.list">
                        <td ng-bind="$index+1"></td>
                        <td class="data_title" ng-click="vm.showDetail(item.id);">
                            <span ng-bind="item.projectName" ></span>
                            <i class="fa fa-star" ng-show="item.defaultItem" title="正在生效的配置!"></i>
                        </td>
                        <td ng-bind="item.stageName"></td>
                        <td ng-bind="item.majorName"></td>
                        <td ng-bind="item.checkTime"></td>
                        <td ng-bind="item.deptName"></td>
                        <td ng-bind="item.handMan"></td>
                        <td ng-bind="item.applyManName"></td>
                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


