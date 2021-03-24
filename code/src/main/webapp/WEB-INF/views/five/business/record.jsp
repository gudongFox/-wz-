<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="business.contract">经营管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">项目信息备案</span>
        </li>
    </ul>
</div>


<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span ng-bind="tableName">项目信息备案</span>
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                        <i class="fa fa-plus"></i> 创建 </a>

                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="back();">
                        <i class="fa fa-arrow-left"></i> 返回 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;">关键字：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="项目名称|项目号"
                                                                           ng-model="vm.params.qName"></label>
                            <label style="margin-right: 10px;">搜索时间类型：
                                <select  style="height:25px;width: 150px" ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'备案查询时间类型'}:true"
                                         ng-model="vm.params.timeType"></select> </label>
                            <label style="margin-right: 10px;">开始时间：
                                <input type="search" class="form-control input-inline input-sm date-picker" id="startTime"
                                       ng-model="vm.params.startTime" value="{{vm.params.startTime|date:'yyyy-MM-dd HH:mm'}}"></label>
                            <label style="margin-right: 10px;">结束时间：
                                <input type="search"  class="form-control input-inline input-sm date-picker" id="endTime"
                                       ng-model="vm.params.endTime" value="{{vm.params.endTime|date:'yyyy-MM-dd HH:mm'}}"></label>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>
                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table filter class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="min-width: 200px;">项目名称</th>
                                <th style="width: 120px">项目号</th>
                                <th class="hidden-md hidden-sm">客户名称</th>
                                <th style="width: 160px;">部门名称</th>
                             <%--   <th style="width: 100px" title="项目类型">项目类型</th>--%>
                                <th style="width: 60px">发起人</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 150px">流程状态</th>
                                <th style="width: 100px;">使用状态</th>
                                <th style="width: 30px;">对内分包标识</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list" on-finish-render>
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.projectName"  class="data_title"  ng-click="vm.show(item);"><strong ></strong>
                                </td>
                                <td ng-bind="item.projectNo" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.customerName" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.deptName"></td>
                               <%-- <td ng-bind="item.projectType"></td>--%>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <span ng-bind="item.processName"></span>
                                </td>
                                <td>
                                    <strong   ng-if="item.preUse"><span style="color: red" ng-click="vm.opt=1;vm.showPreModal(item.id)">超前任务单</span></strong>
                                    <strong   ng-if="item.reviewUse"><span style="color: green" ng-click="vm.opt=0;vm.showReviewModal(item.id)">合同评审</span></strong>
                                    <strong   ng-if="!item.preUse&&!item.reviewUse"><span style="color: grey">未使用</span></strong>
                                </td>
                                <td class="text-center">
                                    <span class="label label-sm label-success"  ng-if="item.subpackageId!=0" ng-click="vm.showSubpackage(item.subpackageId);">是</span>
                                    <span class="label label-sm label-default" ng-if="item.subpackageId==0">否</span>
                                </td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item);" title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-5" ng-click="vm.remove(item.id);" title="删除"
                                       ng-show="item.creator==user.userLogin&&!item.processEnd&&rightData.selectOpts.indexOf('删除')>=0"></i>
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

<%--合同评审使用情况--%>
<div class="modal fade draggable-modal" id="reviewModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10"><i class="icon-clock"></i>  <span ng-bind="vm.reviews[0].projectName"></span>--合同评审情况</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qContractName"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>合同名称</th>
                            <th style="width: 150px" class="hidden-md hidden-sm">合同号</th>
                            <th style="width: 110px" class="hidden-md hidden-sm">合同额</th>
                            <th style="width: 60px">评审级别</th>
                            <th style="width: 120px;">创建时间</th>
                            <th style="width: 150px">流程状态</th>
                            <th style="width: 60px">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.reviews | filter:{contractName:vm.qContractName}">
                            <td class="dt-right" ng-bind="$index+1"></td>
                            <td ng-bind="item.contractName" class="data_title"  ng-click="vm.showReview(item.id);"></td>
                            <td ng-bind="item.contractNo" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.contractMoney|currency : '￥'" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.reviewLevel"></td>
                            <%--                                <td ng-bind="item.reviewTime"></td>--%>
                            <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                            <td>
                                <span ng-bind="item.processName"></span>
                            </td>
                            <td>
                                <i class="fa fa-pencil margin-right-5" ng-click="vm.showReview(item.id);" title="详情"></i>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveInvoice();">确认</button>
            </div>
        </div>
    </div>
</div>
<%--超前任务单使用情况--%>
<div class="modal fade draggable-modal" id="preModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10"><i class="icon-clock"></i>  <span ng-bind="vm.pres[0].projectName"></span>--超前任务单情况</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qCustomer"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th style="width: 160px;">承接部门</th>
                            <th style="width: 110px;">投资额</th>
                            <th style="width: 80px;">总设计师</th>
                            <th style="width: 80px;">项目经理</th>
                            <th style="width: 80px;">设计任务类型</th>
                            <th style="width: 180px;">任务状态</th>
                            <th style="width: 60px">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.pres">
                            <td class="dt-right" ng-bind="$index+1"></td>

                            <td ng-bind="item.deptName"></td>
                            <td ng-bind="item.investMoney|currency : '￥'"></td>
                            <td ng-bind="item.totalDesignerName"></td>
                            <td ng-bind="item.projectManagerName"></td>
                            <td ng-bind="item.designType"></td>
                            <td ng-bind="item.processName"></td>
                            <td>
                                <i class="fa fa-pencil margin-right-5" ng-click="vm.showPre(item.id);" title="详情"></i>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveInvoice();">确认</button>
            </div>
        </div>
    </div>
</div>
