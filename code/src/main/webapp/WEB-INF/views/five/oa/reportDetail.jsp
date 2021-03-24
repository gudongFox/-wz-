<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:40})">公文管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">报告文单</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">报告文单</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>>
                <i class="fa fa-print"></i> 打印 </a>
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
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">办公室主任</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.companyOfficeManName" name="companyOfficeManName"    ng-disabled="processInstance.myRunningTaskName.indexOf('机要秘书-核收')==-1"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('companyOfficeMan');"  ng-disabled="processInstance.myRunningTaskName.indexOf('机要秘书-核收')==-1"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('机要秘书-核收')>=0">请点击左侧按钮选择办公室主任</span>
                                </div>

                                <label class="col-md-2 control-label required">批示领导</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.companyLeaderName" name="companyLeaderName"    ng-disabled="processInstance.myRunningTaskName.indexOf('公司办-核稿')==-1"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('companyLeader');"  ng-disabled="processInstance.myRunningTaskName.indexOf('公司办-核稿')==-1"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('公司办-核稿')>=0">请点击左侧按钮选择批示领导</span>
                                </div>

                            </div>
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">分发方式</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'报告文单分发方式'}:true"
                                            ng-model="vm.item.flag" class="form-control"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('机要秘书-分发')==-1"></select>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('机要秘书-分发')>=0">请点击左侧按钮选择分发方式</span>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.flag=='正副职领导批示'">副职领导</label>
                                <div class="col-md-4" ng-if="vm.item.flag=='正副职领导批示'">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.viceLeaderName" name="viceLeaderName"    ng-disabled="processInstance.myRunningTaskName.indexOf('机要秘书-分发')==-1"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('viceLeader');" ng-disabled="processInstance.myRunningTaskName.indexOf('机要秘书-分发')==-1"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('机要秘书-分发')>=0">注：选择副职领导后自动分发</span>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">送签部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required" style="font-weight: bold;">负责人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.chargeManName" name="chargeManName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('chargeMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>

                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label required">公司办编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.officeNo" name="officeNo" required="true"    ng-disabled="processInstance.myRunningTaskName.indexOf('公司办公室-审批')==-1"/>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('公司办公室-审批')>=0">请填写公司办编号</span>
                                </div>

                                <label class="col-md-2 control-label required">报送日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="reportTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.reportTime" name="reportTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                           </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">事项</label>
                            <div class="col-md-10">
                                <textarea rows="3" class="form-control" ng-model="vm.item.reportContent" required="true" name="reportContent" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
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
                    </form>
                    <jsp:include page="../../common/common-opinion.jsp"/>
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

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade draggable-modal" id="deptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择所属部门</h4>
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

<jsp:include page="../print/print-oaReportDetail.jsp"/>
