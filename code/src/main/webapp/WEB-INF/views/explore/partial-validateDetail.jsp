<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 勘察成果文件审校记录
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/validate/exportData.json?id='+vm.validateId}}" target="_blank">
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
                     style="min-height: 300px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="" ng-model="vm.item.contractNo"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group"> <label class="col-md-2 control-label">项目阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stageName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">分期名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.stepName"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">工程号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo" name="projectNo" required="true" maxlength="30" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label required">校核人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.proofreadManName" name="proofreadManName" required="true" maxlength="30" disabled="disabled" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">审核人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.auditManName" name="auditManName" required="true" maxlength="30" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label required">审定人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.validatManName" name="validatManName" required="true" maxlength="30" disabled="disabled" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" maxlength="100" ng-disabled="!processInstance.firstTask"/>
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
                     style="height: 300px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 300px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-bubble"></i> 校审意见
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.newMark();" ng-show="processInstance.myTaskName=='校核人'||processInstance.myTaskName=='审核人'
                                                                                                                ||processInstance.myTaskName=='审定人'"><i class="fa fa-plus"></i> 增加 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_mark_1" data-toggle="tab" aria-expanded="true">
                        校核意见
                    </a>
                </li>
                <li class="">
                    <a href="#tab_mark_2" data-toggle="tab" aria-expanded="false">
                        审核意见
                    </a>
                </li>
                <li class="">
                    <a href="#tab_mark_3" data-toggle="tab" aria-expanded="false">
                        审定意见
                    </a>
                </li>

            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_mark_1"
                     style="max-height: 350px;overflow-y: auto;overflow-x: hidden;">

                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="width: 250px;" >文件名称</th>
                                <th>校核意见</th>
                                <th style="width: 200px;">设计回复</th>
                                <th style="width: 130px;">创建时间</th>
                                <th style="width: 55px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="mark in marks | filter:{markCategory:'校核'}:true">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="mark.fileName" style="color: blue" ng-click="vm.showMark(mark);"></td>
                                <td title="{{mark.markContent}}">
                                    <i class="fa fa-paw" ng-show="mark.cloudLocated"></i>
                                    <span ng-bind="mark.shortMarkContent"></span>
                                </td>
                                <td ng-bind="mark.shortMarkAnswer" title="{{mark.markAnswer}}" ></td>
                                <td ng-bind="mark.gmtCreate|date:'yyyy-MM-dd HH:mm'" title="{{mark.creatorName}}"></td>
                                <td>
                                    <i class="fa fa-edit margin-right-5" ng-click="vm.showMark(mark);" title="详情"></i>
                                    <i class="fa fa-trash" ng-click="vm.removeMark(mark.id);" ng-show="(processInstance.myTaskName=='校核人')&&mark.creator==user.userLogin" title="删除"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="tab-pane" id="tab_mark_2"
                     style="max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="width: 250px;">文件名称</th>
                                <th>审核意见</th>
                                <th style="width: 200px;">设计回复</th>
                                <th style="width: 130px;">创建时间</th>
                                <th style="width: 55px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="mark in marks | filter:{markCategory:'审核'}:true">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="mark.fileName" style="color: blue" ng-click="vm.showMark(mark);"></td>
                                <td title="{{mark.markContent}}">
                                    <i class="fa fa-paw" ng-show="mark.cloudLocated"></i>
                                    <span ng-bind="mark.shortMarkContent"></span>
                                </td>
                                <td ng-bind="mark.shortMarkAnswer" title="{{mark.markAnswer}}"></td>
                                <td ng-bind="mark.gmtCreate|date:'yyyy-MM-dd HH:mm'" title="{{mark.creatorName}}"></td>
                                <td>
                                    <i class="fa fa-edit margin-right-5" ng-click="vm.showMark(mark);" title="详情"></i>
                                    <i class="fa fa-trash" ng-click="vm.removeMark(mark.id);" ng-show="(processInstance.myTaskName=='审核人')&&vm.mark.creator==user.userLogin" title="删除"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
                <div class="tab-pane" id="tab_mark_3"
                     style="max-height: 350px;overflow-y: auto;overflow-x: hidden;">
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="width: 250px;">文件名称</th>
                                <th>审定意见</th>
                                <th style="width: 200px;">设计回复</th>
                                <th style="width: 130px;">创建时间</th>
                                <th style="width: 55px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="mark in marks | filter:{markCategory:'审定'}:true">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="mark.fileName" style="color: blue" ng-click="vm.showMark(mark);"></td>
                                <td title="{{mark.markContent}}">
                                    <i class="fa fa-paw" ng-show="mark.cloudLocated"></i>
                                    <span ng-bind="mark.shortMarkContent"></span>
                                </td>
                                <td ng-bind="mark.shortMarkAnswer" title="{{mark.markAnswer}}"></td>
                                <td ng-bind="mark.gmtCreate|date:'yyyy-MM-dd HH:mm'" title="{{mark.creatorName}}"></td>
                                <td>
                                    <i class="fa fa-edit margin-right-5" ng-click="vm.showMark(mark);" title="详情"></i>
                                    <i class="fa fa-trash" ng-click="vm.removeMark(mark.id);" ng-show="(processInstance.myTaskName=='审定人')&&vm.mark.creator==user.userLogin" title="删除"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>

        </div>
    </div>
</div>
<div class="modal fade" id="markModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="'校审意见('+vm.mark.markCategory+')'"></h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-2 control-label">文件类型</label>
                            <div class="col-md-10">
                                <select ng-options="s.id as s.fileName for s in files"
                                        ng-model="vm.mark.fileId" class="form-control" ng-disabled="vm.mark.creator!=user.userLogin||vm.mark.answer!=''"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">意见类型</label>
                            <div class="col-md-10">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'校审意见类型'}:true"
                                        ng-model="vm.mark.markLevel" class="form-control" ng-disabled="vm.mark.creator!=user.userLogin||vm.mark.answer!=''"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">图名图号</label>
                            <div class="col-md-10">
                                <input type="text" class="form-control" ng-model="vm.mark.drawNo" ng-disabled="vm.mark.creator!=user.userLogin||vm.mark.answer!=''" />
                            </div>

                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">校审意见</label>
                            <div class="col-md-10">
                                <textarea class="form-control" ng-model="vm.mark.markContent" rows="3" ng-disabled="vm.mark.creator!=user.userLogin||vm.mark.answer!=''"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">设计回复</label>
                            <div class="col-md-10">
                                <textarea class="form-control" ng-model="vm.mark.markAnswer" rows="3" ng-disabled="!(processInstance.firstTask&&vm.mark.solved!=1)"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">校审人</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.mark.creatorName" disabled="disabled"/>
                            </div>
                            <label class="col-md-2 control-label">创建时间</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" value="{{vm.mark.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label">设计人</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.mark.answerName" disabled="disabled"/>
                            </div>
                            <label class="col-md-2 control-label">回复时间</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" value="{{vm.mark.answerTime|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveMark();" ng-show="vm.mark.solved==0&&(processInstance.myTaskName=='校核人'||processInstance.myTaskName=='审核人'||processInstance.myTaskName=='审定人'||processInstance.myTaskName=='项目负责人')">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="./print/print-validateDetail.jsp"/>

