<%--
  Created by IntelliJ IDEA.
  User: Roy
  Date: 2019/2/22
  Time: 15:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="sys.code">系统管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>访问日志-安全保密管理员</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-paw"></i> 数据列表
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.params.qName"/>
                        </div>
                        <div class="col-md-4">
                            <a class="btn green btn-sm" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
                        </div>
                    </div>
                    <div class="tabbable-line">
                        <ul class="nav nav-tabs ">
                            <li class="active">
                                <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                                    用户审计日志 </a>
                            </li>
                            <li class="">
                                <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                                    安全审计员日志
                                </a>
                            </li>

                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="tab_15_1">
                                <div class="table-scrollable">
                                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                        <thead>
                                        <tr>
                                            <th style="width: 60px;">#</th>
                                            <th>地址</th>
                                            <th style="width: 60px;">方式</th>
                                            <th style="width: 150px;">时间</th>
                                            <th style="width: 60px;">耗时</th>
                                            <th style="width: 380px;">参数</th>
                                            <th style="width: 120px;">来源</th>
                                            <th style="width: 60px;">访问人</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr ng-repeat="item in vm.pageInfoUser.list">
                                            <td ng-bind="$index+vm.pageInfoUser.startRow"></td>
                                            <td ng-bind="item.requestUrl"></td>
                                            <td ng-bind="item.requestMethod"></td>
                                            <td ng-bind="item.finishTime|date:'yyyy-MM-dd HH:mm'"></td>
                                            <td ng-bind="item.requestSecond+'s'"></td>
                                            <td ng-bind="item.requestParameter"></td>
                                            <td ng-bind="item.requestIp"></td>
                                            <td ng-bind="item.requestLogin"></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>

                                <my-pager data-page-info="vm.pageInfoUser" on-load="vm.listUserData()"></my-pager>
                            </div>
                            <div class="tab-pane" id="tab_15_2">
                                <div class="table-scrollable">
                                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                        <thead>
                                        <tr>
                                            <th style="width: 60px;">#</th>
                                            <th>地址</th>
                                            <th style="width: 60px;">方式</th>
                                            <th style="width: 150px;">时间</th>
                                            <th style="width: 60px;">耗时</th>
                                            <th style="width: 380px;">参数</th>
                                            <th style="width: 120px;">来源</th>
                                            <th style="width: 60px;">访问人</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr ng-repeat="item in vm.pageInfoAudit.list">
                                            <td ng-bind="$index+vm.pageInfoAudit.startRow"></td>
                                            <td ng-bind="item.requestUrl"></td>
                                            <td ng-bind="item.requestMethod"></td>
                                            <td ng-bind="item.finishTime|date:'yyyy-MM-dd HH:mm'"></td>
                                            <td ng-bind="item.requestSecond+'s'"></td>
                                            <td ng-bind="item.requestParameter"></td>
                                            <td ng-bind="item.requestIp"></td>
                                            <td ng-bind="item.requestLogin"></td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>

                                <my-pager data-page-info="vm.pageInfoAudit" on-load="vm.listAuditData()"></my-pager>
                            </div>

                        </div>

                    </div>


                </div>
            </div>
        </div>
    </div>
</div>

