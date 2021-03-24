<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-settings"></i> 项目分期管理
        </div>
        <div class="actions">

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();" ng-show="vm.isShow">
                <i class="fa fa-save"></i> 保存 </a>

            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form" id="detail_form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label">工程名称</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="工程名称" ng-model="vm.item.projectName"
                               name="projectName" maxlength="50" ng-disabled="!vm.isShow">
                    </div>
                    <label class="col-md-2 control-label">工程号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="工程号" ng-model="vm.item.projectNo"
                               name="projectNo" maxlength="30" required ng-disabled="!vm.isShow">
                    </div>

                </div>
                <div class="form-group">

                    <label class="col-md-2 control-label">合同号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.contractNo" name="contractNo" required="true" ng-disabled="!vm.isShow" >
                    </div>
                    <label class="col-md-2 control-label">设计阶段</label>
                    <div class="col-md-4">


                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'勘察阶段'}:true"
                                ng-model="vm.item.stageName" class="form-control" style="height:35px;" required="true" ng-disabled="!vm.isShow"></select>
                    </div>


                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">分期名称</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.stepName" name="stepName"
                               maxlength="50" required ng-disabled="!vm.isShow">
                    </div>
                    <label class="col-md-2 control-label">分期排序</label>
                    <div class="col-md-4">
                        <input type="number" class="form-control" ng-model="vm.item.seq" name="seq" max="99" required ng-disabled="!vm.isShow">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label"><strong>执行负责人</strong></label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.item.exeChargeManName"
                                   name="designManName" required="true" disabled/>
                            <span class="input-group-btn">
                                <button class="btn default" type="button" ng-click="vm.currentUser=user;vm.optUserType='执行负责人';vm.optUser=vm.item.exeChargeMan;vm.showSelectEmployeeModal();"
                                        ng-disabled="vm.isShow()"><i class="fa fa-user" ></i></button>
                                </span>
                        </div>
                        <span class="help-block" ng-show="vm.isShow">请点击后方按钮选择</span>
                    </div>
                    <label class="col-md-2 control-label">分期负责人</label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.item.chargeManName"
                                   name="designManName" required="true" disabled />
                            <span class="input-group-btn">
                                <button class="btn default" type="button" ng-click="vm.currentUser=user;vm.optUserType='分期负责人';vm.optUser=vm.item.chargeMan;vm.showSelectEmployeeModal();"
                                        ng-disabled="!vm.isShow"><i class="fa fa-user" ></i></button>
                                </span>
                        </div>
                        <span class="help-block"  ng-show="vm.isShow">请点击后方按钮选择</span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label"><strong>协同设计</strong></label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'启用'}:true"
                                ng-model="vm.item.cp" class="form-control" ng-disabled="!vm.isShow"></select>
                    </div>
                    <label class="col-md-2 control-label">运行状态</label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'运行状态'}:true"
                                ng-model="vm.item.cpClosed" class="form-control" ng-disabled="!vm.isShow"></select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">文件排序方式</label>
                    <div class="col-md-4">
                        <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'文件夹排序方式'}:true"
                                ng-model="vm.item.contentSortMethod" class="form-control" ng-disabled="!vm.isShow"></select>
                    </div>
                    <label class="col-md-2 control-label">备注</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.remark" maxlength="100" ng-disabled="!vm.isShow"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label">创建人</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.creatorName" disabled="disabled"/>
                    </div>

                    <label class="col-md-2 control-label">创建时间</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control"
                               value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                    </div>
                </div>

            </div>
        </form>
    </div>
</div>


<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa  fa-building"></i> 子项管理
        </div>
        <div class="actions">

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadBuild();">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addBuild();" ng-show="vm.isShow">
                <i class="fa fa-plus"></i> 新增 </a>
        </div>

    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>名称</th>
                    <th>编号</th>
                    <th style="width: 150px;">创建时间</th>
                    <th style="width: 150px;">修改时间</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="build in vm.buildList">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="build.buildName"></td>
                    <td ng-bind="build.buildNo"></td>
                    <td ng-bind="build.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                    <td ng-bind="build.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showBuild(build.id);" title="详情"></i>
                        <i class="fa fa-trash" ng-click="vm.removeBuild(build.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="buildModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">子项详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-body">

                        <div class="form-group">
                            <label class="col-md-4 control-label">名称</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       ng-model="vm.build.buildName"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">编号</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       ng-model="vm.build.buildNo"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">排序</label>
                            <div class="col-md-7">
                                <input type="number" class="form-control"
                                       ng-model="vm.build.seq"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">创建时间</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       value="{{vm.build.gmtCreate | date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">修改时间</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       value="{{vm.build.gmtModified | date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveBuild();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade" id="selectEmployeeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="vm.optUserType" >人员任命</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">


                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.qEmployee"/>
                        </div>
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="部门名称"
                                   ng-model="vm.qEmployeeDeptName"/>
                        </div>
                        <div class="col-md-8">
                            <a class="btn green btn-sm" ng-click="vm.loadEmployee();"><i class="fa fa-search"></i> 查询
                            </a>
                        </div>
                    </div>
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">序号</th>
                                <th>姓名</th>
                                <th>登录名</th>
                                <th>所属部门</th>
                                <th>员工专业</th>
                                <th>联系电话</th>
                                <th>员工类别</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.employees|filter:{deptName:vm.qEmployeeDeptName}|filter:{userName:vm.qEmployee}">
                                <td>
                                    <input type="checkbox" class="cb_employee" ng-checked="vm.optUser.indexOf(item.userLogin)>=0" data-name="{{item.userLogin+'_'+item.userName}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="item.userName"></td>
                                <td ng-bind="item.userLogin"></td>
                                <td>
                                    <span ng-bind="item.deptName"></span>
                                </td>
                                <td ng-bind="item.specialty"></td>
                                <td ng-bind="item.mobile"></td>
                                <td ng-bind="item.userType">
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectEmployee();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>



