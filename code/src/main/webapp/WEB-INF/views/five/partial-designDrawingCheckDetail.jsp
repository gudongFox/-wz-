<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 设计图纸资料交验
            <small ng-if="!processInstance.myRunningTaskName" ng-bind="processInstance.currentTaskName"></small>
            <small ng-if="processInstance.myRunningTaskName" ng-bind="processInstance.myRunningTaskName" style="color: #35e0e1;"></small>

        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
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
                                <label class="col-md-2 control-label">项目名称</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目编号(母项号)</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.parentNo" name="parentNo" disabled/>
                                </div>
                                <label class="col-md-2 control-label">阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="编号" ng-model="vm.item.stageName"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">子项名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.buildName" name="buildName" required="true" maxlength="30" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">项目分管院长</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="编号" ng-model="vm.item.projectChargeManName"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">交验日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="checkTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.checkTime" name="checkTime" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                              
                                <label class="col-md-2 control-label required">密级</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'项目密级'}:true"
                                            
                                            ng-model="vm.item.secretLevel" class="form-control"  required="true"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">发至份数</label>
                                <div class="col-md-4">
                                    <input type="number" min="0" step="1"  class="form-control" ng-model="vm.item.sendCopies" name="sendCopies" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">发至单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.sendDeptName" name="sendDeptName" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            
                            <span style="font-size: 14px;color: red">成品加工</span>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">活页</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.finishProduceLeaflet" name="finishProduceLeaflet" required ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">简装</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.finishProducePaperback" name="finishProducePaperback" required ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">国内精装</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.finishProduceInlandHardbound" name="finishProduceInlandHardbound" required ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">国外精装</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.finishProduceForeignHardbound" name="finishProduceForeignHardbound" required ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                            </div>
                           <%-- <span style="font-size: 14px;color: red">追加</span>
                            <div class="form-group">
                                <label class="col-md-2 control-label">活页</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.addProduceLeaflet" name="addProduceLeaflet" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">简装</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.addProducePaperback" name="addProducePaperback" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                
                                <label class="col-md-2 control-label">国内精装</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.addProduceInlandHardbound" name="addProduceInlandHardbound" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">国外精装</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.addProduceForeignHardbound" name="addProduceForeignHardbound" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                            </div>--%>
                            <span style="font-size: 14px;color: red">总师</span>
                            <div class="form-group">
                                <label class="col-md-2 control-label">活页</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.totalProduceLeaflet" name="totalProduceLeaflet" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">简装</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.totalProducePaperback" name="totalProducePaperback" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">国内精装</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.totalProduceInlandHardbound" name="totalProduceInlandHardbound" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">国外精装</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="number" min="0" step="1" class="form-control ng-pristine ng-untouched ng-valid ng-empty" ng-model="vm.item.totalProduceForeignHardbound" name="totalProduceForeignHardbound" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-addon">份</span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"  ng-disabled="!processInstance.firstTask"/>
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
<jsp:include page="../common/common-actFlow.jsp"/>

