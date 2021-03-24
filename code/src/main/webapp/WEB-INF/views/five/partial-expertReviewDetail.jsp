<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 公司设计评审申请
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
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
                     style="min-height: 500px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stageName"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                            <label class="col-md-2 control-label">会议名称</label>
                            <div class="col-md-4">
                                <input  ng-model="vm.item.conferenceName" class="form-control" name="conferenceName"  ng-disabled="!processInstance.firstTask"/>
                            </div>
                            <label class="col-md-2 control-label">时间</label>
                            <div class="col-md-4">
                                <div class="input-group date date-picker" id="applyTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.time" name="time" required="true" ng-disabled="!processInstance.firstTask">
                                    <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                    </span>
                                </div>
                            </div>
                        </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">专家签名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.expertSign" name="expertSign" required="true" maxlength="50" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label">地点</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.place" name="place" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">评审专业</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.specialty" name="specialty"  rows="3" maxlength="100" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">评审意见</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control" ng-model="vm.item.reviewContent" name="reviewContent"  rows="3" maxlength="100" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>




                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">

                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" maxlength="100" ng-disabled="!processInstance.firstTask"/>
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

<jsp:include page="../common/common-edFile.jsp"/>

<jsp:include page="./print/print-expertReviewDetail.jsp"/>




