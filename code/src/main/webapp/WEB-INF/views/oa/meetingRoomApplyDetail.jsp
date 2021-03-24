<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:57})">会议管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a >会议室申请</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.meetingRoomName"></span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i> 会议室申请
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
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
                     style="min-height: 300px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请会议室</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.meetingRoomName"
                                               disabled="disabled" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.listAllRoom();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">申请部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName"  name="deptName" ng-disabled="!processInstance.firstTask" disabled/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label">申请用途</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.applyReason"  name="deptName" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">开始时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date form_datetime" id="beginTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.beginTime" value="{{vm.item.beginTime|date:'yyyy-MM-dd HH:mm'}}"  required="true" disabled="disabled">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set " type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">结束时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date form_datetime" id="endTime">
                                        <input type="text" size="16" readonly="" class="form-control"
                                               ng-model="vm.item.endTime" value="{{vm.item.endTime|date:'yyyy-MM-dd HH:mm'}}"  required="true" disabled="disabled">
                                        <span class="input-group-btn">
									<button class="btn default date-set" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
									</span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">会议室详情</label>
                                <div class="col-md-10">
                                    <textarea rows="5" class="form-control ng-pristine ng-untouched ng-valid ng-empty ng-valid-maxlength ng-valid-required"
                                              ng-model="vm.item.meetingRoomInfo" name="meetingRoomInfo" ng-disabled="!processInstance.firstTask"
                                              required="true"></textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName" disabled="disabled"/>
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
                     style="height: 300px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 300px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="meetingRoomModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" >可申请会议室</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">

                    <%--<div class="col-md-12">

                        <label style="margin-right: 6px">会议室名称:
                            <input type="search" class="form-control input-inline input-sm ng-pristine ng-untouched ng-valid ng-empty" placeholder="会议室名称"
                                   ng-model="vm.params.meetingRoomName"/>
                        </label>

                    </div>--%>
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>会议室名称</th>
                                <th style="width: 120px;">会议室地址</th>
                                <th style="width: 50px;">会议室容量</th>
                                <th style="width: 50px;">会议室状态</th>
                                <th style="width: 50px;">创建人</th>
                                <th style="width: 100px;">创建时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.listAll|filter:q">
                                <td>
                                    <input type="checkbox" class="cb_meetingRoom" ng-checked="item.id==vm.item.id"  data-name="{{item}}" style="width: 20px;height: 20px;" />
                                </td>
                                <td ng-bind="item.roomName"></td>
                                <td ng-bind="item.roomAddress"></td>
                                <td ng-bind="item.roomCapacity"></td>
                                <td ng-bind="item.roomStatus"></td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <%--<my-pager data-page-info="vm.pageInfo" on-load="vm.listAllRoom()"></my-pager>--%>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectMeetingRoom();">确认</button>
            </div>
        </div>
    </div>
</div>



<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="../common/common-actFlow.jsp"/>




