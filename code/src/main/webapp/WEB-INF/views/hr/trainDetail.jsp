
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="page-bar" id="pageBar">
    <ul class="page-breadcrumb">
        <li>
            <i class="fa fa-home"></i>
            <a ui-sref="five.home">首页</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <a ui-sref="oa.functionEntrance({moduleId:49})">人资管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span>培训批次管理</span>
        </li>
    </ul>
</div>
<div class="portlet box blue" ng-repeat="group in vm.groupList" ng-if="$index==0&&processInstanceId">
    <div class="portlet-title">
        <div class="caption">
            <span ng-include="'/web/v1/tpl/actPortletCaption.html'"></span>
        </div>
        <div class="actions">
            <span>
                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                    <i class="fa fa-refresh"></i> 刷新 </a>

                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();"
                   ng-show="tplConfig.saveAble">
                    <i class="fa fa-save"></i> 保存 </a>

                <span ng-include="'/web/v1/tpl/actAction.html'"></span>

                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.back();">
                    <i class="fa fa-arrow-left"></i> 返回 </a>

<%--                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.checkForm();">--%>
<%--                    <i class="fa fa-arrow-left"></i> 检查 </a>--%>

            </span>
        </div>


    </div>
    <div class="portlet-body">
        <div class="tabbable-line">
            <ul class="nav nav-tabs ">
                <li class="active">
                    <a href="#tab_basic" data-toggle="tab" aria-expanded="true" ng-bind="group.groupName">
                        基础信息 </a>
                </li>
                <li class="">
                    <a href="#tab_task" data-toggle="tab" aria-expanded="false">
                        流程信息
                    </a>
                </li>
                <li class="">
                    <a href="#tab_diagram" data-toggle="tab" aria-expanded="false">
                        流程图片 </a>
                </li>
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_basic">
                    <div ng-include="'/web/v1/tpl/formInfo.html'"></div>
                </div>
                <div class="tab-pane" id="tab_task">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_diagram">
                    <div ng-include="'/web/v1/tpl/actDiagram.html'"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="portlet box blue" ng-repeat="group in vm.groupList" ng-if="!processInstanceId||$index>0">
    <div class="portlet-title">
        <div class="caption" ng-show="$index>0">
            <i class="icon-note"></i><span ng-bind="($index+1)+'.'+group.groupName"></span>
        </div>
        <div class="caption" ng-show="$index===0">
            <i class="{{vm.template.formIcon}}"></i>  <span class="caption-subject" ng-bind="vm.template.formDesc"></span>
        </div>

        <div class="actions" ng-show="$index===0">
            <span>
                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData();">
                    <i class="fa fa-refresh"></i> 刷新 </a>

                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();"
                   ng-show="tplConfig.saveAble">
                    <i class="fa fa-save"></i> 保存 </a>

                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.back();">
                    <i class="fa fa-arrow-left"></i> 返回 </a>

            </span>
        </div>
    </div>
    <div class="portlet-body">
        <div ng-include="'/web/v1/tpl/formInfo.html'"></div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title" >
        <div class="caption">
            <i class="fa fa-file"></i> 培训参与人员<span ng-bind="' ('+vm.hrTrainMembers.length+')'"></span>
        </div>
        <div class="actions">
            <a id="addBtn"  class="btn btn-sm btn-default" ng-click="vm.selectHrTrainMember();"  ng-if="tplConfig.saveAble">
                <i class="fa fa-plus"></i> 新增 </a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: 450px;overflow-y: auto;overflow-x: hidden;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">#</th>
                    <th style="width: 150px;">工号</th>
                    <th style="width: 200px;">人员名称</th>
                    <th style="width: 200px;">培训结果</th>
                    <th style="width: 200px;">学时</th>
                    <th style="width: 200px;">取得学分</th>
                    <th>备注</th>
                    <th style="width: 60px;" >操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.hrTrainMembers">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.userLogin" class="data_title" ng-click="vm.showHrMember(detail);"></td>
                    <td ng-bind="detail.userLoginName"></td>
                    <td ng-bind="detail.trainResult"></td>
                    <td ng-bind="detail.trainTime"></td>
                    <td ng-bind="detail.trainPoint"></td>
                    <td ng-bind="detail.remark"></td>
                    <td>
                        <i class="fa fa-pencil margin-right-5" ng-click="vm.showHrMember(detail);" title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeHrMember(detail.id);" title="删除" ng-if="tplConfig.saveAble"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>


<div class="modal fade draggable-modal" id="hrMemberModal" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title">培训参与人员详情</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">工号</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.hrMember.userLogin" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">人员名称</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.hrMember.userLoginName" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">培训结果</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'培训结果'}:true"
                                        ng-model="vm.hrMember.trainResult"
                                        class="form-control"></select>

                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">学时</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.hrMember.trainTime">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">取得学分</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.hrMember.trainPoint">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.hrMember.remark">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">创建人</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.hrMember.creatorName" disabled="disabled"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">创建时间</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control"
                                       value="{{vm.hrMember.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveHrMember();" ng-if="tplConfig.saveAble">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='相关附件'"></div>
