
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">综合办公</a>
            <i class="fa fa-angle-right font-black"></i>
        </li>
        <li>
            <span ng-bind="vm.params.deptName"></span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span ng-bind="vm.params.deptName"></span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div >
                <label style="margin-left: 20px;">公告标题：<input type="search"
                                                              class="form-control input-inline input-sm"
                                                              placeholder="公告标题"
                                                              ng-model="vm.params.noticeTitles"></label>
                <label style="margin-left: 20px;">公告类别：<input type="search"
                                                              class="form-control input-inline input-sm"
                                                              placeholder="公告类别"
                                                              ng-model="vm.params.noticeTypes"></label>
                <a class="btn green btn-sm" ng-click="vm.reloadPageData();"><i class="fa fa-search"></i> 查询 </a>
            </div>

        </div>

        <div class="table-scrollable" >
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>公告标题</th>
                    <th>发布部门</th>
                    <th style="width: 120px">公告类别</th>
                    <th style="width: 100px">发布人</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 100px;">更新时间</th>
                    <th style="width: 130px;">点击次数</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.noticeTitle"  class="data_title " ng-click="vm.show(item.id,item.stampType);"></td>
                    <td ng-bind="item.deptName"></td>
                    <td ng-bind="item.noticeType"></td>
                    <td ng-bind="item.publishUserName"></td>
                    <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                    <td ng-bind="item.gmtModified | date:'yyyy-MM-dd'"></td>
                    <td ng-bind="item.pageLoad"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id,item.stampType);" title="详情"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <my-pager  data-page-info="vm.pageInfo" on-load="vm.loadPagedData()" ></my-pager>

    </div>
</div>

