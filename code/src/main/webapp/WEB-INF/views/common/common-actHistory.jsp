<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<div class="table-scrollable">--%>
<%--    <table class="table table-striped table-bordered table-advance table-hover">--%>
<%--        <thead>--%>
<%--        <tr>--%>
<%--            <th style="width: 35px;">#</th>--%>
<%--            <th style="width: 120px;">办理人</th>--%>
<%--            <th style="width: 200px;">办理节点</th>--%>
<%--            <th style="width: 150px;">指派日期</th>--%>
<%--            <th style="width: 150px;">接收日期</th>--%>
<%--            <th style="width: 150px;">结束日期</th>--%>
<%--            <th style="width: 80px;">任务状态</th>--%>
<%--            <th>办理意见</th>--%>
<%--        </tr>--%>
<%--        </thead>--%>
<%--        <tbody>--%>
<%--        <tr ng-repeat="history in historyList"--%>
<%--            style="{{(!history.endTime&&history.endTime==null)?'color:blue;font-weight:bold;':''}}">--%>
<%--            <td ng-bind="$index+1"></td>--%>

<%--            <td>--%>
<%--                <img alt="" class="img-circle" ng-src="{{'/sys/attach/download/'+history.headAttachId}}"--%>
<%--                     onerror="this.src='/m/admin/layout/img/avatar9.jpg'" style="height: 25px;width: 25px;"/>--%>
<%--                <span ng-bind="history.assigneeName"></span>--%>
<%--            </td>--%>
<%--            <td>--%>
<%--                <i class="fa fa-clock-o" ng-if="!history.endTime"></i>--%>
<%--                <span ng-bind="history.name"></span>--%>
<%--            </td>--%>
<%--            <td ng-bind="history.startTime|date:'yyyy-MM-dd HH:mm'"></td>--%>
<%--            <td><strong ng-bind="history.claimTime|date:'yyyy-MM-dd HH:mm'"></strong></td>--%>
<%--            <td ng-bind="history.endTime|date:'yyyy-MM-dd HH:mm'"></td>--%>
<%--            <td ng-bind="history.opt"></td>--%>
<%--            <td>--%>
<%--                <span ng-bind="history.comment"></span>--%>
<%--            </td>--%>
<%--        </tr>--%>
<%--        </tbody>--%>
<%--    </table>--%>
<%--</div>--%>
<style type="text/css">

    #taskTable td {
        line-height: 1.7;
    }

</style>

<div class="table-scrollable">
    <table class="table table-striped table-bordered table-advance table-hover" id="taskTable">
        <thead>
        <tr>
            <th style="width:35px;">#</th>
            <th style="width:100px;">办理人</th>
            <th style="width:200px;">任务名称</th>
            <th style="width:150px;">开始时间</th>
            <th style="width:150px;" class="hidden-md hidden-sm hidden-xs">接收时间</th>
            <th style="width:150px;">结束时间</th>
            <th>处理意见</th>
        </tr>
        </thead>
        <tbody>
        <tr role="row" class="odd" ng-repeat="task in actTaskList">
            <td ng-bind="$index+1"></td>
            <td>
                <img alt="" class="img-circle"
                     ng-src="{{'/common/user/head/'+task.assignee}}"
                     onerror="this.src='/assets/img/avatar.jpg'"
                     style="height: 22px;width: 22px;margin-top: -5px;"/>
                <span ng-bind="task.assigneeName" title="{{task.ownerName}}"></span>
            </td>
            <td>
                <i class="fa fa-clock-o" ng-if="!task.endTime" style="color: blue;"></i>
                <span ng-if="task.endTime">
                     <i class="fa fa-check font-green" title="通过" ng-if="task.passed"></i>
                     <i class="fa fa-times font-red" title="打回" ng-if="!task.passed"></i>
                </span>

                <span ng-if="task.ccTask">
                      <span ng-bind="task.name" style="font-weight: bold;"></span>

                    <span ng-if="task.ccFinishAble"
                          ng-click="showCcFinishActTaskModal(task.id);" style="color: blue;cursor: pointer;">完成</span>
                </span>
                <span ng-if="!task.ccTask">
                     <span ng-if="!adminMode" ng-bind="task.name" style="font-weight: bold;"></span>
                     <span ng-if="adminMode" ng-bind="task.name"
                           style="cursor: pointer;font-weight: bold;"
                           ng-click="showActTaskModal(task.id);"></span>
                </span>
            </td>
            <td ng-bind="task.startTime|date:'yyyy-MM-dd HH:mm'"></td>
            <td ng-bind="task.claimTime|date:'yyyy-MM-dd HH:mm'" class="hidden-md hidden-sm hidden-xs"></td>
            <td ng-bind="task.endTime|date:'yyyy-MM-dd HH:mm'"></td>
            <td>
                                                        <span ng-if="task.endTime">
                                                              <span ng-if="task.passed"
                                                                    style="color: green;font-weight: bold;">通过 | </span>
                                                              <span ng-if="!task.passed"
                                                                    style="color: red;font-weight: bold;">打回 | </span>
                                                        </span>
                <span ng-bind="task.latestComment" ng-click="showActCommentModal(task.id);" style="cursor: pointer;"></span>
            </td>
        </tr>
        </tbody>
    </table>
</div>



