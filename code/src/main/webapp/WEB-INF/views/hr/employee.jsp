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
            <span>员工信息管理</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="row">

    <div class="col-md-3">
        <div class="portlet box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-user"></i> 员工状态
                </div>
                <div class="tools">
                    <i class="fa  fa-arrow-left" title="返回" ng-click="back();"></i>
                </div>
            </div>
            <div class="portlet-body">
                <div id="js_tree"></div>
            </div>
        </div>
    </div>
    <div class="col-md-9" style="padding-left: 0px;">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-user"></i> 员工数据
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.queryData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.params.q"/>
                        </div>
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="部门名称"
                                   ng-model="vm.params.qDeptName"/>
                        </div>
                        <div class="col-md-4">
                            <a class="btn green btn-sm" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>

                            <a class="btn blue btn-sm" ng-click="vm.addEmployee();"><i class="fa fa-plus"></i> 新增 </a>
                        </div>
                    </div>

                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 40px;">#</th>
                                <th>姓名</th>
                                <th>登录名</th>
                                <th>所属部门</th>
                                <th>系统角色</th>
                                <th>职务</th>
                                <th>员工专业</th>
                                <th>员工类别</th>
                                <th style="width: 80px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-click="vm.show(item.userLogin)">
                                    <img alt="" class="img-circle" ng-src="{{'/sys/attach/download/'+item.headAttachId}}"  onerror="this.src='/m/admin/layout/img/avatar9.jpg'" style="height: 30px;width: 30px;border-radius: 10%;"/>
                                    <span ng-bind="item.userName" class="data_title" ></span>
                                </td>
                                <td ng-bind="item.userLogin"></td>
                                <td>
                                    <span ng-bind="item.deptName"></span>
                                    <i class="fa  fa-cog font-blue" title="部门" ng-click="vm.showDeptModal(item);"></i>
                                </td>
                                <td>
                                    <span ng-bind="item.roleNames"></span>
                                    <i class="fa  fa-cog font-blue" title="角色" ng-click="vm.showRoleModal(item);"></i>
                                </td>
                                <td>
                                    <span ng-bind="item.positionNames"></span>
                                    <i class="fa  fa-cog font-blue" title="职务"
                                       ng-click="vm.showPositionModal(item);"></i>
                                </td>
                                <td ng-bind="item.specialty"></td>
                                <td ng-bind="item.userType">
                                <td>
                                    <i class="fa fa-lock margin-right-5" ng-click="vm.resetPwd(item.userLogin);"
                                       title="重置密码"></i>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.userLogin);"
                                       title="详情"></i>
                                    <i class="fa fa-trash-o" ng-show="user.userLogin!=item.userLogin" ng-click="vm.remove(item.userLogin);"
                                       title="删除"></i>
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

<div class="modal fade" id="addEmployeeModal" tabindex="-1" role="basic" aria-hidden="true">
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
                                    <input type="text" class="form-control" ng-model="vm.deptNameId" disabled />
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

<div class="modal fade" id="deptSelectModalId" tabindex="-1" role="basic" aria-hidden="true" >
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
