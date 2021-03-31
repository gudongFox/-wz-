<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>经营管理</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">月度预支奖金签发单</span>
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
            <i class="icon-note"></i><span ng-bind="tableName">月度预支奖金签发单</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);"  style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.sendAble" style="font-size: 14px;line-height: 1.6"
               ng-click="vm.save();" >
                <i class="fa fa-save"></i> 保存 </a>

            <span ng-include="'/web/v1/tpl/actAction.html'" ng-controller="CommonActActionController" ng-if="processInstanceId" ></span>

            <a href="javascript:;" class="btn btn-sm btn-default"  style="font-size: 14px;line-height: 1.6"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6"><%-- ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>
                <i class="fa fa-print"></i> 打印 </a>
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
                     style="min-height: 140px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">

                        <div class="form-group">
                           <%-- <label class="col-md-2 control-label required">上报部门</label>
                            <div class="col-md-4" >
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                    <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                </div>
                            </div>--%>
                            <label class="col-md-2 control-label required"><strong>上报时间</strong></label>
                            <div class="col-md-4">
                                <div class="input-group date date-picker-year-month" id="collectMonth">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.collectMonth" required="true"
                                           readonly>
                                    <span class="input-group-btn">
                                    <button class="btn default" type="button"
                                            ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button></span>
                                </div>
                            </div>
                        </div>
                            <div class="form-group" >
                                <label class="col-md-2 control-label">申报类型</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'申报类型'}:true"
                                            ng-model="vm.item.declareType" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label " style="font-weight: bold; color: red" ng-show="processInstance.firstTask">导出已上报数据统计表</label>
                                <div class="col-md-4">
                                    <a href="javascript:;" class="  btn btn-sm default" ng-show="processInstance.firstTask"
                                       ng-click="vm.downCollect();" >
                                        <i class="fa fa-cloud-download"></i> 下载 </a>
                                </div>
                            </div>

                        <%--发文正文--%>
                        <div ng-controller="RedHeaderController as rh">
                            <div class="form-group">

                                <label class="col-md-2 control-label " style="font-weight: bold;font-size: 18px"><strong>上报内容</strong></label>
                                <div class="col-md-4">
                                    <%--原内容 <a href="#" ng-click="rh.readDocOnly(redheadFile.id,redheadFile.fileName)" ng-bind="redheadFile.fileName"></a>--%>&nbsp;&nbsp;
                                    <a href="#"   ng-click="rh.readDocOnly(redheadFile.id,redheadFile.fileName)" ng-bind="redheadFile.fileName"></a>&nbsp;&nbsp;

                                        <span id="btnUpload0" class="btn btn-sm default fileinput-button" ng-show="processInstance.firstTask">
                                    <i class="fa fa-cloud-upload" ></i><span>上传</span>
                                    <input id="redHead" type="file" name="singleUpload" ></span>

                                    <span class='btn btn-sm default fileinput-button' ng-click=" rh.remove(vm.item.businessKey,redheadFile.id)" ng-if="redheadFile!=undefined&&processInstance.firstTask"><i class='fa fa-trash'></i><span>删除</span></span>
                                </div>
                            </div>
                        </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10" >
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.remark"   ng-disabled="!processInstance.firstTask">
                                </div>
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

<jsp:include page="../print/print.jsp"/>