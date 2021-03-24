<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-star"></i> 设计文件图纸验收清单
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
                     style="height: 310px;overflow-y: auto;overflow-x: hidden;">
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
                                <label class="col-md-2 control-label">期次名称</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.stepName" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">验收单号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.formNo" name="formNo" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">所属部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">验收人</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.userName" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">验收日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="productDate">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.productDate" name="productDate" required="true" ng-disabled="!processInstance.firstTask">
                                        <span class="input-group-btn">
												<button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar" ></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.userTel" ng-disabled="!processInstance.firstTask"/>
                                </div><label class="col-md-2 control-label">备注</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" ng-disabled="!processInstance.firstTask"/>
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
                     style="height: 310px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_15_3"
                     style="height: 310px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actDiagram.jsp"/>
                </div>
            </div>

        </div>
    </div>
</div>
<%--子表详情--%>
<div class="portlet light">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-file"></i>验收清单详情
        </div>
        <div class="actions" ng-show="processInstance.firstTask">
            <a  class="btn btn-sm default fileinput-button"
                ng-click="vm.addDetail();">
                <i class="fa fa-plus"></i> 新增</a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="table-scrollable" style="max-height: 500px;overflow-y: auto;">
            <table class="table table-striped table-hover table-bordered table-advance no-footer">
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th style="width:80px ">专业</th>
                    <th>图号</th>
                    <th  style="width:30px ">修改图</th>
                    <th  style="width:90px ">建筑面积</th>
                    <th style="width:60px ">外文页数</th>
                    <th style="width:90px ">中文页数</th>
                    <th style="width:90px ">中文A1</th>
                    <th style="width:90px ">图纸张数</th>
                    <th style="width:90px ">图纸A1</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.majorName" ng-click="vm.editDetail(detail.id);" style="color: blue;"></td>
                    <td ng-bind="detail.drawNo"></td>
                    <td ng-bind="detail.remark1"></td>
                    <td ng-bind="detail.constructionArea"></td>
                    <td ng-bind="detail.remark2"></td>
                    <td ng-bind="detail.remark3"></td>
                    <td ng-bind="detail.remark4"></td>
                    <td ng-bind="detail.copyCount"></td>
                    <td ng-bind="detail.remark5"></td>
                    <td>
                        <i class="fa fa-edit margin-right-5" ng-click="vm.editDetail(detail.id);"
                           title="详情"></i>
                        <i class="fa fa-trash"  ng-click="vm.removeDetail(detail.id);" title="删除" ng-show="processInstance.firstTask"></i>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="detailModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title margin-right-10">验收清单详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="PointDetail_form">
                    <div class="form-group">
                        <label class="col-md-3 control-label required">专业</label>
                        <div class="col-md-9">
                            <select ng-options="s.majorName as s.majorName for s in vm.arrangeUsers "
                                    ng-model="vm.productDetail.majorName" class="form-control"></select>
                      </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">图号</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.productDetail.drawNo"  required="true"
                                    name="drawNo" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">修改图</label>
                        <div class="col-md-9">
                           <select ng-model="vm.productDetail.remark1" class="form-control">
                               <option value="是">是</option>
                               <option value="否">否</option>
                           </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">建筑面积</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.productDetail.constructionArea"  required="true"
                                  name="constructionArea" placeholder="" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">外文页数</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.productDetail.remark2"  required="true"
                                 name="remark2" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">中文页数</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.productDetail.remark3"  required="true"
                                    name="remark3" placeholder="" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">中文A1</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.productDetail.remark4"  required="true"
                                   maxlength="100" name="remark4" placeholder="" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">图纸张数</label>
                        <div class="col-md-9">
                            <input type="number" class="form-control" ng-model="vm.productDetail.copyCount"  required="true"
                                    name="copyCount" placeholder="" >
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">图纸A1</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.productDetail.remark5"  required="true"
                                   name="remark5" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">设计单位</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.productDetail.deptName"  required="true"
                                    name="deptName" placeholder="" required="true">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">排序</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.productDetail.seq"  required="true"
                                    name="seq" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">备注</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.productDetail.remark7"
                                   maxlength="200" name="remark7" placeholder="">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.updateDetail();" ng-show="processInstance.firstTask">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<jsp:include page="../common/common-edFile.jsp"/>





