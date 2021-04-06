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
            <span ng-bind="tableName">{{vm.item.type}}岗位资格认定</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">{{vm.item.applyType}}资格认定申报</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
              <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
              <small ng-if="vm.item.handled" style="color: red;">岗位资格已更新</small>
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
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 220px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申报年份</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true" ng-model="vm.item.checkYear"  ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label ">申报部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName"
                                           name="deptName" maxlength="100"
                                           disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true" ng-model="vm.item.userName"  disabled />
                                </div>

                                <label class="col-md-2 control-label ">职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true" ng-model="vm.item.userLogin"  disabled />
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

                                <label class="col-md-2 control-label ">最高学历</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'学历'}:true" disabled
                                            ng-model="vm.item.educationBackground" class="form-control"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">入职时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true" ng-model="vm.item.joinCompanyTime"  disabled />
                                </div>
                               <%-- <label class="col-md-2 control-label required ">拟申报岗位资格</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.planPost" required="true"
                                               disabled="disabled"/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"
                                                    ng-click="vm.showProjectTypeModal();">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>--%>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">业绩及获奖情况</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" placeholder="请填写申报说明..."  ng-model="vm.item.remark" name="remark" required maxlength="100" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
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
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-user"></i> <span ng-bind="'审定人申报'"></span>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: 300px;overflow-y: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer" id="detailTable">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="width: 70px">姓名</th>
                    <th style="width: 70px">登录名</th>
                    <th>从事专业</th>
                    <th>项目类型</th>
                    <th><i class="fa fa-bookmark"></i> 审定人-院级</th>
                    <th><i class="fa fa-bookmark"></i> 审定人-公司级</th>
                    <th>设计人</th>
                    <th>校核人</th>
                    <th>审核人</th>
                    <th style="width: 150px">创建时间</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="item.userName"></td>
                    <td ng-bind="item.userLogin"></td>
                    <td ng-bind="item.majorName" class="data_title" ng-click="vm.showMajorModal(item);"></td>
                    <td ng-bind="item.projectType" ></td>
                    <td ng-bind="item.approve?'√':'x'" class="data_title"
                        ng-click="vm.toggleDetail(item.id,'approve',processInstance.firstTask);" style="color:{{item.approve?'green':'red'}}"></td>
                    <td ng-bind="item.majorCharge?'√':'x'" class="data_title"
                        ng-click="vm.toggleDetail(item.id,'majorCharge',processInstance.firstTask);" style="color:{{item.majorCharge?'green':'red'}}"></td>
                    <td ng-bind="item.design?'√':'x'"></td>
                    <td ng-bind="item.proofread?'√':'x'"></td>
                    <td ng-bind="item.audit?'√':'x'"></td>

                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>

                </tr>
                </tbody>
            </table>
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
                        <tr ng-repeat="major in sysCodes | filter:{codeCatalog:'设计专业'}:true">
                            <td>
                                <input type="checkbox" ng-checked="vm.detail.majorName.indexOf(major.code)>=0"
                                       class="cb_major" data-name="{{major.code}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="major.name"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectMajor();" ng-show="processInstance.firstTask">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="selectProjectTypeModal" tabindex="-1" role="basic" aria-hidden="true">
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
                        <tr ng-repeat="projectType in sysCodes | filter:{codeCatalog:'五洲项目类型'}:true">
                            <td>
                                <input type="checkbox" ng-checked="vm.detail.projectType.indexOf(projectType.code)>=0"
                                       class="cb_projectType" data-name="{{projectType.code}}"
                                       style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="projectType.name"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectProjectType();"  ng-show="processInstance.firstTask">确认</button>
            </div>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='申报资料'"></div>




