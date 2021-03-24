<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>技术质量</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a>软件系统管理台账</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.carNo"></span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i> 软件系统管理台账
            <small style="font-size: 50%;" ng-bind="processInstance.creatorName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="vm.item.creator==user.userLogin"
               ng-click="vm.save();">
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>

            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 300px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">软件/系统名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="软件/系统名称"
                                           ng-model="vm.item.softwareName" />
                                </div>

                                <label class="col-md-2 control-label required">软件系统类别</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="软件系统类别"
                                           ng-model="vm.item.softwareType" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">生产/开发单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="生产/开发单位"
                                           ng-model="vm.item.developDept" />
                                </div>

                                <label class="col-md-2 control-label required">主要功能说明</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="主要功能说明"
                                           ng-model="vm.item.funcationContent" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">使用范围（人员/部门）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="使用范围（人员/部门）"
                                           ng-model="vm.item.manager" />
                                </div>

                                <label class="col-md-2 control-label required">维护管理部门（人员）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="维护管理部门（人员）"
                                           ng-model="vm.item.maintainDept" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">数量（套）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="数量（套）"
                                           ng-model="vm.item.number" />
                                </div>

                                <label class="col-md-2 control-label required">价格（元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="价格（元）"
                                           ng-model="vm.item.price" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">运行启动日期</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="运行启动日期"
                                           ng-model="vm.item.startTime" />
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">创建人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.creatorName" disabled="disabled"/>
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

            </div>

        </div>
    </div>
</div>

<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="../common/common-actFlow.jsp"/>




