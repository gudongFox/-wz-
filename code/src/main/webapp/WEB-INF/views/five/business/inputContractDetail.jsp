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
            <span>合同补录</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.projectName"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 合同补录
            <small ng-bind="vm.item.contractNo"></small>
            <small ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);" ng-show="processInstance.refresh">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.passAble"
               ng-click="vm.save();" >
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="showTakeTask();"
               ng-show="processInstance.takeAble"><i
                    class="fa fa-refresh"></i> 接受任务 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="showFetchTask();"
               ng-show="processInstance.fetchAble" title="发起者取回未开始处理的流程"><i
                    class="fa fa-refresh"></i> 取回任务 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showSendTask(true);"
               ng-show="processInstance.sendAble"><i
                    class="fa fa-refresh"></i> 提交任务 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="showSendTask(false);"
               ng-show="processInstance.backAble"><i
                    class="fa fa-refresh"></i> 打回任务 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="showDelegateTask();"
               ng-show="processInstance.delegateAble"><i class="fa fa-refresh"></i> 委托任务 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="showTransferTask();"
               ng-show="processInstance.transferAble"><i class="fa fa-refresh"></i> 移交任务 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="showResolveTask();"
               ng-show="processInstance.resolveAble"><i
                    class="fa fa-refresh"></i> 完成任务 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" ng-show="processInstance.printAble">
                <i class="fa fa-print"></i> 打印 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.returnBack"
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
                     style="min-height: 620px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           name="projectName" required="true"
                                          disabled/>
                                    <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showPreContractModal();"
                                                    ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">项目号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectNo"
                                               name="contractNo" required="true" maxlength="20" placeholder="项目号" disabled
                                        />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-disabled="!processInstance.firstTask"
                                                    ng-click="vm.getProjectNo();"> <i
                                                    class="fa fa-refresh"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">承接部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.item.deptId);vm.opt='承接部门'"
                                                   ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>

                                <label class="col-md-2 control-label required">承办人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.chargeMenName"
                                               name="chargeMenName" required
                                               disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=user;vm.opt ='chargeMen';vm.optUser=vm.item.chargeMen;vm.showUserModel();"
                                                ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.contractNo"
                                               name="contractNo" required="true" maxlength="20" placeholder="合同号" disabled
                                        />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-disabled="!processInstance.firstTask"
                                                    ng-click="vm.getContractNo();"> <i
                                                    class="fa fa-refresh"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">合同额(万元)</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.contractMoney"
                                           placeholder="单位(万元),保留2位小数"
                                           name="contractMoney" maxlength="20" required
                                          ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"/>
                                </div>
                            <%--    <label class="col-md-2 control-label required">合同进度</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同进度'}:true"
                                            ng-model="vm.item.contractSchedule" class="form-control"
                                           ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName=='经营发展部')"></select>
                                </div>--%>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同标志</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractMark"
                                           name="contractMark" required="true"
                                           ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"/>
                                    <span>无合同W</span>
                                </div>
                                <label class="col-md-2 control-label required">季度</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'季度'}:true"
                                            ng-model="vm.item.quarter" class="form-control"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同类型'}:true"
                                            ng-model="vm.item.contractType" class="form-control"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                                <label class="col-md-2 control-label required">签约日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="signTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.signTime" required="true"
                                               ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)">
                                        <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"><i
                                            class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同摘要</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control"
                                              ng-model="vm.item.contractDes"
                                              name="contractDes" required="true"
                                             ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0"/>
                                </div>

                            </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label required">委托方</label>
                                    <div class="col-md-4">
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.customerName"
                                                   name="customerName" required="true"
                                                  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"/>
                                            <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showCustomerModal();"
                                                   ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                        </div>
                                    </div>
                                    <label class="col-md-2 control-label required">部内外</label>
                                    <div class="col-md-4">
                                        <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'部内外'}:true"
                                                ng-model="vm.item.inOrOut" class="form-control"
                                               ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                    </div>

                                </div>

                                   <%-- <div class="form-group">

                                        <label class="col-md-2 control-label required">输入日期</label>
                                        <div class="col-md-4">
                                            <div class="input-group date date-picker" id="enterTime">
                                                <input type="text" class="form-control"
                                                       ng-model="vm.item.enterTime" required="true"
                                                      ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName=='经营发展部')">
                                                <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                           ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName=='经营发展部')"><i
                                            class="fa fa-calendar"></i></button></span>
                                            </div>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <label class="col-md-2 control-label required">开户银行</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" ng-model="vm.item.depositBank"
                                                   name="depositBank" required="true"
                                                  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName=='经营发展部')"/>
                                        </div>
                                        <label class="col-md-2 control-label required">银行账号</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" ng-model="vm.item.bankAccount"
                                                   name="bankAccount" required="true"
                                                  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName=='经营发展部')"/>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <label class="col-md-2 control-label required">邮政编码</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" ng-model="vm.item.postalCode"
                                                   name="postalCode" required="true"
                                                  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName=='经营发展部')"/>
                                        </div>
                                        <label class="col-md-2 control-label required">通讯处</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" ng-model="vm.item.postalAddress"
                                                   name="postalAddress" required="true"
                                                  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName=='经营发展部')"/>
                                        </div>

                                    </div>

                                    <div class="form-group">
                                        <label class="col-md-2 control-label required">联系电话</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" ng-model="vm.item.linkPhone"
                                                   name="linkPhone" required="true"
                                                  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName=='经营发展部')"/>
                                        </div>
                                        <label class="col-md-2 control-label required">委托代理</label>
                                        <div class="col-md-4">
                                            <input type="text" class="form-control" ng-model="vm.item.agency"
                                                   name="agency" required="true"
                                                  ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName=='经营发展部')"/>
                                        </div>

                                    </div>--%>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目性质</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'项目性质'}:true"
                                            ng-model="vm.item.projectNature" class="form-control"
                                           ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                                <label class="col-md-2 control-label required">甲方行业分类</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'甲方行业分类'}:true"
                                            ng-model="vm.item.industryType" class="form-control"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                              <%--  <label class="col-md-2 control-label required">项数</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.itemNum"
                                           name="itemNum" required="true"
                                          ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"/>
                                </div>--%>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">外贸标记</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'外贸标记'}:true"
                                            ng-model="vm.item.foreignTradeMark" class="form-control"
                                           ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                                <label class="col-md-2 control-label required">民用标记</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'民用标记'}:true"
                                            ng-model="vm.item.civilMark" class="form-control"
                                           ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">军品标记</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'军品标记'}:true"
                                            ng-model="vm.item.militaryMark" class="form-control"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                                <label class="col-md-2 control-label required">项目专业分类 一级</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in vm.projectMajorTypeFirst"
                                            ng-model="vm.item.projectMajorTypeFirst" class="form-control" id="projectMajorTypeFirst"
                                            ng-change="vm.changeMajorTypeFirst()"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">项目专业分类 二级</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in vm.projectMajorTypeSecond"
                                            ng-model="vm.item.projectMajorTypeSecond" class="form-control" id="projectMajorTypeSecond"
                                            ng-change="vm.changeMajorTypeSecond()"
                                           ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                                <label class="col-md-2 control-label required">项目专业分类 三级</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in vm.projectMajorTypeThird"
                                            ng-model="vm.item.projectMajorTypeThird" class="form-control" id="projectMajorTypeThird"
                                            ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                        <label class="col-md-2 control-label">备注</label>
                                        <div class="col-md-10">
                                            <input type="text" class="form-control" ng-model="vm.item.remark"
                                                   name="remark"
                                                   maxlength="100"ng-disabled="!(processInstance.firstTask||processInstance.myRunningTaskName.indexOf('经营发展部')>=0)"/>
                                        </div>
                                    </div>
                                </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 620px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 620px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>



<div class="modal fade draggable-modal" id="selectPreContractModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="portlet  box blue" style="width: 60%; margin-left: 20% ;margin-top: 5%">
     <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i>选择合同的前置项目
            <small >当前已选择前置项目：<span ng-bind="vm.showChooseStatus"></span></small>
        </div>
    </div>
    <div class="portlet-body" >
        <div class="tabbable-custom">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_pre" data-toggle="tab" aria-expanded="false">
                        超前任务单 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_pre"
                     style="min-height: 400px;">
                    <div class="modal-body">
                        <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                <thead>
                                <tr>
                                    <th style="width: 35px;">#</th>
                                    <th>项目名称</th>
                                    <th>承接部门</th>
                                    <th style="width: 110px;">投资额(万元)</th>
                                    <th>委托方</th>
<%--                                    <th>项目负责人(总师或项目经理)</th>--%>
                                    <th style="width: 80px;">设计任务类型</th>
                                    <th style="width: 120px;">设计阶段</th>
                                    <th style="width: 75px;">设计管理</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr ng-repeat="item in vm.preContracts" ng-show="item.projectName!=''">
                                    <td>
                                        <input type="checkbox" ng-checked="vm.item.projectName==item.projectName" class="cb_preContract"
                                               data-name="{{item}}" style="width: 15px;height: 15px;"/>
                                    </td>
                                    <td ng-bind="item.projectName"  class="data_title"  ></td>
                                    <td ng-bind="item.deptName"></td>
                                    <td ng-bind="item.investMoney|currency : '￥'"></td>
                                    <td ng-bind="item.customerName"></td>
<%--                                    <td ng-bind="item.chargeMenName"></td>--%>
                                    <td ng-bind="item.designType"></td>
                                    <td ng-bind="item.stageName"></td>
                                    <td class="text-center">
                                        <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.ed" >启用</span>
                                        <span class="label label-sm label-default" ng-if="!item.ed">关闭</span>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn blue" ng-click="vm.saveSelectModel();">确认</button>
                </div>
            </div>
        </div>
    </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="selectCustomerModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">客户名称</h4>
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
                            <th style="width: 50px;">序号</th>
                            <th>客户名称</th>
                            <th>客户地址</th>
                            <th>单位性质</th>
                            <th>联系人</th>
                            <th>联系电话</th>
                            <th>录入部门</th>
                            <th style="width: 100px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="c in vm.customers|filter:{name:vm.qCustomer}" ng-show="c.name!=''">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.customerName==c.name" class="cb_customer"
                                       data-name="{{c}}" style="width: 15px;height: 15px;"/>
                            </td>

                            <td ng-bind="c.name"></td>
                            <td ng-bind="c.address"></td>
                            <td ng-bind="c.customerType"></td>
                            <td ng-bind="c.linkMan"></td>
                            <td ng-bind="c.linkTel"></td>
                            <td ng-bind="c.deptName"></td>
                            <td ng-bind="c.gmtCreate|date:'yyyy-MM-dd'"></td>
                        </tr>
                        </tbody>
                    </table>

                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectCustomer();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="selectPreContractModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">未录入合同的超前任务单</h4>
            </div>
            <div  class="modal-header" ng-if="vm.selectedPreContract.contractId==vm.item.contractId">
                <h4>当前补录合同：<span style="color: blue" ng-bind="vm.selectedPreContract.projectName"></span></h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>项目名称</th>
                            <th>承接部门</th>
                            <th style="width: 110px;">投资额(万元)</th>
                            <th>委托方</th>
                            <th>项目负责人(总师或项目经理)</th>
                            <th style="width: 80px;">设计任务类型</th>
                            <th style="width: 100px;">设计阶段</th>
                            <th style="width: 75px;">设计管理</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.preContracts" ng-show="item.contractId!=vm.item.contractId">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.projectName==item.projectName" class="cb_preContract"
                                       data-name="{{item}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.projectName"  class="data_title"  ng-click="vm.show(item.id);"></td>
                            <td ng-bind="item.deptName"></td>
                            <td ng-bind="item.investMoney|currency : '￥'"></td>
                            <td ng-bind="item.customerName"></td>
                            <td ng-bind="item.chargeMenName"></td>
                            <td ng-bind="item.designType"></td>
                            <td ng-bind="item.stageName"></td>
                            <td class="text-center">
                                <span class="label label-sm label-success" style="cursor: pointer;" ng-if="item.ed" >启用</span>
                                <span class="label label-sm label-default" ng-if="!item.ed">关闭</span>
                            </td>
                        </tr>

                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectPreContract();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="selectMajorListModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择设计专业</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="major in vm.majorNames">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_major"
                           ng-checked="vm.item.designMajor.indexOf(major.name)>=0"
                           data-name="{{major.name}}"/> <span ng-bind="major.name"
                                                              class="margin-right-10"
                                                              style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%6==0"/>
               </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveMajorList();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade" id="selectStageModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择设计阶段</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="stage in vm.stages">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_stage"
                           ng-checked="vm.contract.stageNames.indexOf(stage.name)>=0"
                           data-name="{{stage.name}}"/> <span ng-bind="stage.name"
                                                              class="margin-right-10"
                                                              style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%6==0"/>
               </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveStage();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>


