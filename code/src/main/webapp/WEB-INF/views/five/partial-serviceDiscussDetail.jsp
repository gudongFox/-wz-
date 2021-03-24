<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-bell"></i> 建设。施工单位变更设计洽商单
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/five/serviceDiscuss/exportData.json?id='+vm.item.id}}"
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
                     style="min-height: 500px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label required">更改专业</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.majorName"
                                               name="majorName" required disabled="disabled"/>

                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.showSelectNeedChangeMajorModel()"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>

                                <label class="col-md-2 control-label required">洽商变更内容</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'设计变更更改原因'}:true"
                                            ng-model="vm.item.changeReason" class="form-control"
                                            ng-disabled="!processInstance.firstTask">

                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">原因补充说明</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.changeReasonDetail" placeholder="如甲方提出时间、具体原因" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">其它专业联带修改</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'更改内容已考虑更改涉及的专业'}:true"
                                            ng-model="vm.item.needChangeOther" class="form-control"
                                            ng-disabled="!processInstance.firstTask">

                                    </select>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">洽商变更内容</label>
                                <div class="col-md-10">
                                    <textarea class="form-control" ng-model="vm.item.changeContent" rows="3" required="true" name="changeContent"  ng-disabled="!processInstance.firstTask" placeholder="请详细说明,以便其他专业明白!" ></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label require">更改编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.changeNo"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label">图号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.drawNo" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">附图张数</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.attachCount"  ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-2 control-label">附图编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.attachNo"  ng-disabled="!processInstance.firstTask"/>
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
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>



<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="./print/print-serviceChangeDetail.jsp"/>
<div class="modal fade" id="selectNeedChangeMajorModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">更改专业</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="width: 70px;">专业</th>
                                <th style="width: 80px;">专业负责</th>
                                <th>设计</th>
                                <th style="width: 80px;">标准化审查</th>
                                <th style="width: 80px;">校核</th>
                                <th style="width: 80px;">审核</th>
                                <th style="width: 80px;">审定</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="user in vm.arrangeUsers" ng-if="vm.item.sourceMajor!=user.majorName&&user.majorChargeMan!=''">
                                <td>
                                    <input type="checkbox" class="cb_needChangeMajor" ng-checked="vm.item.majorName.indexOf(user.majorName)>=0"
                                           data-id="{{'file_'+user.id}}" data-name="{{user.majorName}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="user.majorName"></td>
                                <td ng-bind="user.majorChargeMenName"></td>
                                <td ng-bind="user.designMenName"></td>
                                <td ng-bind="user.criterionExamineMenName"></td>
                                <td ng-bind="user.proofreadMenName"></td>
                                <td ng-bind="user.auditMenName"></td>
                                <td ng-bind="user.approveMenName"></td>
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
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectNeedChangeMajor();">保存</button>
            </div>
        </div>
    </div>
</div>