<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:57})">会议管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">决策会议管理</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">决策会议管理</span>
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" ng-if="processInstance.myRunningTaskName.indexOf('发起')==-1&&processInstance.myRunningTaskName.indexOf('会议通知')==-1" style="font-size: 14px;line-height: 1.6" >
            <i class="fa fa-print" ></i> 会议通知 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" ng-if="processInstance.myRunningTaskName.indexOf('发起')>-1||processInstance.myRunningTaskName.indexOf('会议通知')>-1"style="font-size: 14px;line-height: 1.6;color: #fecd1e;border: 1px solid #ffd01e" >
                <i class="fa fa-print" style="color: #fecd1e"></i> 会议通知 </a>
            <a href="javascript:;"  class="btn btn-sm btn-default" ng-click="vm.printItem();" <%--ng-if="processInstance.myRunningTaskName.indexOf('确定')==-1&&processInstance.myRunningTaskName.indexOf('会议纪要')==-1"--%>style="font-size: 14px;line-height: 1.6" >
                <i class="fa fa-print"></i> 会议纪要 </a>
            <a href="javascript:;"  class="btn btn-sm btn-default" ng-click="vm.printItem();" ng-if="processInstance.myRunningTaskName.indexOf('确定')>-1||processInstance.myRunningTaskName.indexOf('会议纪要')>-1" style="font-size: 14px;line-height: 1.6;color: #fecd1e;border: 1px solid #ffd01e" >
                <i class="fa fa-print" style="color: #fecd1e"></i> 会议纪要 </a>
            <a class="btn btn-sm btn-default"  style="font-size: 14px;line-height: 1.6" ng-href="{{'/five/oa/decisionMaking/getXWPFTemplateNotice.json?id='+vm.item.id}}" ng-if="processInstance.myRunningTaskName.indexOf('发起')==-1&&processInstance.myRunningTaskName.indexOf('会议通知')==-1" target="_blank">
                <i class="fa fa-file-word-o" ></i> 导出通知</a>
            <a class="btn btn-sm btn-default"  style="font-size: 14px;line-height: 1.6;color: #fecd1e;border: 1px solid #ffd01e " ng-href="{{'/five/oa/decisionMaking/getXWPFTemplateNotice.json?id='+vm.item.id}}" ng-if="processInstance.myRunningTaskName.indexOf('发起')>-1||processInstance.myRunningTaskName.indexOf('会议通知')>-1" target="_blank">
                <i class="fa fa-file-word-o" style="color: #fecd1e"></i> 导出通知</a>
            <a class="btn btn-sm btn-default"  style="font-size: 14px;line-height: 1.6" ng-href="{{'/five/oa/decisionMaking/getXWPFTemplateSummary.json?id='+vm.item.id}}" ng-if="processInstance.myRunningTaskName.indexOf('确定')==-1&&processInstance.myRunningTaskName.indexOf('会议纪要')==-1" target="_blank">
                <i class="fa fa-file-word-o" ></i> 导出纪要</a>
            <a class="btn btn-sm btn-default"  style="font-size: 14px;line-height: 1.6;color: #fecd1e;border: 1px solid #ffd01e " ng-href="{{'/five/oa/decisionMaking/getXWPFTemplateSummary.json?id='+vm.item.id}}" ng-if="processInstance.myRunningTaskName.indexOf('确定')>-1||processInstance.myRunningTaskName.indexOf('会议纪要')>-1" target="_blank">
                <i class="fa fa-file-word-o" style="color: #fecd1e"></i> 导出纪要</a>
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
                                <label class="col-md-2 control-label required">会议名称</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.item" name="item" required="true" ng-disabled="!processInstance.firstTask" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">次数</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <span class="input-group-addon">第</span>
                                        <input type="text" class="form-control" ng-model="vm.item.stages"  ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">次</span>
                                    </div>

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">出席领导</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.companyLeaderName" name="companyLeaderName" readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('companyLeader');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.firstTask">请点击左侧按钮选择出席领导</span>
                                </div>
                                <label class="col-md-2 control-label ">列席领导</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.attenderName" name="attenderName"    readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('attender');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.firstTask">请点击左侧按钮选择列席领导</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">主持人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.compereName" name="compereName"    readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('compere');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.firstTask">请点击左侧按钮选择主持人</span>
                                </div>
                                <label class="col-md-2 control-label ">记录</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.recordManName" name="recordManName"    readonly/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('recordMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.firstTask">请点击左侧按钮选择会议记录人</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">地点</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.meetingTime" name="meetingTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                </div>

                                <label class="col-md-2 control-label required">会议时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date form_datetime" id="beginTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.meetingTimePlan" value="{{vm.item.meetingTimePlan|date:'yyyy-MM-dd HH:mm'}}"  required="true" disabled="disabled">
                                        <span class="input-group-btn">
                                            <button class="btn default date-set " type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">通知</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label" ng-if="vm.item.meetingType.indexOf('董事')==-1">会议类型</label>
                                <div class="col-md-4" ng-if="vm.item.meetingType.indexOf('董事')==-1">
                                    <input type="text" class="form-control" ng-model="vm.item.meetingType" name="meetingType" disabled  >
                                </div>

                                <label class="col-md-2 control-label" ng-if="vm.item.meetingType.indexOf('董事')>=0">会议类型</label>
                                <div class="col-md-4" ng-if="vm.item.meetingType.indexOf('董事')>=0">
                                    <select ng-model="vm.item.meetingType" class="form-control"
                                            ng-disabled="!processInstance.firstTask">
                                        <option value="董事会">董事会</option>
                                        <option value="董事长工作协调会">董事长工作协调会</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.meetingType=='党委会'">
                                <label class="col-md-2 control-label ">缺席领导</label>
                                <div class="col-md-10">
                                    <textarea type="text"  rows="3" class="form-control" ng-model="vm.item.absentLeader"  name="absentLeader" placeholder="张三(缺席原因);李四(缺席原因)...请使用英文分号间隔" ng-disabled="!processInstance.firstTask" ></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">会议结论</label>
                                <div class="col-md-10">
                                    <textarea type="text" rows="5" class="form-control" ng-model="vm.item.meetingResult" name="meetingResult"  ng-disabled="processInstance.myRunningTaskName.indexOf('决策会议管理员')==-1" ></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"  ng-disabled="!processInstance.firstTask" />
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
                            <div class="portlet light" >
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="fa fa-file"></i> 会议议题
                                    </div>
                                    <div class="actions" ng-show="processInstance.firstTask">
                                        <a href="javascript:;" class="btn btn-sm default" ng-show="browserVersion!='ie9'"
                                           ng-click="vm.addDecisionDetailModal()">
                                            <i class="fa fa-plus" title="新增"></i> 新增 </a>
                                    </div>
                                </div>
                                <div class="portlet-body">
                                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                            <thead>
                                            <tr>
                                                <th style="width: 35px">#</th>
                                                <th style="min-width: 180px;">议题名称</th>
                                                <th style="width: 120px;">议题类型</th>
                                                <th style="min-width: 120px;">列席人</th>
                                                <th >议题结论</th>
                                                <th ng-if="vm.canEdit" style="width: 60px">操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr ng-repeat="detail in vm.detailList">
                                                <td ng-bind="$index+1"></td>
                                                <td ng-if="!detail.isFlowLink" ng-bind="detail.title" ng-click="vm.showTextDetail(detail);"></td>
                                                <td ng-if="detail.isFlowLink" ng-bind="detail.title" style="color: blue" ng-click="vm.showFlowDetail(detail.linkedBusiness);">
                                                </td>
                                                <td ng-bind="detail.detail"></td>
                                                <td ng-bind="detail.attendanceName"></td>
                                                <td ng-bind="detail.conclusion"></td>
                                                <td ng-if="vm.canEdit">
                                                    <i class="fa  fa-pencil margin-right-5" ng-click="vm.editDecisionDetailModal(detail);"
                                                       title="编辑"></i>
                                                    <i class="fa  fa-trash-o margin-right-5" ng-click="vm.removeDetail(detail.id);" ng-show="processInstance.firstTask"
                                                       title="删除"></i>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;font-size: 14px">
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


<div class="modal fade" id="decisionDetailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog" style="min-width: 650px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">设置议题</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <label class="col-md-2 control-label required"  style="text-align: right">议题名称</label>
                    <div class="col-md-10">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.currenDetail.title" ng-disabled="processInstance.myRunningTaskName.indexOf('决策会议管理员')==-1" />
                            <span class="input-group-btn" >
                                <button class="btn default" type="button" ng-click="vm.showSignQuteModal();" ng-disabled="processInstance.myRunningTaskName.indexOf('决策会议管理员')==-1"><i class="fa fa-cog"></i></button>
                            </span>
                        </div>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">议题类型</label>
                    <div class="col-md-10">
                        <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'议题类型'}:true"
                                ng-model="vm.currenDetail.detail" class="form-control"
                                ng-disabled="!processInstance.firstTask"></select>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label required"  style="text-align: right">议题结论</label>
                    <div class="col-md-10">
                        <textarea type="text" rows="3" class="form-control" ng-model="vm.currenDetail.conclusion" name="meetingResult" ng-disabled="processInstance.myRunningTaskName.indexOf('决策会议管理员')==-1" ></textarea>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">列席名单</label>
                    <div class="col-md-10">
                        <div class="input-group">
                            <input type="text" class="form-control" ng-model="vm.currenDetail.attendanceName"    readonly/>
                            <span class="input-group-btn" >
                                <button class="btn default" type="button" ng-click="vm.showUserModel('detailAttendance');"><i class="fa fa-user"></i></button>
                            </span>
                        </div>
                        <span style="color: red" ng-show="processInstance.firstTask">请点击右侧侧按钮选择列席人员</span>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">附件</label>
                    <div class="col-md-10">


                        <span  id="btnUpload" class="btn btn-sm blue fileinput-button"  ng-disabled="processInstance.myRunningTaskName.indexOf('决策会议管理员')==-1">
                        <i class="fa fa-cloud-upload" style="height:15px " ></i>
                        <span>上传</span>
                        <input type="file" name="file" id="uploadFile"
                               accept="*"/>
                        </span>

                        <div style="margin-top: 4px"  ng-repeat="file in vm.currenDetailFileList">
                            <span style="color: blue;font-size: 16px;" ng-bind="file.fileName" ng-click="downloadFile(file,true);"></span>
                            <i class="fa  fa-trash-o" ng-click="vm.removeDetailFile(file.id);"></i><br>
                        </div>

                    </div>
                    <span>&nbsp</span>
                </div>

                <div class="row">
                    <label class="col-md-2 control-label required"  style="text-align: right">顺序</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.seq" ng-disabled="processInstance.myRunningTaskName.indexOf('决策会议管理员')==-1" />
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">入库时间</label>
                    <div class="col-md-4">
                        <input  value="{{vm.currenDetail.gmtCreate|date:'yyyy-MM-dd HH:mm'}}"  class="form-control" readonly/>
                    </div>
                    <label class="col-md-2 control-label " style="text-align: right">议题状态</label>
                    <div class="col-md-4">
                        <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'议题状态'}:true"
                                ng-model="vm.currenDetail.issueStatus" class="form-control"
                                ng-disabled="processInstance.myRunningTaskName.indexOf('决策会议管理员')==-1"></select>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">签批领导</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.leader"    readonly/>
                    </div>
                    <label class="col-md-2 control-label " style="text-align: right">议题管理</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.creatorName"    readonly/>
                    </div>
                    <span>&nbsp</span>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDecisionDetailModal();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade" id="decisionDetailReadModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog" style="min-width: 650px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">设置议题</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <label class="col-md-2 control-label required"  style="text-align: right">议题名称</label>
                    <div class="col-md-10">
                        <a ng-click="vm.showFlowDetail(vm.currenDetail.linkedBusiness)" ng-if="vm.currenDetail.linkedBusiness.indexOf('_')>0">{{vm.currenDetail.title}}</a>

                        <strong  ng-if="!vm.currenDetail.linkedBusiness" >{{vm.currenDetail.title}}</strong>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">议题类型</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.detail"  readonly />
                    </div>
                    <span>&nbsp</span>
                </div>

                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">列席名单</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.attendanceName"    readonly/>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">附件</label>
                    <div class="col-md-10" >
                        <a ng-href="/common/attach/download/{{vm.currenDetail.attachId}}">{{vm.currenDetail.attachName}}</a>

                        <div style="margin-top: 4px"  ng-repeat="file in vm.currenDetailFileList">
                            <span style="color: blue;font-size: 16px;" ng-bind="file.fileName" ng-click="downloadFile(file,true);"></span>
                            <i class="fa  fa-download" ><a ng-href="/common/attach/download/{{file.attachId}}"></a></i><br>
                        </div>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">入库时间</label>
                    <div class="col-md-4">
                        <input value="{{vm.currenDetail.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" class="form-control" readonly/>
                    </div>
                    <label class="col-md-2 control-label " style="text-align: right">议题状态</label>
                    <div class="col-md-4">
                        <input ng-model="vm.currenDetail.issueStatus" class="form-control" readonly/>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">签批领导</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.leader"    readonly/>
                    </div>
                    <label class="col-md-2 control-label " style="text-align: right">议题管理</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.creatorName"    readonly/>
                    </div>
                    <span>&nbsp</span>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDecisionDetailModal();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="signQuteModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">议题名称</h4>
            </div>
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-inline pull-right">
                            <input type="search" class="form-control input-inline" placeholder="请输入关键字" ng-model="vm.signQuteParams.keyWord" >
                            <a class="btn green" ng-click="vm.researchSignQute();">
                                <i class="fa fa-search"></i> 查询 </a>
                        </div>
                    </div>
                </div>
                <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;overflow-x: hidden;">
                    <table class="table table-striped table-hover table-bordered table-advance no-footer">
                        <thead>
                        <tr>
                            <th style="width: 35px;">#</th>
                            <th><i class="fa fa-tasks"></i>议题名称</th>
                            <th><i class="fa fa-tasks"></i>会议类型</th>
                            <th style="width: 60px;"><i class="fa fa-user"></i> 签批领导</th>
                            <th style="width: 140px;"><i class="fa fa-clock-o"></i> 完成时间</th>
                            <th style="width: 60px;"><i class="fa fa-user"></i> 议题状态</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr ng-repeat="task in vm.signQuteList">
                            <td>
                                <input type="checkbox" ng-checked="task.cheacked"  class="cb_relevance"
                                       data-name="{{task}}" style="width: 15px;height: 15px;"/>
                            </td>
                            <td class="highlight" ng-bind="task.title" style="font-weight: bold;cursor: pointer;"></td>
                            <td ng-bind="task.detailType"></td>
                            <td ng-bind="task.leader"></td>
                            <td ng-bind="task.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>
                            <td ng-bind="task.issueStatus"></td>
                        </tr>
                        </tbody>
                    </table>

                </div>
                <my-pager data-page-info="vm.signQutePageInfo"
                          on-load="vm.showSignQuteModal()"></my-pager>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSignQute();">确认</button>
            </div>
        </div>
    </div>
</div>
<%--会议通知--%>
<jsp:include page="../print/print-decisionMakingDetail.jsp"/>
<%--会议纪要 办公会--%>
<jsp:include page="../print/print-decisionMaking.jsp"/>
<%--会议纪要 董事会--%>
<jsp:include page="../print/print-decisionMakingDirector.jsp"/>
<%--会议纪要 党委会--%>
<jsp:include page="../print/print-decisionMakingPart.jsp"/>