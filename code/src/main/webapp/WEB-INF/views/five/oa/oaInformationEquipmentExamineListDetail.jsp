<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link href="/assets/fancybox/source/jquery.fancybox.css" rel="stylesheet"  media="screen" type="text/css" >
<script type="text/javascript" src="/assets/fancybox/source/jquery.fancybox.js"></script>
<script type="text/javascript" src="/assets/fancybox/source/jquery.fancybox.pack.js"></script>
<script type="text/javascript" src="/assets/fancybox/source/helpers/jquery.fancybox-buttons.js?v=1.0.5"></script>

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
            <span ng-bind="tableName">信息化设备验收(多台)审批</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">信息化设备验收(多台)审批</span>
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
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.diskNo" name="diskNo" required="true" readonly  />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showEquipmentApply();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDetailApply();" ng-disabled="vm.item.equipmentApply==null" ><i class="fa fa-star"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red">点击<i style="color: black" class="fa fa-cog"></i>选择采购审批单,点击<i style="color: black" class="fa fa-star"></i>显示所选择的审批单详情</span>
                                </div>
                                <label class="col-md-2 control-label required">申请单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal(vm.opt='deptId');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
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
                        <div  class="form-group" >
                            <label class="col-md-2 control-label ">*类型说明</label>
                            <div class="col-md-5" style="height: 110px">
                                <ui>
                                    <li>01-台式计算机：台式计算机、工作站等</li>
                                    <li>02-便携式计算机：便携式计算机、移动工作站等</li>
                                    <li>03-图文加工设备：打印机、扫描仪、复印机、晒图仪、绘图仪、多功能一体机等</li>
                                    <li>04-移动存储设备：U盘、移动硬盘等</li>
                                    <li>05-服务器</li>
                                </ui>
                            </div>
                            <div class="col-md-5" style="height: 110px">
                                <ui>
                                    <li>06-网络设备：交换机、路由器、网关</li>
                                    <li>07-安全设备</li>
                                    <li>08-加密USBKey：加密狗、网盾等</li>
                                    <li>09-声像设备：投影仪、视频会议设备、数字化会议设备等</li>
                                    <li>10-其他：传真机、碎纸机等</li>
                                </ui>
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
            <i class="fa fa-file"></i> 购置设备信息<span style="margin-left: 30px;color: red;font-size: 12px">总计：<span ng-bind="vm.item.checkPrice+'元'"></span></span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addDetail();"
               ng-show="processInstance.firstTask">
                <i class="fa fa-cog"></i> 新增 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showApplyDetail();"
               ng-show="processInstance.firstTask">
                <i class="fa fa-cog"></i> 验收单子项 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th style="width: 120px;">设备名称</th>
                    <th  style="width: 110px;">设备序列号</th>
                    <th  style="width: 100px;">设备编号</th>
                    <th style="width: 60px">责任人</th>
                    <th style="width: 60px">使用人</th>
                    <th style="width: 110px">设备类型</th>
                    <th style="width: 100px;">资产编号</th>
                    <th>所属单位</th>
                    <th style="width: 60px">用途</th>
                    <th style="width: 100px" >启用时间</th>
                    <th style="width: 60px;">验收价格（元）</th>
                    <th style="width: 75px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.examineList">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.equipmentName" class="data_title" ng-click="vm.showDetail(detail.id);"></td>
                    <td ng-bind="detail.equipmentNo"></td>
                    <td ng-bind="detail.formNo"></td>
                    <td ng-bind="detail.chargeManName"></td>
                    <td ng-bind="detail.userName"></td>
                    <td ng-bind="detail.osInstallTime"></td>
                    <td ng-bind="detail.fixedAssetNo"></td>
                    <td ng-bind="detail.deptName"></td>
                    <td ng-bind="detail.useType"></td>
                    <td ng-bind="detail.startTime"></td>
                    <td ng-bind="detail.checkPrice"></td>
                    <td >
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetail(detail.id);" title="详情"></i>
                        <i class="fa fa-refresh margin-right-5" ng-click="vm.getEquipmentNo(detail.id);" ng-show="processInstance.myRunningTaskName.indexOf('信息化建设与管理部')>-1" title="生成设备编号"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);"  ng-show="processInstance.firstTask" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg" >
        <div class="modal-content ">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">设备详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-group">
                        <label class="col-md-2 control-label required">设备名称</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.examine.equipmentName" name="equipmentName" required="true" ng-disabled="!processInstance.firstTask" />
                        </div>
                        <label class="col-md-2 control-label required">设备序列号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.examine.equipmentNo" name="equipmentNo" required="true"   ng-disabled="!processInstance.firstTask"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label required">设备编号</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.examine.formNo" name="formNo" required="true" ng-disabled="processInstance.myRunningTaskName.indexOf('信息化建设与管理部')==-1"/><%----%>
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.getEquipmentNo();" ng-disabled="processInstance.myRunningTaskName.indexOf('信息化建设与管理部')==-1"><i class="fa fa-refresh"></i></button>
                                         </span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">所属单位</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.examine.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal('examine');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label required">使用类别</label>
                        <div class="col-md-4">
                            <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'使用类别'}:true"
                                    ng-model="vm.examine.secretRank" class="form-control"
                                    ng-disabled="!processInstance.firstTask"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">责任人</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.examine.chargeManName" name="chargeManName" required="true"   readonly/>
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('chargeMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">职工号</label>
                        <div class="col-md-4">
                            <input  type="text" class="form-control" ng-model="vm.examine.chargeMan" name="chargeMan" required="true"   readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">使用人</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.examine.userName" name="userName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('userMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">职工号</label>
                        <div class="col-md-4">
                            <input  type="text" class="form-control" ng-model="vm.examine.user" name="user" required="true"   readonly/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">启用时间</label>
                        <div class="col-md-4">
                            <div class="input-group date date-picker" id="startTime">
                                <input type="text"  class="form-control" required="true" name="startTime"
                                       ng-disabled="!processInstance.firstTask"
                                       ng-model="vm.examine.startTime" placeholder="启用时间">
                                <span class="input-group-btn">
												<button class="btn default" type="button"ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
												</span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">用途</label>
                        <div class="col-md-4">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'使用方式'}:true"
                                    ng-model="vm.examine.useType" class="form-control"
                                    ng-disabled="!processInstance.firstTask"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">设备品牌</label>
                        <div class="col-md-4">
                            <input  type="text" class="form-control" ng-model="vm.examine.brand" name="brand" required="true"   ng-disabled="!processInstance.firstTask"/>
                        </div>
                        <label class="col-md-2 control-label required">设备型号</label>
                        <div class="col-md-4">
                            <input  type="text" class="form-control" ng-model="vm.examine.type" name="type" required="true"   ng-disabled="!processInstance.firstTask"/>
                        </div>
                    </div>


                    <div class="form-group">
                        <label class="col-md-2 control-label required">放置地点</label>
                        <div class="col-md-4">
                            <input  type="text" class="form-control" ng-model="vm.examine.address" name="address" required="true" placeholder="楼号#房间号  "  ng-disabled="!processInstance.firstTask"/>
                        </div>
                        <label class="col-md-2 control-label required">显示器型号</label>
                        <div class="col-md-4">
                            <input  type="text" class="form-control" ng-model="vm.examine.displayNo" name="displayNo" required="true"   ng-disabled="!processInstance.firstTask"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label required">设备类型</label>
                        <div class="col-md-4">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'信息化设备类型'}:true"
                                    ng-model="vm.examine.osInstallTime" class="form-control"
                                    ng-disabled="!processInstance.firstTask"></select>
                        </div>
                        <label class="col-md-2 control-label required">验收价格（元）</label>
                        <div class="col-md-4">
                            <input  type="text" class="form-control" ng-model="vm.examine.checkPrice" name="checkPrice" required="true"  ng-disabled="!processInstance.firstTask"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label required">资产编号</label>
                        <div class="col-md-4">
                            <input  type="text" class="form-control" ng-model="vm.examine.fixedAssetNo" name="fixedAssetNo" required="true"   ng-disabled="processInstance.myRunningTaskName.indexOf('行政事务部(设备台帐岗)')==-1"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label">备注</label>
                        <div class="col-md-10">
                            <input type="text" class="form-control" ng-model="vm.detail.remark" name="remark">
                        </div>
                    </div>
                    <%--图片附件--%>
                    <div class="form-group">
                        <label class="col-md-2 control-label " style="text-align: right">附件</label>
                        <div class="col-md-10">
                            <span  id="btnUpload" class="btn btn-sm blue fileinput-button"  ng-show="processInstance.firstTask&&vm.examineFileList.length<5">
                            <i class="fa fa-cloud-upload" style="height:15px " ></i>
                            <span>上传</span>
                            <input type="file" name="file" id="uploadFile"
                                   accept=".png,.jpg,"/>
                            </span>
                        </div>
                        <div ng-repeat="file in vm.examineFileList" style="margin-top: 4px;position:relative; display:inline-block;text-align: center; "   >
                            <div style="margin-left: 2px;margin-right: 2px; ">
                                <a  class="fancybox" data-fancybox-group="gallery " title="{{file.fileName}}"  href="/common/attach/download/{{file.attachId}}">
                                    <img class="child" ng-src="/common/attach/download/{{file.attachId}}"   style="width: 120px;height: 80px">
                                </a>
                                <div style="display: flex;flex-direction: row; justify-content:space-between">
                                    <div><i style="margin-left: 5px;" class=" fa fa-download" ng-click=" downloadFile(file,false);"></i></div>
                                    <div><i style="margin-right: 5px;" class="fa  fa-trash-o" ng-click="vm.removeDetailFile(file.id);" ng-show="processInstance.firstTask"></i></div>
                                </div>
                            </div>
                            <div id="inline1" style="display: none;">
                                <div  style="width:800px;height:600px;overflow:auto;">
                                    <img class="child"  ng-src="/common/attach/download/{{file.attachId}}"   style="width: 800px;height: 600px">
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();"
                        ng-show="processInstance.firstTask||processInstance.myRunningTaskName.indexOf('行政事务部(设备台帐岗)')>-1||processInstance.myRunningTaskName.indexOf('信息化建设与管理部')>-1">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<%--选择信息化设备采购事项--%>
<div class="modal fade" id="selectEquipmentApplyModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">信息化设备采购事项</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-12">
                        <input type="text" class="form-control input-inline input-sm" placeholder="购置单编号"
                               ng-model="vm.params.formNo"/>
                        <input type="text" class="form-control input-inline input-sm" placeholder="联系人"
                               ng-model="vm.params.qName"/>
                        <a class="btn green btn-sm defaultBtn" ng-click="vm.loadEquipmentApply();"><i class="fa fa-search"></i> 查询 </a>
                    </div>
                </div>
                <div class="table-scrollable"  style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>购置单编号</th>
                            <th style="width: 180px">申请单位</th>
                            <th style="width: 100px">联系人</th>
                            <th >联系电话</th>
                            <th style="width: 100px">申请时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="c in vm.listDetail">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.diskNo==c.formNo" class="cb_apply" data-name="{{c}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="c.formNo" ></td>
                            <td ng-bind="c.deptName" ></td>
                            <td ng-bind="c.linkManName" ></td>
                            <td ng-bind="c.linkManPhone" ></td>
                            <td ng-bind="c.applyTime"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <my-pager data-page-info="vm.pageInfo" on-load="vm.loadEquipmentApply()"></my-pager>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveEquipmentApply();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="equipmentApplyModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">信息化设备采购事项</h4>
            </div>
            <form class="form-horizontal form" role="form" id="detail_form2">
                <div class="form-body">
                    <div class="form-group">
                        <label class="col-md-2 control-label required">购置单编号</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.equipmentApply.formNo" name="formNo" required="true"   disabled/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label required">申请单位</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.equipmentApply.deptName" name="deptName" required="true"   disabled/>
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal(vm.opt='deptId');" disabled><i class="fa fa-cog"></i></button>
                                         </span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">是否在预算内</label>
                        <div class="col-md-4">
                            <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                    ng-model="vm.equipmentApply.plan" class="form-control"
                                    disabled ></select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label required">联系人</label>
                        <div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.equipmentApply.linkManName" name="linkManName"  required="true" disabled />
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('linkMan');" disabled><i class="fa fa-user"></i></button>
                                         </span>
                            </div>
                        </div>
                        <label class="col-md-2 control-label required">联系电话</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control"
                                   ng-model="vm.equipmentApply.linkManPhone" name="linkManPhone" required="true" disabled  >
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-2 control-label ">设备用途及购置理由</label>
                        <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control"
                                              ng-model="vm.equipmentApply.equipmentUse" name="equipmentUse"  disabled   placeholder="预算外采购填报"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label ">备注</label>
                        <div class="col-md-10">
                            <input type="text" class="form-control"
                                   ng-model="vm.equipmentApply.remark" name="remark"  disabled  >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-2 control-label">创建人</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control" ng-model="vm.equipmentApply.creatorName"
                                   disabled="disabled"/>
                        </div>
                        <label class="col-md-2 control-label">创建时间</label>
                        <div class="col-md-4">
                            <input type="text" class="form-control"
                                   value="{{vm.equipmentApply.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                        </div>
                    </div>
                </div>
            </form>

            <div class="portlet light">
                <div class="portlet-title">
                    <div class="caption">
                        <i class="fa fa-file"></i> 购置设备信息<span style="margin-left: 30px;color: red;font-size: 12px">总计：<span ng-bind="vm.equipmentApply.totalMoney+'元'"></span></span>
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
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%--选择信息化设备采购事项子项--%>
<div class="modal fade" id="applyDetailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">信息化设备采购事项</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-12">
                        <input type="text" class="form-control input-inline input-sm" placeholder="购置单编号"
                               ng-model="vm.params.formNo"/>
                        <input type="text" class="form-control input-inline input-sm" placeholder="联系人"
                               ng-model="vm.params.qName"/>
                        <a class="btn green btn-sm defaultBtn" ng-click="vm.loadEquipmentApply();"><i class="fa fa-search"></i> 查询 </a>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px">
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
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="c in vm.detalList">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.diskNo==c.formNo" class="cb_applyDetail" data-name="{{c}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="c.equipmentName" class="data_title" ></td>
                            <td ng-bind="c.deptName"></td>
                            <td ng-bind="c.brand"></td>
                            <td ng-bind="c.equipmentType"></td>
                            <td ng-bind="c.number"></td>
                            <td ng-bind="c.price"></td>
                            <td ng-bind="c.totalPrice"></td>
                            <td ng-bind="c.otherRequirement"></td>
                            <td ng-bind="c.remark"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveApplyDetail();">确认</button>
            </div>
        </div>
    </div>
</div>




<script>
    $(document).ready(function () {
        $(".fancybox").fancybox({
            'type':'image',
            autoScale:false,
            closeClick:true,
            hideOnOverlayClic:false,
            minWidth:600,
            //maxWidth:400,
            minHeight:400,
            //maxHeight:300,

        });
    })

</script>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>