<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>财务管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>公司网银管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.remark"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i>公司网银管理
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);"  style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="rightData.selectOpts.indexOf('修改')>=0" style="font-size: 14px;line-height: 1.6"
               ng-click="vm.save();" >
                <i class="fa fa-save"></i> 保存 </a>

            <a href="javascript:;" class="btn btn-sm btn-default"  style="font-size: 14px;line-height: 1.6"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基本信息 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 140px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">开户名称</label>
                                <div class="col-md-10" >
                                    <input type="text" class="form-control" ng-model="vm.item.userName" name="userName"  required="true" ng-disabled="rightData.selectOpts.indexOf('修改')<0" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">银行卡号</label>
                                <div class="col-md-4" >
                                   <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.userBankNo" name="userBankNo"  required="true" ng-disabled="rightData.selectOpts.indexOf('修改')<0" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.getBankName()" ng-disabled="rightData.selectOpts.indexOf('修改')<0"><i class="fa fa-refresh"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">银行名称</label>
                                <div class="col-md-4" >
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.bankName"  required="true" ng-disabled="rightData.selectOpts.indexOf('修改')<0">
                                    <span style="font-size: 12px;color: red">请填写到具体支行</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10" >
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"  required="true" ng-disabled="rightData.selectOpts.indexOf('修改')<0" />
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

            </div>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>


