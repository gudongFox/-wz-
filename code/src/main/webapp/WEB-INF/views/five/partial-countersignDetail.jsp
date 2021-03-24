<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-loop"></i> 各专业协作表
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
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
                     style="min-height: 220px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">工程名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="项目名称"
                                           ng-model="vm.item.projectName" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="阶段"
                                           ng-model="vm.item.stageName" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">提出专业</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.sourceMajor"
                                           name="destMajor" required="true" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label"><strong>接收专业</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.destMajor"
                                               name="destMajor" required="true" disabled="disabled"/>

                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.showSelectDestMajor()"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">接受专业负责人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.destReceiverName"
                                           name="destReceiverName" required="true"/>
                                </div>
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.remark"
                                           name="remark"  />
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
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_detail_3" style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>
<jsp:include page="../common/common-edFile.jsp"/>

<div class="modal fade" id="selectDestMajorModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">接收专业</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="width: 70px;">专业</th>
                                <th style="width: 80px;">审定</th>
                                <th style="width: 80px;">审核</th>
                                <th style="width: 80px;">专业负责</th>
                                <th style="width: 80px;">校对</th>
                                <th>设计</th>
                                <th style="width: 80px;">标准审查</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="user in vm.users" ng-if="vm.item.sourceMajor!=user.majorName&&user.majorChargeMan!=''">
                                <td>
                                    <input type="checkbox" class="cb_destMajor" ng-checked="vm.item.destMajor.indexOf(user.majorName)>=0"  data-name="{{user}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="user.majorName"></td>
                                <td>
                                    <span ng-bind="user.approveMenName"></span>
                                </td>
                                <td>
                                    <span ng-bind="user.auditMenName"></span>
                                </td>
                                <td>
                                    <span ng-bind="user.majorChargeMenName"></span>
                                </td>
                                <td>
                                    <span ng-bind="user.proofreadMenName"></span>
                                </td>
                                <td>
                                    <span ng-bind="user.designMenName"></span>
                                </td>
                                <td>
                                    <span ng-bind="user.criterionExamineMenName"></span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectDestMajor();">保存</button>
            </div>
        </div>
    </div>
</div>



