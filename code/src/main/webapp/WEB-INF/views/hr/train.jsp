<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="dashboard">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-click="back();" ng-bind="vm.data.template.formCategory"></span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.data.template.formDesc"></span>
        </li>
    </ul>
</div>

<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="{{vm.data.template.formIcon}}"></i> <span>培训批次管理</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row col-md-12">
            <label>
                <input type="search" class="form-control input-inline input-sm" placeholder="关键字"  ng-model="vm.q"></label>
            <a class="btn green btn-sm defaultBtn" ng-click=""><i class="fa fa-search"></i> 查询 </a>
            <a class="btn blue btn-sm" ng-click="vm.newData();"><i class="fa fa-plus"></i> 新增 </a>

        </div>

        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 40px;">#</th>
                    <th ng-repeat="head in vm.data.heads track by $index" ng-bind="head.text" ng-style="head.style"></th>
                    <th style="width: 65px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr role="row"  ng-repeat="item in vm.data.list">
                    <td ng-bind="$index+1"></td>
                    <td ng-repeat="property in item.propertyList  track by $index" ng-bind="property.text" ng-style="property.style"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetail(item.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>