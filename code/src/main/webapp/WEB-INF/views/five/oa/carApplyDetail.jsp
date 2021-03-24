<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:58})">用车管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ng-if="vm.params.showTitle">个人用车申请</a>
            <a ng-if="!vm.params.showTitle">领导用车申请</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.carName"></span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i> <span ng-if="vm.params.showTitle">个人用车申请</span><span ng-if="!vm.params.showTitle">领导用车申请</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator" style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-print"></i> 打印 </a>
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
                                <label class="col-md-2 control-label required">申请车辆</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.carName" name="carName" readonly/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.listAllCar(vm.item.id);"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">申请部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">开始时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date form_datetime" id="beginTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.beginTime"  value="{{vm.item.beginTime|date:'yyyy-MM-dd HH:mm'}}"  required="true" disabled="disabled">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">结束时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date form_datetime" id="endTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.endTime"   value="{{vm.item.endTime|date:'yyyy-MM-dd HH:mm'}}" required="true"  disabled="disabled">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">

                                <label class="col-md-2 control-label required">用车类别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('用车类别')}:true"
                                            ng-model="vm.item.applyType" class="form-control" ng-disabled="!processInstance.firstTask"
                                    ></select>
                                </div>
                                <label class="col-md-2 control-label required">用车事项</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('用车事项')}:true"
                                            ng-model="vm.item.applyReason" class="form-control"ng-disabled="!processInstance.firstTask"
                                    ></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否自驾</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('是否')}:true"
                                            ng-model="vm.item.selfDrive" class="form-control" ng-disabled="!processInstance.firstTask"
                                    ></select>
                                </div>
                                <label class="col-md-2 control-label required" style="font-weight: bold;" ng-if="!vm.item.selfDrive">司机</label>
                                <div class="col-md-4" ng-if="!vm.item.selfDrive">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.driverName" name="driverName"   readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('driver');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">用车事由</label>
                                <div class="col-md-10">
                                    <textarea rows="5" class="form-control ng-pristine ng-untouched ng-valid ng-empty ng-valid-maxlength ng-valid-required"
                                              ng-model="vm.item.carInfo" name="carInfo" ng-disabled="!processInstance.firstTask"
                                              required="true"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">用车目的地</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.destination" name="destination" ng-disabled="!processInstance.firstTask">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">乘客</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.passenger" name="passenger" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label">乘坐人数</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.userNum" name="userNum" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <hr><span style="color: red;margin-left: 40px">司机回执单</span>
                            <div class="form-group">
                                <label class="col-md-2 control-label">里程数</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.mileage" name="mileage" ng-disabled="processInstance.myRunningTaskName.indexOf('司机')==-1"/>
                                </div>
                                <label class="col-md-2 control-label">油费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.soilPay" name="soilPay" ng-disabled="processInstance.myRunningTaskName.indexOf('司机')==-1"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">过路/桥费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.passPay" name="passPay" ng-disabled="processInstance.myRunningTaskName.indexOf('司机')==-1"/>
                                </div>
                                <label class="col-md-2 control-label">停车费</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.partPay" name="partPay" ng-disabled="processInstance.myRunningTaskName.indexOf('司机')==-1"/>
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
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 300px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="carModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" >车辆使用详情</h4>
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
                                <th style="width: 120px;">车牌号</th>
                                <th style="width: 120px;">汽车类型</th>
                                <th style="width: 200px;">车辆所在单位</th>
                                <th style="width: 80px;">车辆状态</th>
                                <th >使用情况</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.listAll|filter:q">
                                <td>
                                    <input type="checkbox" class="cb_car" ng-checked="item.id==vm.item.carId"  data-name="{{item}}" style="width: 20px;height: 20px;" />
                                </td>
                                <td ng-bind="item.carNo"></td>
                                <td ng-bind="item.carType"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.carStatus" style="color: #0bb20c" ng-if="item.carStatus=='正常'"></td>
                                <td ng-bind="item.carStatus" style="color: red" ng-if="item.carStatus=='使用中'"></td>
                                <td ng-bind="item.carStatus" style="color: red" ng-if="item.carStatus=='维修中'"></td>
                                <td ng-bind-html="item.remark"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <%--<my-pager data-page-info="vm.pageInfo" on-load="vm.listAllRoom()"></my-pager>--%>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectCar();">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade draggable-modal" id="deptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择申请部门</h4>
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

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print-oaCarApplyDetail.jsp"/>


