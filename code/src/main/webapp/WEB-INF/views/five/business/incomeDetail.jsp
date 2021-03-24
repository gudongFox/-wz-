<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>经营管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">合同收费</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.contractName"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">合同收费</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();">
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div  class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">合同名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractName"
                                               name="contractName" required="true"
                                               readonly/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showLibraryModal();"
                                                    ng-disabled="!processInstance.firstTask||vm.item.invoiceId!=0"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="!!processInstance.firstTask">请点击后方按钮选择 已入库合同</span>
                                </div>
                                <label class="col-md-2 control-label required">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo" readonly
                                           name="contractNo" required="true" maxlength="20" placeholder="合同号" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同金额（万元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractMoney" name="contractMoney" required="true"   readonly/>
                                </div>
                                <label class="col-md-2 control-label required">合同已收金额（万元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractIncomeMoney" name="contractIncomeMoney" required="true"   readonly/>
                                </div>
                            </div>
                            <div  class="form-group" ng-if="vm.item.invoiceId!=0">
                                <label class="col-md-2 control-label required">发票号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.invoiceNo" readonly
                                           name="invoiceNo" required="true" maxlength="20" placeholder="发票号" />
                                </div>
                                <label class="col-md-2 control-label required" >法人单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.legalDept"
                                           name="legalDept" required="true"
                                           readonly/>
                                </div>
                            </div>
                            <div  class="form-group" ng-if="vm.item.invoiceId!=0">
                                <label class="col-md-2 control-label required">发票开具额(万元)</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.invoiceMoney" readonly
                                           name="invoiceMoney" required="true" maxlength="20" placeholder="" />
                                </div>
                                <label class="col-md-2 control-label required" >发票收入额(万元)</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.invoiceGetMoney"
                                           name="invoiceGetMoney" required="true"
                                           readonly/>
                                </div>
                            </div>
                            <div  class="form-group">
                                <label class="col-md-2 control-label required" >项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           name="projectName" required="true"
                                           readonly/>
                                </div>
                                <label class="col-md-2 control-label required">项目号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo" readonly
                                           name="projectNo" required="true" maxlength="20" placeholder="项目号" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">本次收入金额（万元）</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.incomeMoney" name="incomeMoney" required="true" ng-change="vm.showBigNum();"  ng-disabled="true"/>
                                </div>

                                <label class="col-md-2 control-label required">金额大写</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.incomeMoneyMax" name="incomeMoneyMax" required="true"   ng-disabled="true"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">管理成本占比</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.managePercent" name="managePercent"   ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-2 control-label ">财务确认时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="verifyTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.verifyTime"
                                               ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                                        <button class="btn default" type="button"
                                                                ng-disabled="!processInstance.firstTask"><i
                                                                class="fa fa-calendar"></i></button>
                                                        </span>
                                    </div> </div>
                            </div>

                            <div class="form-group">
                                <label  class="col-md-2 control-label">收入备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark"  name="remark" maxlength="200"
                                           ng-disabled="!processInstance.firstTask" placeholder="" />
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
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade draggable-modal" id="selectLibraryModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">选择合同库中的合同</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                   <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="合同名称"
                               ng-model="vm.qContract"/>
                    </div>
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="项目名称"
                               ng-model="vm.qProject"/>
                    </div>
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="合同号"
                               ng-model="vm.qContractNo"/>
                    </div>
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="项目号"
                               ng-model="vm.qProjectNo"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>合同名称</th>
                            <th>项目名称</th>
                            <th>合同号</th>
                            <th>项目号</th>
                            <th style="width: 80px">合同额（万元）</th>
                            <%--                            <th style="width: 60px">评审级别</th>--%>
                            <th style="width: 120px">签约日期</th>
                            <%--                                        <th style="width: 100px;">创建时间</th>--%>
                            <%--                            <th style="width: 100px;">合同来源</th>--%>
                            <th style="width: 30px;">补充合同</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.librarys |filter:{contractName:vm.qContract}|filter:{projectName:vm.qProject}|filter:{contractNo:vm.qContractNo}|filter:{projectNo:vm.qProjectNo}">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.contractLibraryId==item.id" class="cb_library"
                                       data-name="{{item}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.contractName"><strong ></strong></td>
                            <td ng-bind="item.projectName"><strong ></strong></td>
                            <td ng-bind="item.contractNo" ></td>
                            <td ng-bind="item.projectNo" ></td>
                            <td ng-bind="item.contractMoney " ></td>
                            <%--                            <td ng-bind="item.reviewLevel"></td>--%>
                            <td ng-bind="item.signTime"></td>
                            <%--                                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>--%>
                            <%--                            <td>
                                                            <strong   ng-if="item.contractPreId!=0&&item.contractReviewId==0"><span style="color: red" ng-click="vm.opt=1;vm.showNew(item.contractPreId)">超前任务单</span></strong>
                                                            <strong   ng-if="item.contractPreId!=0&&item.contractReviewId!=0"><span style="color: green" ng-click="vm.opt=1;vm.showNew(item.contractPreId)">超前任务单(已补录)</span></strong>
                                                            <strong   ng-if="item.contractReviewId!=0&&item.contractPreId==0"><span style="color: blue" ng-click="vm.opt=0;vm.showNew(item.contractReviewId)">合同评审</span></strong>
                                                            <strong  ng-if="item.contractReviewId==0&&item.preContractId==0" ><span style="color: grey">其他</span></strong>
                                                        </td>--%>
                            <td class="text-center">
                                <span class="label label-sm label-success"  ng-if="item.main">是</span>
                                <span class="label label-sm label-default" ng-if="!item.main">否</span>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectModel()">确认</button>
            </div>
        </div>
    </div>

</div>
<jsp:include page="../print/print.jsp"/>