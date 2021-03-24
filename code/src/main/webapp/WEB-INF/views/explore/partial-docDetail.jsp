<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 岩土工程勘察成果文件确认记录
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
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
                     style="height: 250px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group"> <label class="col-md-2 control-label">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="编号" ng-model="vm.item.contractNo"
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
                                <label class="col-md-2 control-label required">资料管理员</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.docManager" required="true" name="docManager" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">工程号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo" name="projectNo" required="true" disabled="disabled"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">归档人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" required="true" ng-model="vm.item.docUser" name="docUser"  maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-2 control-label required">归档日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="docDate">
                                        <input type="text"  class="form-control" required="true" name="docDate" ng-disabled="!processInstance.firstTask"
                                               ng-model="vm.item.docDate" placeholder="归档日期">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
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

                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="height: 250px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 250px;overflow-y: auto;overflow-x: hidden;">
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
                    <th style="width: 150px;">分类</th>
                    <th>文件描述</th>
                    <th style="width: 80px;">是否存在</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detailSatisfy in vm.detailSatisfys">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detailSatisfy.contentCategory"></td>
                    <td ng-bind="detailSatisfy.contentDesc"></td>
                    <td  style="padding-left: 30px;">
                        <input type="checkbox"  style="height: 16px;width: 16px;"  ng-if="detailSatisfy.satisfy" checked name="satisfy"    ng-disabled="!processInstance.firstTask"/>
                        <input type="checkbox"  style="height: 16px;width: 16px;"  ng-if="!detailSatisfy.satisfy"  name="satisfy"   ng-disabled="!processInstance.firstTask"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="part-actFile.jsp"/>
<jsp:include page="./print/print-docDetail.jsp"/>





