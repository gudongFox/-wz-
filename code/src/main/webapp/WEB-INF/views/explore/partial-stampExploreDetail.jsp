<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i> 勘察成果用印申请
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_detail_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">

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
                                <label class="col-md-2 control-label">用印所室、部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName"  name="deptName" readonly/>
                                </div>
                                <label class="col-md-2 control-label">合同金额</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractMoney" name="contractMoney"readonly/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>用印成果类别</strong></label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('用印文件类型')}:true"
                                            ng-model="vm.item.docType" class="form-control"
                                            readonly></select>
                                </div>
                                <label class="col-md-2 control-label required">用印时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="useTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.useTime" name="useTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"  ></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">办公室经办人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.officeUserName" name="officeUserName" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">用印单位经办人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptUserName" name="deptUserName" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>用印明细</strong></label>
                                <div class="col-md-10">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.useStamp"
                                               disabled="disabled" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.showStampModel();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">用印事由</label>
                                <div class="col-md-10">
                                    <textarea class="form-control" ng-model="vm.item.useDescription" rows="3" required="true" name="useDescription"  ng-disabled="!processInstance.firstTask" placeholder="请详细说明,用印事由!" ></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName" disabled="disabled"/>
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
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="modal fade" id="selectStampModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">用印明细</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>用印章</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="useStamp in vm.useStamps">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.useStamp.indexOf(useStamp.code)>=0" class="cb_stamp" data-name="{{useStamp.code}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="useStamp.code"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectStamp();">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="selectValidateBatchModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" >勘察成果文件审校记录</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.qName"/>
                        </div>
                    </div>
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>项目名称</th>
                                <th>文件数量</th>
                                <th style="width: 60px;">创建人</th>
                                <th style="width: 100px;">创建时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="validateBatch in vm.validateBatchs|filter:{majorNames:vm.qName}">
                                <td>
                                    <input type="checkbox" class="cb_validate" ng-checked="validateBatch.selected"  data-name="{{validateBatch}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="validateBatch.projectName"></td>
                                <td ng-bind="validateBatch.fileCount"></td>
                                <td ng-bind="validateBatch.creatorName"></td>
                                <td ng-bind="validateBatch.gmtCreate|date:'yyyy-MM-dd'"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveValidateBatch();">保存</button>
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

            <span href="javascript:;"  class="btn btn-sm default fileinput-button" ng-click="vm.listValidateBatch();">
                <i class="icon-bell"></i> 勘察成果文件 </span>

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
                    <th style="width: 150px;">文件类型</th>
                    <th>文件名称</th>
                    <th style="width: 100px;">大小</th>
                    <th style="width: 100px;">创建人</th>
                    <th style="width: 150px;">创建时间</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="file in files">
                    <td ng-bind="$index+1" ></td>
                    <td ng-bind="file.fileType"></td>
                    <td style="text-align: left;">
                        <img class="cloud-file-type" ng-src="/m/img/filetype/{{file.extensionName}}.png"/>
                     <%--   <span ng-bind="file.fileName" class="font-blue pointer" ng-show="processInstance.firstTask" ng-click="showEdFile(file);"></span>--%>
                        <span ng-bind="file.fileName" ></span>
                    </td>
                    <td ng-bind="file.sizeName"></td>
                    <td ng-bind="file.creatorName"></td>
                    <td ng-bind="file.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <i class="fa fa-download margin-right-5" ng-click="download(file.sysAttach.id);"
                           title="下载"></i>
                        <i class="fa fa-trash" ng-show="processInstance.firstTask"
                           ng-click="removeEdFile(file);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="edFileModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">附件详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">文件类型</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="文件类型"
                                       ng-model="edFile.fileType" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">文件名称</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" placeholder="办理人"
                                       ng-model="edFile.sysAttach.name" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">文件大小</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       ng-model="edFile.sizeName" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">创建人</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       ng-model="edFile.creatorName" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">创建时间</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       value="{{edFile.gmtCreate | date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-4 control-label">修改时间</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control"
                                       value="{{edFile.gmtModified | date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="saveEdFile();" ng-show="processInstance.firstTask">保存</button>
            </div>
        </div>
    </div>
</div>


<jsp:include page="print/print-stampDetail.jsp"/>




