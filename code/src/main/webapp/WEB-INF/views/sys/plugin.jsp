<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>协同设置</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>CAD插件管理</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-grid"></i> <span>CAD插件管理</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                <i class="fa fa-plus"></i> 创建 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-2">
                <input type="text" class="form-control input-sm" placeholder="关键字"
                       ng-model="vm.params.qName"/>
            </div>
            <div class="col-md-4">
                <a class="btn green btn-sm" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
            </div>
        </div>

        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>版本号</th>
                    <th>更新包</th>
                    <th>说明</th>
                    <th style="width: 90px;">大小</th>
                    <th style="width: 50px;">创建人</th>
                    <th style="width: 150px;">创建时间</th>
                    <th style="width: 150px;">修改时间</th>
                    <th style="width: 75px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.versionCode" class="data_title" ng-click="vm.edit(item.id);" title="详情"></td>
                    <td ng-bind="item.zipName"></td>
                    <td ng-bind="item.versionDesc"></td>
                    <td ng-bind="item.sizeName"></td>
                    <td ng-bind="item.creatorName"></td>
                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                    <td ng-bind="item.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.edit(item.id);" title="详情"></i>

                        <i class="fa fa-download margin-right-5" ng-click="download(item.attachId);"
                           title="下载"></i>

                        <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>


<div class="modal fade" id="cpDwgStdModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">数据详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="detail_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">附件文件</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.sysPlugin.zipName"
                                           required="true"
                                           maxlength="20" name="zipName" placeholder="" readonly="true" >
                                    <span class="input-group-addon" style="padding: 0;border: none;">
                                    <span id="btnUpload" class="btn blue fileinput-button">
                            <i class="fa fa-cloud-upload" style="height:15px "></i>
                            <span>上传</span>
                            <input type="file" name="multipartFile" id="uploadFile" data-url="/sys/attach/receive.do"
                                   accept="*"/>
                                   </span>
                                        </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">版本号</label>
                            <div class="col-md-9">

                                <input type="text" class="form-control" ng-model="vm.sysPlugin.versionCode"
                                       required="true"
                                       maxlength="200" name="stdDesc" placeholder="">
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">说明</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.sysPlugin.versionDesc" required="true"
                                   maxlength="200" name="stdDesc" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">创建时间</label>
                        <div class="col-md-9">
                            <div class="input-icon right">
                                <i class="fa fa-clock-o"></i>
                                <input type="text" class="form-control"
                                       value="{{vm.sysPlugin.gmtCreate|date:'yyyy-MM-dd HH:mm'}}"
                                       disabled="disabled">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">修改时间</label>
                        <div class="col-md-9">
                            <div class="input-icon right">
                                <i class="fa fa-clock-o"></i>
                                <input type="text" class="form-control"
                                       value="{{vm.sysPlugin.gmtModified |date:'yyyy-MM-dd HH:mm'}}"
                                       disabled="disabled">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.update();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

