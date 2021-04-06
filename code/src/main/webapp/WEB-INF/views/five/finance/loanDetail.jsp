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
            <span ng-bind="tableName">借款</span>
            <i class="fa fa-angle-right"></i>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i> <span ng-bind="tableName">借款</span>
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName"
                   style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();"
               style="font-size: 14px;line-height: 1.6" ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator">
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
                     style="min-height: 280px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="row">
                                <div class="col-md-12 col-xl-12">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <div class="panel-title">
                                                <span href="#panelFormInfo" class="panel-toggle" data-toggle="collapse"
                                                      data-parent="#detail_form">
                                                    表单信息
                                                </span>
                                            </div>
                                        </div>
                                        <div class="panel-body" id="panelFormInfo" class="panel-collapse collapse in">
                                            <div class="form-group">
                                                <label class="col-md-2 control-label required">借款人</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control"
                                                               ng-model="vm.item.applicantName" name="applicantName"
                                                               required="true"
                                                               ng-disabled="!processInstance.firstTask"/>
                                                        <span class="input-group-btn">
                                                            <button class="btn default" type="button"
                                                                    ng-click="vm.showUserModel('applicant');"
                                                                    ng-disabled="!processInstance.firstTask"><i
                                                                    class="fa fa-user"></i></button>
                                                        </span>
                                                    </div>
                                                </div>

                                                <label class="col-md-2 control-label required">借款部门</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control"
                                                               ng-model="vm.item.deptName" name="deptName"
                                                               required="true"
                                                               ng-disabled="!processInstance.firstTask"/>
                                                        <span class="input-group-btn">
                                                        <button class="btn default" type="button"
                                                                ng-click="vm.showDeptModal();"
                                                                ng-disabled="!processInstance.firstTask"><i
                                                                class="fa fa-cog"></i></button></span>
                                                    </div>
                                                </div>

                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label required">借款日期</label>
                                                <div class="col-md-4">
                                                    <div class="input-group date date-picker" id="applicantTime">
                                                        <input type="text" class="form-control"
                                                               ng-model="vm.item.applicantTime" name="applicantTime"
                                                               required="true" ng-disabled="!processInstance.firstTask">
                                                        <span class="input-group-btn">
                                                            <button class="btn default" type="button"
                                                                    ng-disabled="!processInstance.firstTask"><i
                                                                    class="fa fa-calendar"></i></button>
                                                        </span>
                                                    </div>
                                                </div>
                                                <label class="col-md-2 control-label required">单据号</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control" ng-model="vm.item.receiptsNumber" readonly
                                                               name="receiptsNumber" required="true" maxlength="20" placeholder="单据号" disabled/>
                                                        <span class="input-group-btn">
                                                          <button class="btn default" type="button"
                                                                  ng-disabled="!processInstance.firstTask"
                                                                  ng-click="vm.getReceiptsNumber();">
                                                            <i class="fa fa-refresh"></i></button>

                                                        </span>
                                                    </div>
                                                </div>

                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label">是否为投标保证金</label>
                                                <div class="col-md-4">
                                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                                            ng-model="vm.item.bid" class="form-control"
                                                            ng-disabled="!processInstance.firstTask"></select>
                                                </div>
                                                <label class="col-md-2 control-label required">是否存在项目</label>
                                                <div class="col-md-4">
                                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                                            ng-model="vm.item.isProject" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                                </div>
                                            </div>
                                            <div class="form-group" ng-if="vm.item.isProject">
                                                <label class="col-md-2 control-label required">项目名称</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control"
                                                               ng-model="vm.item.projectName" name="projectName"  required="true"
                                                               ng-disabled="vm.item.contractLibraryId==0||!processInstance.firstTask"/>
                                                        <span class="input-group-btn">
                                                            <button class="btn default" type="button"
                                                                    ng-click="vm.showSelectPreOrReviewModal()"
                                                                    ng-disabled="!processInstance.firstTask"><i
                                                                    class="fa fa-cog"></i>
                                                            </button>
                                                        </span>
                                                    </div>
                                                </div>
                                                <label class="col-md-2 control-label required">项目经理</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control"
                                                               ng-model="vm.item.businessManagerName" name="businessManagerName" readonly/>
                                                        <span class="input-group-btn">
                                                            <button class="btn default" type="button" ng-click="vm.showUserModel('businessManager');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group" ng-if="vm.item.isProject">
                                                <label class="col-md-2 control-label required">项目类型</label>
                                                <div class="col-md-4">
                                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'五洲项目类型'}:true"
                                                            ng-model="vm.item.projectType" class="form-control"
                                                            ng-disabled="!processInstance.firstTask"></select>
                                                </div>
                                                <label class="col-md-2 control-label required">是否是科研项目</label>
                                                <div class="col-md-4">
                                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                                            ng-model="vm.item.scientific" class="form-control"
                                                            ng-disabled="!processInstance.firstTask"></select>
                                                </div>

                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label required">借款总金额</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <span class="input-group-addon">￥</span>
                                                        <input type="text" class="form-control"
                                                               ng-model="vm.item.totalApplyMoney" name="totalApplyMoney"
                                                              disabled/>
                                                        <span class="input-group-addon">万元</span>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="col-md-2 control-label required">用款事由</label>
                                                <div class="col-md-10">
                                                    <input type="text" class="form-control" ng-model="vm.item.item"
                                                           name="item" required="true"
                                                           ng-disabled="!processInstance.firstTask"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label">借款用途</label>
                                                <div class="col-md-10">
                                                    <textarea rows="3" type="text" class="form-control"
                                                              ng-model="vm.item.loanReason" name="loanReason"
                                                              ng-disabled="!processInstance.firstTask"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label required">流程标题</label>
                                                <div class="col-md-10">
                                                    <input type="text" class="form-control" ng-model="vm.item.title"
                                                           name="title" ng-disabled="!processInstance.firstTask"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label">创建人</label>
                                                <div class="col-md-4">
                                                    <input type="text" class="form-control"
                                                           ng-model="vm.item.creatorName"
                                                           disabled="disabled"/>
                                                </div>
                                                <label class="col-md-2 control-label">创建时间</label>
                                                <div class="col-md-4">
                                                    <input type="text" class="form-control"
                                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}"
                                                           disabled="disabled"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12 col-xl-12">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                             <span href="#panelDetail" class="panel-toggle" data-toggle="collapse"
                                                   data-parent="#detail_form">
                                                      明细信息
                                             </span>
                                            <a href="javascript:;" class="btn btn-sm btn-default"
                                               style="float: right;line-height: 0;" ng-click="vm.showDetailModel(0);"
                                               ng-show="processInstance.firstTask">
                                                <i class="fa fa-plus"></i> 新增 </a>
                                        </div>
                                        <div class="panel-body" id="panelDetail" class="panel-collapse collapse in">
                                            <div class="form-group">
                                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                                    <thead>
                                                    <tr>
                                                        <th style="width: 50px;">序号</th>
                                                        <th >预算类型</th>
                                                        <th style="width: 180px;">预算剩余余额（万元）</th>
                                                        <th style="width: 180px;">借款金额（万元）</th>
                                                        <th>备注</th>
                                                        <th style="width: 60px;">操作</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr ng-repeat="detail in vm.details">
                                                        <td class="dt-right" ng-bind="$index+1"></td>
                                                        <td ng-bind="detail.budgetNo" class="data_title"
                                                            ng-click="vm.showDetailModel(detail.id);"></td>
                                                        <td ng-bind="detail.budgetBalance"></td>
                                                        <td ng-bind="detail.applyMoney"></td>
                                                        <td ng-bind="detail.remark"></td>
                                                        <td>
                                                            <i class="fa fa-pencil margin-right-5"
                                                               ng-click="vm.showDetailModel(detail.id);" title="详情" ng-disabled="!processInstance.firstTask"></i>
                                                            <i class="fa fa-trash-o"
                                                               ng-click="vm.removeDetail(detail.id);" title="删除" ng-if="processInstance.firstTask"></i>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                    <tr>
                                                        <td style="width: 50px;">合计</td>

                                                        <td></td>
                                                        <%--<td ng-bind="vm.item.totalControlBalance"></td>--%>
                                                        <td ng-bind="vm.item.totalBudgetBalance"></td>
                                                        <td ng-bind="vm.item.totalApplyMoney"></td>
                                                        <td></td>
                                                        <td style="width: 60px;"></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12 col-xl-12">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                             <span href="#panelCompanyBank" class="panel-toggle" data-toggle="collapse"
                                                   data-parent="#detail_form">
                                                    付款单位信息
                                             </span>
                                        </div>
                                        <div class="panel-body" id="panelCompanyBank"
                                             class="panel-collapse collapse in">
                                            <div class="form-group">
                                                <label class="col-md-2 control-label">账号名称</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control" ng-model="vm.item.payName"
                                                               name="payName" ng-disabled="!processInstance.firstTask"/>
                                                        <span class="input-group-btn">
                                                               <button class="btn default" type="button" ng-click="vm.showBankSelect(1);" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                                    </span>
                                                    </div>
                                                </div>
                                                <label class="col-md-2 control-label">开户银行</label>
                                                <div class="col-md-4">
                                                    <input type="text" class="form-control"
                                                           ng-model="vm.item.payBank" name="payBank"
                                                           ng-disabled="!processInstance.firstTask"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label">账号</label>
                                                <div class="col-md-4">
                                                    <input type="text" class="form-control" ng-model="vm.item.payAccount"
                                                           name="payAccount" ng-disabled="!processInstance.firstTask"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12 col-xl-12">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                              <span href="#panelPersonalBank" class="panel-toggle"
                                                    data-toggle="collapse" data-parent="#detail_form">
                                                    收款单位信息
                                             </span>
                                        </div>
                                        <div class="panel-body" id="panelPersonalBank"
                                             class="panel-collapse collapse in">
                                            <div class="form-group">
                                                <label class="col-md-2 control-label">收款人</label>
                                                <div class="col-md-4">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control" ng-model="vm.item.receiveName" name="receiveName" ng-disabled="!processInstance.firstTask"/>
                                                        <span class="input-group-btn">
                                                               <button class="btn default" type="button" ng-click="vm.showBankSelect(0);" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                                        </span>
                                                    </div>
                                                </div>
                                                <label class="col-md-2 control-label">开户银行</label>
                                                <div class="col-md-4">
                                                    <input type="text" class="form-control"
                                                           ng-model="vm.item.receiveBank" name="receiveBank"
                                                           ng-disabled="!processInstance.firstTask"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-2 control-label">账号</label>
                                                <div class="col-md-4">
                                                    <input type="text" class="form-control"
                                                           ng-model="vm.item.receiveAccount" name="receiveAccount"
                                                           ng-disabled="!processInstance.firstTask"/>

                                                </div>

                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'" ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">费用明细</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">扣减预算类型</label>
                            <div class="col-md-7">
                                <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.detail.budgetNo"
                                       name="budgetNo" required="true" disabled>
                                <span class="input-group-btn">
                                    <button class="btn default" type="button" ng-click="vm.showPlanTypeModal();"
                                            ng-disabled="!processInstance.firstTask"><i
                                            class="fa fa-cog"></i>
                                    </button>
                                    </span>
                            </div>
                            </div>
                        </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">预算剩余金额</label>
                        <div class="col-md-7">
                            <div class="input-group">
                                <span class="input-group-addon">￥</span>
                                <input type="text" class="form-control" ng-model="vm.detail.budgetBalance"
                                       name="budgetBalance" required="true" disabled>
                                <span class="input-group-addon">万元</span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label">借款申请金额</label>
                        <div class="col-md-7">
                            <div class="input-group">
                                <span class="input-group-addon">￥</span>
                                <input type="text" class="form-control" ng-model="vm.detail.applyMoney"
                                       name="applyMoney"  ng-disabled="!processInstance.firstTask">
                                <span class="input-group-addon">万元</span>
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
<jsp:include page="../print/print-oaMaterialPurchaseDetail.jsp"/>

