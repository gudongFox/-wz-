<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar"  id="pageBar">
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
            <span>设计资质详情</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>



<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-database"></i>设计资质详情
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>

        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();">
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                       基本信息 </a>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">姓名</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.userName"
                                               disabled="disabled" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.showEmployeeModel();">
                                                <i class="fa fa-user"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block" >请点击后方按钮选择</span>
                                </div>

                                <label class="col-md-2 control-label">专业</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.specialty"
                                               disabled="disabled" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.showSelectMajorModel();">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block" >请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">省份</label>
                                <div class="col-md-4">
                                    <select name="province" class="form-control"
                                            ng-model="vm.provinceId" ng-change="vm.provinceChange(vm.provinceId);"
                                            ng-options="id as name for (id,name) in vm.provinceArr"></select>
                                </div>

                                <label class="col-md-2 control-label">城市</label>
                                <div class="col-md-4">
                                        <select name="city" class="form-control" ng-init="city.id='0'"
                                                ng-model="vm.cityId" ng-change="vm.cityChange(vm.cityId);"
                                                ng-options="id as name for (id,name) in vm.getCityArr"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">开始时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="startTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.startTime" name="time" required="true" >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                    </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">结束时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="endTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.endTime" name="time" required="true" >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                    </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark"  name="remark" maxlength="100"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">修改时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}"disabled="disabled"/>
                                </div>
                            </div>
                            </div>
                    </form>
                </div>
            </div>

        </div>

    </div>
</div>
<jsp:include page="../common/common-edFile.jsp"/>
<div class="modal fade" id="selectEmployeeModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">人员信息</h4>
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
                                    <input type="checkbox" class="cb_employee" ng-checked="item.userLogin.indexOf(vm.item.userLogin)>=0" data-name="{{item.userLogin+'_'+item.userName}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="item.userName"></td>
                                <td ng-bind="item.userLogin"></td>
                                <td>
                                    <span ng-bind="item.deptName"></span>
                                </td>
                                <td ng-bind="item.specialty"></td>
                                <td ng-bind="item.mobile"></td>
                                <td ng-bind="item.userType"></td>
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
<div class="modal fade" id="selectMajorModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">专业列表</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th >专业名称</th>
                                <th >专业代码</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="major in vm.majorNames">
                                <td>
                                    <input type="checkbox" class="cb_destMajor" ng-checked="vm.item.specialty.indexOf(major.name)>=0"  data-name="{{major.name}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td  ng-bind="major.name"></td>
                                <td ng-bind="major.code"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectMajor();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
