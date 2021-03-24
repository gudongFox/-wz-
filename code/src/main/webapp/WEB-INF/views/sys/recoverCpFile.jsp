<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>系统管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>协同附件恢复</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> <span>协同附件恢复</span>
        </div>
        <div class="actions">

        </div>
    </div>
    <div class="portlet-body">
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
                <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
            </div>

        </div>

        <div class="table-scrollable" >
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>名称</th>
                    <th>路径</th>
                    <th style="width: 90px;">大小</th>
                    <th style="width: 60px;">创建人</th>
                    <th style="width: 130px;">创建时间</th>
                    <th style="width: 130px;">修改时间</th>
                    <th style="width: 30px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="file in vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td>
                        <img class="cloud-file-type" ng-src="/m/img/filetype/{{file.extensionName}}.png" onerror="this.src='/m/img/filetype/file.png'"/>
                        <span ng-bind="file.name" ng-show="browserVersion!='ie9'" class="data_title"
                              ng-click="download(file.attachId);"></span>
                        <a ng-href="{{'/sys/attach/download/'+file.attachId}}" class="a_title" ng-show="browserVersion=='ie9'" ng-bind="file.name" ></a>
                    </td>
                    <td ng-bind="file.showDirPath"></td>
                    <td ng-bind="file.sizeName"></td>
                    <td ng-bind="file.creatorName"></td>
                    <td ng-bind="file.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                    <td ng-bind="file.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                    <td>
                        <i class="fa fa-recycle margin-right-5" style="color: green;" ng-click="vm.recoverFile(file.id);" title="恢复"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>

