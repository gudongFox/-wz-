<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 附件列表 <span style="color: red;font-size: 8px" ng-bind="vm.fileRemark"></span>
        </div>
        <div class="actions" >
              <span id="btnUpload" class="btn btn-sm default fileinput-button" ng-show="processInstance.firstTask">
                            <i class="fa fa-cloud-upload"></i>
                            <span>上传</span>
                            <span id="uploadProgress"></span>
                            <input type="file" name="multipartFile" id="uploadEdFile"
                                   accept="*"
                                   multiple="multiple"/>
                             </span>

            <a href="javascript:;" class="btn btn-sm default" ng-show="browserVersion!='ie9'"
               ng-click="batchDownloadEdFile();">
                <i class="fa fa-download" title="批量下载"></i> 下载 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="width: 150px;">文件类型</th>
                    <th>文件名称</th>
                    <th style="width: 100px;">大小</th>
                    <th style="width: 100px;">创建人</th>
                    <th style="width: 150px;">创建时间</th>
                    <th style="width: 75px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="file in files">
                    <td>
                        <input type="checkbox" class="cb_edFile" data-index="{{$index}}" ng-show="browserVersion!='ie9'"/>
                        <span ng-bind="$index+1" ng-show="browserVersion=='ie9'"></span>
                    </td>
                    <td>
                        <span ng-bind="file.fileType" class="data_title" ng-show="processInstance.firstTask" ng-click="showEdFile(file);"></span>
                        <span ng-bind="file.fileType" ng-show="!processInstance.firstTask"></span>
                    </td>
                    <td>
                        <img class="cloud-file-type" ng-src="/m/img/filetype/{{file.extensionName}}.png" onerror="this.src='/m/img/filetype/file.png'"/>
                        <span ng-bind="file.fileName" ng-show="browserVersion!='ie9'" class="data_title"
                              ng-click="download(file.sysAttach.id);"></span>
                        <a ng-href="{{'/sys/attach/download/'+file.sysAttach.id}}" ng-show="browserVersion=='ie9'" ng-bind="file.fileName" class="a_title"></a>
                        <span class="label label-sm label-success" ng-show="processInstance.preHandleTime<file.gmtModified">新</span>
                    </td>

                    <td ng-bind="file.sizeName"></td>
                    <td ng-bind="file.creatorName"></td>
                    <td ng-bind="file.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <i class="fa fa-download margin-right-5" ng-click="downloadFileWithXml(file.id);"
                           title="下载" ng-show="browserVersion!='ie9'"></i>
                        <i class="fa  fa-cloud margin-right-5" ng-click="showVersion(file.id);"
                           title="历史"></i>
                        <i class="fa fa-trash"  ng-show="file.creator==user.userLogin&&processInstance.myTaskName!=''"
                           ng-click="removeEdFile(file);" title="删除"></i>
                    </td>
                </tr>

                </tbody>
            </table>
        </div>
        <p ng-show="browserVersion!='ie9'&&files.length>1" ><span style="cursor: pointer;color: #3598dc;" ng-click="selectAllEdFile();">全选</span><span style="cursor: pointer;color: #3598dc;margin-left: 10px;" ng-click="cancelAllEdFile();">取消</span></p>
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
                            <label class="col-md-4 control-label">文件类别</label>
                            <div class="col-md-7">
                                <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'设计文件归档类别'}:true"
                                        ng-model="edFile.fileType" class="form-control"
                                        style="height:35px;"></select>
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
