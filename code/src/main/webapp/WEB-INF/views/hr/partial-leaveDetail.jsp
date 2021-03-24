<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<div class="page-bar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a>人资管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>离职申请</a>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-diamond"></i>员工离职申请
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();"
               ng-show="processInstance.passAble">
                <i class="fa fa-save"></i> 保存 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.passAble"
               ng-click="vm.showSendFlow();">
                <i class="fa fa-send"></i> 发送 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.backAble"
               ng-click="showBackFlow();">
                <i class="fa fa-reply"></i> 打回 </a>

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
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">员工姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="" ng-model="vm.item.userName" disabled
                                           />
                                </div>
                                <label class="col-md-2 control-label">自助账号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.userLogin" disabled
                                           />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">公司名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.companyName" disabled />
                                </div>
                                <label class="col-md-2 control-label">部门名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" disabled />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">入职时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.joinCompanyTime" disabled />
                                </div>
                                <label class="col-md-2 control-label">电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.mobile" required="true" name="mobile" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">申请离职时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applyLeaveTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.applyLeaveTime" name="applyLeaveTime" required="true" ng-disabled="!processInstance.firstTask" >
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar" ></i></button>
                                        </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label">批准离职时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="approveLeaveTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.approveLeaveTime" name="approveLeaveTime" ng-disabled="!processInstance.firstTask" >
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">离职原因</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.leaveReason" required="true" name="leaveReason"
                                              ng-disabled="!processInstance.firstTask" placeholder=""></textarea>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label">部门工作交接</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.deptResult"  name="deptResult"
                                              ng-required="processInstance.myTaskName=='部门领导'"    ng-disabled="processInstance.myTaskName!='部门领导'" placeholder=""></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">财务交接</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.financeResult"  name="financeResult"
                                              ng-required="processInstance.myTaskName=='财务部'"   ng-disabled="processInstance.myTaskName!='财务部'" placeholder=""></textarea>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">综合管理部交接</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.officeResult" name="officeResult"
                                              ng-required="processInstance.myTaskName=='综合管理部'"    ng-disabled="processInstance.myTaskName!='综合管理部'" placeholder=""></textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">离职说明(hr)</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.hrRemark" name="hrRemark"
                                              ng-disabled="processInstance.myTaskName!='综合管理部'" placeholder=""></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">离职去向</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.hrDest"  name="hrDest"
                                              ng-disabled="processInstance.myTaskName!='综合管理部'" placeholder=""></textarea>
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
                                           value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../ed/project/part-actFlow.jsp"/>

