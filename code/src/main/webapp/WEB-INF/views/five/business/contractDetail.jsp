<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">项目启动</span>
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
            <i class="icon-clock"></i> <span ng-bind="tableName">项目启动</span>
            <small ng-bind="vm.item.businessContractDetail.projectNo"></small>
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>>
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
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName"
                                               name="projectName" required="true" ng-disabled="vm.item.contractLibraryId==0||!processInstance.firstTask"/>
                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.showSelectPreOrReviewModal()"
                                                ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">项目号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.businessContractDetail.projectNo"
                                           name="projectNo" required="true" maxlength="20" placeholder="项目号"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                          <%--      <label class="col-md-2 control-label required">合同号</label>
                                <div class="col-md-4">
                                        <input type="text" class="form-control" ng-model="vm.item.contractNo"
                                               name="contractNo" required="true" maxlength="20" placeholder="合同编号"
                                               readonly/>
                                </div>--%>
                            </div>
                            <div class="form-group">
                                <%--<label class="col-md-2 control-label required">保密等级</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'项目密级'}:true"
                                            ng-model="vm.item.businessContractDetail.secType" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>--%>
                                    <label class="col-md-2 control-label" >项目阶段</label>
                                    <div class="col-md-4" >
                                        <div class="input-group">
                                            <input type="text" class="form-control" ng-model="vm.item.stageNames"
                                                   name="stageNames" maxlength="100" required="true"
                                                   disabled/>
                                            <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showStageModel();"
                                                    ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                        </div>
                                    </div>
                                    <label class="col-md-2 control-label required" ng-if="vm.item.stageNames.indexOf('前期')>=0">档案号-前期</label>
                                    <div class="col-md-4" ng-if="vm.item.stageNames.indexOf('前期')>=0">
                                        <input  class="form-control" ng-model="vm.item.recordNoEarly"
                                                name="recordNoEarly" maxlength="20" required="true"
                                                ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"/>
                                    </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required" ng-if="vm.item.stageNames.indexOf('初步')>=0">档案号-初设</label>
                                <div class="col-md-4" ng-if="vm.item.stageNames.indexOf('初步')>=0">
                                    <input  class="form-control" ng-model="vm.item.recordNoFirst"
                                            name="recordNoFirst" maxlength="20" required="true"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"/>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.stageNames.indexOf('施工')>=0">档案号-施工</label>
                                <div class="col-md-4" ng-if="vm.item.stageNames.indexOf('施工')>=0">
                                    <input  class="form-control" ng-model="vm.item.recordNoConstruction"
                                            name="recordNoConstruction" maxlength="20" required="true"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('档案人员')<0"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">投资额(万元)</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.investMoney"
                                            placeholder="单位(万元),保留2位小数"
                                            name="investMoney" maxlength="20" required
                                            ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">建设地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" required="true"
                                           ng-model="vm.item.projectAddress"
                                           name="projectAddress" maxlength="450"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">客户名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.customerName"
                                               name="customerName" required="true" readonly/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showCustomerModal();"
                                                    ng-disabled="!processInstance.firstTask"> <i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                   <span style="font-size: 12px;color: red" ng-bind="'客户编号：'+vm.item.customerCode"></span>
                                </div>
                                <label class="col-md-2 control-label required">联系人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" required="true" ng-model="vm.item.linkMan"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">联系人职务</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" required="true" ng-model="vm.item.linkFax"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-2 control-label required">联系电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" id="linkTel" ng-model="vm.item.linkTel"
                                           name="linkTel" maxlength="30" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">执行所室</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                              disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.item.deptId);vm.opt='deptId'"
                                                    ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label ">合作部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptNames"
                                               name="deptNames" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.item.deptIds);vm.opt='deptIds'"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>
                            
                           <%-- <div class="form-group">
                                <label class="col-md-2 control-label required">
                                    <strong>启用设计管理</strong>
                                </label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否EPC'}:true"
                                            ng-model="vm.item.ed" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">设计管理类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'设计作业类型'}:true"
                                            ng-model="vm.item.projectType" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>--%>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目主管院长</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.chargeMenName"
                                                name="chargeMenName" readonly required/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=user;vm.opt ='chargeMen';vm.optUser=vm.item.chargeMen;vm.showUserModel();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">项目经理</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.businessManagerName"
                                               name="businessManagerName" readonly required/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.opt ='businessManager';vm.optUser=vm.item.businessManager;vm.showUserModel();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目总师</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.businessContractDetail.totalDesignerName"
                                               name="totalDesignerName" readonly required="true"/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.opt ='totalDesigner';vm.optUser=vm.item.businessContractDetail.totalDesigner;vm.showUserModel();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label ">其他总师</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.businessContractDetail.otherDesignerName"
                                               name="otherDesignerName" disabled
                                        />
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.optUser=vm.item.businessContractDetail.otherDesigner;vm.opt='otherDesigner';vm.showUserModel();"
                                                ng-disabled="!processInstance.firstTask">
                                            <i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
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

<div class="modal fade draggable-modal" id="contractStageNameModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">项目阶段</h4>
            </div>
            <div class="modal-body">

                <div class="table-scrollable">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">序号</th>
                            <th>名字</th>
                            <th>编码</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="item in vm.stageNames">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.stageNames.indexOf(item.name)>=0" class="cb_stage"
                                       style="width: 15px;height: 15px;"
                                       name="{{item.code}}"/>
                            </td>
                            <td ng-bind="item.name"></td>
                            <td ng-bind="item.code"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectStageName();">确认</button>
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
                <div class="table-scrollable" style="max-height: 400px;overflow-y: auto">
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
                                <input type="checkbox" ng-checked="vm.item.customerName==c.name" class="cb_customer" data-name="{{c}}" style="width: 15px;height: 15px;"/>
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

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>