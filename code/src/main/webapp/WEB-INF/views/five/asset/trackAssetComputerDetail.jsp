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
            <span ng-bind="tableName">公司非密计算机及信息化设备台帐(补录)</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">公司非密计算机及信息化设备台帐(补录)</span>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();"  style="font-size: 14px;line-height: 1.6"><%-- ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                <li class="" >
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="" >
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">设备编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.computerNo" name="computerNo" required="true" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"    />
                                </div>
                                <label class="col-md-2 control-label required">设备所属单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"   ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)" />
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
                                        <input type="text" class="form-control" ng-model="vm.item.chargeManName" name="chargeManName"  readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('chargeMan');"  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)" ><i class="fa fa-user" ></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">责任人职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.chargeMan" name="chargeMan" required="true"    ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)" />
                                </div>
                            </div>

                            <div class="form-group" >
                                <label class="col-md-2 control-label required">使用人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.useName" name="useName"  readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('useLogin');"   ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"><i class="fa fa-user" ></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">使用人职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.useLogin" name="useLogin" required="true" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"/>
                                </div>
                            </div>

                            <div class="form-group" >
                                <label class="col-md-2 control-label required">设备序列号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.equipmentNo" name="equipmentNo" required="true"    ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)" />
                                </div>
                                <label class="col-md-2 control-label required">设备名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.computerName" name="computerName" required="true"   ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"  />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">用途</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'使用方式'}:true"  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"
                                            ng-model="vm.item.useWay" class="form-control"></select>
                                </div>
                                <label class="col-md-2 control-label required">使用类别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'使用类别'}:true" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"
                                            ng-model="vm.item.securityLevel" class="form-control"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联网类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'设备网络'}:true" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"
                                            ng-model="vm.item.network" class="form-control"
                                             ></select>
                                </div>

                                <label class="col-md-2 control-label required">初次启用时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applicantTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.useTime" name="useTime" required="true" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"   >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button"  ><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">品牌</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.computerBrand" name="computerBrand" required="true" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"   />
                                </div>
                                <label class="col-md-2 control-label required">型号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.snNo" name="snNo" required="true"  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">放置地点</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.room" name="room" required="true"  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"   />
                                </div>
                                <label class="col-md-2 control-label required">设备类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'信息化设备类型'}:true"
                                            ng-model="vm.item.equipmentType" class="form-control" required name="equipmentType"
                                    ></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">显示器类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.displayType" name="displayType" required="true"   ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"  />
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
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'光驱类型'}:true"  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"
                                            ng-model="vm.item.cdType" class="form-control"></select>
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
                                    <input type="text" class="form-control" ng-model="vm.item.hardDiskNo" name="hardDiskNo" required="true"  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"   />
                                </div>
                                <label class="col-md-2 control-label required">操作系统</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.operatingSystem" name="operatingSystem" required="true"  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"   />
                                </div>


                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">操作系统安装时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="operatingSystemTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.operatingSystemTime" name="operatingSystemTime" required="true"  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"  >
                                        <span class="input-group-btn">
                                               <button class="btn default" type="button"  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">MAC地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.macAddress" name="macAddress" required="true"     ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"/>
                                </div>
                            </div>

                            <div class="form-group">

                                <label class="col-md-2 control-label required">IP地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.ipAddress" name="ipAddress" required="true"    ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)" />
                                </div>
                                <label class="col-md-2 control-label required">资产编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.fixedAssetNo" name="fixedAssetNo" required="true"   ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"  />
                                </div>
                            </div>

                            <div class="form-group">

                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('信息化建设管理部')>=0)"  />
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

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../print/print.jsp"/>
