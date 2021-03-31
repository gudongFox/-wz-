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
            <span ng-bind="tableName">非密信息化设备使用状态变更</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">非密信息化设备使用状态变更</span>
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
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">申请人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.applyUserName" name="applyUserName"  required="true" disabled/>
                                </div>
                                <label class="col-md-2 control-label required">联系电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.applyPhone" name="applyPhone"  required="true" disabled/>
                                </div>
                            </div>
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">计算机设备编号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.computerNo" name="computerNo"  required="true" disabled/>
                                        <span class="input-group-btn" ng-disabled="!processInstance.firstTask">
                                            <button class="btn default" type="button" ng-click="vm.showComputer();"  ng-disabled="!processInstance.firstTask" ><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                    <div style="color: red;font-size:10px;">请选择计算机设备编号</div>
                                </div>
                                <label class="col-md-2 control-label required">设备名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.computerName" name="computerName" required="true" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">资产编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.assetNo" name="computerRoom" required="true" disabled/>
                                </div>
                                <label class="col-md-2 control-label required">MAC地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.macAddress" name="computerPrincipal" required="true" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">变更理由</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.reason" name="reason" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
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

<table>
    <tr>
        <td style="padding-right: 10px">
            <div class="portlet light">
                <div class="portlet-title">
                    <div class="caption">
                        <i class="fa fa-file"></i> 变更前信息
                    </div>
                </div>
                <div class="portlet-body">
                    <form class="form-horizontal" role="form" id="detail_form1">
                        <div class="form-body">
                            <span>责任人：</span>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.dutyName" name="dutyName" disabled/>
                                </div>
                                <label class="col-md-2 control-label ">职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.dutyLogin" name="dutyLogin" disabled/>
                                </div>

                                <label class="col-md-2 control-label" style="margin-top: 15px;">密级</label>
                                <div class="col-md-4"  style="margin-top: 15px;">
                                    <input type="text" class="form-control" ng-model="vm.item.dutySecurity" name="dutySecurity" disabled/>
                                </div>
                                <label class="col-md-2 control-label " style="margin-top: 15px;">所属单位</label>
                                <div class="col-md-4" style="margin-top: 15px;">
                                     <input type="text" class="form-control" ng-model="vm.item.dutyDeptName" name="dutyDeptName" disabled/>
                                </div>
                            </div>
                            <span>使用人：</span>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.usersName" name="usersName" disabled />
                                </div>
                                <label class="col-md-2 control-label ">职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.usersLogin" name="usersLogin" disabled/>
                                </div>

                                <label class="col-md-2 control-label" style="margin-top: 15px;">密级</label>
                                <div class="col-md-4"  style="margin-top: 15px;">
                                    <input type="text" class="form-control" ng-model="vm.item.usersSecurity" name="usersSecurity" disabled/>
                                </div>
                                <label class="col-md-2 control-label " style="margin-top: 15px;">所属单位</label>
                                <div class="col-md-4" style="margin-top: 15px;">
                                    <input type="text" class="form-control" ng-model="vm.item.usersDeptName" name="usersDeptName" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">设备所属单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"  disabled/>
                                </div>
                                <label class="col-md-2 control-label ">使用情况</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.useSituation" name="useSituation" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">用途</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.useWay" name="useWay" disabled/>
                                </div>
                                <label class="col-md-2 control-label ">使用类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.useType" name="useType"  disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">联网类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.network" name="network" disabled/>
                                </div>
                                <label class="col-md-2 control-label ">放置地点</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.room" name="room" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">光驱状态</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.hardDisk" name="hardDisk" disabled/>
                                </div>
                                <label class="col-md-2 control-label">USB状态</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.usb" name="usb" disabled/>
                                </div>
                            </div>



                        </div>
                    </form>
                </div>
            </div>
        </td>
        <td style="padding-left: 10px">
            <div class="portlet light">
                <div class="portlet-title">
                    <div class="caption">
                        <i class="fa fa-file"></i> 变更后信息
                    </div>
                </div>
                <div class="portlet-body">
                    <form class="form-horizontal" role="form" id="detail_form2">
                        <div class="form-body">
                            <span>责任人：</span>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">姓名</label>
                                <div class="col-md-4">

                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.newDutyName" name="newDutyName"  disabled />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('newDutyLogin');"  ng-disabled="!processInstance.firstTask" ><i class="fa fa-user" ></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label ">职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.newDutyLogin" name="newDutyLogin" disabled/>
                            </div>

                                <label class="col-md-2 control-label " style="margin-top: 15px;">密级</label>
                                <div class="col-md-4" style="margin-top: 15px;">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'项目密级'}:true"
                                            ng-model="vm.item.newDutySecurity" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label " style="margin-top: 15px;">所属单位</label>
                                <div class="col-md-4" style="margin-top: 15px;">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.newDutyDeptName" name="newDutyDeptName" disabled/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal('newDutyDeptId');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <span>使用人：</span>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">姓名</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.newUsersName" name="newUsersName"  disabled />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('newUsersLogin');"   ng-disabled="!processInstance.firstTask"><i class="fa fa-user" ></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label ">职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.newUsersLogin" name="newUsersLogin"  disabled/>
                                </div>
                                <label class="col-md-2 control-label " style="margin-top: 15px;">密级</label>
                                <div class="col-md-4" style="margin-top: 15px;">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'项目密级'}:true"
                                            ng-model="vm.item.newUsersSecurty" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label " style="margin-top: 15px;">所属单位</label>
                                <div class="col-md-4" style="margin-top: 15px;">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.newUsersDeptName" name="newUsersDeptName" disabled/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal('newUsersDeptId');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">设备所属单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.newDeptName" name="newDeptName"  ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal('newDeptId');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label ">使用情况</label>
                                <div class="col-md-4">
                                    <select ng-disabled="!processInstance.firstTask" ng-model="vm.item.newUseSituation" class="form-control">
                                        <option value="在用">在用</option>
                                        <option value="停用">停用</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">用途</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'使用方式'}:true"  ng-disabled="!processInstance.firstTask"
                                            ng-model="vm.item.newUseWay" class="form-control"></select>
                                </div>
                                <label class="col-md-2 control-label ">使用类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'使用类别'}:true" ng-disabled="!processInstance.firstTask"
                                            ng-model="vm.item.newUseType" class="form-control"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">联网类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'设备网络'}:true"
                                            ng-model="vm.item.newNetwork" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label ">放置地点</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.newRoom" name="newRoom" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">光驱状态</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.newHardDisk" name="newHardDisk" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label">USB状态</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'usb状态'}:true"
                                            ng-model="vm.item.newUsb" class="form-control" ng-disabled="!processInstance.firstTask"
                                    ></select>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </td>
    </tr>
</table>

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

<div class="modal fade" id="computerModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">设备详情</h4>
            </div>
            <form class="form-horizontal form" role="form" id="detail_form5">
                <div class="form-body">
                    <div class="form-group">
                        <label class="col-md-2 control-label required">设备编号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.computerNo" name="computerNo" required="true"     />
                        </div>
                        <label class="col-md-2 control-label required">设备所属单位</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"   disabled />
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();"    ><i class="fa fa-cog"></i></button>
                                         </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group" >
                        <label class="col-md-2 control-label required">责任人</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.item.chargeManName" name="chargeManName"  disabled />
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('chargeMan');"   ><i class="fa fa-user" ></i></button>
                                         </span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">责任人职工号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.chargeMan" name="chargeMan" required="true"     />
                        </div>
                    </div>

                    <div class="form-group" >
                        <label class="col-md-2 control-label required">使用人</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.item.useName" name="useName"  disabled />
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('useLogin');"   ><i class="fa fa-user" ></i></button>
                                         </span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">使用人职工号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.useLogin" name="useLogin" required="true" />
                        </div>
                    </div>

                    <div class="form-group" >
                        <label class="col-md-2 control-label required">设备序列号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.equipmentNo" name="equipmentNo" required="true"     />
                        </div>
                        <label class="col-md-2 control-label required">设备名称</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.computerName" name="computerName" required="true"     />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">用途</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.useWay" name="useWay" required="true"   />
                        </div>
                        <label class="col-md-2 control-label required">使用类别</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.securityLevel" name="securityLevel" required="true"   />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">联网类型</label>
                        <div class="col-md-4">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'设备网络'}:true"
                                    ng-model="vm.item.network" class="form-control"
                            ></select>
                        </div>

                        <label class="col-md-2 control-label required">初次启用时间</label>
                        <div class="col-md-4">
                            <div class="input-group date date-picker" id="applicantTime">
                                <input type="text" class="form-control"
                                       ng-model="vm.item.useTime" name="useTime" required="true"    >
                                <span class="input-group-btn">
                                               <button class="btn default" type="button"  ><i class="fa fa-calendar"></i></button>
                                        </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">品牌</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.computerBrand" name="computerBrand" required="true"   />
                        </div>
                        <label class="col-md-2 control-label required">型号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.snNo" name="snNo" required="true"   />
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label required">放置地点</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.room" name="room" required="true"     />
                        </div>
                        <label class="col-md-2 control-label required">设备类型</label>
                        <div class="col-md-4">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'信息化设备类型'}:true"
                                    ng-model="vm.item.equipmentType" class="form-control"
                            ></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">显示器类型</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.displayType" name="displayType" required="true"     />
                        </div>
                        <label class="col-md-2 control-label required">使用情况</label>
                        <div class="col-md-4">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'计算机使用情况'}:true"
                                    ng-model="vm.item.useStatus" class="form-control"
                            ></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">光驱类型</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.cdType" name="cdType" required="true"     />
                        </div>
                        <label class="col-md-2 control-label required">USB口状态</label>
                        <div class="col-md-4">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'usb状态'}:true"
                                    ng-model="vm.item.usbStatus" class="form-control"
                            ></select>
                        </div>
                    </div>


                    <%--运维部填写--%>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">硬盘序列号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.hardDiskNo" name="hardDiskNo" required="true"     />
                        </div>
                        <label class="col-md-2 control-label required">操作系统</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.operatingSystem" name="operatingSystem" required="true"     />
                        </div>


                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">操作系统安装时间</label>
                        <div class="col-md-4">
                            <div class="input-group date date-picker" id="operatingSystemTime">
                                <input type="text" class="form-control"
                                       ng-model="vm.item.operatingSystemTime" name="operatingSystemTime" required="true"    >
                                <span class="input-group-btn">
                                               <button class="btn default" type="button"  ><i class="fa fa-calendar"></i></button>
                                        </span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">MAC地址</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.macAddress" name="macAddress" required="true"     />
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-md-2 control-label required">IP地址</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.ipAddress" name="ipAddress" required="true"     />
                        </div>
                        <label class="col-md-2 control-label required">资产编号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.item.fixedAssetNo" name="fixedAssetNo" required="true"     />
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-md-2 control-label">备注</label>
                        <div class="col-md-10">
                            <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"   />
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
    </div>
</div>


<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print-oaComputerChangeDetail.jsp"/>

