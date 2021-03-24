<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-doc"></i> 总体方案评审
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/five/reviewPlan/exportData.json?id='+vm.item.id}}"
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
                     style="min-height: 280px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label required">评审主持人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.hostMenName"
                                               name="hostMenName"
                                               readonly/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=user;vm.opt ='hostMen';vm.optUser=vm.item.hostMen;vm.showUserModel();"
                                                ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block font-red"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">参与人</label>
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
                                    <textarea rows="3" type="text" class="form-control" ng-model="vm.item.planDesc" name="solutionOverview" required ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">方案指导意见</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control" ng-model="vm.item.solutionGuidance" name="solutionGuidance" required ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">重要技术问题讨论</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control" ng-model="vm.item.solutionDiscussion" name="solutionDiscussion" required ng-disabled="!processInstance.firstTask"></textarea>
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
                     style="height: 280px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 280px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>






