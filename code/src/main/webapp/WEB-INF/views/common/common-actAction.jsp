<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);"  style="font-size: 14px;line-height: 1.6">
    <i class="fa fa-refresh"></i> 刷新 </a>

<a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.sendAble" style="font-size: 14px;line-height: 1.6"
   ng-click="vm.save();" >
    <i class="fa fa-save"></i> 保存 </a>

<span ng-include="'/web/v1/tpl/actAction.html'" ng-controller="CommonActActionController" ng-if="processInstanceId" ></span>

<a href="javascript:;" class="btn btn-sm btn-default"  style="font-size: 14px;line-height: 1.6"
   ng-click="back();">
    <i class="fa fa-arrow-left"></i> 返回 </a>


