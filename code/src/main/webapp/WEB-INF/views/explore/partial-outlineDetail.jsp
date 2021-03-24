<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 勘察纲要评审记录
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actActionNoFlows.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/outline/exportData.json?id='+vm.outlineId}}" target="_blank">
                <i class="fa fa-file-word-o"></i> 导出</a>
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
                                <label class="col-md-2 control-label">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.contractNo"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label">项目阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stageName" disabled/>
                                </div>
                                <label class="col-md-2 control-label">分期名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stepName" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目负责人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.exploreCharge" name="exploreCharge" required="true" maxlength="20" disabled/>
                                </div>
                                <label class="col-md-2 control-label required">工程号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.projectNo" name="projectNo" required="true" maxlength="30" disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">评审主持人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.compereUser" name="compereUser" required="true" maxlength="50" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label">勘察文件</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'勘察文件'}:true"
                                            ng-model="vm.item.exploreStage" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">评审日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="checkTime">
                                        <input type="text" class="form-control"  name="checkTime" required="true"
                                               ng-model="vm.item.checkTime" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>

                                <label class="col-md-2 control-label required">参加人员名单</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.attendUser" name="attendUser" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">甲方提供的勘察基础资料<br/>及其条件是否齐全</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.resultDocSatisfy" name="resultDocSatisfy" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="齐全" >齐全</option>
                                        <option value="不够齐全" >不够齐全</option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label">甲方提供的勘察基础资料<br/>及其条件是否齐全</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.resultWorkloadWell" name="resultWorkloadWell" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="合理" >合理</option>
                                        <option value="不够合理" >不够合理</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">纲要是否符合<br/>勘察主规范要求</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.resultOutlineSatisfy" name="resultOutlineSatisfy" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="符合" >符合</option>
                                        <option value="不符合" >不符合</option>
                                    </select>
                                </div>
                                <label class="col-md-2 control-label">野外勘察是否满足<br/>本阶段要求</label>
                                <div class="col-md-4">
                                    <select ng-model="vm.item.resultOutSatisfy" name="resultOutSatisfy" class="form-control" ng-disabled="!processInstance.firstTask">
                                        <option value="通过">通过</option>
                                        <option value="需修改完善">需修改完善</option>
                                        <option value="不能通过">不能通过 </option>
                                    </select>
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
                                           value="{{vm.item.gmtCreate|date:'yyyy-MM-dd HH:mm'}}" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i> 勘察纲要评审记录
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 50px;">序号</th>
                    <th style="width: 150px">文件类别</th>
                    <th>文件描述</th>
                    <th style="width: 80px;">是否满足</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detailSatisfy in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detailSatisfy.contentCategory"></td>
                    <td ng-bind="detailSatisfy.contentDesc"></td>
                    <td ng-if="detailSatisfy.satisfy==true">满足</td>
                    <td ng-if="detailSatisfy.satisfy==false">不满足</td>
                    <td>
                        <i class="fa fa-edit margin-right-10" ng-click="vm.edit(detailSatisfy.id);"
                           title="详情"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="detailSatisfy" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">勘察评审记录资料</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="Satisfy_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">文件类型</label>
                            <div class="col-md-9">
                                <select ng-model="vm.detailSatisfy.contentCategory" name="contentCategory"  class="form-control" readonly>
                                    <option value="评审资料及文件">评审资料及文件</option>
                                    <option value="评审内容及要求">评审内容及要求</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">文件描述</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detailSatisfy.contentDesc" readonly required="true"
                                       maxlength="200" name="contentDesc" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">是否满足</label>
                            <div class="col-md-9">
                                <select ng-model="vm.detailSatisfy.satisfy" name="satisfy"  class="form-control" >
                                    <option ng-selected="vm.detailSatisfy.satisfy==true" value="true">满足</option>
                                    <option ng-selected=" vm.detailSatisfy.satisfy==false"  value="false">不满足</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detailSatisfy.remark"
                                       maxlength="200"  placeholder="">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateDetailSatisfy();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<jsp:include page="../common/common-edFile.jsp"/>
<jsp:include page="./print/print-outlineDetail.jsp"/>


