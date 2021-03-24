    <%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">技术质量</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="">工程档案借阅</a>
            <i class="fa fa-angle-right"></i>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i> 工程档案借阅
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction1.jsp"/>
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
                     style="min-height: 300px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">

                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请项目</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName"
                                               disabled="disabled" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.listArchive();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                            </div>
                            <label class="col-md-2 control-label">申请部门</label>
                            <div class="col-md-4">
                                <input type="text" class="form-control" ng-model="vm.item.deptName"  name="deptName" ng-disabled="!processInstance.firstTask"/>
                            </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">开始时间</label>
                                <div class="col-md-4">
                                <div class="input-group date date-picker" id="startTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.startTime" name="startTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                    <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                                </div>
                                <label class="col-md-2 control-label required">结束时间</label>
                                <div class="col-md-4">
                                <div class="input-group date date-picker" id="endTime">
                                    <input type="text" class="form-control"
                                           ng-model="vm.item.endTime" name="endTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                    <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">申请用途</label>
                                <div class="col-md-10">
                                    <textarea rows="5" class="form-control ng-pristine ng-untouched ng-valid ng-empty ng-valid-maxlength ng-valid-required"
                                              ng-model="vm.item.applyPurpose" name="applyPurpose" ng-disabled="!processInstance.firstTask"
                                              required="true"></textarea>
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
                     style="height: 300px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 300px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="archiveModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" >工程档案</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="col-md-12">

                            <label style="margin-right: 6px">项目名称:
                                <input type="search" class="form-control input-inline input-sm ng-pristine ng-untouched ng-valid ng-empty" placeholder="项目名称"
                                       ng-model="vm.params.projectNames"/>
                            </label>


                    </div>
                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>项目名称</th>
                                <th style="width: 90px;">归档日期</th>
                                <th style="width: 120px;">所属部门</th>
                                <th style="width: 50px;">创建人</th>
                                <th style="width: 100px;">创建时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in  vm.pageInfo.list|filter:{projectName:vm.params.projectNames}">
                                <td>
                                    <input type="checkbox" class="cb_archive" ng-checked="item.id==vm.item.archiveId"  data-name="{{item}}" style="width: 20px;height: 20px;" />
                                </td>
                                <td ng-bind="item.projectName"></td>
                                <td ng-bind="item.archiveTime"></td>
                                <td ng-bind="item.deptName"></td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <my-pager data-page-info="vm.pageInfo" on-load="vm.listArchive()"></my-pager>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.selectArchive();">确认</button>
            </div>
        </div>
    </div>
</div>

<%--子表详情--%>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i>档案资料
        </div>
        <div class="actions" >
            <a  class="btn btn-sm default fileinput-button" ng-show="user.userLogin==processInstance.taskUsers"
                ng-click="vm.chooseDetail();">
                <i class="fa blue fa-save"></i> 保存</a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: 500px;overflow-y: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>文件名称</th>
                    <th>存放地址</th>
                    <th  style="width:60px ">文件类型</th>
                    <th  style="width:40px ">电子档案</th>
                    <th style="width:60px ">创建人</th>
                    <th style="width:90px ">创建时间</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.list">
                    <td>
                        <input type="checkbox" class="cb_detail" ng-checked="detail.selected"  data-name="{{detail.id}}" ng-disabled="user.userLogin.indexOf(processInstance.taskUsers)<0" style="width: 16px;height: 16px;" />
                    </td>
                    <td ng-bind="detail.fileName" ></td>
                    <td ng-bind="detail.realAddress"></td>
                    <td ng-bind="detail.fileType"></td>
                    <td>
                        <span ng-show="detail.online">是</span>
                        <span ng-show="!detail.online">否</span>
                    </td>
                    <td ng-bind="detail.creatorName"></td>
                    <td ng-bind="detail.gmtModified|date:'yyyy-MM-dd'"></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="../common/common-actFlow.jsp"/>




