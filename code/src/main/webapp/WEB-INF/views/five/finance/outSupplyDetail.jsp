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
            <span>对外提供资料</span>
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
            <i class="icon-note"></i>对外提供资料审批
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_15_1" data-toggle="tab" aria-expanded="true">
                        基本信息 </a>
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
                     style="min-height: 140px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                         <div class="form-group">
                            <label  class="col-md-2 control-label required">资料类型</label>
                            <div class="col-md-4">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.type" name="type"  required readonly readonly />
                                    <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showTypeModel();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog" ></i></button>
                                         </span>
                                </div>
                            </div>
                            <label class="col-md-2 control-label required">申报部门</label>
                            <div class="col-md-4" >
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                    <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" readonly><i class="fa fa-cog"></i></button>
                                         </span>
                                </div>
                            </div>
                        </div>
                         <div class="form-group" ng-if="vm.item.type.indexOf('资信证明')>=0||vm.item.type.indexOf('保函')>=0||vm.item.type.indexOf('征信报表')>=0">
                                <label class="col-md-2 control-label required">是否注明银行余额</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.bankBalance" class="form-control"
                                            ng-disabled="!processInstance.firstTask" ></select>
                                </div>
                            </div>
                         <div class="form-group" ng-show="vm.item.type.indexOf('财务报表')>=0">
                             <label class="col-md-2 control-label required">财务报表-开始年份</label>
                             <div class="col-md-4">
                                 <div class="input-group date date-picker-year" id="beginYear">
                                     <input type="text" class="form-control"
                                            ng-model="vm.item.beginYear"   required="true" disabled="disabled">
                                     <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                 </div>
                             </div>
                             <label class="col-md-2 control-label required">财务报表-结束年份</label>
                             <div class="col-md-4">
                                 <div class="input-group date date-picker-year" id="endYear">
                                     <input type="text" class="form-control"
                                            ng-model="vm.item.endYear"   required="true" disabled="disabled">
                                     <span class="input-group-btn">
                                            <button class="btn default date-set" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                 </div>
                             </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.type.indexOf('财务报表')>=0">
                                <label class="col-md-2 control-label required">是否包含资产负债表</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.assetLiabilities" class="form-control"
                                            ng-disabled="!processInstance.firstTask" ></select>
                                </div>
                                <label class="col-md-2 control-label required">是否包含利润及分配表</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.profitsAllocation" class="form-control"
                                            ng-disabled="!processInstance.firstTask" ></select>
                                </div>
                            </div>
                         <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10" >
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.remark"  ng-disabled="!processInstance.firstTask">
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

<div class="modal fade" id="typeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择对外提供资料类型</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="type in vm.types">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_type"
                           ng-checked="vm.item.type.indexOf(type.name)>=0"
                           data-name="{{type.name}}" data-id="{{'file_'+id}}" />
                    <span ng-bind="type.name" class="margin-right-10" style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%5==0"/>
               </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveType();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

