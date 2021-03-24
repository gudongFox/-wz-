<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">综合办公</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>新购计算机信息一览表</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.noticeTitle"></span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> 新购计算机信息一览表
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
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
                     style="min-height: 200px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">申请人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.applicationManName" name="applicationManName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('applicantMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                        <div class="form-group">
                                <label class="col-md-2 control-label required">申请时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applicationTime">
                                        <input type="text"  class="form-control" required="true" name="applicationTime"
                                               ng-disabled="!processInstance.firstTask"
                                               ng-model="vm.item.applicationTime">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
										</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" ng-disabled="!processInstance.firstTask"/>
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
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 新购计算机信息一览表</div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0);"
               ng-show="processInstance.firstTask">
                <i class="fa fa-cog"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height:250px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th>单位</th>
                    <th>姓名</th>
                    <th>职工号</th>
                    <th>密级</th>
                    <th>电话</th>
                    <th>房号</th>
                    <th>设备类型</th>
                    <th>入网类型</th>
                    <th>特殊需求</th>
                    <th>key号</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.deptName" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.person"></td>
                    <td ng-bind="detail.personNo"></td>
                    <td ng-bind="detail.secretLevel"></td>
                    <td ng-bind="detail.phone"></td>
                    <td ng-bind="detail.roomNo"></td>
                    <td ng-bind="detail.deviceType"></td>
                    <td ng-bind="detail.netType"></td>
                    <td ng-bind="detail.command"></td>
                    <td ng-bind="detail.keyNo"></td>
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
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../../common/common-actFlow.jsp"/>

<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">新购计算机一览信息表</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">单位</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.deptName" name="deptName" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                            <label class="col-md-2 control-label">姓名</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.person" name="person" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">职工号</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.personNo" name="personNo" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                            <label class="col-md-2 control-label">密级</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.secretLevel" name="secretLevel" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">电话</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.phone" name="phone" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                            <label class="col-md-2 control-label">房号</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.roomNo" name="roomNo" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">设备类型</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.deviceType" name="deviceType" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                            <label class="col-md-2 control-label">入网类型</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.netType" name="netType" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">特殊需求</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.command" name="command" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                            <label class="col-md-2 control-label">key号</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.keyNo" name="keyNo" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">物理地址</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.physicsAddress" name="physicsAddress" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                            <label class="col-md-2 control-label">IP地址</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.ipAddress" name="ipAddress" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">硬盘序列号</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.diskNo" name="diskNo" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                            <label class="col-md-2 control-label">备注</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.detail.remark" name="remark" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
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
</div>
<div class="modal fade draggable-modal" id="deptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择所属部门</h4>
            </div>
            <div class="modal-body">
                <div id="dept_select_tree" style="max-height:500px;overflow-y: auto;overflow-x: hidden;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDept();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


