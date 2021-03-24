<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css">

    form .form-group {
        padding-bottom: 15px;
        border-bottom: 1px dashed #ebedf2;
    }

    input[type=checkbox], input[type=radio] .cmcu_checkBox {
        width: 18px;
        height: 18px;
        border: none;
        margin: 0px;
    }

</style>

<div class="page-bar" style="margin: -10px 0px 5px 0px;padding:0px;background-color: #F1F3FA;">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="hr.employee">人资管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="hr.employee">个人信息子集</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>职业资格</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-diamond"></i>个人职业资格
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
                                <label class="col-md-2 control-label">用户名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="用户名" ng-model="vm.item.userName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">证书名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="证书名称" required="true" ng-model="vm.item.certificationName"ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">证书编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="证书编号" ng-model="vm.item.certificationNo" ng-disabled="!processInstance.firstTask"
                                           />
                                </div>
                                <label class="col-md-2 control-label">专业类别</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true" ng-model="vm.item.majorName"ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">注册证书号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-disabled="!processInstance.firstTask" ng-model="vm.item.registerNo" name="registerNo" required="true" maxlength="30" />
                                </div>
                                <label class="col-md-2 control-label">印章号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-disabled="!processInstance.firstTask" ng-model="vm.item.sealNo" name="sealNo" required="true"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">职业资格证书号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-disabled="!processInstance.firstTask" ng-model="vm.item.qualifyNo" name="qualifyNo" required="true" maxlength="30" />
                                </div>
                                <label class="col-md-2 control-label">签发时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker"  id="issuseDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.issuseDate" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default"ng-disabled="!processInstance.firstTask"  type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">是否在公司</label>
                                <div class="col-md-4">
                                    <select   ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否在公司'}:true"
                                            ng-model="vm.item.incompany" class="form-control"></select>
                                </div>
                                <label class="col-md-2 control-label">注册进公司时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker"  id="inDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.inDate" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default"ng-disabled="!processInstance.firstTask"  type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">有效时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker"  id="validDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.validDate" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default"ng-disabled="!processInstance.firstTask"  type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-disabled="!processInstance.firstTask" ng-model="vm.item.remark" name="remark" maxlength="100" ng-disabled="!processInstance.firstTask"/>
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

                        </div></form>
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
<jsp:include page="../common/common-actFlow.jsp"/>
<jsp:include page="../common/common-edFile.jsp"/>

