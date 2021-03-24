
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
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>企业动态</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> <span >企业动态</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
        </div>
    </div>


    <div class="portlet-body">
        <div class="row">
            <div >
                <label style="margin-left: 20px;">标题：<input type="search"
                                                              class="form-control input-inline input-sm"
                                                              placeholder="标题"
                                                              ng-model="vm.params.noticeTitle"></label>
                <label style="margin-left: 20px;">发布部门：<input type="search"
                                                            class="form-control input-inline input-sm"
                                                            placeholder="部门"
                                                            ng-model="vm.params.publishDeptName"></label>
                <label style="margin-left: 20px;">发布人：<input type="search"
                                                              class="form-control input-inline input-sm"
                                                              placeholder="发布人"
                                                              ng-model="vm.params.publishUserName"></label>

                <label style="margin-left: 20px;margin-right: 20px;">公告类别：
                    <select class="form-control input-inline input-sm" ng-model="vm.params.type">
                        <option value="">全部</option>
                        <option value="公司新闻">公司新闻</option>
                        <option value="文件简报">文件简报</option>
                        <option value="通知公告">通知公告</option>
                    </select>
                </label>

                <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
            </div>

        </div>
        <div class="table-scrollable " ng-controller="RedHeaderController">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>公告标题</th>
                    <th>发布部门</th>
                    <th style="width: 90px">公告类别</th>
                    <th style="width: 100px">发布人</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 130px;">任务状态</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in  vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.noticeTitle"  class="data_title" ng-click="vm.showNoticeDetail(item.id,item.attachId,item.businessKey);"></td>
                    <td ng-bind="item.publishDeptName"></td>
                    <td ng-bind="item.noticeType"></td>
                    <td ng-bind="item.publishUserName"></td>
                    <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                    <td ng-bind="item.processName"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>

