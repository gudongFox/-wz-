<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 设计人员与计划安表
           <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();"
               ng-show="processInstance.passAble">
                <i class="fa fa-save"></i> 保存 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();">
                <i class="fa fa-print"></i> 打印 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.passAble"
               ng-click="vm.showSendFlow();">
                <i class="fa fa-send"></i> 发送 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-show="processInstance.backAble"
               ng-click="showBackFlow();">
                <i class="fa fa-reply"></i> 打回 </a>

            <a href="javascript:;" class="btn btn-sm btn-default"
               ng-click="back();">
                <i class="fa fa-arrow-left"></i> 返回 </a>
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
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label">编号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="编号" ng-model="vm.item.formNo"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">项目名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">建设单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionOrg" name="constructionOrg" required="true" maxlength="30" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label">工程号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">工程地址</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionAddress" name="constructionAddress" required="true" maxlength="50" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label">工程规模</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionScale" name="constructionScale" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">投资规模</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.investScale" name="investScale" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>

                                <label class="col-md-2 control-label">质量要求</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'质量要求'}:true"
                                            ng-model="vm.item.constructionQuality" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">联系人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionLink" name="constructionLink" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label">联系电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionPhone" name="constructionPhone" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">初勘时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="firstSubmit">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.firstSubmit" name="firstSubmit" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">详勘时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="detailSubmit">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.detailSubmit"  name="detailSubmit" required="true" name="detailSubmit" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">

                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" maxlength="100" ng-disabled="!processInstance.firstTask"/>
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
                                           value="{{vm.item.gmtModified|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="part-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="min-height: 420px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="part-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>

<jsp:include page="../ed/project/part-actFile.jsp"/>





