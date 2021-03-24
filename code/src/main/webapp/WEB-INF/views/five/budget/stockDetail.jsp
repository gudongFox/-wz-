<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:62})">财务管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>股权投资预算</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.deptName"></span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> 股权投资预算
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator">
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>

    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_detail_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 180px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">填报部门</label>
                                <div class="col-md-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                    <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                </div>
                                </div>
                                <label class="col-md-2 control-label">预算总金额</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control" ng-model="vm.item.totalBudgetMoney" name="totalBudgetMoney" readonly/>
                                        <span class="input-group-addon">万元</span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">预算年份</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker-year" id="budgetYear">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.budgetYear"   required="true" disabled="disabled">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">流程标题</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.title" name="title" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 180px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 180px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i><span ng-bind="vm.item.budgetYear+' 预算明细'">
            <span style="padding-left:15px;font-size: 14px;color: red">总计金额:&nbsp;<span style="color:black" ng-bind="vm.item.totalBudgetMoney"/>&nbsp;&nbsp;&nbsp;</span>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0);"
               ng-show="processInstance.firstTask">
                <i class="fa fa-cog"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th>上年项目名称</th>
                    <th>上年投资金额</th>
                    <th>上年预计完成</th>
                    <th>本年项目名称</th>
                    <th>本年预计资金</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.lastYearSystemName" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.lastYearMoney" ></td>
                    <td ng-bind="detail.lastYearSuccess" ></td>
                    <td ng-bind="detail.systemName" ></td>
                    <td ng-bind="detail.budgetMoney"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" ng-show="processInstance.firstTask" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">预算明细</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
<%--                        <div class="form-group">
                            <label class="col-md-4 control-label required">所属部门</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                    <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                    </span>
                                </div>
                            </div>
                        </div>--%>
    <div class="form-group">
        <label class="col-md-3 control-label required">上年项目名称</label>
        <div class="col-md-9">
            <input type="text" class="form-control" ng-model="vm.detail.lastYearSystemName" name="lastYearSystemName" required="true" ng-disabled="!processInstance.firstTask">
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-3 control-label required">上年投资金额</label>
        <div class="col-md-9">
            <div class="input-group">
                <span class="input-group-addon">￥</span>
                <input type="text" class="form-control" ng-model="vm.detail.lastYearMoney" ng-disabled="!processInstance.firstTask||vm.detail.isParent">
                <span class="input-group-addon">万元</span>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-3 control-label required">上年预计完成</label>
        <div class="col-md-9">
            <div class="input-group">
                <span class="input-group-addon">￥</span>
                <input type="text" class="form-control"  ng-model="vm.detail.lastYearSuccess" ng-disabled="!processInstance.firstTask||vm.detail.isParent">
                <span class="input-group-addon">万元</span>
            </div>
        </div>
    </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">本年项目名称</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" placeholder="{{vm.detail.lastYearSystemName}}" ng-model="vm.detail.systemName" name="systemName" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label required">本年预算资金</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <span class="input-group-addon">￥</span>
                                    <input type="text" class="form-control"  ng-model="vm.detail.budgetMoney" ng-disabled="!processInstance.firstTask||vm.detail.isParent">
                                    <span class="input-group-addon">万元</span>
                                </div>
                            </div>
                        </div>

                    </div>

            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();">保存</button>
            </div>
                </form>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
    </div>
</div>

<jsp:include page="../print/print-oaMaterialPurchaseDetail.jsp"/>


        
