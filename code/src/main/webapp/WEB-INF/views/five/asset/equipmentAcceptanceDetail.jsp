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
            <span ng-bind="tableName">物资验收</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">物资验收</span>
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
                                <label class="col-md-2 control-label required">申请部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal('dept');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label required ">验收说明</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control" rows="3" required="true"
                                              ng-model="vm.item.remark" name="remark"  ng-disabled="!processInstance.firstTask"  >
                                    </textarea>
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
            <i class="fa fa-file"></i> 验收物资列表 <span style="color: red;font-size: 14px;" ng-bind="'总计:'+vm.item.totalPrice+'元'"></span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0,'总包');"
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
                    <th>名称</th>
                    <th style="width: 110px">资产编号</th>
                    <th style="width: 110px">发票编号</th>
                    <th style="width: 150px">设备规格</th>
                    <th style="width: 200px">所属部门</th>
                    <th style="width: 80px">单价（元）</th>
                    <th style="width: 80px">数量</th>
                    <th style="width: 80px">总价（元）</th>
                    <th >备注</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.equipmentName" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.fixedAssetNo"></td>
                    <td ng-bind="detail.invoiceNo"></td>
                    <td ng-bind="detail.specification"></td>
                    <td ng-bind="detail.deptName"></td>
                    <td ng-bind="detail.price"></td>
                    <td ng-bind="detail.number"></td>
                    <td ng-bind="detail.totalPrice"></td>
                    <td ng-bind="detail.remark"></td>
                    <td >
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除" ng-show="processInstance.firstTask"></i>
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
                <h4 class="modal-title">验收物资详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="productDetailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">名称</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.equipmentName" name="equipmentName" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">资产编号</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.fixedAssetNo" name="fixedAssetNo" required="true" <%--ng-disabled="processInstance.myRunningTaskName.indexOf('行政事务部资产管理')==-1"--%>>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">发票编号</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.invoiceNo" name="invoiceNo" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">设备规格</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.specification" name="specification" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">所属部门</label>
                        <div class="col-md-7">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.detail.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                <span class="input-group-btn" >
                                    <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">单价（元）</label>
                        <div class="col-md-7">
                            <input type="text"  ng-change="vm.countTotalPrice();"  class="form-control" ng-model="vm.detail.price" name="price" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label" >数量</label>
                        <div class="col-md-7">
                            <input type="number" class="form-control" ng-change="vm.countTotalPrice();" ng-model="vm.detail.number" name="number" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">总价(元)</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.totalPrice" name="totalPrice" disabled required="true" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">备注</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.remark" name="remark" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <%--图片附件--%>
                   <%-- <div class="form-group">
                        <label class="col-md-2 control-label " style="text-align: right">实物照片</label>
                        <div class="col-md-10">
                            <span  id="btnUpload" class="btn btn-sm blue fileinput-button"  ng-show="processInstance.firstTask&&vm.detailFileList.length<3">
                            <i class="fa fa-cloud-upload" style="height:15px " ></i>
                            <span>上传</span>
                            <input type="file" name="file" id="uploadFile"
                                   accept=".png,.jpg,"/>
                            </span>
                        </div>
                        <div ng-repeat="file in vm.detailFileList" style="margin-top: 4px;position:relative; display:inline-block;text-align: center; "   >
                            <div style="margin-left: 2px;margin-right: 2px; ">
                                <a  class="fancybox" data-fancybox-group="gallery " title="{{file.fileName}}"  href="/common/attach/download/{{file.attachId}}">
                                    <img class="child" ng-src="/common/attach/download/{{file.attachId}}"   style="width: 120px;height: 80px">
                                </a>
                                <div style="display: flex;flex-direction: row; justify-content:space-between">
                                    <div><i style="margin-left: 5px;" class=" fa fa-download" ng-click=" downloadFile(file,false);"></i></div>
                                    <div><i style="margin-right: 5px;" class="fa  fa-trash-o" ng-click="vm.removeDetailFile(file.id);"></i></div>
                                </div>
                            </div>
                            <div id="inline1" style="display: none;">
                                <div  style="width:800px;height:600px;overflow:auto;">
                                    <img class="child"  ng-src="/common/attach/download/{{file.attachId}}"   style="width: 800px;height: 600px">
                                </div>
                            </div>
                        </div>
                    </div>
--%>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();" ng-disabled="!processInstance.firstTask && processInstance.myRunningTaskName.indexOf('行政事务部资产管理')==-1">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>




