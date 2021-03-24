
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 附件列表
        </div>
        <div class="actions" ng-show="processInstance.firstTask">
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
                        <span ng-bind="file.fileName"></span>
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
</div>

