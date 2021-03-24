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
            <span>学历信息</span>
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
                    <i class="fa fa-database"></i> 个人学历信息
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
                                <th>学校名称</th>
                                <th>开始时间</th>
                                <th>结束时间</th>
                                <th>教育类型</th>
                                <th>学历</th>
                                <th>学制</th>
                                <th>学位</th>
                                <th>第一专业</th>
                                <th>其他专业</th>
                                <th>任务状态</th>

                                <th style="width: 90px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.educationList">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.userName"  class="data_title" ng-click="vm.show(item.id);" ></td>
                                <td ng-bind="item.schoolName"></td>
                                <td ng-bind="item.beginTime|date:'yyyy-MM-dd HH:mm'">
                                <td ng-bind="item.endTime|date:'yyyy-MM-dd HH:mm'">
                                <td ng-bind="item.educationType"></td>
                                <td ng-bind="item.educationName"></td>
                                <td ng-bind="item.educationYear"></td>
                                <td ng-bind="item.educationDegree"></td>
                                <td ng-bind="item.primarySpecialty"></td>
                                <td ng-bind="item.otherSpecialty"></td>
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


