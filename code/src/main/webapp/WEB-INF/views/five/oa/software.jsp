<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:86})">资产管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName"ng-bind="tableName">软件购置申请</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span ng-bind="tableName">软件购置申请</span>
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                        <i class="fa fa-plus"></i> 创建 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                        <i class="fa fa-arrow-left"></i> 返回 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;">开始时间：
                                <input type="search" class="form-control input-inline input-sm date-picker" id="startTime1"
                                       ng-model="vm.params.startTime1" value="{{vm.params.startTime1|date:'yyyy-MM-dd'}}"></label>
                            <label style="margin-right: 10px;">结束时间：
                                <input type="search"  class="form-control input-inline input-sm date-picker" id="endTime1"
                                       ng-model="vm.params.endTime1" value="{{vm.params.endTime1|date:'yyyy-MM-dd'}}"></label>

                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.downExcel();"><i class="fa fa-download"></i> 导出EXCEL </a>
                          <%--  <label style="margin-right: 10px;">软件名称：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="软件名称"
                                                                           ng-model="vm.params.qName"></label>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>--%>

                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table filter class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="min-width: 200px">软件名称</th>
                                <th style="width: 180px">申请单位</th>
                                <th style="width: 100px">申请类型</th>
                                <th style="width: 100px">软件报价(万元)</th>
                                <th>软件使用专业</th>
                                <th>软件公司名称</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 150px">流程状态</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list" on-finish-render>
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.softwareName"  class="data_title"  ng-click="vm.show(item.id);"><strong ></strong>
                                </td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.applyStyle"></td>
                                <td ng-bind="item.softwareOffer"></td>
                                <td ng-bind="item.softwareUseMajor"></td>
                                <td ng-bind="item.softwareCompanyName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <span ng-bind="item.processName"></span>
                                </td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-5" ng-click="vm.remove(item.id);" title="删除"
                                       ng-show="item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
                </div>
            </div>
        </div>
    </div>

</div>
