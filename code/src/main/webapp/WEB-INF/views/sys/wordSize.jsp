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
            <span>文号管理</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> 文号管理
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.init();">
                <i class="fa fa-refresh"></i> 刷新 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                <i class="fa fa-plus" ></i> 创建 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-12">

            </div>
        </div>
        <div class="dataTables_wrapper no-footer">
            <div class="table-scrollable">
                <table filter class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 35px;">#</th>
                        <th style="min-width:180px;">字</th>
                        <th style="min-width: 200px;">号</th>
                        <th style="min-width: 80px;">年份</th>
                        <th style="min-width: 80px;">类型</th>
                        <th style="width: 60px;">跳号</th>
                        <th style="min-width: 200px;">部门</th>
                        <th style="min-width: 80px;">备注</th>
                        <th style="width: 100px;">创建时间</th>
                        <th style="width: 60px;">操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.pageInfo.list" on-finish-render>
                        <td class="dt-right" ng-bind="$index+1"></td>
                        <td ng-bind="item.word" class="data_title" ng-click="vm.showItemModel(item.id);"></td>
                        <td ng-bind="item.mark"></td>
                        <td ng-bind="item.year" class="hidden-md hidden-sm"></td>
                        <td ng-bind="item.type"></td>
                        <td ng-bind="item.abandonMark"></td>
                        <td ng-bind="item.deptName"></td>
                        <td ng-bind="item.remark"></td>
                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                        <td>
                            <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id);" title="详情"></i>
                            <i class="fa fa-trash-o" ng-click="vm.remove(item.id);" title="删除" <%--ng-show="rightData.selectOpts.indexOf('删除')>=0&&item.creator==user.userLogin"--%>></i>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <my-pager data-page-info="vm.pageInfo" on-load="vm.loadPagedData()"></my-pager>
    </div>
</div>

<div class="modal fade" id="itemModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">文号明细</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">字</label>
                            <div class="col-md-7">
                                <div class="input-group " style="width: 100%;">
                                    <input type="text" class="form-control" ng-model="vm.item.word"
                                           name="words"  ng-disabled="!processInstance.firstTask">
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">发文类型</label>
                            <div class="col-md-7">
                                <div class="input-group" style="width: 100%;">
                                    <input type="text"  class="form-control" ng-model="vm.item.type"
                                           name="type" >
                                    <%--<span class="input-group-btn">
                                    <button class="btn default" type="button" ng-click="vm.showPlanTypeModal();"
                                            ng-disabled="!processInstance.firstTask"><i
                                            class="fa fa-cog"></i>
                                    </button>
                                    </span>--%>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label">部门</label>
                            <div class="col-md-7">
                                <div class="input-group" style="width: 100%;">
                                    <input type="text"  class="form-control" ng-model="vm.item.deptName"
                                           name="deptName"  >
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>