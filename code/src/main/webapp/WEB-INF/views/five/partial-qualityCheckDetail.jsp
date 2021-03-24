<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-bell"></i> 设计质量抽查
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/five/qualityCheck/exportData.json?id='+vm.item.id}}"
               target="_blank">
                <i class="fa fa-file-word-o"></i> 导出</a>
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
                     style="height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label ">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">项目编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stageName" disabled/>
                                </div>
                                <label class="col-md-2 control-label">期次名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stepName" disabled/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">所属部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label required">专业</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.majorName"
                                               name="majorName" required="true" disabled="disabled"/>
                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.showSelectNeedChangeMajorModel()"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">设计人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.designMen" name="designMen" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">检查人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.checkMan" name="checkMan" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">质量部门负责人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.qualityDepartmentMenName"
                                               name="qualityDepartmentMenName"
                                               disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=user;vm.optUser=vm.item.qualityDepartmentMen;vm.showUserModel();"
                                                ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">概要说明</label>
                                <div class="col-md-10">
                                    <textarea class="form-control" rows="4" ng-model="vm.item.remark" ng-disabled="!processInstance.firstTask" required maxlength="400"></textarea>
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
                                           value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="height: 310px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 310px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>
<%--专业选择--%>
<div class="modal fade" id="selectNeedChangeMajorModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">专业选择</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="width: 70px;">专业</th>
                                <th style="width: 80px;">专业负责</th>
                                <th>设计</th>
                                <th style="width: 80px;">标准化审查</th>
                                <th style="width: 80px;">校核</th>
                                <th style="width: 80px;">审核</th>
                                <th style="width: 80px;">审定</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="user in vm.arrangeUsers" ng-if="vm.item.sourceMajor!=user.majorName&&user.majorChargeMan!=''">
                                <td>
                                    <input type="checkbox" class="cb_needChangeMajor"
                                           data-id="{{'file_'+user.id}}" ng-checked="vm.item.majorName.indexOf(user.majorName)>=0"  data-name="{{user.majorName}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="user.majorName"></td>
                                <td ng-bind="user.majorChargeMenName"></td>
                                <td ng-bind="user.designMenName"></td>
                                <td ng-bind="user.criterionExamineMenName"></td>
                                <td ng-bind="user.proofreadMenName"></td>
                                <td ng-bind="user.auditMenName"></td>
                                <td ng-bind="user.approveMenName"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <tr style="padding-left: 15px;">
                           <span class="data_title" style="font-size: 8px;margin-top: 2px" title="全选"
                                 ng-click="vm.selectAllFile();">全选</span><span
                            style="margin-left: 10px;font-size: 8px;" class="data_title" title="反选"
                            ng-click="vm.toggleSelectFile();">反选</span>
                    </tr>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectNeedChangeMajor();">保存</button>
            </div>
        </div>
    </div>
</div>
<%--子表详情--%>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i>质量抽查详情
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
                    <th style="width:120px ">图号或文件名</th>
                    <th>差错类别</th>
                    <th>审校意见</th>
                    <th>强条条文号</th>
                    <th style="width:60px ">处理结果</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.drawNo" ng-click="vm.editDetail(detail.id);" style="color: blue;"></td>
                    <td ng-bind="detail.markCategory"></td>
                    <td ng-bind="detail.markContent"></td>
                    <td ng-bind="detail.ruleNo"></td>
                    <td ng-bind="detail.solutionContent"></td>
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
                <h4 class="modal-title margin-right-10">验收清单详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="PointDetail_form">
                    <div class="form-group">
                        <label class="col-md-3 control-label required">图号或文件名</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.qualityCheckDetail.drawNo"  required="true"
                                   name="drawNo" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">差错类别</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.qualityCheckDetail.markCategory"  required="true"
                                   name="markCategory" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">校审意见</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.qualityCheckDetail.markContent"  required="true"
                                   name="markContent" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">强条条文号</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.qualityCheckDetail.ruleNo"  required="true"
                                   name="ruleNo" placeholder="" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">处理结果</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.qualityCheckDetail.solutionContent"  required="true"
                                   name="solutionContent" placeholder="">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">排序</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.qualityCheckDetail.seq"  required="true"
                                   name="seq" placeholder="">
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





