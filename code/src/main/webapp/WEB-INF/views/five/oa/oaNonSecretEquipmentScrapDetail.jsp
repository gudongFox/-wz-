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
            <span ng-bind="tableName">非密计算机及外设报废申请</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">非密计算机及外设报废申请</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" >
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
                                <label class="col-md-2 control-label required">设备所属单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">部门负责人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptChargeMenName" name="deptChargeMenName" required="true"   ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('deptChargeMen');" ng-disabled="!processInstance.firstTask" ><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">信息化设备编号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.equipmentNo" name="equipmentNo"  required="true" disabled/>
                                        <span class="input-group-btn" ng-disabled="!processInstance.firstTask">
                                            <button class="btn default" type="button" ng-click="vm.showComputer();"  ng-disabled="!processInstance.firstTask" ><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                    <div style="color: red;font-size:10px;" ng-if="!vm.item.equipmentNo">请选择计算机设备编号</div>
                                </div>
                                <label class="col-md-2 control-label required">设备序列号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.equipmentSerial" name="equipmentSerial" required="true" disabled />
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">硬盘序列号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.hardNo" name="hardNo" disabled
                                           required="true"/>
                                </div>
                                <label class="col-md-2 control-label required">设备名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.equipmentName"
                                           name="equipmentName" required="true" disabled/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.applyManName" name="applyManName" required="true" disabled  />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('applyMan');" ng-disabled="!processInstance.firstTask" ><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label required">开始时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" disabled
                                           ng-model="vm.item.startTime" name="startTime" required="true"  >
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">资产编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.assetsNo"
                                           name="equipmentName" required="true" disabled/>
                                </div>
                                <label class="col-md-2 control-label required">设备类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.equipmentType"
                                           name="equipmentType" required="true" disabled/>
                                </div>
                            </div>
                            <div  class="form-group">
                                <label class="col-md-2 control-label required">是否固定资产</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.disposeAsset" name="disposeAsset" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                    <span style="color: red">请选择是否为固定资产</span>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">报废原因</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control" ng-model="vm.item.scrapReason" name="scrapReason" required="true"   ng-disabled="!processInstance.firstTask" />
                                </div>

                            </div>
                            <p style="color: red">网络运维中心-安全</p>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">安全处理记录 </label>
                                <div class="col-md-4">
                                    <input type="checkbox"   ng-model="vm.item.secretHard" ng-checked="vm.detail.secretHard=='1'"  style="width: 18px;height: 18px;" ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心-安全')==-1"/>
                                    <span>硬盘</span>
                                </div>
                                <label class="col-md-2 control-label ">安全处理-硬盘序列号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.secretHardNo" name="secretHardNo"   ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心-安全')==-1" />
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">安全处理记录 </label>
                                <div class="col-md-4">
                                    <input type="checkbox"   ng-model="vm.item.secretMemory" ng-checked="vm.detail.secretMemory=='1'"  style="width: 18px;height: 18px;" ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心-安全')==-1"/>
                                    <span>内存</span>
                                </div>
                                <label class="col-md-2 control-label ">安全处理-内存序列号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.secretMemoryNo" name="secretMemoryNo"  ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心-安全')==-1" />
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">安全处理记录 </label>
                                <div class="col-md-4">
                                    <input type="checkbox"   ng-model="vm.item.secretOther" ng-checked="vm.detail.secretOther=='1'"  style="width: 18px;height: 18px;" ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心-安全')==-1"/>
                                    <span>其他</span>
                                </div>
                                <label class="col-md-2 control-label ">安全处理-其他序列号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.secretOtherNo" name="secretOtherNo"    ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心-安全')==-1" />
                                </div>
                            </div>
                            <p  ng-show="vm.item.disposeAsset" style="color: red">财务金融部-固定资产岗</p>
                            <div ng-show="vm.item.disposeAsset" class="form-group">
                                <label class="col-md-2 control-label ">原值</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.originalValue"
                                           name="originalValue"
                                           ng-disabled="processInstance.myRunningTaskName.indexOf('财务金融部（固定资产岗）')==-1"/>
                                </div>
                                <label class="col-md-2 control-label ">折旧年限</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.depreciationYear"
                                           name="depreciationYear"
                                           ng-disabled="processInstance.myRunningTaskName.indexOf('财务金融部（固定资产岗）')==-1"/>
                                </div>

                            </div>
                            <div ng-show="vm.item.disposeAsset" class="form-group">
                                <label class="col-md-2 control-label ">已提折旧</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.depreciationAlready" name="depreciationAlready"  ng-disabled="processInstance.myRunningTaskName.indexOf('财务金融部（固定资产岗）')==-1" />
                                </div>
                                <label class="col-md-2 control-label ">净值</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.netWorth" name="netWorth"  ng-disabled="processInstance.myRunningTaskName.indexOf('财务金融部（固定资产岗）')==-1" />
                                </div>

                            </div>
                            <div ng-show="vm.item.disposeAsset" class="form-group">
                                <label class="col-md-2 control-label ">台账是否处理</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.originalValue" class="form-control" required="true" ng-disabled="processInstance.myRunningTaskName.indexOf('财务金融部（固定资产岗）')==-1"
                                            ng-disabled="false"></select>
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

<div class="modal fade draggable-modal" id="selectComputerModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">非密信息化设备</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-4" style="margin-left: 5px;">
                        <input type="search" class="form-control input-inline input-sm" placeholder="编号" ng-model="vm.qCustomer"></label>
                    </div>
                </div>
                <div class="table-scrollable" style="height: {{contentHeight/2}}px;overflow-y: auto;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer" >
                        <thead>
                        <tr>
                            <th style="width: 35px;">序号</th>
                            <th>计算机设备编号</th>
                            <th>设备名称</th>
                            <th>固定资产编号</th>
                            <th>MAC地址</th>
                            <th>责任人</th>
                            <th>使用人</th>
                            <th style="width: 100px;">创建时间</th>
                            <th style="width: 60px">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="computer in vm.listComputer|filter:{computerNo:vm.qCustomer}" >
                            <td ng-bind="$index +1"></td>
                            <td ng-bind="computer.computerNo" style="color:{{computer.computerNo==vm.item.computerNo?'red':'blue'}}"></td>
                            <td ng-bind="computer.computerName"></td>
                            <td ng-bind="computer.fixedAssetNo"></td>
                            <td ng-bind="computer.macAddress"></td>
                            <td ng-bind="computer.chargeManName"></td>
                            <td ng-bind="computer.useName"></td>
                            <td ng-bind="computer.gmtModified|date:'yyyy-MM-dd'"></td>
                            <td >
                                <button type="button" class="btn blue btn-sm" ng-click="vm.saveComputer(computer);">确认</button>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>

    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print-oaNonSecretEquipmentScrapDetail.jsp"/>

