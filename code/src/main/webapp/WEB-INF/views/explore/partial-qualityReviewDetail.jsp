<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 岩土工程勘察质量综合评审表
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/qualityReview/exportData.json?id='+vm.qualityReviewId}}" target="_blank">
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
                     style="height: 280px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label">质量等级</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.reviewLevel"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">评审意见</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.reviewResult" required="true"  ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">实际得分</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.reviewScore" name="reviewScore" required="true" maxlength="30" disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" maxlength="100" ng-disabled="!processInstance.firstTask"/>
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
                     style="height: 280px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 280px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 评审详表
        </div>
        <div class="actions" ng-show="processInstance.firstTask">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.saveScore();">
                <i class="fa fa-save"></i> 保存 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer" id="score">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="width: 120px;">工序</th>
                    <th>评审内容</th>
                    <th style="width: 100px;">评分标准</th>
                    <th style="width: 100px;">实际评分</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="qualityReviewDetail in vm.qualityReviewDetails">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="qualityReviewDetail.workCategory"></td>
                    <td ng-bind="qualityReviewDetail.workContent"></td>
                    <td ng-bind="qualityReviewDetail.workScore" name="workScore"></td>
                    <td>
                        <input type="text" class="form-control input-sm"  ng-model="qualityReviewDetail.actualScore" name="actualScore" maxlength="10"  required="true" ng-disabled="!processInstance.firstTask"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="print/print-qualityReviewDetail.jsp"/>



