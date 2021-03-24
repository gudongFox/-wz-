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
            <span >设计回访</span>
        </li>
    </ul>
</div>

<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-compass"></i> 设计回访
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/five/returnVisit/exportData.json?id='+vm.item.id}}"
               target="_blank">
                <i class="fa fa-file-word-o"></i> 导出word</a>
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
                     style="min-height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label ">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">项目编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label">项目阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stageName" disabled/>
                                </div>
                                <label class="col-md-2 control-label">期次名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stepName" disabled/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">建设单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionOrg" required="true" name="constructionOrg" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">建设地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="建设地址"
                                           ng-model="vm.item.constructionAddress"   name="constructionAddress" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">受访人（单位）</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.ownerName" required="true" name="ownerName"ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">回访人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.visitor" required="true" name="visitor"ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">回访日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="visitTime">
                                        <input type="text"  class="form-control" required="true" name="visitTime"
                                               ng-disabled="!processInstance.firstTask"
                                               ng-model="vm.item.visitTime" placeholder="回访日期">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
												</span>
                                    </div>

                                </div>
                                <label class="col-md-2 control-label required">设计完成日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="designFinishTime">
                                        <input type="text"  class="form-control" required="true" name="designFinishTime" ng-disabled="!processInstance.firstTask"
                                               ng-model="vm.item.designFinishTime" placeholder="设计完成日期">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
												</span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label">项目竣工日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="constructionFinishTime">
                                        <input type="text"  class="form-control"  name="constructionFinishTime"
                                               ng-model="vm.item.constructionFinishTime" placeholder="竣工日期" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
												</span>
                                    </div>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark"  name="remark" maxlength="400" ng-disabled="!processInstance.firstTask"/>
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
                <div class="tab-pane" id="tab_15_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <div ng-include="'/web/v1/tpl/actTaskList.html'"></div>
                </div>
                <div class="tab-pane" id="tab_15_3"
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
            <i class="fa fa-file"></i> 实施
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label required">
                        <strong>回访形式</strong>
                    </label>
                    <div class="col-md-4">
                        <input type="text" class="form-control" ng-model="vm.item.returnType" required="true" name="returnType"ng-disabled="!processInstance.firstTask"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">提出问题</label>
                    <div class="col-md-10">
                        <textarea rows="3" class="form-control" ng-model="vm.item.raiseQuestion" required="true" name="raiseQuestion" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">措施</label>
                    <div class="col-md-10">
                        <textarea rows="3" class="form-control" ng-model="vm.item.measure" required="true" name="measure" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                    </div>
                </div>

            </div>
        </form>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 意见反馈
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal" role="form">
            <div class="form-body">
                <div class="form-group">
                    <label class="col-md-2 control-label required">业主建议和意见</label>
                    <div class="col-md-10">
                        <textarea rows="2" class="form-control" ng-model="vm.item.ownerComment" required="true" name="exceptionDesc" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-md-2 control-label required">设计单位处理意见</label>
                    <div class="col-md-10">
                        <textarea rows="2" class="form-control" ng-model="vm.item.designSolve" required="true" name="designSolve" placeholder=""ng-disabled="!processInstance.firstTask"></textarea>
                    </div>
                </div>
            </div>
        </form>    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 回访内容
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th style="width: 300px">内容</th>
                    <th>满意度</th>
                    <th style="width: 80px;" ng-show="processInstance.firstTask">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.visitDetails">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.appraiseType"></td>
                    <td ng-bind="detail.appraiseResult"></td>
                    <td ng-show="processInstance.firstTask">
                        <i class="fa fa-edit margin-right-10" ng-click="vm.showDetail(detail);"
                           title="详情"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>


<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>


<jsp:include page="../print/print-returnVisitDetail.jsp"/>