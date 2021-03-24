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
            <span>专利发明</span>
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
                    <i class="fa fa-database"></i> 个人专业发明
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click=" vm.loadData(user.userLogin);">
                        <i class="fa fa-refresh"></i> 刷新 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>发明名称</th>
                                <th>第一发明人</th>
                                <th style="width: 90px">申报单位</th>
                                <th >所属专业</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 150px">流程状态</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.invnetList">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.inventName"  class="data_title"  ng-click="vm.show(item.id);"><strong ></strong></td>
                                <td ng-bind="item.firstInventManName" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.majorName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <span ng-bind="item.processName"></span>
                                </td>
                                <td>
                                    <i class="fa fa-pencil margin-right-10" ng-click="vm.show(item.id);" title="详情"></i>
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


