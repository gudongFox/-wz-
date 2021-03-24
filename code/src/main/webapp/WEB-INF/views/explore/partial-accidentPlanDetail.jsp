<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 事故应急救援预案
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
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
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

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 附件列表
        </div>
        <div class="actions" ng-show="processInstance.firstTask">
            <span  class="btn btn-sm default fileinput-button">
                            <i class="fa fa-cloud-upload"></i>
                            <span>上传模板</span>
                            <input type="file" name="multipartFile" id="uploadModelFile"
                                   accept="*"
                                   multiple="multiple"/>
                             </span>
            <a ng-href="{{'/explore/accidentPlan/downloadModel.json'}}" target="_blank">
                         <span id="btnDown" class="btn btn-sm default fileinput-button">
                            <i class="fa fa-cloud-download"></i>
                            <span>下载模板</span>
                        </span>
            </a>
            <span id="btnUpload" class="btn btn-sm default fileinput-button">
                            <i class="fa fa-cloud-upload"></i>
                            <span>上传</span>
                            <span id="uploadProgress"></span>
                            <input type="file" name="multipartFile" id="uploadEdFile"
                                   accept="*"
                                   multiple="multiple"/>
                             </span>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>名称</th>
                    <th style="width: 90px;">大小</th>
                    <th style="width: 60px;">创建人</th>
                    <th style="width: 130px;">创建时间</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="file in files">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-click="download(file.sysAttach.id);">
                        <img class="cloud-file-type" ng-src="/m/img/filetype/{{file.extensionName}}.png"/>
                        <span ng-bind="file.sysAttach.name"></span>
                    </td>
                    <td ng-bind="file.sizeName"></td>
                    <td ng-bind="file.creatorName"></td>
                    <td ng-bind="file.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <i class="fa fa-download margin-right-5" ng-click="download(file.sysAttach.id);"
                           title="下载"></i>
                        <i class="fa fa-trash" ng-show="file.creator==user.userLogin&&processInstance.firstTask" ng-click="removeEdFile(file);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
<jsp:include page="./print/print-onSecDetail.jsp"/>




