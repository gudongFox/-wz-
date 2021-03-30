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
            <span ng-bind="tableName">工程承包项目招标文件评审</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.contractName"></span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">工程承包项目招标文件评审</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" >
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
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName" name="projectName" required="true"  readonly />
                                    <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showRecordModal();"
                                                    ng-disabled="!processInstance.firstTask"> <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">评审级别</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'评审级别'}:true"
                                            ng-model="vm.item.projectLevel" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required" ng-if="vm.item.projectLevel=='公司级'">评审人员</label>
                                <div class="col-md-4" ng-if="vm.item.projectLevel=='公司级'">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.reviewUsername" name="reviewUsername"  required="true" ng-disabled="true"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('reviewUserName');" ><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.projectLevel=='公司级'">主管部门</label>
                                <div class="col-md-4" ng-if="vm.item.projectLevel=='公司级'">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptChargeName" name="deptChargeName"  required="true" ng-disabled="true"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal(1);" ><i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.projectLevel=='院级'">评审人员</label>
                                <div class="col-md-4" ng-if="vm.item.projectLevel=='院级'">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptReviewUsername" name="deptReviewUsername"  required="true" readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('deptReviewUsername');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">部门负责人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptChargeManName" name="deptChargeManName"  required="true"  readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('deptChargeManName');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目经理</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectManagerName" name="projectManagerName"  required="true"  readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('projectManager');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">发包单位</label>
                                <div class="col-md-4" >
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal(0);" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">项目地点</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectLocation" name="projectLocation"  placeholder=""ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">标书来源</label>
                                <div class="col-md-4">
                                    <input rows="1" type="text" class="form-control" ng-model="vm.item.tenderSource" name="tenderSource"  placeholder=""ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">计划发包时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="startTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.beginTime" name="beginTime" required="true"  readonly ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>  <label class="col-md-2 control-label required">计划开始时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="endTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.planBeginTime" name="planBeginTime" required="true"  readonly ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group" >
                                <label class="col-md-2 control-label required">总投资（万元）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.totalPrice" required="true" ng-change="vm.countTotalPrice();" name="totalPrice"  placeholder=""ng-disabled="!processInstance.firstTask" />
                                </div>
                                <label class="col-md-2 control-label required">是否为联合体</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.combo" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                    <span style="color: red" ng-if="vm.item.combo=='是'">请上传附件</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目概况</label>
                                <div class="col-md-10">
                                    <textarea rows="3" type="text" class="form-control" ng-model="vm.item.projectSituation" name="projectSituation"  placeholder=""ng-disabled="!processInstance.firstTask" ></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <textarea rows="1" type="text" class="form-control" ng-model="vm.item.remark" name="remark"  placeholder=""ng-disabled="!processInstance.firstTask" ></textarea>
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

<div class="modal fade draggable-modal" id="selectRecordModal" tabindex="-1" role="basic" aria-hidden="true" >
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4>选择项目信息备案</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-3">
                        <input type="text" class="form-control input-sm" placeholder="项目名称"
                               ng-model="vm.qProject"/>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: {{contentHeight-400}}px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th>项目名称</th>
                            <th style="width: 80px;">项目类型</th>
                            <th>客户名称</th>
                            <th>所属部门</th>
                            <th style="width: 110px;">投资额(万元)</th>
                            <th>参与人</th>
                            <th style="width: 40px;">是否已录入合同</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="record in vm.listRecord|filter:{projectName:vm.qProject}" >
                            <td>
                                <input type="checkbox" ng-checked="vm.item.recordId==record.id" class="cb_record"
                                       data-name="{{record}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="record.projectName"  class="data_title"  ng-click="vm.show(item.id);"></td>
                            <td ng-bind="record.projectType"></td>
                            <td ng-bind="record.customerName"></td>
                            <td ng-bind="record.deptName"></td>
                            <td ng-bind="record.projectInvest|currency : '￥'"></td>
                            <td ng-bind="record.businessUserName"></td>
                            <td class="text-center">
                                <span class="label label-sm label-success" style="cursor: pointer;" ng-if="record.contractReviewId!=0">是</span>
                                <span class="label label-sm label-default" ng-if="record.contractReviewId==0">否</span>
                            </td>
                        </tr>

                        </tbody>
                    </table>
                </div>


            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectRecordModel()">确认</button>
            </div>
        </div>
    </div>

</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="../print/print.jsp"/>

