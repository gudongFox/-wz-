<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="page-bar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a>人资管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>便捷服务管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>员工在职证明</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a></a>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-diamond"></i>员工在职证明
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();"
               ng-show="processInstance.passAble">
                <i class="fa fa-save"></i> 保存 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();">
                <i class="fa fa-print"></i> 打印 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.passAble"
               ng-click="vm.showSendFlow();">
                <i class="fa fa-send"></i> 发送 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.backAble"
               ng-click="showBackFlow();">
                <i class="fa fa-reply"></i> 打回 </a>

            <a href="javascript:;" class="btn btn-sm btn-default"
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
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">员工姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="" ng-model="vm.item.userName" disabled />
                                </div>
                                <label class="col-md-2 control-label">入职时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.joinCompanyTime" disabled />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">证件类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'证件类型'}:true"
                                            ng-model="vm.item.idCardType" disabled class="form-control"></select>
                                </div>
                                <label class="col-md-2 control-label">证件编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.idCardNo" disabled />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">公司名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.companyName"  name="companyName" disabled/>
                                </div>
                                <label class="col-md-2 control-label">申请时间</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" value="{{vm.item.gmtCreate|date:'yyyy-MM-dd'}}" disabled />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">使用说明</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.remark" required="true" name="remark"
                                              ng-disabled="!processInstance.firstTask" placeholder=""></textarea>
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

<jsp:include page="../ed/project/part-actFlow.jsp"/>

<jsp:include page="print/print-employeeProof.jsp"/>

