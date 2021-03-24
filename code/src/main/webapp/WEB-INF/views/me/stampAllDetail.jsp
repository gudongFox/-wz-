<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <div class="page-bar" id="pageBar">
        <ul class="page-breadcrumb">
            <li>
                <i class="fa fa-home"></i>
                <a ui-sref="five.home">首页</a>
                <i class="fa fa-angle-right"></i>
            </li>
            <li>
                <a ui-sref="me.edProject">项目管理</a>
                <i class="fa fa-angle-right"></i>
            </li>
            <li>
                <span ng-bind="vm.item.stampType"></span>
            </li>
        </ul>
    </div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i> <span ng-show="vm.item.stampType==''">成果用印审批表</span><span ng-bind="vm.item.stampType"></span>
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="vm.item.businessKey==''"
               ng-click="vm.save();" >
                <i class="fa fa-save"></i> 保存 </a>
            <jsp:include page="../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/ed/stamp/exportData.json?id='+vm.item.id}}" target="_blank">
                <i class="fa fa-file-word-o"></i> 导出</a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_detail_1" data-toggle="tab" aria-expanded="true">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_detail_2" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_detail_3" data-toggle="tab" aria-expanded="false">
                        流程图 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_detail_1"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required"> <strong>用印所室、部门</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName"
                                               name="deptName" maxlength="100"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.showDeptModal(vm.item.deptId);"
                                                    ng-disabled="vm.item.businessKey!=''" > <i
                                                    class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="vm.item.businessKey==''" >请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required"><strong>项目名称</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName"
                                               name="projectName" maxlength="100" ng-disabled="vm.item.businessKey!=''"
                                        />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"
                                                    ng-click="vm.listDeptSteps();" ng-disabled="vm.item.businessKey!=''">
                                                   <i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="vm.item.businessKey==''">请点击后方按钮选择</span>
                                </div>

                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required"><strong>用印成果类别</strong></label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('用印文件类型')}:true"
                                            ng-model="vm.item.docType" class="form-control" ng-disabled="vm.item.businessKey!=''"
                                    ></select>
                                </div>
                                <label class="col-md-2 control-label " ><strong>用印专业</strong></label>
                                <div class="col-md-4" >
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.majors" required="true" name="majors"
                                               disabled="disabled" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.showMajorModal();"
                                                    >
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">合同金额</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractMoney" name="contractMoney " ng-disabled="vm.item.businessKey!=''" />
                                </div>
                                <label class="col-md-2 control-label required">执行阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stageName" ng-disabled="vm.item.businessKey!=''" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">分期</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stepName" name="stepName" required="true" ng-disabled="vm.item.businessKey!=''"  />
                                </div>
                                <label class="col-md-2 control-label required">用印时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="useTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.useTime" name="useTime" required="true" ng-disabled="!processInstance.firstTask" >
                                        <span class="input-group-btn">
												<button class="btn default" type="button"  ><i class="fa fa-calendar" ng-disabled="!processInstance.firstTask" ></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required"><strong>用印明细</strong></label>
                                <div class="col-md-10">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.useStamp"
                                               disabled/>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.showStampModel();"
                                                    ng-disabled="!processInstance.firstTask" >
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block" ng-show="processInstance.firstTask" >请点击后方按钮选择</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">办公室经办人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.officeUserName" name="officeUserName"  ng-disabled="!processInstance.firstTask"required="true"  />
                                </div>
                                <label class="col-md-2 control-label">用印单位经办人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptUserName" name="deptUserName"  ng-disabled="!processInstance.firstTask" required="true"  />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">用印事由</label>
                                <div class="col-md-10">
                                    <textarea class="form-control" ng-model="vm.item.useDescription" rows="3" required="true" name="useDescription" ng-disabled="!processInstance.firstTask"   placeholder="请详细说明,用印事由!" ></textarea>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" ng-disabled="!processInstance.firstTask" name="remark"  />
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
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 设计与开发审定记录 文件
        </div>
        <div class="actions">
            <a href="javascript:;" style="float:right;margin-top: 5px;" class="btn btn-sm btn-default"
               ng-click="vm.newMark();"
               ng-show="processInstance.myTaskName=='审定人'"><i
                    class="fa fa-plus"></i> 增加 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: 200px;overflow-y: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="width: 280px;">文件名称</th>
                    <th>校核意见</th>
                    <th style="width: 200px;">设计回复</th>
                    <th style="width: 130px;">创建时间</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="mark in marks ">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="mark.fileName" ng-click="vm.showMark(mark);" title="详情"
                        style="color: blue;"></td>
                    <td title="{{mark.markContent}}">
                        <i class="fa fa-paw" ng-show="mark.cloudLocated"></i>
                        <span ng-bind="mark.shortMarkContent"></span>
                    </td>
                    <td ng-bind="mark.shortMarkAnswer" title="{{mark.markAnswer}}"></td>
                    <td ng-bind="mark.gmtCreate|date:'yyyy-MM-dd HH:mm'" title="{{mark.creatorName}}"></td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.showMark(mark);" title="详情"></i>
                        <i class="fa fa-trash" ng-click="vm.removeMark(mark.id);"
                           ng-show="(processInstance.myTaskName=='审定人')&&vm.mark.creator==user.userLogin"
                           title="删除"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>


<div class="modal fade draggable-modal" id="deptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择执行所室</h4>
            </div>
            <div class="modal-body">
                <div id="dept_select_tree" style="max-height:500px;overflow-y: auto;overflow-x: hidden;"></div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDept();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="stepModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="vm.item.deptName+'-项目详情'"></h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="row">
                        <div class="col-md-2">
                            <input type="text" class="form-control input-sm" placeholder="关键字"
                                   ng-model="vm.qName"/>
                        </div>
                    </div>
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>项目名称</th>
                                <th>项目阶段</th>
                                <th>分期名称</th>
                                <th>合同金额</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="step in vm.stepList|filter:{projectName:vm.qName}">
                                <td>
                                    <input type="checkbox" class="cb_step"
                                           ng-checked="vm.item.stepId==step.id"
                                           data-name="{{step}}" style="width: 15px;height: 15px;"/>
                                </td>
                                <td ng-bind="step.projectName"></td>
                                <td ng-bind="step.stageName"></td>
                                <td ng-bind="step.stepName"></td>
                                <td ng-bind="step.contractMoney"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveStep();">保存</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="selectStampModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">用印明细</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>用印章</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="useStamp in vm.useStamps">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.useStamp.indexOf(useStamp.code)>=0" class="cb_stamp" data-name="{{useStamp.code}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="useStamp.code"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSelectStamp();">确认</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="listMajorsModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">用印专业</h4>
            </div>
            <div class="modal-body">
                <div class="table-scrollable" style="max-height: 400px">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 50px;">#</th>
                            <th>用印专业</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="major in vm.majors">
                            <td>
                                <input type="checkbox" ng-checked="vm.item.majors.indexOf(major.majorName)>-1" class="cb_major" data-name="{{major.majorName}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td ng-bind="major.majorName"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveMajor();">确认</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="../common/common-actFlow.jsp"/>
<jsp:include page="print/print-stampDetail.jsp"/>




