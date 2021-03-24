<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:50})">科研审批流程</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">专利项目交费申请</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">专利项目交费申请</span>
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
                            <div class="form-group" >
                                <label class="col-md-2 control-label ">专利产生、使用单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"    ng-disabled="!processInstance.firstTask" />
                                </div>

                                <label class="col-md-2 control-label ">日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="paymentTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.paymentTime" name="paymentTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
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
            <i class="fa fa-file"></i>缴费项目清单
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
                    <th style="width:120px ">专利号</th>
                    <th>专利名称</th>
                    <th>缴费年份</th>
                    <th>缴费金额（元）</th>
                    <th>维持/放弃改项专利理由</th>
                    <th>维持/放弃意见</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.detalList">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.inventNo" ng-click="vm.editDetail(detail.id);" style="color: blue;"></td>
                    <td ng-bind="detail.inventName"></td>
                    <td ng-bind="detail.year"></td>
                    <td ng-bind="detail.money"></td>
                    <td ng-bind="detail.reason"></td>
                    <td ng-bind="detail.keepGiveUp"></td>
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
                <h4 class="modal-title margin-right-10">缴费项目</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="PointDetail_form">
                    <div class="form-group">
                        <label class="col-md-3 control-label required">专利号</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.inventNo"  required="true"
                                   name="inventNo" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">专利名称</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.inventName"  required="true"
                                   name="inventName" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">缴费年份</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.year"  required="true"
                                   name="year" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">缴费金额</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.money"  required="true"
                                   name="money" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">维持/放弃改项专利理由</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.reason"  required="true"
                                   name="reason" placeholder="" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">维持/放弃意见</label>
                        <div class="col-md-9">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'专利缴费意见'}:true"
                                    ng-model="vm.detail.keepGiveUp" class="form-control"
                                   ></select>
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

<jsp:include page="../print/print-oaInventPaymentDetail.jsp"/>