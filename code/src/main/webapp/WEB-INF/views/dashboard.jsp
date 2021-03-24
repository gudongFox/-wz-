<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<link rel="stylesheet" href="/assets/liMarquee/css/liMarquee.css">--%>
<%--<div class="row" ng-show="vm.notices.length>0" ng-controller="RedHeaderController">--%>
<%--    <div class="col-md-12" style="padding-bottom: 10px;">--%>
<%--        <div id="noticeList" style="background: #fff;height: 40px;line-height: 40px;font-weight: bold;">--%>
<%--            <a ng-click="vm.showNoticeDetail(notice.id,notice.attachId)" ng-repeat="notice in vm.notices" style="margin-left: 100px;">--%>
<%--                <span ng-show="notice.top" style="color: red;">置顶</span>--%>
<%--                <span ng-bind="'【'+notice.noticeType+'】'"></span>--%>
<%--                <span ng-bind="notice.gmtCreate|date:'yyyy-MM-dd'"></span>--%>
<%--                <span ng-bind="notice.noticeTitle"></span>--%>
<%--            </a>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

<div class="row ">
    <div class="col-md-2" style="padding-right: 0px;">
        <div class="portlet box blue">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-star"></i> 流程类别
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-default btn-sm" ng-click="vm.loadTree(true);">
                        <i class="fa fa-refresh"></i> 全部 </a>

                </div>
            </div>
            <div class="portlet-body" style="padding-left: 5px;">
                <div id="js_tree"></div>
            </div>
        </div>
    </div>
    <div class="col-md-10">
        <!-- BEGIN PORTLET -->
        <div class="portlet box blue ">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-clock"></i>
                    <span class="caption-subject bold uppercase">待办事宜</span>
                </div>
                <div class="actions">
                    <a href="javascript:;" class="btn btn-default btn-sm" ng-click="vm.init();">
                        <i class="fa fa-refresh"></i> 刷新 </a>

                    <a href="javascript:;" class="btn btn-default btn-sm" ng-click="back();">
                        <i class="fa fa-arrow-left"></i> 返回 </a>
                </div>
            </div>
            <div class="portlet-body">
                <div class="tabbable-line">
                    <ul class="nav nav-tabs ">
                        <li class="active">
                            <a href="#tab_15_1" data-toggle="tab" aria-expanded="true" ng-click="vm.showTable('待办任务');">
                                待办任务 <span class="badge badge-success" ng-bind="vm.taskTotal" ng-if="vm.taskTotal>0"></span> </a>
                        </li>
                        <li>
                            <a href="#tab_15_1_2" data-toggle="tab" aria-expanded="true" ng-click="vm.showTable('抄送我的');">
                                抄送我的 <span class="badge badge-success" ng-bind="vm.ccTaskTotal" ng-if="vm.ccTaskTotal>0"></span> </a>
                        </li>
                        <li>
                            <a href="#tab_15_2" data-toggle="tab" aria-expanded="true"  ng-click="vm.showTable('我发起的');">
                                我发起的 </a>
                        </li>
                        <li class="">
                            <a href="#tab_15_3" data-toggle="tab" aria-expanded="false"  ng-click="vm.showTable('我审批的');">
                                我审批的 </a>
                        </li>
                    </ul>

                    <div class="tab-content">
                        <div class="tab-pane active" id="tab_15_1">
                            <div class="row">
                                <div class="col-md-12">

                                    <a class="btn green" style="float: right;" ng-click="vm.reloadTask();"><i
                                            class="fa fa-search"></i> 查询 </a>
                                    <label style="float: right;margin-right: 10px;">流程描述：<input type="search"
                                                                                                class="form-control input-inline"
                                                                                                placeholder="流程描述"
                                                                                                ng-model="vm.taskParams.description"></label>

                                    <label style="float: right;margin-right: 10px;">发起人：<input type="search"
                                                                                                class="form-control input-inline"
                                                                                                placeholder="发起人"
                                                                                                ng-model="vm.taskParams.initiator"></label>
                                </div>
                            </div>
                            <div class="table-scrollable">
                                <table class="table table-striped table-bordered table-advance table-hover">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th><i class="fa fa-tasks"></i> 流程名称</th>
                                        <th style="width: 35%;"><i class="fa fa-tasks"></i> 流程描述</th>
                                        <th style="width: 150px;"><i class="fa fa-bookmark-o"></i> 我的节点</th>
                                        <th style="width: 140px;"><i class="fa fa-clock-o"></i> 指派时间</th>
                                        <th style="width: 100px;"><i class="fa fa-user"></i> 发起人</th>
                                        <th style="width: 60px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="task in vm.taskList">
                                        <td class="dt-right" ng-bind="$index+vm.taskPageInfo.startRow"></td>
                                        <td ng-click="vm.showDetail(task.businessKey);">
                                            <span ng-bind="task.processDefinitionName" ng-style="{'color':'{{task.firstNewTask?'grey':'blue'}}'}"></span>
                                            <span ng-show="task.ccTask"  class="label label-sm label-default ">抄送</span>
                                            <span ng-hide="task.outTime!=3"  class="label label-sm label-danger ">超期</span>
                                        </td>
                                        <td ng-bind="task.processDescription"></td>

                                        <td ng-click="vm.showTaskProcess(task);" style="cursor: pointer;">
                                            <strong ng-bind="task.name"></strong>
                                        </td>
                                        <td ng-bind="task.createTime|date:'yyyy-MM-dd HH:mm'"></td>
                                        <td ng-bind="task.initiatorName"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" title="立即办理"
                                               ng-click="vm.showDetail(task.businessKey);"></i>

                                            <i class="fa fa-trash" title="删除流程" ng-if="task.firstTask"
                                               ng-click="vm.removeProcess(task.processInstanceId);"></i>

                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <my-pager data-page-info="vm.taskPageInfo"
                                      on-load="vm.loadTask()"></my-pager>
                        </div>


                        <div class="tab-pane " id="tab_15_1_2">

                            <div class="row">
                                <div class="col-md-12">

                                    <a class="btn green" style="float: right;" ng-click="vm.reloadCcTask();"><i
                                            class="fa fa-search"></i> 查询 </a>
                                    <label style="float: right;margin-right: 10px;">流程描述：<input type="search"
                                                                                                class="form-control input-inline"
                                                                                                placeholder="流程描述"
                                                                                                ng-model="vm.ccTaskParams.description"></label>

                                    <label style="float: right;margin-right: 10px;">发起人：<input type="search"
                                                                                               class="form-control input-inline"
                                                                                               placeholder="发起人"
                                                                                               ng-model="vm.ccTaskParams.initiator"></label>
                                </div>
                            </div>
                            <div class="table-scrollable">
                                <table class="table table-striped table-bordered table-advance table-hover" >
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th><i class="fa fa-tasks"></i> 流程名称</th>
                                        <th style="width: 35%;"><i class="fa fa-tasks"></i> 流程描述</th>
                                        <th style="width: 200px;"><i class="fa fa-bookmark-o"></i> 我的节点</th>
                                        <th style="width: 140px;"><i class="fa fa-clock-o"></i> 指派时间</th>
                                        <th style="width: 100px;"><i class="fa fa-user"></i> 发起人</th>
                                        <th style="width: 60px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="task in vm.ccTaskList">
                                        <td class="dt-right" ng-bind="$index+vm.taskPageInfo.startRow"></td>
                                        <td ng-click="vm.showDetail(task.businessKey);">
                                            <span ng-bind="task.processDefinitionName" ng-style="{'color':'{{task.firstNewTask?'grey':'blue'}}'}"></span>
                                            <span ng-hide="task.outTime!=3"  class="label label-sm label-danger ">超期</span>
                                        </td>
                                        <td ng-bind="task.processDescription"></td>

                                        <td ng-click="vm.showTaskProcess(task);" class="no-wrap">
                                            <strong ng-bind="task.name"></strong>
                                        </td>
                                        <td ng-bind="task.createTime|date:'yyyy-MM-dd HH:mm'"></td>
                                        <td ng-bind="task.initiatorName"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" title="立即办理"
                                               ng-click="vm.showDetail(task.businessKey);"></i>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <my-pager data-page-info="vm.ccTaskPageInfo"
                                      on-load="vm.loadCcTask()"></my-pager>
                        </div>

                        <div class="tab-pane" id="tab_15_2"
                             style="min-height: 400px;overflow-y: auto;overflow-x: hidden;">
                            <div class="row">
                                <div class="col-md-12">
                                    <a class="btn green" style="float: right;" ng-click="vm.reloadMyProcess();"><i
                                            class="fa fa-search"></i> 查询 </a>

                                    <label style="float: right;margin-right: 10px;">
                                        <select class="form-control" ng-model="vm.myParams.finish">
                                            <option value="">全部</option>
                                            <option value="0">处理中流程</option>
                                            <option value='1'>已完成流程</option>
                                        </select>
                                    </label>
                                    <label style="float: right;margin-right: 10px;">流程描述：<input type="search"
                                                                                                class="form-control input-inline"
                                                                                                placeholder="流程描述"
                                                                                                ng-model="vm.myParams.description"></label>

<%--                                    <label style="float: right;margin-right: 10px;">流程名称：<input type="search"--%>
<%--                                                                                                class="form-control input-inline"--%>
<%--                                                                                                placeholder="流程名称"--%>
<%--                                                                                                ng-model="vm.myParams.processDefinitionName"></label>--%>

                                </div>
                            </div>
                            <div class="table-scrollable">
                                <table class="table table-striped table-bordered table-advance table-hover">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th><i class="fa fa-tasks"></i> 流程名称</th>
                                        <th style="width: 35%;"><i class="fa fa-tasks"></i> 流程描述</th>
                                        <th><i class="fa fa-bookmark-o"></i> 当前进度</th>
                                        <th style="width: 140px;"><i class="fa fa-clock-o"></i> 开始时间</th>
                                        <th style="width: 60px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="process in vm.myProcessList">
                                        <td class="dt-right" ng-bind="$index+vm.myPageInfo.startRow"></td>
                                        <td class="data_title" ng-bind="process.instance.processDefinitionName"
                                            ng-click="vm.showDetail(process.instance.businessKey);"></td>
                                        <td ng-bind="process.processDescription"></td>
                                        <td ng-click="vm.showTaskProcess(process.instance);" style="cursor: pointer;">
                                            <strong ng-bind="process.currentTaskName"></strong>
                                        </td>
                                        <td ng-bind="process.initiatorTime|date:'yyyy-MM-dd HH:mm'"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" title="查看详情"
                                               ng-click="vm.showDetail(process.instance.businessKey);"></i>

                                            <i class="fa fa-trash" title="删除流程" ng-if="process.firstTask"
                                               ng-click="vm.removeProcess(process.instance.id);"></i>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>

                            <my-pager data-page-info="vm.myPageInfo"
                                      on-load="vm.loadMyProcess()"></my-pager>
                        </div>
                        <div class="tab-pane" id="tab_15_3"
                             style="min-height: 400px;overflow-y: auto;overflow-x: hidden;">
                            <div class="row">
                                <div class="col-md-12">
                                    <a class="btn green" style="float: right;" ng-click="vm.reloadDoneTask();"><i
                                            class="fa fa-search"></i> 查询 </a>
                                    <label style="float: right;margin-right: 10px;">流程描述：<input type="search"
                                                                                                class="form-control input-inline"
                                                                                                placeholder="流程描述"
                                                                                                ng-model="vm.doneParams.description"></label>

                                    <label style="float: right;margin-right: 10px;">发起人：<input type="search"
                                                                                               class="form-control input-inline"
                                                                                               placeholder="发起人"
                                                                                               ng-model="vm.doneParams.initiator"></label>

                                </div>
                            </div>
                            <div class="table-scrollable">
                                <table class="table table-striped table-bordered table-advance table-hover">
                                    <thead>
                                    <tr>
                                        <th style="width: 35px;">#</th>
                                        <th><i class="fa fa-tasks"></i> 流程名称</th>
                                        <th style="width: 35%;"><i class="fa fa-tasks"></i> 流程描述</th>
                                        <th ><i class="fa fa-clock-o"></i> 当前状态</th>
                                        <th style="width: 140px;"><i class="fa fa-clock-o"></i> 指派时间</th>
                                        <th style="width:100px;"><i class="fa fa-user"></i> 发起人</th>
                                        <th style="width: 30px;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="task in vm.taskDoneList">
                                        <td class="dt-right" ng-bind="$index+vm.taskDonePageInfo.startRow"></td>
                                        <td class="highlight" ng-bind="task.instance.processDefinitionName"
                                            ng-click="vm.showDetail(task.instance.businessKey);"
                                            style="font-weight: bold;cursor: pointer;"></td>
                                        <td ng-bind="task.processDescription"></td>
                                        <td ng-bind="task.currentTaskName"></td>
                                        <td ng-bind="task.initiatorTime|date:'yyyy-MM-dd HH:mm'"></td>
                                        <td ng-bind="task.initiatorName"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-5" title="立即办理"
                                               ng-click="vm.showDetail(task.instance.businessKey);"></i>
                                        </td>
                                    </tr>

                                    </tbody>
                                </table>
                            </div>

                            <my-pager data-page-info="vm.taskDonePageInfo" on-load="vm.loadDoneTask()"></my-pager>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- END PORTLET -->

        <div class="modal" id="userModal" tabindex="-1" role="basic" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"
                                aria-hidden="true"></button>
                        <h4 class="modal-title">用户详情</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal" role="form" style="padding-right: 30px;"
                              id="user_from">
                            <div class="form-body">

                                <div class="form-group">
                                    <label class="col-md-2 control-label">用户名称</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control" required="true"
                                               ng-model="vm.sysUser.userName"
                                               name="userName" placeholder="" disabled="disabled">
                                    </div>
                                    <label class="col-md-2 control-label">登录名</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control" required="true"
                                               name="userLogin"
                                               ng-model="vm.sysUser.userLogin"
                                               placeholder="请勿重复" disabled="disabled">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">用户专业</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control" name="specialty"
                                               ng-model="vm.sysUser.specialty">
                                    </div>
                                    <label class="col-md-2 control-label">手机号码</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control" ng-model="vm.sysUser.mobile"
                                               placeholder="11位手机号码">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">
                                        <a ng-href="{{vm.sysUser.headUrl}}" target="_blank"
                                           style="cursor: pointer;">用户头像</a>
                                    </label>
                                    <div class="col-md-10">
                                        <div class="input-group">
                                            <input type="text" class="form-control"
                                                   ng-model="vm.sysUser.headUrl"
                                                   maxlength="200" name="headUrl" placeholder="">
                                            <span class="input-group-addon"
                                                  style="padding: 0;border: none;">
                                    <span class="btn blue fileinput-button">
                                      <i class="fa fa-cloud-upload" style="height:15px "></i>
                                      <span>上传</span>
                                      <input type="file" id="btnUploadHead" name="multipartFile"
                                             data-url="/sys/attach/receive.json"
                                             accept="*"/>
                                    </span>
                                        </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">
                                        <a ng-href="{{vm.sysUser.signUrl}}" target="_blank"
                                           style="cursor: pointer;">签名文件</a>
                                    </label>
                                    <div class="col-md-10">
                                        <div class="input-group">
                                            <input type="text" class="form-control"
                                                   ng-model="vm.sysUser.signUrl"
                                                   maxlength="200" name="signUrl" placeholder="">
                                            <span class="input-group-addon"
                                                  style="padding: 0;border: none;">
                                    <span class="btn blue fileinput-button">
                                      <i class="fa fa-cloud-upload" style="height:15px "></i>
                                      <span>上传</span>
                                      <input type="file" id="btnUploadSign" name="multipartFile"
                                             data-url="/sys/attach/receive.json"
                                             accept="*"/>
                                    </span>
                                        </span>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">职务</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control"
                                               ng-model="vm.sysUser.workTitle"
                                               placeholder="职务">
                                    </div>

                                    <label class="col-md-2 control-label">岗位</label>
                                    <div class="col-md-4">
                                        <input type="text" class="form-control" required="true"
                                               name="workPosition"
                                               ng-model="vm.sysUser.workPosition"
                                               placeholder="岗位">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-2 control-label">创建时间</label>
                                    <div class="col-md-4">
                                        <div class="input-icon right">
                                            <i class="fa fa-clock-o"></i>
                                            <input type="text" class="form-control"
                                                   value="{{vm.sysUser.gmtCreate|date:'yyyy-MM-dd HH:mm'}}"
                                                   disabled="disabled">
                                        </div>
                                    </div>

                                    <label class="col-md-2 control-label">修改时间</label>
                                    <div class="col-md-4">
                                        <div class="input-icon right">
                                            <i class="fa fa-clock-o"></i>
                                            <input type="text" class="form-control"
                                                   value="{{vm.sysUser.gmtModified|date:'yyyy-MM-dd HH:mm'}}"
                                                   disabled="disabled">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn blue" ng-click="vm.saveUser();">保存</button>
                    </div>
                </div>
                <!-- /.modal-content -->
            </div>
            <!-- /.modal-dialog -->
        </div>

    </div>
</div>

<div class="modal drag fade" id="taskProcessModal" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" ng-bind="vm.task.processDefinitionName+' '+ vm.task.description"></h4>
            </div>
            <div class="modal-body row">
                <div class="col-md-12">
                    <div class="tabbable-line">
                        <ul class="nav nav-tabs ">
                            <li class="active">
                                <a href="#tab_2_1" data-toggle="tab" aria-expanded="true">
                                    流程图 </a>
                            </li>
                            <li class="">
                                <a href="#tab_2_2" data-toggle="tab" aria-expanded="false">
                                    流程信息 </a>
                            </li>
                        </ul>

                        <div class="tab-content ">
                            <div class="tab-pane active" id="tab_2_1"
                                 style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                                <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                            </div>
                            <div class="tab-pane" id="tab_2_2"
                                 style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                                <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.showDetailByModal(vm.task);">查看详情</button>
            </div>
        </div>
    </div>
</div>

<%--<div class="modal fade" id="noticeShowModel" tabindex="-1" role="basic" aria-hidden="true">--%>
<%--    <div class="modal-dialog modal-lg">--%>
<%--        <div class="modal-content">--%>
<%--            <div class="modal-header">--%>
<%--                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>--%>
<%--                <h4 class="modal-title">电子公告详情-<span ng-bind="vm.notice.noticeTitle"></span></h4>--%>
<%--            </div>--%>
<%--            <div class="modal-body">--%>
<%--                <textarea name="content" style="width:100%;height: 400px;" id="editorText"  ></textarea>--%>
<%--            </div>--%>
<%--            <div class="portlet light">--%>
<%--                <div class="portlet-title">--%>
<%--                    <div class="caption">--%>
<%--                        <i class="fa fa-file"></i> 附件列表--%>
<%--                    </div>--%>
<%--                    <div class="actions" >--%>
<%--                        <a href="javascript:;" class="btn btn-sm default" ng-show="browserVersion!='ie9'"--%>
<%--                           ng-click="vm.batchDownloadEdFile();">--%>
<%--                            <i class="fa fa-download" title="批量下载"></i> 下载 </a>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--                <div class="portlet-body">--%>
<%--                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">--%>
<%--                        <table class="table table-striped table-hover table-bordered table-advance no-footer">--%>
<%--                            <thead>--%>
<%--                            <tr>--%>
<%--                                <th style="width: 35px;">#</th>--%>
<%--                                <th>名称</th>--%>
<%--                                <th style="width: 90px;">大小</th>--%>
<%--                                <th style="width: 60px;">创建人</th>--%>
<%--                                <th style="width: 130px;">创建时间</th>--%>
<%--                                <th style="width: 130px;">修改时间</th>--%>
<%--                            </tr>--%>
<%--                            </thead>--%>
<%--                            <tbody>--%>
<%--                            <tr ng-repeat="file in vm.files">--%>
<%--                                <td>--%>
<%--                                    <input type="checkbox" class="cb_edFile" data-index="{{$index}}" ng-show="browserVersion!='ie9'"/>--%>
<%--                                    <span ng-bind="$index+1" ng-show="browserVersion=='ie9'"></span>--%>
<%--                                </td>--%>
<%--                                <td>--%>
<%--                                    <img class="cloud-file-type" ng-src="/m/img/filetype/{{file.extensionName}}.png" onerror="this.src='/m/img/filetype/file.png'"/>--%>
<%--                                    <span ng-bind="file.fileName" ng-show="browserVersion!='ie9'" class="data_title"--%>
<%--                                          ng-click="downloadFileWithXml(file.sysAttach.id);"></span>--%>
<%--                                    <a ng-href="{{'/sys/attach/download/'+file.sysAttach.id}}" class="a_title" ng-show="browserVersion=='ie9'" ng-bind="file.fileName" ></a>--%>
<%--                                    <span class="label label-sm label-success" ng-show="processInstance.preHandleTime<file.gmtModified">新</span>--%>
<%--                                </td>--%>
<%--                                <td ng-bind="file.sizeName"></td>--%>
<%--                                <td ng-bind="file.creatorName"></td>--%>
<%--                                <td ng-bind="file.gmtCreate|date:'yyyy-MM-dd HH:mm'"></td>--%>
<%--                                <td ng-bind="file.gmtModified|date:'yyyy-MM-dd HH:mm'"></td>--%>
<%--                            </tr>--%>
<%--                            </tbody>--%>
<%--                        </table>--%>
<%--                    </div>--%>
<%--                    <p ng-show="browserVersion!='ie9'&&vm.files.length>1"><span style="cursor: pointer;color: #3598dc;" ng-click="selectAllEdFile();">全选</span><span style="cursor: pointer;color: #3598dc;margin-left: 10px;"  ng-click="cancelAllEdFile();" >取消</span></p>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--            <div class="modal-footer">--%>
<%--                <button type="button" class="btn default" data-dismiss="modal">关闭</button>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <!-- /.modal-content -->--%>
<%--    </div>--%>
<%--    <!-- /.modal-dialog -->--%>
<%--</div>--%>
<%--<script src="/assets/liMarquee/js/jquery.liMarquee.js"></script>--%>


