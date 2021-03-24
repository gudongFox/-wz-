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
            <span ng-bind="tableName"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">非密信息化设备维修审批</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>>
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
                                <label class="col-md-2 control-label required">所属单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   disabled/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" disabled><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">责任人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.maintainManName" name="maintainManName"  required="true" disabled />
                                        <span class="input-group-btn" >
                                                <button class="btn default" type="button" ng-click="vm.showUserModel('maintainMan');" disabled><i class="fa fa-user"></i></button>
                                             </span>
                                </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">设备编号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control ng-pristine ng-untouched ng-empty ng-invalid ng-invalid-required" disabled ng-model="vm.item.deviceNo" name="deviceNo" required="true" disabled>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showComputer();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showComputerDetail(vm.item.deviceNo);" ng-disabled="vm.item.deviceNo==null" ><i class="fa fa-star"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">设备名称</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.deviceName" name="deviceName" required="true"  readonly/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">故障描述</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.errorDescription"  name="errorDescription" required="true" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">放置地点</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.chargeComment" name="chargeComment" required="true"   disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请人电话</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.chargeName" name="chargeName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">信息安全防范措施</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'信息安全防范措施'}:true"
                                            ng-model="vm.item.deviceType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.deviceType=='其他'">
                                <label class="col-md-2 control-label">信息安全防范措施-其他描述</label>
                                <div class="col-md-10">
                                    <input  type="text" class="form-control" ng-model="vm.item.otherContent" name="otherContent"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div  class="form-group">
                                <span style="color: red">网络运维中心-维修人员</span>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">维修建议</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.maintainAdvice"  name="maintainAdvice" required="true" placeholder="" ng-disabled="processInstance.myRunningTaskName.indexOf('维修意见')==-1"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">维修单位名称</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.chargeMan" name="chargeMan" required="true"   ng-disabled="processInstance.myRunningTaskName.indexOf('维修人员')==-1"/>
                                </div>
                                <label class="col-md-2 control-label required">联系电话</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.chargeNo" name="chargeNo" required="true"   ng-disabled="processInstance.myRunningTaskName.indexOf('维修人员')==-1"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">维修地点</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.chargeTel" name="chargeTel" required="true"   ng-disabled="processInstance.myRunningTaskName.indexOf('维修人员')==-1"/>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label required">维修方式</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'维修方式'}:true"
                                            ng-model="vm.item.deviceLevel" class="form-control" ng-disabled="processInstance.myRunningTaskName.indexOf('维修人员')==-1"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">故障原因</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.failureCause"  name="failureCause" placeholder="" ng-disabled="processInstance.myRunningTaskName.indexOf('维修人员')==-1"></textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">解决方法</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.computerComment"  name="computerComment" placeholder="" ng-disabled="processInstance.myRunningTaskName.indexOf('维修人员')==-1"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">维修结果</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.repairTime"  name="repairTime" placeholder="" ng-disabled="processInstance.myRunningTaskName.indexOf('维修人员')==-1"></textarea>
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
                            <th>固定资产编号</th>
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
                <h4 class="modal-title margin-right-10">设备台账</h4>
            </div>
            <form class="form-horizontal form" role="form" id="detail_form2">
                <div class="form-body">
                    <div class="form-group">
                        <label class="col-md-2 control-label required">设备编号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.computerNo" name="computerNo" required="true"  disabled   />
                        </div>
                        <label class="col-md-2 control-label required">设备所属单位</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.computer.deptName" name="deptName"   disabled />
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();"  disabled  ><i class="fa fa-cog"></i></button>
                                         </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group" >
                        <label class="col-md-2 control-label required">责任人</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.computer.chargeManName" name="chargeManName"  disabled />
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('chargeMan');" disabled  ><i class="fa fa-user" ></i></button>
                                         </span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">责任人职工号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.chargeMan" name="chargeMan" required="true" disabled    />
                        </div>
                    </div>

                    <div class="form-group" >
                        <label class="col-md-2 control-label required">使用人</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.computer.useName" name="useName"  disabled />
                                <span class="input-group-btn" >
                                    <button class="btn default" type="button" ng-click="vm.showUserModel('useLogin');"  disabled ><i class="fa fa-user" ></i></button>
                                </span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">使用人职工号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.useLogin" name="useLogin" required="true" disabled/>
                        </div>
                    </div>

                    <div class="form-group" >
                        <label class="col-md-2 control-label required">设备序列号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.equipmentNo" name="equipmentNo" required="true"  disabled   />
                        </div>
                        <label class="col-md-2 control-label required">设备名称</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.computerName" name="computerName" required="true"  disabled   />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">用途</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.useWay" name="useWay" required="true"  disabled />
                        </div>
                        <label class="col-md-2 control-label required">使用类别</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.securityLevel" name="securityLevel" required="true" disabled  />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">联网类型</label>
                        <div class="col-md-4">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'设备网络'}:true" disabled
                                    ng-model="vm.computer.network" class="form-control"
                            ></select>
                        </div>

                        <label class="col-md-2 control-label required">初次启用时间</label>
                        <div class="col-md-4">
                            <div class="input-group date date-picker" id="applicantTime">
                                <input type="text" class="form-control"
                                       ng-model="vm.computer.useTime" name="useTime" required="true"  disabled  >
                                <span class="input-group-btn">
                                               <button class="btn default" type="button"  ><i class="fa fa-calendar"></i></button>
                                        </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">品牌</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.computerBrand" name="computerBrand" required="true" disabled  />
                        </div>
                        <label class="col-md-2 control-label required">型号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.snNo" name="snNo" required="true"   disabled/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label required">放置地点</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.room" name="room" required="true"  disabled   />
                        </div>
                        <label class="col-md-2 control-label required">设备类型</label>
                        <div class="col-md-4">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'信息化设备类型'}:true" disabled
                                    ng-model="vm.computer.equipmentType" class="form-control"
                            ></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">显示器类型</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.displayType" name="displayType" required="true"   disabled  />
                        </div>
                        <label class="col-md-2 control-label required">使用情况</label>
                        <div class="col-md-4">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'计算机使用情况'}:true" disabled
                                    ng-model="vm.computer.useStatus" class="form-control"
                            ></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">光驱类型</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.cdType" name="cdType" required="true"  disabled   />
                        </div>
                        <label class="col-md-2 control-label required">USB口状态</label>
                        <div class="col-md-4">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'usb状态'}:true" disabled
                                    ng-model="vm.computer.usbStatus" class="form-control"
                            ></select>
                        </div>
                    </div>


                    <%--运维部填写--%>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">硬盘序列号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.hardDiskNo" name="hardDiskNo" required="true"  disabled   />
                        </div>
                        <label class="col-md-2 control-label required">操作系统</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.operatingSystem" name="operatingSystem" required="true"  disabled   />
                        </div>


                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">操作系统安装时间</label>
                        <div class="col-md-4">
                            <div class="input-group date date-picker" id="operatingSystemTime">
                                <input type="text" class="form-control"
                                       ng-model="vm.computer.operatingSystemTime" name="operatingSystemTime" required="true"  disabled  >
                                <span class="input-group-btn">
                                               <button class="btn default" type="button"  ><i class="fa fa-calendar"></i></button>
                                        </span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">MAC地址</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.macAddress" name="macAddress" required="true"  disabled   />
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-md-2 control-label required">IP地址</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.ipAddress" name="ipAddress" required="true"  disabled   />
                        </div>
                        <label class="col-md-2 control-label required">资产编号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.fixedAssetNo" name="fixedAssetNo" required="true"   disabled  />
                        </div>
                    </div>

                    <div class="form-group">

                        <label class="col-md-2 control-label">备注</label>
                        <div class="col-md-10">
                            <input type="text" class="form-control" ng-model="vm.computer.remark" name="remark"  disabled />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">创建人</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.computer.creatorName"
                                   disabled="disabled"/>
                        </div>
                        <label class="col-md-2 control-label">创建时间</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control"
                                   value="{{vm.computer.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                        </div>
                    </div>
                </div>
            </form>


        </div>
    </div>
</div>


<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print-oaComputerMaintainDetail.jsp"/>
