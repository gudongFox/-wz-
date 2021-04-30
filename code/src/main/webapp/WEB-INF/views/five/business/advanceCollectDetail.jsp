<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>经营管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">月度预支奖金签发单</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.remark"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">月度预支奖金签发单</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);"  style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.sendAble" style="font-size: 14px;line-height: 1.6"
               ng-click="vm.save();" >
                <i class="fa fa-save"></i> 保存 </a>

            <span ng-include="'/web/v1/tpl/actAction.html'" ng-controller="CommonActActionController" ng-if="processInstanceId" ></span>

            <a href="javascript:;" class="btn btn-sm btn-default"  style="font-size: 14px;line-height: 1.6"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6"><%-- ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>
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
                     style="min-height: 140px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">

                        <div class="form-group">
                           <%-- <label class="col-md-2 control-label required">上报部门</label>
                            <div class="col-md-4" >
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                    <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                </div>
                            </div>--%>
                            <label class="col-md-2 control-label required"><strong>上报时间</strong></label>
                            <div class="col-md-4">
                                <div class="input-group date date-picker-year-month" id="collectMonth">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.collectMonth" required="true"
                                           readonly>
                                    <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                            ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button></span>
                                </div>
                            </div>
                        </div>
                            <div class="form-group" >
                                <label class="col-md-2 control-label">申报类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'申报类型'}:true"
                                            ng-model="vm.item.declareType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label " style="font-weight: bold; color: red" ng-show="processInstance.firstTask">导出已上报数据</label>
                                <div class="col-md-4">
                                    <a href="javascript:;" class="  btn btn-sm default" ng-show="processInstance.firstTask"
                                       ng-click="vm.downCollect();" >
                                        <i class="fa fa-cloud-download"></i> 数据统计 </a>
                                    <a class="btn btn-sm green" ng-click="vm.downTotalData()" target="_blank">
                                        <i class="fa fa-cloud-download"></i> 数据汇总</a>
                                </div>
                            </div>

                        <%--发文正文--%>
                        <div ng-controller="RedHeaderController as rh">
                            <div class="form-group">

                                <label class="col-md-2 control-label " style="font-weight: bold;font-size: 18px"><strong>上报内容</strong></label>
                                <div class="col-md-4">
                                    <span id="btnUpload" class="btn btn-sm btn-default fileinput-button yellow-stripe">
                            <i class="fa fa-upload"></i>
                            <span>上传</span>
                            <input type="file" name="file" id="uploadFile1"
                                   multiple accept="application/vnd.ms-excel">
                </span>
                                    <a href="/assets/doc/导出模板/月度预支奖金签发单.xls" class="btn btn-sm btn-default yellow-stripe"
                                       ng-show="processInstance.firstTask">
                                        <i class="fa fa-download"></i> 模板下载 </a>
                                </div>
                            </div>
                        </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10" >
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.remark"   ng-disabled="!processInstance.firstTask">
                                </div>
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
            <i class="fa fa-file"></i>明细
        </div>

        <div class="actions">
            <a id="addBtn"  class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0);">
                <i class="fa fa-refresh"></i> 刷新 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: 500px;overflow-y: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">序号</th>
                    <th >单位名称</th>
                    <th>申请额度（万元）</th>
                    <th>公司核定额度（万元）</th>
                    <th>实际发放额度（万元）</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1" >
                    </td>
                    <td ng-bind="detail.deptName"></td>
                    <td ng-bind="detail.applyMoney"></td>
                    <td ng-bind="detail.companyMoney"></td>
                    <td ng-bind="detail.realMoney"></td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.showDetailModel(detail.id);"
                           title="详情"></i>
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
                <h4 class="modal-title margin-right-10">预支详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="detailForm">
                    <div class="form-group">
                        <label class="col-md-4 control-label required">单位名称</label>
                        <div class="col-md-8">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.detail.deptName" name="deptName"  required="true" readonly/>
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('personName');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label required">申请额度（万元）</label>
                        <div class="col-md-8">
                            <input type="text" class="form-control" ng-model="vm.detail.applyMoney" ng-disabled="!processInstance.firstTask"
                                   name="applyMoney" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label required" >公司核定额度（万元）</label>
                        <div class="col-md-8">
                            <input type="text" class="form-control" ng-model="vm.detail.companyMoney" name="companyMoney" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label required">实际发放额度（万元）</label>
                        <div class="col-md-8">
                            <input type="text" class="form-control" ng-model="vm.detail.realMoney" name="realMoney" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">备注</label>
                        <div class="col-md-8">
                            <input type="text" class="form-control" ng-model="vm.detail.remark" name="remark" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();" ng-show="processInstance.firstTask">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>