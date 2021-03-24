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
            <span ng-bind="tableName">非密信息化设备安全准入审批</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">非密信息化设备安全准入审批</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
             <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <%----%>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();"  style="font-size: 14px;line-height: 1.6"><%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>
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
                            <%--2020-12-23HNZ --%>

                                <div class="form-group">
                                    <div class="col-md-12">
                                        <p style="color: red;font-size: 14px;text-align: center">提起入网申请须事先完成信息化设备验收，在台账中选择信息化设备编号</p>
                                    </div>
                                </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal('deptId');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">信息化设备编号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.equipmentNo" name="equipmentNo" required="true"   readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showComputer();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">责任人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.chargeManName" name="chargeManName" required="true"    readonly/>
                                </div>
                                <label class="col-md-2 control-label required">责任人职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.chargeMan" name="chargeMan" required="true"  readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">使用人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.userName" name="userName" required="true"    readonly/>
                                </div>
                                <label class="col-md-2 control-label">使用人职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.userLogin" name="userLogin"   readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                            <label class="col-md-2 control-label required">设备名称</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.equipmentName" name="equipmentName" required="true"    readonly/>
                            </div>
                            <label class="col-md-2 control-label required">设备类型</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.equipmentType" name="equipmentType" required="true"  readonly/>
                            </div>
                        </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.linkPhone" name="linkPhone" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">设备序列号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.serialNo" name="serialNo" required="true"    readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">接入网络</label>
                                <div class="col-md-4 "  >
                                    <input type="checkbox" name="network" ng-click="vm.onchange1('networkEach');" ng-model="vm.item.networkEach" ng-checked="vm.detail.networkEach=='1'"  style="width: 18px;height: 18px;text-align: center" ng-disabled="!processInstance.firstTask"/>
                                    <span >互联网</span>
                                    <input type="checkbox" name="network" ng-click="vm.onchange1('networkNoSecret');"    ng-model="vm.item.networkNoSecret" ng-checked="vm.detail.networkNoSecret=='1'"  style="width: 18px;height: 18px;text-align: center" ng-disabled="!processInstance.firstTask"/>
                                    <span>非密内网</span>
                                    <input type="checkbox" name="network" ng-click="vm.onchange1('networkMiddle');"  ng-model="vm.item.networkMiddle" ng-checked="vm.detail.networkMiddle=='1'"  style="width: 18px;height: 18px;text-align: center" ng-disabled="!processInstance.firstTask"/>
                                    <span>中间机</span>
                                    <input type="checkbox" name="network" ng-click="vm.onchange1('networkAlone');"  ng-model="vm.item.networkAlone" ng-checked="vm.detail.networkAlone=='1'"  style="width: 18px;height: 18px;text-align: center" ng-disabled="!processInstance.firstTask"/>
                                    <span>单机</span>
                                    <input type="checkbox" name="network" ng-click="vm.onchange1('networkOther');" ng-model="vm.item.networkOther" ng-checked="vm.detail.networkOther=='1'"  style="width: 18px;height: 18px;text-align: center" ng-disabled="!processInstance.firstTask"/>
                                    <span>其他</span>
                                </div>

                                <label class="col-md-2 control-label " ng-if="vm.item.networkOther">网络其他备注</label>
                                <div class="col-md-4" ng-if="vm.item.networkOther">
                                    <input type="text" class="form-control" ng-model="vm.item.networkOtherRemark" name="networkOtherRemark"     ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请开放权限</label>
                                <div class="col-md-4">
                                    <input type="checkbox"   ng-model="vm.item.modelCd" ng-checked="vm.detail.modelCd=='1'"  style="width: 18px;height: 18px;text-align: center" ng-disabled="!processInstance.firstTask"/>
                                    <span>光驱</span>
                                    <input type="checkbox"   ng-model="vm.item.modelUsb" ng-checked="vm.detail.modelUsb=='1'"  style="width: 18px;height: 18px;text-align: center" ng-disabled="!processInstance.firstTask"/>
                                    <span>USB</span>
                                    <input type="checkbox"   ng-model="vm.item.modelOther" ng-checked="vm.detail.modelOther=='1'"  style="width: 18px;height: 18px;text-align: center" ng-disabled="!processInstance.firstTask"/>
                                    <span>其他</span>
                                </div>
                                <label class="col-md-2 control-label" ng-if="vm.item.modelOther">权限其他备注</label>
                                <div class="col-md-4" ng-if="vm.item.modelOther">
                                    <input type="text" class="form-control" ng-model="vm.item.modelOtherRemark" name="modelOtherRemark" required="true"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">申请用途</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.applyReason" name="applyReason" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.remark" name="remark" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <span style="color: red">运维部填写</span>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">硬盘序列号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.hardDiskNo" name="hardDiskNo" required="true"    ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心')==-1"  />
                                </div>
                                <label class="col-md-2 control-label required">操作系统</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.operatingSystem" name="operatingSystem" required="true"   ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心')==-1"   />
                                </div>


                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">操作系统安装时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="operatingSystemTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.operatingSystemTime" name="operatingSystemTime" required="true"   ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心')==-1"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button"  ><i class="fa fa-calendar" ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心')==-1" ></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">MAC地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.macAddress" name="macAddress" required="true"   ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心')==-1"   />
                                </div>
                            </div>

                            <div class="form-group">

                                <label class="col-md-2 control-label required">IP地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.ipAddress" name="ipAddress" required="true"  ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心')==-1"    />
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
                <h4 class="modal-title margin-right-10">变更计算机</h4>
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
                            <th>资产编号</th>
                            <th>MAC地址</th>
                            <th>责任人</th>
                            <th>使用人</th>
                            <th style="width: 100px;">创建时间</th>
                            <th style="width: 60px">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                            <tr ng-repeat="computer in vm.listComputer|filter:{name:vm.qCustomer}" ng-show="computer.name!=''">
                                <td ng-bind="$index +1"></td>
                                <td ng-bind="computer.computerNo" style="color:{{computer.computerNo==vm.item.equipmentNo?'red':'blue'}}"></td>
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

<jsp:include page="../print/print-oaComputerNetworkDetail.jsp"/>

