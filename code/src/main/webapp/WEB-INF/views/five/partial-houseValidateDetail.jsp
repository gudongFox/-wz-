<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> 设计文件审校
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/five/houseValidate/exportData.json?id='+vm.item.id}}"
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
                     style="height: 360px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label required">评审专业</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.majorName" disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.showArrangeUserModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">评审方式</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.designReviewType"
                                           disabled="disabled"/>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label">会签人员</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.otherMenName"
                                               disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.showCountersignModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.remark"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">校核人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.proofreadMenName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">审核人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.auditMenName"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">专业负责人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.majorChargeManName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">审定人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.approveMenName"
                                           disabled="disabled"/>
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
                     style="height: 360px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 360px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<div ng-include="'/web/v1/tpl/commonEdMark.html'"  ng-init="markTplTitle='意见标注'" ng-if="tplConfig.markRoleNames.length>0"></div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>


<div class="modal fade draggable-modal" id="markModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="'校审意见('+vm.mark.markCategory+')'"></h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">文件名称</label>
                            <div class="col-md-10">
                                <select ng-options="s.id as s.fileName for s in vm.files"
                                        ng-model="vm.mark.fileId" class="form-control"
                                        ng-disabled="vm.mark.creator!=user.userLogin||vm.mark.markAnswer!=''"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">意见类型</label>
                            <div class="col-md-10">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'校审意见类型'}:true"
                                        ng-model="vm.mark.markLevel" class="form-control"
                                        ng-disabled="vm.mark.creator!=user.userLogin||vm.mark.markAnswer!=''"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">图名图号</label>
                            <div class="col-md-10">

                                <input type="text" class="form-control" ng-model="vm.mark.drawNo"
                                       ng-disabled="vm.mark.creator!=user.userLogin||vm.mark.markAnswer!=''"/>
                            </div>

                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">校审意见</label>
                            <div class="col-md-10">
                                <textarea class="form-control" ng-model="vm.mark.markContent" rows="3"
                                          ng-disabled="vm.mark.creator!=user.userLogin||vm.mark.markAnswer!=''"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">设计回复</label>
                            <div class="col-md-10">
                                <textarea class="form-control" ng-model="vm.mark.markAnswer" rows="3"
                                          ng-disabled="!processInstance.firstTask"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">校审人</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.mark.creatorName"
                                       disabled="disabled"/>
                            </div>
                            <label class="col-md-2 control-label">创建时间</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control"
                                       value="{{vm.mark.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">设计人</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.mark.answerName"
                                       disabled="disabled"/>
                            </div>
                            <label class="col-md-2 control-label">回复时间</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control"
                                       value="{{vm.mark.answerTime|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveMark();"
                        ng-show="processInstance.myRunningTaskName!=''&&(processInstance.myRunningTaskName.indexOf('设计')>=0||processInstance.myRunningTaskName.indexOf(vm.mark.markCategory)>=0||vm.mark.answer==user.userLogin)">保存
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<%--发布区文件--%>
<div class="modal fade draggable-modal" id="selectCoModel">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">选择发布区文件</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>文件名称</th>
                            <th>专业</th>
                            <th>子项</th>
                            <th>文件大小</th>
                            <th style="width: 80px;">创建人</th>
                            <th style="width: 120px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="file in vm.commonCoFiles">
                            <td>
                                <input type="checkbox" ng-checked="vm.fileSourceIds.indexOf(','+file.id+',')>=0"
                                       class="cb_file" data-id="{{'coFile_'+file.id}}"
                                       data-name="{{file}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="file.fileName"></td>
                            <td ng-bind="file.majorName"></td>
                            <td ng-bind="file.buildName"></td>
                            <td ng-bind="file.commonAttach.sizeName"></td>
                            <td ng-bind="file.creator"></td>
                            <td ng-bind="file.gmtCreate|date:'yyyy-MM-dd'"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <tr style="padding-left: 15px;">
                           <span class="data_title" style="font-size: 8px;margin-top: 2px" title="全选"
                                 ng-click="vm.selectAllCoFile();">全选</span><span
                        style="margin-left: 10px;font-size: 8px;" class="data_title" title="反选"
                        ng-click="vm.toggleSelectCoFile();">反选</span>
                </tr>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectCo();">确认</button>
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
                                <input type="checkbox" ng-checked="vm.item.majorName==arrange.majorName"
                                       class="cb_arrange" data-id="{{'file_'+arrange.id}}"
                                       data-name="{{arrange}}" style="width: 15px;height: 15px;"/>
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

<div class="modal fade draggable-modal" id="countersignModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">会签人员</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height:500px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>专业</th>
                            <th>姓名</th>
                            <th>职务</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="map in vm.list">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.otherMen.indexOf(map.userLogin)>-1"
                                       class="cb_map"
                                       data-name="{{map}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="map.majorName"></td>
                            <td ng-bind="map.userName"></td>
                            <td ng-bind="map.profession"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveCountersign();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
