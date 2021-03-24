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
            <span ng-bind="tableName">工程设计任务启动（超前任务单）</span>
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
            <i class="icon-clock"></i> <span ng-bind="tableName">工程设计任务启动（超前任务单）</span>
            <small ng-bind="vm.item.contractNo"></small>
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" >
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
                     style="min-height: 500px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">客户名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.customerName" name="customerName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectCustomer();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
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
                                <label class="col-md-2 control-label required">备案项目名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.recordProjectName"
                                               name="recordProjectName" required="true"
                                               readonly/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showRecordModal();"
                                                    ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask">项目备案请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">项目号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo"
                                           name="contractNo" required="true" maxlength="20" placeholder="项目号" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-10">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName" ng-disabled="!processInstance.firstTask"
                                               name="projectName" required="true"/>
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
                                                    ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">投资额(万元)</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.investMoney"
                                           placeholder="单位(万元),保留2位小数"
                                           name="investMoney" maxlength="20" required
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">建设地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectAddress"
                                           name="projectAddress" required="true"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">建设规模</label>
                                <div class="col-md-4">
                                    <input class="form-control" ng-model="vm.item.constructionArea"
                                           placeholder="单位(万㎡)"
                                           name="constructionArea" required
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">投资来源</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'投资来源'}:true"
                                            ng-model="vm.item.investSource" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select> </div>
                                <label class="col-md-2 control-label required">项目性质</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'五洲项目类型'}:true"
                                            ng-model="vm.item.projectNature" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">预计开始时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="planBeginTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.planBeginTime" required="true"
                                               ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                            ng-disabled="!processInstance.firstTask"><i
                                            class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label required">预计结束时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="planEndTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.planEndTime" required="true"
                                               ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                 <button class="btn default" type="button"
                                         ng-disabled="!processInstance.firstTask"><i
                                         class="fa fa-calendar"></i></button></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">总设计师</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.totalDesignerName" name="totalDesignerName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('totalDesigner');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">项目经理</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectManagerName" name="projectManagerName"  required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('projectManager');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">主要涉及内容</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control"
                                              ng-model="vm.item.designContent"
                                              name="designContent" required="true"
                                              ng-disabled="!processInstance.firstTask"/>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask">主要设计内容,太多请用附件补充</span>
                                </div>

                            </div>
                            <div class="form-group">
                               <%-- <label class="col-md-2 control-label required">涉及专业</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.designMajor" readonly required
                                               name ="designMajor"/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showSelectMajorListModal();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>--%>

                                <label class="col-md-2 control-label required">非常规涉及条件情形的说明</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.preDesc"  name ="preDesc" required  readonly
                                               />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showSelectPreDescModal();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">应对措施及具备的条件</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.preCondition" readonly required
                                               name ="preCondition"/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showSelectPreConditionModal();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"
                                          ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label">应对措施及具备的条件补充说明</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.preConditionRemark" name="preConditionRemark"
                                           maxlength="100" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <%--<label class="col-md-2 control-label required">设计人员安排截止时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="arrangeEndTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.arrangeEndTime" required="true"
                                               ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                                        <button class="btn default" type="button"
                                                                ng-disabled="!processInstance.firstTask"><i
                                                                class="fa fa-calendar"></i></button>
                                                        </span>
                                    </div>
                                </div>--%>

                                    <label class="col-md-2 control-label">备注</label>
                                    <div class="col-md-10">
                                        <input type="text" class="form-control" ng-model="vm.item.remark"
                                               name="remark"
                                               maxlength="100" ng-disabled="!processInstance.firstTask"/>
                                    </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">其他不满足相关条件的情形及措施</label>
                                <div class="col-md-10">
                                    <textarea type="text" class="form-control"
                                              ng-model="vm.item.otherSituation"
                                              name="otherSituation"
                                              ng-disabled="!processInstance.firstTask"/>
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
                            <input type="text" class="form-control" ng-model="vm.item.customerName" name="customerName"  required="true" ng-disabled="!processInstance.firstTask" />
                            <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.selectCustomer();" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                        </div>
                    </div>
                    <label class="col-md-2 control-label required">客户编号</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="客户编号" ng-model="vm.item.customerCode" ng-disabled="!processInstance.firstTask"
                               required="true" maxlength="50"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">客户地址</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" placeholder="" ng-model="vm.item.customerAddress"  ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label required">联系人</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkMan" name="linkMan" required="true" ng-disabled="!processInstance.firstTask" />
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">联系人职务</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkTitle" name="linkTitle" required="true"   ng-disabled="!processInstance.firstTask"/>
                    </div>
                    <label class="col-md-2 control-label required">联系电话</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.linkTel" name="linkTel" required="true"   ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>--%>

<div class="modal fade draggable-modal" id="selectRecordModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">项目信息备案</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-4">
                        <input type="text" class="form-control input-sm" placeholder="项目名称"
                               ng-model="vm.qProject"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>项目名称</th>
                            <th style="width: 80px;">项目类型</th>
                            <th>客户名称</th>
                            <th>所属部门</th>
                            <th style="width: 110px;">投资额(万元)</th>
                            <th>参与人</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="record in vm.listRecord|filter:{projectName:vm.qProject}">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.recordId==record.id" class="cb_record"
                                       data-name="{{record}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="record.projectName" class="data_title" ng-click="vm.show(item.id);"></td>
                            <td ng-bind="record.projectType"></td>
                            <td ng-bind="record.customerName"></td>
                            <td ng-bind="record.deptName"></td>
                            <td ng-bind="record.projectInvest|currency : '￥'"></td>
                            <td ng-bind="record.businessUserName"></td>
                        </tr>

                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectRecord();">确认</button>
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

<div class="modal fade" id="selectPreDescModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择设计专业</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="preDesc in vm.preDescs">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_preDesc"
                           ng-checked="vm.item.preDesc.indexOf(preDesc.name)>=0"
                           data-name="{{preDesc.name}}"/> <span ng-bind="preDesc.name"
                                                                class="margin-right-10"
                                                                style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%6==0"/>
               </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.savePreDesc();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade" id="selectPreConditionModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择应对措施及具备的条件</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="preCondition in vm.preConditions">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_preCondition"
                           ng-checked="vm.item.preCondition.indexOf(preCondition.name)>=0"
                           data-name="{{preCondition.name}}"/> <span ng-bind="preCondition.name"
                                                                     class="margin-right-10"
                                                                     style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%4==0"/>
               </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.savePreCondition();">保存</button>
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
                           ng-checked="vm.item.stageName.indexOf(stage.name)>=0"
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
                <div class="table-scrollable" style="max-height: 400px">
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

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'" ng-init="fileTplTitle='业务附件'"></div>
<jsp:include page="../print/print.jsp"/>