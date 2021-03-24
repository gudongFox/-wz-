<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">财务管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>部门资金管理</span>
        </li>
    </ul>
</div>


<div class="row">
    <div class="col-md-3" style="padding-right: 0px;">
        <div class="portlet box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-tree"></i> 部门资金管理
                </div>
                <div class="tools">
                    <i class="fa fa-refresh margin-right-5" title="刷新" ng-click="vm.refreshDept();"></i>
                </div>
            </div>
            <div class="portlet-body" style="max-height:800px;overflow-x: auto;overflow-y:auto;">
                <div id="dept_tree" ></div>
            </div>
        </div>
    </div>

    <div class="col-md-9">
        <div class="portlet  box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-tree"></i> 部门资金详情
                </div>
                <ul class="nav nav-tabs">
                    <li class="active" id="infoTab">
                        <a href="#info_tab" data-toggle="tab" aria-expanded="true">
                            基础信息</a>
                    </li>
                </ul>
            </div>
            <div class="portlet-body">
                <div class="tab-content">
                    <div class="tab-pane active" id="info_tab">
                        <form class="form-horizontal form" role="form" id="detail_form">
                            <div class="form-body">
                                <div class="form-group">
                                    <label class="col-md-2 control-label">部门名称</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true" disabled="disabled" />
                                    </div>
                                    <label class="col-md-2 control-label">部门预算</label>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.deptBudgetMoney" name="deptBudgetMoney"  readonly />
                                            <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptBudget(vm.item.deptBudgetId);" ><i class="fa fa-cog" ></i></button>
                                         </span>
                                        </div>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">总金额（万元）</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control" ng-model="vm.item.totalMoney" name="totalMoney" required="true" disabled="disabled" />
                                    </div>
                                    <label class="col-md-2 control-label">使用金额（万元）</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control" ng-model="vm.item.subMoneyTotal" name="subMoney" required="true" disabled="disabled" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">剩余金额（万元）</label>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.nowMoney" name="nowMoney"  readonly />
                                            <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.refreshMoney(vm.item.deptId);" ><i class="fa fa-refresh" ></i></button>
                                         </span>
                                        </div>
                                    </div>
                                    <label class="col-md-2 control-label">更新时间</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control"
                                               value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">备注</label>
                                    <div class="col-md-10">
                                        <input type="text" class="form-control" ng-model="vm.item.remark" disabled="disabled"/>
                                    </div>
                                </div>
                            </div>
                           <%-- <div class="form-actions right" style="padding: 10px;">
                                <button type="button" class="btn blue btn-sm" ng-click="vm.update();" ng-disabled="vm.item.parentId==0">保存</button>
                                <button type="button" class="btn default btn-sm" ng-click="vm.loadDept();" ng-disabled="vm.item.parentId==0">重置</button>
                            </div>--%>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="portlet light">
            <div class="portlet-title">
                <div class="caption">
                    <i class="fa fa-file"></i> 资金使用详情
                </div>
            </div>
            <div class="portlet-body">
                <div class="row">
                    <div >
                        <label style="margin-left: 20px;">使用类型：
                            <select class="form-control input-inline input-sm" ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('部门资金使用类型')}:true"
                                    ng-model="vm.item.queryType" ></select>
                        </label>
                    </div>
                </div>
                <div class="table-scrollable">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>使用类型</th>
                            <th>使用金额</th>
                            <th>当前状态</th>
                            <th style="width: 50px;">创建人</th>
                            <th style="width: 100px;">创建时间</th>
                            <th style="width: 50px;">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.item.details | filter:{type:vm.queryType}:true">
                            <td class="dt-right" ng-bind="$index+1"></td>
                            <td ng-bind="item.type" style="color: blue" ng-click="vm.show(item.url,item.urlIdName,item.urlId);"></td>
                            <td ng-bind="item.money"></td>
                            <td ng-bind="item.processName"></td>
                            <td ng-bind="item.creator"></td>
                            <td ng-bind="item.gmtModified | date:'yyyy-MM-dd'"></td>
                            <td>
                                <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.url,item.urlIdName,item.urlId);" title="详情"></i>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
