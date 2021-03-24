<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>

        <li>
            <span>项目投标报名申请表</span>
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
            <i class="icon-note"></i>项目投标报名申请表
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
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
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">投标所室</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="" ng-model="vm.item.deptName" disabled="disabled"
                                    />
                                </div>
                                <label class="col-md-2 control-label required">是否挂网</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否挂网'}:true"
                                            ng-model="vm.item.online" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName"
                                               name="projectName" required="true" ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn">
										<button class="btn default" type="button" ng-click="vm.listRecord()" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">项目规模</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectScale" name="projectScale" required="true" maxlength="10"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">客户名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.customerName" name="customerName" required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn"  >
												<button class="btn default" type="button" ng-click="vm.selectCustomer();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
												</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">代理公司</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.agentName" name="agentName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.listAgent();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">业务内容</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'合同类型'}:true"
                                            ng-model="vm.item.projectType" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">经营模式</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'经营模式'}:true"
                                            ng-model="vm.item.businessType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">投标方式</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'投标方式'}:true"
                                            ng-model="vm.item.attendType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">开标时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="openTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.openTime" name="openTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">其他说明</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.remark"  name="remark" maxlength="200"
                                              ng-disabled="!processInstance.firstTask" placeholder=""></textarea>
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
                                           value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>
        </div>


    </div>
</div>

<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="../common/common-actFlow.jsp"/>

<jsp:include page="part-customer.jsp"/>

<div class="modal fade" id="selectAgentModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">代理公司</h4>
            </div>
            <div class="modal-body">

                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="关键字"
                               ng-model="vm.qAgent"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>公司名称</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="agent in vm.agents|filter:vm.qAgent">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.agentName==agent" class="cb_agent" data-name="{{agent}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="agent"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectAgent();">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="recordModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" >项目信息备案</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.qName"/>
                        </div>
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="部门名称"
                                   ng-model="vm.deptName"/>
                        </div>
                    </div>
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>项目名称</th>
                                <th>客户名称</th>
                                <th>部门名称</th>
                                <th style="width: 90px;">经办人</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="record in vm.list|filter:{projectName:vm.qName}|filter:{deptName:vm.deptName}">
                                <td>
                                    <input type="checkbox" class="cb_record" ng-checked="vm.item.recordId==record.id"  data-name="{{record}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="record.projectName"></td>
                                <td ng-bind="record.customerName"></td>
                                <td ng-bind="record.deptName"></td>
                                <td ng-bind="record.creatorName"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.chooseRecord();">保存</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="print/print-bidapplyDetail.jsp"/>

