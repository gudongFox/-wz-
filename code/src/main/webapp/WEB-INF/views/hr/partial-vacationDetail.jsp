<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" style="margin: -10px 0px 5px 0px;padding:0px;background-color: #F1F3FA;">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a>人资管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>便捷服务管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>员工休假申请</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ng-bind="vm.item.userName"></a>
        </li>
    </ul>

</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-diamond"></i>员工休假申请
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
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
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">员工姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="" ng-model="vm.item.userName" disabled
                                           />
                                </div>
                                <label class="col-md-2 control-label">自助账号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.userLogin" disabled
                                           />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">部门名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" disabled />
                                </div>
                                <label class="col-md-2 control-label">职位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.userType" disabled />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">参加工作时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.joinWorkTime" disabled />
                                </div>
                                <label class="col-md-2 control-label">加入公司时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.joinCompanyTime" disabled />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">年假总计</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.annualTotal"  name="annualTotal" disabled/>
                                </div>

                                <label class="col-md-2 control-label">集体休假</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.forceHoliday"  name="forceHoliday" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">休假说明</label>
                                <div class="col-md-10">

                                    <textarea rows="3" class="form-control" ng-model="vm.item.remark" required="true" name="remark"
                                              ng-disabled="!processInstance.firstTask" placeholder=""></textarea>
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
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>
            <div class="portlet light">
                <div class="portlet-title">
                    <div class="caption">
                        <i class="fa fa-file"></i>休假申请详情
                    </div>
                    <div class="actions" >
                        <a  class="btn btn-sm default fileinput-button"
                            ng-click="vm.addVacationDetail();" ng-show="processInstance.firstTask">
                            <i class="fa fa-plus"></i> 新增 </a>
                    </div>

                </div>
                <div class="portlet-body">
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">序号</th>
                                <th>休假类型</th>
                                <th>计划开始时间</th>
                                <th>计划结束时间</th>
                                <th>计划天数</th>
                                <th>实际开始时间</th>
                                <th>实际结束时间</th>
                                <th>上年度剩余天数</th>
                                <th style="width: 80px;" >操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="vacationDetail in vm.list">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="vacationDetail.vacationType"></td>
                                <td ng-bind="vacationDetail.planBegin"></td>
                                <td ng-bind="vacationDetail.planEnd"></td>
                                <td ng-bind="vacationDetail.planDay"></td>
                                <td ng-bind="vacationDetail.actualBegin"></td>
                                <td ng-bind="vacationDetail.actualEnd"></td>
                                <td ng-bind="vacationDetail.lastLeft"></td>
                                <td>
                                    <i class="fa fa-edit margin-right-10" ng-click="vm.editVacationDetail(vacationDetail.id);"
                                       title="详情"></i>
                                    <i class="fa fa-trash"  ng-click="vm.deleteVacationDetail(vacationDetail.id);" title="删除" ng-show="processInstance.firstTask"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../ed/project/part-actFlow.jsp"/>

<div class="modal fade" id="vacationDetail" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">休假申请详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="VacationDetail_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">休假类型</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'休假类型'}:true"
                                        ng-model="vm.vacationDetail.vacationType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">计划开始时间</label>
                            <div class="col-md-9">
                                <div class="input-group date date-picker" id="planBegin">
                                    <input type="text" class="form-control"
                                           ng-model="vm.vacationDetail.planBegin"  id="planBeginTime" name="planBegin" required="true" ng-disabled="!processInstance.firstTask"
                                    ng-change="vm.changePlanDay();">
                                    <span class="input-group-btn">
												<button class="btn default" ng-disabled="!processInstance.firstTask" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">计划结束时间</label>
                            <div class="col-md-9">
                                <div class="input-group date date-picker" id="planEnd">
                                    <input type="text" class="form-control"
                                           ng-model="vm.vacationDetail.planEnd" id="planEndTime" ng-change="vm.changePlanDay();" name="planEnd" required="true"  ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" ng-disabled="!processInstance.firstTask" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">计划天数（天）</label>
                            <div class="col-md-9">
                                <input type="number" class="form-control" ng-model="vm.vacationDetail.planDay" disabled
                                       maxlength="200" name="planDay" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">实际开始时间</label>
                            <div class="col-md-9">
                                <div class="input-group date date-picker" id="actualBegin">
                                    <input type="text" class="form-control"
                                           ng-model="vm.vacationDetail.actualBegin" name="actualBegin" ng-required="processInstance.myTaskName=='销假'" ng-disabled="processInstance.myTaskName!='销假'">
                                    <span class="input-group-btn">
                                        <button class="btn default" type="button"  ng-disabled="processInstance.myTaskName!='销假'"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">实际结束时间</label>
                            <div class="col-md-9">
                                <div class="input-group date date-picker" id="actualEnd">
                                    <input type="text" class="form-control"
                                           ng-model="vm.vacationDetail.actualEnd" name="actualEnd" ng-required="processInstance.myTaskName=='销假'" ng-disabled="processInstance.myTaskName!='销假'" >
                                    <span class="input-group-btn">
                                        <button class="btn default" type="button" ng-disabled="processInstance.myTaskName!='销假'"><i class="fa fa-calendar"></i></button>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">上年剩余天数</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.vacationDetail.lastLeft"  name="lastLeft" disabled/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateVacationDetail();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

