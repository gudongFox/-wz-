<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>人资管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>设计资质备案</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-database"></i> 设计资质备案
                </div>
                <div class="actions">

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click=" vm.init();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();">
                        <i class="fa fa-plus"></i> 创建 </a>
                </div>            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="row">
                        <div class="col-md-12">

                            <label style="margin-right: 10px;">用户名称：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="用户名称"
                                                                           ng-model="vm.params.q"></label>



                            <a class="btn green btn-sm" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>

<%--                            <a class="btn btn-sm green" href="/assets/doc/导出模板/设计资质备案.xls" target="_blank"><i class="fa fa-cloud-download"></i> 下载模板 </a>--%>

<%--                            <span id="btnUpload" class="btn btn-sm blue fileinput-button">--%>
<%--                            <i class="fa fa-cloud-upload"></i>--%>
<%--                            <span>上传数据</span>--%>
<%--                            <span id="uploadProgress"></span>--%>
<%--                            <input type="file" name="multipartFile" id="uploadModelFile"--%>
<%--                                   accept="*"--%>
<%--                                   multiple="multiple"/>--%>
<%--                             </span>--%>
                        </div>
                    </div>

                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>姓名</th>
                                <th>所在省份</th>
                                <th>所在城市</th>
                                <th>专业</th>
                                <th style="width: 140px;">创建时间</th>
                                <th style="width: 140px;">修改时间</th>
                                <th style="width: 50px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list">
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.userName" class="data_title" ng-click="vm.show(item.id);"></td>
                                <td ng-bind="item.province"></td>
                                <td ng-bind="item.city"></td>
                                <td ng-bind="item.specialty"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>
                                <td ng-bind="item.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                                    <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" ></i>
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




