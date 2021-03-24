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
            <a ui-sref="sys.role">系统管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>权限模块</span>
        </li>
    </ul>
</div>

<div class="row">
    <div class="col-md-3">
        <div class="portlet box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-tree"></i> 权限模块
                </div>
                <div class="tools">
                    <i class="fa fa-plus margin-right-5" title="增加" ng-click="vm.addAclModule();"></i>
                    <i class="fa fa-edit" title="编辑" ng-click="vm.editAclModule();"></i>
                </div>

            </div>
            <div class="portlet-body">
                <div id="acl_module_tree"></div>
            </div>
        </div>

    </div>

    <div class="col-md-9" style="padding-left: 0px;">
        <div class="portlet box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-tree"></i> <span ng-bind="vm.currentAclModuleName+' - 权限列表'"></span>
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-default btn-sm" ng-click="vm.addAcl();">
                        <i class="fa fa-plus"></i> 增加 </a>
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.clearCache1();" >
                        <i class="glyphicon glyphicon-refresh "></i> 缓存清理 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.params.qAcl"/>
                        </div>
                        <div class="col-md-4">
                            <a class="btn green btn-sm" ng-click="vm.loadAcl();"><i class="fa fa-search"></i> 查询 </a>
                        </div>
                    </div>

                    <div class="table-scrollable" style="max-height: {{contentHeight-200}}px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>名称</th>
                                <th>权限模块</th>
                                <th style="width: 35px;">类型</th>
                                <th style="width: 200px;">ui-sref</th>
                                <th style="width: 160px;">功能点</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.acls">
                                <td ng-bind="$index+1"></td>
                                <td>
                                    <i class="{{item.icon}}"></i>
                                    <span ng-bind="item.name" class="data_title" ng-click="vm.editAcl(item.id);"></span>
                                </td>
                                <td ng-bind="item.aclModuleName"></td>
                                <td>
                                    <span ng-if="item.type==1">菜单</span>
                                    <span ng-if="item.type==2">按钮</span>
                                    <span ng-if="item.type==3">其他</span>
                                </td>
                                <td ng-bind="item.uiSref"></td>
                                <td ng-bind="item.opts"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="modal fade draggable-modal" id="aclModuleModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">权限模块详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;">
                    <div class="form-body">

                        <div class="form-group">
                            <label class="col-md-3 control-label">模块名称</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.aclModule.name" placeholder="">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">模块编码</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.aclModule.code" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">上级模块</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.aclModule.parentName"
                                           placeholder="" disabled="disabled">
                                    <span class="input-group-addon" ng-click="vm.showParentTree(vm.aclModule);">
												<i class="fa fa-asterisk"></i>
												</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group" ng-if="vm.aclModule.parentId>0">
                            <label class="col-md-3 control-label">菜单图标</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.aclModule.icon" placeholder="">
                                    <span class="input-group-addon">
												<i class="{{vm.aclModule.icon}}"></i>
												</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group" ng-if="vm.aclModule.parentId>0">
                            <label class="col-md-3 control-label">左侧显示</label>
                            <div class="col-md-9">
                                <select class="form-control" ng-model="vm.aclModule.left"
                                        ng-init="deptStates=[{text:'关闭',value:false},{text:'开启',value:true}]"
                                        ng-options="c.value as c.text for c in deptStates">
                                </select>
                            </div>
                        </div>
                        <div class="form-group" ng-if="vm.aclModule.parentId>0">
                            <label class="col-md-3 control-label">顶部显示</label>
                            <div class="col-md-9">
                                <select class="form-control" ng-model="vm.aclModule.top"
                                        ng-init="deptStates=[{text:'关闭',value:false},{text:'开启',value:true}]"
                                        ng-options="c.value as c.text for c in deptStates">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">排序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.aclModule.seq"
                                       placeholder="请输入整数">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">状态</label>
                            <div class="col-md-9">
                                <select class="form-control" ng-model="vm.aclModule.deleted"
                                        ng-init="deptStates=[{text:'有效',value:false},{text:'无效',value:true}]"
                                        ng-options="c.value as c.text for c in deptStates">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.aclModule.remark">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveAclModule();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade draggable-modal" id="aclModuleParentTreeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择父级</h4>
            </div>
            <div class="modal-body">
                <div id="acl_module_parent_tree"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.chooseAclModuleParent();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade draggable-modal" id="aclModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">权限详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;">
                    <div class="form-body">

                        <div class="form-group">
                            <label class="col-md-3 control-label">权限名称</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.sysAcl.name" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">模块名称</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.sysAcl.aclModuleName"
                                           placeholder="" disabled="disabled">
                                    <span class="input-group-addon" ng-click="vm.showAclModuleSelectTree();">
												<i class="fa fa-asterisk"></i>
												</span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">功能点</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.sysAcl.opts" placeholder="控制节点">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">权限代码</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.sysAcl.code" placeholder="">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">类型</label>
                            <div class="col-md-9">
                                <select class="form-control" ng-model="vm.sysAcl.type"
                                        ng-init="typeStates=[{text:'菜单',value:1},{text:'按钮',value:2},{text:'其他',value:3}]"
                                        ng-options="c.value as c.text for c in typeStates">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">ui-sref</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.sysAcl.uiSref" placeholder="ng">
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-md-3 control-label">url</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.sysAcl.url" placeholder="url">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">icon</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.sysAcl.icon" placeholder="图标">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">排序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.sysAcl.seq" placeholder="请输入整数">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">状态</label>
                            <div class="col-md-9">
                                <select class="form-control" ng-model="vm.sysAcl.deleted"
                                        ng-init="deptStates=[{text:'有效',value:false},{text:'无效',value:true}]"
                                        ng-options="c.value as c.text for c in deptStates">
                                </select>
                            </div>
                        </div>

                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveAcl();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade" id="aclModuleSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择所处部门</h4>
            </div>
            <div class="modal-body">
                <div id="acl_module_select_tree"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectAclModule();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>






