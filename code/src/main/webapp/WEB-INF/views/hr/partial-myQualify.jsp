<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="hr.basic">基础信息</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>设计资格</span>
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
                    <i class="fa fa-database"></i> 设计资格
                </div>
                <div class="actions">

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click=" vm.loadData(user.userLogin);">
                        <i class="fa fa-refresh"></i> 刷新 </a>
<%--                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                        <i class="fa fa-plus"></i> 创建 </a>--%>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.params.qName"/>
                        </div>
                        <div class="col-md-4">
                            <a class="btn green btn-sm" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">#</th>
                                <th style="width: 10%">用户姓名</th>
                                <th>从事专业</th>
                                <th>检查年度</th>
                                <th>项目类型</th>
                                <th>项目负责人资格</th>
                                <th>专业负责人资格</th>
                                <th>校对人资格</th>
                                <th>审核人资格</th>
                                <th>审定人资格</th>
                                <th>总设计师资格</th>
                                <th>是否为兼职总设计师</th>
                                <th>创建时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.list">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.userName" ></td>
                                <td ng-bind="item.majorName">
                                <td ng-bind="item.checkYear">
                                <td ng-bind="item.projectType">
                                <td ng-bind="item.isProjectCharge">
                                <td ng-bind="item.isMajorCharge">
                                <td ng-bind="item.isProofread"></td>
                                <td ng-bind="item.isAudit"></td>
                                <td ng-bind="item.isApprove"></td>
                                <td ng-bind="item.isChiefDesigner"></td>
                                <td ng-bind="item.isProChief"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd '"></td>
                            </tr>
                            </tbody>

                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>


</div>

