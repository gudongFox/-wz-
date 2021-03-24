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
            <span ng-bind="tableName">部门发文单</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">部门发文单</span>
               <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" <%--ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator"--%>>
                <i class="fa fa-print"></i> 打印 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.goNotice();" style="font-size: 14px;line-height: 1.6"  ng-show="vm.item.processEnd">
                <i class="fa fa-print"></i> 转通知公告 </a>
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
                                <label class="col-md-2 control-label required">标题</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.title" name="title" required="true" ng-disabled="!processInstance.firstTask" />
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
                                        <span class='btn btn-sm default fileinput-button' ng-click=" rh.editRedHead(vm.item)" ng-show="redheadFile!=undefined&&(processInstance.myRunningTaskName.indexOf('发起人')>=0||processInstance.myRunningTaskName.indexOf('文号')>=0)"><i class='fa fa-edit'></i><span>编辑/套红</span></span>
                                        <a class='btn btn-sm default fileinput-button' ng-href="/wuzhou/file/downloadRedHead/{{vm.item.businessKey}}" ng-if="redheadFile!=undefined&&vm.item.processEnd"><i class='fa fa-download'></i><span>下载</span></a>
                                        <span class='btn btn-sm default fileinput-button' ng-click=" rh.remove(vm.item.businessKey,redheadFile.id)" ng-if="redheadFile!=undefined&&processInstance.firstTask"><i class='fa fa-trash'></i><span>删除</span></span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group" >
                                <label class="col-md-2 control-label required" style="font-weight: bold;font-size: 18px">发文</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.dispatch" name="dispatch" required="true"  ng-disabled="processInstance.myRunningTaskName.indexOf('部门负责人-审批文号')==-1||vm.showDis"/>
                                </div>
                                <label class="col-md-2 control-label required" ng-if="processInstance.myRunningTaskName.indexOf('部门负责人-审批文号')>=0&&vm.showDis">申请文号</label>
                                <div class="col-md-4" ng-if="processInstance.myRunningTaskName.indexOf('部门负责人-审批文号')>=0&&vm.showDis">
                                    <div class="input-group" >
                                        <select style="height: 34px;width: 60%" ng-change="vm.changeWord(1);"  ng-options="s.word as s.word for s in vm.wordSizeList" ng-model="vm.selectword" ></select>
                                        <select style="height: 34px;width: 20%"  ng-model="vm.selectyear" ng-change="vm.changeWord(2);">
                                            <option ng-value="vm.year">{{vm.year}}</option>
                                            <option ng-value="vm.year+1">{{vm.year+1}}</option>
                                            <option ng-value="vm.year-1">{{vm.year-1}}</option>
                                            <option value=" "> </option>
                                        </select>
                                        <input style="height: 34px;width: 20%" type="number"  ng-model="vm.selectmark" ng-change="vm.changeWord(3);">
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.updateWordSize();" >申请<i class="fa fa-send"></i></button>
                                        </span>
                                        <span>&nbsp;</span>
                                        <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.loadHistory(1);" ><i class="fa fa-history"></i></button>
                                      </span>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">拟稿人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.drafterName" name="auditManName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showUserModel('drafter');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label " style="font-weight: bold;">核稿人</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.checkManName" name="checkManName"   ng-disabled="!processInstance.firstTask" />
                                        <span class="input-group-btn" >
                                            <button class="btn default"  type="button" ng-click="vm.showUserModel('checkMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-user"></i></button>
                                         </span>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">主办单位</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" required="true"   ng-disabled="!processInstance.firstTask"/>
                                        <span class="input-group-btn" >
                                            <button class="btn default" type="button" ng-click="vm.showDeptModal('deptId');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">主题词</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.subjectTerms" name="subjectTerms"   ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>


                            <div class="form-group">
                                <label class="col-md-2 control-label ">主送</label>
                                <div class="col-md-10">
                                    <div class="input-group">
                                        <textarea rows="3" class="form-control" ng-model="vm.item.realSendManName" name="realSendManName" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                                        <span class="input-group-btn" >
                                            <button class="btn default" style="height: 74px;" type="button" ng-click="vm.showDeptModal('realSendMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">抄送</label>
                                <div class="col-md-10">
                                    <div class="input-group">
                                        <textarea rows="3" class="form-control" ng-model="vm.item.copySendManName"  name="copySendManName" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                                        <span class="input-group-btn" >
                                            <button class="btn default" style="height: 74px;" type="button" ng-click="vm.showDeptModal('copySendMan');" ng-disabled="!processInstance.firstTask"><i class="fa fa-cog"></i></button>
                                         </span>
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
                        </div>
                    </form>
                    <jsp:include page="../../common/common-opinion.jsp"/>
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

<div class="modal fade" id="planTypeListModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">选择转发类型或部门</h4>
            </div>
            <div class="modal-body">
                <span ng-repeat="type in vm.noticeType">
                    <input type="checkbox" style="width: 15px;height: 15px;" class="cb_type"

                           data-name="{{type}}"/> <span ng-bind="type"
                                                        class="margin-right-10"
                                                        style="font-size: 15px;"></span>
                    <br ng-if="($index+1)%4==0"/>
               </span>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.goNotice();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<jsp:include page="../print/print-oaDepartmentPostDetail.jsp"/>
