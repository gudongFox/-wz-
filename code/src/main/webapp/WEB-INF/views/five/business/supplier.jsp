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
            <span ng-bind="tableName">供方管理</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span ng-bind="tableName">供方管理</span>
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
                            <label style="margin-right: 10px;">关键字：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="供方编号|供方名称"
                                                                           ng-model="vm.params.qName"></label>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>

                        </div>
                    </div>
                    <div class="dataTables_wrapper no-footer">
                        <div class="table-scrollable">
                            <table filter class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 35px;">#</th>
                                    <th>供方编号</th>
                                    <th style="min-width: 200px;">供方名称</th>
                                    <th>入库类别</th>
                                    <th>录入部门</th>
                                    <th style="width: 120px;">单位类型</th>
                                    <th style="width: 80px;">项目数量</th>
                                    <th style="width: 80px;">联系人</th>
                                    <th style="width: 110px;">联系电话</th>
                                    <th style="width: 100px;">创建时间</th>
                                    <th style="width: 150px">流程状态</th>
                                    <th style="width: 60px;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="item in vm.pageInfo.list" on-finish-render="">
                                    <td class="dt-right" ng-bind="$index+1"></td>
                                    <td ng-bind="item.code"  class="data_title" ng-click="vm.show(item.id);"></td>
                                    <td ng-bind="item.name"></td>
                                    <td ng-bind="item.inType"></td>
                                    <td ng-bind="item.deptName"></td>
                                    <td ng-bind="item.supplierType"></td>
                                    <td ng-bind="item.cooperProjectNum"></td>
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
        </div>
    </div>

</div>
