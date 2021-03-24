<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:43})">协会学会审批</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">技术培训审批</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">技术培训审批</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%> >
                <i class="fa fa-print"></i> 打印 </a>
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
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">

                            <div class="form-group">
                                <label class="col-md-2 control-label required">培训名称 </label>

                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.trainName" name="trainName" required="true" ng-disabled="!processInstance.firstTask"  >
                                </div>
                                <label class="col-md-2 control-label required">组织单位</label>

                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.deptName" name="deptName" required="true" ng-disabled="!processInstance.firstTask"  >
                                </div>
                            </div>
                            <div class="form-group">
<%--                                <label class="col-md-2 control-label required">通知名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.askTitle" name="askTitle" required="true" ng-disabled="!processInstance.firstTask"  >
                                </div>--%>
                                <label class="col-md-2 control-label required">出国培训通知</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.goAbroadTitle" name="goAbroadTitle" required="true" ng-disabled="!processInstance.firstTask"  >
                                </div>
                                <label class="col-md-2 control-label required">拟培训时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applyTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.applyTime" name="applyTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">

                                <label class="col-md-2 control-label required">拟派人员数量</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.staffName" name="staffName" required="true" ng-disabled="!processInstance.firstTask"  >
                                </div>

                                <label class="col-md-2 control-label required">人均费用</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.staffCost" name="staffCost" required="true" ng-disabled="!processInstance.firstTask"  >
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">培训地点</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.staffPlace" name="staffPlace" required="true" ng-disabled="!processInstance.firstTask"  >
                                </div>
                            </div>

                         <%--   <div class="form-group">
                                <label class="col-md-2 control-label ">信息化建设与管理部意见</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control"
                                              ng-model="vm.item.technologyDeptComment" name="technologyDeptComment"  ng-disabled="user.userLogin.indexOf(processInstance.assignee)<0"  />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">专家委意见</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control"
                                              ng-model="vm.item.specialistComment" name="specialistComment"  ng-disabled="user.userLogin.indexOf(processInstance.assignee)<0"  />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">公司总工程师意见</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control"
                                              ng-model="vm.item.totalEngineerComment" name="totalEngineerComment"  ng-disabled="user.userLogin.indexOf(processInstance.assignee)<0"  />
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.remark" name="remark"  ng-disabled="!processInstance.firstTask"  >
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
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>
<%--<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 拟派人员名单 </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0);"
               ng-show="processInstance.firstTask">
                <i class="fa fa-cog"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th>培训名称</th>
                    <th>参加单位名称</th>
                    <th>参加人员</th>
                    <th>备注</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.trainName" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.deptName"></td>
                    <td ng-bind="detail.attendUserName"></td>
                    <td ng-bind="detail.remark"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">拟派人员名单</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">培训名称</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.trainName" name="trainName" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label required">参加部门</label>
                        <div class="col-md-7">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.detail.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal(vm.opt='detailDeptId');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                    <label class="col-md-4 control-label required">参加人员</label>
                    <div class="col-md-7">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.detail.attendUserName" name="attendUserName"  required="true" ng-disabled="!processInstance.firstTask" />
                            <span class="input-group-btn" >
                                <button class="btn default" type="button" ng-click="vm.showUserModel('attendUser');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                            </span>
                        </div>
                    </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">备注</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.remark" name="remark" required="true">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>--%>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../print/print-oaGoAbroadTrainAskDetail.jsp"/>
