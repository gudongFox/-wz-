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
            <span ng-bind="tableName">车辆维护</span>
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
            <i class="icon-envelope-letter"></i> <span ng-bind="tableName">车辆维护</span>
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();">
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
                                        <input type="text" class="form-control" ng-model="vm.item.carName"
                                               disabled="disabled" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.listAllCar();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label  class="col-md-2 control-label">维护类型</label>
                                <div class="col-md-4">
                                 <%--   <div class="input-group">
                                        &lt;%&ndash;<input type="text" class="form-control" ng-model="vm.item.type" name="type"  readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showTypeModel();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog" ></i></button>
                                         </span>&ndash;%&gt;

                                    </div>--%>
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'车辆维护类型'}:true"
                                            ng-model="vm.item.type" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>

                            <div class="form-group" ng-show="vm.item.type.indexOf('油卡')>=0">
                                <label class="col-md-2 control-label required">加油/充卡时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="soilTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.soilTime" name="soilTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                           </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">加油/充卡费用</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.soilMoney" name="soilMoney"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group" ng-show="vm.item.type.indexOf('保养/维护')>=0">
                                <label class="col-md-2 control-label required">保养里程</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.upkeepCourse" name="upkeepCourse"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">保养时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="upkeepTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.upkeepTime" name="upkeepTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                               </div>
                            </div>
                            <div class="form-group" ng-show="vm.item.type.indexOf('保养/维护')>=0">
                                <label class="col-md-2 control-label required">修理厂</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.upkeepFactory" name="upkeepFactory"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">保养费用</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.upkeepMoney" name="upkeepMoney"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group" ng-show="vm.item.type.indexOf('保养/维护')>=0">
                                <label class="col-md-2 control-label required">发票号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.upkeepInvoiceNo" name="upkeepInvoiceNo"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">发票费用</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.upkeepInvoiceMoney" name="upkeepInvoiceMoney"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group" ng-show="vm.item.type.indexOf('年检')>=0">
                                <label class="col-md-2 control-label required">年检时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="checkTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.checkTime" name="checkTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                 </div>
                                <label class="col-md-2 control-label required">年检费用</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.checkMoney" name="checkMoney"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group" ng-show="vm.item.type.indexOf('年检')>=0">
                                <label class="col-md-2 control-label required">年检地址</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.checkAddress" name="checkAddress"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group" ng-show="vm.item.type.indexOf('ETC')>=0">
                                <label class="col-md-2 control-label required">ETC充值</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.etcMoney" name="etcMoney"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">ETC充值时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="etcTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.etcTime" name="etcTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group" ng-show="vm.item.type.indexOf('其他')>=0">
                                <label class="col-md-2 control-label required">其他事项</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.otherType" name="otherType"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">其他费用</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.otherMoney" name="otherMoney"  required="true" ng-disabled="!processInstance.firstTask" />
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
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="typeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择维护类型</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="type in vm.types">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_type"
                           ng-checked="vm.item.type.indexOf(type.name)>=0"
                           data-name="{{type.name}}" data-id="{{'file_'+id}}" /> <span ng-bind="type.name"
                                                                                       class="margin-right-10"
                                                                                       style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%5==0"/>
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
<div class="modal fade" id="carModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" >选择车辆</h4>
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
                                <th>车牌号</th>
                                <th style="width: 120px;">汽车类型</th>
                                <th style="width: 50px;">汽车品牌</th>
                                <th style="width: 50px;">车辆状态</th>
                                <th style="width: 50px;">创建人</th>
                                <th style="width: 100px;">创建时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.listAll|filter:q">
                                <td>
                                    <input type="checkbox" class="cb_car" ng-checked="item.id==vm.item.carId"  data-name="{{item}}" style="width: 20px;height: 20px;" />
                                </td>
                                <td ng-bind="item.carNo"></td>
                                <td ng-bind="item.carType"></td>
                                <td ng-bind="item.carBrand"></td>
                                <td ng-bind="item.carStatus"></td>
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
                <button type="button" class="btn blue" ng-click="vm.selectCar();">确认</button>
            </div>
        </div>
    </div>
</div>



<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../print/print.jsp"/>




