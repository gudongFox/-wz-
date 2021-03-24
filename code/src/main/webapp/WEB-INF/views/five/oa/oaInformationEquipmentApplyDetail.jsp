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
            <span ng-bind="tableName">信息化设备采购事项审批</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">信息化设备采购事项审批</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" ><%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>
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
                                <label class="col-md-2 control-label required">采购审批编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.formNo" name="formNo" required="true"   disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal(vm.opt='deptId');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">是否在预算内</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.plan" class="form-control"
                                            ng-disabled="!processInstance.firstTask" ></select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.linkManName" name="linkManName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('linkMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">联系电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.linkManPhone" name="linkManPhone" required="true" ng-disabled="!processInstance.firstTask"  >
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">设备用途及购置理由</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control"
                                              ng-model="vm.item.equipmentUse" name="equipmentUse"  ng-disabled="!processInstance.firstTask"   placeholder="预算外采购填报"></textarea>
                                </div>
                            </div>
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
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 购置设备信息<span style="margin-left: 30px;color: red;font-size: 12px">总计：<span ng-bind="vm.item.totalMoney+'元'"></span></span>
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
                    <th>设备名称</th>
                    <th>设备所在单位</th>
                    <th>品牌</th>
                    <th>设备类型</th>
                    <th>购置数量</th>
                    <th>单价（元）</th>
                    <th>总价（元）</th>
                    <th>特殊需求</th>
                    <th>备注</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.detalList">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.equipmentName" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.deptName"></td>
                    <td ng-bind="detail.brand"></td>
                    <td ng-bind="detail.equipmentType"></td>
                    <td ng-bind="detail.number"></td>
                    <td ng-bind="detail.price"></td>
                    <td ng-bind="detail.totalPrice"></td>
                    <td ng-bind="detail.otherRequirement"></td>
                    <td ng-bind="detail.remark"></td>
                    <td >
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);"  ng-show="processInstance.firstTask" title="删除"></i>
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
                <h4 class="modal-title">设备详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">设备名称</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.equipmentName" name="equipmentName" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label required">设备所属单位</label>
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
                        <label class="col-md-4 control-label">品牌</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.brand" name="brand" required="true" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">设备型号</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.equipmentType" name="equipmentType" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">数量</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.number" name="number" required="true" ng-change=" vm.coutTotal();">
                        </div>
                    </div>
                <%--    <div class="form-group">
                        <label class="col-md-4 control-label">入网类型</label>
                        <div class="col-md-7">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'设备网络'}:true"
                                    ng-model="vm.detail.netType" class="form-control"
                                    ></select>
                        </div>
                    </div>--%>
                    <div class="form-group">
                        <label class="col-md-4 control-label">单价（元）</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.price" name="price" required="true" ng-change=" vm.coutTotal();">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">总价（元）</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.totalPrice" name="totalPrice" disabled>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">特殊需求</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.otherRequirement" name="otherRequirement"  placeholder="例：双显卡等">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">备注</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.remark" name="remark">
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

<jsp:include page="../print/print-oaInformationEquipmentApplyDetail.jsp"/>