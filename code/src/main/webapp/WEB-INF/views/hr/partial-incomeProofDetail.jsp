<%@ page contentType="text/html;charset=UTF-8" language="java" %>



<div class="page-bar" style="margin: -10px 0px 5px 0px;padding:0px;background-color: #F1F3FA;">
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
            <a>收入证明</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ng-bind="vm.item.userName"></a>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-diamond"></i>员工收入证明
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();"
               ng-show="processInstance.passAble">
                <i class="fa fa-save"></i> 保存 </a>

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
                                    <input type="text" class="form-control" placeholder="" ng-model="vm.item.userName" disabled
                                           />
                                </div>
                                <label class="col-md-2 control-label">自助账号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.userLogin" disabled
                                           />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">证件类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.idCardType" disabled />
                                </div>
                                <label class="col-md-2 control-label">证件号码</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.idCardNo" disabled />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">工作年限</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.workYearNumber" disabled />
                                </div>
                                <label class="col-md-2 control-label">工作职位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.workPosition" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">月收入</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.monthIncome" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label">年收入</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.yearIncome" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">公司名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.companyName" disabled />
                                </div>
                                <label class="col-md-2 control-label">公司地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.companyAddress" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">联系人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.companyLinkMan" disabled />
                                </div>
                                <label class="col-md-2 control-label">公司电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.companyTel" disabled />
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

<jsp:include page="../ed/project/part-actFlow.jsp"/>
