<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i>  <span style="font-size: 16px" ng-bind="'文件夹详情 '+item.cnName"></span>
        </div>

        <div class="actions" style="float:right">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="loadData()"
               style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"  style="font-size: 14px"
               ng-click="save();" >
                <i class="fa fa-save"></i> 保存 </a>
        </div>
    </div>
    <div class="portlet-body">
        <span ng-include="'/web/v1/tpl/infoDir.html'"></span>
    </div>
</div>


