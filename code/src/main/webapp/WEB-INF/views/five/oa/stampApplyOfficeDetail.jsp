<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:56})">用印管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">用印审批</a>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-doc"></i> 用印审批
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" >
                <i class="fa fa-print"></i> 打印 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_detail_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 550px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">用印事由</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control"
                                              ng-model="vm.item.item" name="item" ng-disabled="!processInstance.firstTask"
                                              required="true"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">用印时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="stampTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.stampDate" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                                <button class="btn default" type="button" ng-disabled="!processInstance.firstTask">
                                                    <i class="fa fa-calendar"></i>
                                                </button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">文件份数</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.fileCount"
                                           placeholder="文件份数"
                                           ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required" >事项归口管理部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.itemDeptName" name="itemDeptName" required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal('itemDept');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">文件/图纸名称</label>
                                <div class="col-md-4">
                                    <input  class="form-control" ng-model="vm.item.fileName" name="fileName"
                                            ng-disabled="!processInstance.firstTask" required="true" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">印章类型</label><%--办公室印章审批类型--%>
                                    <div class="col-md-10">
                                        <div class="icheck-inline" ng-disabled="!processInstance.firstTask">
                                            <label><input type="checkbox"  ng-checked=" vm.item.stampName.indexOf('公司章')>=0" value="公司章" ng-click="vm.checkBoxType('stampName','公司章')" class="icheck" ng-disabled="!processInstance.firstTask">公司章</label>
                                            <label><input type="checkbox" ng-checked=" vm.item.stampName.indexOf('法人章')>=0" value="法人章" ng-click="vm.checkBoxType('stampName','法人章')" class="icheck" ng-disabled="!processInstance.firstTask">法人章</label>
                                            <label><input type="checkbox" ng-checked=" vm.item.stampName.indexOf('法人签名')>=0" value="个人存储" ng-click="vm.checkBoxType('stampName','法人签名')" class="icheck" ng-disabled="!processInstance.firstTask">法人签名</label>
                                            <label><input type="checkbox" ng-checked=" vm.item.stampName.indexOf('保密办用印')>=0" value="涉密邮件" ng-click="vm.checkBoxType('stampName','保密办用印')" class="icheck" ng-disabled="!processInstance.firstTask">保密办用印</label>
                                        </div>
                                    </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">是否需法律审核</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.legalReview" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label">合同是否签订</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.contracted" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目是否备案</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.record" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label required">盖章内容是否包含军工军队保密协议</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.secrecy" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">


                                <label class="col-md-2 control-label">项目号或合同号</label>
                                <div class="col-md-4">
                                    <input  class="form-control ng-pristine ng-untouched ng-valid ng-empty ng-valid-maxlength ng-valid-required"
                                            ng-model="vm.item.contractNo" name="contractNo" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input  class="form-control ng-pristine ng-untouched ng-valid ng-empty ng-valid-maxlength ng-valid-required"
                                            ng-model="vm.item.remark" name="remark" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true" readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal('dept');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">申请人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName" name="creatorName" readonly/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">创建时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
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
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 500px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 500px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>

