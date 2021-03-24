<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet light" >
    <div class="portlet-title ">
        <div class="caption">
            <i class="fa fa-file"></i> 附件列表
        </div>
        <div class="actions" >
              <span id="btnUpload" class="btn btn-sm default fileinput-button" ng-show="processInstance.myTaskName!=''">
                            <i class="fa fa-cloud-upload"></i>
                            <span>上传</span>
                            <span id="uploadProgress"></span>
                            <input type="file" name="multipartFile" id="uploadEdFile"
                                   accept="*"
                                   multiple="multiple"/>
                             </span>

            <a href="javascript:;" class="btn btn-sm default" ng-show="browserVersion!='ie9'&&processInstance.refresh"
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
                    <th>名称</th>
                    <th style="width: 90px;">大小</th>
                    <th style="width: 60px;">创建人</th>
                    <th style="width: 150px;">创建时间</th>
                    <th style="width: 150px;">修改时间</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="file in files">
                    <td>
                        <input type="checkbox" class="cb_edFile" data-index="{{$index}}" ng-show="browserVersion!='ie9'"/>
                        <span ng-bind="$index+1" ng-show="browserVersion=='ie9'"></span>
                    </td>
                    <td>
                        <img class="cloud-file-type" ng-src="/m/img/filetype/{{file.extensionName}}.png" onerror="this.src='/m/img/filetype/file.png'"/>
                        <span ng-bind="file.fileName" ng-show="browserVersion!='ie9'" class="data_title"
                              ng-click="downloadEdFileWithXml(file);"></span>
                        <a ng-href="{{'/sys/attach/download/'+file.sysAttach.id}}" class="a_title" ng-show="browserVersion=='ie9'" ng-bind="file.fileName" ></a>
                        <span class="label label-sm label-success" ng-show="processInstance.preHandleTime<file.gmtModified">新</span>
                    </td>
                    <td ng-bind="file.sizeName"></td>
                    <td ng-bind="file.creatorName"></td>
                    <td ng-bind="file.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                    <td ng-bind="file.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <i class="fa  fa-cloud margin-right-5" ng-click="showVersion(file.id);"
                           title="历史"></i>
                        <i class="fa fa-trash" ng-show="file.creator==user.userLogin&&processInstance.myTaskName!=''"
                           ng-click="removeEdFile(file);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <p ng-show="browserVersion!='ie9'&&files.length>1&&processInstance.refresh"><span style="cursor: pointer;color: #3598dc;" ng-click="selectAllEdFile();">全选</span><span style="cursor: pointer;color: #3598dc;margin-left: 10px;"  ng-click="cancelAllEdFile();" >取消</span></p>
    </div>
</div>


