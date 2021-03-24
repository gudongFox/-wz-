<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-doc"></i> 公司及设计评审申报表
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();"
               ng-show="processInstance.passAble">
                <i class="fa fa-save"></i> 保存 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.passAble"
               ng-click="vm.showSendFlow();">
                <i class="fa fa-send"></i> 发送 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.backAble"
               ng-click="showBackFlow();">
                <i class="fa fa-reply"></i> 打回 </a>

            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_detail_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="height: 520px;overflow-y: auto;overflow-x: hidden;">
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
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo"
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
                                <label class="col-md-2 control-label">项目总师</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.totalDesigner"  disabled/>
                                </div>
                                <label class="col-md-2 control-label required">联系电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.totalDesignerTel" required ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" required ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">申请日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applyTime">
                                        <input type="text" class="form-control" required
                                               ng-model="vm.item.applyTime" name="applyTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar" ></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目性质</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectType" name="projectType" required ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">项目规模</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.designScale" name="designScale" required ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label required">涉及专业</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.referSpecialty"
                                               name="referSpecialty" required readonly/>

                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.showSelectReferSpecialty()"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="!processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">希望评审时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="planReviewTime">
                                        <input type="text" class="form-control" required
                                               ng-model="vm.item.planReviewTime" name="planReviewTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar" ></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目概况</label>
                                <div class="col-md-10">
                                    <textarea  class="form-control" required="true" ng-model="vm.item.projectDesc" name="projectDesc" required ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">评审内容要点<br>或申请理由</label>
                                <div class="col-md-10">
                                    <textarea  class="form-control" ng-model="vm.item.reviewContent" name="reviewContent" required="true" ng-disabled="!processInstance.firstTask"/>
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
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 350px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_detail_3" style="height: 350px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>
<jsp:include page="../common/common-edFile.jsp"/>

<div class="modal fade draggable-modal" id="selectReferSpecialtyModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">涉及专业</h4>
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
                            <tr ng-repeat="user in vm.users" ng-if="vm.item.sourceMajor!=user.majorName&&user.majorChargeMan!=''">
                                <td>
                                    <input type="checkbox" class="cb_referSpecialty" ng-checked="vm.item.referSpecialty.indexOf(user.majorName)>=0"  data-name="{{user.majorName}}" data-id="{{'file_'+user.id}}" style="width: 15px;height: 15px;"/>
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
                <button type="button" class="btn blue" ng-click="vm.selectReferSpecialty();">保存</button>
            </div>
        </div>
    </div>
</div>



