<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet box blue" ng-repeat="group in groupList" ng-if="$index==0&&processInstanceId">
    <div class="portlet-title">
        <div class="caption">
            <i class="{{template.formIcon}}"></i> <span class="caption-subject" ng-bind="template.formDesc"></span>
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName"
                   style="color: #35e0e1;"></small>
        </div>

        <div class="actions">
            <span>
                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="loadData();">
                    <i class="fa fa-refresh"></i> 刷新 </a>

                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="save();"
                   ng-show="tplConfig.saveAble">
                    <i class="fa fa-save"></i> 保存 </a>

                <span ng-include="'/web/v1/tpl/actAction.html'"></span>

                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                    <i class="fa fa-arrow-left"></i> 返回 </a>
            </span>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_basic" data-toggle="tab" aria-expanded="true" ng-bind="group.groupName">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_task" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_diagram" data-toggle="tab" aria-expanded="false">
                        流程图片 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_basic">
                    <div ng-include="'/web/v1/tpl/formInfo.html'"></div>
                </div>
                <div class="tab-pane" id="tab_task">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_diagram">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="portlet box blue" ng-repeat="group in groupList" ng-if="!processInstanceId||$index>0">
    <div class="portlet-title">
        <div class="caption" ng-show="$index>0">
            <i class="icon-note"></i><span ng-bind="($index+1)+'.'+group.groupName"></span>
        </div>
        <div class="caption" ng-show="$index===0">
            <i class="{{tplConfig.formIcon}}"></i><span ng-bind="tplConfig.formDesc"></span>
        </div>
    </div>
    <div class="portlet-body">
        <div ng-include="'/web/v1/tpl/formInfo.html'"></div>
    </div>
</div>

<div class="portlet box blue" id="div_user">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-user"></i> 人员安排
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="loadUser();">
                <i class="fa fa-refresh"></i> <span ng-click="showMethod=!(showMethod)"
                                                    ng-bind="showMethod?'子项分类显示':'专业分类显示'"></span> </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="initArrangeUserFromLatestEdTask();"
               ng-show="tplConfig.saveAble">
                <i class="fa  fa-retweet"></i> 以设计任务书初始化 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="exportUser();"><i
                    class="fa fa-download"></i> 下载数据 </a>
            <span class="btn btn-sm btn-default fileinput-button" ng-show="tplConfig.saveAble">
                            <i class="fa fa-upload"></i>
                            <span> 上传数据 </span>
                            <input type="file" name="file" id="uploadUserData"
                                   data-url="/common/edArrangeUser/upload.json" accept="application/vnd.ms-excel">
             </span>


            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="loadUser();">
                <i class="fa fa-refresh"></i> 刷新 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="tabbable-line" ng-if="showMethod">
            <ul class="nav nav-tabs">
                <li ng-class="{true: 'active', false: ''}[$index==0]" ng-repeat="majorName in majorNameList">
                    <a ng-href="{{'#tb_major_'+$index}}" data-toggle="tab" aria-expanded="true">
                        <span ng-bind="majorName"></span></a>
                </li>
            </ul>
            <div class="tab-content">
                <div ng-class="{true: 'tab-pane active', false: 'tab-pane'}[$index==0]"
                     id="{{'tb_major_'+$index}}" ng-repeat="majorName in majorNameList">
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 15px;">#</th>
                                <th style="width: 120px;" ng-repeat="property in users[0].propertyList"
                                    ng-if="$index==0" ng-bind="property.name"></th>
                                <th style="width: 80px;" ng-repeat="property in users[0].propertyList" ng-if="$index==1"
                                    ng-bind="property.name"></th>
                                <th ng-repeat="property in users[0].propertyList" ng-if="$index>1"
                                    ng-bind="property.name"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="user in users| filter:{majorName:majorName}:true">
                                <td ng-bind="$index+1"></td>
                                <td ng-repeat="property in user.propertyList" ng-bind="property.name"
                                    ng-click="showUser($index,property);"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="tabbable-line" ng-if="!showMethod">
            <ul class="nav nav-tabs">
                <li ng-class="{true: 'active', false: ''}[$index==0]" ng-repeat="buildName in buildNameList">
                    <a ng-href="{{'#tb_build_'+$index}}" data-toggle="tab" aria-expanded="true">
                        <span ng-bind="buildName"></span></a>
                </li>
            </ul>
            <div class="tab-content">
                <div ng-class="{true: 'tab-pane active', false: 'tab-pane'}[$index==0]"
                     id="{{'tb_build_'+$index}}" ng-repeat="buildName in buildNameList">
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 15px;">#</th>
                                <th style="width: 120px;" ng-repeat="property in users[0].propertyList"
                                    ng-if="$index==0" ng-bind="property.name"></th>
                                <th style="width: 80px;" ng-repeat="property in users[0].propertyList" ng-if="$index==1"
                                    ng-bind="property.name"></th>
                                <th ng-repeat="property in users[0].propertyList" ng-if="$index>1"
                                    ng-bind="property.name"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="user in users| filter:{buildName:buildName}:true">
                                <td ng-bind="$index+1"></td>
                                <td ng-repeat="property in user.propertyList" ng-bind="property.name"
                                    ng-click="showUser($index,property);"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>


<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 计划安排
        </div>
        <div class="actions">

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="newPlan();"
               ng-if="processInstance.firstTask">
                <i class="fa fa-plus"></i> 新增 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="loadPlan();">
                <i class="fa fa-refresh"></i> 刷新 </a>
        </div>
    </div>
    <div class="portlet-body">


        <div class="tabbable-line">
            <ul class="nav nav-tabs">
                <li ng-class="{true: 'active', false: ''}[$index==0]" ng-repeat="buildName in planBuildNameList">
                    <a ng-href="{{'#tb_plan_build_'+buildName}}" data-toggle="tab" aria-expanded="true">
                        <span ng-bind="buildName"></span></a>
                </li>


            </ul>
            <div class="tab-content">
                <div ng-class="{true: 'tab-pane active', false: 'tab-pane'}[$index==0]"
                     id="{{'tb_plan_build_'+buildName}}" ng-repeat="buildName in planBuildNameList">
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 15px;">#</th>
                                <th style="width: 120px;">子项名称</th>
                                <th>提资内容</th>
                                <th style="width: 90px;" class="hidden-md hidden-sm hidden">建筑面积</th>
                                <th style="width: 60%;">提资情况描述</th>
                                <th style="width: 95px;" ng-if="processInstance.firstTask">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="plan in plans| filter:{buildName:buildName}:true">
                                <td ng-bind="$index+1"></td>
                                <td ng-bind="plan.buildName"></td>
                                <td>
                                    <input type="text" class="form-control input-sm" ng-model="plan.planName"/>
                                </td>
                                <td class="hidden-md hidden-sm hidden">
                                    <input type="text" class="form-control input-sm" ng-model="plan.planArea"/>
                                </td>
                                <td>
                                    <input type="text" class="form-control input-sm" ng-model="plan.planDesc"
                                           placeholder="{{plan.planTip}}"/>
                                </td>
                                <td ng-if="processInstance.firstTask">
                                    <i class="fa fa-save" style="margin-right: 2px;" title="保存"
                                       ng-click="savePlan(plan);"></i>
                                    <i class="fa fa-arrow-up" style="margin-right: 2px;" title="上移"
                                       ng-click="sortPlan(plan.id,true);"></i>
                                    <i class="fa fa-arrow-down" style="margin-right: 2px;" title="下移"
                                       ng-click="sortPlan(plan.id,false);"></i>
                                    <i class="fa fa-trash" title="删除" ng-click="removePlan(plan.id);"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>


<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'" ng-init="fileTplTitle='业务附件'"></div>

