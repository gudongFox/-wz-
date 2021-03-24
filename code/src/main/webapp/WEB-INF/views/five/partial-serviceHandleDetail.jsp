<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 技术服务问题处理单
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/five/serviceHandle/exportData.json?id='+vm.item.id}}"
               target="_blank">
                <i class="fa fa-file-word-o"></i> 导出</a>
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
                     style="height: 500px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label ">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">项目编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label">项目阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stageName" disabled/>
                                </div>
                                <label class="col-md-2 control-label">期次名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stepName" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">用户单位名称</label>
                                <div class="col-md-4">
                                    <input  ng-model="vm.item.departmentName" class="form-control" name="departmentName"   disabled/>
                                </div>
                                <label class="col-md-2 control-label required">登记日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="registerTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.registerTime" name="time" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                    </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">类别</label>
                                <div class="col-md-4">
                                    <input  ng-model="vm.item.category" class="form-control" name="category"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label">登记编号</label>
                                <div class="col-md-4">
                                    <input  ng-model="vm.item.registerNo" class="form-control" name="registerNo"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <%--<div class="form-group">
                                <label class="col-md-2 control-label">经办人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                            ng-model="vm.item.agent"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label">答复日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="replyTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.replyTime" name="time" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                    </span>
                                    </div>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">逐条登记</br>问题内容</label>
                                <div class="col-md-10">

                                    <textarea type="text" class="form-control" ng-model="vm.item.registerContent" name="remark" required="true" maxlength="100" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">专业人员</br>处理意见</label>
                                <div class="col-md-10">

                                    <textarea type="text" class="form-control" ng-model="vm.item.specialistReview" name="remark" maxlength="100" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">主管领导总设</br>计师签署意见</label>
                                <div class="col-md-10">

                                    <textarea type="text" class="form-control" ng-model="vm.item.departmentReview" name="remark" maxlength="100"
                                              ng-disabled="processInstance.myRunningTaskName!='项目总师'" placeholder="项目总师填写意见"
                                    />
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
                     style="height: 500px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 500px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="./print/print-serviceHandleDetail.jsp"/>




