<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="business.contract">经营管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>经营(经济)合同评审表</span>
        </li>
    </ul>
</div>
<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> 经营(经济)合同评审表
                </div>
                <div class="actions">

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                        <i class="fa fa-plus"></i> 创建 </a>
                </div>            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.params.qName"/>
                        </div>
                        <div class="col-md-4">
                            <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
                        </div>
                    </div>

                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>项目名称</th>
                                <th>客户名称</th>
                                <th>部门名称</th>
                                <th>工程价格</th>
                                <th style="width: 60px">业务内容</th>
                                <th style="width: 60px">项目类型</th>
                                <th style="width: 50px">经办人</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 130px">流程状态</th>
                                <th style="width: 50px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.projectName"  class="data_title"  ng-click="vm.show(item.id);"></td>
                                <td ng-bind="item.customerName"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.contractMoney|currency : '￥'"></td>
                                <td ng-bind="item.contractType"></td>
                                <td ng-bind="item.projectType"></td>
                                <td ng-bind="item.creatorName" ></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <span ng-bind="item.processName"></span>
                                </td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                    <i class="fa fa-trash-o " ng-click="vm.remove(item.id);" title="删除" ng-show="item.creator==user.userLogin&&!item.processEnd"></i>
                                    <i class="fa fa-plus " ng-click=" vm.addContract(item.id);" ng-show="item.creator==user.userLogin&&item.processEnd" title="创建合同台账"></i>
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


