<%--
  Created by IntelliJ IDEA.
  User: luodong
  Date: 2019/7/17
  Time: 14:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
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
            <span ng-bind="tableName">人员信息查询</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-3" style="padding-right: 0px;">
        <div class="portlet box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-tree"></i> 组织机构
                </div>
                <div class="tools">
                    <i class="fa fa-refresh margin-right-5" title="刷新" ng-click="vm.buildTree();"></i>
                    <i class="fa  fa-arrow-left" title="返回" ng-click="back();"></i>
                </div>
            </div>
            <div class="portlet-body">
                <div id="dept_tree" ></div>
            </div>
        </div>
    </div>

    <div class="col-md-9">
        <div class="portlet  box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-tree"></i> 组织机构详情
                </div>
                <ul class="nav nav-tabs">

                    <li class="active" id="deptTab">
                        <a href="#dept_tab" data-toggle="tab" aria-expanded="false">
                            员工信息</a>
                    </li>

                    <li class="" id="infoTab">
                        <a href="#info_tab" data-toggle="tab" aria-expanded="false">
                            基础信息</a>
                    </li>

                </ul>
            </div>
            <div class="portlet-body">

                <div class="tab-content">

                    <div class="tab-pane active" id="dept_tab">
                        <div class="row">
                            <div class="col-md-12">
                                <label style="margin-right: 10px;">关键字：<input type="search"
                                                                               class="form-control input-inline input-sm"
                                                                               placeholder="姓名，职工号，简拼" id="applyCertNum"
                                                                               ng-model="vm.params.userName"></label>

                                <label style="margin-right: 10px;">部门名称：<input type="search"
                                                                               class="form-control input-inline input-sm"
                                                                               placeholder="部门名称"
                                                                               ng-model="vm.params.deptName"></label>

                                <a class="btn green btn-sm defaultBtn" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
                            </div>
                        </div>
                        <div class="table-scrollable">
                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 35px;">#</th>
                                    <th style="width:110px;">姓名</th>
                                    <th style="width:100px;">职工号</th>
                                    <th>工作电话</th>
                                    <th>部门名称</th>
                                    <th>系统角色</th>
                                    <th>职务</th>
                                    <th style="width:100px;">员工类型</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="employee in vm.pageInfo.list |filter: {userStatus:vm.userFilfter}">
                                    <td ng-bind="$index+vm.pageInfo.startRow"></td>
                                    <td>
                                        <img alt="" class="img-circle" ng-src="{{employee.avatar}}"  onerror="this.src='/m/admin/layout/img/avatar9.jpg'" style="height: 30px;width: 30px;border-radius: 10%;"/>
                                        <strong><span ng-bind="employee.userName" ></span></strong>
                                    </td>
                                    <td ng-bind="employee.userNo"></td>
                                    <td ng-bind="employee.officeTel"></td>
                                    <td ng-bind="employee.deptName" ></td>
                                    <td ng-bind="employee.roleNames"  ></td>
                                    <td ng-bind="employee.positionNames" ></td>
                                    <td ng-bind="employee.userType"></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
                    </div>

                    <div class="tab-pane " id="info_tab">
                        <form class="form-horizontal form" role="form" id="detail_form">
                            <div class="form-body">
                                <div class="form-group">

                                    <label class="col-md-2 control-label">部门名称</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control" ng-model="vm.item.name" name="name" disabled
                                               required="true" maxlength="20"/>
                                    </div>
                                    <label class="col-md-2 control-label">上级部门</label>
                                    <div class="col-md-4">
                                        <select ng-options="s.id as s.name for s in vm.parents" disabled
                                                ng-model="vm.item.parentId"
                                                class="form-control"></select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">部门编号</label>
                                    <div class="col-md-4">
                                        <input class="form-control" disabled ng-model="vm.item.deptCode"
                                               name="deptCode" maxlength="10"/>
                                    </div>
                                    <label class="col-md-2 control-label">部门类型</label>
                                    <div class="col-md-4">
                                        <select disabled ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'部门类型'}:true"
                                                ng-model="vm.item.deptType"
                                                class="form-control"></select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-2 control-label">排序</label>
                                    <div class="col-md-4">
                                        <input type="number" class="form-control" ng-model="vm.item.seq" disabled
                                               name="seq" placeholder="请输入整数">
                                    </div>

                                    <label class="col-md-2 control-label">状态</label>
                                    <div class="col-md-4">
                                        <select disabled ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'状态'}:true"
                                                ng-model="vm.item.deleted" class="form-control"></select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-2 control-label">备注</label>
                                    <div class="col-md-10">
                                        <textarea disabled type="text" class="form-control" ng-model="vm.item.remark"/>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
