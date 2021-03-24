<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">综合办公</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>督办管理子项</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.noticeTitle"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> 督办管理子项
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator">
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>

    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_detail_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 280px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">督办类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'督办类型'}:true"
                                            ng-model="vm.item.superviseType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">任务来源</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.taskSource"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">任务内容</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.taskDefinition"  name="taskDefinition" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">办结时限</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applyTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.timeLimit" name="timeLimit" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">承办理部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">反馈周期</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'反馈周期'}:true"
                                            ng-model="vm.item.feedbackTime" class="form-control"
                                            ng-disabled="!processInstance.firstTask"   ></select>
                                </div>

                                <label class="col-md-2 control-label required">承办人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.transactorName" name="transactorName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                                <button class="btn default" type="button" ng-click="vm.showUserModel('transactor');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                            </span>
                                    </div>
                                </div>


                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">办理进度</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'办理进度'}:true"
                                            ng-model="vm.item.transactionPlan" class="form-control"

                                            ng-disabled="processInstance.currentTaskName.indexOf('办理人')==-1"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">承办人意见</label>
                                <div class="col-md-10">
                                    
                                    <textarea rows="3" class="form-control" ng-model="vm.item.companyLeaderOpinion" name="companyLeaderOpinion" placeholder="" ng-disabled="processInstance.currentTaskName.indexOf('办理人')==-1"></textarea>
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
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>




