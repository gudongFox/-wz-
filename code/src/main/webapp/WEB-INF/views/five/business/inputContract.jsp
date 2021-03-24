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
            <span>合同补录</span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-star"></i> 合同补录
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                        <i class="fa fa-plus"></i> 创建 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                        <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-12">
                <label style="margin-right: 10px;">关键字：<input type="search"
                                                               class="form-control input-inline input-sm"
                                                               placeholder="名称，委托方，合同额"
                                                               ng-model="vm.params.qName"></label>
                <label style="margin-right: 10px;">部门：<input type="search"
                                                              class="form-control input-inline input-sm"
                                                              placeholder="部门"
                                                              ng-model="vm.params.qDeptName"></label>
                <a class="btn green btn-sm defaultBtn" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
                <span style="vertical-align:bottom; margin-left:5px;font-size: 12px;color: red">金额：(万元)</span>
            </div>
        </div>
        <div class="dataTables_wrapper no-footer">
            <div class="table-scrollable">
                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 35px;">#</th>
                        <th style="min-width: 200px;">项目名称</th>
                        <th>承接部门</th>
                        <th style="width: 110px;">合同额</th>
                        <th>委托方</th>
                        <th>承办人</th>
                        <th style="width: 80px;">合同进度</th>
                        <th style="width: 100px;">签约日期</th>
                        <th style="width: 160px;">任务状态</th>
 <%--                       <th style="width: 75px;">设计管理</th>--%>
                        <th style="width: 55px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.pageInfo.list">
                        <td ng-bind="$index+1"></td>
                        <td ng-bind="item.projectName"  class="data_title"  ng-click="vm.show(item.id);"></td>
                        <td ng-bind="item.deptName"></td>
                        <td ng-bind="item.contractMoney|currency : '￥'"></td>
                        <td ng-bind="item.customerName"></td>
                        <td ng-bind="item.chargeMenName"></td>
                        <td ng-bind="item.contractSchedule"></td>
                        <td ng-bind="item.signTime"></td>
                        <td ng-bind="item.processName"></td>
<%--                        <td class="text-center">
                            <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.ed" ng-click="vm.showEd(item.contractId);">启用</span>
                            <span class="label label-sm label-default" ng-if="!item.ed">关闭</span>
                        </td>--%>
                        <td>
                            <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                            <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0"></i>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>


