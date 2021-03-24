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
            <a ui-sref="">工程档案管理</a>
            <i class="fa fa-angle-right"></i>
        </li>
        <li>
            <span ng-bind="vm.item.projectName"></span>
        </li>
    </ul>
</div>
<div class="portlet  box blue">
    <div class="portlet-title">
        <div class="caption">
            <i class="icon-envelope-letter"></i> 工程档案管理
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
                     style="min-height: 380px;overflow-y: auto;overflow-x: hidden;">

                    <form class="form-horizontal" role="form" id="detail_form">
                        <div class="form-body">
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目名称</label>
                                <div class="col-md-4">
                                    <div class="input-group">
                                        <input type="text" class="form-control" ng-model="vm.item.projectName"
                                               disabled="disabled" />
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button"  ng-click="vm.listAllSteps();"
                                                    ng-disabled="!processInstance.firstTask">
                                                <i class="fa fa-cog"></i>
                                            </button>
                                        </span>
                                    </div>
                                    <span class="help-block"  ng-show="processInstance.firstTask">请点击后方按钮选择</span>
                                </div>
                                <label class="col-md-2 control-label required">合同号</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" placeholder="合同号"
                                           ng-model="vm.item.contractNo" ng-disabled="!processInstance.firstTask" required/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">项目阶段</label>
                                <div class="col-md-4">
                                    <select ng-options="s.name as s.name for s in vm.stages "
                                            ng-model="vm.item.stageName" name="stateName" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                                <label class="col-md-2 control-label">所属部门</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.deptName" name="deptName" ng-disabled="!processInstance.firstTask"/>
                                </div>

                            </div>

                            <div class="form-group">
                                <label class="col-md-2 control-label required">设计时间</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="designTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.designTime" name="designTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                                <label class="col-md-2 control-label required">归档日期</label>
                                <div class="col-md-4">
                                    <div class="input-group date date-picker" id="archiveTime">
                                        <input type="text" class="form-control"
                                               ng-model="vm.item.archiveTime" name="archiveTime" required="true" ng-disabled="!processInstance.firstTask"  >
                                        <span class="input-group-btn">
                                            <button class="btn default" type="button" ng-disabled="!processInstance.firstTask"><i class="fa fa-calendar"></i></button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">工程规模(m2)</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" required="true" ng-model="vm.item.projectScale"  name="projectScale" ng-disabled="!processInstance.firstTask"/>
                                </div>
                                <label class="col-md-2 control-label required">工程性质</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control"  required="true" ng-model="vm.item.projectType" name="projectType" ng-disabled="!processInstance.firstTask"/>
                                </div>
                            </div>
                            <div class="form-group">

                                <label class="col-md-2 control-label required">建设单位</label>
                                <div class="col-md-4">
                                    <input type="text" class="form-control" ng-model="vm.item.constructionOrg"  required="true" name="constructionOrg" ng-disabled="!processInstance.firstTask"/>
                                </div>
                               <label class="col-md-2 control-label required">部门拥有</label>
                                <div class="col-md-4">
                                    <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('是否发布')}:true"
                                            ng-model="vm.item.deptOwn" class="form-control"
                                            ng-disabled="!processInstance.firstTask"></select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label required">项目简述</label>
                                <div class="col-md-10">
                                    <textarea rows="3" class="form-control ng-pristine ng-untouched ng-valid ng-empty ng-valid-maxlength ng-valid-required"
                                              ng-model="vm.item.projectDesc" name="projectDesc" ng-disabled="!processInstance.firstTask"
                                              required="true"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">备注</label>
                                <div class="col-md-10">
                                    <input type="text" class="form-control" ng-model="vm.item.remark" name="remark" ng-disabled="!processInstance.firstTask"/>
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
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
                    <jsp:include page="../common/common-actHistory.jsp"/>
                </div>
                <div class="tab-pane" id="tab_detail_3"
                     style="height: 380px;overflow-y: auto;overflow-x: hidden;">
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
            <i class="fa fa-file"></i>档案资料
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
                    <th>文件名称</th>
                    <th>存放地址</th>
                    <th  style="width:60px ">文件类型</th>
                    <th  style="width:40px ">电子档案</th>
                    <th style="width:60px ">创建人</th>
                    <th style="width:90px ">创建时间</th>
                    <th style="width: 55px;">操作</th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="detail in vm.list">
                    <td class="dt-right" ng-bind="$index+1"></td>
                    <td ng-bind="detail.fileName" ng-click="vm.editDetail(detail.id);" style="color: blue;"></td>
                    <td ng-bind="detail.realAddress"></td>
                    <td ng-bind="detail.fileType"></td>
                    <td>
                        <span ng-show="detail.online">是</span>
                        <span ng-show="!detail.online">否</span>
                    </td>

                    <td ng-bind="detail.creatorName"></td>
                    <td ng-bind="detail.gmtModified|date:'yyyy-MM-dd'"></td>
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
                <h4 class="modal-title margin-right-10">档案资料详情</h4>

            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form" style="padding-right: 30px;" id="PointDetail_form">
                        <div class="form-group">
                            <label class="col-md-3 control-label required">文件名称</label>
                            <div class="col-md-9">
                                <div class="input-group">
                                    <input type="text" class="form-control" ng-model="vm.archiveDetail.fileName"
                                           required="true"
                                           maxlength="20" name="fileName" placeholder="" ng-disabled="!processInstance.firstTask">
                                    <span class="input-group-addon" style="padding: 0;border: none;">
                                        <span id="btnUpload" class="btn btn-sm blue fileinput-button" ng-disabled="!processInstance.firstTask">
                                        <i class="fa fa-cloud-upload"></i>
                                        <span>上传</span>
                                        <span id="uploadProgress"></span>
                                        <input type="file" name="multipartFile" id="uploadModelFile"
                                               accept="*"
                                               multiple="multiple"/>
                                         </span>
                                    </span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">文件类型</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('档案资料文件类型')}:true"
                                        ng-model="vm.archiveDetail.fileType" class="form-control"
                                        ng-disabled="!processInstance.firstTask"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">存放地址</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.archiveDetail.realAddress"  required="true"
                                      name="realAddress" placeholder="" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label required">电子档案</label>
                            <div class="col-md-9">
                                <select ng-options="s.codeValue as s.name for s in sysCodes | filter:{codeCatalog:('是否发布')}:true"
                                        ng-model="vm.archiveDetail.online" class="form-control" ng-disabled="!processInstance.firstTask"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">排序</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.archiveDetail.seq"  required="true"
                                        name="seq" placeholder="" ng-disabled="!processInstance.firstTask">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label">备注</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control" ng-model="vm.archiveDetail.remark"
                                      name="remark" placeholder="" ng-disabled="!processInstance.firstTask">
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


<div class="modal fade" id="stepModel" tabindex="-1" role="basic" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                <h4 class="modal-title" >项目详情</h4>
            </div>
            <div class="modal-body">
                <div class="dataTables_wrapper no-footer">

                    <div class="table-scrollable" style="max-height: 400px;overflow-y: auto;">
                        <table class="table table-striped table-hover table-bordered table-advance no-footer">
                            <thead>
                            <tr>
                                <th style="width: 35px;">#</th>
                                <th>项目名称</th>
                                <th>设计阶段</th>
                                <th>分期名称</th>
                                <th>合同号</th>
                                <th style="width: 50px;">创建人</th>
                                <th style="width: 100px;">创建时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr ng-repeat="item in vm.listStep">
                                <td>
                                    <input type="checkbox" class="cb_step" ng-checked="vm.item.projectName.indexOf(item.projectName)>-1"  data-name="{{item}}" style="width: 20px;height: 20px;" />
                                </td>
                                <td ng-bind="item.projectName"></td>
                                <td ng-bind="item.stageName"></td>
                                <td ng-bind="item.stepName"></td>
                                <td ng-bind="item.contractNo"></td>
                                <td ng-bind="item.creatorName"></td>
                                <td ng-bind="item.gmtCreate | date:'yyyy-MM-dd'"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn blue" ng-click="vm.saveStep();">确认</button>
                </div>
            </div>
        </div>
    </div>
</div>


<%--<jsp:include page="../common/common-edFile.jsp"/>--%>
<jsp:include page="../common/common-actFlow.jsp"/>



