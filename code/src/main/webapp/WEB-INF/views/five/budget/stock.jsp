<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:62})">财务管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>股权投资预算</span>
            <i class="fa fa-angle-right"></i>
        </li>
 <%--       <li>
            <span>差旅费报销单</span>
        </li>--%>
    </ul>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span >股权投资预算</span>
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
                            <label style="margin-right: 10px;">标题：<input type="search"
                                                                         class="form-control input-inline input-sm"
                                                                         placeholder="标题"
                                                                         ng-model="vm.params.qName"></label>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
                            <span style="vertical-align:bottom; margin-left:5px;font-size: 12px;color: red">金额：(万元)</span>
                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="width: 160px;">填报部门</th>
                                <th style="width: 160px;">预算年份</th>
                                <th style="width: 180px;">预算总金额</th>
                                <th>备注</th>
                                <th style="width: 60px;">创建人</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 150px">流程状态</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.deptName"  class="data_title"  ng-click="vm.show(item.id);"></td>
                                <td ng-bind="item.budgetYear"></td>
                                <td ng-bind="item.totalBudgetMoney"></td>
                                <td ng-bind="item.remark" ></td>
                                <td ng-bind="item.creatorName" ></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td><span ng-bind="item.processName"></span></td>
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
