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
            <span ng-bind="tableName">工程设计任务启动（超前任务单）</span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-star"></i> <span ng-bind="tableName">工程设计任务启动（超前任务单）</span>
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
                                                               placeholder="项目名称|项目号"
                                                               ng-model="vm.params.qName"></label>
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
                        <th style="width: 110px;">项目号</th>
                        <th style="width: 160px;">承接部门</th>
                        <th style="width: 110px;">投资额</th>
                        <th>委托方</th>
                        <th style="width: 80px;">总设计师</th>
                        <th style="width: 80px;">设计任务类型</th>
                        <th style="width: 180px;">任务状态</th>
                        <th style="width: 75px;">是否补录合同</th>
                        <th style="width: 55px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.pageInfo.list">
                        <td ng-bind="$index+1"></td>
                        <td ng-bind="item.projectName"  class="data_title"  ng-click="vm.show(item.id);"></td>
                        <td ng-bind="item.projectNo"></td>
                        <td ng-bind="item.deptName"></td>
                        <td ng-bind="item.investMoney|currency : '￥'"></td>
                        <td ng-bind="item.customerName"></td>
                        <td ng-bind="item.totalDesignerName"></td>
                        <td ng-bind="item.designType"></td>
                        <td ng-bind="item.processName"></td>
                        <td class="text-center">
                            <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.reviewContractId!=0" ng-click="vm.showReviewContract(item.reviewContractId);">已补录合同</span>
                            <span class="label label-sm label-default" ng-if="item.reviewContractId == 0" ng-click="vm.addReviewContract(item);">点击补录合同</span>
                        </td>
                        <td>
                            <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                            <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="item.reviewContractId==0&&item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0"></i>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>


