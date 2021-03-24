<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:51})">信息化审批</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">涉密信息系统账户及权限开通、变更</span>
        </li>

    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">涉密信息系统账户及权限开通、变更</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
              <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6"><%-- ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>
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
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">姓名</label>
                                <div class="col-md-4">

                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.applyUserName" name="applyUserName" required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('user');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.applyUserNo" name="applyUserNo" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                             <div class="form-group">
                                 <label class="col-md-2 control-label required">部门</label>
                                 <div class="col-md-4">
                                     <div class="input-group">
                                         <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true" disabled />
                                         <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                     </div>
                                 </div>
                                 <label class="col-md-2 control-label required">电话</label>
                                 <div class="col-md-4">
                                     <input type="text" class="form-control" ng-model="vm.item.phone" name="phone" required="true"   ng-disabled="!processInstance.firstTask"/>
                                 </div>
                             </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">系统名称</label>
                                <div class="col-md-10">
                                    <div class="icheck-inline" ng-disabled="!processInstance.firstTask">
                                        <label><input type="checkbox"  ng-checked=" vm.item.systemName.indexOf('安全保密管理平台')>=0" value="安全保密管理平台" ng-click="vm.checkBoxType('systemName','安全保密管理平台')" class="icheck" ng-disabled="!processInstance.firstTask">安全保密管理平台</label>
                                        <label><input type="checkbox" ng-checked=" vm.item.systemName.indexOf('打印刻录审计系统')>=0" value="打印刻录审计系统" ng-click="vm.checkBoxType('systemName','打印刻录审计系统')" class="icheck" ng-disabled="!processInstance.firstTask">打印刻录审计系统</label>
                                        <label><input type="checkbox" ng-checked=" vm.item.systemName.indexOf('个人存储')>=0" value="个人存储" ng-click="vm.checkBoxType('systemName','个人存储')" class="icheck" ng-disabled="!processInstance.firstTask">个人存储</label>
                                        <label><input type="checkbox" ng-checked=" vm.item.systemName.indexOf('涉密邮件')>=0" value="涉密邮件" ng-click="vm.checkBoxType('systemName','涉密邮件')" class="icheck" ng-disabled="!processInstance.firstTask">涉密邮件</label>
                                        <label><input type="checkbox" ng-checked=" vm.item.systemName.indexOf('其他')>=0" value="其他" ng-click="vm.checkBoxType('systemName','其他')" class="icheck" ng-disabled="!processInstance.firstTask">其他</label>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">涉密等级</label>
                                <div class="col-md-10">
                                    <div class="icheck-inline" ng-disabled="!processInstance.firstTask" >
                                        <label><input type="checkbox"  ng-checked=" vm.item.secretLevel.indexOf('一般')>=0" value="一般" ng-click="vm.checkBoxType('secretLevel','一般')" class="icheck" ng-disabled="!processInstance.firstTask">一般</label>
                                        <label><input type="checkbox"  ng-checked=" vm.item.secretLevel.indexOf('重要')>=0" value="重要" ng-click="vm.checkBoxType('secretLevel','重要')" class="icheck" ng-disabled="!processInstance.firstTask">重要</label>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请开通变更分类</label>
                                <div class="col-md-10">
                                    <div class="icheck-inline" >
                                        <label><input type="checkbox"  ng-checked=" vm.item.jurisdictionType.indexOf('开通账号')>=0" value="开通账号" ng-click="vm.checkBoxType('jurisdictionType','开通账号')" class="icheck" ng-disabled="!processInstance.firstTask">开通账号</label>
                                        <label><input type="checkbox"  ng-checked=" vm.item.jurisdictionType.indexOf('停用账号')>=0" value="停用账号" ng-click="vm.checkBoxType('jurisdictionType','停用账号')" class="icheck" ng-disabled="!processInstance.firstTask">停用账号</label>
                                        <label><input type="checkbox"  ng-checked=" vm.item.jurisdictionType.indexOf('撤销账号')>=0" value="撤销账号" ng-click="vm.checkBoxType('jurisdictionType','撤销账号')" class="icheck" ng-disabled="!processInstance.firstTask">撤销账号</label>
                                        <label><input type="checkbox"  ng-checked=" vm.item.jurisdictionType.indexOf('账号权限调整')>=0" value="账号权限调整" ng-click="vm.checkBoxType('jurisdictionType','账号权限调整')" class="icheck" ng-disabled="!processInstance.firstTask">账号权限调整</label>
                                        <label><input type="checkbox"  ng-checked=" vm.item.jurisdictionType.indexOf('用户所在单位变更')>=0" value="用户所在单位变更" ng-click="vm.checkBoxType('jurisdictionType','用户所在单位变更')" class="icheck" ng-disabled="!processInstance.firstTask">用户所在单位变更</label>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">帐户分类</label>
                                <div class="col-md-10">
                                    <div class="icheck-inline"  >
                                        <label><input type="checkbox"  ng-checked=" vm.item.accountType.indexOf('普通帐户')>=0" value="普通帐户" ng-click="vm.checkBoxType('accountType','普通帐户')" class="icheck" ng-disabled="!processInstance.firstTask">普通帐户</label>
                                        <label><input type="checkbox"  ng-checked=" vm.item.accountType.indexOf('部门审批帐户')>=0" value="部门审批帐户" ng-click="vm.checkBoxType('accountType','部门审批帐户')" class="icheck" ng-disabled="!processInstance.firstTask">部门审批帐户</label>
                                        <label><input type="checkbox"  ng-checked=" vm.item.accountType.indexOf('系统管理员帐户')>=0" value="系统管理员帐户" ng-click="vm.checkBoxType('accountType','系统管理员帐户')" class="icheck" ng-disabled="!processInstance.firstTask">系统管理员帐户</label>
                                        <label><input type="checkbox"  ng-checked=" vm.item.accountType.indexOf('安全保密管理员')>=0" value="安全保密管理员" ng-click="vm.checkBoxType('accountType','安全保密管理员')" class="icheck" ng-disabled="!processInstance.firstTask">安全保密管理员</label>
                                        <label><input type="checkbox"  ng-checked=" vm.item.accountType.indexOf('安全审计员')>=0" value="安全审计员" ng-click="vm.checkBoxType('accountType','安全审计员')" class="icheck" ng-disabled="!processInstance.firstTask">安全审计员</label>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">开通变更原因</label>
                                <div class="col-md-10">
                                    <input  type="text" class="form-control" ng-model="vm.item.applyReason" name="applyReason"  required  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <input  type="text" class="form-control" ng-model="vm.item.remark" name="remark"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2" style="color: red;text-align: right">网络运维中心实施填写:</label>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">执行情况</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'执行情况'}:true"
                                            ng-model="vm.detail.executeType" class="form-control"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('网络运维中心实施')==-1"></select>
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

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>
