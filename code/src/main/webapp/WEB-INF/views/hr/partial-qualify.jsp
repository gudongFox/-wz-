<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>人资管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>设计资格管理</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
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
                </div>
            </div>
            <div class="portlet-body">
                <div id="dept_tree" ></div>
            </div>
        </div>
    </div>

    <div class="col-md-9">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-database"></i> 设计资格管理
                </div>
                <div class="actions">

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click=" vm.loadData(user.userLogin);">
                        <i class="fa fa-refresh"></i> 刷新 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;">用户名称：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="用户名称"
                                                                           ng-model="vm.params.userName"></label>

                            <label style="margin-right: 10px;">部门名称：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="部门名称"
                                                                           ng-model="vm.params.deptName"></label>

                            <a class="btn green btn-sm" ng-click="vm.queryData();"><i
                                    class="fa fa-search"></i> 查询 </a>

                            <a class="btn blue btn-sm" ng-click="vm.initMccData();"><i
                                    class="fa fa-search"></i> 初始化数据 </a>
                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">#</th>
                                <th style="width: 100px">姓名</th>
                                <th>部门名称</th>
                                <th style="width: 100px">专业</th>
                                <th style="width: 80px">设计人</th>
                                <th style="width: 80px">校核人</th>
                                <th style="width: 80px">审核人</th>
                                <th style="width: 80px">审定人</th>
                                <th style="width: 100px">专业负责人</th>
                                <th style="width: 100px">项目负责人</th>
                                <th style="width: 100px">执行项目负责人</th>
                                <th style="width: 55px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.userName" class="data_title" ng-click="vm.show(item.id);"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-click="vm.qualify=item;vm.showSelectMajor();">
                                    <span ng-bind="item.majorName" ></span>
                                    <i class="fa fa-cog"></i>
                                </td>
                                <td ng-click="vm.toggleEnable(item.id,'设计人')"><span ng-bind="item.design?'√':'×'" style="cursor: pointer;color:{{item.design?'green':'red'}}"></span></td>
                                <td ng-click="vm.toggleEnable(item.id,'校核人')"><span ng-bind="item.proofread?'√':'×'" style="cursor: pointer;color:{{item.proofread?'green':'red'}}"></span></td>
                                <td ng-click="vm.toggleEnable(item.id,'审核人')"><span ng-bind="item.audit?'√':'×'" style="cursor: pointer;color:{{item.audit?'green':'red'}}"></span></td>
                                <td ng-click="vm.toggleEnable(item.id,'审定人')"><span ng-bind="item.approve?'√':'×'" style="cursor: pointer;color:{{item.approve?'green':'red'}}"></span></td>
                                <td ng-click="vm.toggleEnable(item.id,'专业负责人')"><span ng-bind="item.majorCharge?'√':'×'" style="cursor: pointer;color:{{item.majorCharge?'green':'red'}}"></span></td>
                                <td ng-click="vm.toggleEnable(item.id,'项目负责人')"><span ng-bind="item.projectCharge?'√':'×'" style="cursor: pointer;color:{{item.projectCharge?'green':'red'}}"></span></td>
                                <td ng-click="vm.toggleEnable(item.id,'执行项目负责人')"><span ng-bind="item.projectExeCharge?'√':'×'" style="cursor: pointer;color:{{item.projectExeCharge?'green':'red'}}"></span></td>
                                <td>
                                    <i class="fa fa-edit margin-right-5" title="复制" ng-click="vm.show(item.id)"></i>
<%--                                    <i class="fa fa-copy margin-right-5" title="复制" ng-click="vm.copy(item)"></i>--%>
                                    <i class="fa fa-trash" title="删除" ng-click="vm.remove(item.id);"></i>
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


<div class="modal fade" id="selectMajorModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10" ng-bind="vm.qualify.deptName+'-'+vm.qualify.userName"></h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 300px;overflow-y: auto;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>专业名称</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="major in vm.majorNames">
                            <td>
                                <input type="checkbox" ng-checked="vm.qualify.majorName.indexOf(major)>=0" class="cb_major" data-name="{{major}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="major"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectMajor();">确认</button>
            </div>
        </div>
    </div>
</div>

