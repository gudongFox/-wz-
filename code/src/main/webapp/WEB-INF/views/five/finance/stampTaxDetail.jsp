<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>财务管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>印花税申报</span>
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
            <i class="icon-note"></i>印花税申报
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);"  style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.sendAble" style="font-size: 14px;line-height: 1.6"
               ng-click="vm.save();" >
                <i class="fa fa-save"></i> 保存 </a>

            <a href="javascript:;" class="btn btn-sm btn-default"  style="font-size: 14px;line-height: 1.6"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <%--<li class="">
                    <a href="#tab_15_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_15_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>--%>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 140px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractName" name="contractName" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.contractId!=0"/>
                                        <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.showContractLibraryModal();" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-cog"></i></button>
                                    </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.contractId!=0" >
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.contractId!=0" >
                                </div>
                                <label class="col-md-2 control-label required">项目号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.contractId!=0" >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">客户名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.customerName" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.contractId!=0" >
                                </div>
                                <label class="col-md-2 control-label required">签订时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.signTime" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.contractId!=0"  >
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同额</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractMoney" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.contractId!=0" >
                                </div>
                                <label class="col-md-2 control-label required" style="color: red">税目</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同印花税税目'}:true"
                                            ng-model="vm.item.taxType" class="form-control" ng-change="vm.save()"
                                            ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label" >税率</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.taxNum"  ng-disabled="vm.item.taxType!='其他'||rightData.selectOpts.indexOf('修改')<0">
                                        <span class="input-group-addon">%</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label" >印花税额(万元)</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <span class="input-group-addon">￥</span>
                                        <input type="text" class="form-control" ng-model="vm.item.stampTaxMoney"  readonly>
                                        <span class="input-group-addon">万元</span>
                                    </div>
                                    <span style=" margin-left:40px;font-size: 13px;color: red" >{{(vm.item.stampTaxMoney*10000).toFixed(2)+'元'}}</span>
                                </div>
                            </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">合同所属部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractDeptName" name="contractDeptName" required="true" readonly/>
                                </div>
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" required="true" ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
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
               <%-- <div class="tab-pane" id="tab_15_2"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>--%>
            </div>
        </div>
    </div>
</div>
<%--<div class="portlet light">
    <div class="portlet-title" >
        <div class="caption">
            <i class="fa fa-file"></i> 合同明细
        </div>
        <div class="actions">
            <a id="addBtn"  class="btn btn-sm btn-default" ng-click="vm.showDetailModel(0);"
               ng-if="processInstance.firstTask">
                <i class="fa fa-plus"></i> 选择合同 </a>
            <a class="btn btn-sm green" ng-click="vm.downloadModelExcel()" target="_blank">
                <i class="fa fa-cloud-download"></i> 导出明细 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">#</th>
                    <th>合同名称</th>
                    <th>合同号</th>
                    <th>项目名称</th>
                    <th>顾客单位</th>
                    <th>签订时间</th>
                    <th>合同金额</th>
                    <th>税目</th>
                    <th>税率</th>
                    <th>印花税额</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.contractName" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.contractNo"></td>
                    <td ng-bind="detail.projectName"></td>
                    <td ng-bind="detail.customerName"></td>
                    <td ng-bind="detail.signTime"></td>
                    <td ng-bind="detail.contractMoney"></td>
                    <td ng-bind="detail.taxType"></td>
                    <td ng-bind="detail.taxNum"></td>
                    <td ng-bind="detail.stampTaxMoney"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-show="detail.creator==user.userLogin" ng-click="vm.removeDetail(detail.id);" title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>--%>

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
                    <span style="color: red;font-size: 12px">金额:万元</span>
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
                            <th style="width: 80px">合同额</th>
                            <th style="width: 80px">开具发票额</th>
                            <th style="width: 80px">合同收入额</th>
                            <th style="width: 120px">签约日期</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.librarys |filter:{contractName:vm.qContract}|filter:{projectName:vm.qProject}|filter:{contractNo:vm.qContractNo}|filter:{projectNo:vm.qProjectNo}">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.contractId==item.id" class="cb_library"
                                       data-name="{{item}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.contractName"><strong ></strong></td>
                            <td ng-bind="item.projectName"><strong ></strong></td>
                            <td ng-bind="item.contractNo" ></td>
                            <td ng-bind="item.projectNo" ></td>
                            <td ng-bind="item.contractMoney " ></td>
                            <td ng-bind="item.contractInvoiceMoney |currency : '￥'" ></td>
                            <td ng-bind="item.contractIncomeMoney |currency : '￥'" ></td>
                            <td ng-bind="item.signTime"></td>
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

<div class="modal fade draggable-modal" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">印花税详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label required">合同名称</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.detail.contractName" name="contractName" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.detail.contractId!=0"/>
                                    <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.showContractLibraryModal();" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-cog"></i></button>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">合同号</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.contractNo" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.detail.contractId!=0" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">项目名称</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.projectName" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.detail.contractId!=0" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">项目号</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.projectNo" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.detail.contractId!=0" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">客户名称</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.customerName" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.detail.contractId!=0" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">签订时间</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.signTime" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.detail.contractId!=0"  >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">合同额</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.contractMoney" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.detail.contractId!=0" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required" style="color: red">税目</label>
                            <div class="col-md-9">
                                <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同印花税税目'}:true"
                                        ng-model="vm.detail.taxType" class="form-control" ng-change="vm.saveDetail()"
                                        ng-disabled="rightData.selectOpts.indexOf('修改')<0"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" >税率</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.taxNum"  readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" >印花税额</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.stampTaxMoney"  readonly>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detail.remark" ng-disabled="rightData.selectOpts.indexOf('修改')<0">
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

