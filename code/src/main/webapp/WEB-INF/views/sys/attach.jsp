<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="sys.role">系统管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>附件列表</span>
        </li>
    </ul>
</div>
<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-file"></i> 附件列表
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="row">
                        <div >
                            <label style="margin-left: 20px;">文件名称：<input type="search"
                                                                          class="form-control input-inline input-sm"
                                                                          placeholder="文件名称"
                                                                          ng-model="vm.params.fileNames"></label>
                            <label style="margin-right: 20px;">创建人：<input type="search"
                                                                          class="form-control input-inline input-sm"
                                                                          placeholder="创建人"
                                                                          ng-model="vm.params.userName"></label>
                            <a class="btn green btn-sm" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
                            <span id="btnUpload" class="btn btn-sm blue fileinput-button">
                            <i class="fa fa-cloud-upload"></i>
                            <span>上传</span>
                            <span id="uploadProgress"></span>
                            <input type="file" name="multipartFile" id="uploadFile" data-url="/sys/attach/receive.do"
                                   accept="*"
                                   multiple="multiple"/>
                             </span>
                        </div>

                    </div>
                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">#</th>
                                <th>文件名称</th>
                                <th>大小</th>
                                <th>创建人</th>
                                <th style="width: 140px;">创建时间</th>
                                <th style="width: 50px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-click="vm.download(item.id);">
                                    <img class="cloud-file-type" ng-src="/m/img/filetype/{{item.extensionName}}.png"  onerror="this.src='/m/img/filetype/file.png'"/>
                                    <span class="font-blue2 data_title" ng-bind="item.name" ></span>
                                </td>
                                <td ng-bind="item.sizeName"></td>
                                <td ng-bind="item.creator"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                                <td>
                                    <i class="fa fa-download mr-5" ng-click="vm.download(item.id);" title="下载"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
                </div>
            </div>
        </div>
    </div>
</div>

<link href="/m/global/plugins/jquery-file-upload/css/jquery.fileupload.css" rel="stylesheet"/>
<link href="/m/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet"/>
<script src="/m/global/plugins/jquery-file-upload/js/vendor/jquery.ui.widget.js"></script>
<script src="/m/global/plugins/jquery-file-upload/js/jquery.iframe-transport.js"></script>
<script src="/m/global/plugins/jquery-file-upload/js/jquery.fileupload.js"></script>
<script src="/m/global/plugins/jquery-file-upload/js/jquery.fileupload-process.js"></script>
<script src="/m/global/plugins/jquery-file-upload/js/jquery.fileupload-validate.js"></script>
