<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" style="margin: -10px 0px 5px 0px;padding:0px;background-color: #F1F3FA;">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="hr.employee">人资管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="hr.employee">个人信息子集</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>执业资格</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-database"></i> 个人执业资格
                </div>
                <div class="actions">

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click=" vm.loadData(user.userLogin);">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                        <i class="fa fa-plus"></i> 创建 </a>
                </div>            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">序号</th>
                                <th>用户姓名</th>
                                <th>证书名称</th>
                                <th>证书编号</th>
                                <th>专业类别</th>
                                <th>注册证书号</th>
                                <th>印章号</th>
                                <th>职业资格证书号</th>
                                <th style="width: 120px;">任务状态</th>
                                <th style="width: 100px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.certificationList">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.userName"  class="data_title" ng-click="vm.show(item.id);"></td>
                                <td ng-bind="item.certificationName"></td>
                                <td ng-bind="item.certificationNo"></td>
                                <td ng-bind="item.majorName"></td>
                                <td ng-bind="item.registerNo"></td>
                                <td ng-bind="item.sealNo"></td>
                                <td ng-bind="item.qualifyNo"></td>
                                <td ng-bind="item.processName"></td>

                                <td>
                                    <i class="fa fa-pencil margin-right-10" ng-click="vm.show(item.id);" title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-10" ng-click="vm.remove(item.id);" title="删除"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>


                </div>
            </div>
        </div>
    </div>


</div>


