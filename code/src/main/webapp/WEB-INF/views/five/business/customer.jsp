<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>经营管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">客户信息录入</span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-user-following"></i> <span ng-bind="tableName">客户信息录入</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.init();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                <i class="fa fa-plus" ></i> 创建 </a>
        </div>
    </div>
    <div class="portlet-body">
<%--        <div ui-calendar="uiConfig.calendar" class="span8 calendar" ng-model="eventSources"></div>--%>
        <div class="row">
            <div class="col-md-12">
<%--                <label style="margin-right: 10px;">客户编号：<input type="search"
                                                               class="form-control input-inline input-sm"
                                                               placeholder="客户编号"
                                                               ng-model="vm.params.qCode"></label>--%>

               <%-- <label style="margin-right: 10px;">关键字 ：<input type="search"
                                                               class="form-control input-inline input-sm"
                                                               placeholder="客户名称|客户编号"
                                                               ng-model="vm.params.qName"></label>
                <a class="btn green btn-sm defaultBtn" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>--%>
            </div>
        </div>
        <div class="dataTables_wrapper no-footer">
            <div class="table-scrollable">
                <table filter class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 35px;">#</th>
                        <th>客户编号</th>
                        <th style="min-width: 200px;">客户名称</th>
                        <th style="min-width: 200px;" class="hidden-md hidden-sm">客户地址</th>
                        <th style="width: 160px;">录入部门</th>
                        <th style="width: 60px;">合作项目数量</th>
                      <%--  <th style="width: 110px;">单位类型</th>--%>
                        <th style="width: 60px;">联系人</th>
                        <th style="width: 130px;">联系电话</th>
                        <th style="width: 100px;">创建时间</th>
                        <th style="width: 160px">流程状态</th>
                        <th style="width: 60px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.pageInfo.list" on-finish-render="">
                        <td class="dt-right" ng-bind="$index+1"></td>
                        <td ng-bind="item.code"  class="data_title" ng-click="vm.show(item.id);"></td>
                        <td ng-bind="item.name"></td>
                        <td ng-bind="item.address" class="hidden-md hidden-sm"></td>
                        <td ng-bind="item.deptName"></td>
                        <td ng-bind="item.contractCount"></td>
                        <%--<td ng-bind="item.customerType"></td>--%>
                        <td ng-bind="item.linkMan"></td>
                        <td ng-bind="item.linkTel"></td>
                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                        <td>
                            <span ng-bind="item.processName"></span>
                        </td>
                        <td>
                            <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                            <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="rightData.selectOpts.indexOf('删除')>=0&&item.creator==user.userLogin"></i>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>


