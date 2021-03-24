<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);" ng-show="processInstance.refresh">
    <i class="fa fa-refresh"></i> 刷新 </a>

<a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.passAble"
   ng-click="vm.save();" >
    <i class="fa fa-save"></i> 保存 </a>

<a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.fetchAble"
   ng-click="commonFetchFlow();">
    <i class="fa fa-send-o" title="后续流程用户未接收任务"></i> 取回任务 </a>

<a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.passAble"
   ng-click="vm.showSendFlow();">
    <i class="fa fa-send"></i>  {{processInstance.firstTask?'提交审批':'同意通过'}} </a>

<a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.backAble"
   ng-click="commonBackFlow();">
    <i class="fa fa-reply"></i> 打回修改 </a>

<a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.returnBack"
   ng-click="back();">
    <i class="fa fa-arrow-left"></i> 返回 </a>
