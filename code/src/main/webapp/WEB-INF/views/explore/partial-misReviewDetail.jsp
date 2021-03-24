<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 一体化管理体系内部审核检查及记录表
            <small style="font-size: 50%;" ng-bind="processInstance.processName"></small>
        </div>
        <div class="actions">
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.loadData(true);">
                <i class="fa fa-refresh"></i> 刷新 </a>

            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.save();">
                <i class="fa fa-save"></i> 保存 </a>
            <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.print();">
                <i class="fa fa-print"></i> 打印 </a>

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
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 220px;overflow-y: auto;overflow-x: hidden;">
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
                                    <input type="text" class="form-control" placeholder ng-model="vm.item.projectName"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">受审核部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  ng-model="vm.item.checkDepartment"/>
                                </div>
                                <label class="col-md-2 control-label">负责人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.checkCharge"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">内审员</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.reviewUser"/>
                                </div>
                                <label class="col-md-2 control-label">受审日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="reviewDate">
                                        <input type="text" class="form-control" name="reviewDate"
                                               ng-model="vm.item.reviewDate" required="true" >
                                        <span class="input-group-btn">
												<button class="btn default" type="button"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" maxlength="100" />
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
                    <div class="portlet light">
                        <div class="portlet-title">
                            <div class="caption">
                                <i class="fa fa-file"></i> 审核检查及记录
                            </div>
                            <div class="actions">
                                <a href="javascript:;" class="btn btn-sm btn-default" ng-click="vm.addDetail();">
                                    <i class="fa fa-save"></i> 添加 </a>
                            </div>
                        </div>
                        <div class="portlet-body">
                            <div class="table-scrollable">
                                <table class="table table-striped table-hover table-bordered table-advance no-footer" id="pass">
                                    <thead>
                                    <tr>
                                        <th style="width: 100px;">序号</th>
                                        <th>手册条款</th>
                                        <th>标准条款</th>
                                        <th>检查要点</th>
                                        <th>审核客观记录</th>
                                        <th style="width: 100px;">是否符合</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="misReviewDetail in vm.misReviewDetails">
                                        <td class="dt-right" ng-bind="$index+1"></td>
                                        <td ng-bind="misReviewDetail.bookRule"></td>
                                        <td ng-bind="misReviewDetail.standardRule"></td>
                                        <td ng-bind="misReviewDetail.checkPoint"></td>
                                        <td ng-bind="misReviewDetail.checkResult"></td>
                                        <td>
                                            <i class="fa fa-pencil margin-right-10" ng-click="vm.showDetail(misReviewDetail.id);" title="详情"></i>
                                            <i class="fa fa-trash-o margin-right-10" ng-click="vm.removeDetail(misReviewDetail.id);" title="删除">
                                            </i>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            </div>
        </div>
    </div>
</div>



<div class="modal fade" id="misReviewDetail" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">审核检查及记录</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="misReview_form">
                    <div class="form-body">
                        <div class="form-group">
                            <label class="col-md-3 control-label">手册条款</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.misReviewDetail.bookRule" required="true">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 control-label">标准条款</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.misReviewDetail.standardRule" required="true"
                                       maxlength="200" name="standardRule" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">检查要点</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.misReviewDetail.checkPoint" required="true"
                                       maxlength="200" name="checkPoint" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">审核客观记录</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.misReviewDetail.checkResult"  required="true"
                                       maxlength="200" name="checkResult" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">排序</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.misReviewDetail.seq"  required="true"
                                       maxlength="200" name="seq" placeholder="">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<jsp:include page="./print/print-misReviewDetail.jsp"/>
