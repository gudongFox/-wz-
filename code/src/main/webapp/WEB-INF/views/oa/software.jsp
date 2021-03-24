
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">技术质量</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>软件系统管理台账</span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>  <span>软件系统管理台账</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                <i class="fa fa-plus"></i> 新建 </a>

        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div >
                <label style="margin-left: 20px;">设备名称：<input type="search" class="form-control input-inline input-sm" placeholder="设备名称" ng-model="vm.pageInfo.equipmentName"></label>
                <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>

                <a class="btn btn-sm green" href="/assets/doc/导出模板/软件系统管理台账.xls" target="_blank"><i class="fa fa-cloud-download"></i> 下载模板 </a>

                <span id="btnUpload" class="btn btn-sm blue fileinput-button">
                            <i class="fa fa-cloud-upload"></i>
                            <span>上传数据</span>
                            <span id="uploadProgress"></span>
                            <input type="file" name="multipartFile" id="uploadModelFile"
                                   accept="*"
                                   multiple="multiple"/>
                             </span>
            </div>
        </div>

        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>软件/系统名称</th>
                    <th>软件系统类别</th>
                    <th>生产开发单位</th>
                    <th>主要功能说明</th>
                    <th>数量</th>
                    <th>价格</th>
                    <th style="width: 50px;">创建人</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 50px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in  vm.pageInfo.list">
                    <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                    <td ng-bind="item.softwareName" style="color: blue" ng-click="vm.show(item.id);"></td>
                    <td ng-bind="item.softwareType"></td>
                    <td ng-bind="item.developDept"></td>
                    <td ng-bind="item.funcationContent"></td>
                    <td ng-bind="item.number"></td>
                    <td ng-bind="item.price"></td>
                    <td ng-bind="item.creatorName"></td>
                    <td ng-bind="item.gmtModified | date:'yyyy-MM-dd'"></td>

                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ng-show="item.creator==user.userLogin"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>

        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>

