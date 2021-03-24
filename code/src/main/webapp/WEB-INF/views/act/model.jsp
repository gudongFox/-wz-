<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="dashboard">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="act.processInstance">工作流管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>模型定义</span>
        </li>
    </ul>
</div>
<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-file"></i> 数据列表
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <input type="search" class="form-control input-inline input-sm"
                                   placeholder="流程名称"
                                   ng-model="vm.params.qq">

                            <a class="btn green btn-sm" ng-click="vm.queryData();"><i class="fa fa-search"></i> 查询 </a>
                            <a class="btn blue btn-sm"  ng-click="vm.newModel();"><i class="fa fa-plus"></i> 新增 </a>
                        </div>
                    </div>

                    <div class="table-scrollable">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>业务类别</th>
                                <th>模型主键</th>
                                <th>模型名称</th>
                                <th title="保存次数" style="width: 60px;">版本</th>
                                <th title="最后一次部署id" style="width: 80px;">部署</th>
                                <th style="width: 150px;">创建时间</th>
                                <th style="width: 150px;">更新时间</th>
                                <th style="width: 100px;">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr role="row" class="odd" ng-repeat="item in vm.pageInfo.list">
                                <td ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.metaInfo.modelCategory"></td>
                                <td><a ng-href="{{'/act/modelDetail?modelId='+item.id}}" target="_blank"
                                       ng-bind="item.metaInfo.modelKey"></a></td>
                                <td ng-bind="item.name"></td>
                                <td ng-bind="item.revision"></td>
                                <td ng-bind="item.deploymentId"></td>
                                <td ng-bind="item.createTime"></td>
                                <td ng-bind="item.lastUpdateTime"></td>
                                <td>
                                    <span class="kt-badge kt-badge--inline kt-badge--info cursorPointer"
                                          ng-click="vm.deployment(item.id);">部署</span>
                                    <span class="kt-badge kt-badge--inline kt-badge--danger cursorPointer"
                                          ng-click="vm.remove(item.id);">删除</span>
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

