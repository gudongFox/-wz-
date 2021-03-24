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
            <span ng-bind="tableName">技术培训申报</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">技术培训申报</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6">
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
                                <label class="col-md-2 control-label required">培训名称</label>

                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.trainName" name="trainName"  disabled/>
                                        <span class="input-group-btn" ng-disabled="!processInstance.firstTask">
                                            <button class="btn default" type="button" ng-click="vm.showAssociation();"  ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">培训类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'培训类型'}:true"
                                            ng-model="vm.item.declareTime" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.applyDeptName" name="applyDeptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal(vm.opt='apply');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">培训单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.trainDeptName" name="trainDeptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal(vm.opt='train');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">相关通知</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.otherNotic" name="otherNotic"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">拟派人员</label>
                                <div class="col-md-10">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.attendManName" name="attendManName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('attendMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">选派理由</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.attendReason" required="true" name="attendReason" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                           <%-- <div class="form-group">
                                <label class="col-md-2 control-label ">申报单位意见</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.deptComment"  name="deptComment" placeholder="" ng-disabled="user.userLogin.indexOf(processInstance.assignee)<0"></textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">技术质量部意见</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.technologyComment"  name="technologyComment" placeholder="" ng-disabled="user.userLogin.indexOf(processInstance.assignee)<0"></textarea>
                                </div>
                            </div>--%>

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
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade draggable-modal" id="selectAssociationModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">培训名称</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qCustomer"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>培训名称</th>
                            <th>组织单位</th>
                            <th>出国培训通知</th>
                            <th>培训时间</th>
                            <th style="width: 100px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="association in vm.listAssociation|filter:{name:vm.qCustomer}" ng-show="association.trainName!=''">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.trainName==association.trainName" class="cb_association"
                                       data-name="{{association.id}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="association.trainName"></td>
                            <td ng-bind="association.deptName"></td>
                            <td ng-bind="association.goAbroadTitle"></td>
                            <td ng-bind="association.applyTime"></td>
                            <td ng-bind="association.gmtModified|date:'yyyy-MM-dd'"></td>
                        </tr>
                        </tbody>
                    </table>

                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveAssociation();">确认</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../print/print-oaGoAbroadTrainDeclareDetail.jsp"/>