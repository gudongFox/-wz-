<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 钻探现场安全检查记录
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>

        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/onSec/exportData.json?id='+vm.onSecId}}" target="_blank">
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
                            <div class="form-group"> <label class="col-md-2 control-label">项目名称</label>
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
                                <label class="col-md-2 control-label ">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.creatorName"
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
            <i class="fa fa-file"></i> 钻探现场安全检查记录详情
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
                    <th style="width: 200px;">检查项目名称</th>
                    <th >检查情况</th>
                    <th style="width: 150px;">检查时间</th>
                    <th style="width: 100px;">备注</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="exploreOnSecDetail in vm.exploreOnSecDetails">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="exploreOnSecDetail.itemName"></td>
                    <td ng-bind="exploreOnSecDetail.checkDesc"></td>
                    <td ng-bind="exploreOnSecDetail.checkTime"></td>
                    <td ng-bind="exploreOnSecDetail.checkRemark"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.updateDetail(exploreOnSecDetail.id);" title="修改"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(exploreOnSecDetail.id);" title="删除"></i>
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
                <h4 class="modal-title margin-right-10">钻探现场安全检查记录</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" >
                <div class="form-group">
                    <label class="col-md-3 control-label">检查项目名称</label>
                    <div class="col-md-9">
                        <input type="text" class="form-control" required="required" ng-model="vm.exploreOnSecDetail.itemName"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">检查情况</label>
                    <div class="col-md-9">
                        <input type="text" class="form-control"  required="required" ng-model="vm.exploreOnSecDetail.checkDesc"/>
                    </div>
                </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">检查时间</label>
                        <div class="col-md-9">
                            <div class="input-group date date-picker" id="checkTime">
                                <input type="text" class="form-control"
                                       ng-model="vm.exploreOnSecDetail.checkTime">
                                <span class="input-group-btn">
                                    <button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                </span>
                            </div>
                        </div>
                    </div>
                <div class="form-group">
                    <label class="col-md-3 control-label">备注</label>
                    <div class="col-md-9">
                        <input type="text" class="form-control" required="required" ng-model="vm.exploreOnSecDetail.checkRemark"/>
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
<jsp:include page="./print/print-onSecDetail.jsp"/>




