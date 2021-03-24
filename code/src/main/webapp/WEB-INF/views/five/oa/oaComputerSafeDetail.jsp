<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:51})">信息化审批</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>非密信息化设备安全技术处理工作记录表</span>
            <i class="fa fa-angle-right"></i>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span >非密信息化设备安全技术处理工作记录表</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator">
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
                                <label class="col-md-2 control-label required">设备名称</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.deviceName" name="deviceName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">设备编号</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.deviceNo" name="deviceNo" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">所属单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">设备责任人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.dutyPersonName" name="dutyPersonName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                                <button class="btn default" type="button" ng-click="vm.showUserModel('dutyPersonId');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                             </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">设备密级</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'项目密级'}:true"
                                            ng-model="vm.item.deviceSecurity" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                            <label class="col-md-2 control-label required">日期</label>
                            <div class="col-md-4">
                                <div class="input-group date date-picker" id="startTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.startTime" name="startTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                    <span class="input-group-btn">
                                                   <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                            </span>
                                </div>
                            </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">处理原因</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.deviceName" name="deviceName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">操作内容</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.reason"  name="reason" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">操作人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.removelManName" name="removelManName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                                <button class="btn default" type="button" ng-click="vm.showUserModel('removelMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                             </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label ">接收人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.accompanyManName" name="accompanyManName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                                <button class="btn default" type="button" ng-click="vm.showUserModel('accompanyMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                             </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <textarea class="form-control" ng-model="vm.item.remark" name="remark" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
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
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 所拆部件清单</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <%--<a href="/assets/doc/five/五洲工程设备采购白名单.xlsx"  style="color:blue;text-align: center"  target="_blank">五洲工程设备采购白名单.xlsx</a>--%>
        </div>
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
                    <th>名称</th>
                    <th>序列号</th>
                    <th>备注</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.equipmentName" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.equipmentModel"></td>
                    <td ng-bind="detail.remark"></td>
                    <td >
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除" ng-show="processInstance.firstTask"></i>
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
                <h4 class="modal-title">所拆部件清单</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">名称</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.equipmentName" name="equipmentName"
                                       ng-disabled="!processInstance.firstTask" required="true" >
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">序列号</label>
                        <div class="col-md-7">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'信息化设备类型'}:true"
                                    ng-model="vm.detail.equipmentModel" class="form-control"
                                    ng-disabled="!processInstance.firstTask"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">备注</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.remark" name="remark"
                                   ng-disabled="!processInstance.firstTask" required="true" placeholder="请备注是否为白名单设备，如不是请另行说明原因">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();" ng-show="processInstance.firstTask">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../print/print-oaComputerMaintainDetail.jsp"/>
