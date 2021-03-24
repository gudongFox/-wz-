<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:86})">资产管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">涉密计算机及外设设备报废审批</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">涉密计算机及外设设备报废审批</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();"  style="font-size: 14px;line-height: 1.6" >
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
                                <label class="col-md-2 control-label required">申请人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.applicantName" name="applicantName"  ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('applicant');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user" ></i></button>
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
                            <div class="form-group">
                                <label class="col-md-2 control-label ">申请理由</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.applicantReason" name="applicantReason" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">计算机所设备检定意见</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.authenticateResult" name="authenticateResult"  ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心-维修')==-1"  />
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">设备处理过程意见</label>
                                <div class="col-md-10">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.handleResult" name="handleResult"  ng-disabled="!processInstance.firstTask"  readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showTypeModel1();" ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心(维修)')==-1"><i class="fa fa-cog" ></i></button>
                                         </span>
                                    </div>
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
            <i class="fa fa-file"></i>设备信息</div>
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
                    <th>设备类型</th>
                    <th>资产编号</th>
                    <th>品牌型号</th>
                   <%-- <th>密级</th>--%>
                    <th>责任人</th>
                  <%--  <th>硬盘序列号</th>--%>
                    <th>启用时间</th>
                    <th>原值</th>
                    <th>折旧年限</th>
                    <th>已提折旧</th>
                    <th>净值</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.deviceType" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.deviceNo"></td>
                    <td ng-bind="detail.deviceModel"></td>
                  <%--  <td ng-bind="detail.secretLevel"></td>--%>
                    <td ng-bind="detail.dutyPerson"></td>
                   <%-- <td ng-bind="detail.hardDiskNo"></td>--%>
                    <td ng-bind="detail.startTime"></td>
                    <td ng-bind="detail.originalValue"></td>
                    <td ng-bind="detail.depreciableLife"></td>
                    <td ng-bind="detail.submitted"></td>
                    <td ng-bind="detail.netWorth"></td>
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
<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">设备信息</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">设备类型</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.deviceType" name="deviceType" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">资产编号</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.deviceNo" name="deviceNo" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">品牌型号</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.deviceModel" name="deviceModel" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <%--<div class="form-group">
                        <label class="col-md-4 control-label">密级</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.secretLevel" name="secretLevel" required="true">
                        </div>
                    </div>--%>

                    <div class="form-group">
                        <label class="col-md-4 control-label">责任人</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.dutyPerson" name="dutyPerson" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <%--<div class="form-group">
                        <label class="col-md-4 control-label">硬盘序列号</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.hardDiskNo" name="hardDiskNo" required="true">
                        </div>
                    </div>--%>
                    <div class="form-group">
                        <label class="col-md-4 control-label">启用时间</label>
                        <div class="col-md-7">
                            <div class="input-group date date-picker" id="startTime">
                                <input type="text" class="form-control"
                                       ng-model="vm.detail.startTime" name="startTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                <span class="input-group-btn">
                                               <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">原值</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.originalValue" name="originalValue" required="true"   ng-disabled="processInstance.myRunningTaskName.indexOf('财务金融部(固定资产岗)')==-1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">折旧年限</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.depreciableLife" name="depreciableLife" required="true" ng-disabled="processInstance.myRunningTaskName.indexOf('财务金融部(固定资产岗)')==-1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">已提折旧</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.submitted" name="submitted" required="true" ng-disabled="processInstance.myRunningTaskName.indexOf('财务金融部(固定资产岗)')==-1">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">净值</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.netWorth" name="netWorth" required="true" ng-disabled="processInstance.myRunningTaskName.indexOf('财务金融部(固定资产岗)')==-1">
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
<div class="modal fade" id="typeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">计算机所设备检定意见</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="type in vm.types">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_type"
                           ng-checked="vm.item.type.indexOf(type.name)>=0"
                           data-name="{{type.name}}" data-id="{{'file_'+id}}" /> <span ng-bind="type.name"
                                                                                       class="margin-right-10"
                                                                                       style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%4==0"/>
               </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveType();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade" id="typeModal1" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">设备处置过程意见</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="type in vm.types">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_type"
                           ng-checked="vm.item.type.indexOf(type.name)>=0"
                           data-name="{{type.name}}" data-id="{{'file_'+id}}" /> <span ng-bind="type.name"
                                                                                       class="margin-right-10"
                                                                                       style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%4==0"/>
               </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveType1();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<jsp:include page="../print/print.jsp"/>