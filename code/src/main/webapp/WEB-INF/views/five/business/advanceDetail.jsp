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
            <span ng-bind="tableName">月度预支绩效工资上报</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.contractName"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">月度预支绩效工资上报</span>
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
                                <label class="col-md-2 control-label required">申报部门</label>
                                <div class="col-md-4" >
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">申报时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker-year-month" id="month">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.month" required="true" readonly>
                                        <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                            ng-disabled="!processInstance.firstTask"><i
                                            class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group" >
                                <label class="col-md-2 control-label">预支说明</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control" ng-model="vm.item.remark" name="remark"  placeholder=""ng-disabled="!processInstance.firstTask" ></textarea>
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
            <i class="fa fa-file"></i>预支详情 <span style="margin-left: 30px;color: red;font-size: 12px">总计：<span style="font-size: 16px;" ng-bind="vm.item.totalPrice+'元'"></span></span>
            <a class="btn green btn-sm defaultBtn" style="margin-left: 10px" href="/assets/doc/预支明细.xls"><i class="fa fa-download"></i>模板下载</a>
            <span style="margin-left: 3px;" id="btnUpload" class="btn btn-sm blue fileinput-button" >
                <i class="fa fa-cloud-upload"></i>
                <span>上传数据</span>
                <span id="uploadProgress"></span>
                <input type="file" name="multipartFile" id="uploadModelFile"
                       accept="*"
                       multiple="multiple"/>
            </span>
            <a class="btn green btn-sm defaultBtn" ng-click="vm.downExcel();"><i class="fa fa-download"></i> 导出EXCEL </a>
        </div>
        <div class="actions" ng-show="processInstance.firstTask">
            <a  class="btn btn-sm default fileinput-button"
                ng-click="vm.showDetailModel(0);">
                <i class="fa fa-plus"></i> 新增</a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: 500px;overflow-y: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">序号</th>
                    <th style="width:120px ">职工号</th>
                    <th>姓名</th>
                    <th>具体部门</th>
                    <th>人员类别</th>
                    <th>项目奖金（元）</th>
                    <th>备注</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1" >
                    </td>
                    <td ng-bind="detail.personNo" ng-click="vm.showDetailModel(detail.id);" style="color: blue;"></td>
                    <td ng-bind="detail.personName"></td>
                    <td ng-bind="detail.deptName"></td>
                    <td ng-bind="detail.personnelCategory"></td>
                    <td ng-bind="detail.projectBonus"></td>
                    <td ng-bind="detail.remark"></td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.showDetailModel(detail.id);"
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
                <h4 class="modal-title margin-right-10">预支详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="detailForm">
                    <div class="form-group">
                        <label class="col-md-3 control-label">姓名</label>
                        <div class="col-md-9">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.detail.personName" name="personName"  required="true" readonly/>
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('personName');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">职工号</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.personNo" disabled
                                   name="personName" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">具体部门</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.deptName" name="deptName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">人员类别</label>
                        <div class="col-md-9">
                            <%--<input type="text" class="form-control" ng-model="vm.detail.personnelCategory"  required="true"
                                   name="personnelCategory" placeholder="">--%>
                            <select ng-model="vm.detail.personnelCategory" class="form-control"
                                    ng-disabled="!processInstance.firstTask">
                                <option value="在职">在职</option>
                                <option value="返聘">返聘</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">项目奖金（元）</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.projectBonus" name="projectBonus" ng-change="vm.countTotalPrice();" required="true" ng-disabled="!processInstance.firstTask" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">备注</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.remark" name="remark" >
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

