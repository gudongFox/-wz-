<%--
  Created by IntelliJ IDEA.
  User: Roy
  Date: 2019/2/22
  Time: 15:46
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
            <a ui-sref="sys.dept">用户管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>角色管理</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-3">
        <div class="portlet box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-tree"></i> 角色管理
                </div>
                <div class="tools">
                    <input type="text" id="autocomplete" placeholder="关键字" ng-model="vm.params.queryName" style=" color:black;height: 25px; border-color:white;" >
                    <i class="icon-magnifier margin-right-5" ng-click=" vm.buildTree()"></i>
                    <i class="fa fa-plus margin-right-5" title="增加" ng-click="vm.addRole();"></i>
                    <i class="fa fa-edit" title="编辑" ng-click="vm.editRole();"></i>
                </div>

            </div>
            <div class="portlet-body">
                <div id="role_tree"></div>
            </div>
        </div>

    </div>

    <div class="col-md-9" style="padding-left: 0px;">
        <div class="portlet box blue">
            <div class="portlet-title tabbable">
                <div class="caption">
                    <i class="fa fa-user"></i> <span ng-bind="vm.params.currentRoleName"></span>
                </div>
                <div class="actions" style="margin-left: 10px">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.clearCache1();" >
                        <i class="glyphicon glyphicon-refresh "></i> 缓存清理 </a>
                </div>
                <ul class="nav nav-tabs">
                    <li class="active">
                        <a href="#role_user_tab" data-toggle="tab" aria-expanded="false">
                            用户列表 </a>
                    </li>
                    <li class="">
                        <a href="#role_acl_tab" data-toggle="tab" aria-expanded="false">
                            权限列表 </a>
                    </li>

                </ul>
            </div>
            <div class="portlet-body">
                <div class="tab-content">
                    <div class="tab-pane active" id="role_user_tab">
                        <div class="dataTables_wrapper no-footer">
                            <div class="row">
                                <div class="col-md-12">

                                    <label style="margin-right: 10px;">用户名称：<input type="search"
                                                                                   class="form-control input-inline input-sm"
                                                                                   placeholder="用户名称"
                                                                                   ng-model="vm.params.qUserName"></label>
                                    <a class="btn green btn-sm defaultBtn" ng-click="vm.queryData();"><i
                                            class="fa fa-search"></i> 查询 </a>
                                    <a class="btn blue btn-sm" ng-click="vm.showUserSelectTree();"><i
                                            class="fa fa-cog"></i> 配置用户 </a>
                                </div>
                            </div>
                            <div class="table-scrollable" style="max-height: {{contentHeight-200}}px;overflow-y: auto;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th style="width: 100px;">姓名</th>
                                        <th style="width: 120px;">登录名</th>
                                        <th>所属部门</th>
                                        <th>系统角色</th>
                                        <th>员工职务</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="item in vm.pageInfo.list">
                                        <td ng-bind="$index+1"></td>
                                        <td ng-bind="item.userName"></td>
                                        <td ng-bind="item.userLogin"></td>
                                        <td>
                                            <span ng-bind="item.deptName"></span>
                                        </td>
                                        <td>
                                            <span  ng-bind="item.roleNames" class="data_title" ng-click="vm.showRoleModal(item);"></span>
                                        </td>
                                        <td>
                                            <span ng-bind="item.positionNames"></span>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>

                            <my-pager data-page-info="vm.pageInfo" on-load="vm.loadEmployee()"></my-pager>
                        </div>
                    </div>
                    <div class="tab-pane" id="role_acl_tab">
                        <div class="dataTables_wrapper no-footer">
                            <div class="row">
                                <div class="col-md-12">
                                    <label style="margin-right: 10px;">权限名称：<input type="search"
                                                                                   class="form-control input-inline input-sm"
                                                                                   placeholder="权限名称"
                                                                                   ng-model="vm.params.qAclName"></label>

                                    <a class="btn blue btn-sm" ng-click="vm.showRoleAclSelectTree();"><i
                                            class="fa fa-cog"></i> 配置权限 </a>
                                </div>
                            </div>

                            <div class="table-scrollable" style="max-height:{{contentHeight-200}}px;overflow-y: auto;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th style="width: 160px;">权限名称</th>
                                        <th>类型</th>
                                        <th>url</th>
                                        <th>ui-sref</th>
                                        <th style="width: 160px;">功能点</th>
                                        <th style="width: 35px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="item in vm.acls|filter:{name:vm.qAclName}">
                                        <td ng-bind="$index+1"></td>
                                        <td>
                                            <i class="{{item.icon}}"></i>
                                            <span ng-bind="item.name"></span>
                                        </td>
                                        <td>
                                            <span ng-if="item.type==1">菜单</span>
                                            <span ng-if="item.type==2">按钮</span>
                                            <span ng-if="item.type==3">其他</span>
                                        </td>
                                        <td ng-bind="item.url"></td>
                                        <td ng-bind="item.uiSref"></td>
                                        <td ng-bind="item.selectOpts"></td>
                                        <td>
                                            <i class="fa fa-trash-o margin-right-5" ng-click="vm.deleteAcl(item.id);"
                                               title="删除"></i>
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

                <div class="table-scrollable">
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
                <button type="button" class="btn blue" ng-click="vm.saveSelectRole();">确认</button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade draggable-modal" id="roleModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">角色详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label required">所属分类</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'角色类别'}:true"
                                        ng-model="vm.sysRole.roleCategory" class="form-control"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">角色名称</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.sysRole.name" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">数据权限</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.sysRole.childDeptIds"
                                           name="childDeptIds" maxlength="100"
                                           disabled/>
                                    <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.sysRole.childDeptIds);">
                                                <i class="fa fa-cog"></i></button>
                                        </span>
                                </div>
                                <span class="help-block" >请点击后方按钮选择</span>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-md-3 control-label required">排序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.sysRole.seq" placeholder="请输入整数">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label required">状态</label>
                            <div class="col-md-9">
                                <select class="form-control" ng-model="vm.sysRole.deleted"
                                        ng-init="deptStates=[{text:'有效',value:false},{text:'无效',value:true}]"
                                        ng-options="c.value as c.text for c in deptStates">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.sysRole.remark">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveRole();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade draggable-modal" id="roleAclSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="vm.currentRoleName+'-权限配置'"></h4>
            </div>
            <div class="modal-body">
                <div id="acl_select_tree" style="max-height: {{contentHeight/2}}px;overflow-y: auto;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveRoleSelectAcl();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade draggable-modal" id="userSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="vm.currentRoleName+'-用户配置'"></h4>
            </div>
            <div class="modal-body">
                <div id="user_select_tree" style="max-height: {{contentHeight/2}}px;overflow-y: auto;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectUser();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>






<div class="modal fade draggable-modal" id="deptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">配置数据权限</h4>
            </div>
            <div class="modal-body">
                <div id="dept_select_tree" style="max-height:500px;overflow-y: auto;overflow-x: hidden;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectDept();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>



