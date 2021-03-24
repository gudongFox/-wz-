<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 岩土工程勘察成果文件确认记录
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/product/exportData.json?id='+vm.item.id}}" target="_blank">
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
                     style="height: 400px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label required">确认意见</label>
                                <div class="col-md-10">
                                    <textarea class="form-control" ng-model="vm.item.confirmResult" rows="3" required="true"  ng-disabled="!processInstance.firstTask"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">确认人/确认单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" required="true" ng-model="vm.item.receiveUser" name="receiveUser" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">确认日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="receiveDate">
                                        <input type="text" class="form-control" name="receiveDate"
                                               ng-model="vm.item.receiveDate" required="true"  ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" maxlength="100" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" required="true" ng-model="vm.item.creatorName"
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
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 勘察成果文件清单
        </div>
        <div class="actions" ng-show="processInstance.firstTask">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.saveSatisfy();">
                <i class="fa fa-save"></i> 保存 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer" id="detailSatisfy">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>文件名称</th>
                    <th>备注</th>
                    <th style="width: 80px;">是否存在</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detailSatisfy in vm.detailSatisfys">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detailSatisfy.contentDesc"></td>
                    <td ng-bind="detailSatisfy.remark"></td>
                    <td style="padding-left: 30px;">
                        <input type="checkbox"  style="height: 16px;width: 16px;"  ng-if="detailSatisfy.satisfy" checked name="satisfy"    ng-disabled="!processInstance.firstTask"/>
                        <input type="checkbox" style="height: 16px;width: 16px;" ng-if="!detailSatisfy.satisfy"  name="satisfy"   ng-disabled="!processInstance.firstTask"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <p style="color: red;">备注：所有资料一式六份</p>
    </div>
</div>
<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="./print/print-productDetail.jsp"/>




