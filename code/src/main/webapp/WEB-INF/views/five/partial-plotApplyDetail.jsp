<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-shield"></i> 出图申请单
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/five/plotApply/exportData.json?id='+vm.item.id}}"
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
                     style="height: 250px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label"><strong>申请专业</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.majorName"
                                               disabled="disabled" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.showAllMajor();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">申请时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="useTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.plotTime" name="plotTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"  ></i></button>
                                        </span>
                                    </div>
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
                     style="height: 430px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 430px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
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
                <h4 class="modal-title margin-right-10">选择申请专业</h4>
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
                        <tr ng-repeat="arrange in vm.edArrangeUsers">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.majors.indexOf(arrange.majorName)>-1" class="cb_arrange"
                                       data-id="{{'file_'+arrange.id}}"    data-name="{{arrange.majorName}}" style="width: 15px;height: 15px;"/>
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
                <button type="button" class="btn blue" ng-click="vm.saveMajors();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i>出图清单
        </div>
        <div class="actions" ng-show="processInstance.firstTask">
            <a  class="btn btn-sm default fileinput-button"
                ng-click="vm.addDetail();">
                <i class="fa fa-plus"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: 500px;overflow-y: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">#</th>
                    <th>图框类型</th>
                    <th>图号</th>
                    <th>张数</th>
                    <th>加长</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.frameSize" ng-click="vm.edit(detail.id);" style="color: blue;"></td>
                    <td ng-bind="detail.frameNo"></td>
                    <td ng-bind="detail.frameNumber"></td>
                    <td ng-bind="detail.frameLengthen"></td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.edit(detail.id);"
                           title="详情"></i>
                        <i class="fa fa-trash"  ng-click="vm.deleteDetail(detail.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="plotApplyModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">出图清单详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="PlotDetail_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">图框大小</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" ng-model="vm.detail.frameSize" required="true"
                                       maxlength="100" name="frameSize" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">图号</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" ng-model="vm.detail.frameNo" required="true"
                                       maxlength="100" name="frameNo" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">张数</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" ng-model="vm.detail.frameNumber"  required="true"
                                       maxlength="100" name="frameNumber" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">加长</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" ng-model="vm.detail.frameLengthen"  required="true"
                                       maxlength="200" name="frameLengthen" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">备注</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" ng-model="vm.detail.remark"
                                       maxlength="200" name="remark" placeholder="">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>





