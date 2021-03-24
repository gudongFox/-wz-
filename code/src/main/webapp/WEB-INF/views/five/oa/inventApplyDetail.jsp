<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:50})">科研审批流程</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">专利申请</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">专利申请</span>
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
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">公司内申请单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName"    ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-2 control-label required">公司外合作单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.cooperator" name="cooperator"    ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">申请人顺序</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.applyNameSort" name="applyNameSort"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">发明名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.inventName" name="inventName"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">所属专业</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.majorName" name="majorName"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showMajorModel();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required" style="font-weight: bold;">第一发明人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.firstInventManName" name="firstInventManName"  required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">其他发明人顺序</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.otherInventMen" name="otherInventMen" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">身份证号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.idNumber" name="idNumber" required="true"    ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-2 control-label required">日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="applyTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.applyTime" name="applyTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                           </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">与关键技术或创新点有关保密情</label>
                            <div class="col-md-10">
                                <textarea rows="6" class="form-control" ng-model="vm.item.securityDec" required="true" name="securityDec" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label required">关键技术或创新点</label>
                            <div class="col-md-10">
                                <textarea rows="6" class="form-control" ng-model="vm.item.technologyAndInnovate" required="true" name="technologyAndInnovate" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">检索查新情况</label>
                            <div class="col-md-10">
                                <textarea rows="6" class="form-control" ng-model="vm.item.retruevalDec" required="true" name="retruevalDec" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">技术与市场预测</label>
                            <div class="col-md-10">
                                <textarea rows="6" class="form-control" ng-model="vm.item.technologyMarket" required="true" name="retruevalDec" placeholder="" ng-disabled="!processInstance.firstTask"></textarea>
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

<div class="modal fade" id="selectMajorModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">从事专业</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height:350px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>专业名称</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="major in vm.majors">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.majorName.indexOf(major.code)>=0" class="cb_major" data-name="{{major.code}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="major.code"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectMajor();">确认</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../print/print-oaInventApplyDetail.jsp"/>