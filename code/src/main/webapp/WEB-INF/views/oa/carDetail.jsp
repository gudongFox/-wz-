<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">综合办公</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a >车辆信息管理</a>
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
            <i class="icon-envelope-letter"></i> 车辆详情
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
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">车牌号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="车牌号"
                                           ng-model="vm.item.carNo" />
                                </div>

                                <label class="col-md-2 control-label required">汽车类型</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="汽车类型"
                                           ng-model="vm.item.carType" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">汽车排量</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="汽车排量"
                                           ng-model="vm.item.carCc" />
                                </div>

                                <label class="col-md-2 control-label required">汽车价格</label>
                                <div class="col-md-4">
                                    <input type="number" class="form-control" placeholder="汽车价格"
                                           ng-model="vm.item.carPrice" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">汽车品牌</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="汽车品牌"
                                           ng-model="vm.item.carBrand" />
                                </div>

                                <label class="col-md-2 control-label required">汽车颜色</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="汽车颜色"
                                           ng-model="vm.item.carColor" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">购买时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="buyDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.buyDate" name="buyDate" required="true" disabled>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">汽车状态</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('车辆状态')}:true"
                                            ng-model="vm.item.carStatus" class="form-control"
                                            ></select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">所属部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="所属部门"  ng-model="vm.item.deptName" disabled="disabled"/>
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

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>




