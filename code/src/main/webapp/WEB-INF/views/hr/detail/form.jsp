<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="portlet  box blue" ng-keydown="vm.keyDown();">
    <div class="portlet-title">
        <div class="caption">
            <i class="{{vm.template.formIcon}}"></i> <span ng-bind="vm.template.formDesc"></span>
        </div>
        <div class="actions">

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.newData();">
                <i class="fa fa-plus"></i> 新增 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.queryData();">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.exportData();">
                <i class="fa fa-file-excel-o"></i> 导出数据 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.back();" ng-show="!vm.onlyMe">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="row">
            <div class="col-md-12">

                <label style="margin-left: 10px;" ng-show="!vm.onlyMe">工号：<input type="text"
                                                             class="form-control input-inline input-sm ng-pristine ng-untouched ng-valid ng-not-empty"
                                                             placeholder="工号" ng-model="vm.params.qOwner"></label>

                <label style="margin-left: 10px;">关键词：<input type="text"
                                                              class="form-control input-inline input-sm ng-pristine ng-untouched ng-valid ng-not-empty"
                                                              placeholder="关键词" ng-model="vm.params.qq"></label>

                <a class="btn green btn-sm" ng-click="vm.queryData();" style="margin-left: 10px;"><i class="fa fa-search"></i> 查询 </a>

            </div>
        </div>

        <div class="dataTables_wrapper no-footer">
            <div class="table-scrollable">
                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 30px;">#</th>
                        <th ng-repeat="head in vm.heads track by $index" ng-bind="head.text" ng-style="head.style"></th>
                        <th style="width: 70px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr role="row" class="odd" ng-repeat="item in vm.pageInfo.list">
                        <td ng-bind="$index+1"></td>
                        <td ng-repeat="property in item.propertyList  track by $index" ng-bind="property.text"
                            ng-style="property.style"></td>
                        <td>
                            <i class="fa fa-edit" style="margin-left: 5px;cursor: pointer;" title="编辑"
                               ng-click="vm.detail(item);"/>
                            <i class="fa fa-trash" style="margin-left: 5px;cursor: pointer;" title="删除"
                               ng-click="vm.remove(item.id);" ng-show="!item.processEnd"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
        </div>
    </div>
</div>


