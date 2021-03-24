<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:49})">档案申请</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="tableName">非密计算机及信息化设备台帐</span>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.noticeTitle"></span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-note"></i><span ng-bind="tableName">非密计算机及信息化设备台帐</span>
              <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>


        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();" style="font-size: 14px;line-height: 1.6" ng-show="vm.item.processEnd&&user.userLogin==vm.item.creator">
                <i class="fa fa-print"></i> 打印 </a>
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
                     style="min-height: 380px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.applicantName" name="applicantName"  ng-disabled="!processInstance.firstTask" />
                                </div>

                                <label class="col-md-2 control-label ">职工号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.applicantNo"  ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label ">所在单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label ">所属专业</label>
                                <div class="col-md-4">

                                    <input type="text" class="form-control" ng-model="vm.item.applicantMajor" ng-disabled="!processInstance.firstTask"/>

                                </div>
                            </div>
                        <div class="form-group">
                                <label class="col-md-2 control-label required">电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.applicantTel" name="applicantTel" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">借出</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.borrow" name="borrow" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">查阅</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.consult" name="consult" ng-disabled="!processInstance.firstTask"/>
                            </div>
                            <label class="col-md-2 control-label required">复制</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.copy" name="copy" ng-disabled="!processInstance.firstTask"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">共计</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.count" name="count" ng-disabled="!processInstance.firstTask"/>
                            </div>
                            <label class="col-md-2 control-label ">备注</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" ng-disabled="!processInstance.firstTask"/>
                            </div>
                        </div>
<%--                        <div class="form-group">
                            <label class="col-md-2 control-label required">申请单位负责人审批</label>
                            <div class="col-md-10">
                                <textarea rows="3" class="form-control" ng-model="vm.item.applicantComment" required="true" name="applicantComment" placeholder="" ng-disabled="user.userLogin.indexOf(processInstance.assignee)<0"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">借用人接收</label>
                            <div class="col-md-10">
                                <textarea rows="3" class="form-control" ng-model="vm.item.borrowerComment" required="true" name="borrowerComment" placeholder="" ng-disabled="user.userLogin.indexOf(processInstance.assignee)<0"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-2 control-label required">档案管理人员移交</label>
                            <div class="col-md-10">
                                <textarea rows="3" class="form-control" ng-model="vm.item.fileTransferComment" required="true" name="fileTransferComment" placeholder="" ng-disabled="user.userLogin.indexOf(processInstance.assignee)<0"></textarea>
                            </div>
                        </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">借用人归还</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.borrowerReturn" required="true" name="borrowerReturn" placeholder="" ng-disabled="user.userLogin.indexOf(processInstance.assignee)<0"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">档案管理人员接收</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control" ng-model="vm.item.fileTransferRecieve" required="true" name="fileTransferRecieve" placeholder="" ng-disabled="user.userLogin.indexOf(processInstance.assignee)<0"></textarea>
                                </div>
                            </div>--%>
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
                <div class="tab-pane" id="tab_detail_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 资料采购</div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addDetail();"
               ng-show="processInstance.firstTask">
                <i class="fa fa-cog"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: {{contentHeight-250}}px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th>文件资料名称</th>
                    <th>档号</th>
                    <th>档案类型</th>
                    <th>专业</th>
                    <th>归档单位</th>
                    <th>密级</th>
                    <th>底稿</th>
                    <th>蓝图</th>
                    <th>文书</th>
                    <th>DWG</th>
                    <th>PDF</th>
                    <th>件数</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.details">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.fileName" class="data_title" ng-click="vm.showDetailModel(detail.id);"></td>
                    <td ng-bind="detail.fileNo"></td>
                    <td ng-bind="detail.fileType"></td>
                    <td ng-bind="detail.major"></td>
                    <td ng-bind="detail.deptName"></td>
                    <td ng-bind="detail.fileLevel"></td>
                    <td >{{detail.draft?'√':''}}</td>
                    <td >{{detail.blueprint?'√':''}}</td>
                    <td >{{detail.word?'√':''}}</td>
                    <td >{{detail.dwg?'√':''}}</td>
                    <td >{{detail.pdf?'√':''}}</td>
                    <td ng-bind="detail.count"></td>

                    <td >
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showDetailModel(detail.id);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetail(detail.id);" title="删除" ng-show="processInstance.firstTask"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<div class="modal fade" id="detailModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">档案资料借阅电子复制申请表</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" id="detailForm">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-4 control-label">文件资料名称</label>
                            <div class="col-md-7">
                                <input type="text" class="form-control" ng-model="vm.detail.fileName" name="fileName" required="true" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">档号</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.fileNo" name="fileNo" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">档案类型</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.fileType" name="fileType" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">专业</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.major" name="major" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">归档单位</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.deptName" name="deptName" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">密级</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.fileLevel" name="fileLevel" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">底稿（硫酸纸）</label>
                        <div class="col-md-7">
                            <input type="checkbox"   ng-model="vm.detail.draft" ng-checked="vm.detail.draft=='1'"  style="width: 20px;height: 20px;" ng-disabled="!processInstance.firstTask" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">蓝图</label>
                        <div class="col-md-7">
                            <input type="checkbox"   ng-model="vm.detail.blueprint" ng-checked="vm.detail.blueprint=='1'"  style="width: 20px;height: 20px;"  ng-disabled="!processInstance.firstTask"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">文书档案</label>
                        <div class="col-md-7">
                            <input type="checkbox"   ng-model="vm.detail.word" ng-checked="vm.detail.word=='1'"  style="width: 20px;height: 20px;" ng-disabled="!processInstance.firstTask"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">电子档案管理</label>
                        <div class="col-md-7">
                            <span>DWG</span>
                            <input type="checkbox"   ng-model="vm.detail.dwg" ng-checked="vm.detail.dwg=='1'"  style="width: 20px;height: 20px;" ng-disabled="!processInstance.firstTask"/>
                            <span>PDF</span>
                            <input type="checkbox"   ng-model="vm.detail.pdf" ng-checked="vm.detail.pdf=='1'"  style="width: 20px;height: 20px;"ng-disabled="!processInstance.firstTask" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">件数</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.count" name="count" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-4 control-label">备注</label>
                        <div class="col-md-7">
                            <input type="text" class="form-control" ng-model="vm.detail.remark" name="remark" required="true" ng-disabled="!processInstance.firstTask">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();"  ng-show="processInstance.firstTask">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
