<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:40})">公文管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">协同文件</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">协同文件</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>
        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>>
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
                     style="min-height: 240px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal form" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                    <label class="col-md-2 control-label required">事项</label>
                                    <div class="col-md-10">
                                        <textarea rows="3" class="form-control" ng-model="vm.item.content" required="true" name="reportContent" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                                    </div>
                                </div>
                            <%--发文正文--%>
                            <div ng-controller="RedHeaderController as rh">
                                <div class="form-group">
                                    <label class="col-md-2 control-label " style="font-weight: bold;font-size: 18px"><strong>正文</strong></label>
                                    <div class="col-md-10">
                                        <a href="#" ng-click="rh.readDocOnly(redheadFile.id,redheadFile.fileName)" ng-bind="redheadFile.fileName"></a>&nbsp;&nbsp;
                                        <span id="btnUpload0" class="btn btn-sm default fileinput-button" ng-show="processInstance.firstTask">
                                        <i class="fa fa-cloud-upload" ></i><span>上传</span>
                                        <input id="redHead" type="file" name="singleUpload" accept=".doc,.docx,.pdf"></span>
                                        <span class='btn btn-sm default fileinput-button' ng-click=" rh.remove(vm.item.businessKey,redheadFile.id)" ng-if="redheadFile!=undefined&&processInstance.firstTask"><i class='fa fa-trash'></i><span>删除</span></span>
                                    </div>
                                </div>
                            </div>
                            <div  class="form-group">
                                     <label class="col-md-2 control-label ">会签人</label>
                                     <div class="col-md-4">
                                         <div class="input-group">
                                             <input type="text" class="form-control" ng-model="vm.item.signerName" name="signerName"    ng-disabled="processInstance.myRunningTaskName.indexOf('机要秘书-核收')==-1"/>
                                             <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('signer');"  ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                         </div>
                                     </div>

                                     <label class="col-md-2 control-label ">呈阅领导</label>
                                     <div class="col-md-4">
                                         <div class="input-group">
                                             <input type="text" class="form-control" ng-model="vm.item.chargeMenName" name="chargeMenName"  disabled/>
                                             <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('chargeMen');"  ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                         </div>
                                     </div>
                                 </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">所属部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName"
                                           disabled="disabled"/>
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
<jsp:include page="../print/print.jsp"/>

