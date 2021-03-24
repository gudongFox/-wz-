<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 勘察与开发策划书
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/arrange/exportData.json?id='+vm.arrangeId}}" target="_blank">
                <i class="fa fa-file-word-o"></i> 导出</a>
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
                     style="height: 780px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stageName" disabled/>
                                </div>
                                <label class="col-md-2 control-label">分期名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stepName" disabled/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label">勘察阶段</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'勘察阶段'}:true"
                                            ng-model="vm.item.exploreStage" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label">工程号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo"
                                           disabled="disabled"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">建设单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionOrg"
                                           name="constructionOrg" required="true" maxlength="30"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">联系人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionLink"
                                           name="constructionLink" required="true" maxlength="20"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">野外测量-测量</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.measureUserName"
                                               name="measureUserName" required="true" maxlength="20"
                                              disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.measureUser;vm.opt ='measureUser';vm.optName ='野外测量-测量';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">野外测量-校核</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.measureProofreadName"
                                               name="measureProofreadName" required="true" maxlength="20"
                                              disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.measureProofread;vm.opt ='measureProofread';vm.optName ='野外测量-校对';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">野外测量-审核</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.measureAuditName"
                                               name="measureAuditName" required="true" maxlength="20"
                                              disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.measureAudit;vm.opt ='measureAudit';vm.optName ='野外测量-审核';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">野外钻探-钻探机长</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.exploreCommanderName"
                                               name="exploreCommanderName" required="true" maxlength="20"
                                            disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.exploreCommander;vm.opt ='exploreCommander';vm.optName ='野外钻探-钻探机长';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">野外钻探-现场编录</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.exploreWriterName"
                                               name="exploreWriterName" required="true" maxlength="20"
                                             disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.exploreWriter;vm.opt ='exploreWriter';vm.optName ='野外钻探-现场编录';vm.optUser=vm.item.chargeMen;vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">野外钻探-现场负责</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.exploreOnChargeName"
                                               name="exploreOnChargeName" required="true" maxlength="20"
                                              disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.exploreOnCharge;vm.opt ='exploreOnCharge';vm.optName ='野外钻探-现场负责';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">野外钻探-项目负责</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.exploreChargeName"
                                               name="exploreChargeName" required="true" maxlength="20"
                                               disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.exploreCharge;vm.opt ='exploreCharge';vm.optName ='野外钻探-项目负责';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">资料整理-技术员</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.docWriterName"
                                               name="docWriterName" required="true" maxlength="20"
                                               disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.docWriter;vm.opt ='docWriter';vm.optName ='资料整理-技术员';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">资料整理-校核</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.docProofreadName"
                                               name="docProofreadName" required="true" maxlength="20"
                                               disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.docProofread;vm.opt ='docProofread';vm.optName ='资料整理-校核';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">资料整理-审核</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.docAuditName"
                                               name="docAuditName" required="true" maxlength="20"
                                              disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.docAudit;vm.opt ='docAudit';vm.optName ='资料整理-审核';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">资料整理-项目负责</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.docChargeName"
                                               name="docChargeName" required="true" maxlength="20"
                                               disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.docCharge;vm.opt ='docCharge';vm.optName ='资料整理-项目负责';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">勘察采用软件</label>
                                <div class="col-md-4">
                                    <input type="text" disabled="disabled" class="form-control" name="softwareName"
                                           required="true" ng-model="vm.item.softwareName" name="constructionLink"
                                           required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">野外钻探时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="exploreTime">
                                        <input type="text" class="form-control" name="exploreTime" required="true"
                                               ng-model="vm.item.exploreTime"  ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"  ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">资料整理时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="docTime">
                                        <input type="text" class="form-control" name="docTime" required="true"
                                               ng-model="vm.item.docTime" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"  ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">计划评审时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="planTime">
                                        <input type="text" class="form-control" name="planTime" required="true"
                                               ng-model="vm.item.planTime" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"  ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">计划验收时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="planCheckTime">
                                        <input type="text" class="form-control" name="planCheckTime" required="true"
                                               ng-model="vm.item.planCheckTime" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">计划确认时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="planConfirmTime">
                                        <input type="text" class="form-control" name="planConfirmTime" required="true"
                                               ng-model="vm.item.planConfirmTime"
                                               ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"  ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">审查人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.examineManName"
                                               name="examineManName" required="true" maxlength="20"
                                               disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.examineMan;vm.opt ='examineMan';vm.optName ='审查人';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">审定人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.approveManName"
                                               name="approveManName" required="true" maxlength="20"
                                               disabled/>
                                        <span class="input-group-btn">
                                        <button class="btn default" type="button"
                                                ng-click="vm.currentUser=vm.item.approveMan;vm.opt ='approveMan';vm.optName ='审定人';vm.showSelectEmployeeModal();"
                                                ng-disabled="!processInstance.firstTask"><i
                                                class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"
                                           maxlength="100" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName"
                                           name="creatorName"
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
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="selectEmployeeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="vm.optName+' 人员任命'"></h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">
                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="姓名"
                                   ng-model="vm.qEmployee"/>
                        </div>
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="部门"
                                   ng-model="vm.qEmployeeDeptName"/>
                        </div>
                        <div class="col-md-8">
                            <a class="btn green btn-sm" ng-click="vm.loadEmployee();"><i class="fa fa-search"></i> 查询
                            </a>
                        </div>
                    </div>
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 50px;">序号</th>
                                <th>姓名</th>
                                <th>登录名</th>
                                <th>所属部门</th>
                                <th>员工专业</th>
                                <th>联系电话</th>
                                <th>员工类别</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.employees|filter:{deptName:vm.qEmployeeDeptName}|filter:{userName:vm.qEmployee}">
                                <td>
                                    <input type="checkbox" class="cb_employee"
                                           ng-checked="vm.optUser.indexOf(item.userLogin)>=0"
                                           data-id="{{item.userLogin}}" data-name="{{item.userName}}"
                                           style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="item.userName"></td>
                                <td ng-bind="item.userLogin"></td>
                                <td>
                                    <span ng-bind="item.deptName"></span>
                                </td>
                                <td ng-bind="item.specialty"></td>
                                <td ng-bind="item.mobile"></td>
                                <td ng-bind="item.userType">
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectEmployee();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="./print/print-arrangeDetail.jsp"/>

