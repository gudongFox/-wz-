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
            <span>组织机构管理-安全保密管理员</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-3" style="padding-right: 0px;">
        <div class="portlet box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-tree"></i> 组织机构-安全保密管理员
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
                                <label style="margin-right: 10px;">用户名称：<input type="search"
                                                                               class="form-control input-inline input-sm"
                                                                               placeholder="用户名称"
                                                                               ng-model="vm.params.userName"></label>

                                <label style="margin-right: 10px;">部门名称：<input type="search"
                                                                               class="form-control input-inline input-sm"
                                                                               placeholder="部门名称"
                                                                               ng-model="vm.params.deptName"></label>

                                <a class="btn green btn-sm defaultBtn"  ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
                            </div>
                        </div>
                        <div class="table-scrollable">
                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 35px;">#</th>
                                    <th style="width:130px;">姓名</th>
                                    <th style="width:80px;">登录名</th>
                                    <th>部门名称</th>
                                    <th>系统角色</th>
                                    <th>职务</th>
                                    <th style="width:100px;">员工类型</th>
                                    <th style="width: 100px;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="employee in vm.pageInfo.list">
                                    <td ng-bind="$index+vm.pageInfo.startRow"></td>
                                    <td ng-click="vm.show(employee.userLogin)">
                                        <img alt="" class="img-circle" ng-src="{{'/sys/attach/download/'+employee.headAttachId}}"  onerror="this.src='/m/admin/layout/img/avatar9.jpg'" style="height: 30px;width: 30px;border-radius: 10%;"/>
                                        <span ng-bind="employee.userName" class="data_title" ></span>
                                    </td>
                                    <td ng-bind="employee.userNo"></td>
                                    <td ng-bind="employee.deptName" ng-click="vm.showDeptModal(employee);" class="data_title2"></td>
                                    <td ng-bind="employee.roleNames" ng-click="vm.showRoleModal(employee);" class="data_title2"></td>
                                    <td ng-bind="employee.positionNames" ng-click="vm.showPositionModal(employee);" class="data_title2"></td>
                                    <td ng-bind="employee.userType" ng-click="vm.showTypeModal(employee);"></td>

                                    <td>
                                        <i class="fa fa-cog margin-right-5" ng-click="vm.showUserDeptConfig(employee);"
                                           title="用户权限配置"></i>
                                        <i class="fa fa-lock margin-right-5" ng-click="vm.resetPwd(employee.userLogin);"
                                           title="重置密码"></i>
                                        <i class="fa fa-pencil margin-right-5" ng-click="vm.show(employee.userLogin);"
                                           title="详情"></i>
                                    </td>
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
                                        <input type="text" class="form-control" ng-model="vm.item.name" name="name"
                                               required="true" maxlength="20"/>
                                    </div>
                                    <label class="col-md-2 control-label">上级部门</label>
                                    <div class="col-md-4">
                                        <select ng-options="s.id as s.name for s in vm.parents"
                                                ng-model="vm.item.parentId"
                                                class="form-control"></select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">部门编号</label>
                                    <div class="col-md-4">
                                        <input class="form-control" ng-model="vm.item.deptCode"
                                               name="deptCode" maxlength="10"/>
                                    </div>
                                    <label class="col-md-2 control-label">部门类型</label>
                                    <div class="col-md-4">
                                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'部门类型'}:true"
                                                ng-model="vm.item.deptType"
                                                class="form-control"></select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-2 control-label">排序</label>
                                    <div class="col-md-4">
                                        <input type="number" class="form-control" ng-model="vm.item.seq" required="true"
                                               name="seq" placeholder="请输入整数">
                                    </div>

                                    <label class="col-md-2 control-label">状态</label>
                                    <div class="col-md-4">
                                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'状态'}:true"
                                                ng-model="vm.item.deleted" class="form-control"></select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">部门正职领导</label>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.deptFirstLeaderName" name="deptFirstLeaderName"  />
                                            <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('first');"><i class="fa fa-user"></i></button>
                                         </span>
                                        </div>
                                    </div>
                                    <label class="col-md-2 control-label">部门副职领导</label>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.deptSecondLeaderName" name="deptSecondLeaderName"  />
                                            <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('second');"><i class="fa fa-user"></i></button>
                                         </span>
                                        </div>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">部门分管公司领导</label>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptChargeManName" name="deptChargeManName"  />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel();"><i class="fa fa-user"></i></button>
                                         </span>
                                        </div>
                                    </div>
                                    <label class="col-md-2 control-label">部门财务人员</label>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.deptFinanceManName"
                                                   name="deptFinanceManName"/>
                                            <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showUserModel('finance');"><i
                                                    class="fa fa-user"></i></button>
                                         </span>
                                        </div>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">备注</label>
                                    <div class="col-md-10">
                                        <textarea type="text" class="form-control" ng-model="vm.item.remark"/>
                                    </div>
                                </div>
                            </div>
                            <div class="form-actions right" style="padding: 10px;">
                               <%-- <button type="button" class="btn blue btn-sm" ng-click="vm.update();" ng-disabled="vm.item.parentId==0">保存</button>
                                <button type="button" class="btn default btn-sm" ng-click="vm.loadDept();" ng-disabled="vm.item.parentId==0">重置</button>--%>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="roleSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10" ng-bind="vm.current.deptName+' '+vm.current.userName"></h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 500px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>名字</th>
                            <th>备注</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.roles">
                            <td>
                                <input type="checkbox" ng-checked="vm.current.roleIds.indexOf(','+item.id+',')>=0"
                                       class="cb_role"
                                       data-id="{{item.id}}"/>
                            </td>
                            <td ng-bind="item.name"></td>
                            <td ng-bind="item.remark"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveRole();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="positionSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10" ng-bind="vm.current.deptName+' '+vm.current.userName"></h4>
            </div>
            <div class="modal-body">

                <div class="table-scrollable" style="max-height: 450px;overflow-y: auto;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>职务名称</th>
                            <th>是否负责人</th>
                            <th>备注</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.positions">
                            <td>
                                <input type="checkbox" ng-checked="vm.current.positionIds.indexOf(','+item.id+',')>=0"
                                       class="cb_position"
                                       data-id="{{item.id}}"/>
                            </td>
                            <td ng-bind="item.positionName"></td>
                            <td ng-bind="item.deptCharge?'是':'否'"></td>
                            <td ng-bind="item.remark"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.savePosition();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="deptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择所处部门</h4>
            </div>
            <div class="modal-body">
                <div id="dept_select_tree" style="max-height:500px;overflow-y: auto;overflow-x: hidden;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDept();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="typeSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10" ng-bind="vm.current.deptName+' '+vm.current.userName"></h4>
            </div>
            <div class="modal-body">

                <div class="table-scrollable" style="max-height: 450px;overflow-y: auto;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>选项</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.type">
                            <td>
                                <input type="checkbox" ng-checked="vm.current.userType.indexOf(item.code)>=0"
                                       class="cb_type"
                                       data-name="{{item.code}}"/>
                            </td>
                            <td ng-bind="item.code"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveType();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="addEmployeeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">新增用户</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="employee_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">用户名称</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" required="true" ng-model="vm.employee.userName"
                                       name="userName" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">登录名</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" required="true" name="userLogin"
                                       ng-model="vm.employee.userLogin"
                                       placeholder="唯一标识">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">部门名称</label>
                            <div class="col-md-8">
                                <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.deptNameId" />
                                <span class="input-group-btn" >
                                    <button class="btn default" type="button" ng-click="vm.showDeptModalById(vm.employee.deptId);"><i class="fa fa-cog font-blue"></i></button>
                                </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">员工类型</label>
                            <div class="col-md-8">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'员工类型'}:true"
                                        ng-model="vm.employee.userType" class="form-control"></select>

                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.insertEmployee();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="deptSelectModalId" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择所处部门</h4>
            </div>
            <div class="modal-body">
                <div id="dept_select_tree1" style="max-height:500px;overflow-y: auto;overflow-x: hidden;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDeptById();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="userAclDeptConfigModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="vm.currentUser.userName+'-数据权限特殊配置'"></h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;">权限名称：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="权限名称"
                                                                           ng-model="vm.qMyAcl"></label>

                            <a class="btn blue btn-sm" ng-click="vm.showUserAclTree();"><i
                                    class="fa fa-cog"></i> 用户功能点配置 </a>
                        </div>
                    </div>

                    <div class="table-scrollable" style="max-height:{{contentHeight/2}}px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="width: 160px;">权限名称</th>
                                <th>类型</th>
                                <th>ui-sref</th>
                                <th style="width: 160px;">功能权限</th>
                                <th style="width: 60px;">数据权限</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.myAclInfoList|filter:{name:vm.qMyAcl}">
                                <td ng-bind="$index+1"></td>
                                <td>
                                    <span ng-bind="item.name" ></span>
                                    <i class="fa fa-trash margin-right-5" ng-click="vm.removeUserAclById(item);" ng-show="item.allConfig"  style="color: red;"></i>
                                </td>
                                <td>
                                    <span ng-if="item.type==1">菜单</span>
                                    <span ng-if="item.type==2">按钮</span>
                                    <span ng-if="item.type==3">其他</span>
                                </td>
                                <td ng-bind="item.uiSref"></td>
                                <td>
                                    <span ng-bind="item.selectOpts"  title="{{item.oSelectOpts}}"></span>
                                    <i class="fa fa-trash margin-right-5" ng-click="vm.removeUserAclOptById(item);" ng-show="item.optConfig"  style="color: red;"></i>
                                </td>
                                <td>
                                    <i class="fa fa-cog margin-right-5" ng-click="vm.showUserAclDeptSelectModal(item);"></i>
                                    <i class="fa fa-trash margin-right-5" ng-click="vm.removeUserAclDeptById(item);" ng-show="item.deptConfig" style="color: red;"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>

                    </div>
                    <p style="color: red;">1.用户功能权限默认等于角色权限功能合集。</p>
                    <p style="color: red;">2.用户数据权限默认等于职位和角色管辖部门合集（包含自己部门）。</p>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="userAclDeptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="vm.currentUser.userName+'-'+vm.aclInfo.name+'-数据权限'"></h4>
            </div>
            <div class="modal-body">
                <div id="user_acl_dept_select_tree" style="max-height:500px;overflow-y: auto;overflow-x: hidden;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveAclSelectDept();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="userAclSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="vm.currentUser.userName+'-功能点配置'"></h4>
            </div>
            <div class="modal-body">
                <div id="user_acl_select_tree" style="max-height: {{contentHeight/2}}px;overflow-y: auto;"></div>
            </div>
            <div class="modal-footer">
                <span style="color: red;cursor: pointer;float: left;" ng-show="vm.currentUser.aclConfig" ng-click="vm.removeUserAcl();">删除用户独立配置权限</span>
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveUserSelectAcl();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

