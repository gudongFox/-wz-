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
            <span>行政职务设置</span>
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
                    <i class="fa fa-tree"></i> 行政职务管理
                </div>
                <div class="tools">
                    <i class="fa fa-plus margin-right-5" title="增加" ng-click="vm.addPosition();"></i>
                    <i class="fa fa-edit" title="编辑" ng-click="vm.show();"></i>
                </div>

            </div>
            <div class="portlet-body">
                <div id="position_tree"></div>
            </div>
        </div>

    </div>

    <div class="col-md-9" style="padding-left: 0px;">
        <div class="portlet box blue">
            <div class="portlet-title tabbable">
                <div class="caption">
                    <i class="fa fa-user"></i> <span ng-bind="vm.params.currentPositionName"></span>
                </div>
                <ul class="nav nav-tabs">
                    <li class="active">
                        <a href="#Position_user_tab" data-toggle="tab" aria-expanded="false">
                            用户列表 </a>
                    </li>
                </ul>
            </div>
            <div class="portlet-body">
                <div class="tab-content">
                    <div class="tab-pane active" id="Position_user_tab">
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
                                            <span  ng-bind="item.PositionNames" class="data_title" ng-click="vm.showPositionModal(item);"></span>
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
                </div>
            </div>
        </div>
    </div>
</div>


<div class="modal fade draggable-modal" id="positionModal" tabindex="-1" Position="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">行政职务详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" Position="form" style="padding-right: 30px;" id="detail_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label required">职务名称</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.position.positionName"
                                       required="true"
                                       maxlength="20" name="positionName" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">部门负责人</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                        ng-model="vm.position.deptCharge" class="form-control"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">行政层级</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.position.positionLv"
                                       placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">数据权限</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.position.childDeptIds"
                                           name="childDeptIds"
                                           disabled/>
                                    <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.position.childDeptIds);">
                                                <i class="fa fa-cog"></i></button>
                                        </span>
                                </div>
                                <span class="help-block" >请点击后方按钮选择</span>
                            </div>
                        </div>


                        <div class="form-group">
                            <label class="col-md-3 control-label required">顺序</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.position.seq"
                                       placeholder="输入数字越小,排序越靠前">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">状态</label>
                            <div class="col-md-9">
                                <select class="form-control" ng-model="vm.position.deleted"
                                        ng-init="deptStates=[{text:'有效',value:false},{text:'无效',value:true}]"
                                        ng-options="c.value as c.text for c in deptStates">
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">创建时间</label>
                            <div class="col-md-9">
                                <div class="input-icon right">
                                    <i class="fa fa-clock-o"></i>
                                    <input type="text" class="form-control"
                                           value="{{vm.position.gmtCreate|date:'yyyy-MM-dd HH:mm'}}"
                                           disabled="disabled">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">修改时间</label>
                            <div class="col-md-9">
                                <div class="input-icon right">
                                    <i class="fa fa-clock-o"></i>
                                    <input type="text" class="form-control"
                                           value="{{vm.position.gmtModified |date:'yyyy-MM-dd HH:mm'}}"
                                           disabled="disabled">
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.position.remark "
                                       placeholder="">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.save();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade draggable-modal" id="deptSelectModal" tabindex="-1" Position="basic" aria-hidden="true">
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