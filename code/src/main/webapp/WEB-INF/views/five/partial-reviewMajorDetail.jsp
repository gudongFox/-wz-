<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-doc"></i> 专业设计评审
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/five/reviewMajor/exportData.json?id='+vm.item.id}}"
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
                     style="min-height: 380px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label required">设计评审形式</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'设计评审形式'}:true"
                                            ng-model="vm.item.reviewType" name="reviewType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>

                                <label class="col-md-2 control-label">评审方式</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.designReviewType" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">评审专业</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.majorName" name="majorName" readonly required/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.showArrangeUserModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-gear"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block font-red" ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">评审主持人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.hostMenName" name="hostMenName" required readonly/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=user;vm.opt ='hostMen';vm.optUser=vm.item.hostMen;vm.showUserModel();"
                                                ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block font-red"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>

                         <%--   <div class="form-group">
                                <label class="col-md-2 control-label">校核人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.proofreadMenName" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">审核人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.auditMenName" disabled="disabled"/>
                                </div>
                            </div>--%>

                            <div class="form-group" >

                                <label class="col-md-2 control-label required">参与人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.attendUserName" name="attendUserName" required readonly/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=user;vm.opt ='attendUser';vm.optUser=vm.item.attendUser;vm.showUserModel();"
                                                ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block font-red"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">方案概况</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control" ng-model="vm.item.solutionOverview" name="solutionOverview" required ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">方案指导意见</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control" ng-model="vm.item.solutionGuidance" name="solutionGuidance" required ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">重要技术问题讨论</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control" ng-model="vm.item.solutionDiscussion" name="solutionDiscussion" required ng-disabled="!processInstance.firstTask"/>
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
                     style="height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="arrangeUserModal">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">选择评审专业</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>专业</th>
                            <th>专业负责人</th>
                            <th>设计人</th>
                            <th>标准化审查</th>
                            <th>校核</th>
                            <th>审核</th>
                            <th>审定</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="arrange in vm.arrangeUsers">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.majorName==arrange.majorName" class="cb_arrange"
                                       data-name="{{arrange}}" data-id="{{'file_'+arrange.id}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="arrange.majorName"></td>
                            <td ng-bind="arrange.majorChargeMenName"></td>
                            <td ng-bind="arrange.designMenName"></td>
                            <td ng-bind="arrange.criterionExamineMenName"></td>
                            <td ng-bind="arrange.proofreadMenName"></td>
                            <td ng-bind="arrange.auditMenName"></td>
                            <td ng-bind="arrange.approveMenName"></td>
                        </tr>

                        </tbody>
                    </table>
                </div>
                <tr style="padding-left: 15px;">
                           <span class="data_title" style="font-size: 8px;margin-top: 2px" title="全选"
                                 ng-click="vm.selectAllFile();">全选</span><span
                        style="margin-left: 10px;font-size: 8px;" class="data_title" title="反选"
                        ng-click="vm.toggleSelectFile();">反选</span>
                </tr>
            </div>
            <div class="modal-footer">

                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectMajor();">确认</button>
            </div>
        </div>
    </div>
</div>


<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>






