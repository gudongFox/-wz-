<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<span ng-controller="CloudDirAndFileController" >
    <link rel="stylesheet" href="/assets/jquery-file-upload/css/jquery.fileupload.css" type="text/css"/>
    <link rel="stylesheet" href="/assets/jquery-file-upload/css/jquery.fileupload-ui.css" type="text/css"/>

    <style type="text/css">
        input[type=checkbox] {
            width: 15px;
            height: 15px;
        }
    </style>

    <div class="portlet light">
        <div class="portlet-title " style="border-bottom:1px solid #eee;">
            <div class="caption">
                <i class="fa fa-file"></i>
                <span class="caption-subject" >业务附件</span>
                <span class="caption-helper" ng-bind="tplConfig.businessFileTip"></span>
            </div>
            <div class="actions">
            </div>
        </div>
        <div class="portlet-body" style="padding-top: 25px;">
            <div class="page-bar">
				<ul class="page-breadcrumb" ng-show="breadcrumbs.length>1">
					<li ng-repeat="breadcrumb in breadcrumbs" ng-click="loadData(breadcrumb.id);">
						<a ng-bind="breadcrumb.text"></a>
						<i class="fa fa-angle-right" ng-if="$index!==breadcrumbs.length-1"></i>
					</li>
				</ul>
				<div class="page-toolbar">
					<div class="pull-right">

                         <div class="btn-group">
                            <button type="button" class="btn btn-default"
                                    ng-click="downloadMore();"><i
                                    class="fa fa-download"></i> 下载</button>
                            <button type="button" class="btn btn-default"
                                    ng-click="removeMore();" ng-show="tplConfig.saveAble"><i
                                    class="fa fa-trash"></i> 删除</button>
                             <div class="btn-group" ng-show="tplConfig.saveAble">
                                <button type="button"
                                        class="btn btn-default dropdown-toggle"
                                        data-toggle="dropdown" aria-expanded="false">
                                <i class="fa fa-ellipsis-horizontal"></i> 上传 <i class="fa fa-angle-down"></i>
                                </button>
                                <ul class="dropdown-menu">
                                    <li>
                                         <span class="btn  default fileinput-button" style="width: 98%;padding: 5px;">
                                                <span>上传文件</span>
                                                <input id="uploadFile" type="file" name="file" multiple accept="*"/>
                                          </span>
                                    </li>
                                </ul>
                            </div>
                        </div>
					</div>
				</div>
			</div>

            <div class="row">
                        <div class="col-md-12">
                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 30px;">
                                        <label for="cb_all"></label><input type="checkbox" id="cb_all"/>
                                    </th>
                                    <th>文件名</th>
                                    <th style="width: 100px;" ng-show="tplConfig.showFileType"
                                        class="hidden-sm">文件类型</th>
                                    <th style="width: 80px;">上传人</th>
                                    <th style="width: 170px;" class="hidden-sm">修改时间</th>
                                    <th style="width: 100px;">大小</th>
                                    <th style="width: 100px;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="dir in dirs">
                                    <td>
                                        <div class="icheck-list">
                                             <label>
                                                <input type="checkbox" class="cbDirOrFile" ata-id="file_{{dir.id}}">
                                             </label>
                                        </div>
                                    </td>
                                    <td style="cursor: pointer;word-break:break-all;"
                                        ng-click="loadData(dir.id);">
                                        <img ng-src="{{'/assets/img/dir.png'}}" style="width: 25px;height: 25px;"
                                             alt="">
                                        <span ng-bind="dir.cnName"></span>
                                    </td>
                                    <td ng-show="tplConfig.showFileType" class="hidden-sm"></td>
                                    <td ng-bind="dir.creatorName"></td>
                                    <td ng-bind="dir.gmtModified|date:'yyyy-MM-dd HH:mm'" class="hidden-sm"></td>
                                    <td ng-bind="dir.sizeName"></td>
                                    <td>
                                        <i class="fa fa-download" style="margin-left: 5px;cursor: pointer;" title="下载" ng-click="downloadDir(dir.id);"/>
                                        <i class="fa fa-edit" style="margin-left: 5px;cursor: pointer;" title="编辑" ng-show="tplConfig.editable" ng-click="showDir(dir.id);"/>
                                        <i class="fa fa-trash" style="margin-left: 5px;cursor: pointer;" title="删除" ng-click="removeDir(dir.id);" ng-show="tplConfig.editable&&dir.creator==user.enLogin"/>
                                    </td>
                                </tr>
                                <tr ng-repeat="file in files">
                                    <td>
                                        <label>
                                           <input type="checkbox" class="cbDirOrFile" data-id="file_{{file.id}}">
                                        </label>
                                    </td>
                                    <td style="word-break:break-all;">
                                        <img ng-src="{{'/assets/img/'+file.commonAttach.extensionName+'.png'}}"
                                             onerror="this.src='/assets/img/doc.png'"
                                             style="width: 25px;height: 25px;" alt="">
                                        <span style="font-size: 10px;margin-right: 5px;"></span>
                                        <span ng-bind="file.fileName"></span>
                                    </td>
                                    <td ng-show="tplConfig.showFileType" ng-bind="file.fileType" class="hidden-sm"></td>
                                    <td ng-bind="file.creatorName"></td>
                                    <td class="hidden-sm">
                                        <span ng-bind="file.gmtModified|date:'yyyy-MM-dd HH:mm'"
                                              style="{{file.commonFileProperty.signAttachId>0?'color:green;font-weight:bold;':''}}"></span>
                                    </td>
                                    <td ng-bind="file.commonAttach.sizeName"></td>
                                    <td>
                                        <i class="fa fa-download" style="margin-left: 5px;cursor: pointer;" title="下载" ng-click="downloadFile(file.id);"/>
                                        <i class="fa fa-edit" style="margin-left: 5px;cursor: pointer;" title="编辑" ng-show="tplConfig.editable" ng-click="showFile(file.id);"/>
                                        <i class="fa fa-trash" style="margin-left: 5px;cursor: pointer;" title="删除" ng-click="removeFile(file.id);" ng-show="tplConfig.editable&&file.lockedLogin==user.enLogin"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

        </div>
    </div>


    <div class="modal" id="commonFileModal">
        <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" ng-bind="item.fileName"></h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body" style="padding-top: 5px;">
                 <div class="tabbable-line">
                  <ul class="nav nav-tabs">
                     <li class="active">
                            <a class="active" data-toggle="tab" href="#tab_file_detail" role="tab">文件详情</a>
                     </li>
                      <li>
                         <a data-toggle="tab" href="#tab_file_history" role="tab">历史记录</a>
                      </li>
                  </ul>

                  <div class="tab-content">
                      <div class="tab-pane active" id="tab_file_detail" role="tabpanel"
                           style="height: 300px;overflow-y: auto;padding-left: 15px;padding-right: 15px;">

                                      <div class="form-group" style="margin-bottom: 0.5rem;">
                                        <label>文件名称<span style="color: red;margin-left: 2px;">*</span></label>
                                        <input type="text" class="form-control" ng-model="item.fileName"
                                               placeholder="请输入名称">
                                      </div>

                                     <div class="row">
                                      <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>文件类型<span style="color: red;margin-left: 2px;">*</span></label>
                                          <select class="form-control" ng-options="s.code as s.name for s in fileTypes"
                                                  ng-model="item.fileType"></select>

                                      </div>


                                     <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>文件排序<span style="color: red;margin-left: 2px;">*</span></label>
                                        <input type="number" class="form-control" ng-model="item.seq"
                                               placeholder="请输入名称">
                                      </div>

                                    </div>
                                    <div class="form-group" style="margin-bottom: 0.5rem;"
                                         ng-show="item.commonFileProperty.signAttachId">
                                        <label ng-bind="'专家打码('+item.commonFileProperty.signUserName+')'">专家签章<span
                                                style="color: red;margin-left: 2px;">*</span></label>
                                          <div class="input-group">
                                              <input type="text" class="form-control"
                                                     ng-value="item.commonFileProperty.signTime|date:'yyyy-MM-dd HH:mm:ss'"
                                                     disabled="disabled"/>
                                              <div class="input-group-append"
                                                   ng-click="downloadAttach(item.commonFileProperty.signAttachId);"><span
                                                      class="input-group-text"><i
                                                      class="la la-download"></i></span></div>
											</div>
                                      </div>

                          <div class="row">
                                     <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>创建人</label>
                                        <input type="text" class="form-control" ng-model="item.creatorName"
                                               disabled="disabled">
                                      </div>
                                     <div class="form-group col-md-6" style="margin-bottom: 0.5rem;">
                                        <label>创建时间</label>
                                        <input type="text" class="form-control"
                                               ng-value="item.gmtCreate|date:'yyyy-MM-dd HH:mm:ss'" disabled="disabled">
                                      </div>
                                </div>
                                   <div class="form-group" style="margin-bottom: 0.5rem;">
                                        <label>备注</label>
                                        <input type="text" class="form-control" ng-model="item.remark"
                                               placeholder="">
                                      </div>
                       </div>
                       <div class="tab-pane" id="tab_file_history" role="tabpanel"
                            style="height: 300px;overflow-y: auto;">

                              <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 200px;">上传时间</th>
                                <th style="width: 90px;">上传人</th>
                                <th style="width: 120px;">文件大小</th>
                                <th>备注</th>
                                <th style="width: 80px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr role="row" ng-repeat="history in historyList">
                                <td ng-bind="history.gmtCreate|date:'yyyy-MM-dd HH:mm:ss'"></td>
                                <td ng-bind="history.creator"></td>
                                <td ng-bind="history.sizeName"></td>
                                <td ng-bind="history.remark"></td>
                                <td>
                                     <i class="fa fa-download" style="margin-left: 5px;cursor: pointer;" title="下载"
                                        ng-click="downloadAttach(history.id);"/>

                                    <i class="fa fa-trash" style="margin-left: 5px;cursor: pointer;" title="删除"
                                       ng-click="removeHistory(history.id);" ng-show="history.creator==user.enLogin"/>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                       </div>
                     </div>
                 </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" ng-show="tplConfig.editable&&item.creator==user.enLogin"
                        ng-click="saveFile(item);">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
    </div>
    <div class="modal" id="commonDirModal">
        <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">文件夹详情</h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body" style="padding-bottom: 5px;padding-top: 10px;">
                 <div class="form-group" style="margin-bottom: 0.5rem;">
                                        <label>文件名称<span style="color: red;margin-left: 2px;">*</span></label>
                                        <input type="text" class="form-control" ng-model="item.cnName"
                                               placeholder="请输入名称">
                                      </div>
                 <div class="form-group" style="margin-bottom: 0.5rem;">
                                        <label>文件排序<span style="color: red;margin-left: 2px;">*</span></label>
                                        <input type="number" class="form-control" ng-model="item.seq"
                                               placeholder="">
                                      </div>
                 <div class="form-group" style="margin-bottom: 0.5rem;" ng-if="item.buildId">
                                        <label>子项名称<span style="color: red;margin-left: 2px;">*</span></label>
                                        <input type="number" class="form-control" ng-model="item.buildId"
                                               placeholder="">
                  </div>
                  <div class="form-group" style="margin-bottom: 0.5rem;" ng-if="item.buildId">
                                        <label>专业名称<span style="color: red;margin-left: 2px;">*</span></label>
                                        <input type="number" class="form-control" ng-model="item.majorName"
                                               placeholder="">
                  </div>
                 <div class="form-group" style="margin-bottom: 0.5rem;">
                                        <label>创建人</label>
                                        <input type="text" class="form-control" ng-model="item.creatorName"
                                               disabled="disabled">
                                      </div>
                 <div class="form-group" style="margin-bottom: 0.5rem;">
                                        <label>创建时间</label>
                                        <input type="text" class="form-control"
                                               ng-value="item.gmtCreate|date:'yyyy-MM-dd HH:mm:ss'" disabled="disabled">
                                      </div>
                 <div class="form-group" style="margin-bottom: 0.5rem;">
                                        <label>备注</label>
                                        <input type="text" class="form-control" ng-model="item.remark"
                                               placeholder="">
                                      </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" ng-show="tplConfig.editable&&item.creator==user.enLogin"
                        ng-click="saveDir(item);">保存</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
    </div>

    <script src="/assets/jquery-file-upload/js/vendor/jquery.ui.widget.js"></script>
    <script src="/assets/jquery-file-upload/js/jquery.iframe-transport.js"></script>
    <script src="/assets/jquery-file-upload/js/jquery.fileupload.js"></script>
    <script src="/assets/jquery-file-upload/js/jquery.fileupload-process.js"></script>
    <script src="/assets/jquery-file-upload/js/jquery.fileupload-validate.js"></script>
</span>