<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 噪音污染监测记录
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/noisy/exportData.json?id='+vm.noisyId}}" target="_blank">
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
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label required">编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.codeNo" name="codeNo" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">监测日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="recordDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.recordDate" required="true" name="recordDate" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">监测仪器型号</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.machineName" required="true"  ng-disabled="!processInstance.firstTask"
                                          name="machineName"/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">监测规则</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.nosiyRule" name="nosiyRule" required="true"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
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
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <%--流程信息--%>
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
<%--子表详情--%>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i>噪音污染监测记录
        </div>
        <a href="javascript:;" style="float: right;margin-right: 10px" class="btn btn-sm default fileinput-button" ng-show="processInstance.passAble"
           ng-click="vm.addNoisyDetail();">
            <i class="fa fa-plus"></i> 新增 </a>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>监测区域</th>
                    <th>监测时间</th>
                    <th>监测值(db)</th>
                    <th style="width: 55px;">监测人</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="noisyDetail in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="noisyDetail.areaName"></td>
                    <td ng-bind="noisyDetail.recordTime"></td>
                    <td ng-bind="noisyDetail.recordValue"></td>
                    <td ng-bind="noisyDetail.recordUser"></td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.edit(noisyDetail.id);"
                           title="详情"></i>
                        <i class="fa fa-trash"  ng-click="vm.deleteNoisyDetail(noisyDetail.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="noisyDetail" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">噪音污染监测</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="NoisyDetail_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">监测区域</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.noisyDetail.areaName"  required="true"
                                       maxlength="200" name="areaName" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">监测时间</label>
                            <div class="col-md-9">
                                <div class="input-group date date-picker" id="recordTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.noisyDetail.recordTime" required="true" name="recordTime" ng-disabled="!processInstance.firstTask">
                                    <span class="input-group-btn">
                                        <button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                    </span>
                                </div>
                             </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">监测值（db）</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.noisyDetail.recordValue" ng-disabled="!processInstance.firstTask" required="true"
                                       maxlength="200" name="recordValue" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">监测人</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.noisyDetail.recordUser" ng-disabled="!processInstance.firstTask" required="true"
                                       maxlength="200" name="recordUser" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">排序</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.noisyDetail.seq" ng-disabled="!processInstance.firstTask" required="true"
                                       maxlength="200" name="seq" placeholder="">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateNoisyDetail();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="print/print-noisyDetail.jsp"/>

