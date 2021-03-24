<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal fade" id="selectEmployeeCommonModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="selectEmployeeCommonModalTitle">人员信息</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-12">
                            <label style="margin-right: 10px;">姓名：<input type="search"
                                                                         class="form-control input-inline input-sm"
                                                                         placeholder="姓名"
                                                                         ng-model="qSelectEmployeeName"
                                                                         ng-keyup="vm.enterEvent($event)"></label>

                            <label style="margin-right: 10px;">部门名称：<input type="search"
                                                                           class="form-control input-inline input-sm"
                                                                           placeholder="部门名称"
                                                                           ng-model="qSelectEmployeeDeptName"></label>

                        </div>
                    </div>
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">#</th>
                                <th>姓名</th>
                                <th>登录名</th>
                                <th>所属部门</th>
                                <th>专业</th>
                                <th>设计</th>
                                <th>校核</th>
                                <th>审核</th>
                                <th>审定</th>
                                <th>总设计师</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in selectEmployees|filter:{deptName:qSelectEmployeeDeptName}|filter:{userName:qSelectEmployeeName}"
                                ng-show="$index<50">
                                <td>
                                    <input type="checkbox" class="cb_select_employee"
                                           ng-checked="qSelectUserLoginList.indexOf(','+item.userLogin+',')>=0"
                                           data-name="{{item.userLogin}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="item.userName"></td>
                                <td ng-bind="item.userLogin"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.specialty"></td>
                                <td ng-bind="item.hrQualify.design?'√':'x'"
                                    style="color: {{item.hrQualify.design?'green':'red'}}"></td>
                                <td ng-bind="item.hrQualify.proofread?'√':'x'"
                                    style="color: {{item.hrQualify.proofread?'green':'red'}}"></td>
                                <td ng-bind="item.hrQualify.audit?'√':'x'"
                                    style="color: {{item.hrQualify.audit?'green':'red'}}"></td>
                                <td ng-bind="item.hrQualify.approve?'√':'x'"
                                    style="color: {{item.hrQualify.approve?'green':'red'}}"></td>
                                <td>
                                    <label ng-if="(item.hrQualify.chiefDesigner||item.hrQualify.proChief)"
                                           style="color: green;">
                                        √ <span ng-if="!item.hrQualify.chiefDesigner" style="font-size: 6px;">兼</span>
                                    </label>
                                    <span ng-if="!item.hrQualify.chiefDesigner&&!item.hrQualify.proChief"
                                          style="color: red;">×</span>

                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="saveSelectEmployee();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade draggable-modal" id="commonSelectEmployeeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog" style="width: 62%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="selectEmployeeConfig.title"></h4>
            </div>
            <div class="modal-body" style="padding: 10px 5px 5px 5px;">
                <div class="row">
                    <div class="col-md-4" style="padding-right: 5px;">
                        <div class="portlet box blue" style="margin-bottom: 0px;">
                            <div class="portlet-title">
                                <div class="caption">
                                    <i class="fa fa-tree"></i> 组织机构
                                </div>
                            </div>
                            <div class="portlet-body" style="height: 580px;overflow-y:auto">
                                <%--  <div class="scroller">--%>
                                <div id="employee_dept_tree_"></div>
                                <%--  </div>--%>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-8" style="padding-left: 0px;">
                        <div class="portlet  box blue" style="margin-bottom: 0px;">
                            <div class="portlet-title">
                                <div class="caption">
                                    <i class="fa fa-user"></i> 用户列表
                                </div>
                            </div>
                            <div class="portlet-body" style="height: 580px;overflow-y: auto;overflow-x: hidden;">
                                <div class="row">
                                    <div class="col-md-12">
                                        <label style="margin-right: 10px;">姓名：<input type="text"
                                                                                     class="form-control input-inline input-sm"
                                                                                     placeholder="姓名|工号"
                                                                                     ng-model="selectEmployeeParam.name"
                                                                                     style="width: 80px;"></label>
                                        <label style="margin-right: 10px;">专业：<input type="text"
                                                                                     class="form-control input-inline input-sm"
                                                                                     placeholder="专业"
                                                                                     ng-model="selectEmployeeParam.specialty"
                                                                                     style="width: 80px;"></label>
                                        <label style="margin-right: 10px;">职位：<input type="text"
                                                                                     class="form-control input-inline input-sm"
                                                                                     placeholder="职位"
                                                                                     ng-model="selectEmployeeParam.position"
                                                                                     style="width: 80px;"></label>
                                        <label style="margin-right: 10px;">设计资格：
                                            <select ng-options="s.code  as s.name for s in sysCodes | filter:{codeCatalog:'设计资格'}:true"
                                                    ng-model="selectEmployeeParam.qualify"
                                                    class="form-control input-inline input-sm"></select>
                                            <a class="btn green btn-sm defaultBtn" ng-click="querySelectEmployee_();"><i
                                                    class="fa fa-search"></i> 查询 </a>

                                    </div>
                                </div>
                                <div class="full-height-content full-height-content-scrollable"
                                     style="margin-bottom: 0px !important;margin-top: 0px !important;">
                                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                        <thead>
                                        <tr>
                                            <th style="width: 30px;">#</th>
                                            <th>姓名</th>
                                            <th>登录名</th>
                                            <th>专业</th>
                                            <th>职务</th>
                                            <th>设计</th>
                                            <th>校核</th>
                                            <th>审核</th>
                                            <th>审定</th>
                                            <th ng-if="selectEmployeeParam.appCode=='wuzhou'">总设计师</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr ng-repeat="item in selectEmployeePageInfo.list">
                                            <td>
                                                <input type="checkbox" ng-click="toggleSelectedUser_(item.userLogin);"
                                                       ng-checked="selectEmployeeConfig.userLoginList.indexOf(','+item.userLogin+',')>=0"
                                                       data-name="{{item.userLogin}}"
                                                       style="width: 15px;height: 15px;"/>
                                            </td>
                                            <td ng-bind="item.userName"></td>
                                            <td ng-bind="item.userLogin"></td>
                                            <td ng-bind="item.specialty"></td>
                                            <td ng-bind="item.shortPositionNames" title="{{item.positionNames}}"></td>
                                            <td ng-bind="item.design?'√':'x'"
                                                style="color: {{item.design?'green':'red'}}"></td>
                                            <td ng-bind="item.proofread?'√':'x'"
                                                style="color: {{item.proofread?'green':'red'}}"></td>
                                            <td ng-bind="item.audit?'√':'x'"
                                                style="color: {{item.audit?'green':'red'}}"></td>
                                            <td ng-bind="item.approve?'√':'x'"
                                                style="color: {{item.approve?'green':'red'}}"></td>
                                            <td ng-if="selectEmployeeParam.appCode=='wuzhou'">
                                                <label ng-if="(item.chiefDesigner||item.proChief)"
                                                       style="color: green;">
                                                    √ <span ng-if="!item.chiefDesigner"
                                                            style="font-size: 6px;">兼</span>
                                                </label>
                                                <span ng-if="!item.chiefDesigner&&!item.proChief"
                                                      style="color: red;">×</span>

                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <my-pager data-page-info="selectEmployeePageInfo"
                                          on-load="loadSelectEmployee_();"></my-pager>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <label style="float: left;">
                    <i class="fa fa-users" ng-show="selectEmployeeConfig.multiple" title="可选多个用户"></i>
                    <i class="fa fa-user" ng-show="!selectEmployeeConfig.multiple" title="仅选一个用户"></i>
                    <span style="color: forestgreen;font-weight: bold;"
                          ng-bind="'已选用户('+selectedUsers_.length+')：'"></span>
                    <label ng-repeat="selectedUser in selectedUsers_">
                        <span ng-bind="selectedUser.userName">罗冬</span><i class="fa fa-times font-red"
                                                                          style="margin-right: 5px;"
                                                                          ng-click="removeSelectedUser_(selectedUser.userLogin);"></i>
                    </label>
                </label>
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="saveSelectEmployee_();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="OaSelectEmployeeModal" style="z-index: 99999" tabindex="-1" role="basic"
     aria-hidden="true">
    <div class="modal-dialog" style="width: 50%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="selectOaEmployeeConfig.title"></h4>
            </div>
            <div class="modal-body" style="padding: 10px 5px 5px 5px;">
                <table style="width: 100%;">
                    <tr>
                        <td rowspan="1" style="width: 35%;">
                            <div class="portlet box blue" style="margin-bottom: 0px;">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="fa fa-tree"></i>
                                        <span ng-if="selectOaEmployeeParam.type=='部门'">组织机构</span>
                                        <span ng-if="selectOaEmployeeParam.type=='人员'">组织机构</span>
                                        <span ng-if="selectOaEmployeeParam.type=='角色'">角色信息</span>
                                    </div>
                                    <div class="tools" style="width: 70%;float:right;">
                                        <input type="text" id="autocomplete" placeholder="关键字"
                                               ng-model="selectOaEmployeeParam.keyInAll"
                                               style=" color:black;height: 25px;width: 80%; border-color:white;"/>
                                        <i class="icon-magnifier"></i>
                                    </div>
                                </div>
                                <div class="portlet-body">
                                    <div class="scroller" style="height: 550px;">
                                        <div id="oa_employee_dept_tree_"></div>
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td style="width: 65%;">
                            <div class="portlet box blue" style="margin-bottom: 0px;margin-left: 5px">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="fa fa-users" ng-show="selectOaEmployeeConfig.multiple"
                                           title="可选多个用户"></i>
                                        <i class="fa fa-user" ng-show="!selectOaEmployeeConfig.multiple"
                                           title="仅选一个用户"></i>
                                        <span style="font-weight: bold;" ng-if="selectOaEmployeeConfig.type!='选部门'"
                                              ng-bind="'已选用户('+selectedOaUsers_.length+')'"></span>
                                        <span style="font-weight: bold;" ng-if="selectOaEmployeeConfig.type=='选部门'"
                                              ng-bind="'已选部门('+selectedOaDepts_.length+')'"></span>
                                    </div>
                                </div>
                                <div class="portlet-body" style="height: 572px;overflow-y: auto;overflow-x: hidden;">
                                    <%-- <div class="table-scrollable" style="margin-bottom: 0px !important;margin-top: 5px !important;">--%>
                                    <table class="table  table-hover table-bordered table-advance no-footer"
                                           id="oaSelectEmployeeTable">
                                        <label ng-if="selectOaEmployeeConfig.type!='选部门'"
                                               ng-repeat="selectedUser in selectedOaUsers_">
                                            <span style="font-size: 18px;margin-left: 10px"
                                                  ng-if="selectOaEmployeeConfig.type=='部门'"
                                                  ng-bind="selectedUser.userName"
                                                  ng-click="queryOaDeptSelectEmployee_(selectedUser.deptId,selectedUser.userLogin)">罗冬</span>
                                            <span style="font-size: 18px;margin-left: 10px"
                                                  ng-if="selectOaEmployeeConfig.type=='领导'"
                                                  ng-bind="selectedUser.userName"
                                                  ng-click="queryOaDeptSelectEmployee_(selectedUser.deptId,selectedUser.userLogin)">罗冬</span>
                                            <span style="font-size: 18px;margin-left: 10px"
                                                  ng-if="selectOaEmployeeConfig.type=='角色'"
                                                  ng-bind="selectedUser.userName"
                                                  ng-click="queryOaRoleSelectEmployee_(selectedUser.roleIds,selectedUser.userLogin)">罗冬</span>
                                            <span style="font-size: 18px;margin-left: 10px"
                                                  ng-if="selectOaEmployeeConfig.type=='人员'"
                                                  ng-bind="selectedUser.userName"></span>
                                            <i style="width: 20px;height: 20px" class="fa fa-times font-red"
                                               style="margin-right: 10px;"
                                               ng-click="removeOaSelectedUser_(selectedUser.userLogin);"></i>
                                        </label>
                                        <label ng-if="selectOaEmployeeConfig.type=='选部门'"
                                               ng-repeat="dept in selectedOaDepts_">
                                            <span style="font-size: 18px;margin-left: 10px"
                                                  ng-bind="dept.name">罗冬</span>
                                            <i style="width: 20px;height: 20px" class="fa fa-times font-red"
                                               style="margin-right: 10px;"
                                               ng-click="removeOaSelectedDept_(dept.id);"></i>
                                        </label>
                                    </table>
                                    <%-- </div>--%>
                                </div>
                            </div>
                        </td>
                    </tr>
                    <%--                    <tr>
                                            <td>
                                                <div class="portlet box blue" style="margin-bottom: 5px;margin-left: 5px">
                                                    <div class="portlet-title">
                                                        <div class="caption">
                                                            <i class="fa fa-users" ng-show="selectOaEmployeeConfig.multiple" title="可选多个用户"></i>
                                                            <i class="fa fa-user" ng-show="!selectOaEmployeeConfig.multiple" title="仅选一个用户"></i>
                                                            <span style="font-weight: bold;" ng-if="selectOaEmployeeConfig.type!='选部门'" ng-bind="'已选用户('+selectedOaUsers_.length+')'"></span>
                                                            <span style="font-weight: bold;" ng-if="selectOaEmployeeConfig.type=='选部门'" ng-bind="'已选部门('+selectedOaDepts_.length+')'"></span>
                                                        </div>
                                                    </div>
                                                    <div class="portlet-body" style="height: 300px;overflow-y: auto;overflow-x: hidden;">
                                                        <div class="table-scrollable" style="margin-bottom: 0px !important;margin-top: 5px !important;">
                                                            <table class="table  table-hover table-bordered table-advance no-footer" id="oaSelectEmployeeTable">
                                                               <div ng-if="selectOaEmployeeConfig.type!='选部门'" ng-repeat="selectedUser in selectedOaUsers_">
                                                                    <span style="font-size: 18px;margin-left: 10px" ng-if="selectOaEmployeeConfig.type=='部门'"ng-bind="selectedUser.userName" ng-click="queryOaDeptSelectEmployee_(selectedUser.deptId,selectedUser.userLogin)">罗冬</span>
                                                                   <span style="font-size: 18px;margin-left: 10px" ng-if="selectOaEmployeeConfig.type=='角色'"ng-bind="selectedUser.userName" ng-click="queryOaRoleSelectEmployee_(selectedUser.roleIds,selectedUser.userLogin)">罗冬</span>
                                                                    <i style="width: 20px;height: 20px" class="fa fa-times font-red" style="margin-right: 10px;" ng-click="removeOaSelectedUser_(selectedUser.userLogin);"></i>
                                                                </div>
                                                                <div ng-if="selectOaEmployeeConfig.type=='选部门'" ng-repeat="dept in selectedOaDepts_" >
                                                                    <span style="font-size: 18px;margin-left: 10px" ng-bind="dept.name">罗冬</span>
                                                                    <i style="width: 20px;height: 20px" class="fa fa-times font-red" style="margin-right: 10px;" ng-click="removeOaSelectedDept_(dept.id);"></i>
                                                                </div>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </td>
                                        </tr>--%>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="closeOaSelectEmployeeModal_();"
                        data-dismiss="modal">关闭
                </button>
                <button ng-if="selectOaEmployeeConfig.type!='选部门'" type="button" class="btn blue"
                        ng-click="saveSelectEmployee_();">保存
                </button>
                <button ng-if="selectOaEmployeeConfig.type=='选部门'" type="button" class="btn blue"
                        ng-click="saveSelectDept_();">保存
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>


<div class="modal fade draggable-modal" id="selectBudgetModal" style="z-index: 99999" tabindex="-1" role="basic"
     aria-hidden="true">
    <div id="demo-toolbar1" class="btn-group" role="group">
<%--            预算类型:
        <select  style="width: 180px"
                 ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'预算类型'}:true"
                 ng-model="budgetConfig.budgetType" ng-change="getBudgetInformation()"></select>--%>
        当前选择的预算：
        <span style="color: red;font-size: 15px;margin-right: 20px" ng-bind="selectBudgetDetail.typeName"></span>
        预算总金额：
        <span style="color: red;font-size: 15px;margin-left: 10px;margin-right: 20px"
              ng-bind="selectBudgetDetail.budgetMoney"></span>
        预算剩余金额：
        <span style="color: red;font-size: 15px;margin-left: 10px;margin-right: 20px"
              ng-bind="selectBudgetDetail.remainMoney"></span>
    </div>
    <div class="modal-dialog" style="width: 62%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择抵扣预算</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <div class="portlet box blue" style="margin-bottom: 0px;">
                            <div class="portlet-title">
                                <div class="caption">
                                    <i class="fa fa-tree"></i>
                                    <span ng-bind="selectBudget.budgetDeptName+'-'+selectBudget.budgetYear+'预算'"></span>
                                </div>
                            </div>
                            <div class="portlet-body" <%--style="height: 580px;overflow-y:auto"--%>>
                                <%--  <div class="scroller">--%>
                                <div id="budgetTreeTable"
                                     class="table table-striped table-hover table-bordered  no-footer "></div>
                                <%--  </div>--%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="saveSelectBudget_();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="selectBankModal" style="z-index: 99999" tabindex="-1" role="basic"
     aria-hidden="true">
    <div class="portlet  box blue" style="width: 60%; margin-left: 20% ;margin-top: 5%">
        <div class="portlet-title">
            <div class="caption">
                <i class="icon-clock">请选择相应的网银信息</i>
            </div>
        </div>
        <div class="portlet-body">
            <div class="tabbable-custom">
                <ul class="nav nav-tabs ">
                    <li id="selfBankLi" class="active">
                        <a href="#tab_15_self" id="selfBankTab" data-toggle="tab" aria-expanded="true">
                            个人网银信息
                        </a>
                    </li>
                    <li id="companyBankLi" class="">
                        <a href="#tab_15_company" id="companyBankTab" data-toggle="tab" aria-expanded="false">
                            公司网银信息 </a>
                    </li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane active" id="tab_15_self"
                         style="min-height: 400px;">
                        <div class="modal-body">
                            <div class="table-scrollable"
                                 style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th style="width: 80px;">人员姓名</th>
                                        <th style="width: 160px;">身份证号</th>
                                        <th style="width: 160px;">银行名称</th>
                                        <th>银行卡号</th>
                                        <th style="width: 100px;">创建时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="item in selfBankPageInfo.list">
                                        <td class="dt-right">
                                            <input type="checkbox" ng-checked="selectedUserBankNo==item.userBankNo"
                                                   ng-click="getSelectBankInformation()" class="cb_selfBank"
                                                   data-name="{{item}}" style="width: 15px;height: 15px;"/>
                                        </td>
                                        <td ng-bind="item.userName" class="data_title" ng-click="vm.show(item.id);">
                                            <strong></strong></td>
                                        <td ng-bind="item.userIdNo"></td>
                                        <td ng-bind="item.bankName"></td>
                                        <td ng-bind="item.userBankNo"></td>
                                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <my-pager data-page-info="selfBankPageInfo" on-load="loadBankPagedData()"></my-pager>
                        </div>
                    </div>
                    <div class="tab-pane" id="tab_15_company" style="min-height: 400px;">
                        <div class="modal-body">
                            <div class="table-scrollable"
                                 style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th style="width: 160px;">开户账号</th>
                                        <th style="width: 160px;">银行名称</th>
                                        <th>银行卡号</th>
                                        <th style="width: 60px">经办人</th>
                                        <th style="width: 100px;">创建时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="item in companyBanks">
                                        <td class="dt-right">
                                            <input type="checkbox" ng-checked="selectedUserBankNo==item.userBankNo"
                                                   ng-click="getSelectBankInformation()" class="cb_companyBank"
                                                   data-name="{{item}}" style="width: 15px;height: 15px;"/>
                                        </td>
                                        <td ng-bind="item.userName" class="data_title"><strong></strong></td>
                                        <td ng-bind="item.bankName"></td>
                                        <td ng-bind="item.userBankNo"></td>
                                        <td ng-bind="item.creatorName"></td>
                                        <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <div class="btn-group" style="float: left" role="group">当前选择的网银信息类型：
                        <span style="color: red;font-size: 15px;margin-right: 20px" ng-bind="selectBank.type"></span>
                        账号：<span style="color: red;font-size: 15px;margin-left: 10px;margin-right: 20px"
                                 ng-bind="selectBank.bankNo"></span>
                        银行：<span style="color: red;font-size: 15px;margin-left: 10px;margin-right: 20px"
                                 ng-bind="selectBank.bankName"></span>
                    </div>
                    <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn blue" ng-click="saveSelectBank_();">确认</button>
                </div>

            </div>
        </div>
    </div>
</div>

<div class="modal fade draggable-modal" id="commonSelectLibraryModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg" style="width: 70%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10"><i class="icon-note" style="margin-right: 10px"></i>选择合同库中的合同
                </h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="float: left" role="group">当前选择的合同信息：
                    <span style="color: red;font-size: 15px;margin-right: 20px"
                          ng-bind="selectedLibrary.contractName"></span><br>
                    合同号：<span style="color: red;font-size: 15px;margin-left: 10px;margin-right: 20px"
                              ng-bind="selectedLibrary.contractNo"></span>
                    项目号：<span style="color: red;font-size: 15px;margin-left: 10px;margin-right: 20px"
                              ng-bind="selectedLibrary.projectNo"></span>
                </div>
                <div class="table-scrollable">
                    <table id="common" class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>合同名称</th>
                            <th style="width: 120px" class="hidden-md hidden-sm">合同号</th>
                            <th style="width: 80px" class="hidden-md hidden-sm">项目号</th>
                            <th style="width: 100px">签约日期</th>
                            <th style="width: 120px">合同类型</th>
                            <th style="width: 120px" class="hidden-md hidden-sm">合同额(万元)</th>
                            <th style="width: 160px">承接部门</th>
                            <th style="width: 80px">创建人</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td class="dt-right"><i class="fa fa-search" title="查询全数据" style="color: black;width: 26px"
                                                    ng-click="searchLibraryPagedData()"></i></td>
                            <td class="data_title"><input ng-model="libraryParams.contractName" type="text" style="height: 26px"
                                                          class="form-control"></td>
                            <td class="hidden-md hidden-sm"><input ng-model="libraryParams.contractNo" type="text" style="height: 26px"
                                                                   class="form-control"></td>
                            <td class="hidden-md hidden-sm"><input ng-model="libraryParams.projectNo" type="text" style="height: 26px"
                                                                   class="form-control"></td>
                            <td><input ng-model="libraryParams.signTime" type="text" style="height: 26px" class="form-control"></td>
                            <td><input ng-model="libraryParams.contractType" type="text" style="height: 26px" class="form-control"></td>
                            <td class="hidden-md hidden-sm"><input ng-model="libraryParams.contractMoney" type="text"
                                                                   style="height: 26px" class="form-control"></td>
                            <td><input ng-model="libraryParams.deptName" type="text" style="height: 26px"class="form-control"></td>
                            <td><input ng-model="libraryParams.creatorName" type="text"style="height: 26px" class="form-control"></td>
                        </tr>
                        <tr ng-repeat="item in libraryPageInfo.list|filter:{contractName:libraryParams.contractName}|filter:{contractNo:libraryParams.contractNo}
|filter:{projectNo:libraryParams.projectNo}|filter:{signTime:libraryParams.signTime}|filter:{contractType:libraryParams.contractType}|filter:{deptName:libraryParams.deptName}|filter:{creatorName:libraryParams.creatorName}">
                            <td class="dt-right">
                                <input type="checkbox" ng-checked="selectLibraryId==item.id"
                                       ng-click="getSelectLibraryInformation()" class="cb_commonLibrary"
                                       data-name="{{item.id}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.contractName" class="data_title"
                                title="{{'项目备案名称：'+ item.recordProjectName}}"></td>
                            <td ng-bind="item.contractNo" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.projectNo" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.signTime"></td>
                            <td ng-bind="item.contractType"></td>
                            <td ng-bind="item.contractMoney" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.deptName"></td>
                            <td ng-bind="item.creatorName"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <my-pager data-page-info="libraryPageInfo" on-load="loadLibraryPagedData()"></my-pager>
            </div>
            <div class="modal-footer">

                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="saveSelectLibrary_()">确认</button>
            </div>
        </div>
    </div>

</div>

<div class="modal fade draggable-modal" id="commonSelectLibrarySubpackageModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg" style="width: 70%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10"><i class="icon-note" style="margin-right: 10px"></i>选择签出合同库中的合同
                </h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="float: left" role="group">当前选择的合同信息：
                    <span style="color: red;font-size: 15px;margin-right: 20px"
                          ng-bind="selectedLibrarySubpackage.subContractName"></span>
                    合同号：<span style="color: red;font-size: 15px;margin-left: 10px;margin-right: 20px"
                              ng-bind="selectedLibrarySubpackage.subContractNo"></span>
                </div>
                <div class="table-scrollable">
                    <table id="commonLibrarySubpackage" class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>合同名称</th>
                            <th style="width: 120px" class="hidden-md hidden-sm">合同号</th>
                            <th style="width: 180px" class="hidden-md hidden-sm">供方名称</th>
                            <th style="width: 120px">发包部门名称</th>
                            <th style="width: 120px">主合同号</th>
                            <th style="width: 120px" class="hidden-md hidden-sm">合同额(万元)</th>
                            <th style="width: 80px">创建人</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td class="dt-right"><i class="fa fa-search" title="查询全数据" style="color: black;width: 26px"
                                                    ng-click="searchLibrarySubpackagePagedData()"></i></td>
                            <td class="data_title"><input ng-model="librarySubpackageParams.subContractName" type="text" style="height: 26px"
                                                          class="form-control"></td>
                            <td class="hidden-md hidden-sm"><input ng-model="librarySubpackageParams.subContractNo" type="text" style="height: 26px"
                                                                   class="form-control"></td>
                            <td class="hidden-md hidden-sm"><input ng-model="librarySubpackageParams.supplierName" type="text" style="height: 26px"
                                                                   class="form-control"></td>
                            <td><input ng-model="librarySubpackageParams.deptName" type="text" style="height: 26px" class="form-control"></td>
                            <td><input ng-model="librarySubpackageParams.contractNo" type="text" style="height: 26px" class="form-control"></td>
                            <td class="hidden-md hidden-sm"><input ng-model="librarySubpackageParams.contractMoney" type="text"
                                                                   style="height: 26px" class="form-control"></td>
                            <td><input ng-model="librarySubpackageParams.creatorName" type="text"style="height: 26px" class="form-control"></td>
                        </tr>
                        <tr ng-repeat="item in librarySubpackagePageInfo.list|filter:{subContractName:librarySubpackageParams.subContractName}
                        |filter:{subContractNo:librarySubpackageParams.subContractNo}|filter:{supplierName:librarySubpackageParams.supplierName}
                        |filter:{deptName:librarySubpackageParams.deptName}|filter:{contractNo:librarySubpackageParams.contractNo}
                        |filter:{subContractMoney:librarySubpackageParams.subContractMoney}|filter:{creatorName:librarySubpackageParams.creatorName}">
                            <td class="dt-right">
                                <input type="checkbox" ng-checked="selectLibrarySubpackageId==item.id"
                                       ng-click="getSelectLibrarySubpackageInformation()" class="cb_commonLibrarySubpackage"
                                       data-name="{{item.id}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.subContractName" class="data_title"
                                title="{{'项目备案名称：'+ item.recordProjectName}}"></td>
                            <td ng-bind="item.subContractNo" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.supplierName" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.deptName"></td>
                            <td ng-bind="item.contractNo"></td>
                            <td ng-bind="item.subContractMoney" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.creatorName" ></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <my-pager data-page-info="librarySubpackagePageInfo" on-load="loadLibrarySubpackagePagedData()"></my-pager>
            </div>
            <div class="modal-footer">

                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="saveSelectLibrarySubpackage_()">确认</button>
            </div>
        </div>
    </div>

</div>

<div class="modal fade draggable-modal" id="commonSelectRecordModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg" style="width: 70%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10"><i class="icon-note" style="margin-right: 10px"></i>选择备案项目信息
                </h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="float: left" role="group">当前选择的备案信息：
                    <span style="color: red;font-size: 15px;margin-right: 20px"
                          ng-bind="selectedRecord.projectName"></span><br>
                    项目号：<span style="color: red;font-size: 15px;margin-left: 10px;margin-right: 20px"
                              ng-bind="selectedRecord.projectNo"></span>
                    客户名称：<span style="color: red;font-size: 15px;margin-left: 10px;margin-right: 20px"
                              ng-bind="selectedRecord.customerName"></span>
                </div>
                <div class="table-scrollable">
                    <table id="commonRecord" class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th style="min-width: 200px;">项目名称</th>
                            <th style="width: 120px">项目号</th>
                            <th class="hidden-md hidden-sm">客户名称</th>
                            <th style="width: 100px" title="项目类型">项目类型</th>
                            <th style="width: 160px;">部门名称</th>
                            <th class="hidden-md hidden-sm">项目联系人</th>
                            <th style="width: 100px;">创建时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td class="dt-right"><i class="fa fa-search" title="查询全数据" style="color: black;width: 26px"
                                                    ng-click="searchRecordPagedData()"></i></td>
                            <td class="data_title"><input ng-model="recordParams.projectName" type="text" style="height: 26px"
                                                          class="form-control"></td>
                            <td ><input ng-model="recordParams.projectNo" type="text" style="height: 26px"
                                                                   class="form-control"></td>
                            <td class="hidden-md hidden-sm"><input ng-model="recordParams.customerName" type="text" style="height: 26px"
                                                                   class="form-control"></td>
                            <td class="hidden-md hidden-sm"><input ng-model="recordParams.projectType" type="text" style="height: 26px"
                                                                   class="form-control"></td>
                            <td><input ng-model="recordParams.deptName" type="text" style="height: 26px" class="form-control"></td>
                            <td><input ng-model="recordParams.businessUserName" type="text" style="height: 26px"class="form-control"></td>
                            <td></td>
                        </tr>
                        <tr ng-repeat="item in recordPageInfo.list|filter:{projectName:recordParams.projectName}|filter:{projectNo:recordParams.projectNo}
|filter:{deptName:recordParams.deptName}|filter:{customerName:recordParams.customerName}|filter:{businessUserName:recordParams.businessUserName}
|filter:{projectType:recordParams.projectType}">
                            <td class="dt-right">
                                <input type="checkbox" ng-checked="selectRecordId==item.id"
                                       ng-click="getSelectRecordInformation()" class="cb_commonRecord"
                                       data-name="{{item.id}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="item.projectName" class="data_title"></td>
                            <td ng-bind="item.projectNo" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.customerName" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.projectType"></td>
                            <td ng-bind="item.deptName"></td>
                            <td ng-bind="item.businessUserName" class="hidden-md hidden-sm"></td>
                            <td ng-bind="item.gmtCreate|date:'yyyy-MM-dd'"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <my-pager data-page-info="recordPageInfo" on-load="loadRecordPagedData()"></my-pager>
            </div>
            <div class="modal-footer">

                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="saveSelectRecord_()">确认</button>
            </div>
        </div>
    </div>

</div>
