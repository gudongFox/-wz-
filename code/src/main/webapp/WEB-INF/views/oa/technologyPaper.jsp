
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a >技术质量</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>科技质量管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a >论文提交</a>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span>论文提交</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                <i class="fa fa-plus"></i> 新建 </a>

        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div >
                <label style="margin-left: 20px;">标题：<input type="search"
                                                              class="form-control input-inline input-sm"
                                                              placeholder="标题"
                                                              ng-model="vm.params.technologyTitles"></label>

                <label style="margin-right: 20px;">部门：<input type="search"
                                                               class="form-control input-inline input-sm"
                                                               placeholder="部门"
                                                               ng-model="vm.params.deptNames"></label>

                <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
            </div>

        </div>

        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>标题</th>
                    <th>类别</th>
                    <th>所属部门</th>
                    <th style="width: 50px;">创建者</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 130px;">任务状态</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in  vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.technologyTitle"  class="data_title" ng-click="vm.show(item.id,item.stampType);"></td>
                    <td ng-bind="item.technologyType"></td>
                    <td ng-bind="item.deptName"></td>
                    <td ng-bind="item.creatorName"></td>
                    <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                    <td ng-bind="item.processName"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id,item.stampType);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="item.creator==user.userLogin"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>

