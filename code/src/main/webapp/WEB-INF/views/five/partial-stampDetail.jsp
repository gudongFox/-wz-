<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-shield"></i> 设计成果用印申请表
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/five/stamp/exportData.json?id='+vm.item.id}}"
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
                     style="height: 430px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label"><strong>用印专业</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.majors"
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
                                <label class="col-md-2 control-label">用印所室、部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName"  name="deptName" readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>用印成果类别</strong></label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('五洲用印文件类型')}:true"
                                            ng-model="vm.item.docType" class="form-control" ng-disabled="!processInstance.firstTask"
                                           ></select>
                                </div>
                                <label class="col-md-2 control-label required">用印时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="useTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.useTime" name="useTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"  ></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>用印明细</strong></label>
                                <div class="col-md-10">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.useStamp"
                                               disabled="disabled" required="true"/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.showStampModel();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">用印事由</label>
                                <div class="col-md-10">
                                    <textarea class="form-control" ng-model="vm.item.useDescription" rows="3" required="true" name="useDescription"  ng-disabled="!processInstance.firstTask" placeholder="请详细说明,用印事由!" ></textarea>
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

<div class="modal fade" id="selectStampModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">用印明细</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>用印章</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="useStamp in vm.useStamps">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.useStamp.indexOf(useStamp.code)>=0" class="cb_stamp" data-name="{{useStamp.code}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="useStamp.code"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectStamp();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="plotApplyModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" >出图申请记录</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.qName"/>
                        </div>
                    </div>
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>评审专业</th>
                                <th>文件数量</th>
                                <th style="width: 60px;">创建人</th>
                                <th style="width: 100px;">创建时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="plotApply in vm.list|filter:{majorName:vm.qName}">
                                <td>
                                    <input type="checkbox" class="cb_plot" ng-checked="plotApply.selected"  data-name="{{plotApply}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="plotApply.majorName"></td>
                                <td ng-bind="plotApply.fileCount"></td>
                                <td ng-bind="plotApply.creatorName"></td>
                                <td ng-bind="plotApply.gmtCreate|date:'yyyy-MM-dd'"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.savePlotApply();">保存</button>
            </div>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>






