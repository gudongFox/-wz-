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
            <span ng-bind="tableName">签报</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">签报</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6">
            <i class="fa fa-print"></i> 打印 </a>
          <%--  <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.createDecisionBySignBusinessKey();" style="font-size: 14px;line-height: 1.6">
                <i class="fa fa-print"></i> 议题 </a>--%>
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
                    <a href="#tab_15_4" data-toggle="tab" aria-expanded="false">
                        议题信息
                    </a>
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

                        <div class="form-group">
                            <label class="col-md-2 control-label required">事项</label>
                            <div class="col-md-10">
                                <input type="text" class="form-control" ng-model="vm.item.item" name="item" required="true" ng-disabled="processInstance.myRunningTaskName.indexOf('发起人')==-1" />
                            </div>
                        </div>
                        <%--发文正文--%>
                        <div ng-controller="RedHeaderController as rh">
                            <div class="form-group">
                                <label class="col-md-2 control-label " style="font-weight: bold;font-size: 18px"><strong>发文正文</strong></label>
                                <div class="col-md-10">
                                    <a href="#" ng-click="rh.readDocOnly(vm.item.businessKey)" ng-bind="redheadFile.fileName"></a>&nbsp;&nbsp;
                                    <span id="btnUpload0" class="btn btn-sm default fileinput-button" ng-show="processInstance.myRunningTaskName.indexOf('发起人')>=0">
                                        <i class="fa fa-cloud-upload" ></i><span>上传</span>
                                        <input id="redHead" type="file" name="singleUpload" accept=".doc,.docx,.pdf"></span>
                                    <a class='btn btn-sm default fileinput-button' ng-href="/wuzhou/file/downloadRedHead/{{vm.item.businessKey}}" ng-if="redheadFile!=undefined&&vm.item.processEnd"><i class='fa fa-download'></i><span>下载</span></a>
                                    <span class='btn btn-sm default fileinput-button' ng-click=" rh.remove(vm.item.businessKey,redheadFile.id)" ng-if="redheadFile!=undefined&&processInstance.myRunningTaskName.indexOf('发起人')>=0"><i class='fa fa-trash'></i><span>删除</span></span>
                                </div>
                            </div>
                        </div>
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label ">公司办编号</label>
                                <div class="col-md-4">
                                    <input  type="text" class="form-control" ng-model="vm.item.companyNo" name="companyNo" ng-disabled="processInstance.myRunningTaskName.indexOf('机要秘书-分发公司领导')==-1"/>
                                </div>
                                <label class="col-md-2 control-label required">会签类型</label>
                                <div class="col-md-4">
                                  <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'会签类型'}:true"
                                          ng-model="vm.item.flag" class="form-control" ng-disabled="processInstance.myRunningTaskName.indexOf('部门负责人')==-1"></select>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('部门负责人')>=0">请选择会签类型</span>
                                </div>
                            </div>
                            <div class="form-group" ng-if="vm.item.flag!='不会签'">
                                <label class="col-md-2 control-label required" >会签部门</label>
                                <div class="col-md-4" >
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.instructDeptName" name="instructDeptName" required="true"    disabled/>
                                        <span class="input-group-btn" >
                                                <button class="btn default" type="button" ng-click="vm.opt='instructDeptId';vm.showDeptModal(vm.item.instructDeptId);"   ng-disabled="processInstance.myRunningTaskName.indexOf('部门负责人')==-1"><i class="fa fa-cog"></i></button>
                                        </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('部门负责人')>=0">请选择会签部门</span>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="vm.item.flag=='通用会签'">是否法律审查</label>
                                <div class="col-md-4" ng-if="vm.item.flag=='通用会签'">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否法律审查'}:true"
                                            ng-model="vm.item.sign" class="form-control"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('部门负责人')==-1"></select>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('部门负责人')>=0">请选择是否法律审查</span>

                                </div>

                                <label class="col-md-2 control-label required"  ng-if="vm.item.flag=='制度会签'">制度会签类型</label>
                                <div class="col-md-4"  ng-if="vm.item.flag=='制度会签'">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'制度会签类型'}:true"
                                            ng-model="vm.item.tab" class="form-control"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('部门负责人')==-1"></select>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('部门负责人')>=0">请选择制度类型</span>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label ">批示领导</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.instructLeaderName" name="instructLeaderName" required="true"  disabled/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('instructLeader');" ng-disabled="processInstance.myRunningTaskName.indexOf('公司办')==-1"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('公司办')>=0">请点击左侧按钮选择批示领导</span>
                                </div>
                                <label class="col-md-2 control-label ">阅办领导</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.companyLeaderName" name="companyLeaderName"   disabled/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('companyLeader');" ng-disabled="processInstance.myRunningTaskName.indexOf('机要秘书-分发公司领导')==-1"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('机要秘书-分发公司领导')>=0">请点击左侧按钮选择公司领导,非必填！</span>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">公司办核收</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.companyCheckManName" name="companyCheckManName" required="true"  readonly/>
                                        <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.showUserModel('companyCheckMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                       </span>
                                    </div>
                                    <span class="help-block" style="color: red" ng-show="processInstance.firstTask">请选择公司办核收人员</span>
                                </div>
                                <label class="col-md-2 control-label required">是否属于“三重一大”事项</label>
                                <div class="col-md-4 required" >
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                            ng-model="vm.item.belongThreeOne"  class="form-control"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('部门负责人')==-1"></select>

                                    <span  style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('部门负责人')>=0">请部门负责人选择是否属于“三重一大”事项分类</span>
                                </div>

                            </div>
                            <div class="form-group" ng-if="vm.item.belongThreeOne=='是'">
                                <label class="col-md-2 control-label required">“三重一大”事项分类</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'三重一大分类'}:true"
                                            ng-model="vm.item.belongType" class="form-control"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('部门负责人')==-1"></select>

                                    <span  style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('部门负责人')>=0">请部门负责人选择“三重一大”事项分类</span>
                                </div>
                                <label class="col-md-2 control-label required">“三重一大”具体内容</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:vm.item.belongType}:true"
                                            ng-model="vm.item.belongContent" class="form-control"
                                            ng-disabled="processInstance.myRunningTaskName.indexOf('部门负责人')==-1"></select>

                                    <span  style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('部门负责人')>=0">请部门负责人选择“三重一大”具体内容</span>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">送签部门</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.opt='deptId';vm.showDeptModal();" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                    <p class="help-block" style="color: red" ng-show="processInstance.firstTask">请选择送签部门</p >
                                </div>
                                <label class="col-md-2 control-label required">部门负责人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptChargeManName" name="deptChargeManName" required="true"   readonly />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('deptChargeMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <p class="help-block" style="color: red" ng-show="processInstance.firstTask">请选择部门负责人审核</p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">会议类型</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control ng-pristine ng-untouched ng-empty ng-invalid ng-invalid-required" ng-model="vm.item.meetingType"
                                               name="meetingType" disabled>
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showMeetingModel('agent');"
                                                    ng-disabled="processInstance.myRunningTaskName.indexOf('机要秘书-分发部门')==-1"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                    <p class="help-block" style="color: red"ng-show="processInstance.myRunningTaskName.indexOf('机要秘书-分发部门')>=0">请根据需要点击左侧按钮选择会议类型</p >
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">经办人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control ng-pristine ng-untouched ng-empty ng-invalid ng-invalid-required" ng-model="vm.item.agentName" name="agentName" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('agent');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">成文日期</label>
                                <div class="col-md-4">
                                    <input type="text"  class="form-control" required="true" name="submitTime"
                                           disabled
                                           ng-model="vm.item.submitTime" placeholder="">
                                </div>
                            </div>
                        </div>
                    </form>
                    <jsp:include page="../../common/common-opinion2.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_4"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <div class="portlet light" >
                        <div class="portlet-title">
                            <div class="caption"><i class="fa fa-file"></i> 会议议题</div>
                        </div>
                        <div class="portlet-body">
                            <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer">
                                    <thead>
                                    <tr>
                                        <th style="min-width: 20px;max-width:30px">#</th>
                                        <th style="min-width: 200px;">议题名称</th>
                                        <th style="width: 130px;">会议类型</th>
                                        <th style="min-width: 120px;">列席人</th>
                                        <th >议题结论</th>
                                        <th style="min-width: 120px;">议题状态</th>
                                        <th style="min-width: 90px;">排会人</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="detail in vm.decisionMakingList">
                                        <td ng-bind="$index+1"></td>
                                        <td ng-bind="detail.title"  class="data_title" ng-click="vm.getDecisionMakingModel(detail);"></td>
                                        <td ng-bind="detail.detailType"></td>
                                        <td ng-bind="detail.attendanceName"></td>
                                        <td ng-bind="detail.conclusion"></td>
                                        <td ng-bind="detail.issueStatus"></td>
                                        <td ng-bind="detail.arrangeManName"></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<jsp:include page="../../common/common-actRelevance.jsp"/>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>
<%--不会签--%>
<jsp:include page="../print/print-oaSignQuoteDetail.jsp"/>
<%--通用会签--%>
<jsp:include page="../print/print-oaSignQuoteCommonDetail.jsp"/>
<%--制度会签--%>
<jsp:include page="../print/print-oaSignQuoteSystemDetail.jsp"/>

<div class="modal fade" id="meetingTypeModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择会议类型</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="meeting in vm.meetingList">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_meet"
                           ng-checked="vm.item.meetingType.indexOf(meeting.name)>=0"
                           data-name="{{meeting.name}}" data-id="{{'file_'+id}}" /> <span ng-bind="meeting.name"
                                                                                        class="margin-right-10"
                                                                                        style="font-size: 15px;"></span>
                    <br ng-show="($index+1)%6==0"/>
               </span>
                <div style="padding-left: 5px;padding-top: 10px">
                    <span class="data_title" style="font-size: 8px;margin-top: 2px" title="全选"
                          ng-click="vm.selectAllFile();">全选</span><span
                        style="margin-left: 10px;font-size: 8px;" class="data_title" title="反选"
                        ng-click="vm.toggleSelectFile();">反选</span>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveMeetingType();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade" id="decisionDetailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog" style="min-width: 650px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">议题详情</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <label class="col-md-2 control-label required"  style="text-align: right">议题名称</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.title" disabled/>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label required"  style="text-align: right">会议类型</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.detailType" disabled/>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">议题类型</label>
                    <div class="col-md-10">
                        <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'议题类型'}:true"
                                ng-model="vm.currenDetail.detail" class="form-control"
                                disabled></select>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label required"  style="text-align: right">议题结论</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.conclusion" disabled/>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">列席名单</label>
                    <div class="col-md-10">
                            <input type="text" class="form-control" ng-model="vm.currenDetail.attendanceName"    disabled/>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">附件</label>
                    <div class="col-md-10">
                        <a   ng-href="{{'/common/attach/download/'+vm.currenDetail.attachId}}" >{{vm.currenDetail.attachName}}</a>
                    </div>
                    <span>&nbsp</span>
                </div>
                <%--<div class="row">
                    <label class="col-md-2 control-label required"  style="text-align: right">顺序</label>
                    <div class="col-md-10">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.seq" disabled/>
                    </div>
                    <span>&nbsp</span>
                </div>--%>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">入库时间</label>
                    <div class="col-md-4">
                        <input  value="{{vm.currenDetail.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" class="form-control" readonly/>
                    </div>
                    <label class="col-md-2 control-label " style="text-align: right">议题状态</label>
                    <div class="col-md-4">
                        <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'议题状态'}:true"
                                ng-model="vm.currenDetail.issueStatus" class="form-control"
                                disabled></select>
                    </div>
                    <span>&nbsp</span>
                </div>
                <div class="row">
                    <label class="col-md-2 control-label " style="text-align: right">签批领导</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.leader"    disabled/>
                    </div>
                    <label class="col-md-2 control-label " style="text-align: right">议题管理</label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.currenDetail.creatorName"    disabled/>
                    </div>
                    <span>&nbsp</span>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>