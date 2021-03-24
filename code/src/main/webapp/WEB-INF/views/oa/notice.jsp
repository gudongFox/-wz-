
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>

        </li>
        <li>
            <i class="fa fa-angle-right"></i>
            <a ui-sref="">综合办公</a>
        </li>
        <li>
            <i class="fa fa-angle-right"></i>
            <span>信息发布</span>

        </li>
        <li>
            <i class="fa fa-angle-right"></i>
            <span ng-bind="vm.params.modelName"></span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span>信息发布</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add(); " ng-show="vm.addRight">
                <i class="fa fa-plus"></i> 新建 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="table-scrollable">
            <table filter class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>公告标题</th>
                    <th>发布部门</th>
                    <th style="width: 90px">公告类别</th>
                    <th style="width: 200px">发布人</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 130px;">任务状态</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-if="!vm.multiple" ng-repeat="item in  vm.pageInfo.list"  on-finish-render>
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.noticeTitle"  class="data_title" ng-click="vm.show(item.id,item.stampType);"></td>
                    <td ng-bind="item.deptName"></td>
                    <td ng-bind="item.noticeType"></td>
                    <td ng-bind="item.publishUserName"></td>
                    <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                    <td ng-bind="item.processName"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-show="vm.editRight||item.creator==user.userLogin"  ng-click="vm.edit(item.id,item.stampType);" title="修改"></i>
                        <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="vm.deleteRight||item.creator==user.userLogin" ></i>
                    </td>
                </tr>
                <tr ng-if="vm.multiple" ng-repeat="item in  vm.pageInfo.categoryRows"  on-finish-render>
                    <td colspan="8" ng-if="item.rowType=='category'" ng-bind="item.content"></td>
                    <td ng-if="item.rowType=='data'" class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-if="item.rowType=='data'" ng-bind="item.content.noticeTitle"  class="data_title" ng-click="vm.show(item.content.id,item.content.stampType);"></td>
                    <td ng-if="item.rowType=='data'" ng-bind="item.content.deptName"></td>
                    <td ng-if="item.rowType=='data'" ng-bind="item.content.noticeType"></td>
                    <td ng-if="item.rowType=='data'" ng-bind="item.content.publishUserName"></td>
                    <td ng-if="item.rowType=='data'" ng-bind="item.content.gmtCreate | date:'yyyy-MM-dd'"></td>
                    <td ng-if="item.rowType=='data'" ng-bind="item.content.processName"></td>
                    <td ng-if="item.rowType=='data'">
                        <i class="fa fa-pencil margin-right-5" ng-show="vm.addRight||item.content.creator==user.userLogin"  ng-click="vm.edit(item.content.id,item.content.stampType);" title="修改"></i>
                        <i class="fa fa-trash-o" ng-click="vm.remove(item.content.id);" title="删除" ng-show="vm.deleteRight||item.content.creator==user.userLogin" ></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>

