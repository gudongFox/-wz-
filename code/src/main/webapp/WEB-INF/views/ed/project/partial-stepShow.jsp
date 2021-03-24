<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 进度概览
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showStepDetail();">
                <i class="fa fa-info"></i> 分期信息 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="panel panel-info">
            <!-- Default panel contents -->
            <div class="panel-heading">
                <h3 class="panel-title" ng-bind="vm.data.stepName+'【'+vm.data.stageName+'】'"></h3>
            </div>
            <!-- List group -->

            <div class="table-scrollable">
                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 35px;">#</th>
                        <th>流程名称</th>
                        <th>流程责任人</th>
                        <th>流程图</th>
                        <th>当前分期流程进行情况</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.data.list">
                        <td ng-bind="$index+1"></td>
                        <td ng-bind="item.processDefinitionName"></td>
                        <td ng-bind="item.firstUserTaskName"></td>
                        <td >
                            <a href="javascript:;"  ng-click="vm.showPng(item);"><i class="fa fa-search"></i> 流程图 </a>
                        </td>
                        <td ng-bind="item.processName"></td>
                    </tr>
                    </tbody>
                </table>
        </div>
    </div>
</div>

<div class="modal fade" id="showPngModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title"><span ng-bind="vm.processDefinitionName +'-'+vm.processDefinitionKey"></span></h4>
            </div>
            <div class="modal-body">
                <img ng-src="{{'/myAct/downloadPngByProcessDefinitionKey/'+vm.processDefinitionKey}}"  style="max-width: 99%;"/>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>



