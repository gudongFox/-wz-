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
            <span>{{vm.item.type}}认定申报表</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>{{vm.item.applyType}}认定申报表
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

            <span ng-show="item.type=='总设计师'&&item.processEnd&&!item.handled" style="color: red;">等待自动更新</span>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
        </div>
    </div>
    <div class="portlet-body">

        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1" style="min-height: 400px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" style="padding-right: 30px;" id="detail_form">
                        <div class="form-body">

                            <div class="form-group">
                                <label class="col-md-2 control-label required">姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="姓名" name="chiefUserName" required="true" ng-model="vm.item.chiefUserName"
                                           disabled="disabled" />
                                </div>
                                <label class="col-md-2 control-label required">职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  placeholder="职工号" name="chiefUserLogin" required="true" disabled ng-model="vm.item.chiefUserLogin" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">性别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'性别'}:true" disabled
                                            ng-model="vm.item.gender" class="form-control"></select>
                                </div>

                                <label class="col-md-2 control-label ">出生日期</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true" ng-model="vm.item.birthDay"  disabled />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">职称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true" ng-model="vm.item.ranks"  disabled />
                                </div>

                                <label class="col-md-2 control-label ">职称认定时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true" ng-model="vm.item.rankTime"  disabled />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">员工类别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'员工类型'}:true" disabled
                                            ng-model="vm.item.userType" class="form-control"></select>
                                </div>

                                <label class="col-md-2 control-label ">设计专业</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true" ng-model="vm.item.specialty"  disabled />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">学历</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'学历'}:true" disabled
                                            ng-model="vm.item.educationBackground" class="form-control"></select>
                                </div>

                                <label class="col-md-2 control-label ">入职时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true" ng-model="vm.item.joinCompanyTime"  disabled />
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.type=='兼职总设计师'">
                                <label class="col-md-2 control-label required">拟承担设计项目</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectTypeNow" required="true" name="projectTypeNow"  ng-disabled="!processInstance.firstTask"/>

                                </div>
                                <label class="col-md-2 control-label required">拟承担项目类型</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectTypeApply" required="true"   ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.showProjectTypeNowModel('projectTypeApply');vm.opt=='projectTypeApply'"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">业绩及获奖情况</label>
                                <div class="col-md-10">
                                   <textarea type="text" class="form-control"  placeholder="业绩及获奖情况" rows="6" name="performance" required="true" ng-disabled="!processInstance.firstTask" ng-model="vm.item.performance"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="selectMajorModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">从事专业</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>专业名称</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="major in vm.majors">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.major.indexOf(major.code)>=0" class="cb_major" data-name="{{major.code}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="major.code"></td>
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

<div class="modal fade" id="selectProjectTypeNowModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">项目类型</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>项目类型名称</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="sysCode in sysCodes | filter:{codeCatalog:'五洲项目类型'}:true">
                            <td ng-if="vm.opt=='projectTypeNow'">
                                <input type="checkbox" ng-checked="vm.item.projectTypeNow.indexOf(sysCode.code)>=0" class="cb_projectTypeNow" data-name="{{sysCode.code}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-if="vm.opt=='projectTypeApply'">
                                <input type="checkbox" ng-checked="vm.item.projectTypeApply.indexOf(sysCode.code)>=0" class="cb_projectTypeNow" data-name="{{sysCode.code}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="sysCode.name"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectProjectTypeNow();">确认</button>
            </div>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='申报资料'"></div>

<jsp:include page="../../hr/print/print_five_hr_qualify_apply_chief.jsp"/>
<jsp:include page="../../hr/print/print_five_hr_qualify_apply_proChief.jsp"/>

