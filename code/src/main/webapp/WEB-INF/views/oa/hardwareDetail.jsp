<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a >技术质量</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a >硬件设备详情</a>
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
            <i class="icon-envelope-letter"></i> 硬件设备详情
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
                                <label class="col-md-2 control-label required">设备名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="设备名称"
                                           ng-model="vm.item.equipmentName" />
                                </div>

                                <label class="col-md-2 control-label required">设备类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="设计类型"
                                           ng-model="vm.item.equipmentType" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">品牌</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="品牌"
                                           ng-model="vm.item.bank" />
                                </div>

                                <label class="col-md-2 control-label required">型号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="型号"
                                           ng-model="vm.item.model" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">主要配置参数</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="主要配置参数"
                                           ng-model="vm.item.parameter" />
                                </div>

                                <label class="col-md-2 control-label required">数量</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="数量"
                                           ng-model="vm.item.number" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">放置地点</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="放置地点"
                                           ng-model="vm.item.address" />
                                </div>

                                <label class="col-md-2 control-label required">使用/管理人员/部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="使用/管理人员/部门"
                                           ng-model="vm.item.manager" />
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




