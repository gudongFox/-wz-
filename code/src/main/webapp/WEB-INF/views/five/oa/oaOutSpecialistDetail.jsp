<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:43})">协会学会审批</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">外部专家申请表</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">外部专家申请表</span>
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
                                <label class="col-md-2 control-label required">申请日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="submitTime">
                                        <input type="text"  class="form-control" required="true" name="submitTime"
                                               ng-disabled="!processInstance.firstTask"
                                               ng-model="vm.item.submitTime" placeholder="申请日期">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
												</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">姓名</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.name" name="name" required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">身份证号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.identityCardNo" name="identityCardNo" required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">送审单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">地址（房间号）</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.address" name="address" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">职务</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.position" name="position" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">职称</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.rank" name="rank" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">证书编号</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.rankCode" name="rankCode" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">政治面貌</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.politicsStatus" name="politicsStatus" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label ">称号</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.label" name="label"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">毕业院校</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.graduateCollege" name="graduateCollege" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">最高学历</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.highestEducation" name="highestEducation" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">主要擅长</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.good" name="good" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">专业</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.major" name="major" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">手机</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.phone" name="phone"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label ">电话</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.telephone" name="telephone" r   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">所属专家组</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.specialistGroup" name="specialistGroup" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label ">拟担任外部专家外部机构名称</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.institutionName" name="institutionName"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">是否加盖公司章</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.companyStamp" class="form-control" required="true"
                                            ng-disabled="false"></select>
                                </div>
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.remark" name="remark"    ng-disabled="!processInstance.firstTask"/>
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
<jsp:include page="../print/print-oaOutSpecialistDetail.jsp"/>
