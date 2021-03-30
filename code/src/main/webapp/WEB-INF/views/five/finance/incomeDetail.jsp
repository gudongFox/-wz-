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
            <a ui-sref="oa.functionEntrance({moduleId:80})">收入管理</a>
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
            <i class="icon-note"></i><span>收入管理</span>
              <%--<small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>--%>

        </div>
        <div class="actions">
          <%--  <jsp:include page="../../common/common-actAction.jsp"/>--%>
              <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                  <i class="fa fa-refresh"></i> 刷新 </a>
              <a href="javascript:;" class="btn btn-sm btn-default" ng-show="rightData.selectOpts.indexOf('修改')>=0"
                 ng-click="vm.save();" >
                  <i class="fa fa-save"></i> 保存 </a>
              <a href="javascript:;" class="btn btn-sm btn-default" ng-show="!vm.item.processEnd"
                 ng-click="vm.getNotarizeIncome(vm.item.id,vm.item.creator);" >
                  <i class="glyphicon glyphicon-ok"></i> 确认收入 </a>
              <a href="javascript:;" class="btn btn-sm btn-default" ng-show="vm.item.processEnd&&vm.item.confirmIds==''"
                  ng-click="vm.getNotarizeIncome(vm.item.id,vm.item.creator);" >
                  <i class="glyphicon glyphicon-remove"></i> 取消确认 </a>
              <a href="javascript:;" class="btn btn-sm btn-default"
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
                <li class="">
                    <a href="#tab_15_4" data-toggle="tab" aria-expanded="">
                        收入确认情况 </a>
                </li>
               <%-- <li class="">
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
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">对方账户</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.sourceAccount" name="sourceAccount" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.processEnd"/>
                                </div>
                                <label class="col-md-2 control-label required">收入接收账户</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.targetAccount" name="targetAccount" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.processEnd"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">对方账户名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.sourceName" name="sourceName" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.processEnd"/>
                                </div>
                                <label class="col-md-2 control-label required">对方账户银行</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.sourceBank" name="sourceBank" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.processEnd"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label  class="col-md-2 control-label">收入类型</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.type" name="type"  readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showTypeModel();" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.processEnd"><i class="fa fa-cog" ></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label  class="col-md-2 control-label">交易时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="incomeTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.incomeTime" name="incomeTime" required="true" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.processEnd">
                                        <span class="input-group-btn" ng-disabled="rightData.selectOpts.indexOf('修改')<0">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
<%--                            <div class="form-group" ng-if="vm.item.type.indexOf('打款')>=0">
                                <label class="col-md-2 control-label required">打款收入金额（万元）</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.moneyRemit" name="moneyRemit" required="true" ng-change="vm.showBigNum();"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>

                                <label class="col-md-2 control-label required">打款金额大写</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.moneyRemitMax" name="moneyRemitMax" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.type.indexOf('票据')>=0">
                                <label class="col-md-2 control-label required">票据收入总金额（万元）</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.moneyNode" name="moneyNode" required="true" ng-change="vm.showBigNum();"   readonly/>
                                </div>

                                <label class="col-md-2 control-label required">票据收入总金额大写</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.moneyNodeMax" name="moneyNodeMax" required="true"   readonly/>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.type.indexOf('现金')>=0">
                                <label class="col-md-2 control-label required">现金收入金额（万元）</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.moneyCash" name="moneyCash" required="true" ng-change="vm.showBigNum();"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>

                                <label class="col-md-2 control-label required">现金金额大写</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.moneyCashMax" name="moneyCashMax" required="true"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">总收入金额（元）</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.money" name="money" required="true" ng-change="vm.showBigNum();" ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.processEnd"  />
                                </div>
                                <label class="col-md-2 control-label required">总金额大写</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.moneyMax" name="moneyMax" required="true"   readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">收入备注</label>
                                <div class="col-md-10">
                                    <input type="text"  class="form-control" ng-model="vm.item.remark" name="remark"  ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.processEnd"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">描述</label>
                                <div class="col-md-10">
                                    <textarea style="row-span: 3" type="text"  class="form-control" ng-model="vm.item.description" name="description"    ng-disabled="rightData.selectOpts.indexOf('修改')<0||vm.item.processEnd"/>
                                </div>
                            </div>
                            <%--<div class="form-group">
                                <label class="col-md-2 control-label ">收入已认领金额（元）</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.incomeConfirmMoney" name="incomeConfirmMoney" required="true"   readonly/>
                                </div>
                                <label class="col-md-2 control-label ">收入正在认领金额（元）</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" ng-model="vm.item.incomeConfirmMoneyIng" name="incomeConfirmMoneyIng" required="true"   readonly/>
                                </div>
                            </div>--%>
<%--
                            <div class="form-group">
                                <label class="col-md-1 control-label" style="color: red">结转管控设置:</label>
                                <label class="col-md-1 control-label">事业部名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="rightData.selectOpts.indexOf('删除')<0"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="rightData.selectOpts.indexOf('删除')<0"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">本年管控比</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.managerRate" name="managerRate" required="true" ng-change="vm.showBigNum();"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">设计目标金额（万元）</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.designTargetMoney" name="designTargetMoney" required="true" ng-change="vm.showBigNum();"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                                <label class="col-md-2 control-label required">设计完成金额（万元）</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.designSucessMoney" name="designSucessMoney" required="true" ng-change="vm.showBigNum();"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">总承包目标额（万元）</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.totalTargetMoney" name="totalTargetMoney" required="true" ng-change="vm.showBigNum();"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                                <label class="col-md-2 control-label required">总承包完成额（万元）</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.totalSucesstMoney" name="totalSucesstMoney" required="true" ng-change="vm.showBigNum();"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">设计咨询(超比)</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.designAskRate" name="designAskRate" required="true" ng-change="vm.showBigNum();"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                                <label class="col-md-2 control-label required">总承包(超比)</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" ng-model="vm.item.totalRate" name="totalRate" required="true" ng-change="vm.showBigNum();"   ng-disabled="rightData.selectOpts.indexOf('修改')<0"/>
                                </div>
                            </div>
--%>

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
                <div class="tab-pane " id="tab_15_4"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <div class="table-scrollable" style="max-height: 400px">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th >收入备注</th>
                                <th class="hidden-md hidden-sm">收入来源账户</th>
                                <th style="width: 180px">事业部名称</th>
                                <th style="width: 120px">认领金额（元）</th>
                                <th style="width: 60px">经办人</th>
                                <th style="width: 100px;">创建时间</th>
                                <th style="width: 150px">流程状态</th>
                                <th style="width: 55px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.incomeConfirms">
                                <td class="dt-right" ng-bind="$index+1"></td>
                                <td ng-bind="item.incomeRemark"  class="data_title"  ng-click="vm.jumpIncomeConfirm(item.id);">
                                <td ng-bind="item.sourceAccount" class="hidden-md hidden-sm"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.money"></td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <span ng-bind="item.processName"></span>
                                </td>
                                <td>
                                    <i class="fa fa-pencil margin-right-5" ng-click="vm.jumpIncomeConfirm(item.id);" title="详情"></i>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
               <%-- <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>--%>
            </div>
        </div>
    </div>
</div>
<div class="portlet light" ng-if="vm.item.type.indexOf('票据')>=0">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 票据信息
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showNodeModel();"
               ng-if="rightData.selectOpts.indexOf('修改')>=0">
                <i class="fa fa-plus"></i> 选择票据 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addNode();"
               ng-if="rightData.selectOpts.indexOf('修改')>=0">
                <i class="fa fa-plus"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="width: 120px">票据类型</th>
                    <th class="hidden-md hidden-sm">票据来源账户</th>
                    <th class="hidden-md hidden-sm">票据接受账户</th>
                    <th style="width: 120px">票据金额（万元）</th>
                    <th style="width: 200px">票据金额大写</th>
                    <th style="width: 60px">经办人</th>
                    <th style="width: 100px;">创建时间</th>
                    <th style="width: 150px">流程状态</th>
                    <th style="width: 55px">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="item in vm.nodes">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="item.type"  class="data_title"  ng-click="vm.showNode(item.id);"><strong ></strong>
                    </td>
                    <td ng-bind="item.sourceAccount" class="hidden-md hidden-sm"></td>
                    <td ng-bind="item.targetAccount" class="hidden-md hidden-sm"></td>
                    <td ng-bind="item.money"></td>
                    <td ng-bind="item.moneyMax"></td>
                    <td ng-bind="item.creatorName"></td>
                    <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                    <td>
                        <span ng-bind="item.processName"></span>
                    </td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showNode(item.id);" title="详情"></i>
                        <i class="fa fa-trash-o margin-right-5" ng-click="vm.removeNode(item.id);" title="删除"
                           ng-show="rightData.selectOpts.indexOf('修改')>=0&&!vm.item.processEnd"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade" id="typeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择收入类型</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="type in vm.types">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_type"
                           ng-checked="vm.item.type.indexOf(type.name)>=0"
                           data-name="{{type.name}}" data-id="{{'file_'+id}}" />
                    <span ng-bind="type.name" class="margin-right-10" style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%4==0"/>
               </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveType();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="selectNodeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">请选择已申请票据信息</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qCustomer"/>
                    </div>
                </div>
                <table  style="margin-top: 10px" class="table table-striped table-hover table-bordered table-advance no-footer">
                    <thead>
                    <tr>
                        <th style="width: 35px;">#</th>
                        <th style="width: 120px">票据类型</th>
                        <th class="hidden-md hidden-sm">票据来源账户</th>
                        <th style="width: 150px">票据金额（万元）</th>
                        <th style="width: 120px">到期时间（天）</th>
                        <th style="width: 60px">经办人</th>
                        <th style="width: 100px;">创建时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr ng-repeat="item in vm.selectNodes">
                        <td>
                            <input type="checkbox"  class="cb_node" data-id="{{item.id}}"  style="width: 15px;height: 15px;"/>
                        </td>
                        <td ng-bind="item.type"  class="data_title"  ng-click="vm.show(item.id);"></td>
                        <td ng-bind="item.sourceAccount" class="hidden-md hidden-sm"></td>
                        <td ng-bind="item.money"></td>
                        <td ng-bind="item.remainTime" style="color: red" ng-if="item.remainTime=='已到期'"></td>
                        <td ng-bind="item.remainTime" style="color: green" ng-if="item.remainTime!='已到期'"></td>
                        <td ng-bind="item.creatorName"></td>
                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                    </tr>
                    </tbody>
                </table>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectNode();">确认</button>
            </div>
        </div>
    </div>
</div>



