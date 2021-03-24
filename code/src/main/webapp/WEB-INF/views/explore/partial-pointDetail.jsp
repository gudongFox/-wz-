<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 勘探点性质一览表
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/pointDetail/exportData.json?id='+vm.pointId}}" target="_blank">
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
                     style="height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label ">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label">项目阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stageName" disabled/>
                                </div>
                                <label class="col-md-2 control-label">分期名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stepName" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" maxlength="100" ng-disabled="!processInstance.firstTask" />
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
                     style="height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 240px;overflow-y: auto;overflow-x: hidden;">
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
            <i class="fa fa-file"></i>勘探点资料
        </div>
        <div class="actions" ng-show="processInstance.firstTask">
            <a  class="btn btn-sm default fileinput-button"
                ng-click="vm.addPointDetail();">
                <i class="fa fa-plus"></i> 新增数据 </a>
<%--            <span id="btnDown" class="btn btn-sm default fileinput-button">
                            <i class="fa fa-cloud-download"></i>
                            <span>下载模板</span>
                            <span id="downProgress"></span>
                            <input ng-click="vm.downloadModel();"/>
                             </span>--%>
            <span id="btnUpload" class="btn btn-sm default fileinput-button">
                            <i class="fa fa-cloud-upload"></i>
                            <span>导入数据</span>
                            <span id="uploadProgress"></span>
                            <input type="file" name="multipartFile" id="uploadModelFile"
                                   accept="*"
                                   multiple="multiple"/>
                             </span>

                <a  class="btn btn-sm default fileinput-button"
                    ng-href="{{'/explore/pointDetail/downloadExcel.json?modelId='+vm.modelId+'&pointId='+vm.pointId}}" target="_blank">
                    <i class="fa fa-file"></i> 导出数据 </a>

        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: 500px;overflow-y: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">#</th>
                    <th>勘探点类型</th>
                    <th>深度（m）</th>
                    <th>取样(土/件)</th>
                    <th>取样(岩/组)</th>
                    <th>原位测试</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="pointDetail in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="pointDetail.itemType" ng-click="vm.edit(pointDetail.id);" style="color: blue;"></td>
                    <td ng-bind="pointDetail.itemHeight"></td>
                    <td ng-bind="pointDetail.itemSoilsample"></td>
                    <td ng-bind="pointDetail.itemRocksample"></td>
                    <td ng-bind="pointDetail.itemTest"></td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.edit(pointDetail.id);"
                           title="详情"></i>
                        <i class="fa fa-trash"  ng-click="vm.deletePointDetail(pointDetail.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="pointDetail" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">勘探点详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="PointDetail_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">勘探点类型</label>
                            <div class="col-md-8">
                            <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'勘探点类型'}:true"
                                    ng-model="vm.pointDetail.itemType" class="form-control" ng-disabled=""></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">勘探点深度（米）</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" ng-model="vm.pointDetail.itemHeight" required="true"
                                       maxlength="100" name="itemHeight" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">取样（土/件）</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" ng-model="vm.pointDetail.itemSoilsample"  required="true"
                                       maxlength="100" name="itemSoilsample" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">取样（岩/组）</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" ng-model="vm.pointDetail.itemRocksample"  required="true"
                                       maxlength="200" name="itemRocksample" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">原位测试（次）</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" ng-model="vm.pointDetail.itemTest"  required="true"
                                       maxlength="200" name="itemTest" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">排序</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" ng-model="vm.pointDetail.seq"  required="true"
                                       maxlength="200" name="seq" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">备注</label>
                            <div class="col-md-8">
                                <input type="text" class="form-control" ng-model="vm.pointDetail.itemRemark"
                                       maxlength="200" name="itemRemark" placeholder="">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updatePointDetail();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="./print/print-pointDetail.jsp"/>

