<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-clock"></i> 设计图纸验收清单
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
            </ul>
            <div class="tab-content">
                <div class="tab-pane active" id="tab_15_1"
                     style="min-height: 360px;overflow-y: auto;overflow-x: hidden;">
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

                                <label class="col-md-2 control-label">专业</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="编号" ng-model="vm.item.majorName"
                                           disabled="disabled"/>
                                </div>
                                <label class="col-md-2 control-label">阶段</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="编号" ng-model="vm.item.stageName"
                                           disabled="disabled"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="checkTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.checkTime" name="checkTime" required="true" disabled>
                                        <span class="input-group-btn">
												<button class="btn default" type="button" disabled><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label">验收单号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.formNo" name="formNo" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">交验人</label>
                                <div class="col-md-4">
                                    <input type="text"   class="form-control" ng-model="vm.item.applyManName" name="applyManName" required="true" maxlength="20" disabled/>
                                </div>
                                <label class="col-md-2 control-label">电话</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.applyPhone" name="applyPhone" required="true" maxlength="20" disabled/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">验收人</label>
                                <div class="col-md-4">
                                    <input type="text"   class="form-control" ng-model="vm.item.handMan" name="handMan" required="true" maxlength="20" disabled/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark"  disabled/>
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
            <table class="table table-striped table-hover table-bordered table-advance no-footer " >
                <thead>
                <tr>
                    <th style="width: 35px;">#</th>
                    <th>子项名称</th>
                    <th style="width: 60px">专业</th>
                    <th>图号</th>
                    <th style="width: 60px">密级</th>
                    <th style="width: 60px">修改图</th>
                    <th>建筑面积</th>
                    <th style="width: 60px">外文页数</th>
                    <th style="width: 60px">中文页数</th>
                    <th style="width: 60px">中文A1</th>
                    <th style="width: 60px">图纸张数</th>
                    <th style="width: 60px">复制份数</th>
                    <th style="width: 60px">图纸A1</th>
                    <th>设计单位</th>

                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.listDetail">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.buildName" ng-click="vm.showDetail(detail.id);" style="color: blue;"></td>
                    <td ng-bind="detail.majorName"></td>
                    <td ng-bind="detail.drawNo"></td>
                    <td ng-bind="detail.secretLevel"></td>
                    <td >
                        <span ng-if="detail.change">是</span>
                        <span ng-if="!detail.change">否</span>
                    </td>
                    <td ng-bind="detail.buildArea"></td>
                    <td ng-bind="detail.foreignPage"></td>
                    <td ng-bind="detail.inlandPage"></td>
                    <td ng-bind="detail.inlandA1Page"></td>
                    <td ng-bind="detail.drawNumber"></td>
                    <td ng-bind="detail.copyNumber"></td>
                    <td ng-bind="detail.drawA1Number"></td>
                    <td ng-bind="detail.deptName"></td>
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
                        <label class="col-md-3 control-label">子项名称</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.buildName" disabled required="true" disabled
                                   name="buildName" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">专业</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.majorName"  required="true" disabled
                                   name="majorName" placeholder="">
                           
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">图号</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.drawNo"  required="true" disabled
                                   name="drawNo" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">密级</label>
                        <div class="col-md-9">
                            <select ng-options="s.name as s.name for s in sysCodes | filter:{codeCatalog:'项目密级'}:true"
                                    ng-model="vm.detail.secretLevel" class="form-control"
                                    disabled   ></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">修改图</label>
                        <div class="col-md-9">
                            <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:'是否'}:true"
                                    ng-model="vm.detail.change" class="form-control"
                                    disabled   ></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">建筑面积</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.buildArea"   required="true" disabled
                                   name="buildArea" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">外文页数</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.foreignPage"  required="true" disabled
                                   name="foreignPage" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">中文页数</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.inlandPage"  required="true" disabled
                                   name="inlandPage" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">中文A1</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.inlandA1Page"  required="true" disabled
                                   name="inlandA1Page" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">图纸张数</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.drawNumber"  required="true" disabled
                                   name="drawNumber" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">复制份数</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.copyNumber"  required="true" disabled
                                   name="copyNumber" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label">图纸A1</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.drawA1Number"  required="true" disabled
                                   name="drawA1Number" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 control-label required">设计单位</label>
                        <div class="col-md-9">
                            <div class="input-group">
                                <input type="text" class="form-control" ng-model="vm.detail.deptName" name="deptName"   readonly/>
                                <span class="input-group-btn" >
                                        <button class="btn default" type="button" ng-click="vm.showDeptModal();" disabled><i class="fa fa-cog"></i></button>
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 control-label">备注
                        </label>
                        <div class="col-md-9">
                            <input type="text" class="form-control" ng-model="vm.detail.remark"  required="true"  disabled
                                   name="remark" placeholder="">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn default" ng-click="" data-dismiss="modal">关闭</button>
                <button type="button" class="btn blue" ng-click="vm.saveDetail();" ng-show="processInstance.firstTask">保存</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

<div ng-include="'/web/v1/tpl/cloudDirAndFile.html'"  ng-init="fileTplTitle='业务附件'"></div>

<jsp:include page="print/print-majorDrawingCheckl.jsp"/>





