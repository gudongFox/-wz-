<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 勘察输入清单及评审记录
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <jsp:include page="../common/common-actAction.jsp"/>
            <a class="btn btn-sm btn-default" ng-href="{{'/explore/input/exportData.json?id='+vm.inputId}}" target="_blank">
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
                     style="height: 300px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label required">机号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.machineCode" name="machineCode" required="true" ng-disabled="!processInstance.firstTask"
                                           />
                                </div>
                                <label class="col-md-2 control-label required">所用软件</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.softwareName" name="softwareName" required="true" ng-disabled="!processInstance.firstTask"
                                           />
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
                    <p style="color: red;margin-left: 50px">* 请发送该流程前 确认后续ios资料表单已上传</p>
                </div>
                <div class="tab-pane" id="tab_15_2"
                     style="height: 250px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 250px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>




<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <span> <i class="fa fa-file"></i> 勘察输入清单</span>
        </div>
        <a href="javascript:;" style="float: right;margin-right: 10px" class="btn btn-sm btn-default" ng-click="vm.addDetailSatisfy();" ng-show="processInstance.firstTask">
            <i class="fa fa-plus" ></i> 创建 </a>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="width: 150px">文件类型</th>
                    <th>文件描述</th>
                    <th style="width: 80px;">是否满足</th>
                    <th style="width: 60px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detailSatisfy in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detailSatisfy.contentCategory"></td>
                    <td ng-bind="detailSatisfy.contentDesc" style="color: blue" ng-click="vm.edit(detailSatisfy.id)"></td>
                    <td ng-if="detailSatisfy.satisfy==true">满足</td>
                    <td ng-if="detailSatisfy.satisfy==false">不满足</td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.edit(detailSatisfy.id);"
                           title="详情"></i>
                        <i class="fa fa-trash-o" ng-click="vm.removeDetailSatisfy(detailSatisfy.id);"
                           title="删除" ng-show="processInstance.firstTask"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <span> <i class="fa fa-file"></i> 勘察输入评审记录</span>
        </div>
    </div>
    <div class="portlet-body">
        <form class="form-horizontal">
            <%--勘察输入评审记录--%>
            <div class="form-group">
                <label class="col-md-2 control-label">参与评审人员</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" ng-model="vm.item.attendUser" name="attendUser" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                </div>
                <label class="col-md-2 control-label">主持人</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" ng-model="vm.item.compereUser" name="compereUser" required="true" maxlength="50" ng-disabled="!processInstance.firstTask"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label">勘察资料及条件是否齐全</label>
                <div class="col-md-4">
                    <select ng-model="vm.item.docResult" name="docResult" class="form-control" ng-disabled="!processInstance.firstTask">
                        <option value="齐全" >齐全</option>
                        <option value="不够齐全" >不够齐全</option>
                    </select>
                </div>
                <label class="col-md-2 control-label">勘察要求是否正确</label>
                <div class="col-md-4">
                    <select ng-model="vm.item.requestResult" name="requestResult" class="form-control" ng-disabled="!processInstance.firstTask">
                        <option value="正确" >正确</option>
                        <option value="不够正确" >不够正确</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label">勘察输入的完整型</label>
                <div class="col-md-4">
                    <select ng-model="vm.item.inputResult" name="inputResult" class="form-control" ng-disabled="!processInstance.firstTask">
                        <option value="完整" >完整</option>
                        <option value="不够完整" >不够完整</option>
                    </select>
                </div>
                <label class="col-md-2 control-label">其他</label>
                <div class="col-md-4">
                    <input type="text" class="form-control" ng-model="vm.item.otherResult" name="otherResult" required="true" maxlength="20" ng-disabled="!processInstance.firstTask"/>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-2 control-label">针对勘察输入存在的问题的应对措施</label>
                <div class="col-md-10">

                                    <textarea id="maxlength_textarea" ng-model="vm.item.solveSuggestion" name="solveSuggestion" required="true" class="form-control" maxlength="225" rows="4" placeholder="" ng-disabled="!processInstance.firstTask">
                                </textarea>
                </div>
            </div>
        </form>
    </div>
</div>

<div class="modal fade" id="detailSatisfy" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">勘察输入清单</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="Satisfy_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">文件类型</label>
                            <div class="col-md-9">
                                <select ng-model="vm.detailSatisfy.contentCategory" name="contentCategory"  class="form-control" readonly>
                                    <option value="勘察输入清单">勘察输入清单</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">文件描述</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detailSatisfy.contentDesc"  required="true"
                                       maxlength="200" name="contentDesc" placeholder="" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">文件是否满足</label>
                            <div class="col-md-9">
                                <select ng-model="vm.detailSatisfy.satisfy" name="satisfy"  class="form-control" ng-disabled="!processInstance.firstTask">
                                    <option ng-selected="vm.detailSatisfy.satisfy==true" value="true">满足</option>
                                    <option ng-selected="vm.detailSatisfy.satisfy==false"  value="false">不满足</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.detailSatisfy.remark" ng-disabled="!processInstance.firstTask"
                                       maxlength="200"  placeholder="">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateDetailSatisfy();" ng-show="processInstance.firstTask">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<jsp:include page="../common/common-edFile.jsp"/>

<jsp:include page="./print/print-inputDetail.jsp"/>


