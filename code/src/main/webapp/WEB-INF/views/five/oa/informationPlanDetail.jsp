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
            <span ng-bind="tableName">年度软件采购预算</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">年度软件采购预算</span>
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
                                <label class="col-md-2 control-label ">申请单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"    disabled/>
                                </div>
                                <label class="col-md-2 control-label ">预算年份</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.year" name="year" required="true" disabled  >
                                </div>

                            </div>

                            <div class="form-group" >
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" placeholder=""ng-disabled="!processInstance.firstTask" />
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
<%--子表详情--%>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i>预算详情 <span style="margin-left: 30px;color: red;font-size: 12px">总计：<span ng-bind="vm.item.totalMoney+'元'"></span></span>
        </div>
        <div class="actions" ng-show="processInstance.firstTask">
            <a  class="btn btn-sm default fileinput-button"
                ng-click="vm.addDetail();">
                <i class="fa fa-plus"></i> 新增</a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: 500px;overflow-y: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">序号</th>
                    <th style="width:200px ">软件名称</th>
                    <th>功能模块</th>
                    <th>类型</th>
                    <th>用途</th>
                    <th>专业</th>
                    <th style="width:60px">数量（套）</th>
                    <th style="width:90px">单价（元）</th>
                    <th style="width:120px">合计（元）</th>
                    <th>备注</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.detalList">
                    <td class="dt-right" ng-bind="$index+1" >
                    </td>
                    <td ng-bind="detail.softwareName" ng-click="vm.editDetail(detail.id);" style="color: blue;"></td>
                    <td ng-bind="detail.softwareModel"></td>
                    <td ng-bind="detail.softwareType"></td>
                    <td ng-bind="detail.softwareUseWay"></td>
                    <td ng-bind="detail.useMajor"></td>
                    <td ng-bind="detail.softwareNumber"></td>
                    <td ng-bind="detail.softwarePrice"></td>
                    <td ng-bind="detail.softwareTotal"></td>
                    <td ng-bind="detail.remark"></td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.editDetail(detail.id);"
                           title="详情"></i>
                        <i class="fa fa-trash"  ng-click="vm.removeDetail(detail.id);" title="删除" ng-show="processInstance.firstTask"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="detailModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">预算详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="PointDetail_form">
                    <div class="form-group">
                        <label class="col-md-3 control-label required">软件名称</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.softwareName"  required="true"
                                   name="softwareName" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">功能模块</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.softwareModel"  required="true"
                                   name="softwareModel" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">类型</label>
                        <div class="col-md-9">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'软件联网类型'}:true"
                                    ng-model="vm.detail.softwareType" class="form-control"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">用途</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.softwareUseWay"  required="true"
                                   name="softwareUseWay" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">专业</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.useMajor"  required="true"
                                   name="useMajor" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">数量（套）</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.softwareNumber" ng-change="vm.coutTotal();"     required="true"
                                   name="reason" placeholder="" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">单价（元）</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.softwarePrice" ng-change="vm.coutTotal();"   required="true"
                                   name="year" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">合计（元）</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.softwareTotal"  required="true" disabled
                                   name="softwareTotal" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">备注</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.remark"  required="true"
                                   name="remark" placeholder="请备注是否为自主开发">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateDetail();" ng-show="processInstance.firstTask">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print-informationPlan.jsp"/>