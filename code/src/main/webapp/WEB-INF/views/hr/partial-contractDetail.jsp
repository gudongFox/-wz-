<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="page-bar" id="pageBar">
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
            <span>劳动合同</span>
        </li>
    </ul>
    <div class="page-toolbar">

    </div>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-diamond"></i>个人劳动合同信息
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
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form" >
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">用户姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="用户姓名" ng-model="vm.contract.userName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">合同性质</label>
                                <div class="col-md-4">
                                    <select disabled="disabled" ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'合同性质'}:true"
                                            ng-model="vm.contract.contractType" class="form-control"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">用工形式</label>
                                <div class="col-md-4">
                                    <select disabled="disabled"  ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'用工形式'}:true"
                                            ng-model="vm.contract.workType" class="form-control"></select>
                                </div>
                                <label class="col-md-2 control-label">合同区域</label>
                                <div class="col-md-4">
                                    <input disabled="disabled" type="text" class="form-control" placeholder="合同区域" ng-model="vm.contract.contractLocation"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">社保缴纳地</label>
                                <div class="col-md-4">
                                    <input disabled="disabled" type="text" class="form-control" placeholder="社保缴纳地" ng-model="vm.contract.insureLocation"/>
                                </div>
                                <label class="col-md-2 control-label">合同年限</label>
                                <div class="col-md-4">
                                    <input disabled="disabled" type="text" class="form-control" placeholder="合同年限" ng-model="vm.contract.contractYear"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">开始时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="beginTime" >
                                        <input disabled="disabled" type="text" class="form-control"
                                               ng-model="vm.contract.beginTime" >
                                        <span  class="input-group-btn">
												<button disabled="disabled" class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">结束时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="endTime" >
                                        <input disabled="disabled" type="text" class="form-control"
                                               ng-model="vm.contract.endTime" >
                                        <span  class="input-group-btn">
												<button  disabled="disabled" class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">通知领取时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="noticeTime">
                                        <input disabled="disabled" type="text" class="form-control"
                                               ng-model="vm.contract.noticeTime" >
                                        <span  class="input-group-btn">
												<button disabled="disabled" class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">用户领取时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="receiveTime">
                                        <input disabled="disabled" type="text" class="form-control"
                                               ng-model="vm.contract.receiveTime" >
                                        <span  class="input-group-btn">
												<button class="btn default" type="button" disabled="disabled"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>


                        </div></form>

                </div>

            </div>
        </div>
    </div>
</div>



