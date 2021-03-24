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
            <span ng-bind="tableName">入协会申请</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">入协会申请</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();"  style="font-size: 14px;line-height: 1.6" <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>>
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

                            <div class="form-group" >

<%--                                <label class="col-md-2 control-label required">协会编码</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.associationNo" name="associationNo" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div--%>
                                <label class="col-md-2 control-label required">申请人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.handleManName" name="handleManName"  ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('handleMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user" ></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">申请部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group" >
                                <label class="col-md-2 control-label required">拟入协会名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.associationName" name="associationName" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">协会级别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'协会级别'}:true"
                                            ng-model="vm.item.associationLevel" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">协会主管单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptChargeName" name="deptChargeName" required="true"  ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-2 control-label required">协会类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'协会性质'}:true"
                                            ng-model="vm.item.associationType" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">推荐负责人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.recommendManName" name="recommendManName"    ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('recommendMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>

                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">公司在协会担任角色</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'协会角色'}:true"
                                            ng-model="vm.item.associationRole" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">公司联系人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.linkMan" name="linkMan" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-2 control-label required">会费(万元)</label>
                                <div class="col-md-2">
                                    <input type="text" class="form-control" ng-model="vm.item.associationFee" name="associationFee" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-1 control-label required">年限（年）</label>
                                <div class="col-md-1">
                                    <input type="text" class="form-control" ng-model="vm.item.holdRole" name="holdRole" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>

                          <%--  <div class="form-group" ng-if="vm.item.associationLevel=='一类'">
                                <label class="col-md-2 control-label required">公司领导</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.companyLeaderName" name="companyLeaderName"  ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('companyLeader');"   ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    &lt;%&ndash;<span style="color: red" ng-show="!processInstance.firstTask">注：请选择公司领导审批</span>&ndash;%&gt;
                                </div>
                            </div>--%>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">协会概况</label>
                                <div class="col-md-10">
                                    <textarea rows="6" class="form-control" ng-model="vm.item.associationSummarize" required="true" name="associationSummarize" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">参加理由</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.attendReason" required="true" name="attendReason" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
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

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../print/print-oaAssociationApplyDetail.jsp"/>
