<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
    <i class="fa fa-refresh"></i> 刷新 </a>

<a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.passAble"
   ng-click="vm.save();" >
    <i class="fa fa-save"></i> 保存 </a>

<a href="javascript:;" class="btn btn-sm btn-default"
   ng-click="back();">
    <i class="fa fa-arrow-left"></i> 返回 </a>
