<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
              <a >经营管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">各地公共资源平台备案</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">各地公共资源平台备案</span>
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
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.recordName"
                                               name="recordName" required="true"
                                               readonly/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showRecordModal();"
                                                    ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask">项目备案请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">项目号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo" name="projectNo" required="true"  disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">单位名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">备案流水号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.recordNo" name="recordNo" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.remark" name="remark" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
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
            <i class="fa fa-file"></i>备案信息
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
                    <th style="width: 35px;">#</th>
                    <th style="width:120px ">备案人</th>
                    <th>备案时间</th>
                    <th>省份</th>
                    <th>城市或地区</th>
                    <th>平台名称</th>
                    <th>平台网址</th>
                    <th>备案内容</th>
                    <th>有效期</th>
                    <th>是否有密码</th>
                    <th>密码管理者</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.detalList">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.recordMan" ng-click="vm.editDetail(detail.id);" style="color: blue;"></td>
                    <td ng-bind="detail.recordTime"></td>
                    <td ng-bind="detail.province"></td>
                    <td ng-bind="detail.city"></td>
                    <td ng-bind="detail.platformName"></td>
                    <td ng-bind="detail.platformUrl"></td>
                    <td ng-bind="detail.recordContent"></td>
                    <td ng-bind="detail.recordValidity"></td>
                    <td >
                        <span ng-if="detail.password">是</span>
                        <span ng-if="!detail.password">否</span>
                    </td>
                    <td ng-bind="detail.passwordCustodian"></td>
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
                <h4 class="modal-title margin-right-10">备案详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="PointDetail_form">
                    <div class="form-group">
                        <label class="col-md-3 control-label required">备案人</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.recordMan"  required="true"
                                   name="recordMan" placeholder=""  ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">备案时间</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.recordTime"  required="true"
                                   name="recordTime" placeholder=""  ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">省份</label>
                        <div class="col-md-9">
                            <select name="province" class="form-control"
                                    ng-model="vm.provinceId" ng-change="vm.provinceChange(vm.provinceId);"
                                    ng-options="id as name for (id,name) in vm.provinceArr" ng-disabled="disableFlag"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">城市或地区</label>
                        <div class="col-md-9">
                            <select name="city" class="form-control" ng-init="city.id='0'"
                                    ng-model="vm.cityId" ng-change="vm.cityChange(vm.cityId);"
                                    ng-options="id as name for (id,name) in vm.getCityArr" ng-disabled="disableFlag"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">平台名称</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.platformName"  required="true"
                                   name="platformName" placeholder=""  ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">平台网址
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.platformUrl"  required="true"
                                   name="platformUrl" placeholder=""  ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">备案内容
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.recordContent"  required="true"
                                   name="recordContent" placeholder=""  ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">有效期
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.recordValidity"  required="true"
                                   name="recordValidity" placeholder=""  ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">是否有密码
                        </label>
                        <div class="col-md-9">
                            <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                    ng-model="vm.detail.password" class="form-control"
                                    ng-disabled="!processInstance.firstTask"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">密码管理人
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.passwordCustodian"  required="true"
                                   name="passwordCustodian" placeholder=""  ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">备注
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.remark"  required="true"
                                   name="remark" placeholder=""  ng-disabled="!processInstance.firstTask">
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

<div class="modal fade draggable-modal" id="selectRecordModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">项目信息备案</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qPreContract"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>项目名称</th>
                            <th>项目号</th>
                            <th style="width: 80px;">项目类型</th>
                            <th>客户名称</th>
                            <th>所属部门</th>
                            <th style="width: 110px;">投资额(万元)</th>
                            <th>参与人</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="record in vm.listRecord">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.recordId==record.id" class="cb_record"
                                       data-name="{{record}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="record.projectName" class="data_title" ng-click="vm.show(item.id);"></td>
                            <td ng-bind="record.projectNo"></td>
                            <td ng-bind="record.projectType"></td>
                            <td ng-bind="record.customerName"></td>
                            <td ng-bind="record.deptName"></td>
                            <td ng-bind="record.projectInvest|currency : '￥'"></td>
                            <td ng-bind="record.businessUserName"></td>
                        </tr>

                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectRecord();">确认</button>
            </div>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print-oaPlatformRecordDetail.jsp"/>


