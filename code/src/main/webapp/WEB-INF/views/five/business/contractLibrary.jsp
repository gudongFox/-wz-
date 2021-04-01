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
            <span ng-bind="tableName">合同库</span>
        </li>
    </ul>
</div>


<div class="row">
    <div class="col-md-12">
        <div class="portlet blue box">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-note"></i> <span ng-bind="tableName">合同库</span>
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadPagedData();">
                        <i class="fa fa-refresh"></i> 刷新 </a>
                  <%--  <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.add();" ng-show="rightData.selectOpts.indexOf('增加')>=0">
                        <i class="fa fa-plus"></i> 补录历史合同 </a>--%>

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
                                                                           placeholder="项目名称|合同号|项目号"
                                                                           ng-model="vm.params.qName"></label>
                            <a class="btn green btn-sm defaultBtn" ng-click="vm.loadPagedData();"><i class="fa fa-search"></i> 查询 </a>

                            <a class="btn btn-sm green" ng-click="vm.downTempleXml()" target="_blank">
                                <i class="fa fa-cloud-download"></i> 下载模板 </a>
                            <a class="btn btn-sm green" ng-click="vm.statisticalData()" target="_blank" ng-show="rightData.selectOpts.indexOf('统计')>=0">
                                <i class="fa fa-cloud-download"></i> 统计数据 </a>
                            <span id="btnUpload" class="btn btn-sm blue fileinput-button"  ng-show="rightData.selectOpts.indexOf('修改')>=0">
                                    <i class="fa fa-cloud-upload"></i>
                                    <span>上传数据</span>
                                    <span id="uploadProgress"></span>
                                    <input type="file" name="multipartFile" id="uploadModelFile"
                                           accept="*"
                                           multiple="multiple"/>
                                 </span>
                            <a style="float: right;margin-right: 20px" ng-show="rightData.selectOpts.indexOf('修改')>=0"
                               class="btn btn-sm green" ng-click="vm.downloadModelExcel()" target="_blank"><i class="fa fa-cloud-download"></i> 导出合同 </a>
                            <span style="vertical-align:bottom; margin-left:5px;font-size: 12px;color: red">金额：(万元)</span>
                        </div>
                    </div>
                    <div class="table-scrollable">
                        <table filter class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th style="min-width: 200px;">合同名称</th>
                                <th style="width: 120px" class="hidden-md hidden-sm">合同号</th>
                                <th style="width: 80px" class="hidden-md hidden-sm">项目号</th>
                                <th style="width: 120px">签约日期</th>
                                <th style="width: 120px" class="hidden-md hidden-sm">合同额(万元)</th>
                                <th style="width: 160px" >承接部门</th>
                                <th style="width: 50px;">发票情况</th>
                                <th style="width: 50px;">收入情况</th>
                                <th style="width: 100px;">合同来源</th>
                                <th style="width: 50px;">是否立项</th>
                                <th style="width: 50px;">补充情况</th>
                                <th style="width: 50px;">分包情况</th>
                                <th style="width: 30px;" ng-if="rightData.selectOpts.indexOf('修改')>=0">是否开启</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.pageInfo.list" on-finish-render>
                                <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                                <td ng-bind="item.contractName"  class="data_title" title="{{'项目备案名称：'+ item.recordProjectName}}"  ng-click="vm.show(item.id,item.subpackageId);"><strong ></strong>
                                </td>

                                <td ng-bind="item.contractNo" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.projectNo" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.signTime"></td>
                                <td ng-bind="item.contractMoney" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.deptName" ></td>
                                <td ng-click="vm.opt=0;vm.showInvoiceModal(item.id)">
                                    <strong  ng-if="item.invoiceIds!=''"  ><span style="color: green" ng-bind="item.contractInvoiceMoney"></span></strong>
                                    <strong  ng-if="item.invoiceIds==''" ><span style="color: grey">无发票</span></strong>
                                </td>
                                <td ng-click="vm.opt=0;vm.showIncomeModal(item.id)">
                                    <strong  ng-if="item.incomeIds!=''"  ><span style="color: green"  ng-bind="item.contractIncomeMoney"></span></strong>
                                    <strong  ng-if="item.incomeIds==''" ><span style="color: grey">无收入</span></strong>
                                </td>
                                <td>
                                    <strong   ng-if="item.contractPreId!=0&&item.contractReviewId==0"><span style="color: red" ng-click="vm.opt=1;vm.showNew(item.contractPreId)">超前任务单</span></strong>
                                    <strong   ng-if="item.contractPreId!=0&&item.contractReviewId!=0"><span style="color: green" ng-click="vm.opt=1;vm.showNew(item.contractPreId)">超前任务单(已补录)</span></strong>
                                    <strong   ng-if="item.contractReviewId!=0&&item.contractPreId==0"><span style="color: blue" ng-click="vm.opt=0;vm.showNew(item.contractReviewId)">合同评审</span></strong>
                                    <strong   ng-if="item.contractReviewId==0&&item.contractPreId==0&&item.insertType==0" ><span style="color: grey">导入合同</span></strong>
                                    <strong   ng-if="item.contractReviewId==0&&item.contractPreId==0&&item.insertType==1" ><span style="color: green">补录合同</span></strong>
                                </td>
                                <td class="text-center">
                                    <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.contractId!=0" ng-click="vm.showEd(item.contractId);">启用</span>
                                    <span class="label label-sm label-default" ng-if="item.contractId==0">关闭</span>
                                </td>
                                <td>
                                    <strong  ng-if="item.mainReviewIds!=''" ><span style="color: green" ng-click="vm.opt=0;vm.showMainReviewModal(item.id)">已补充</span></strong>
                                    <strong  ng-if="item.mainReviewIds==''" ><span style="color: grey">无补充</span></strong>
                                </td>
                                <td>
                                    <strong  ng-if="item.subpackageIds!=''" ><span style="color: green" ng-click="vm.opt=0;vm.showSubpackageModal(item.id)">已分包</span></strong>
                                    <strong  ng-if="item.subpackageIds==''" ><span style="color: grey">未分包</span></strong>
                                </td>

                                <td class="text-center" ng-if="rightData.selectOpts.indexOf('修改')>=0" ng-click="vm.changeOpen(item.id)">
                                    <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.open">开启</span>
                                    <span class="label label-sm label-default" ng-if="!item.open">关闭</span>
                                </td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.show(item.id,item.subpackageId);" title="详情"></i>
                                    <i class="fa fa-trash-o margin-right-5" ng-click="vm.remove(item.id);" title="删除"
                                       ng-show="item.creator==user.userLogin&&rightData.selectOpts.indexOf('删除')>=0"></i>
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
<%--分包合同情况--%>
<div class="modal fade draggable-modal" id="subpackageModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10"><i class="icon-clock"></i> <span ng-bind="vm.subpackages[0].contractName"></span>--分包/采购情况</h4>
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
                            <th>分包/采购合同名称</th>
                            <th class="hidden-md hidden-sm">供方名称</th>
                            <th>发包部门名称</th>
                            <th>主合同名称</th>
                            <th style="width: 100px">主合同金额(万元)</th>
                            <th style="width: 60px">经办人</th>
                            <th style="width: 100px;">创建时间</th>
                            <th style="width: 55px">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.subpackages">
                            <td class="dt-right" ng-bind="$index+vm.pageInfo.startRow"></td>
                            <td ng-bind="item.subContractName"  class="data_title"  ng-click="vm.jumpSubpackage(item.id);"><strong ></strong>
                            </td>
                            <td ng-bind="item.supplierName" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.deptName"></td>
                            <td ng-bind="item.contractName"></td>
                            <td ng-bind="item.contractMoney"></td>
                            <td ng-bind="item.creatorName"></td>
                            <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                            <td>
                                <i class="fa fa-pencil margin-right-5" ng-click="vm.jumpSubpackage(item.id);" title="详情"></i>
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
<%--补充合同情况--%>
<div class="modal fade draggable-modal" id="mainReviewModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10"><i class="icon-clock"></i> <span ng-bind="vm.mainReviews[0].contractName"></span>补充合同情况</h4>
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
                            <th>项目名称</th>
                            <th style="width: 150px">合同号</th>
                            <th style="width: 150px">项目号</th>
                            <th style="width: 90px">合同额（万元）</th>
                            <th >客户名称</th>
                            <th style="width: 100px;">创建时间</th>
                            <th style="width: 30px;">补充合同标识</th>
                            <th style="width: 30px;">补录标识</th>
                            <th style="width: 30px;">对内分包标识</th>
                            <th style="width: 55px">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.mainReviews">
                            <td class="dt-right" ng-bind="$index+1"></td>
                            <td ng-bind="item.projectName"  class="data_title"  ng-click="vm.jumpMainReview(item.id);"><strong ></strong>
                            </td>
                            <td ng-bind="item.contractNo" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.projectNo" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.investMoney|currency : '￥'" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.customerName" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                            <td class="text-center">
                                <span class="label label-sm label-success"  ng-if="item.main">是</span>
                                <span class="label label-sm label-default" ng-if="!item.main">否</span>
                            </td>
                            <td class="text-center">
                                <span class="label label-sm label-success"  ng-if="item.preContractId!=0" ng-click="vm.showPreContract(item.preContractId);">是</span>
                                <span class="label label-sm label-default" ng-if="item.preContractId==0">否</span>
                            </td>
                            <td class="text-center">
                                <span class="label label-sm label-success"  ng-if="item.subpackageId!=0" ng-click="vm.showSubpackage(item.subpackageId);">是</span>
                                <span class="label label-sm label-default" ng-if="item.subpackageId==0">否</span>
                            </td>
                            <td>
                                <i class="fa fa-pencil margin-right-5" ng-click="vm.jumpMainReview(item.id);" title="详情"></i>
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
<%--收入情况--%>
<div class="modal fade draggable-modal" id="incomeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10"><i class="icon-clock"></i> <span ng-bind="vm.incomes[0].contractName"></span>--合同收费情况</h4>
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
                            <th>收入备注</th>
                            <th style="width: 100px">收入金额（万元）</th>
                            <th style="width: 100px" >合同已领金额（万元）</th>
                            <th style="width: 100px" >发票信息</th>
                            <th style="width: 60px">创建人</th>
                            <th style="width: 100px;">创建时间</th>
                            <th style="width: 60px">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.incomes">
                            <td class="dt-right" ng-bind="$index+1"></td>
                            <td ng-bind="item.remark"  class="data_title"  ng-click="vm.jumpIncome(item.id);"></td>
                            <td ng-bind="item.incomeMoney" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.contractIncomeMoney"></td>
                            <td ng-if="item.invoiceId==0" style="color: red" >无发票</td>
                            <td ng-if="item.invoiceId!=0" style="color: green" ng-click="vm.showInvoice(item.invoiceId)" ng-bind="item.invoiceNo"></td>
                            <td ng-bind="item.creatorName"></td>
                            <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                            <td>
                                <i class="fa fa-pencil margin-right-5" ng-click="vm.jumpIncome(item.id);" title="详情"></i>
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
<%--发票情况--%>
<div class="modal fade draggable-modal" id="invoiceModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10"><i class="icon-clock"></i>  <span ng-bind="vm.invoices[0].contractName"></span>--合同发票情况</h4>
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
                            <th>发票号</th>
                            <th>发票抬头</th>
                            <th>开票时间</th>
                            <th>发票金额（万元）</th>
                            <th>发票已收入金额（万元）</th>
                            <th style="width: 60px">创建人</th>
                            <th style="width: 100px;">创建时间</th>
                            <th style="width: 60px">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.invoices">
                            <td class="dt-right" ng-bind="$index+1"></td>
                            <td ng-bind="item.invoiceNo"  class="data_title"  ng-click="vm.jumpInvoice(item.id);"></td>
                            <td ng-bind="item.invoiceHeadName" ></td>
                            <td ng-bind="item.invoiceTime"></td>
                            <td ng-bind="item.invoiceMoney"></td>
                            <td ng-bind="item.invoiceGetMoney"></td>
                            <td ng-bind="item.creatorName"></td>
                            <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                            <td>
                                <i class="fa fa-pencil margin-right-5" ng-click="vm.jumpInvoice(item.id);" title="详情"></i>
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
