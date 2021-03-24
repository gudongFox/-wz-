<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 重要环境因素清单
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/environment/exportData.json?id='+vm.environmentId}}" target="_blank">
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
                     style="height: 220px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" maxlength="100" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">创建人</label>
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
                     style="height: 220px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 220px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 重要环境因素清单详情
        </div>
        <div class="actions" ng-show="processInstance.firstTask">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addDetail();">
                <i class="fa fa-plus"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer" id="score">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="width: 100px;">产品/活动/服务</th>
                    <th style="width: 150px;">环境因素名称</th>
                    <th>环境影响</th>
                    <th style="width: 100px;">时态/状态</th>
                    <th style="width: 100px;">管理方式</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="exploreEnvironmentDetail in vm.exploreEnvironmentDetails">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="exploreEnvironmentDetail.productName"></td>
                    <td ng-bind="exploreEnvironmentDetail.environmentName"></td>
                    <td ng-bind="exploreEnvironmentDetail.environmentImpact"></td>
                    <td ng-bind="exploreEnvironmentDetail.environmentState"></td>
                    <td ng-bind="exploreEnvironmentDetail.environmentMis"></td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.editDetail(exploreEnvironmentDetail.id);"
                           title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(exploreEnvironmentDetail.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<div class="modal fade" id="AddDetailsModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">重要环境因素</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" >
                <div class="form-group">
                    <label class="col-md-3 control-label">产品/活动/服务</label>
                    <div class="col-md-9">
                        <input type="text" class="form-control" required="required" ng-model="vm.exploreEnvironmentDetail.productName"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">环境因素名称</label>
                    <div class="col-md-9">
                        <input type="text" class="form-control"  required="required" ng-model="vm.exploreEnvironmentDetail.environmentName"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">环境影响</label>
                    <div class="col-md-9">
                        <input type="text" class="form-control"  required="required" ng-model="vm.exploreEnvironmentDetail.environmentImpact"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">时态/状态</label>
                    <div class="col-md-9">
                        <input type="text" class="form-control" required="required" ng-model="vm.exploreEnvironmentDetail.environmentState"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">管理方式</label>
                    <div class="col-md-9">
                        <input type="text" class="form-control"  required="required" ng-model="vm.exploreEnvironmentDetail.environmentMis"/>
                    </div>
                </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="./print/print-environmentDetail.jsp"/>




