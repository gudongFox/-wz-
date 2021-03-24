<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 项目概览
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="panel panel-info" ng-repeat="data in vm.steps">
            <!-- Default panel contents -->
            <div class="panel-heading">
                <h3 class="panel-title" ng-bind="data.stepName+'【'+data.stageName+'】'"></h3>
            </div>
            <!-- List group -->
            <ul class="list-group" >
                <li class="list-group-item" ng-repeat="item in data.list">
                    <span ng-bind="item.processDefinitionName"></span>
                    <span style="float: right;" ng-bind="item.processTime|date:'yyyy-MM-dd HH:mm'"></span>
                    <span style="float: right;margin-right: 10px" ng-bind="item.processName"></span>
                </li>
            </ul>
        </div>
    </div>
</div>



