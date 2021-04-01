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
            <span ng-bind="tableName">合同评审</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span ng-bind="tableName">合同评审</span>
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
                           <%-- <label style="margin-right: 10px;">关键字：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="项目名称|项目号|合同号"
                                                                           ng-model="vm.params.qName"></label>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
                            <span style="vertical-align:bottom; margin-left:5px;font-size: 12px;color: red">金额：(万元)</span>--%>

                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table filter  class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="min-width: 200px;">备案项目名称</th>
                                <th style="min-width: 120px;">合同名称</th>
                                <th style="width: 120px" class="hidden-md hidden-sm">合同号</th>
                                <th style="width: 120px" class="hidden-md hidden-sm">项目号</th>
                                <th style="width: 100px" class="hidden-md hidden-sm">合同额(万元)</th>
                                <th class="hidden-md hidden-sm">客户名称</th>
                                <th style="width: 60px">评审级别</th>
<%--                                <th style="width: 120px">评审时间</th>--%>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 30px;">补充标识</th>
                                <th style="width: 30px;">补录标识</th>
                                <th style="width: 30px;" ng-if="rightData.selectOpts.indexOf('修改')>=0">是否开启</th>
                              <%--  <th style="width: 30px;">印花税状态</th>--%>
                                <th style="width: 150px">流程状态</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list" on-finish-render="">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.projectName"  class="data_title"  ng-click="vm.show(item.id);"><strong ></strong>
                                </td>
                                <td ng-bind="item.contractName"></td>
                                <td ng-bind="item.contractNo" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.projectNo" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.contractMoney" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.customerName" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.reviewLevel"></td>
<%--                                <td ng-bind="item.reviewTime"></td>--%>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td class="text-center">
                                    <span class="label label-sm label-success"  ng-if="item.main">是</span>
                                    <span class="label label-sm label-default" ng-if="!item.main">否</span>
                                </td>
                                <td class="text-center">
                                    <span class="label label-sm label-success"  ng-if="item.preContractId!=0" ng-click="vm.showPreContract(item.preContractId);">是</span>
                                    <span class="label label-sm label-default" ng-if="item.preContractId==0">否</span>
                                </td>
                                <td class="text-center" ng-if="rightData.selectOpts.indexOf('修改')>=0" ng-click="vm.changeOpen(item.id)">
                                    <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.open">开启</span>
                                    <span class="label label-sm label-default" ng-if="!item.open">关闭</span>
                                </td>
                              <%--  <td class="text-center" ng-if="rightData.selectOpts.indexOf('修改')>=0" ng-click="vm.changeOpenStamp(item.id)">
                                    <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.openStamp">已开票</span>
                                    <span class="label label-sm label-default" ng-if="!item.openStamp">未开票</span>
                                </td>--%>
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
