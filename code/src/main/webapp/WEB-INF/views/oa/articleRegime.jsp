
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
            <span ng-bind="vm.params.type"></span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>   <span ng-bind="vm.params.type"></span>
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
                <label style="margin-left: 20px;margin-right: 20px;">类别：
                    <select class="form-control input-inline input-sm" ng-model="vm.params.noticeLevel">
                        <option value="">全部</option>
                        <option value="基本制度">基本制度</option>
                        <option value="根本制度">根本制度</option>
                        <option value="专项制度">专项制度</option>
                        <option value="操作制度">操作制度</option>
                    </select>
                </label>
              <%--  <label style="margin-left: 20px;margin-right: 20px;">制度分类：
                    <select class="form-control input-inline input-sm" ng-model="vm.params.noticeLevel">
                        <option value="">全部</option>
                        <option value="基本制度">基本制度</option>
                        <option value="根本制度">根本制度</option>
                        <option value="专项制度">专项制度</option>
                        <option value="操作制度">操作制度</option>
                    </select>
                </label>--%>
                <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
            </div>

        </div>

        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>
                        <span  ng-if="vm.params.type.indexOf('制度')==-1" >公告标题</span>
                        <span ng-if="vm.params.type.indexOf('制度')>=0" >制度标题</span>
                    </th>
                    <th>发布部门</th>
                    <th style="width: 90px">
                        <span  ng-if="vm.params.type.indexOf('公司制度')==-1" >公告类别</span>
                        <span ng-if="vm.params.type.indexOf('公司制度')>=0" >制度层级</span>
                    </th>
                    <th ng-if="vm.params.type=='公司制度'">制度分类</th>
                    <th style="width: 120px">发布人</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 100px;">更新时间</th>
                    <th style="width: 90px;">点击次数</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in  vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.noticeTitle"  class="data_title" ng-click="vm.show(item.id);"></td>
                    <td ng-bind="item.deptName"></td>
                    <td ng-bind="item.noticeLevel"></td>
                    <td ng-bind="item.noticeSystemType"></td>
                    <td ng-bind="item.publishUserName"></td>
                    <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                    <td ng-bind="item.gmtModified | date:'yyyy-MM-dd'"></td>
                    <td ng-bind="item.pageLoad"></td>
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

