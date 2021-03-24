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
            <span ng-bind="tableName">公司收文</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">公司收文</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.showSupervise();" style="font-size: 14px;line-height: 1.6;color: #fecd1e;border: 1px solid #ffd01e" ng-show="processInstance.myRunningTaskName.indexOf('公司领导')>-1">
                <i class="fa fa-print" style="color: #ffc81f"></i> 督办 </a>
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
                                <label class="col-md-2 control-label required">文件标题</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.fileTitle" required="true" name="fileTitle" placeholder="" ng-disabled="!processInstance.firstTask"></input>
                                </div>
                            </div>
                            <%--发文正文--%>
                            <div ng-controller="RedHeaderController as rh">
                                <div class="form-group">
                                    <label class="col-md-2 control-label " style="font-weight: bold;font-size: 18px"><strong>发文正文</strong></label>
                                    <div class="col-md-10">
                                        <a href="#" ng-click="rh.readDocOnly(redheadFile.id,redheadFile.fileName)" ng-bind="redheadFile.fileName">红头文件.doc</a>&nbsp;&nbsp;
                                        <span id="btnUpload0" class="btn btn-sm default fileinput-button" ng-show="processInstance.firstTask">
                                        <i class="fa fa-cloud-upload" ></i><span>上传</span>
                                        <input id="redHead" type="file" name="singleUpload" accept=".doc,.docx,.pdf"></span>
                                        <a class='btn btn-sm default fileinput-button' ng-href="/wuzhou/file/downloadRedHead/{{vm.item.businessKey}}" ng-if="redheadFile!=undefined"><i class='fa fa-download'></i><span>下载</span></a>
                                        <span class='btn btn-sm default fileinput-button' ng-click=" rh.remove(vm.item.businessKey,redheadFile.id)" ng-if="redheadFile!=undefined&&processInstance.firstTask"><i class='fa fa-trash'></i><span>删除</span></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required" style="font-weight: bold;">签收人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.signerName" name="signerName"  required="true" ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('signer');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">签收日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="receiveTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.receiveTime" name="receiveTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group" >
                                <label class="col-md-2 control-label required">批示领导</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.companyLeaderName" name="companyLeaderName"  required="true"   disabled/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('companyLeader');"  ng-disabled="processInstance.myRunningTaskName.indexOf('核稿')==-1"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('核稿')>=0">请点击左侧按钮选择批示领导</span>
                                </div>
                                <label class="col-md-2 control-label ">承办单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.undertakeDeptName" name="undertakeDeptName" title="{{vm.item.undertakeDeptName}}"  disabled/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal(vm.item.undertakeDeptId);" ng-disabled="processInstance.myRunningTaskName.indexOf('机要秘书-分发')==-1" ><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('机要秘书-分发')>=0">请点击左侧按钮选择承办单位：可单选，多选，不选</span>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">阅办领导</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.readLeaderName" name="readLeaderName"  required="true"   disabled/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('readLeader');"  ng-disabled="processInstance.myRunningTaskName.indexOf('机要秘书-分发')==-1"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                    <span style="color: red" ng-show="processInstance.myRunningTaskName.indexOf('机要秘书-分发')>=0">机要秘书,请点击左侧按钮选择阅示领导</span>
                                </div>

                                <label class="col-md-2 control-label required">来文单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.sendDeptName" name="sendDeptName" required="true" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">收文号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.receiveWordSize" name="receiveWordSize" required="true"   readonly/>
                                </div>

                                <label class="col-md-2 control-label required">来文号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.sendWordSize" name="sendWordSize" required="true"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <%----%>
                            <div class="form-group" ng-if="processInstance.myRunningTaskName.indexOf('收文登记')>=0">
                                <label class="col-md-2 control-label required"><strong>选择收文</strong></label>
                                <div class="col-md-4">
                                    <div class="input-group" >
                                        <select style="height: 34px;width: 60%" ng-change="vm.changeWord(1);"  ng-options="s.word as s.word for s in vm.wordSizeList" ng-model="vm.selectword"></select>
                                        <select style="height: 34px;width: 20%"  ng-model="vm.selectyear" ng-change="vm.changeWord(2);">
                                            <option ng-value="vm.year">{{vm.year}}</option>
                                            <option ng-value="vm.year-1">{{vm.year-1}}</option>
                                            <option ng-value="vm.year+1">{{vm.year+1}}</option>
                                        </select>
                                        <input style="height: 34px;width: 20%" type="number"  ng-model="vm.selectmark" ng-change="vm.changeWord(3);">
                                        <span class="input-group-btn"  >
                                            <button class="btn default" type="button" ng-click="vm.updateWordSize();" >申请<i class="fa fa-send-o"></i></button>
                                        </span>
                                        <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.loadHistory(1);" ><i class="fa fa-history"></i></button>
                                      </span>
                                    </div>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label required" >是否涉密</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'是否默认'}:true"
                                            ng-model="vm.item.security" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                    <span ng-show="vm.item.security=='是'" style="color: red">注：此平台为非密平台，严禁填写涉密信息！</span>
                                </div>

                                <label class="col-md-2 control-label required">正文份数</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.textNumber" name="textNumber" required="true"   ng-disabled="!processInstance.firstTask"/>
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
                            <div class="form-group" ng-show="processInstance.myRunningTaskName.indexOf('公司领导')>-1">
                                <div style="color: red;font-size: 16px;padding-left: 200px;text-align: left">注:可点击右上督办按钮选择是否督办。</div>
                            </div>
                        </div>
                    </form>
                    <style type="text/css">
                        .span{
                            font-size: 16px;
                            display:-moz-inline-box;
                            display:inline-block;
                            width:150px;
                        }
                    </style>

                    <div class="portlet light"  >
                        <div class="portlet-title " style="border-bottom:1px solid #eee;">
                            <div class="caption">
                                <i class="fa fa-file"></i>
                                <span class="caption-subject" >处理意见</span>
                            </div>
                        </div>
                        <div class="portlet-body ">
                            <form class="form-horizontal" role="form">
                                <div class="form-body">
                                    <div class="col-md-12" >
                                        <div class="col-md-7" style="text-align:center;border-right: 1px red solid">
                                            <div style="padding-bottom: 10px" ng-if="vm.optionlistLeader.length>0"><span style="color: red;font-size: 16px">领导节点意见</span></div>
                                            <div class="form-group" ng-repeat="task in vm.optionlistLeader" ng-if="task.latestComment!=''">
                                                <div class="col-md-12"  >
                                                    <span class="col-md-4" style="text-align: right" ng-bind="task.name"></span>
                                                    <span class="col-md-2" ng-bind="task.assigneeName"  style="cursor: pointer;"></span>
                                                    <img  class="col-md-1" style="width: 90px;height: 35px;margin-left: 20px;margin-right: 20px;"  ng-src="{{'/sys/attach/downloadPic/'+task.assignee}}">
                                                    <div>
                                                        <span class="" ng-bind="task.latestComment"  style="cursor: pointer;"></span>
                                                    </div>
                                                    <div>
                                                        <span class="" ng-bind="task.endTime|date:'yyyy-MM-dd HH:mm'"></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-5" style="text-align:center;">
                                            <div style="padding-bottom: 10px" ng-if="vm.optionlistCountSign.length>0"><span style="color: red;font-size: 16px;">会签节点意见</span></div>
                                            <div class="form-group" ng-repeat="task in vm.optionlistCountSign" ng-if="task.latestComment!=''" >
                                                <div class="col-md-12" >
                                                    <span class="col-md-4" style="text-align: right" ng-bind="task.name"></span>
                                                    <span class="col-md-2" ng-bind="task.assigneeName"  style="cursor: pointer;"></span>
                                                    <img  class="col-md-1" style="width: 90px;height: 35px;margin-left: 20px;margin-right: 20px;"  ng-src="{{'/sys/attach/downloadPic/'+task.assignee}}">
                                                    <div>
                                                        <span class="" ng-bind="task.latestComment"  style="cursor: pointer;"></span>
                                                    </div>
                                                    <div>
                                                        <span class="" ng-bind="task.endTime|date:'yyyy-MM-dd HH:mm'"></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </form>
                        </div>

                    </div>
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

<jsp:include page="../../common/common-actRelevance.jsp"/>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade draggable-modal" id="deptSelectModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择所属部门</h4>
            </div>
            <div class="modal-body">
                <div id="dept_select_tree" style="max-height:500px;overflow-y: auto;overflow-x: hidden;"></div>
            </div>
            <div class="modal-footer">
                <span style="float: left;color: red">注：ctrl+鼠标左键可多选</span>
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDept();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div class="modal fade draggable-modal" id="wordSizeModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" >字号</h4>
            </div>
            <div class="modal-body">
                <div class="actions">
                    <div class="btn-group" data-toggle="buttons">
                        <label class="btn grey-steel btn-sm active" ng-click="vm.loadHistory(1);">
                            <input type="radio" name="options" class="toggle" id="option1" >已使用</label>
                        <label class="btn grey-steel btn-sm "  ng-click="vm.loadHistory(2);">
                            <input type="radio" name="options" class="toggle" id="option2" >废弃号</label>
                        <label class="btn grey-steel btn-sm " ng-click="vm.loadHistory(vm.item.businessKey);"0>
                            <input type="radio" name="options" class="toggle" id="option3">本文单使用号</label>
                    </div>
                </div>
                <div class="dataTables_wrapper no-footer">
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>文号</th>
                                <th>使用人</th>
                                <th>使用时间</th>
                                <th>状态</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="history in  vm.wordHistoryList">
                                <td>
                                    <span ng-bind="$index+1"></span>
                                </td>
                                <td>
                                    <span ng-bind="history.word"></span>
                                    〔<span ng-bind="history.year"></span>〕
                                    <span ng-bind="history.mark"></span>号
                                </td>
                                <td ng-bind="history.creatorName"></td>
                                <td ng-bind="history.gmtModified|date:'yyyy-MM-dd'"></td>
                                <td>
                                    <span ng-if="!history.deleted" style="color: #0bb20c">已使用</span>
                                    <span ng-if="history.deleted" style="color: red">废弃</span>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <%--<my-pager data-page-info="vm.pageInfo" on-load="vm.listAllRoom()"></my-pager>--%>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveWordSize();">确认</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<div class="modal fade" id="superviseModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">督办</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="PointDetail_form">
                    <div class="form-group">
                        <label class="col-md-3 control-label required">选择督办人</label>
                        <div class="col-md-9">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.super.userName" name="userLogin"  required="true" ng-disabled="false" disabled/>
                                <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('userLogin');" disabled><i class="fa fa-user"></i></button>
                                         </span>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">督办类型</label>
                        <div class="col-md-9">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'督办类型'}:true " disabled
                                    ng-model="vm.super.superviseType" class="form-control"
                                    ng-disabled="false"></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">办理意见</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.super.view" required="true"
                                   name="view" placeholder="">
                        </div>
                    </div>
<%--                    <div class="form-group">
                        <label class="col-md-3 control-label required">论述放弃理由</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.reason"  required="true"
                                   name="reason" placeholder="" >
                        </div>
                    </div>--%>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveSupervise();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<jsp:include page="../print/print-oaFileInstructionDetail.jsp"/>

