<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="table-scrollable">
    <table class="table table-striped table-bordered table-advance table-hover">
        <thead>
        <tr>
            <th style="width: 50px;">#</th>
            <th>办理节点</th>
            <th style="width: 100px;">办理人</th>
            <th style="width: 150px;">指派时间</th>
            <th style="width: 150px;">办理时间</th>
            <th>办理意见</th>
        </tr>
        </thead>
        <tbody>
        <tr ng-repeat="history in historyList">
            <td ng-bind="$index+1"></td>
            <td>
                                <span ng-if="!history.finished">
                                      <i class="fa fa-clock-o"></i>
                                      <strong ng-bind="history.activityName"></strong>
                                </span>
                <span ng-if="history.finished" ng-bind="history.activityName"></span>
            </td>
            <td ng-bind="history.userName"></td>
            <td ng-bind="history.startTime|date:'yyyy-MM-dd HH:mm'"></td>
            <td ng-bind="history.endTime|date:'yyyy-MM-dd HH:mm'"></td>
            <td>
                <i class="fa fa-mail-reply" title="打回" ng-if="!history.passed"></i>
                <span ng-bind="history.comment"></span>
            </td>
        </tr>
        </tbody>
    </table>
</div>



