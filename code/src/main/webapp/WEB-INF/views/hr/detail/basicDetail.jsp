<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet box blue" ng-repeat="group in vm.groupList" ng-if="$index==0&&processInstanceId">
    <div class="portlet-title">
        <div class="caption">
            <span ng-include="'/web/v1/tpl/actPortletCaption.html'"></span>
        </div>
        <div class="actions">
            <span>
                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                    <i class="fa fa-refresh"></i> 刷新 </a>

                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();"
                   ng-show="tplConfig.saveAble">
                    <i class="fa fa-save"></i> 保存 </a>

                <span ng-include="'/web/v1/tpl/actAction.html'"></span>

                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.back();">
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

<div class="portlet box blue" ng-repeat="group in vm.groupList" ng-if="!processInstanceId||$index>0">
    <div class="portlet-title">
        <div class="caption" ng-show="$index>0">
            <i class="icon-note"></i><span ng-bind="($index+1)+'.'+group.groupName"></span>
        </div>
        <div class="caption" ng-show="$index===0">
            <i class="{{vm.template.formIcon}}"></i> <span class="caption-subject"
                                                           ng-bind="vm.template.formDesc"></span>
        </div>

        <div class="actions" ng-show="$index===0">
            <span>
                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                    <i class="fa fa-refresh"></i> 刷新 </a>

                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();"
                   ng-show="tplConfig.saveAble">
                    <i class="fa fa-save"></i> 保存 </a>

                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.back();">
                    <i class="fa fa-arrow-left"></i> 返回 </a>

            </span>
        </div>
    </div>
    <div class="portlet-body">
        <div ng-include="'/web/v1/tpl/formInfo.html'"></div>
    </div>
</div>

<div class="portlet blue box">
    <div class="portlet-title">
        <div class="caption">
            <i class=" icon-info"></i> 常用信息展示
        </div>
    </div>
    <div class="portlet-body">


        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_hrEducation" data-toggle="tab" aria-expanded="true">
                        （1） 学历信息 </a>
                </li>
                <li class="">
                    <a href="#tab_hrTechnology" data-toggle="tab" aria-expanded="false">
                        （2） 技术职务
                    </a>
                </li>
                <li class="">
                    <a href="#tab_hrQualification" data-toggle="tab" aria-expanded="false">
                        （3） 执业资格 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_hrEducation">
                    <div class="dataTables_wrapper no-footer">
                        <div class="table-scrollable">
                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 30px;">#</th>
                                    <th ng-repeat="head in vm.hrEducation.heads track by $index" ng-bind="head.text"
                                        ng-style="head.style"></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr role="row" class="odd" ng-repeat="item in vm.hrEducation.list">
                                    <td ng-bind="$index+1"></td>
                                    <td ng-repeat="property in item.propertyList  track by $index"
                                        ng-bind="property.text"
                                        ng-style="property.style"></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="tab-pane" id="tab_hrTechnology">
                    <div class="dataTables_wrapper no-footer">
                        <div class="table-scrollable">
                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 30px;">#</th>
                                    <th ng-repeat="head in vm.hrTechnology.heads track by $index" ng-bind="head.text"
                                        ng-style="head.style"></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr role="row" class="odd" ng-repeat="item in vm.hrTechnology.list">
                                    <td ng-bind="$index+1"></td>
                                    <td ng-repeat="property in item.propertyList  track by $index"
                                        ng-bind="property.text"
                                        ng-style="property.style"></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="tab-pane" id="tab_hrQualification">
                    <div class="dataTables_wrapper no-footer">
                        <div class="table-scrollable">
                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 30px;">#</th>
                                    <th ng-repeat="head in vm.hrQualification.heads track by $index" ng-bind="head.text"
                                        ng-style="head.style"></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr role="row" class="odd" ng-repeat="item in vm.hrQualification.list">
                                    <td ng-bind="$index+1"></td>
                                    <td ng-repeat="property in item.propertyList  track by $index"
                                        ng-bind="property.text"
                                        ng-style="property.style"></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>


<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'" ng-init="fileTplTitle='相关附件'"></div>
