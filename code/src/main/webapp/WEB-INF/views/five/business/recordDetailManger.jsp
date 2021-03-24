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
            <span>项目信息备案</span>
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
            <i class="icon-note"></i>项目信息备案(管理)
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);"  style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default"  style="font-size: 14px;line-height: 1.6"
               ng-click="vm.save();" >
                <i class="fa fa-save"></i> 保存 </a>

            <a href="javascript:;" class="btn btn-sm btn-default"  style="font-size: 14px;line-height: 1.6"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>>
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">

        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        项目信息 </a>
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
                            <div class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">客户名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.customerName" name="customerName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectCustomer();" ><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">客户编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.item.customerCode" readonly
                                           required="true" maxlength="50"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">纳税人识别号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="纳税人识别号" ng-model="vm.item.taxNo" readonly
                                           required="true" maxlength="50"/>
                                </div>
                                <label class="col-md-2 control-label required">客户地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="" ng-model="vm.item.customerAddress" readonly/>
                                </div>
                            </div>
                            <div  class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">项目名称</label>
<%--                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName"
                                               name="projectName" required="true"
                                               ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0||vm.item.subpackageId!=0"/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showSubpackageModal();"
                                                    ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask">对内分包项目请点击后方按钮选择</span>
                                </div>--%>
                                <div class="col-md-4">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName"
                                               name="projectName" required="true"
                                               ng-disabled="vm.item.subpackageId!=0"/>
                                </div>
                                <label class="col-md-2 control-label required">项目性质</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'五洲项目类型'}:true"
                                            ng-model="vm.item.projectType" class="form-control"></select>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">预计开始时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="planBeginTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.planBeginTime" required="true">
                                        <span class="input-group-btn">
                                    <button class="btn default" type="button"><i class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label required">预计结束时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="planEndTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.planEndTime" required="true">
                                        <span class="input-group-btn">
                                 <button class="btn default" type="button"><i
                                         class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">项目号</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectNo" readonly
                                               name="contractNo" required="true" maxlength="20" placeholder="项目号" disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.getProjectNo();"> <i
                                                    class="fa fa-refresh"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">建设地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectAddress" name="projectAddress" required="true"   />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">建设规模</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectScale" name="projectScale" required="true"   />
                                </div>
                                <label class="col-md-2 control-label required">投资额度（万元）</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.projectInvest" name="projectInvest" required="true"   />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">投资来源</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'投资来源'}:true"
                                            ng-model="vm.item.investSource" class="form-control"></select>

                                </div>
                                <label class="col-md-2 control-label required">业务内容</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.businessContent" name="businessContent" required="true"  />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">行业分类</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'行业分类'}:true"
                                            ng-model="vm.item.industryType" class="form-control"></select>
                                </div>
                                <label class="col-md-2 control-label required">行业分类标准</label>
                                <div class="col-md-4">
                                    <a href="/assets/doc/行业分类标准.xls"  style="color:blue;text-align: center"  target="_blank">行业分类标准.xls</a>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required" >项目联系人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.businessUserName" name="businessUserName"  required="true" disabled />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel(vm.opt='businessUser');" ><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block" >请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">登记部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal(vm.opt='deptId')"> <i
                                                    class="fa fa-cog" ></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block" >请点击后方按钮选择</span>
                                </div>
                            </div>
                          <%--  <div class="form-group">
                                <label class="col-md-2 control-label required">信息提供部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.messageDeptName"
                                               name="messageDeptName" maxlength="100"
                                               readonly/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0"
                                                    ng-click="vm.showDeptModal(vm.opt='messageDeptId')"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block" >请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required" >信息提供人员</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.messageUserName" name="messageUserName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel(vm.opt='messageUser');" ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">计划开标时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="bidPlanOpen">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.bidPlanOpen" name="bidPlanOpen"   >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                              <%--  <label class="col-md-2 control-label">实际开标时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="bidRealOpen">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.bidRealOpen" name="bidRealOpen" required="true" ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0" ><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>--%>
                                <label class="col-md-2 control-label">是否涉密</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否中标'}:true"
                                            ng-model="vm.item.secret" class="form-control"  ></select>
                                    <span class="help-block"  ng-if="vm.item.secret"  style="color: red">此平台为非密平台，严禁录入涉密信息，请确认。</span>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">是否投标</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否中标'}:true"
                                            ng-model="vm.item.attend" class="form-control" ></select>
                                </div>
<%--                                <label class="col-md-2 control-label">是否中标</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否中标'}:true"
                                            ng-model="vm.item.win" class="form-control" ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0" ></select>
                                </div>--%>
                            </div>
<%--                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否开具保函</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否开具保函'}:true"
                                            ng-model="vm.item.tenderBond" class="form-control" ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0"></select>
                                </div>

                            </div>--%>
                          <%--  <div class="form-group" ng-show="vm.item.tenderBond">
                                <label class="col-md-2 control-label">保函额度（万元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.tenderBondMoney" name="tenderBondMoney" ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0" />
                                </div>

                                <label class="col-md-2 control-label">保函方式</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'保函方式'}:true"
                                            ng-model="vm.item.tenderBondType" class="form-control"
                                            ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0"></select>
                                </div>
                            </div>--%>
                            <div class="form-group">
                                <label  class="col-md-2 control-label">项目备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark"  name="remark" maxlength="200" placeholder="" />
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
<%--<div class="portlet box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-thumb-tack"></i> 客户信息
        </div>
        <div  class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="vm.addCustomer(vm.item.id);" >
                <i class="fa fa-save"></i> 保存客户信息 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="vm.showCustomer(vm.item.customerId);" >
                <i class="fa fa-save"></i> 完善客户信息 </a>

        </div>

    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label required" style="font-weight: bold;">客户名称</label>
                    <div class="col-md-4">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.item.customerName" name="customerName"  required="true" ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0" />
                            <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectCustomer();" ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0"><i class="fa fa-user"></i></button>
                                        </span>
                        </div>
                    </div>
                    <label class="col-md-2 control-label required">客户编号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.item.customerCode" ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0"
                               required="true" maxlength="50"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">客户地址</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="" ng-model="vm.item.customerAddress"  ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0"/>
                    </div>
                    <label class="col-md-2 control-label required">联系人</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkMan" name="linkMan" required="true" ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">联系人职务</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkTitle" name="linkTitle" required="true"   ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0"/>
                    </div>
                    <label class="col-md-2 control-label required">联系电话</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkTel" name="linkTel" required="true"   ng-disabled="!processInstance.firstTask&&processInstance.myRunningTaskName.indexOf('经营发展部人员')<0"/>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>--%>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<div class="modal fade draggable-modal" id="selectSubpackageModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">对内分包合同库</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>分包合同名称</th>
                            <th>分包合同号</th>
                            <th>发包部门名称</th>
                            <th>主合同名称</th>
                            <th>主合同号</th>
                            <th style="width: 100px">主合同金额(万元)</th>
                            <th style="width: 60px">经办人</th>
                            <th style="width: 100px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.lists">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.subpackageId==item.id" class="cb_subpackage"
                                       data-name="{{item}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.subContractName"  class="data_title"  ng-click="vm.show(item.id);"><strong ></strong>
                            </td>
                            <td ng-bind="item.subContractNo" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.deptName" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.contractName"></td>
                            <td ng-bind="item.contractNo"></td>
                            <td ng-bind="item.contractMoney"></td>
                            <td ng-bind="item.creatorName"></td>
                            <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                        </tr>

                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectSubpackage();">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="selectCustomerModal" tabindex="-1" role="basic" aria-hidden="true">
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
                <div class="table-scrollable" style="max-height: 400px;overflow-y: auto">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>客户名称</th>
                            <th>客户编号</th>
                            <th>客户地址</th>
                            <th>录入部门</th>
                            <th>联系人</th>
                            <th>联系电话</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="c in vm.customers|filter:{name:vm.qCustomer}">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.customerName==c.name" class="cb_customer" data-name="{{c}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="c.name"></td>
                            <td ng-bind="c.code"></td>
                            <td ng-bind="c.address"></td>
                            <td ng-bind="c.deptName"></td>
                            <td ng-bind="c.linkMan"></td>
                            <td ng-bind="c.linkTel"></td>
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
<jsp:include page="../print/print.jsp"/>