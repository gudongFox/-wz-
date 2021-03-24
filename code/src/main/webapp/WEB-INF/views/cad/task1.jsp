<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i>  <span style="font-size: 18px" ng-bind="template.formDesc"></span>
            <small ng-if="!processInstance.myRunningTaskName"
                   ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName"
                   style="color: #35e0e1;"></small>
        </div>

        <div class="actions" style="float:right">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="refresh()"
               style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <span ng-include="'/web/v1/tpl/actAction.html'" ng-controller="CommonActActionController" ></span>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_basic" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_act" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_pic" data-toggle="tab" aria-expanded="false">
                        流程图片 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_basic"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div class="form-group row" style="margin-bottom: 0.5rem;">
                        <div ng-repeat="item in group.items"
                             ng-class="{true:'col-md-6',false:'col-md-12'}[item.commonFormDetail.inputSize==6]"
                             style="margin-bottom: 0.5rem;" ng-if="!item.commonFormDetail.detailHidden">
                            <label>
                                <span ng-bind="item.commonFormDetail.inputText"></span> <span style="color: red;" ng-if="item.commonFormDetail.required">*</span>
                            </label>

                            <%--                                        <input ng-if="item.commonFormDetail.inputType=='text'" type="text" class="form-control"--%>
                            <%--                                               placeholder="{{item.commonFormDetail.inputTip}}" ng-model="item.inputValue"--%>
                            <%--                                               ng-disabled="!item.commonFormDetail.editable"/>--%>
                            <input type="text" class="form-control"
                                   placeholder="{{item.commonFormDetail.inputTip}}" ng-model="item.inputValue"
                                   ng-disabled="!item.commonFormDetail.editable"/>

                        </div>
                    </div>
                </div>
                <div class="tab-pane" id="tab_act"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_pic"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div ng-if="tplConfig.markRoleNames.length>0" ng-include="'/web/v1/tpl/commonEdMark.html'"  ng-init="markTplTitle='意见标注'"></div>
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>


