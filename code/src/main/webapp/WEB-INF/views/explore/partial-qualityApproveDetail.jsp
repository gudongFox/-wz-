<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i>岩土工程勘察质量审查验收表
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/qualityApprove/exportData.json?id='+vm.qualityApproveId}}" target="_blank">
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
                     style="min-height: 310px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label">主任工程师<br/>审查意见</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.departChiefEngineerResult" ng-disabled="processInstance.myTaskName!='主任工程师'"
                                           name="departChiefEnineerResult" /><%--ng-disabled="processInstance.ta"--%>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">审定人<br/>审查意见</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.viceChiefEngineerResult" ng-disabled="!processInstance.myTaskName!='审定人'"
                                           name="viceChiefEngineerResult" />
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
                     style="height: 310px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 310px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

            </div>
        </div>
    </div>
</div>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 岩土工程勘察质量审查验收表
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="width: 150px">评审类别</th>
                    <th>评审要点</th>
                    <th style="width: 80px;">是否满足</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detailSatisfy in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detailSatisfy.contentCategory"></td>
                    <td ng-bind="detailSatisfy.contentDesc"></td>
                    <td ng-if="detailSatisfy.satisfy==true">满足</td>
                    <td ng-if="detailSatisfy.satisfy==false">不满足</td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.edit(detailSatisfy.id);"
                           title="详情"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="modal fade" id="detailSatisfy" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">岩土工程勘察质量审查验收表</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="Satisfy_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">文件类型</label>
                            <div class="col-md-9">
                                <select ng-model="vm.detailSatisfy.contentCategory" name="contentCategory"  class="form-control" readonly>
                                    <option value="勘察成果资料质量">勘察成果资料质量</option>
                                    <option value="勘察结论与建议">勘察结论与建议</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">文件描述</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detailSatisfy.contentDesc" readonly required="true"
                                       maxlength="200" name="contentDesc" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">文件是否满足</label>
                            <div class="col-md-9">
                                <select ng-model="vm.detailSatisfy.satisfy" name="satisfy"  class="form-control" >
                                    <option ng-selected="vm.detailSatisfy.satisfy==true" value="true">满足</option>
                                    <option ng-selected="vm.detailSatisfy.satisfy==false" value="false">不满足</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detailSatisfy.remark"
                                       maxlength="200"  placeholder="">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateDetailSatisfy();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<jsp:include page="../common/common-edFile2.jsp"/>

<jsp:include page="print/print-qualityApproveDetail.jsp"/>

